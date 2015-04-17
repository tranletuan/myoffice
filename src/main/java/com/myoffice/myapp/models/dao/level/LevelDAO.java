package com.myoffice.myapp.models.dao.level;

import java.util.List;

import com.myoffice.myapp.models.dto.Level;

public interface LevelDAO {

	Level findLevelByName(String levelName);
	
	List<Level> findAllLevels();
	
	void saveLevel(Level level);
	
	void deleteLevelByName(String levelName);
}
