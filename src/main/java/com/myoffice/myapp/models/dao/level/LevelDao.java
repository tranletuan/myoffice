package com.myoffice.myapp.models.dao.level;

import java.util.List;

import com.myoffice.myapp.models.dto.Level;

public interface LevelDao {

	List<Level> findAllLevel();
	
	Level findLevelById(Integer levelId);
	
	void saveLevel(Level level);
	
}
