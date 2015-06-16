package com.myoffice.myapp.models.service;

public class DataConfig {
	public static final String DIR_SERVER = "D:/DataServer/";
	public static final String DIR_FILE_FLOW = DIR_SERVER + "Flow/";
	
	public static final String RSC_NAME_FLOW_OUT = "release_document_process.bpmn20";
	public static final String DEPLOY_RSC_FLOW_OUT = RSC_NAME_FLOW_OUT + ".xml";
	public static final String PROC_DEF_KEY_FLOW_OUT = "release_document_process";
	public static final String DIR_SERVER_NAME_FLOW_OUT = DIR_FILE_FLOW + RSC_NAME_FLOW_OUT;
	
	public static final String RSC_NAME_FLOW_IN = "receive_document_process.bpmn20";
	public static final String DEPLOY_RSC_FLOW_IN = RSC_NAME_FLOW_IN + ".xml";
	public static final String PROC_DEF_KEY_FLOW_IN = "receive_document_process";
	public static final String DIR_SERVER_NAME_FLOW_IN = DIR_FILE_FLOW + RSC_NAME_FLOW_IN;
	

}
