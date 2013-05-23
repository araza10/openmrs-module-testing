package org.openmrs.module.OpenMRSSMSModule.api.impl;

import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.Reminder;
import org.openmrs.module.OpenMRSSMSModule.api.reminderService;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOReminder;

public class reminderServiceImplementation implements reminderService{
	
	private DAOReminder r;

	public DAOReminder getR() {
		return r;
	}

	public void setR(DAOReminder r) {
		this.r = r;
	}

	@Override
	public List<Reminder> getAllReminders() {
		List<Reminder> rem=r.getAllReminders();
		return rem;
	}

	@Override
	public Reminder getReminder(int reminderId) {
		List<Reminder> rem=r.getReminder(reminderId);
		if(rem.size()==0){
			//throw new ReminderException(ReminderException.REMINDER_NOT_EXIST,ReminderException.REMINDER_NOT_EXIST);
		}
		return rem.get(0);
	}

	@Override
	public Reminder getReminder(String reminderName) {
		List<Reminder> rem=r.getReminder(reminderName);
		if(rem.size()==0){
			//throw new ReminderException(ReminderException.REMINDER_NOT_EXIST,ReminderException.REMINDER_NOT_EXIST);
		}
		return rem.get(0);
	}

	@Override
	public List<Reminder> getRemindersByName(String reminderName) {
		return r.getRemindersByName(reminderName);
	}

}
