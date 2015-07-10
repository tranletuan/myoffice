package com.myoffice.myapp.models.dao.document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentFile;
import com.myoffice.myapp.models.dto.DocumentRecipient;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.PrivacyLevel;
import com.myoffice.myapp.models.dto.Tenure;
import com.myoffice.myapp.support.DocTypeWait;

@Repository
public class DocumentDaoImp extends AbstractDao implements DocumentDao {

	private static final Logger logger = LoggerFactory
			.getLogger(DocumentDaoImp.class);

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
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findDocumentBy(Integer organId, Integer tenureId,
			Integer docTypeId, int completed, int firstResult, int maxResult, int enabled) {
		Criteria criteria = getSession().createCriteria(Document.class);
		criteria.createAlias("organ", "o");
		criteria.createAlias("tenure", "t");
		criteria.createAlias("docType", "dt");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.eq("t.tenureId", tenureId)));
		criteria.add(Restrictions.and(Restrictions.eq("dt.docTypeId", docTypeId)));	
		
		if(completed != -1) {
			if(completed == 1) {
				criteria.add(Restrictions.and(Restrictions.eq("completed", true)));
			} else {
				Criterion rest1 = Restrictions.eq("completed", false);
				Criterion rest2 = Restrictions.eq("sended", false);
				criteria.add(Restrictions.and(Restrictions.or(rest1, rest2)));
			}
		}
		
		if(enabled != -1){
			boolean value = enabled == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("enabled", value)));
		}
		criteria.addOrder(Order.desc("docId"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findDocumentBy(Integer organId, int completed, int firstResult, int maxResult, int enabled) {
		Criteria criteria = getSession().createCriteria(Document.class);
		criteria.createAlias("organ", "o");
		criteria.add(Restrictions.eq("o.organId", organId));
	
		if(completed != -1) {
			if(completed == 1) {
				criteria.add(Restrictions.and(Restrictions.eq("completed", true)));
			} else {
				Criterion rest1 = Restrictions.eq("completed", false);
				Criterion rest2 = Restrictions.eq("sended", false);
				criteria.add(Restrictions.and(Restrictions.or(rest1, rest2)));
			}
		}
		
		if(enabled != -1){
			boolean value = enabled == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("enabled", value)));
		}
		
		criteria.addOrder(Order.desc("docId"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> findDocumentBy(Integer organId, Integer docTypeId, int completed, int firstResult,
			int maxResult, int enabled) {
		Criteria criteria = getSession().createCriteria(Document.class);
		criteria.createAlias("organ", "o");
		criteria.createAlias("tenure", "t");
		criteria.createAlias("docType", "dt");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.eq("dt.docTypeId", docTypeId)));	
		
		if(completed != -1) {
			if(completed == 1) {
				criteria.add(Restrictions.and(Restrictions.eq("completed", true)));
			} else {
				Criterion rest1 = Restrictions.eq("completed", false);
				Criterion rest2 = Restrictions.eq("sended", false);
				criteria.add(Restrictions.and(Restrictions.or(rest1, rest2)));
			}
		}
		
		if(enabled != -1){
			boolean value = enabled == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("enabled", value)));
		}
		criteria.addOrder(Order.desc("docId"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	//DocumentType
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
		criteria.addOrder(Order.desc("tenureId"));
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
	
	@Override
	public Tenure findLastTenure() {
		Criteria criteria = getSession().createCriteria(Tenure.class);
		criteria.addOrder(Order.desc("tenureId"));
		criteria.setMaxResults(1);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return (Tenure) criteria.uniqueResult();
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
	public DocumentFile findNewestDocFile(Integer docId) {
		Criteria criteria = getSession().createCriteria(DocumentFile.class);
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("d.docId", docId));
		criteria.addOrder(Order.desc("version"));
		criteria.setMaxResults(1);
		DocumentFile file = (DocumentFile) criteria.uniqueResult();
		if(file != null){
			return file;
		}
		
		return null;
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
		persist(docRec);
	}

	@Override
	public DocumentRecipient findDocRecipient(Integer docId, Integer organId) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("document", "d");
		criteria.createAlias("organ", "o");
		criteria.add(Restrictions.eq("d.docId", docId)).add(
				Restrictions.and(Restrictions.eq("o.organId", organId)));
		return (DocumentRecipient) criteria.uniqueResult(); 
	}

	@Override
	public Integer findMaxDocRecNumber(Integer tenureId, Integer organId) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("document", "d");
		criteria.createAlias("organ", "o");
		criteria.add(Restrictions.eq("d.tenure.tenureId", tenureId));
		criteria.add(Restrictions.and(Restrictions.eq("o.organId", organId)));
		criteria.addOrder(Order.desc("number"));
		criteria.setMaxResults(1);
		DocumentRecipient docRec = (DocumentRecipient)criteria.uniqueResult();
		if(docRec.getNumber() == null) return 1;
		return (docRec.getNumber() + 1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentRecipient> findDocRecipient(Integer organId,
			Integer tenureId, Integer docTypeId, int completed, int firstResult, int maxResult) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("organ", "o");
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.eq("d.tenure.tenureId", tenureId)));
		criteria.add(Restrictions.and(Restrictions.eq("d.docType.docTypeId", docTypeId)));
		criteria.add(Restrictions.and(Restrictions.eq("d.enabled", true)));
		
		if(completed != -1) {
			boolean value = completed == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("completed", value)));
		}
		
		criteria.addOrder(Order.desc("receiveTime"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentRecipient> findDocRecipient(Integer organId, int completed, 
			int firstResult, int maxResult) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("organ", "o");
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.eq("d.enabled", true)));
		
		if(completed != -1) {
			boolean value = completed == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("completed", value)));
		}
		
		criteria.addOrder(Order.desc("receiveTime"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentRecipient> findDocRecipient(Integer organId, int docTypeId, int completed, int firstResult,
			int maxResult) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("organ", "o");
		criteria.createAlias("document", "d");
		criteria.add(Restrictions.eq("o.organId", organId));
		criteria.add(Restrictions.and(Restrictions.eq("d.enabled", true)));
		criteria.add(Restrictions.and(Restrictions.eq("d.docType.docTypeId", docTypeId)));
		
		if(completed != -1) {
			boolean value = completed == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("completed", value)));
		}
		
		criteria.addOrder(Order.desc("receiveTime"));
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResult);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentRecipient> findDocRecByAssignDate(Integer organId,
			int completed, Date start, Date end) {
		Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
		criteria.createAlias("candidate", "c");
		criteria.createAlias("organ", "o");
		Criterion rest1 = Restrictions.and(Restrictions.ge("c.timeStart", start), Restrictions.le("c.timeStart", end));
		Criterion rest2 = Restrictions.and(Restrictions.ge("c.timeEnd", start), Restrictions.le("c.timeEnd", end));
		criteria.add(Restrictions.or(rest1, rest2));
		criteria.add(Restrictions.and(Restrictions.eq("o.organId", organId)));
		if(completed != -1) {
			boolean value = completed == 1? true : false;
			criteria.add(Restrictions.and(Restrictions.eq("completed", value)));
		}
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		
		return criteria.list();
	}
	
	//WAITING DOC
	//OUT	
	@Override
	public List<DocTypeWait> findMenuDocOut(Integer organId) {
		List<DocumentType> docTypeList = findAllDocType();
		List<DocTypeWait> docTypeOutList = new ArrayList<DocTypeWait>();
		
		for(DocumentType docType : docTypeList){
			Criteria criteria = getSession().createCriteria(Document.class);
			criteria.createAlias("organ", "o");
			criteria.createAlias("docType", "dt");
			
			Criterion rest1 = Restrictions.eq("o.organId", organId);
			Criterion rest2 = Restrictions.eq("dt.docTypeId", docType.getDocTypeId());
			Criterion rest3 = Restrictions.eq("completed", false);
			Criterion rest4 = Restrictions.eq("sended", false);
			Criterion rest5 = Restrictions.eq("enabled", true);
			
			criteria.add(Restrictions.and(rest1, rest2, Restrictions.or(rest3, rest4), rest5));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			criteria.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			docTypeOutList.add(new DocTypeWait(docType, count));
		}
		
		return docTypeOutList;
	}

	//IN
	@Override
	public List<DocTypeWait> findMenuDocIn(Integer organId) {
		List<DocumentType> docTypeList = findAllDocType();
		List<DocTypeWait> docTypeInList = new ArrayList<DocTypeWait>();
		
		for(DocumentType docType : docTypeList) {
			Criteria criteria = getSession().createCriteria(DocumentRecipient.class);
			criteria.createAlias("document", "d");
			criteria.createAlias("organ", "o");
			
			Criterion rest1 = Restrictions.eq("o.organId", organId);
			Criterion rest2 = Restrictions.eq("d.docType.docTypeId", docType.getDocTypeId());
			Criterion rest3 = Restrictions.eq("completed", false);
			Criterion rest4 = Restrictions.eq("d.completed", true);
			Criterion rest5 = Restrictions.eq("d.enabled", true);
			
			criteria.add(Restrictions.and(rest1, rest2, rest3, rest4, rest5));
			criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			criteria.setProjection(Projections.rowCount());
			Long count = (Long) criteria.uniqueResult();
			docTypeInList.add(new DocTypeWait(docType, count));
		}
		
		return docTypeInList;
	}
	
	 
	
	
}
