package com.example.attendance.constants;

public enum RtnCode {

	SUCCESSFUL(200, "Successful!!"), // 成功
	PARAM_ERROR(400,"Param error!!"),
	ID_HAS_EXISTED(400,"Id has existed!!"),
	DEPARTMENT_NOT_FOUND(400,"Department not found!!"),
	ID_NOT_FOUND(404,"Id not found!!"),
	PASSWORD_ERROR(400,"Password error!!"),
	EMPLOYEE_CREATE_ERROR(400,"Employee create error!!"),
	PLEASE_lOGIN_FIRST(404,"Please login first!!"),
	UNAUTHORIZATED(401,"Unauthorizated!!"),
	CHANGE_PASSWORD_ERROR(400,"Change password error!!"),
	OLD_PASSWORD_AND_NEW_PASSWORD_ARE_IDENTICAL(400,"Old password and new password are identical!!"),
	FORGET_PASSWORD_ERROR(400,"Forget password error!!"),
	AUTH_CODE_NOT_MATCH(400,"Auth code not match!!"),
	AUTH_CODE_EXPIRED(400,"Auth code expired!!"),
	UPDATE_FAILED(400,"update failed!!"),
	UPDATE_ERROR(400,"update error!!"),

	ACCOUNT_DEACTIVATE(400,"account deactivate!!"),


	;

	private int code;

	private String message;

	// source -->
	private RtnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	// 只有用到getter
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
