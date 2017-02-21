package com.liang.furniture.adapter;

import java.util.List;

import com.doug.emojihelper.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import wheel.adapters.AbstractWheelTextAdapter;

public class BaseTextWheelAdapter extends AbstractWheelTextAdapter {

	private List<String> datas;
	
	
	/**
	 * Constructor
	 */
	public BaseTextWheelAdapter(Context context, List<String> datas) {
		super(context, R.layout.item_wheel, NO_RESOURCE);
		this.datas = datas;
		setItemTextResource(R.id.txtWheelTitle);
	}
	
	public BaseTextWheelAdapter(int resId, Context context, List<String> datas) {
		super(context, resId, NO_RESOURCE);
		this.datas = datas;
		setItemTextResource(R.id.txtWheelTitle);
	}

	@Override
	public View getItem(int index, View cachedView, ViewGroup parent) {
		View view = super.getItem(index, cachedView, parent);
		return view;
	}

	@Override
	public int getItemsCount() {
		return datas.size();
	}

	@Override
	protected CharSequence getItemText(int index) {
		return datas.get(index);
	}
	
}
