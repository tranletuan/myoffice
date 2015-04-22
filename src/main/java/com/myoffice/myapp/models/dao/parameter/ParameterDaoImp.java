package com.myoffice.myapp.models.dao.parameter;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.myoffice.myapp.models.dao.common.AbstractDao;

@Repository
public class ParameterDaoImp extends AbstractDao implements Parameter {

	private static final Logger logger = LoggerFactory
			.getLogger(ParameterDaoImp.class);

	public ParameterDaoImp() {
		logger.info("PARAMETER DAO HAS CONSTRUCTED");
	}

	@Override
	@SuppressWarnings("unchecked")
	public Parameter findParameterByName(String paramName) {

		Query query = (Query) getSession().createSQLQuery(
				"from Parameter where param_name=?");
		query.setParameter(0, paramName);
		List<Parameter> params = query.list();

		if (params.size() > 0) {
			return params.get(0);
		}

		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Parameter> findAllParameters() {
		Criteria criteria = getSession().createCriteria(Parameter.class);
		return (List<Parameter>) criteria.list();
	}

	@Override
	public void saveParameter(Parameter param) {
		persist(param);
	}

	@Override
	public void deleteParameter(Parameter param) {
		delete(param);
	}

}
