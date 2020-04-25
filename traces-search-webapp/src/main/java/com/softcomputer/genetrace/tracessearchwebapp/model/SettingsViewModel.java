package com.softcomputer.genetrace.tracessearchwebapp.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SettingsViewModel {

	@NotBlank(message = "{NotEmpty.message}")
	@Size(max = 150, message = "{Size.name2}")
	private String pathoffset;
	
	@Min(1)
	@Max(1000)
	@NotBlank(message = "{NotEmpty.message}")
	private String linesBefore;

	@Min(1)
	@Max(1000)
	@NotBlank(message = "{NotEmpty.message}")
	private String linesAfter;
 
	
	public String getPathoffset() {
		return pathoffset;
	}

	public void setPathoffset(String pathoffset) {
		this.pathoffset = pathoffset;
	}

	public String getLinesBefore() {
		return linesBefore;
	}

	public void setLinesBefore(String linesBefore) {
		this.linesBefore = linesBefore;
	}

	public String getLinesAfter() {
		return linesAfter;
	}

	public void setLinesAfter(String linesAfter) {
		this.linesAfter = linesAfter;
	}

	
	
	
	
}
