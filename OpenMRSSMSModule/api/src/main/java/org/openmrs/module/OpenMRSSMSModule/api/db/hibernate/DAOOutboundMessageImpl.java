package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.OutboundStatus;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOOutBoundMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DAOOutboundMessageImpl implements org.openmrs.module.OpenMRSSMSModule.api.db.DAOOutBoundMessage{
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}

/*	public DAOOutboundMessageImpl(Session session)
	{
		super(session);
		this.session = session;
	}*/
	
	public OutboundMessage findById(long id)
	{
		OutboundMessage usms = (OutboundMessage) sessionFactory.getCurrentSession().get(OutboundMessage.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(usms == null ? 0 : 1);
		return usms;
	}
	
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT)
	{
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}
	public Serializable save(Object objectinstance)
	{
		return sessionFactory.getCurrentSession().save(objectinstance);
	}

	
	public List<OutboundMessage> findByCriteria(Date duedatesmaller, Date duedategreater,
			Date sentdatesmaller, Date sentdategreater
			, OutboundStatus smsStatus,  String recipient,  String originator
			, String imei,  String projectName,
			boolean putNotWithSmsStatus, boolean orderByPriority, int firstResult, int fetchsize) throws DataException
	{
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(duedatesmaller, duedategreater,
				sentdatesmaller, sentdategreater
				, smsStatus, recipient,  originator
				, imei , projectName , putNotWithSmsStatus));
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(OutboundMessage.class);

		if (duedatesmaller != null && duedategreater != null)
		{
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if (sentdatesmaller != null && sentdategreater != null)
		{
			cri.add(Restrictions.between("sentdate", sentdatesmaller, sentdategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (originator != null)
		{
			cri.add(Restrictions.like("originator", originator, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (smsStatus != null)
		{
			try
			{
				if (putNotWithSmsStatus)
				{
					cri.add(Restrictions.not(Restrictions.eq("status", smsStatus)));
				}
				else
				{
					cri.add(Restrictions.eq("status", smsStatus));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		if(orderByPriority){
			cri.addOrder(Order.asc("priority"));
		}
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	
	public Number countCriteriaRows(Date duedatesmaller, Date duedategreater
			, Date sentdatesmaller, Date sentdategreater
			, OutboundStatus smsStatus, String recipient, String originator
			, String imei,  String projectName, boolean putNotWithSmsStatus)
			throws DataException
	{
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(OutboundMessage.class);

		if (duedatesmaller != null && duedategreater != null)
		{
			cri.add(Restrictions.between("dueDate", duedatesmaller, duedategreater));
		}
		if (sentdatesmaller != null && sentdategreater != null)
		{
			cri.add(Restrictions.between("sentdate", sentdatesmaller, sentdategreater));
		}
		if (recipient != null)
		{
			cri.add(Restrictions.like("recipient", recipient, MatchMode.END));
		}
		if (originator != null)
		{
			cri.add(Restrictions.like("originator", originator, MatchMode.END));
		}
		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (smsStatus != null)
		{
			try
			{
				if (putNotWithSmsStatus)
				{
					cri.add(Restrictions.not(Restrictions.eq("status", smsStatus)));
				}
				else
				{
					cri.add(Restrictions.eq("status", smsStatus));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		cri.setProjection(Projections.rowCount());
		return (Number) cri.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<String> getPatientsByReference(String patientId)
	{
		List<String> list=sessionFactory.getCurrentSession().createQuery("from OutboundMessage where patient_id='"+patientId+"'").list();
		return list;
	}

	@Override
	public void update(Object objectinstance) {
		sessionFactory.getCurrentSession().update(objectinstance);
		
	}

	@Override
	public List<OutboundMessage> getOutBoundByPatientID(String patientId) {
		@SuppressWarnings("unchecked")
		List<OutboundMessage> list=sessionFactory.getCurrentSession().createQuery("from OutboundMessage where patient_id='"+patientId+"'").list();
		return list;
	}
	
}
