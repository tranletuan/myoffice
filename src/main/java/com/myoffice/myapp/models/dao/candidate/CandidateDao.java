package com.myoffice.myapp.models.dao.candidate;

import java.util.List;

import com.myoffice.myapp.models.dto.Candidate;

public interface CandidateDao {

	List<Candidate> findAllCandidate();
	
	Candidate findCandidateById(Integer canId);
	
	void saveCandidate(Candidate candidate);
	
	void deleteCandidate(Candidate candidate);
}
