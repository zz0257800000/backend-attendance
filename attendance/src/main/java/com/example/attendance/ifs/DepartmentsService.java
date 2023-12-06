package com.example.attendance.ifs;

import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.BasicRes;

public interface DepartmentsService {
   
	
	public BasicRes create(DepartmentsCreateReq req);
}
