package com.doug.component.bean;

/**
 * 消息中心
 * */
public class MessageCenter {

	private String subject;// 消息标题
	private String sender;// 发送者
	private String ins_date;// 消息时间
	private String contents; // 消息内容
	private String msg_type; // 消息类型
	private String open_flg; // 打开标志
	private String del_flg; // 删除标志
	private String id; // 消息ID

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getIns_date() {
		return ins_date;
	}

	public void setIns_date(String ins_date) {
		this.ins_date = ins_date;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getOpen_flg() {
		return open_flg;
	}

	public void setOpen_flg(String open_flg) {
		this.open_flg = open_flg;
	}

	public String getDel_flg() {
		return del_flg;
	}

	public void setDel_flg(String del_flg) {
		this.del_flg = del_flg;
	}

}
