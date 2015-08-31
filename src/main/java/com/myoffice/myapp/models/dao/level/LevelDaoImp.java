package com.myoffice.myapp.models.dao.level;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Level;

@Repository
public class LevelDaoImp extends AbstractDao implements LevelDao{

	@SuppressWarnings("unchecked")
	@Override
	public List<Level> findAllLevel() {
		Criteria criteria = getSession().createCriteria(Level.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Level findLevelById(Integer levelId) {
		return (Level) getSession().get(Level.class, levelId);
	}

	@Override
	public void saveLevel(Level level) {
		persist(level);
	}

	
}
