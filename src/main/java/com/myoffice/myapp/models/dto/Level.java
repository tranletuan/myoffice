package com.myoffice.myapp.models.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "level")
public class Level {

	@Id
	@Column(name = "level_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer levelId;
	
	@Column(name = "level_name", nullable = false, unique = true)
	private String levelName;
	
	@Column(name = "short_name", nullable = false)
	private String shortName;
	
	@Column(name = "value", nullable = false)
	private int value;
	
	public Level() {
		super();
	}

	
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLevelName() {
		return levelName;
	}


	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}


	public Integer getLevelId() {
		return levelId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
