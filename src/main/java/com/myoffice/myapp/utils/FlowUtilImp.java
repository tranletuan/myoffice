package com.myoffice.myapp.utils;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowUtilImp implements FlowUtil {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;

	private static final Logger logger = LoggerFactory
			.getLogger(FlowUtilImp.class);

	public FlowUtilImp() {
		logger.info("FLOW UTIL HAS CONSTRUCTED");
	}

	@Override
	public boolean deployProcess(String resource) {

		try {
			//Deploy process
			repositoryService.createDeployment().addClasspathResource(resource)
					.deploy();
			
			//Number of process definition
			logger.info("Number of Process Definition : "
					+ repositoryService.createProcessDefinitionQuery().count());
			
			return true;
		} catch (ActivitiException e) {
			logger.info("WORKFLOW ERROR : " + e.getMessage());
			return false;
		}

	}

	@Override
	public boolean startProcess(String processDefinitionKey) {
		try {
			//Start process instance by key
			runtimeService.startProcessInstanceByKey(processDefinitionKey);
			
			logger.info("Number of Process Instances : " + runtimeService.createProcessInstanceQuery().count());
			return true;
		}
		catch (ActivitiException e){
			logger.info("WORKFLOW ERROR : " + e.getMessage());
			return false;
		}
	}
	
	

}
