package com.liang.furniture.ui.mainfragment;

import com.doug.emojihelper.R;
import com.liang.AppVariables;
import com.liang.furniture.ui.SigninActivity;
import com.liang.furniture.ui.myabstract.HomeFragment;
import com.liang.furniture.widget.LoudingDialogIOS;
import com.louding.frame.KJHttp;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AccountFragment extends HomeFragment {
	
	private View v;
	private KJHttp kjh;
	
	private RelativeLayout rlCustomer, rlInvest, rlPersonData, rlMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		
		initialed = true;
		v = inflater.inflate(R.layout.fragment_account_v2, null);
		kjh = new KJHttp();

		initView();
		initData();
		return v;
	}
	
	/**
	 * 页面加载时服务器信息拉取
	 */
	private void initData() {
		
	}
	
	
	private void initView() {
		rlCustomer = (RelativeLayout) v.findViewById(R.id.rlCustomer);
		rlCustomer.setOnClickListener(listener);
		
		rlInvest = (RelativeLayout) v.findViewById(R.id.rlInvest);
		rlInvest.setOnClickListener(listener);
		
		rlPersonData = (RelativeLayout) v.findViewById(R.id.rlPersonData);
		rlPersonData.setOnClickListener(listener);
		
		rlMessage = (RelativeLayout) v.findViewById(R.id.rlMessage);
		rlMessage.setOnClickListener(listener);
		v.findViewById(R.id.logout);
		
	}
	
	private void loginout() {
		AppVariables.clear();
		AppVariables.isSignin = false;
		startActivity(new Intent(getActivity(), SigninActivity.class));
		getActivity().finish();
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			new Intent();
			switch (v.getId()) {
				case R.id.logout :
					final LoudingDialogIOS ldsignout = new LoudingDialogIOS(getActivity());
					ldsignout.showOperateMessage("确认退出登录？");
					ldsignout.setPositiveButton("确定",
							R.drawable.dialog_positive_btn, new OnClickListener() {
								@Override
								public void onClick(View v) {
									loginout();
									ldsignout.dismiss();
								}
							});
					break;
//				case R.id.rlCustomer :
//					i.setClass(getActivity(), ProductListActivity.class);
//					getActivity().startActivity(i);
//					break;
//				case R.id.rlInvest :
//					i.setClass(getActivity(),InvestMainActivity.class);
//					getActivity().startActivity(i);
//					break;
//				case R.id.rlPersonData :
//					i.setClass(getActivity(),ProductDetailActivity.class);
//					getActivity().startActivity(i);
//					break;
//				case R.id.rlMessage :
//					i.setClass(getActivity(),MessageMainActivity.class);
//					getActivity().startActivity(i);
//					break;
			}
		}
	};
	
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	

	@Override
	public void refreshData() {
		if (!isInitialed()) return;
		//开始切换刷新
//		initData();
	}
	
}
