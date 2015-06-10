package com.myoffice.myapp.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;
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

	@Override
	public String getProcessDefinitionId(String resourceName, String key) {
		try {
			String rs = repositoryService.createProcessDefinitionQuery()
					.processDefinitionKey(key)
					.processDefinitionResourceName(resourceName)
					.latestVersion().singleResult().getId();
			return rs;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return " ";
		}
	}

	@Override
	public boolean deployProcess(String resourceName, String filePath) {
		try {
			
			// Deploy process
			repositoryService
					.createDeployment()
					.enableDuplicateFiltering()
					.addInputStream(resourceName, new FileInputStream(filePath))
					.deploy();
			
			return true;
		} catch (ActivitiException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			return false;
		} catch (FileNotFoundException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			e.printStackTrace();
			return false;
		}
		

	}

	@Override
	public String startProcess(String processDefinitionId) {
		try {
			// Start process instance
			runtimeService.startProcessInstanceById(processDefinitionId);
			String processInstanceId = runtimeService
					.createProcessInstanceQuery()
					.processDefinitionId(processDefinitionId).singleResult()
					.getId();
			logger.info("Process Instances Id: " + processInstanceId);
			return processInstanceId;

		} catch (ActivitiException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			return null;
		}
	}

	@Override
	public Task getCurrentTask(String processInstanceId) {

		try {
			Task task = taskService.createTaskQuery()
					.processInstanceId(processInstanceId).singleResult();

			logger.info("Current Task : " + task.toString());
			return task;
		} catch (ActivitiException e) {
			logger.error("TASK ERROR : " + e.getMessage());
			return null;
		}
	}

	@Override
	public Execution getCurrentExecution(String processInstanceId) {
		try {
			Execution execution = runtimeService.createExecutionQuery()
					.processInstanceId(processInstanceId).singleResult();
			
			logger.info("Current Execution : " + execution.toString());
			return execution;
			
		} catch (ActivitiException e) {
			logger.error("EXECUTION ERROR : " + e.getMessage());
			return null;
		}
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
}
