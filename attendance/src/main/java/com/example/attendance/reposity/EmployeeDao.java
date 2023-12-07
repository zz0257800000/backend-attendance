package com.example.attendance.reposity;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.attendance.entity.Employee;



public interface EmployeeDao extends JpaRepository<Employee, String> {

	
	public Employee findByEmail(String email);
	
	public boolean existsByEmail(String email);

	public boolean existsByIdAndActive(String id,boolean isActive);

	//清除持久化上下文,清除資料
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query(value = "update Employee set active = :inputActive where id = :inputId")
	public int updateActive(@Param("inputId")String employeeId,@Param("inputActive")boolean active);
	
}
