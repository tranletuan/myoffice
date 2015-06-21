package com.myoffice.myapp.models.dao.candidate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Candidate;

@Repository
public class CandidateDaoImp extends AbstractDao implements CandidateDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Candidate> findAllCandidate() {
		Criteria criteria = getSession().createCriteria(Candidate.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void saveCandidate(Candidate candidate) {
		persist(candidate);
	}

	@Override
	public void deleteCandidate(Candidate candidate) {
		delete(candidate);
	}

	
}
