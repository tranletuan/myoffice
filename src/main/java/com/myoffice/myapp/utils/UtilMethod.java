package com.myoffice.myapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilMethod {
	
	//Covert String to Date
	public static Date toDate(String dateString, String dateFormat) throws ParseException{
	    DateFormat df = new SimpleDateFormat(dateFormat); 
	    Date rsDate = df.parse(dateString);
	    return rsDate;
	}
}
