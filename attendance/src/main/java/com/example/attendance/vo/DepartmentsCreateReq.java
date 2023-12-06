package com.example.attendance.vo;

import java.util.List;

import com.example.attendance.entity.Departments;

public class DepartmentsCreateReq extends Departments{

	
	private List<Departments> depList;

	


	public DepartmentsCreateReq(List<Departments> depList) {
		super();
		this.depList = depList;
	}


	public List<Departments> getDepList() {
		return depList;
	}

	public void setDepList(List<Departments> depList) {
		this.depList = depList;
	}
	
	
}
