package org.openmrs.module.OpenMRSSMSModule;

public class DataException extends Exception{
	
	public static final String INVALID_CRITERIA_VALUE_SPECIFIED="Invalid value specified for search criteria";

	//public static final String 

	public String ERROR_CODE;

		private String errorMessage;
		public DataException(String errorcode){
			this.errorMessage=errorcode.toString();
			this.ERROR_CODE=errorcode.toString();
		}
		public DataException(String errorcode,String message){
			this.errorMessage=message;
			this.ERROR_CODE=errorcode.toString();
		}
		public String getMessage(){
			return errorMessage+(super.getMessage()==null?"":("\n"+super.getMessage()));
		}
	}
