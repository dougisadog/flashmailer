package com.doug.component.adapter.fragment;

import java.util.List;

import com.doug.component.ui.myabstract.HomeFragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

	private List<HomeFragment> fragments;
	public HomeFragmentAdapter(FragmentManager fm,
			List<HomeFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public HomeFragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
	
	public void setList(List<HomeFragment> fragments) {
		this.fragments = fragments;
		notifyDataSetChanged();
	}
}
