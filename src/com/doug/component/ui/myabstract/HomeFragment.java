package com.doug.component.ui.myabstract;

import android.support.v4.app.Fragment;

public abstract class HomeFragment extends Fragment{
	
	//该页是否已加载完成
	protected boolean initialed = false;
	
	public abstract void refreshData();

	public boolean isInitialed() {
		return initialed;
	}
	
	public void onResume() {
	    super.onResume();
	}
	public void onPause() {
	    super.onPause();
	}

}
