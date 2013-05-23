package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAODevice;
import org.springframework.stereotype.Repository;


@Repository
public class DAODeviceImpl implements DAODevice {
	
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	
	public Device findById(int id)
	{
		return (Device) sessionFactory.getCurrentSession().get(Device.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Device> getAll(int firstResult, int fetchsize)
	{
		return sessionFactory.getCurrentSession().createQuery("from Device ")
				.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}
	
	public Serializable save(Object objectinstance)
	{
		return sessionFactory.getCurrentSession().save(objectinstance);
	}

	@SuppressWarnings("unchecked")
	public List<Device> findByCriteria(String imei, DeviceStatus status , boolean notStatus, String projectName , String sim) throws DataException
	{
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(Device.class);

		if (imei != null)
		{
			cri.add(Restrictions.like("imei", imei, MatchMode.EXACT));
		}
		if (sim != null)
		{
			cri.add(Restrictions.like("sim", sim, MatchMode.EXACT));
		}
		if (projectName != null)
		{
			cri.createAlias("project", "p").add(Restrictions.like("p.name", projectName, MatchMode.EXACT));
		}
		if (status != null)
		{
			try
			{
				if(notStatus){
					cri.add(Restrictions.not(Restrictions.eq("status", status)));
				}
				else{
					cri.add(Restrictions.eq("status", status));
				}
			}
			catch (Exception e)
			{
				throw new DataException(DataException.INVALID_CRITERIA_VALUE_SPECIFIED);
			}
		}
		return cri.list();
	}

	@Override
	public void update(Object objectinstance) {
		 sessionFactory.getCurrentSession().update(objectinstance);
		
	}


}
