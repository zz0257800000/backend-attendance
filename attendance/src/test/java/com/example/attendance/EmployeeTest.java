package com.example.attendance;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.Assert;

import com.example.attendance.entity.Employee;
import com.example.attendance.ifs.EmployeeService;
import com.example.attendance.reposity.EmployeeDao;
@SpringBootTest

public class EmployeeTest {
	
	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeDao employeeDao;
	// only for initial 
	@Test
	public void createAdminTest(){
		 BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		 Employee employee = employeeDao.save(new Employee("zz025784", "Liao", encoder.encode("123"),
			        "admin@G", "0933433771", LocalDate.of(2000, 1, 1), LocalDate.now(),
			        "工程部", "职位", LocalDate.now(), "离职原因",
			        true, 10, 5));
				Assert.isTrue(employee !=null, "Create admin error");

	} 
	
	
}
