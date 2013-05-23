package org.openmrs.module.OpenMRSSMSModule.api.db;

import java.io.Serializable;
import java.util.List;

import org.openmrs.module.OpenMRSSMSModule.DataException;


public interface DAODevice {

	org.openmrs.module.OpenMRSSMSModule.Device findById(int id);
	
	List<org.openmrs.module.OpenMRSSMSModule.Device> findByCriteria(String imei, org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus status , boolean notStatus, String projectName , String sim) throws DataException;

	List<org.openmrs.module.OpenMRSSMSModule.Device> getAll(int firstResult, int fetchsize);
	
	public Serializable save(Object objectinstance);
	
	public void update(Object objectinstance);

}
