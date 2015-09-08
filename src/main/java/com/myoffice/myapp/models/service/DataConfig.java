package com.myoffice.myapp.models.service;

import java.io.File;

public class DataConfig {
	public static final String DIR_SERVER = "C:" + File.separator + "DataServer" + File.separator;
	public static final String DIR_FILE_FLOW = DIR_SERVER + "Flow" + File.separator; 
	
	public static final String RSC_NAME_FLOW_OUT = "release_document_process.bpmn20.xml";
	public static final String PROC_DEF_KEY_FLOW_OUT = "release_document_process";
	public static final String DIR_SERVER_NAME_FLOW_OUT = DIR_FILE_FLOW + RSC_NAME_FLOW_OUT;
	
	public static final String RSC_NAME_FLOW_IN = "receive_document_process.bpmn20.xml";
	public static final String PROC_DEF_KEY_FLOW_IN = "receive_document_process";
	public static final String DIR_SERVER_NAME_FLOW_IN = DIR_FILE_FLOW + RSC_NAME_FLOW_IN;
	
	public static final String DATE_FORMAT_STRING = "dd-MM-yyyy";
	public static final String SYSTEM_MAIL = "freelanceruit@gmail.com"; 

}
