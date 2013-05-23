package org.openmrs.module.OpenMRSSMSModule;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.openmrs.BaseOpenmrsObject;

@Entity
@Table(name = "project")
public class Project extends BaseOpenmrsObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "projectId")
	private int		projectId;

	@Column(name = "name", unique = true)
	private String	name;

	@Column(name = "description")
	private String	description;

	public void setProjectId(int projectId)
	{
		this.projectId = projectId;
	}

	public int getProjectId()
	{
		return projectId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getDescription()
	{
		return description;
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
