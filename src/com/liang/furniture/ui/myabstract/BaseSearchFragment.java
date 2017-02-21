package com.liang.furniture.ui.myabstract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseSearchFragment extends Fragment{

	public static interface CallBackDialogConfirm {
		
		public void onKeyBack();
		public void onSubmit(Object values, String content);
		
	}
	
	protected CallBackDialogConfirm callback;
	
	protected static ViewGroup vg;
	
	protected Object values = new Object();
	
	public BaseSearchFragment() {
		super();
	}
	
	public BaseSearchFragment(ViewGroup vg, CallBackDialogConfirm callback) {
		super();
		this.callback = callback;
		BaseSearchFragment.vg = vg;
		values = new Object();
	}
	
	public void show(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(vg.getId(), this);
        ft.addToBackStack(null);
        ft.commitAllowingStateLoss();
	}
	
	
	public void hide(FragmentManager fragmentManager) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.remove(this);
		ft.commit();
	}
	
	
	@Override
	public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState);
	
	
	
}
