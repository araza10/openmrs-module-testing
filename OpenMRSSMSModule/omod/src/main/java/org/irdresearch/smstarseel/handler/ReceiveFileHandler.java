package org.irdresearch.smstarseel.handler;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.irdresearch.smstarseel.SmsTarseelUtil;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.LoginResponse;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseCode;
import org.openmrs.module.OpenMRSSMSModule.api.impl.RequestParam.ResponseMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.SmsTarseelResponse;
import org.json.JSONException;
import org.json.JSONObject;

public class ReceiveFileHandler {

	public ReceiveFileHandler() {

	}
	
	public static void receiveLogFileData(JSONObject jReq, HttpServletResponse resp, FileItem file) throws IOException, JSONException {
		try
		{
			SmsTarseelUtil.PHONELOGLOGGER.error("--------------------******************************--------------------");
			SmsTarseelUtil.PHONELOGLOGGER.error(jReq.toString());
			byte[] b = new byte[(int) file.getSize()];
			file.getInputStream().read(b);
			SmsTarseelUtil.PHONELOGLOGGER.error(new String(b));
			SmsTarseelUtil.PHONELOGLOGGER.error("--------------------***************END OF LOG FILE***************--------------------");
		}
		catch (IOException e2)
		{
			e2.printStackTrace();
			SmsTarseelUtil.sendResponse(resp, new SmsTarseelResponse(ResponseCode.ERROR, ResponseMessage.UNKNOWN_ERROR, e2.getMessage()).jsonToString());
		}
		
		SmsTarseelResponse json = new SmsTarseelResponse(ResponseCode.SUCCESS, ResponseMessage.SUCCESS,"");
		json.addElement(LoginResponse.DETAILS.KEY(), "Log saved successfully on server");
		
		SmsTarseelUtil.sendResponse(resp, json.jsonToString());
	}
}
