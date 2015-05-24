package com.myoffice.myapp.models.dao.parameter;

import java.util.List;

import com.myoffice.myapp.models.dto.Parameter;

public interface ParameterDao {
	
	Parameter findParameterByName(String paramName);
	
	List<Parameter> findAllParameters();
	
	void saveParameter(Parameter param);
	
	void deleteParameter(Parameter param);
	

}
