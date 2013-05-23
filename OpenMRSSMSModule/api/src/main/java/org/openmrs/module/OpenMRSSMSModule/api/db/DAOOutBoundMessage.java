package org.openmrs.module.OpenMRSSMSModule.api.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundStatus;

public interface DAOOutBoundMessage {

	public Serializable save(Object objectinstance);
	List<String> getPatientsByReference(String patientId);
	List<OutboundMessage> getOutBoundByPatientID(String patientId);
	OutboundMessage findById(long id);
	public void update(Object objectinstance);
	List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater, OutboundStatus smsStatus
			, String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, boolean orderByPriority, int firstResult, int fetchsize)
			throws DataException;
}
