package com.myoffice.myapp.utils;

import java.io.FileInputStream;
import java.io.InputStream;

import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;


public interface FlowUtil {
	
	boolean deployProcess(String resourceName, String inputStream);
	
	String startProcess(String processDefinitionId);
	
	Task getCurrentTask(String processInstanceId);
	
	Execution getCurrentExecution(String processInstanceId);
	
	String getProcessDefinitionId(String resourceName, String key);
	

}
