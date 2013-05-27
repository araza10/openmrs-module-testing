package org.irdresearch.dwr.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.web.dwr.DWRPatientService;

public class DWRMRSPatientService {
			 	
	
/*	 public List<String> getAllLocations() {
	     List<String> locationNames = new Vector<String>();
	     for (Location loc : Context.getAllLocations()) {
	           locationNames.add(loc.getName());
	     }
	     return locationNames;
	}*/
	 
	 
	public List<PatientGridRow> getSearchParameter(String patientName)
	 {
		List<PatientGridRow> patientgrList = null;
		List<Patient> patientList = null;
		
		
		 try{
			 System.out.println("Patient Name= "+patientName);
		//Context.openSession();
			 /*patientgrList = new  ArrayList<PatientGridRow>();
			 patientList = new  ArrayList<Patient>();*/
			 
			PatientService service	= Context.getPatientService();
			patientList=service.getPatients(patientName);
		
/*		String given_name = patient.getGivenName();
		System.out.println(given_name+" Name given here>...........!!!!!!!!!!!!");
		 ls = new ArrayList<String>();
		 ls.add(given_name);*/
		 }
		 catch (Exception e) {
				e.printStackTrace();
				}
			 for (Patient patient : patientList) {
				 	patientgrList = new  ArrayList<PatientGridRow>();
				 	PatientGridRow pg = new PatientGridRow();
				 	PatientIdentifier pidentifier = patient.getPatientIdentifier(); 
					PersonAttribute perattribute=patient.getAttribute("Mobile Number");
					if(perattribute==null)
					{
						pg.setError("Person Attribute 'Mobile Number' not registered for this patient!");
						patientgrList.add(pg);
						return patientgrList;
					}
					pg.setMobileNumber(perattribute.getValue());
					pg.setPatientId(pidentifier.getIdentifier());
					pg.setGender(patient.getGender());
					pg.setGivenName(patient.getGivenName());
					patientgrList.add(pg);
				}
		
		

		 return patientgrList;
		
	 }

}
