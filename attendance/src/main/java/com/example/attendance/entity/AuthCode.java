package com.example.attendance.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_code")
public class AuthCode {
	@Id
	@Column(name = "employee_if")
	private String employeeIf;
	
	@Column(name = "auth_code")
	private String authCode;

	@Column(name = "auth_datetime")
	private LocalDateTime authDatetime;

	public AuthCode() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthCode(String employeeIf, String authCode, LocalDateTime authDatetime) {
		super();
		this.employeeIf = employeeIf;
		this.authCode = authCode;
		this.authDatetime = authDatetime;
	}

	public String getEmployeeIf() {
		return employeeIf;
	}

	public void setEmployeeIf(String employeeIf) {
		this.employeeIf = employeeIf;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public LocalDateTime getAuthDatetime() {
		return authDatetime;
	}

	public void setAuthDatetime(LocalDateTime authDatetime) {
		this.authDatetime = authDatetime;
	}



	
	
}
