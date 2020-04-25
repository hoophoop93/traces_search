package com.softcomputer.genetrace.tracessearchwebapp.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "settings")
public class Settings {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsettings")
    private int idSettings;
	
	@NotEmpty
	@Size(max = 150)
	@Column(name = "pathoffset")
	private String pathoffset;
	
	@NotNull
	@Column(name = "linesafter") 
	private int linesAfter;
	
	@NotNull
	@Column(name = "linesbefore")
	private int linesBefore;

	public String getPathoffset() {
		return pathoffset;
	}

	public void setPathoffset(String pathoffset) {
		this.pathoffset = pathoffset;
	}

	public int getLinesAfter() {
		return linesAfter;
	}

	public void setLinesAfter(int linesAfter) {
		this.linesAfter = linesAfter;
	}

	public int getLinesBefore() {
		return linesBefore;
	}

	public void setLinesBefore(int linesBefore) {
		this.linesBefore = linesBefore;
	}

	public int getIdSettings() {
		return idSettings;
	}

	public void setIdSettings(int idSettings) {
		this.idSettings = idSettings;
	}
	public String toString() {
		return pathoffset +" "+linesAfter+" "+linesBefore;
	}
	
}
