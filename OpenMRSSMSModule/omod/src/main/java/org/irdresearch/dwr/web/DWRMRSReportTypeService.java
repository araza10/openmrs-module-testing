package org.irdresearch.dwr.web;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.module.OpenMRSSMSModule.api.SmsModuleService;
import org.springframework.beans.support.PagedListHolder;
public class DWRMRSReportTypeService {

	
	public List<OutboundMessage> getMessageByName(String patientID)
	{
		
		List<OutboundMessage> patientList = null;
		try
		{
		
		
		patientList = new  ArrayList<OutboundMessage>();
		
		SmsModuleService smsService = Context.getService(SmsModuleService.class);
		patientList = smsService.findByPatientID(patientID);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return patientList;
	}

}
