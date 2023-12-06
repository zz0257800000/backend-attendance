package com.example.attendance.ifs;

import javax.servlet.http.HttpSession;

import com.example.attendance.vo.BasicRes;
import com.example.attendance.vo.EmployeeCreateReq;

public interface EmployeeService {

    public BasicRes create(EmployeeCreateReq req);
    
    public BasicRes login(String id,String pwd,HttpSession session);

    public BasicRes changePassword(String id,String oldPwd,String newPwd);
    public BasicRes  forgotPassword(String id,String email );
}
