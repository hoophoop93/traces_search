package com.softcomputer.genetrace.tracessearchwebapp.model;

import javax.validation.constraints.*;

public class LogsPanelViewModel {

	@NotBlank(message = "{NotEmpty.message}")
	@Size(max = 100, message = "{Size.name}")
	private String phrase;
	
	private Boolean splitFlag;

	private String dateStart;
	private String dateEnd;

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public String getDateStart() {
		return dateStart;
	}

	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}

	public String getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Boolean getSplitFlag() {
		return splitFlag;
	}

	public void setSplitFlag(Boolean splitFlag) {
		this.splitFlag = splitFlag;
	}

	
	
}
