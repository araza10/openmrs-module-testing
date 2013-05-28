/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.OpenMRSSMSModule.web.controller;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.OpenMRSSMSModule.OutboundMessage.Priority;
import org.openmrs.module.OpenMRSSMSModule.api.SmsModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The main controller.
 */
@Controller
public class  OpenMRSSMSModuleManageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value = "/module/OpenMRSSMSModule/manage", method = RequestMethod.GET)
	public void manage(ModelMap model) {
		//model.addAttribute("user", Context.getAuthenticatedUser());
	}
	
	@RequestMapping(value = "/module/OpenMRSSMSModule/manage", method = RequestMethod.POST)
	public void storingOutbound(@RequestParam(value="cellnum", required=true)String[] m, @RequestParam(value="txtMsg", required=true)String message,
			@RequestParam(value="patientid", required=true)String patientid, ModelMap model){
		
		int validityPeriod=4;
		try{
			
		SmsModuleService smsservice=Context.getService(SmsModuleService.class);
		
		
		for (String recipient : m) {
			
			smsservice.createNewOutboundSms(recipient, message, new Date(), Priority.HIGH, validityPeriod, patientid , org.openmrs.module.OpenMRSSMSModule.OutboundMessage.PeriodType.HOUR, 1, "");
		}
		model.addAttribute("messageText", "Messages Queued for sending");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
}
