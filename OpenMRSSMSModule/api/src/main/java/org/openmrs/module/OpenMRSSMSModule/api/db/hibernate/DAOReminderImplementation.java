package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.module.OpenMRSSMSModule.Reminder;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOReminder;

public class DAOReminderImplementation implements DAOReminder {
	
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getAllReminders() {
		Query q=sessionFactory.getCurrentSession().createQuery("from Reminder order by name");
		
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getReminder(int reminderId) {
		Query q=sessionFactory.getCurrentSession().createQuery("from Reminder where reminderId=? order by name");
		q.setInteger(0, reminderId);
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getReminder(String reminderName) {
		Query q=sessionFactory.getCurrentSession().createQuery("from Reminder where name like '"+reminderName+"' order by name");
		return q.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reminder> getRemindersByName(String reminderName) {
		Query q=sessionFactory.getCurrentSession().createQuery("from Reminder where name like '"+reminderName+"%' order by name");
		return q.list();
	}

}
