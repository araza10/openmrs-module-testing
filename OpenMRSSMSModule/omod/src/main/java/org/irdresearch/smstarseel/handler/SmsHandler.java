package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.irdresearch.smstarseel.DateUtils;
import org.irdresearch.smstarseel.DateUtils.TIME_INTERVAL;
//import org.ird.immunizationreminder.utils.reporting.ExceptionUtil;
//import org.irdresearch.smstarseel.EmailEngine;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.irdresearch.smstarseel.TarseelWebGlobals;
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.api.GeneralService;
import org.openmrs.module.OpenMRSSMSModule.api.smsmoduleService;
import org.openmrs.module.OpenMRSSMSModule.api.impl.ActiveDevice;
/*import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelGlobals;
import org.irdresearch.smstarseel.context.TarseelServices;*/
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundStatus;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundType;
//import org.openmrs.module.InboundMessage;
//import org.research.smstarseel.data.InboundMessage.InboundStatus;
//import org.irdresearch.smstarseel.data.InboundMessage.InboundType;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundStatus;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.InboundSmsParams;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.OuboundSmsParams;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.RequestMendatoryParam;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseCode;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelGlobal;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelResponse;
import org.openmrs.module.OpenMRSSMSModule.api.impl.TarseelGlobals;
import org.springframework.context.ApplicationContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsHandler 
{
private static Date lastSmsCollectedErrorEmailDate = new Date(120000000000L);
private static Date lastSmsFetchedErrorEmailDate = new Date(120000000000L);

public static synchronized void revertUnknownSmses(String response){
	//TarseelServices sc = TarseelContext.getServices();
	smsmoduleService smsc=Context.getService(smsmoduleService.class);
	try{
		JSONObject json = new JSONObject(response);
		final JSONArray smslist = (JSONArray)json.get(OuboundSmsParams.LIST_ID.KEY());
		for (int i = 0; i < smslist.length(); i++)
		{
			final JSONObject sms = smslist.getJSONObject(i);
			OutboundMessage ob = smsc.findOutboundById(sms.getLong(OuboundSmsParams.SMSID.KEY()));
			
			if(ob.getErrormessage() == null
					|| (ob.getErrormessage().length() - ob.getErrormessage().toLowerCase().replace("reverted", "reverte").length()) <= TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES ){
				ob.setStatus(OutboundStatus.PENDING);
				if(ob.getDescription() == null || ob.getDescription().length() < 230){
					ob.setDescription((ob.getDescription()==null?"":ob.getDescription())+";"+"Reverted");
				}
				
				ob.setErrormessage((ob.getErrormessage()==null?"":ob.getErrormessage())+"Reverted;");

				try{
				//	EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("SMS REVERTED "+ob.getOutboundId(), "SMS REVERTED "+ob.getOutboundId());
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			else{
				try{
					//EmailEngine.getInstance().emailErrorReportToAdminAsASeparateThread("SMS NOT REVERTED "+ob.getOutboundId(), "SMS NOT REVERTED "+ob.getOutboundId()+" . Tries found exceeded than limit "+TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			smsc.updateOutbound(ob);
			
		}
		
		//sc.commitTransaction();
	}
	catch (Exception e) {
		e.printStackTrace();
	}
	finally{
		//sc.closeSession();
	}
}
	public static synchronized void getPendingSmsTillNow(JSONObject request, HttpServletResponse resp) throws IOException, JSONException
	{
		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim 	= (String) request.get(RequestMendatoryParam.SIM.KEY());
		String projectName 	= (String) request.get(RequestMendatoryParam.PROJECT_REGISTERED.KEY());

		Device prjRegDev = SmsTarseelUtil.verifyDeviceProject(imei, sim, projectName);
		
		if(prjRegDev == null){
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,"Error while finding device registered").jsonToString());
		}
		
		////check if any recipient have been scheduled to receive more than 3 exactly similar smses for the day
		//Session tempsc = TarseelServices.getNewSession();
		
				
		try{
			//Transaction t = tempsc.beginTransaction();
		GeneralService gs	=	Context.getService(GeneralService.class);
			try{
				while(true){
/*					List spamidl = session(List).createSQLQuery("select o.outboundid, o.recipient, A.count Acount,B.count Bcount from outboundmessage as o " +
							" left join (select text,recipient, count(*) as count from outboundmessage " +
							"	where date(sentdate) = curdate() and status in ('sent','unknown') " +
							"	group by recipient) as A on A.recipient = o.recipient and A.text=o.text " +
							" left join (select recipient, count(*) as count from outboundmessage " +
							"	where date(sentdate) = curdate() and status in ('sent','unknown') " +
							"	group by recipient) as B on B.recipient = o.recipient " +
							" where o.status = 'pending' " +
							" having Acount > "+TarseelWebGlobals.MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY+" " +
									" OR Bcount > "+TarseelWebGlobals.MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY+" " +
							" order by recipient").setFirstResult(0).setMaxResults(100).list();*/
					
					List spamidl = gs.slqQuery();
					if(spamidl.size()>0){
						String updatesql = "update outboundmessage set " +
								" systemProcessingStartDate = now(), " +
								" status = 'MISSED', " +
								" description = concat(description, failureCause, errorMessage), " +
								" failureCause = concat(IFNULL(failureCause,''),'SPAM;'), " +
								" errorMessage = concat(IFNULL(errorMessage,''),'SPAM DETECTED ', now(), ';'), " +
								" systemProcessingEndDate = now() " +
								" where outboundId in ( ";
						for (int i = 0; i < spamidl.size(); i++) {
							updatesql += "?,";
						}
						if(updatesql.endsWith(",")){
							updatesql=updatesql.substring(0,updatesql.lastIndexOf(","));
						}
						updatesql += ")";
						//SQLQuery updateqry = sessionFactory.openSession().createSQLQuery(updatesql);
						SQLQuery updateqry = gs.update(updatesql);
						for (int i = 0; i < spamidl.size(); i++) {
							Object[] o = (Object[]) spamidl.get(i);
							updateqry.setLong(i, Long.parseLong(o[0].toString()));
						}
						
						updateqry.executeUpdate();
					}
					else{
						break;
					}
				}
				//t.commit();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		finally{
			try{
				//tempsc.close();
			}
			catch (Exception e) {
				System.out.println("CLOSING MANUALLY RETRIEVED SESSION THREW EXCEPTION");
				e.printStackTrace();
			}
		}
		
		try{
		//	Transaction t = tempscmis.beginTransaction();
			GeneralService gs = Context.getService(GeneralService.class);
			String missedsmsSql = "update outboundmessage " + 
					" set status = 'MISSED' " +
					" , failureCause = concat(IFNULL(failureCause,''),'VALIDITY PERIOD PASSED;') " +
					" , errormessage = concat(IFNULL(errormessage,''),'VALIDITY PERIOD PASSED ', now(), ';') " +
					" where status='PENDING' " +
					" and TIME_TO_SEC(TIMEDIFF(now(),(case when periodType='DAY' then DATE_ADD(duedate,INTERVAL validityPeriod DAY) " +
					" when periodtype ='HOUR' then DATE_ADD(duedate,INTERVAL validityPeriod HOUR) " +
					" when periodtype ='WEEK' then DATE_ADD(duedate,INTERVAL (validityPeriod*7) DAY) " +
					" else DATE_ADD(duedate,INTERVAL 1 SECOND) END))) > 60 ";
			//SQLQuery missedquery = tempscmis.createSQLQuery(missedsmsSql);
			SQLQuery missedquery = gs.update(missedsmsSql);
			missedquery.executeUpdate();
			//t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				//tempscmis.close();
			}
			catch (Exception e) {
				System.out.println("CLOSING MANUALLY RETRIEVED SESSION THREW EXCEPTION");
				e.printStackTrace();
			}
		}
		
		//Session tempscunkwn = TarseelServices.getNewSession();
		
		
		try{
			
			GeneralService gs = Context.getService(GeneralService.class);
			String missedsmsSql = "update outboundmessage " +
					" set status = 'PENDING' " +
					" , failureCause = concat(IFNULL(failureCause,''),'LOST_RETRY;') " +
					" , errormessage = concat(IFNULL(errormessage,''),'LOST_RETRY ', now(), ';') " +
					" where status = 'UNKNOWN' " +
					" and duedate > DATE_SUB(curdate(),INTERVAL 7 DAY) " +
					" and (errorMessage IS NULL OR ( length(errorMessage)-length(replace(errorMessage,'LOST_RETRY','LOST_RETR')) ) < "+TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES+" ) " +
					" and (failureCause IS NULL OR ( length(failureCause)-length(replace(failureCause,'LOST_RETRY','LOST_RETR')) ) < "+TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES+" ) " +
					" and DATE_ADD(systemProcessingStartDate,INTERVAL 1 HOUR) < now() " +
					" and tries <= " + TarseelWebGlobals.MAX_OUTBOUND_REVERT_TRIES;
			SQLQuery unknownquery = gs.update(missedsmsSql);
			unknownquery.executeUpdate();
			//t.commit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
			//	tempscunkwn.close();
			}
			catch (Exception e) {
				System.out.println("CLOSING MANUALLY RETRIEVED SESSION THREW EXCEPTION");
				e.printStackTrace();
			}
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
		//TarseelServices sc = TarseelContext.getServices();
		smsmoduleService sc= Context.getService(smsmoduleService.class);
		try
		{
			JSONArray smsList = new JSONArray();

			List<OutboundMessage> oml = sc.findPendingOutboundTillNow(projectName.trim(), true, TarseelWebGlobals.MAX_OUTBOUND_FETCH_PER_GO);
			for (OutboundMessage om : oml) {
				om.setSystemProcessingStartDate(new Date());

				TIME_INTERVAL interv = null;
				switch (om.getPeriodType()) {
				case DAY:
					interv = TIME_INTERVAL.DAY;
					break;
				case HOUR:
					interv = TIME_INTERVAL.HOUR;
					break;

				case WEEK:
					interv = TIME_INTERVAL.WEEK;
					break;
					
				default:
					break;
				}
				Date validDate = DateUtils.addInterval(om.getDueDate(), om.getValidityPeriod(), interv);
				if(validDate.before(new Date()))
				{
					om.setStatus(OutboundStatus.MISSED);
					om.setFailureCause((om.getFailureCause() == null ? "" : om.getFailureCause().trim())+"VALIDITY PERIOD PASSED;");
					om.setErrormessage((om.getErrormessage() == null ? "" : om.getErrormessage().trim())+"VALIDITY PERIOD PASSED;");
					om.setSystemProcessingEndDate(new Date());
				}
				else
				{
					om.setStatus(OutboundStatus.UNKNOWN);
					om.setImei(imei);
					om.setTries((om.getTries()==null?0:om.getTries())+1);

					JSONObject smsMap = new JSONObject();
					smsMap.put(OuboundSmsParams.CELL.KEY(), om.getRecipient());
					smsMap.put(OuboundSmsParams.SMSID.KEY(), Long.toString(om.getOutboundId()));
					smsMap.put(OuboundSmsParams.TEXT.KEY(), om.getText());
					smsMap.put(OuboundSmsParams.IS_SENT.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.SIM.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.SENT_DATE.KEY(), JSONObject.NULL);
					smsMap.put(OuboundSmsParams.DESC.KEY(), om.getDescription());
					smsMap.put(OuboundSmsParams.ERR_MSG.KEY(), om.getErrormessage());
					smsMap.put(OuboundSmsParams.FAIL_CAUSE.KEY(), om.getFailureCause());

					smsList.put(smsMap);
				}
				
				sc.updateOutbound(om);
				
				//sc.commitTransaction();
				
			}
			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addObjectList(OuboundSmsParams.LIST_ID.KEY(),smsList);
			
			try{
				if(!SmsTarseelUtil.sendResponse(resp, json.jsonToString())){
					throw new IOException("Sending response returned FALSE");
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				//SmsTarseelUtil.DBLOGGER.error("REVERTING SMSES STATUS"+ExceptionUtil.getStackTrace(e));
				System.out.println("REVERTING SMSES STATUS");
				if(smsList.length() > 0) SmsHandler.revertUnknownSmses(json.jsonToString());
			}
		}
		catch (DataException e)
		{
			e.printStackTrace();
			if(DateUtils.differenceBetweenIntervals(new Date(), lastSmsFetchedErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
				//EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error fetching sms", "SmsTarseel: Error fetching sms:"+e.getMessage());
				lastSmsFetchedErrorEmailDate = new Date();
			}
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		} 
		finally{
			//sc.closeSession();
		}
	}
	public static synchronized void submitSmsSendAttemptResult(JSONObject request, HttpServletResponse resp) throws IOException, JSONException
	{
		//TarseelServices sc = TarseelContext.getServices();
		smsmoduleService sc = Context.getService(smsmoduleService.class);
		JSONArray list = request.getJSONArray(OuboundSmsParams.LIST_ID.KEY());
		
		try{
			for (int i = 0; i < list.length(); i++)
			{
				JSONObject sms = new JSONObject();
				try{
					sms = list.optJSONObject(i);
					OutboundMessage ob = sc.findOutboundById(sms.getLong(OuboundSmsParams.SMSID.KEY()));
					if(ob == null){//ideally will never happen
						String respsms = "SMS: "+sms.toString();
						SmsTarseelUtil.DBLOGGER.error("SMS LOST FROM DB\n"+respsms);
					}
					else{
						try{
						ob.setErrormessage((ob.getErrormessage() == null ? "" : ob.getErrormessage().trim())+sms.getString(OuboundSmsParams.ERR_MSG.KEY())+";");
						}catch (Exception e) {
						}
						try{
						ob.setFailureCause((ob.getFailureCause() == null ? "" : ob.getFailureCause().trim()+"\n")+sms.getString(OuboundSmsParams.FAIL_CAUSE.KEY()));
						}catch (Exception e) {
						}
						ob.setOriginator(sms.getString(OuboundSmsParams.SIM.KEY()));
						try{
						ob.setSentdate(new SimpleDateFormat(SmsTarseelGlobal.DEFAULT_DATE_FORMAT).parse(sms.getString(OuboundSmsParams.SENT_DATE.KEY())));
						}catch(Exception e){
							e.printStackTrace();
						}
						ob.setStatus(sms.getBoolean(OuboundSmsParams.IS_SENT.KEY())?OutboundStatus.SENT:OutboundStatus.FAILED);
						ob.setSystemProcessingEndDate(new Date());
						
						sc.updateOutbound(ob);
					}
					
				//	sc.commitTransaction();
				}
				catch (Exception e) {
					e.printStackTrace();
					//EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error logging device`s sms sent", "SmsTarseel: Error logging device`s sms sent:"+e.getMessage()+",SMS:"+sms.toString());
					//SmsTarseelUtil.DBLOGGER.error(ExceptionUtil.getStackTrace(e));
				}
			}

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		finally{
			//sc.closeSession();
		}
	}
	public static synchronized void submitRecivedSms(JSONObject request, HttpServletResponse resp) throws JSONException, IOException  {

		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim 	= (String) request.get(RequestMendatoryParam.SIM.KEY());
		String projectName 	= (String) request.get(RequestMendatoryParam.PROJECT_REGISTERED.KEY());

		Device prjRegDev = SmsTarseelUtil.verifyDeviceProject(imei, sim, projectName);
		
		if(prjRegDev == null){
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,"Error while finding device registered").jsonToString());
		}
		
		//TarseelServices sc = TarseelContext.getServices();
		smsmoduleService sc = Context.getService(smsmoduleService.class);
		
		JSONArray list = request.getJSONArray(InboundSmsParams.LIST_ID.KEY());
		
		try{
			for (int i = 0; i < list.length(); i++)
			{
				JSONObject sms = new JSONObject();
				try{
					sms = list.optJSONObject(i);
					InboundMessage ib = new InboundMessage();
					ib.setImei(imei);
					ib.setOriginator((String) sms.get(InboundSmsParams.SENDER_NUM.KEY()));
					
					ib.setProjectId(prjRegDev.getProject().getProjectId());
					ib.setReferenceNumber(TarseelGlobals.getUniqueSmsId(prjRegDev.getProject().getProjectId()));
					
					Date recvDate = org.openmrs.module.OpenMRSSMSModule.api.impl.DateUtils.parseRequestDate((String) sms.get(InboundSmsParams.RECIEVED_DATE.KEY()));
					ib.setRecieveDate(recvDate);
					
					ib.setRecipient(sim);
					ib.setStatus(InboundStatus.UNREAD);//should be unread until the application (whome it is meant for) doesnot mark it read
					
					ib.setSystemProcessingStartDate(org.openmrs.module.OpenMRSSMSModule.api.impl.DateUtils.parseRequestDate((String) sms.get(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY())));
					
					//ib.setSystemRecieveDate(null);
					ib.setText((String) sms.get(InboundSmsParams.TEXT.KEY()));
					
					ib.setType(InboundType.SMS);
	
					ib.setSystemProcessingEndDate(new Date());
	
					sms.remove(InboundSmsParams.SYSTEM_PROCESS_START_DATE.KEY());
					sms.remove(InboundSmsParams.TEXT.KEY());
					sms.remove(InboundSmsParams.SENDER_NUM.KEY());
					
					//sc.getSmsService().saveInbound(ib);
					sc.saveInbound(ib);
					//sc.commitTransaction();
	
					///TODO never delete it : it is notify that inbound sms has been saved
					sms.put(InboundSmsParams.IS_SAVED.KEY(), true);
				}
				catch (Exception e) {
					e.printStackTrace();
/*					if(DateUtils.differenceBetweenIntervals(new Date(), lastSmsCollectedErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
						EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error logging device`s sms collected", "SmsTarseel: Error logging device`s sms collected:"+e.getMessage()+", EX:"+sms.toString());
						lastSmsCollectedErrorEmailDate = new Date();
					}*/
				}
			}
			
			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addObjectList(InboundSmsParams.LIST_ID.KEY(), list);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		finally{
			//sc.closeSession();
		}
	}
}
