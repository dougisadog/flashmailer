package com.doug.component.bean;

public class CItem {
	private String id;
	private String name;
	private String value;

	public CItem() {
		name = "";
		value = "";
	}

	public CItem(String name, String value, String id) {
		this.name = name;
		this.value = value;
		this.id = id;
	}

	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public String getId() {
		return id;
	}
}
