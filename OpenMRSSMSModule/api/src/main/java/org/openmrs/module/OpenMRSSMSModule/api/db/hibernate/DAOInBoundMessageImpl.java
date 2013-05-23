package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage;
import org.openmrs.module.OpenMRSSMSModule.InboundMessage.InboundStatus;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOInBoundMessage;

public class DAOInBoundMessageImpl implements DAOInBoundMessage{
	
	SessionFactory sessionFactory;
	private Number			LAST_QUERY_TOTAL_ROW__COUNT;
	
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Serializable save(Object objectinstance) {
		
		return sessionFactory.getCurrentSession().save(objectinstance);
	}

	@Override
	public void update(Object objectinstance) {
		sessionFactory.getCurrentSession().update(objectinstance);
		
	}

	@Override
	public InboundMessage findById(long id) {
		InboundMessage usms = (InboundMessage) sessionFactory.getCurrentSession().get(InboundMessage.class, id);
		setLAST_QUERY_TOTAL_ROW__COUNT(usms == null ? 0 : 1);
		return usms;
	}
	
	private void setLAST_QUERY_TOTAL_ROW__COUNT(Number LAST_QUERY_TOTAL_ROW__COUNT)
	{
		this.LAST_QUERY_TOTAL_ROW__COUNT = LAST_QUERY_TOTAL_ROW__COUNT;
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public InboundMessage findByReferenceNumber(String referenceNumber,
			boolean readonly) {
		
		List<InboundMessage> list = sessionFactory.getCurrentSession().createQuery("from InboundMessage where referenceNumber='"+referenceNumber+"' ").setReadOnly(readonly).list();
		setLAST_QUERY_TOTAL_ROW__COUNT(list.size());
		return list.size()>0?list.get(0):null;
	}

	@Override
	public Number countCriteriaRows(Date recieveDatesmaller,
			Date recieveDategreater, InboundStatus smsStatus, String recipient,
			String originator, String imei, String projectName,
			boolean putNotWithSmsStatus) throws DataException {
		
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(InboundMessage.class);

		if (recieveDatesmaller != null && recieveDategreater != null)
		{
			cri.add(Restrictions.between("recieveDate", recieveDatesmaller, recieveDategreater));
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
					cri.add(Restrictions.not(Restrictions.eq("status", smsStatus )));
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

	@Override
	public Number countAllRows() {
		return (Number) sessionFactory.getCurrentSession().createQuery(
				"select count(*) from InboundMessage").uniqueResult();
	}

	@Override
	public Number LAST_QUERY_TOTAL_ROW__COUNT() {
		return LAST_QUERY_TOTAL_ROW__COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InboundMessage> findByCriteria(Date recieveDatesmaller,
			Date recieveDategreater, InboundStatus smsStatus, String recipient,
			String originator, String imei, Integer projectId,
			boolean putNotWithSmsStatus) throws DataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(null);
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(InboundMessage.class);

		if (recieveDatesmaller != null && recieveDategreater != null)
		{
			cri.add(Restrictions.between("recieveDate", recieveDatesmaller, recieveDategreater));
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
		if (projectId != null)
		{
			cri.add(Restrictions.eq("projectId", projectId));
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
		return cri.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InboundMessage> findByCriteria(Date recieveDatesmaller,
			Date recieveDategreater, InboundStatus smsStatus, String recipient,
			String originator, String imei, String projectName,
			boolean putNotWithSmsStatus, int firstResult, int fetchsize)
			throws DataException {
		setLAST_QUERY_TOTAL_ROW__COUNT(countCriteriaRows(recieveDatesmaller, recieveDategreater
				, smsStatus, recipient,  originator
				, imei , projectName , putNotWithSmsStatus));
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(InboundMessage.class);

		if (recieveDatesmaller != null && recieveDategreater != null)
		{
			cri.add(Restrictions.between("recieveDate", recieveDatesmaller, recieveDategreater));
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
		return cri.setFirstResult(firstResult).setMaxResults(fetchsize).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<InboundMessage> getAll(int firstResult, int fetchsize) {
		setLAST_QUERY_TOTAL_ROW__COUNT(countAllRows());
		return sessionFactory.getCurrentSession().createQuery("from InboundMessage order by recieveDate desc")
				.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	@Override
	public int markInboundAsRead(String referenceNumber) {
		Query q = sessionFactory.getCurrentSession().createQuery("update InboundMessage set status = '"+InboundStatus.READ+"' where referenceNumber = '"+referenceNumber+"' ");
		return q.executeUpdate();
	}

	@Override
	public int markInboundAsRead(long inboundId) {
		Query q = sessionFactory.getCurrentSession().createQuery("update InboundMessage set status = '"+InboundStatus.READ+"' where inboundId = "+inboundId+" ");
		return q.executeUpdate();
	}

}
