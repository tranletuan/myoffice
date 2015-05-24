package com.myoffice.myapp.models.dao.unit;

import java.util.List;

import com.myoffice.myapp.models.dto.Unit;

public interface UnitDao {
	
	List<Unit> findAllUnit();
	
	Unit findUnitById(Integer unitId);
	
	void saveUnit(Unit unit);
	
	void deleteUnit(Unit unit);
	
}
