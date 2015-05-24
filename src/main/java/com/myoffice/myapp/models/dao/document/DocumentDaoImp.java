package com.myoffice.myapp.models.dao.document;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dao.parameter.ParameterDao;
import com.myoffice.myapp.models.dto.Document;
import com.myoffice.myapp.models.dto.DocumentType;
import com.myoffice.myapp.models.dto.EmergencyLevel;
import com.myoffice.myapp.models.dto.Parameter;
import com.myoffice.myapp.models.dto.PrivacyLevel;

@Repository
public class DocumentDaoImp extends AbstractDao  implements DocumentDao {

	@Override
	public void rollBackDocument() {
		rollBack();
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
	public List<Document> findDocumentBy(DocumentType docType,
			boolean completed, boolean incomming) {
		Query query = (Query)getSession().createQuery("from Document where type_id=? and "
				+ "completed=? and incomming=?");
		query.setParameter(0, docType.getTypeId());
		query.setParameter(1, completed);
		query.setParameter(2, incomming);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DocumentType> findAllDocType() {
		Criteria criteria = getSession().createCriteria(DocumentType.class);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmergencyLevel> findAllEmergencyLevel() {
		Criteria criteria = getSession().createCriteria(EmergencyLevel.class);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PrivacyLevel> findAllPrivacyLevel() {
		Criteria criteria = getSession().createCriteria(PrivacyLevel.class);
		return criteria.list();
	}

	
	

}
