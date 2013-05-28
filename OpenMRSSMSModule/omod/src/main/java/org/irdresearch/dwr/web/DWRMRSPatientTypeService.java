package org.irdresearch.dwr.web;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.PatientIdentifier;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.Reminder;
import org.openmrs.module.OpenMRSSMSModule.api.ReminderService;

public class DWRMRSPatientTypeService {
	
	public List<PatientText> getPatientType(String patientType)
	{
		String patientText = null;
		List<PatientText> patientgrList = null;
		Reminder rm =null;
		
		try
		{
			
			
			patientgrList = new  ArrayList<PatientText>();
			ReminderService rs = Context.getService(ReminderService.class);
			if(patientType.equalsIgnoreCase("tb")){
			rm = rs.getReminder("SMS_TB");
			patientText = rm.getText();}
			else if(patientType.equalsIgnoreCase("hiv"))
			{
			rm = rs.getReminder("SMS_HIV");
			patientText = rm.getText();
			}
			else if(patientType.equalsIgnoreCase("Poliomyelitis"))
			{
				rm = rs.getReminder("SMS_POLIO");
				patientText = rm.getText();
			}
			else
			{
				patientText = "";
			}
			PatientText pt = new PatientText();
			pt.setText(patientText);
			patientgrList.add(pt);
			
			
		}
		catch (Exception e) {
			
		}
		return patientgrList;
	}
	
	public boolean patIdentifierSearch(String pid)
	{
		boolean b = false;
		PatientService ps = Context.getPatientService();
		List<PatientIdentifier> piD =   ps.getPatientIdentifiers(pid, null, null, null, true);
		  
		if(piD.size()>0)
		{
			b = true;
			
		}
		
		return b;
	}

}
