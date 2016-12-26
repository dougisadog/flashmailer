package com.doug.component.bean;

public class ShopADData implements CycleData, Comparable<ShopADData>{

	private String title;
	private String description;
	private String androidUrl;
	private String iosUrl;
	private String imgUrl;
	private ShopADType type = ShopADType.goods;
	private Double sortNo = 0D;

	public static enum ShopADType {

		goods("商品"), url("链接"), ;

		String type;

		ShopADType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAndroidUrl() {
		return androidUrl;
	}

	public void setAndroidUrl(String androidUrl) {
		this.androidUrl = androidUrl;
	}

	public String getIosUrl() {
		return iosUrl;
	}

	public void setIosUrl(String iosUrl) {
		this.iosUrl = iosUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Double getSortNo() {
		return sortNo;
	}

	public void setSortNo(Double sortNo) {
		this.sortNo = sortNo;
	}

	public ShopADType getType() {
		return type;
	}

	public void setType(ShopADType type) {
		this.type = type;
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return imgUrl;
	}

	@Override
	public String getLinkUrl() {
		// TODO Auto-generated method stub
		return androidUrl;
	}

	@Override
	public int compareTo(ShopADData another) {
		return this.getSortNo().compareTo(another.getSortNo());
		//修正为倒序
//		return another.getSortNo().compareTo(this.getSortNo());
	}

}
