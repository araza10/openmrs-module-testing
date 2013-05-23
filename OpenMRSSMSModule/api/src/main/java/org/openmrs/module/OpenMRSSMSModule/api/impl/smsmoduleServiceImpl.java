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
package org.openmrs.module.OpenMRSSMSModule.api.impl;

import java.util.Date;
import java.util.List;

import  org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import  org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundStatus;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.api.impl.DatUtils;
import org.openmrs.module.OpenMRSSMSModule.api.impl.DatUtils.TIME_INTERVAL;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundStatus;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundType;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOInBoundMessage;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOOutBoundMessage;
import org.openmrs.module.OpenMRSSMSModule.api.impl.TarseelGlobals;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.PeriodType;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.Priority;
import org.openmrs.module.OpenMRSSMSModule.api.smsmoduleService;


/**
 * It is a default implementation of {@link smsmoduleService}.
 */
public class smsmoduleServiceImpl extends BaseOpenmrsService implements smsmoduleService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private DAOOutBoundMessage	daooutbound;
	private DAOInBoundMessage	daoinbound;
	private Number				LAST_QUERY_TOTAL_ROW__COUNT;
	
	
	private void setLASTS_ROWS_RETURNED_COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT) {
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}



	public DAOInBoundMessage getDaoinbound() {
		return daoinbound;
	}



	public void setDaoinbound(DAOInBoundMessage daoinbound) {
		this.daoinbound = daoinbound;
	}



	/**
     * @param dao the dao to set
     */
	public void setDaooutbound(DAOOutBoundMessage daooutbound) {
    	this.daooutbound = daooutbound;
    }
	
	public void saveInbound(InboundMessage Inbound)
	{
		daoinbound.save(Inbound);
	}


	@Override
	public int markInboundAsRead(String referenceNumber)
	{
		return daoinbound.markInboundAsRead(referenceNumber);
	}

	@Override
	public int markInboundAsRead(long inboundId)
	{
		return daoinbound.markInboundAsRead(inboundId);
	}

    
	public InboundMessage findInboundById(long id)
	{
		return daoinbound.findById(id);
	}
	
	public List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater,
			InboundStatus smsStatus, String recipient, String originator, String imei, Integer projectId,
			boolean putNotWithSmsStatus) throws DataException
	{
		return daoinbound.findByCriteria(recieveDatesmaller
				, recieveDategreater, smsStatus, recipient, originator, imei, projectId, putNotWithSmsStatus);
	}

	public List<InboundMessage> findInbound(Date recieveDatesmaller, Date recieveDategreater,
			InboundStatus smsStatus, String recipient, String originator, String imei, String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize) throws DataException
	{
		List<InboundMessage> list = daoinbound.findByCriteria(recieveDatesmaller
				, recieveDategreater, smsStatus, recipient, originator, imei, projectName, putNotWithSmsStatus, firstResult, fetchsize);
		setLASTS_ROWS_RETURNED_COUNT(daoinbound.LAST_QUERY_TOTAL_ROW__COUNT());
		return list;
	}


    public String createNewOutboundSms(String recipient, String text, Date duedate, Priority priority, int validityPeriod,String patientid, PeriodType periodType, Integer projectId, String additionalDescription) {
		OutboundMessage o = new OutboundMessage();
		o.setCreatedDate(new Date());
		o.setDescription(additionalDescription);
		o.setDueDate(duedate);
		o.setValidityPeriod(validityPeriod);
		o.setPatient_id(patientid);
		o.setPeriodType(periodType);
		o.setPriority(priority);
		o.setProjectId(projectId);
		o.setRecipient(recipient);
		o.setStatus(OutboundStatus.PENDING);
		o.setText(text);
		o.setType(OutboundType.SMS);
		
		o.setReferenceNumber(TarseelGlobals.getUniqueSmsId(projectId));
		daooutbound.save(o);
		
		return o.getReferenceNumber();
	}
    
    public List<OutboundMessage> findPendingOutboundTillNow(String projectName, boolean orderByPriority, int fetchsize)
			throws DataException
	{
		return daooutbound.findByCriteria(DatUtils.subtractInterval(new Date(), 2, TIME_INTERVAL.YEAR),
				new Date(), null, null, OutboundStatus.PENDING, null, null, null, projectName
				, false, orderByPriority,0,fetchsize);
	}
    
    public OutboundMessage findOutboundById(long id)
	{
		return daooutbound.findById(id);
	}
    
    public void updateOutbound(OutboundMessage Outbound)
	{
		daooutbound.update(Outbound);
	}
    
    public List<OutboundMessage> findByPatientID(String patientID)
    {
    	return daooutbound.getOutBoundByPatientID(patientID);
    }
	
	public List<String> getReferenceNumberByPatientID(String patientID)
	{
		return daooutbound.getPatientsByReference(patientID);
	}



	@Override
	public List<InboundMessage> getAllInboundRecord(int firstResult,
			int fetchsize) {
		List<InboundMessage> list = daoinbound.getAll(firstResult, fetchsize);
		setLASTS_ROWS_RETURNED_COUNT(daoinbound.LAST_QUERY_TOTAL_ROW__COUNT());
		return list;
	}



	@Override
	public InboundMessage findInboundMessageByReferenceNumber(
			String referenceNumber, boolean readonly) {
		return daoinbound.findByReferenceNumber(referenceNumber, readonly);
	}



	@Override
	public void updateInbound(InboundMessage Inbound) {
		daoinbound.update(Inbound);
		
	}
}