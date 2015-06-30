package com.myoffice.myapp.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowUtil {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private HistoryService historyService;

	private static final Logger logger = LoggerFactory
			.getLogger(FlowUtil.class);

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

	public boolean deployProcess(String resourceName, String filePath) throws IOException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(filePath); 
			// Deploy process
			repositoryService
					.createDeployment()
					.enableDuplicateFiltering()
					.addInputStream(resourceName, inputStream)
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
		finally{
			inputStream.close();
		}
		

	}

	public String startProcess(String processDefinitionId) {
		try {
			// Start process instance
			ProcessInstance procInst = runtimeService.startProcessInstanceById(processDefinitionId);
			logger.info("Process Instances Id: " + procInst.getId());
			return procInst.getId();

		} catch (ActivitiException e) {
			logger.error("WORKFLOW ERROR : " + e.getMessage());
			return null;
		}
	}

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

	public Execution getCurrentExecution(String processInstanceId) {
		try {
			Execution execution = runtimeService.createExecutionQuery()
					.processInstanceId(processInstanceId).singleResult();
			
			
			logger.info("Current Execution : " + execution.toString());
			return execution;
			
		} catch (ActivitiException e)  {
			logger.error("EXECUTION ERROR : " + e.getMessage());
			return null;
		}
	}

	public void deleteProcessInstanceById(String processInstanceId, String deleteReason){
		try{
			runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
			logger.info("DELETE PROCESS ID : " + processInstanceId);
			
		}catch (ActivitiException e){
			logger.error(e.getMessage());
		}
	}
	
	public boolean isEnded(String processInstanceId) {
		try {
			boolean rs = historyService.createHistoricProcessInstanceQuery()
					.processInstanceId(processInstanceId)
					.singleResult()
					.getEndTime() == null ? false : true;
			return rs;
		} catch (ActivitiException e) {
			logger.info(e.getMessage());
			return false;
		}
	}
	
	public HistoricTaskInstance getPreviousCompletedTask(String processInstanceId) {
		try{
			List<HistoricTaskInstance> preTasks = historyService.createHistoricTaskInstanceQuery()
					.orderByHistoricTaskInstanceEndTime()
					.desc()
					.processInstanceId(processInstanceId)
					.finished()
					.list();
			
			logger.info("Pre Task : " + preTasks.get(0).getName());
			if(preTasks.size() > 0) return preTasks.get(0); 
			return null;
		}
		catch(ActivitiException e){
			logger.error(e.getMessage());
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
