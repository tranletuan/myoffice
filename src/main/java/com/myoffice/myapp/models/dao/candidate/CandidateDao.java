package com.myoffice.myapp.models.dao.candidate;

import java.text.ParseException;
import java.util.List;

import com.myoffice.myapp.models.dto.Candidate;

public interface CandidateDao {

	List<Candidate> findAllCandidate();
	
	Candidate findCandidateById(Integer canId);
	
	void saveCandidate(Candidate candidate);
	
	void deleteCandidate(Candidate candidate);
	
	List<Candidate> findCandidateBy(int month, int year);
	
	List<Candidate> findCandidateBy(int startDay, int endDay, int month, int year) throws ParseException;
}
