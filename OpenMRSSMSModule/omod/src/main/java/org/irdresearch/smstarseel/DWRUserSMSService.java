/*package org.irdresearch.smstarseel;

import ird.xoutTB.context.ServiceContext;
import ird.xoutTB.context.UserContext.LoggedInUser;
import ird.xoutTB.db.entity.UserSMS;
import ird.xoutTB.db.entity.UserSMS.SMS_STATUS;
import ird.xoutTB.reporting.XoutLogger;
import ird.xoutTB.utils.date.DateUtils;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;
import org.irdresearch.smstarseel.data.OutboundMessage.PeriodType;
import org.irdresearch.smstarseel.data.OutboundMessage.Priority;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.directwebremoting.WebContextFactory;

public class DWRUserSMSService {
	private int validityPeriod=4;
	//private ServiceContext sc=new ServiceContext();
	Calendar cal=Calendar.getInstance();
	TarseelServices ts=TarseelContext.getServices();
	String projectUniID = null;
	public String sendMessages(String[] recipients,String message,int dueDate,String description){
		try{
			//LoggedInUser user=UserSessionUtils.getActiveUser(WebContextFactory.get().getHttpServletRequest());
			if(user==null){
				return null;
			}
			Date date = new Date();
			System.out.println(date+"  llll");
			

			
			  
			//System.out.println(datee.getYear()+"-"+datee.getMonth()+"-"+datee.getDate()+" "+datee.getHours()+":"+datee.getMinutes()+":"+datee.getSeconds());
			//cal.add(Calendar.HOUR, dueDate);
			//sc.openSession();
			
			for (String recipient : recipients) {System.out.println("All of them +++++++"+recipient+" "+message+" "+new Date()+Priority.MEDIUM+" "+validityPeriod+PeriodType.HOUR+""+1+""+null);
				projectUniID=ts.getSmsService().createNewOutboundSms(recipient, message, new Date(), Priority.MEDIUM, validityPeriod, PeriodType.HOUR, 1, null);
				UserSMS usms=new UserSMS();
				usms.setDescription(description);
				usms.setDueDate(new Date());
				usms.setText(message);
				usms.setSMSStatus(SMS_STATUS.PENDING);
				usms.setRecipient(recipient);
				//usms.setCreator(user.getUser());
				usms.setCreator(null);
				usms.setUniqueID(projectUniID);
				sc.getUserSMSService().saveUserSMS(usms);
							}
			//sc.commitTransaction();
			ts.commitTransaction();
		}catch (Exception e) {
			XoutLogger.getLogger().error(new Date()+":"+e);
			return "System unable to queue messages, error message :"+e.getMessage()+". Check log for details.";
		}
		finally
		{
			ts.closeSession();
			//sc.closeSession();
		}
		
		return "messages queued for sending successfully";
	}
}
*/