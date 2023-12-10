package com.example.attendance.constants;

public enum JobPosition {

	//����
	ADMIN(99,"Admin"),
	SUPERVISOR(21,"Superviosr"),
	DIRECTOR(20,"Director"),
	SENIOR(2,"Senior"),
	GENERAL(1,"General");

	
	private int permission;
	
	private String title;

	private JobPosition(int permission, String title) {
		this.permission = permission;
		this.title = title;
	}

	public int getPermission() {
		return permission;
	}

	public String getTitle() {
		return title;
	}
	//�ھ� �~�����Ǩ� title�ѼƭȨ��o�������v��(permission)
	//�i�H�T�{ title�Ѽƭ� �O�_�w�q�b��JobPosition��
	public  static  int parser(String title) {
		for(JobPosition item : JobPosition.values()) {
			if(title.equalsIgnoreCase(item.getTitle())) {
				return item.getPermission();
				
			}
			
		}
		return 0;
		
	}
	public  static  int reviewPermission  = 20;
	public  static boolean hasReviewPermission(String title) {
		int callerPermission = parser(title);
		return callerPermission >= reviewPermission ? true : false;
		
	}
	

}
