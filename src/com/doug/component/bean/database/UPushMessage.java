package com.doug.component.bean.database;

import org.kymjs.kjframe.database.annotate.Id;

/**
 * UMeng推送信息
 * @author Doug
 * 
 */
public class UPushMessage {
	
	@Id()
	public int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	private long receiveTime; //接受时间
	
	private long showedTime; //显示时间
	
	private String title; //标题
	
	private String content; //内容信息
	
	private int productId; //商品id
	
	private String url;   //跳转地址
	
	private int type; //类型
	
	private int showed = 0; //1 已读  0 未读
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}


	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}
	
	public long getShowedTime() {
		return showedTime;
	}

	public void setShowedTime(long showedTime) {
		this.showedTime = showedTime;
	}

	public int getShowed() {
		return showed;
	}

	public void setShowed(int showed) {
		this.showed = showed;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

	


}
