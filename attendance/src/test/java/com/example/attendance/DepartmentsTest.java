package com.example.attendance;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.attendance.entity.Departments;
import com.example.attendance.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.BasicRes;
@SpringBootTest
public class DepartmentsTest {
	
	@Autowired
	private DepartmentsService service;
	// only for initial 
	@Test
	public void adminDepTest() {
		Departments dep = new Departments("99","ADMIN");
		DepartmentsCreateReq req = new DepartmentsCreateReq(Arrays.asList(dep));
				BasicRes res = service.create(req);
				Assert.isTrue(res.getRtnCode().getCode() == 200, "create error");

	}
}
