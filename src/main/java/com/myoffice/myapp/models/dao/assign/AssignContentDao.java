package com.myoffice.myapp.models.dao.assign;

import java.text.ParseException;
import java.util.List;

import com.myoffice.myapp.models.dto.AssignContent;

public interface AssignContentDao {

	List<AssignContent> findAllAssignContent();
	
	AssignContent findAssignContentById(Integer canId);
	
	void saveAssignContent(AssignContent assContent);
	
	void deleteAssignContent(AssignContent assContent);
}
