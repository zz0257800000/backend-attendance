package com.example.attendance.reposity;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;

import com.example.attendance.entity.ResignApplication;
@Repository
public interface ResignApplicationDao extends JpaRepository<ResignApplication, Integer> {
	
public ResignApplication findByEmployeeId(String employeeId);

}
