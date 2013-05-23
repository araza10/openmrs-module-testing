package org.openmrs.module.OpenMRSSMSModule.api;

import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.Reminder;

public interface reminderService {

	
	public List<Reminder> getAllReminders();
	public Reminder getReminder(int reminderId);
	public Reminder getReminder(String reminderName);
	public List<Reminder> getRemindersByName(String reminderName);


}
