package com.myoffice.myapp.models.dao.level;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Level;

@Repository
public class LevelDAOImp extends AbstractDao implements LevelDAO {

	private static final Logger logger = LoggerFactory.getLogger(LevelDAOImp.class);
	
	public LevelDAOImp() {
		logger.info("LEVEL DAO HAS CONSTRUCTED");
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Level findLevelByName(String levelName) {
		Query query = (Query) getSession().createQuery(
				"from Level where level_name=?");
		query.setParameter(0, levelName);
		List<Level> levels = query.list();

		if (levels.size() > 0) {
			return levels.get(0);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Level> findAllLevels() {
		Criteria criteria = getSession().createCriteria(Level.class);
		return (List<Level>)criteria.list();
	}

	@Override
	public void saveLevel(Level level) {
		persist(level);
	}

	@Override
	public void deleteLevel(Level level) {
		delete(level);
	}

}
