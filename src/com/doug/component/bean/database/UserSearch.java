package com.doug.component.bean.database;

import org.kymjs.kjframe.database.annotate.Id;

public class UserSearch {

	@Id()
	public int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private String content; //查询内容
	
	private String userid; //账号id


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}


}
