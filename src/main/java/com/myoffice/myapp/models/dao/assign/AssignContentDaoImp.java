package com.myoffice.myapp.models.dao.assign;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.AssignContent;
import com.myoffice.myapp.utils.UtilMethod;
import com.mysql.jdbc.Util;

@Repository
public class AssignContentDaoImp extends AbstractDao implements AssignContentDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<AssignContent> findAllAssignContent() {
		Criteria criteria = getSession().createCriteria(AssignContent.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	@Override
	public void saveAssignContent(AssignContent assContent) {
		persist(assContent);
	}

	@Override
	public void deleteAssignContent(AssignContent assContent) {
		delete(assContent);
	}


	@Override
	public AssignContent findAssignContentById(Integer assContentId) {
		return (AssignContent) getSession().get(AssignContent.class, assContentId);
	}
}
