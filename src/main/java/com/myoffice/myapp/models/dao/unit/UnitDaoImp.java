package com.myoffice.myapp.models.dao.unit;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Unit;

@Repository
public class UnitDaoImp extends AbstractDao implements UnitDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> findAllUnit() {
		Criteria criteria = getSession().createCriteria(Unit.class);
		return criteria.list();
	}

	@Override
	public Unit findUnitById(Integer unitId) {
		return (Unit)getSession().get(Unit.class, unitId);
	}

	@Override
	public void saveUnit(Unit unit) {
		persist(unit);
		
	}

	@Override
	public void deleteUnit(Unit unit) {	
		delete(unit);
	}

}
