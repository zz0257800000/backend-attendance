package com.example.attendance.ifs;

import javax.servlet.http.HttpSession;

import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;

public interface EmployeeService {

	public BasicRes create(EmployeeCreateReq req);

	public BasicRes login(String id, String pwd, HttpSession session);

	public BasicRes changePassword(String id, String oldPwd, String newPwd);

	public BasicRes forgotPassword(String id, String email);

	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd);

	public BasicRes activate(String executorId, String employeeId);

	public BasicRes deactivate(String executorId, String employeeId);
	//同時可以達到activate  和 deactivate的效用
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive);
	
	public BasicRes resign( String executorId, String employeeId);
}
