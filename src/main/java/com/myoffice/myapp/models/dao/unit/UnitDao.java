package com.myoffice.myapp.models.dao.unit;

import java.util.List;
import java.util.Set;

import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.Unit;

public interface UnitDao {
	
	//unit
	List<Unit> findAllUnit();
	
	Unit findUnitById(Integer unitId);
	
	void saveUnit(Unit unit);
	
	void deleteUnit(Unit unit);
	
	List<Unit> findUnitByArray(Integer[] arrId);
	
	//organ
	List<Organ> findAllOrgan();
	
	Organ findOrganById(Integer organId);
	
	void saveOrgan(Organ organ);
	
	void deleteOrgan(Organ organ);
}
