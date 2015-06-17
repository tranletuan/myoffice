package com.myoffice.myapp.models.dao.candidate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Candidate> findCandidatesBy(boolean completed) {
		Query query = (Query)getSession().createQuery("from Candidate where completed=?");
		query.setParameter(0, completed);
		return query.list();
	}

	@Override
	public Candidate findCandidateById(Integer candidateId) {
		return (Candidate)getSession().get(Candidate.class, candidateId);
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
