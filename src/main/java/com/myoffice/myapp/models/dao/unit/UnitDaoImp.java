package com.myoffice.myapp.models.dao.unit;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
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
		return (Unit) getSession().get(Unit.class, unitId);
	}

	@Override
	public void saveUnit(Unit unit) {
		persist(unit);

	}

	@Override
	public void deleteUnit(Unit unit) {
		delete(unit);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> findUnitByArray(Integer[] arrId) {

		String queryString = "from Unit where ";
		for (int i = 0; i < arrId.length; i++) {
			queryString += "unit_id=" + arrId[i];
			if (i >= 0 && i < arrId.length - 1) {
				queryString += " or ";
			}
		}

		Query query = (Query) getSession().createQuery(queryString);
		return query.list();
	}

}
