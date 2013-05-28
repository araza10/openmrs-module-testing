package org.openmrs.module.OpenMRSSMSModule.api.impl;

import java.util.HashMap;
import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.api.impl.ActiveDevice;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAODevice;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOOutBoundMessage;
import  org.openmrs.module.OpenMRSSMSModule.api.db.DAOProject;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.Project;
import org.openmrs.module.OpenMRSSMSModule.api.DeviceService;

public class DeviceServiceImpl extends BaseOpenmrsService implements DeviceService{
	
	private DAODevice daodevice;
	private DAOProject daoproj;
	public static HashMap<String, ActiveDevice> 	ACTIVE_DEVICES   = new HashMap<String, ActiveDevice>();
	
	
/*	public deviceServiceImpl(DAODevice daodevice, DAOProject daoproj)
	{
		this.daodevice = daodevice;
		this.daoproj = daoproj;
	}*/
	
	public void setDaodevice(DAODevice daodevice) {
    	this.daodevice = daodevice;
    }
	
	public void setDaoproj(DAOProject daoproj) {
    	this.daoproj = daoproj;
    }

	@Override
	public void onStartup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onShutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Device findDeviceById(int id) {
		return daodevice.findById(id);
	}

	@Override
	public List<Device> findDevice(String imei, DeviceStatus status,
			boolean notStatus, String projectName, String sim)
			throws DataException {
		
		return daodevice.findByCriteria(imei, status, notStatus ,projectName, sim);
	}

	@Override
	public List<Device> getAllDevices(int firstResult, int fetchsize) {
		return daodevice.getAll(firstResult, fetchsize);
	}

	@Override
	public void saveDevice(Device device) {
		daodevice.save(device);
		
	}

	@Override
	public void updateDevice(Device device) {
		daodevice.update(device);
		
	}

	@Override
	public Project findProjectById(int id) {
		return daoproj.findById(id);
	}

	@Override
	public List<Project> findProject(String projectname) {
		return daoproj.findByCriteria(projectname);
	}

	@Override
	public List<Project> getAllProjects(int firstResult, int fetchsize) {
		return daoproj.getAll(firstResult, fetchsize);
	}

	@Override
	public void saveProject(Project project) {
		daoproj.save(project);
		
	}

	@Override
	public void updateProject(Project project) {
		daoproj.update(project);
		
	}

}
