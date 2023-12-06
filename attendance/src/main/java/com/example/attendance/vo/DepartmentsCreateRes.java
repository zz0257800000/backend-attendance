package com.example.attendance.vo;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;

public class DepartmentsCreateRes extends Departments{

	private RtnCode rtnCode;

	public DepartmentsCreateRes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DepartmentsCreateRes(RtnCode rtnCode) {
		super();
		this.rtnCode = rtnCode;
	}

	public RtnCode getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(RtnCode rtnCode) {
		this.rtnCode = rtnCode;
	}

	
	
}
