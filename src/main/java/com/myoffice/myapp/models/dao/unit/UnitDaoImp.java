package com.myoffice.myapp.models.dao.unit;

import java.util.List;

import org.dom4j.util.PerThreadSingleton;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Organ;
import com.myoffice.myapp.models.dto.OrganType;
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

	
	//Organ
	@SuppressWarnings("unchecked")
	@Override
	public List<Organ> findAllOrgan() {
		Criteria criteria = getSession().createCriteria(Organ.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Organ findOrganById(Integer organId) {
		return (Organ)getSession().get(Organ.class, organId);
	}

	@Override
	public void saveOrgan(Organ organ) {
		persist(organ);
	}

	@Override
	public void deleteOrgan(Organ organ) {
		delete(organ);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Organ> findOrganByArray(Integer[] arrId) {
		String queryString = "from Organ where ";
		for (int i = 0; i < arrId.length; i++) {
			queryString += "organ_id=" + arrId[i];
			if (i >= 0 && i < arrId.length - 1) {
				queryString += " or ";
			}
		}

		Query query = (Query) getSession().createQuery(queryString);
		return query.list();
	}

	//ORGAN TYPE
	@SuppressWarnings("unchecked")
	@Override
	public List<OrganType> findAllOrganType() {
		Criteria criteria = getSession().createCriteria(OrganType.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public OrganType findOrganTypeById(Integer organTypeId) {
		return (OrganType) getSession().get(OrganType.class, organTypeId);
	}

	@Override
	public void saveOrganType(OrganType organType) {
		persist(organType);
	}

}
