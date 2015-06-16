package com.myoffice.myapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.task.Task;

import com.myoffice.myapp.models.dto.User;

public class UtilMethod {
	
	//Covert String to Date
	public static Date toDate(String dateString, String dateFormat) throws ParseException{
	    DateFormat df = new SimpleDateFormat(dateFormat); 
	    Date rsDate = df.parse(dateString);
	    return rsDate;
	}
	
	// Check String is Number
	public static Integer parseNumDoc(String number) {
		Integer integer = -1;
		try {
			integer = Integer.parseInt(number);
		} catch (NumberFormatException nfe) {

		}
		return integer;
	}
}
