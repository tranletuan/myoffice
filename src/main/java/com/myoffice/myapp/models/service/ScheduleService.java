package com.myoffice.myapp.models.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

public class ScheduleService {

	private static final Logger logger = LoggerFactory
			.getLogger(ScheduleService.class);
	
	@Scheduled(fixedDelay = 5000)
	public void schedulingDocInTask() {
		logger.info("This is test schedule service");
	}
}
