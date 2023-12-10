package com.example.attendance.ifs;

import javax.servlet.http.HttpSession;

import com.example.attendance.entity.Employee;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

public interface EmployeeService {

	public BasicRes create(EmployeeCreateReq req);

	public BasicRes login(String id, String pwd, HttpSession session);

	public BasicRes changePassword(String id, String oldPwd, String newPwd);

	public BasicRes forgotPassword(String id, String email);

	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd);

	public BasicRes activate(String executorId, String employeeId);

	public BasicRes deactivate(String executorId, String employeeId);

	// 同時可以達到activate 和 deactivate的效用
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive);

	public BasicRes updateResign(String executorId, String employeeId);

	public BasicRes resignApplication(String employeeId);

	public BasicRes updateInfo(String executorId, Employee employee);

	public EmployeeRes findByEmployeeId(String employeeId);

	public EmployeeRes findStaffInfo(String callerId,String targetId);

}
