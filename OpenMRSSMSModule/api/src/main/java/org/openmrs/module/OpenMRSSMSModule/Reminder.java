package org.openmrs.module.OpenMRSSMSModule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsObject;


@Entity
@Table(name = "reminder")
public class Reminder extends BaseOpenmrsObject implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue
	@Column(name = "reminder_id")
	private int				reminder_id;
	
	@Column(name = "name")
	private String			name;
	
	@Column(name= "description")
	private String 			description;
	
	@Column(name = "category")
	private String  		category;
	
	@Column(name = "text")
	private String  		text;
	
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getReminder_id() {
		return reminder_id;
	}

	public void setReminder_id(int reminder_id) {
		this.reminder_id = reminder_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(Integer id) {
		// TODO Auto-generated method stub
		
	}

}
