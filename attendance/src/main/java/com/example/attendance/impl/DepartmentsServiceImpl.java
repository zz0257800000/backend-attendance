package com.example.attendance.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;
import com.example.attendance.ifs.DepartmentsService;
import com.example.attendance.reposity.DepartmentsDao;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.BasicRes;
import org.springframework.util.StringUtils;

@Service

public class DepartmentsServiceImpl implements DepartmentsService {
	@Autowired
	private DepartmentsDao dao;

	@Override
	public BasicRes create(DepartmentsCreateReq req) {
		if (CollectionUtils.isEmpty(req.getDepList())) {
			return new BasicRes(RtnCode.PARAM_ERROR);

		}
		List<String> idList = new ArrayList<>();
		for (Departments item : req.getDepList()) {

			if (!StringUtils.hasText(item.getId()) || !StringUtils.hasText(item.getName())) {
				return new BasicRes(RtnCode.PARAM_ERROR);

			}
			idList.add(item.getId());
		}
		if (dao.existsByIdIn(idList)) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);

		}
		dao.saveAll(req.getDepList());
		return new BasicRes(RtnCode.SUCCESSFUL);

	}

}
