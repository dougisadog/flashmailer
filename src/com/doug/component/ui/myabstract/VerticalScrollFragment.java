package com.doug.component.ui.myabstract;

public abstract class VerticalScrollFragment extends HomeFragment{
	
	protected ScollCallBack scollCallBack;
	
	public interface ScollCallBack {
		
		public void onScrollTop();
		
		public void onScrollBottom();
		
	}

	public ScollCallBack getScollCallBack() {
		return scollCallBack;
	}

	public void setScollCallBack(ScollCallBack scollCallBack) {
		this.scollCallBack = scollCallBack;
	}
	
	

}
