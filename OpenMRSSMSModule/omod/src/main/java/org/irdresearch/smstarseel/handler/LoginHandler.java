package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.derby.tools.sysinfo;
import org.h2.java.lang.System;

import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.openmrs.module.OpenMRSSMSModule.api.DeviceService;
import org.openmrs.module.OpenMRSSMSModule.api.impl.ActiveDevice;
/*import org.irdresearch.smstarseel.context.LoggedInUser;
import org.irdresearch.smstarseel.context.SystemPermissions;
import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;*/
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.Project;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.DeviceRegisterParam;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.LoginRequest;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.LoginResponse;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.RequestMendatoryParam;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseCode;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelGlobal;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelResponse;
//import org.irdresearch.smstarseel.service.UserServiceException;
import org.jfree.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import org.irdresearch.smstarseel.DateUtils;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.api.impl.DeviceServiceImpl;

public class LoginHandler {

	public LoginHandler() {

	}
	
	public static void handleDeviceLogin(JSONObject request, HttpServletResponse resp) throws JSONException, IOException {
		String username = (String) request.get(LoginRequest.USERNAME.KEY());
		String password = (String) request.get(LoginRequest.PASSWORD.KEY());
		String imei 	= (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String date 	= (String) request.get(RequestMendatoryParam.DATE.KEY());
		//import org.ird.immunizationreminder.utils.date.DateUtils;
		try
		{
			Date d = new SimpleDateFormat(SmsTarseelGlobal.DEFAULT_DATE_FORMAT).parse(date);
			Log.debug(d);
			
			if(!DateUtils.datesEqualUptoHour(d, new Date())){
				SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_PHONE_DATE,"").jsonToString());
			}
		}
		catch (Throwable th)
		{
			Log.error(th);
			System.out.print(th.getMessage());
		}
//		catch (ParseException e2)
//		{
//			e2.printStackTrace();
//			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_PHONE_DATE_ERROR,e2.getMessage()).jsonToString());
//		}

		try
		{
			String message = "";
			
			//LoggedInUser user = TarseelContext.getAuthenticatedUser(username, password) ;
			//	Context.openSession();
			Context.authenticate(username, password);
			//String uswname=Context.getAuthenticatedUser().getUsername();
			/*if(Context.isAuthenticated())
			{
				System.out.println("Authenticated!!!");
			}*/
			
			/*if(!user.hasPermission(SystemPermissions.DEVICE_OPEARTIONS)){
				throw new UserServiceException(UserServiceException.PERMISSION_UNGRANTED, UserServiceException.PERMISSION_UNGRANTED);
			}*/
			
			if(!Context.isAuthenticated()){
				//throw new UserServiceException(UserServiceException.PERMISSION_UNGRANTED, UserServiceException.PERMISSION_UNGRANTED);
			}
			message += "\nUser : " + username;
			message += "\nImei : " + imei;

			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addElement(LoginResponse.DETAILS.KEY(), message);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		/*catch (UserServiceException e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.INVALID_USER,e.getMessage()).jsonToString());
		}*/
		
		catch (Exception e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
		catch (Throwable th)
		{
			Log.error(th);
			System.out.print(th.getMessage());
		}
		finally{
			Context.closeSession();
		}
	}
	
	public static void registerDevice(JSONObject request, HttpServletResponse resp) throws JSONException, IOException 
	{
		String imei = (String) request.get(RequestMendatoryParam.IMEI.KEY());
		String sim = (String) request.get(RequestMendatoryParam.SIM.KEY());
		String project = (String) request.get(DeviceRegisterParam.PROJECT_NAME.KEY());
		
		Project prj = null;
	//	TarseelServices sc = TarseelContext.getServices();
		DeviceService ds =Context.getService(DeviceService.class);
		try{
			String message = "";
			
			for (Device dd : ds.findDevice(imei, DeviceStatus.ACTIVE, false, null, null)) {
				dd.setStatus(DeviceStatus.DISCARDED);
				
				ds.updateDevice(dd);
			}
			
			//sc.commitTransaction();
			
			prj = ds.findProject(project).get(0);
			
			Device d = new Device();
			d.setDateAdded(new Date());
			d.setDeviceName(imei);
			d.setImei(imei);
			d.setProject(prj);
			d.setSim(sim);
			d.setStatus(DeviceStatus.ACTIVE);
			
			ds.saveDevice(d);
			
			//sc.commitTransaction();
			
			message += "\nImei    : " + imei;
			message += "\nSim     : " + sim;
			message += "\nProject : " + project;
			
			//TarseelContext.ACTIVE_DEVICES.put(imei, new ActiveDevice(imei));
			DeviceServiceImpl.ACTIVE_DEVICES.put(imei, new ActiveDevice(imei));
			SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
			json.addElement("details", message);
			
			SmsTarseelUtil.sendResponse(resp, json.jsonToString());
		}
		catch (DataException e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
		finally{
			//sc.closeSession();
		}
	}
}
