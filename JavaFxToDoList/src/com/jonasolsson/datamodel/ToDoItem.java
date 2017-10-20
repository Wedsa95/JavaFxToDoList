package com.jonasolsson.datamodel;

import java.time.LocalDate;

public class ToDoItem {
	
	private String shortDesc;
	private String details;
	private LocalDate dueDate;
	
	public ToDoItem(String shortDesc, String details, LocalDate dueDate) {
		this.shortDesc = shortDesc;
		this.details = details;
		this.dueDate = dueDate;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
}
