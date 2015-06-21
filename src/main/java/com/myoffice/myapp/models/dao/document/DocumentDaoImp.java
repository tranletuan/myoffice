package com.myoffice.myapp.models.dao.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.engine.impl.HistoryServiceImpl;
import org.apache.ibatis.executor.ReuseExecutor;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dao.parameter.ParameterDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.support.NoteDoctypeInt;
import com.myoffice.myapp.utils.FlowUtil;

@Repository
public class DocumentDaoImp extends AbstractDao implements DocumentDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DocumentDaoImp.class);

	
	@Override
	public Integer countDocument(boolean incoming, boolean completed,
			Integer docTypeId) {
		try {
			if (docTypeId == null || docTypeId <= 0) {
				Query query = (Query) getSession()
						.createQuery(
								"Select count(*) from Document where incoming=? and completed=?");
				query.setParameter(0, incoming);
				query.setParameter(1, completed);
				return Integer.parseInt(query.uniqueResult().toString());
			} else {
				Query query = (Query) getSession()
						.createQuery(
								"Select count(*) from Document where doc_type_id=? and incoming=? and completed=?");
				query.setParameter(0, docTypeId);
				query.setParameter(1, incoming);
				query.setParameter(2, completed);
				return Integer.parseInt(query.uniqueResult().toString());
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return 0;
		}
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Document> findWaitingDocByType(boolean incoming, boolean completed, Integer docTypeId) {
		if (docTypeId == null || docTypeId <= 0) {
			Query query = (Query) getSession().createQuery(
					"from Document where incoming=? and completed=?");
			query.setParameter(0, incoming);
			query.setParameter(1, completed);
			return query.list();
		} else {
			Query query = (Query) getSession()
					.createQuery(
							"from Document where doc_type_id=? and incoming=? and completed=?");
			query.setParameter(0, docTypeId);
			query.setParameter(1, incoming);
			query.setParameter(2, completed);
			return query.list();
		}
	}*/

	@Override
	public Document findDocumentById(Integer docId) {
		return (Document) getSession().get(Document.class, docId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Document findDocumentByName(String docName) {

		Query query = (Query) getSession().createSQLQuery(
				"from Document where document_name=?");
		query.setParameter(0, docName);
		List<Document> docs = query.list();

		if (docs.size() > 0) {
			return docs.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findAllDocument() {
		Criteria criteria = getSession().createCriteria(Document.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (List<Document>) criteria.list();
	}

	@Override
	public void saveDocument(Document doc) {
		persist(doc);
	}

	@Override
	public void deleteDocument(Document doc) {
		delete(doc);
	}

	/*@SuppressWarnings("unchecked")
	@Override
	public List<Document> findDocumentBy(DocumentType docType,
			boolean completed, boolean incomming) {
		Query query = (Query) getSession().createQuery(
				"from Document where type_id=? and "
						+ "completed=? and incomming=?");
		query.setParameter(0, docType.getDocTypeId());
		query.setParameter(1, completed);
		query.setParameter(2, incomming);

		return query.list();
	}*/

	// DocumentType
	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentType> findAllDocType() {
		Criteria criteria = getSession().createCriteria(DocumentType.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public DocumentType findDocTypeById(Integer typeId) {
		return (DocumentType) getSession().get(DocumentType.class, typeId);
	}

	@Override
	public void saveDocType(DocumentType docType) {
		persist(docType);
	}

	
	@Override
	public List<NoteDoctypeInt> findWaitingMenu(boolean incoming) {
		List<NoteDoctypeInt> map = new ArrayList<NoteDoctypeInt>();
		List<DocumentType> listDocType = findAllDocType();
		
		for(int i = 0; i < listDocType.size(); i++){
			DocumentType docType = listDocType.get(i);
			Query query = (Query)getSession().createQuery("Select count(*) from Document where doc_type_id=? and incoming=? and completed=?");
			query.setParameter(0, docType.getDocTypeId());
			query.setParameter(1, incoming);
			query.setParameter(2, false);
			
			map.add(new NoteDoctypeInt(docType, Integer.parseInt(query.uniqueResult().toString())));
		}
		
		return map;
	}

	// ========EMERGENCY LEVEL
	@SuppressWarnings("unchecked")
	@Override
	public List<EmergencyLevel> findAllEmergencyLevel() {
		Criteria criteria = getSession().createCriteria(EmergencyLevel.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public EmergencyLevel findEmergencyLevelById(Integer emergencyLevelId) {
		return (EmergencyLevel) getSession().get(EmergencyLevel.class,
				emergencyLevelId);
	}

	@Override
	public void saveEmergency(EmergencyLevel emergency) {
		persist(emergency);
		
	}

	@Override
	public void deleteEmergency(EmergencyLevel emergency) {
		delete(emergency);
	}

	// =======PRIVACY LEVEL
	@SuppressWarnings("unchecked")
	@Override
	public List<PrivacyLevel> findAllPrivacyLevel() {
		Criteria criteria = getSession().createCriteria(PrivacyLevel.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public PrivacyLevel findPrivacyLevelById(Integer privacyLevelId) {
		return (PrivacyLevel) getSession().get(PrivacyLevel.class,
				privacyLevelId);
	}

	@Override
	public void savePrivacyLevel(PrivacyLevel privacy) {
		persist(privacy);
	}

	@Override
	public void deletePrivacyLevel(PrivacyLevel privacy) {
		delete(privacy);
	}

	//TENURE
	@SuppressWarnings("unchecked")
	@Override
	public List<Tenure> findAllTenure() {
		Criteria criteria = getSession().createCriteria(Tenure.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Tenure findTenureById(Integer tenureId) {
		return (Tenure)getSession().get(Tenure.class, tenureId);
	}

	@Override
	public void saveTenure(Tenure tenure) {
		persist(tenure);
	}

	@Override
	public void deleteTenure(Tenure tenure) {
		delete(tenure);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer findMaxNumber(Integer tenureId, Integer docTypeId,
			Integer organId, boolean incoming) {
		Query query = (Query) getSession()
				.createQuery(
						"SELECT number from Document where tenure_id=? and doc_type_id=? and "
						+ "organ_id=? and incoming=? ORDER BY number DESC");
		query.setParameter(0, tenureId);
		query.setParameter(1, docTypeId);
		query.setParameter(2, organId);
		query.setParameter(3, incoming);
		query.setMaxResults(1);
		List<Integer> numbers = query.list();
		if (query.list().size() > 0) {
			return numbers.get(0) + 1;
		}

		return 1;
	}

	@Override
	public boolean isDocumentExist(Integer organId, String docPath) {
		Query query = (Query)getSession().createQuery("select count(*) from Document where organ_id=? and document_path=?");
		query.setParameter(0, organId);
		query.setParameter(1, docPath);
		int rs = Integer.parseInt(query.uniqueResult().toString());
		if(rs > 0) return true;
		return false;
	}

	@Override
	public Integer findNewestDocFile(Integer docId) {
		Criteria criteria = getSession().createCriteria(DocumentFile.class);
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("d.docId", docId));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
		DocumentFile file = (DocumentFile) criteria.uniqueResult();
		if(file != null){
			return file.getVersion(); 
		}
		
		return 0;
	}

	@Override
	public void saveDocFile(DocumentFile docFile) {
		persist(docFile);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentFile> findAllFile(Integer docId) {
		Criteria criteria = getSession().createCriteria(DocumentFile.class);
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("d.docId", docId));
		criteria.addOrder(Order.desc("version"));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public DocumentFile findDocFileById(Integer docFileId) {
		return (DocumentFile) getSession().get(DocumentFile.class, docFileId);
	}

	@Override
	public void saveDocRecipient(DocumentRecipient docRec) {
		// TODO Auto-generated method stub
		
	}
}
