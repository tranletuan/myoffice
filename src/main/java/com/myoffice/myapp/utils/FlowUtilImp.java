package com.myoffice.myapp.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

	public FlowUtilImp() {
		logger.info("FLOW UTIL HAS CONSTRUCTED");
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

			// Number of process definition
			logger.info("Process Definition Id : "
					+ repositoryService.createProcessDefinitionQuery()
							.processDefinitionResourceName(resourceName)
							.latestVersion().singleResult().getId());

			return true;
		} catch (ActivitiException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			return false;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean startProcess(String processDefinitionId) {
		try {
			// Start process instance
			runtimeService.startProcessInstanceById(processDefinitionId);

			logger.info("Process Instances Id: "
					+ runtimeService.createProcessInstanceQuery()
							.processDefinitionId(processDefinitionId)
							.singleResult().getId());
			return true;
		} catch (ActivitiException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			return false;
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

		return null;
	}
}
