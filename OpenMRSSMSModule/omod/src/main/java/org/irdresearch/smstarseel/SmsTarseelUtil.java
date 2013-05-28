package org.irdresearch.smstarseel;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

/*import org.irdresearch.smstarseel.context.TarseelContext;
import org.irdresearch.smstarseel.context.TarseelServices;*/
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.api.DeviceService;
import org.irdresearch.smstarseel.DateUtils;
import org.irdresearch.smstarseel.DateUtils.TIME_INTERVAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsTarseelUtil {
	public static final Logger DBLOGGER = LoggerFactory.getLogger("dbAppender");
	public static final Logger FILELOGGER = LoggerFactory.getLogger("fileAppender");
	public static final Logger PHONECOMMLOGGER = LoggerFactory.getLogger("phoneCommfileAppender");
	public static final Logger PHONELOGLOGGER = LoggerFactory.getLogger("phonefileAppender");
	
	private static Date lastDeviceErrorEmailDate = new Date(120000000000L);

	public static boolean sendResponse(HttpServletResponse response , String responseToSend) throws IOException 
	{
		SmsTarseelUtil.PHONECOMMLOGGER.info("response:"+responseToSend);
		response.setCharacterEncoding("UTF-8");
		PrintWriter wrtr = response.getWriter();
		wrtr.println(responseToSend);
		return !wrtr.checkError(); // returns true if there is any error false otherwise, but we need to send true if there is no error
		/*ServletOutputStream out = response.getOutputStream();
		out.println(responseToSend);
		out.close();*/
	}
	
	public static Device verifyDeviceProject(String imei, String sim, String projectName){
		Device prjRegDev = null;
		StringBuffer errormsg = new StringBuffer("Error while finding device.\n");
		//TarseelServices sc = TarseelContext.getServices();
		DeviceService dsc=Context.getService(DeviceService.class);
		try{
			//prjRegDev = sc.getDeviceService().findDevice(imei, DeviceStatus.ACTIVE, false, projectName, sim).get(0);
			prjRegDev = dsc.findDevice(imei, DeviceStatus.ACTIVE, false, projectName, sim).get(0);
		}catch (Exception e) {
			e.printStackTrace();
			errormsg.append("Trace is:"+e.getMessage()+".\n");
		}
		finally{
			//sc.closeSession();
		}
		
		if(prjRegDev == null){
			errormsg.append("No Device found for imei:"+imei+", sim:"+sim+", project:"+projectName);
			if(DateUtils.differenceBetweenIntervals(new Date(), lastDeviceErrorEmailDate, TIME_INTERVAL.HOUR) > 1){
				//EmailEngine.getInstance().emailErrorReportToAdmin("SmsTarseel: Error device not verified", errormsg.toString());
				lastDeviceErrorEmailDate = new Date();
			}
		}
		return prjRegDev;
	}
}
