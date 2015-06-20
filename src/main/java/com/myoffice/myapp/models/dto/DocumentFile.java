package com.myoffice.myapp.models.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "document_file")
public class DocumentFile {

	@Id
	@Column(name = "file_id", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer fileId;
	
	@Column(name = "file_path", nullable = false)
	private String filePath;
	
	@Column(name = "file_name", nullable = false, unique = true)
	private String fileName;
	
	@Column(name = "version", nullable = false)
	private Integer version = 1;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "document_id", nullable = false)
	private Document document;
	
	public DocumentFile() {
		super();
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getFileId() {
		return fileId;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}
	
	
}
