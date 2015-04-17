package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="levels")
public class Level {

	@Id
	@Column(name="level_id", unique=true, nullable=false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer levelId;
	
	@Column(name="level_name", unique=true, nullable=false)
	private String levelName;
	
	@Column(name = "description")
	private String description;

	public Level(Integer levelId, String levelName, String description) {
		this.levelId = levelId;
		this.levelName = levelName;
		this.description = description;
	}

	public Level(){
		
	}
	
	public Level(String levelName, String description) {
		this.levelName = levelName;
		this.description = description;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevelId() {
		return levelId;
	}
	
	
}
