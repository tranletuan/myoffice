package com.myoffice.myapp.support;

import java.util.ArrayList;
import java.util.List;

import com.myoffice.myapp.models.dto.Tenure;

public class TenureMenuItem {

	private Tenure tenure;
	
	private List<DocTypeMenuItem> docTypeItemList = new ArrayList<DocTypeMenuItem>();

	private Long count;

	public TenureMenuItem(Tenure tenure, List<DocTypeMenuItem> docTypeItemList, Long count) {
		super();
		this.tenure = tenure;
		this.docTypeItemList = docTypeItemList;
		this.count = count;
	}

	public TenureMenuItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tenure getTenure() {
		return tenure;
	}

	public void setTenure(Tenure tenure) {
		this.tenure = tenure;
	}

	public List<DocTypeMenuItem> getDocTypeItemList() {
		return docTypeItemList;
	}

	public void setDocTypeItemList(List<DocTypeMenuItem> docTypeItemList) {
		this.docTypeItemList = docTypeItemList;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
