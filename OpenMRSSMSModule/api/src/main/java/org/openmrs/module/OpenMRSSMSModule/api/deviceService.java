package org.openmrs.module.OpenMRSSMSModule.api;


import java.util.List;


import org.openmrs.api.OpenmrsService;
import org.openmrs.module.OpenMRSSMSModule.DataException;
import org.openmrs.module.OpenMRSSMSModule.Device;
import org.openmrs.module.OpenMRSSMSModule.Device.DeviceStatus;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.PeriodType;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.Priority;
import org.openmrs.module.OpenMRSSMSModule.Project;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface deviceService extends OpenmrsService {
	
org.openmrs.module.OpenMRSSMSModule.Device findDeviceById(int id);
	
	List<org.openmrs.module.OpenMRSSMSModule.Device> findDevice(String imei, DeviceStatus status, boolean notStatus , String projectName , String sim) throws DataException;

	List<org.openmrs.module.OpenMRSSMSModule.Device> getAllDevices(int firstResult, int fetchsize);
	
	void saveDevice(Device device);
	
	void updateDevice(Device device);
	
	Project findProjectById(int id);
	
	List<Project> findProject(String projectname);

	List<Project> getAllProjects(int firstResult, int fetchsize);
	
	void saveProject(Project project);
	
	void updateProject(Project project);

}
