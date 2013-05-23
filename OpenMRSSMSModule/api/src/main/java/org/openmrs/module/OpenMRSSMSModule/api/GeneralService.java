package org.openmrs.module.OpenMRSSMSModule.api;

import java.util.List;

import org.hibernate.SQLQuery;
import org.openmrs.api.OpenmrsService;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public interface GeneralService extends OpenmrsService {
	
	 List slqQuery();
	 SQLQuery update(String query);
	

}
