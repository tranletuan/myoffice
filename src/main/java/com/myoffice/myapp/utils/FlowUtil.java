package com.myoffice.myapp.utils;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;


public interface FlowUtil {
	
	boolean deployProcess(String resourceName, String filePath);
	
	boolean startProcess(String processDefinitionId);
	
	Task getCurrentTask(String processInstanceId);
	
	Execution getCurrentExecution(String processInstanceId);
	
	
}
