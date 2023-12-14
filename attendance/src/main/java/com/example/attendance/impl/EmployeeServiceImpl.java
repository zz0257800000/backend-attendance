package com.example.attendance.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.JobPosition;
import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.AuthCode;
import com.example.attendance.entity.Employee;
import com.example.attendance.entity.ResignApplication;
import com.example.attendance.ifs.EmployeeService;
import com.example.attendance.reposity.AuthCodeDao;
import com.example.attendance.reposity.DepartmentsDao;
import com.example.attendance.reposity.EmployeeDao;
import com.example.attendance.reposity.ResignApplicationDao;
import com.example.attendance.vo.EmployeeCreateReq;
import com.example.attendance.vo.EmployeeRes;

import net.bytebuddy.utility.RandomString;

import com.example.attendance.vo.BasicRes;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Value("${authcode.expired.time}")
	private int authCodeExpiredTime;
	@Autowired
	private EmployeeDao dao;

	@Autowired
	private DepartmentsDao departmentsDao;

	@Autowired
	private AuthCodeDao authCodeDao;
	@Autowired
	private ResignApplicationDao resignApplicationDao;

	@Override
	public BasicRes create(EmployeeCreateReq req) {
		if (!StringUtils.hasText(req.getId()) 
				|| !StringUtils.hasText(req.getName()) || !StringUtils.hasText(req.getPwd())
				|| !StringUtils.hasText(req.getEmail()) 
				) {
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
		if (!employee.isActive()) {
			return new BasicRes(RtnCode.ACCOUNT_DEACTIVATE);

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
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		if (oldPwd.equals(newPwd)) {

			return new BasicRes(RtnCode.OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL);

		}

		// �S�g
		Employee employee = dao.findById(id).get();
		if (!encoder.matches(oldPwd, employee.getPwd())) {
			{
				return new BasicRes(RtnCode.PASSWORD_ERROR);

			}
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
			// �]�w30����
			authCodeDao.save(new AuthCode(employee.getId(), authCode, now.plusMinutes(authCodeExpiredTime)));

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.FORGET_PASSWORD_ERROR);

		}
		// ����email�ñH�eauth_Code
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes changePasswordByAuthCode(String id, String authCode, String newPwd) {
		if (!StringUtils.hasText(id) || !StringUtils.hasText(authCode) || !StringUtils.hasText(newPwd)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		Optional<AuthCode> op = authCodeDao.findById(id);

		if (op.isEmpty()) {
			return new BasicRes(RtnCode.ID_NOT_FOUND);

		}
		AuthCode authCodeEntity = op.get();
		if (!authCodeEntity.getAuthCode().equals(authCode)) {
			return new BasicRes(RtnCode.AUTH_CODE_NOT_MATCH);

		}

		LocalDateTime now = LocalDateTime.now();
		// ��b�o�Ӯɶ�����
		if (now.isAfter(authCodeEntity.getAuthDatetime())) {
			return new BasicRes(RtnCode.AUTH_CODE_EXPIRED);

		}

		Employee employee = dao.findById(id).get();
		employee.setPwd(encoder.encode(newPwd));
		try {
			dao.save(employee);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.CHANGE_PASSWORD_ERROR);

		}
		// check auth code
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// ����k���,�]����updateActivate���N
	@Deprecated
	@Override
	public BasicRes activate(String executorId, String employeeId) {
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		Employee executor = dao.findById(executorId).get();

		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		if (dao.updateActive(employeeId, true) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	// ����k���,�]����updateActivate���N
	@Deprecated
	@Override
	public BasicRes deactivate(String executorId, String employeeId) {

		if (!StringUtils.hasText(executorId) || !StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		Employee executor = dao.findById(executorId).get();

		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}

		if (dao.updateActive(employeeId, false) != 1) {
			return new BasicRes(RtnCode.UPDATE_FAILED);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateActivate(String executorId, String employeeId, boolean isActive) {
		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		// �P�_�O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_

		Employee executor = dao.findById(executorId).get();

		if (!executor.getDepartment().equalsIgnoreCase("ADMIN") && !executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		try {
			int res = dao.updateActive(employeeId, isActive);
			if (res != 1) {
				return new BasicRes(RtnCode.UPDATE_FAILED);

			}
		} catch (Exception e) {

			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);
		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateResign(String executorId, String employeeId) {
		// ���ΧP�_employeeId �O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_

		if (!StringUtils.hasText(employeeId) || executorId.equals(employeeId)) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		Employee executor = dao.findById(executorId).get();

		if (executor.getDepartment().equalsIgnoreCase("HR")) {
			return new BasicRes(RtnCode.UNAUTHORIZATED);
		}
		ResignApplication application = resignApplicationDao.findByEmployeeId(employeeId);

		Employee employee = dao.findById(employeeId).get();
		employee.setResignationDate(application.getResignationDate());
		employee.setQuitReason(application.getQuitReason());

		try {
			dao.save(employee);

		} catch (Exception e) {

			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);

		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes resignApplication(String employeeId) {
		// ���ΧP�_employeeId �O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_

		Employee employee = dao.findById(employeeId).get();
		try {
			resignApplicationDao.save(new ResignApplication(employeeId, employee.getDepartment(),
					LocalDate.now().plusMonths(1), "�ɵ����Q�F�F!!!", false, false));
		}

		catch (Exception e) {
			logger.error(e.getMessage());

			return new BasicRes(RtnCode.UPDATE_ERROR);
		}
		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public BasicRes updateInfo(String executorId, Employee employee) {
		// ���ΧP�_employeeId �O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_

		if (!StringUtils.hasText(employee.getId()) || executorId.equals(employee.getId())) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		try {
			int res = dao.updateInfo(employee.getId(), employee.getDepartment(), employee.getName(),
					employee.getEmail(), employee.getJobPosition(), employee.getBirthDate(), employee.getArrivalDate(),
					employee.getAnnualLeave(), employee.getSickLeave());
			if (res != 1) {

				return new BasicRes(RtnCode.UPDATE_FAILED);

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new BasicRes(RtnCode.UPDATE_ERROR);

		}

		return new BasicRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public EmployeeRes findByEmployeeId(String employeeId) {
		// ���ΧP�_employeeId �O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_
		Employee employee = dao.findById(employeeId).get();

		return new EmployeeRes(RtnCode.SUCCESSFUL, employee);

	}

	@Override
	public EmployeeRes findStaffInfo(String callerId, String targetId) {
		// ���ΧP�_callerId �O�_����,��k�ݭnlogin�~��ϥ�,login��k�w���P�_

		if (!StringUtils.hasText(targetId)) {

			return new EmployeeRes(RtnCode.PARAM_ERROR, null);

		}
		Optional<Employee> op = dao.findById(callerId);
		if (op.isEmpty()) {

			return new EmployeeRes(RtnCode.ID_NOT_FOUND, null);

		}
		Employee targetInfo = op.get();
		Employee callerInfo = dao.findById(callerId).get();

		String callerDepartment = callerInfo.getDepartment();
		
		//1.�P�����Bcaller  �O���D��2.caller�OHR����
		if ((callerDepartment.equals(targetInfo.getDepartment()) 
				&& JobPosition.hasReviewPermission(callerInfo.getJobPosition()))
				|| callerDepartment.equalsIgnoreCase("HR")) {
			return new EmployeeRes(RtnCode.SUCCESSFUL,targetInfo);

		}
		return new EmployeeRes(RtnCode.UNAUTHORIZATED,null);
	}

}