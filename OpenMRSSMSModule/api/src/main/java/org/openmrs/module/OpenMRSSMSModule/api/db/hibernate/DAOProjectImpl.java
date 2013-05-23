package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.OpenMRSSMSModule.Project;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOProject;

public class DAOProjectImpl implements DAOProject {
	
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	
	public Project findById(int id)
	{
		return (Project) sessionFactory.getCurrentSession().get(Project.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Project> getAll(int firstResult, int fetchsize)
	{
		return sessionFactory.getCurrentSession().createQuery("from Project ")
				.setFirstResult(firstResult).setMaxResults(fetchsize).list();
	}

	@SuppressWarnings("unchecked")
	public List<Project> findByCriteria(String projectName)
	{
		Criteria cri = sessionFactory.getCurrentSession().createCriteria(Project.class);

		if (projectName != null)
		{
			cri.add(Restrictions.like("name", projectName, MatchMode.EXACT));
		}
		
		return cri.list();
	}

	@Override
	public Serializable save(Object objectinstance) {
		
		return sessionFactory.getCurrentSession().save(objectinstance);
	}

	@Override
	public void update(Object objectinstance) {
		sessionFactory.getCurrentSession().update(objectinstance);
		
	}

}
