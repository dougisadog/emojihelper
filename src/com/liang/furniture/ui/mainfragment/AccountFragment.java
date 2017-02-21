package com.liang.furniture.ui.mainfragment;

import com.liang.furniture.R;
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
import com.liang.furniture.ui.shopcart.ShopCartListActivity;
import com.liang.furniture.ui.product.ProductListActivity;
import com.liang.furniture.ui.anno.AnnoActivity;

public class AccountFragment extends HomeFragment {
	
	private View v;
	private KJHttp kjh;
	
	private RelativeLayout rlShopCart, rlProduct, rlSolution, rlAnno;

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
		rlShopCart = (RelativeLayout) v.findViewById(R.id.rlShopCart);
		rlShopCart.setOnClickListener(listener);
		
		rlProduct = (RelativeLayout) v.findViewById(R.id.rlProduct);
		rlProduct.setOnClickListener(listener);
		
		rlSolution = (RelativeLayout) v.findViewById(R.id.rlSolution);
		rlSolution.setOnClickListener(listener);
		
		rlAnno = (RelativeLayout) v.findViewById(R.id.rlAnno);
		rlAnno.setOnClickListener(listener);
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
			Intent i = new Intent();
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
				case R.id.rlProduct :
					i.setClass(getActivity(), ProductListActivity.class);
					getActivity().startActivity(i);
					break;
				case R.id.rlShopCart :
					i.setClass(getActivity(),ShopCartListActivity.class);
					getActivity().startActivity(i);
					break;
				case R.id.rlAnno :
					i.setClass(getActivity(),AnnoActivity.class);
					getActivity().startActivity(i);
					break;
				case R.id.rlSolution :
					i.setClass(getActivity(),AnnoActivity.class);
					getActivity().startActivity(i);
					break;
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
