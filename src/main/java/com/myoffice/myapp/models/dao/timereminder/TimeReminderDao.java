package com.myoffice.myapp.models.dao.timereminder;

import java.util.Date;
import java.util.List;

import com.myoffice.myapp.models.dto.TimeReminder;

public interface TimeReminderDao {

	TimeReminder findTimeReminderById(Integer timeId);
	
	List<TimeReminder> findAllTimeReminder();
	
	List<TimeReminder> findActiveTimeReminder(Date toDay);
	
	void saveTimeReminder(TimeReminder timeReminder);
	
}
