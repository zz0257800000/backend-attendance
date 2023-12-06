package com.example.attendance.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.ifs.EmployeeService;
import com.example.attendance.reposity.AuthCodeDao;
import com.example.attendance.reposity.DepartmentsDao;
import com.example.attendance.reposity.EmployeeDao;
import com.example.attendance.vo.EmployeeCreateReq;

import net.bytebuddy.utility.RandomString;

import com.example.attendance.vo.BasicRes;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private EmployeeDao dao;

	@Autowired
	private DepartmentsDao departmentsDao;
	
	@Autowired
	private AuthCodeDao authCodeDao;

	@Override
	public BasicRes create(EmployeeCreateReq req) {
		if (!StringUtils.hasText(req.getId()) || !StringUtils.hasText(req.getDepartment())
				|| !StringUtils.hasText(req.getName()) || !StringUtils.hasText(req.getPwd())
				|| !StringUtils.hasText(req.getEmail()) || !StringUtils.hasText(req.getJobPosition())
				|| req.getBirthDate() == null || req.getBirthDate() == null) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		if (dao.existsById(req.getId())) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);

		}
		// check deparement_name
		if (departmentsDao.existsByName(req.getDepartment())) {
			return new BasicRes(RtnCode.DEPARTMENT_NOT_FOUND);

		}
		req.setPwd(encoder.encode(req.getPwd()));
		try {
			dao.save((Employee) req);

		} catch (Exception e) {

			logger.error(e.getMessage());
			return new BasicRes(RtnCode.EMPLOYEE_CREATE_ERROR);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public BasicRes login(String id, String pwd, HttpSession session) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(pwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}

		Optional<Employee> op = dao.findById(id);
		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);

		}

		Employee employee = op.get();
		if (!encoder.matches(pwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);

		}
		session.setAttribute(id, id);
		// ��������
		session.setMaxInactiveInterval(300);
		logger.info("loging successful!!");
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePassword(String id, String oldPwd, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {

		}
		if (oldPwd.equals(newPwd)) {

			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);

		}
		Optional<Employee> op = dao.findById(id);
		if (op.isEmpty()) {
			{

			}
		}

		Employee employee = op.get();
		if (!encoder.matches(oldPwd, employee.getPwd())) {
			return new BasicRes(RtnCode.PASSWORD_ERROR);

		}

		employee.setPwd(encoder.encode(newPwd));
		try {
			dao.save(employee);

		} catch (Exception e) {

			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);

		}

		return new BasicRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public BasicRes forgotPassword(String id, String email) {
		if (!StringUtils.hasText(id) && !StringUtils.hasText(email)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		Employee employee = null;
		if (StringUtils.hasText(id)) {
			Optional<Employee> op = dao.findById(id);
			if (op.isEmpty()) {

				return new BasicRes(RtnCode.ID_NOT_FOUND);

			}
			employee = op.get();

		} else {
			employee = dao.findByEmail(email);
			if (employee == null) {
				return new BasicRes(RtnCode.ID_NOT_FOUND);
			}
		}
		String randomPwd = RandomString.make(12);
		employee.setPwd(encoder.encode(randomPwd));
		
		// �������ҽX,����30��
		String authCode = RandomString.make(6);
		
		LocalDateTime now = LocalDateTime.now();
		try {
			dao.save(employee);
			authCodeDao.save(new AuthCode(employee.getId(),authCode,now));

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGET_PASSWORD_ERROR);

		}
		// ����email�ñH�eauth_Code
		return new BasicRes(RtnCode.SUCCESSFUL);
	}
}