/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.OpenMRSSMSModule.api;

import java.util.Date;
import java.util.List;

//import org.irdresearch.smstarseel.data.DataException;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundStatus;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundType;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.PeriodType;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.Priority;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service exposes module's core functionality. It is a Spring managed bean which is configured in moduleApplicationContext.xml.
 * <p>
 * It can be accessed only via Context:<br>
 * <code>
 * Context.getService(smsmoduleService.class).someMethod();
 * </code>
 * 
 * @see org.openmrs.api.context.Context
 */
@Transactional
public interface smsmoduleService extends OpenmrsService {
     
	Number LAST_QUERY_TOTAL_ROW__COUNT();
String createNewOutboundSms(String recipient, String text, Date duedate, Priority priority, int validityPeriod, String patientid, PeriodType periodType, Integer projectId, String additionalDescription);
	
	List<String> getReferenceNumberByPatientID(String patientID);
	
	List<OutboundMessage> findByPatientID(String patientID);
	
	OutboundMessage findOutboundById(long id);
	
	void updateOutbound(OutboundMessage Outbound);
	
	List<OutboundMessage> findPendingOutboundTillNow(String projectName, boolean orderByPriority, int fetchsize) throws org.openmrs.module.OpenMRSSMSModule.DataException;
	
	List<InboundMessage> getAllInboundRecord(int firstResult, int fetchsize);

	InboundMessage findInboundById(long id);

	InboundMessage findInboundMessageByReferenceNumber(String referenceNumber, boolean readonly);

	List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater
			, InboundStatus smsStatus , String recipient,  String originator
			, String imei,  Integer projectId,
			boolean putNotWithSmsStatus)
			throws DataException;
	
	List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater
			,InboundStatus smsStatus , String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize)
			throws DataException;

	int markInboundAsRead(String referenceNumber);
	
	int markInboundAsRead(long inboundId);
	
	void updateInbound(InboundMessage Inbound);

	void saveInbound(InboundMessage Inbound);




}