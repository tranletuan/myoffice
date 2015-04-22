package com.myoffice.myapp.models.dao.parameter;

import java.util.List;

public interface Parameter {
	
	Parameter findParameterByName(String paramName);
	
	List<Parameter> findAllParameters();
	
	void saveParameter(Parameter param);
	
	void deleteParameter(Parameter param);
}
