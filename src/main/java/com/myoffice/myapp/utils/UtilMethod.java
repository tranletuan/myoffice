package com.myoffice.myapp.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.web.servlet.ModelAndView;

import com.myoffice.myapp.models.dto.User;

public class UtilMethod {
	
	//Covert String to Date
	public static Date toDate(String dateString, String dateFormat) throws ParseException{
	    DateFormat df = new SimpleDateFormat(dateFormat); 
	    Date rsDate = df.parse(dateString);
	    return rsDate;
	}
	
	//Convert Date to String
	public static String dateToString(Date date, String dateFormat) {
		DateFormat df = new SimpleDateFormat(dateFormat);
		String rsDate = df.format(date);
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
	
	public static void preparePagination(List<Integer> rowList, List<Integer> elemList, List infoList,
			ModelAndView model, Integer maxElementPerRow) {
		int maxElem = 10;
		if(maxElementPerRow != null) maxElem = maxElementPerRow;
		for(int i = 0; i < maxElem; i++){
			elemList.add(i);
		}
		
		int numRow = infoList.size() / 10;
		int maxRow = infoList.size() % 10 == 0 ? numRow : numRow + 1;
		
		for(int i = 0; i < maxRow; i++){
			rowList.add(i);
		}
		

		model.addObject("rowList", rowList);
		model.addObject("elemList", elemList);
	}
}
