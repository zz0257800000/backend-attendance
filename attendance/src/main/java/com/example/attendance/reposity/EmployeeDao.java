package com.example.attendance.reposity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.entity.Employee;



public interface EmployeeDao extends JpaRepository<Employee, String> {

	
	public Employee findByEmail(String email);
}
