package org.openmrs.module.OpenMRSSMSModule;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openmrs.module.OpenMRSSMSModule.Project;
import org.openmrs.BaseOpenmrsObject;

@Entity
@Table(name = "outboundmessage")
public class OutboundMessage extends BaseOpenmrsObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum OutboundStatus {
		PENDING, SENT, FAILED, MISSED, UNKNOWN
	}

	public enum OutboundType {
		SMS, MMS, PDU, UNKNOWN
	}

	public enum PeriodType {
		HOUR, DAY, WEEK
	}

	public enum Priority {
		HIGHEST, HIGH, MEDIUM, LOW, LOWEST
	}

	@Id
	@GeneratedValue
	@Column(name = "outboundId")
	private long outboundId;

	@Column(name = "text", nullable = false)
	private String text;

	@Column(name = "recipient", nullable = false)
	private String recipient;

	@Column(name = "originator")
	private String originator;

	@Column(name = "patient_id", nullable = false)
	private String patient_id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dueDate", nullable = false)
	private Date dueDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "sentDate")
	private Date sentDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingStartDate")
	private Date systemProcessingStartDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "systemProcessingEndDate")
	private Date systemProcessingEndDate;

	@Column(name = "tries")
	private Integer tries;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private OutboundStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private OutboundType type;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "priority", nullable = false)
	private Priority priority;

	@Column(name = "referenceNumber", nullable = false, unique = true)
	private String referenceNumber;

	@Column(name = "validityPeriod", nullable = false)
	private int validityPeriod;

	@Enumerated(EnumType.STRING)
	@Column(name = "periodType", nullable = false)
	private PeriodType periodType;

	@Column(name = "imei")
	private String imei;

	@Column(name = "description")
	private String description;

	@Column(name = "errorMessage")
	private String errorMessage;

	@Column(name = "failureCause")
	private String failureCause;

	private Integer projectId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "projectId", updatable = false, insertable = false)
	private Project project;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate", nullable = false)
	private Date createdDate;

	/**
	 * @param recipient
	 *            the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return this.recipient;
	}

	public String getPatient_id() {
		return patient_id;
	}

	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the dueDate
	 */
	public Date getDueDate() {
		return this.dueDate;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param sentdate
	 *            the sentdate to set
	 */
	public void setSentdate(Date sentdate) {
		this.sentDate = sentdate;
	}

	/**
	 * @return the sentdate
	 */
	public Date getSentdate() {
		return this.sentDate;
	}

	/**
	 * @param errormessage
	 *            the errormessage to set
	 */
	public void setErrormessage(String errormessage) {
		this.errorMessage = errormessage;
	}

	/**
	 * @return the errormessage
	 */
	public String getErrormessage() {
		return this.errorMessage;
	}

	/**
	 * @param failureCause
	 *            the failureCause to set
	 */
	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	/**
	 * @return the failureCause
	 */
	public String getFailureCause() {
		return this.failureCause;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public String getOriginator() {
		return originator;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getImei() {
		return imei;
	}

	public void setSystemProcessingStartDate(Date systemProcessingStartDate) {
		this.systemProcessingStartDate = systemProcessingStartDate;
	}

	public Date getSystemProcessingStartDate() {
		return systemProcessingStartDate;
	}

	public void setSystemProcessingEndDate(Date systemProcessingEndDate) {
		this.systemProcessingEndDate = systemProcessingEndDate;
	}

	public Date getSystemProcessingEndDate() {
		return systemProcessingEndDate;
	}

	public Integer getTries() {
		return tries;
	}

	public void setTries(Integer tries) {
		this.tries = tries;
	}

	public void setOutboundId(long outboundId) {
		this.outboundId = outboundId;
	}

	public long getOutboundId() {
		return outboundId;
	}

	public void setStatus(OutboundStatus status) {
		this.status = status;
	}

	public OutboundStatus getStatus() {
		return status;
	}

	public void setType(OutboundType type) {
		this.type = type;
	}

	public OutboundType getType() {
		return type;
	}

	public void setValidityPeriod(int validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public int getValidityPeriod() {
		return validityPeriod;
	}

	public void setPeriodType(PeriodType periodType) {
		this.periodType = periodType;
	}

	public PeriodType getPeriodType() {
		return periodType;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Integer arg0) {
		// TODO Auto-generated method stub

	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}
