package com.example.attendance.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.ifs.EmployeeService;
import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.ChangePasswordReq;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.ForgotPasswordReq;
import com.example.attendance.vo.LoginReq;

@RestController
public class EmployeeeController {

	@Autowired
	private EmployeeService employeeservice;

	@GetMapping(value = "api/attendance/login")
	public BasicRes login(@RequestBody LoginReq req, HttpSession session) {
		if (session.getAttribute(req.getId()) == null) {
			return employeeservice.login(req.getId(), req.getPwd(), session);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

//	//如果參數不一樣在改
//	@GetMapping(value = "api/attendance/login")
//    public BasicRes login1(
//    		@RequestParam(value = "id") String id,
//    		@RequestParam(value = "password") String pwd,
//    		HttpSession session) 
//	
//	{
//    	if(session.getAttribute("id") == null) {
//    	return employeeservice.login(id, pwd, session);
//    	
//    }
//	return new BasicRes(RtnCode.SUCCESSFUL);
//}
	// 需要把原本存的東西清掉
	@GetMapping(value = "api/attendance/logout")
	public BasicRes logout(HttpSession session) {
		session.invalidate();
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@PostMapping(value = "api/attendance/employee/create")

	public BasicRes create(@RequestBody EmployeeCreateReq req, HttpSession session) {
		// 只允許一個超級管理員
		if (session.getAttribute(req.getCreatorId()) == null) {
			return new BasicRes(RtnCode.PLEASE_lOGIN_FIRST);

		}
		// 權限
		if (!session.getAttribute(req.getCreatorId()).toString().equalsIgnoreCase("admin")) {

			return new BasicRes(RtnCode.UNAUTHORIZATED);

		}
		return employeeservice.create(req);
	}

	@PostMapping(value = "api/attendance/employee/change_password")

	public BasicRes changePassword(@RequestBody ChangePasswordReq req, HttpSession session) {
		if (session.getAttribute(req.getId()) == null) {
			return new BasicRes(RtnCode.PLEASE_lOGIN_FIRST);

		}
		return employeeservice.changePassword(req.getId(), req.getOldPwd(), req.getNewPwd());
	}

	@PostMapping(value = "api/attendance/employee/forgot_password")

	public BasicRes forgotPassword(@RequestBody ForgotPasswordReq req) {

		return employeeservice.forgotPassword(req.getId(), req.getEmail());

	}

	@PostMapping(value = "api/attendance/employee/change_password_by_auth_code")

	public BasicRes changePasswordByAuthCode(@RequestBody ChangePasswordReq req) {

		return employeeservice.changePasswordByAuthCode(req.getId(), req.getAuthCode(), req.getNewPwd());

	}

}
