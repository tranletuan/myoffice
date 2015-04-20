package com.myoffice.myapp.utils;

public interface FlowUtil {
	
	boolean deployProcess(String resource);
	
	boolean startProcess(String processDefinitionKey);
	
	
}
