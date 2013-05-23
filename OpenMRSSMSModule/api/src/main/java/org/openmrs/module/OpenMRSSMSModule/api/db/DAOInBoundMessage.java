package org.openmrs.module.OpenMRSSMSModule.api.db;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundStatus;

public interface DAOInBoundMessage {

	public Serializable save(Object objectinstance);
	public void update(Object objectinstance);
	InboundMessage findById(long id);

	InboundMessage findByReferenceNumber(String referenceNumber, boolean readonly);
	
	Number countCriteriaRows(Date recieveDatesmaller, Date recieveDategreater,
			InboundStatus smsStatus , String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus) throws DataException;

	Number countAllRows();

	Number LAST_QUERY_TOTAL_ROW__COUNT();

	List<InboundMessage> findByCriteria(Date recieveDatesmaller, Date recieveDategreater
			, InboundStatus smsStatus , String recipient,  String originator
			, String imei,  Integer projectId,
			boolean putNotWithSmsStatus)
			throws DataException;
	
	List<InboundMessage> findByCriteria(Date recieveDatesmaller, Date recieveDategreater
			,InboundStatus smsStatus , String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize)
			throws DataException;

	List<InboundMessage> getAll(int firstResult, int fetchsize);
	
	int markInboundAsRead(String referenceNumber);
	
	int markInboundAsRead(long inboundId);
}
