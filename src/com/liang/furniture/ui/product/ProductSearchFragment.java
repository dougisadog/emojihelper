package com.liang.furniture.ui.product;

import com.liang.furniture.R;
import com.liang.furniture.dialog.DialogAlertFragment.CallBackDialogConfirm;
import com.liang.furniture.dialog.DialogWheelFragment;
import com.liang.furniture.ui.myabstract.BaseSearchFragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductSearchFragment extends BaseSearchFragment implements OnClickListener {

	private Button confirm;
	
	private RelativeLayout searchContainer;
	
	private EditText name,tel,idcard;
	
	private TextView sex;
	
	private DialogWheelFragment fragment;
	
	private String contentText;
	
	public ProductSearchFragment(ViewGroup vg, CallBackDialogConfirm callback) {
		super(vg, callback);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.dialog_customer_search, null);// 得到加载view
		
		searchContainer = (RelativeLayout) v.findViewById(R.id.searchContainer);
		searchContainer.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				callback.onKeyBack();
				return true;
			}
		});
		
		name = (EditText) v.findViewById(R.id.name);
		tel = (EditText) v.findViewById(R.id.tel);
		idcard = (EditText) v.findViewById(R.id.idcard);
		
		sex = (TextView) v.findViewById(R.id.sex);
		sex.setOnClickListener(this);
		
		confirm = (Button) v.findViewById(R.id.confirm);
		confirm.setOnClickListener(this);
		
		
		
		return v;
	}
	
	/**
	 * 处理选择结果
	 */
	private void combineResult() {
		
	}
	
	
	@Override
	public void onClick(View v) {
		if (null == callback) {
			return;
		}
		switch (v.getId()) {
		case R.id.confirm:
			combineResult();
			callback.onSubmit(values, contentText);
			break;
		case R.id.sex:
			if (null == fragment) {
				String[] sexarr = getResources().getStringArray(
							R.array.message_func);
				fragment = new DialogWheelFragment(0, new DialogWheelFragment.CallBackDialogWheel() {
					
					@Override
					public void submit(int tag, String data) {
						sex.setText(data);
						fragment.dismiss();
					}
					
					@Override
					public void cancel() {
						fragment.dismiss();
						
					}
				}, "性别", sexarr, "");
			}
			fragment.showDialog(getFragmentManager());
			break;
		}
	}
	
	
	
}
