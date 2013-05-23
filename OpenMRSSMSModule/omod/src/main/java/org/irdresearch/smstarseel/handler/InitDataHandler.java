package org.irdresearch.smstarseel.handler;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.irdresearch.smstarseel.SmsTarseelUtil;
/*import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;*/
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.Project;
import org.openmrs.module.OpenMRSSMSModule.api.deviceService;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ProjectParams;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseCode;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InitDataHandler {

	public static void getProjectList(JSONObject request, HttpServletResponse resp) throws JSONException, IOException 
	{
		//TarseelServices sc = TarseelContext.getServices();
		deviceService sc=Context.getService(deviceService.class);
		JSONArray prjList = new JSONArray();

		try
		{
			List<Project> list = sc.getAllProjects(0, 100);
			
			for (Project prj : list)
			{
				JSONObject prjMap = new JSONObject();
				prjMap.put(ProjectParams.NAME.KEY(), prj.getName());
				prjMap.put(ProjectParams.PID.KEY(), Integer.toString(prj.getProjectId()));
				
				prjList.put(prjMap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR,e.getMessage()).jsonToString());
		}
		finally{
			//sc.closeSession();
		}
		
		
		SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
		json.addObjectList(ProjectParams.LIST_ID.KEY(),prjList);

		SmsTarseelUtil.sendResponse(resp, json.jsonToString());
	}
}
