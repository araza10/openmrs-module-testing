package org.irdresearch.dwr.web;

public class PatientGridRow {

	private String patientId;
	private String gender;
	private String givenName;
	private String mobileNumber;
	private String error;


	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public PatientGridRow() {
	}

}
