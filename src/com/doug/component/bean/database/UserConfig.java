package com.doug.component.bean.database;

import org.kymjs.kjframe.database.annotate.Id;

public class UserConfig {

	@Id()
	public int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int uid = 0;

	//开启手势密码
	private boolean needGesture = false;
	
	//手势密码
	private String handPwd;
	
	//上次手势密码核对时间
	private long lastGestureCheckTime;
	
	//账号&电话
	private String tel;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public boolean isNeedGesture() {
		return needGesture;
	}

	public void setNeedGesture(boolean needGesture) {
		this.needGesture = needGesture;
	}

	public String getHandPwd() {
		return handPwd;
	}

	public void setHandPwd(String handPwd) {
		this.handPwd = handPwd;
	}

	public long getLastGestureCheckTime() {
		return lastGestureCheckTime;
	}

	public void setLastGestureCheckTime(long lastGestureCheckTime) {
		this.lastGestureCheckTime = lastGestureCheckTime;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

}
