package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "code")
	private Integer code;

	@Column(name = "name")
	private String name;

	@Column(name = "title")
	private String title;

	@Column(name = "date")
	private Date date;

	@Column(name = "completed")
	private Boolean completed;

	@Column(name = "shared")
	private Integer shared;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = dateFormat.format(this.date);
		return strDate;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}

	public Integer getShared() {
		return shared;
	}

	public void setShared(Integer shared) {
		this.shared = shared;
	}

	public Task(Integer code, String name, String title, Date date, Boolean completed, Integer shared) {
		this(name, title, date, completed, shared);
		this.code = code;
	}

	public Task(String name, String title, Date date, Boolean completed, Integer shared) {
		this.name = name;
		this.title = title;
		this.completed = completed;
		this.shared = shared;
		this.date = date;
	}

	public Task() {
	}
}
