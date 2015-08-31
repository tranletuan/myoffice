package com.myoffice.myapp.support;

public class JSonRow {

	private Integer id;
	private String fullName;
	private String level;
	private Integer countTask = 0;
	private Integer countLate = 0;
	private Integer countCompleted = 0;
	
	public Integer getId() {
		return id;
	}
	public String getFullName() {
		return fullName;
	}
	public String getLevel() {
		return level;
	}
	
	public Integer getCountTask() {
		return countTask;
	}
	public Integer getCountLate() {
		return countLate;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public void setLevel(String level) {
		this.level = level;
	}

	public void setCountTask(Integer countTask) {
		this.countTask = countTask;
	}
	public void setCountLate(Integer countLate) {
		this.countLate = countLate;
	}

	public Integer getCountCompleted() {
		return countCompleted;
	}
	public void setCountCompleted(Integer countCompleted) {
		this.countCompleted = countCompleted;
	}
	public void increaseCountTask(){
		this.countTask++;
	}
	public void increaseCountLate(){
		this.countLate++;
	}
	public void increaseCountCompleted(){
		this.countCompleted++;
	}
	
}
