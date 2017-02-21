package com.liang.furniture.ui.mainfragment;

import com.doug.emojihelper.R;
import com.liang.furniture.ui.myabstract.HomeFragment;
import com.louding.frame.KJHttp;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment1;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import wheel.WheelView;

public class MainHomeFragment extends HomeFragment{
	
	private TextView name,orgnaization,level;
	
	private TextView siteMessage, warnMessage;
	
	private WheelView wv;
	
	private KJHttp kjh;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initialed = true;
		
		kjh = new KJHttp();
		View v = inflater.inflate(R.layout.fragment_main_home, null);
		
		siteMessage = (TextView) v.findViewById(R.id.siteMessage);
		warnMessage = (TextView) v.findViewById(R.id.warnMessage);
		
		name = (TextView) v.findViewById(R.id.name);
		orgnaization = (TextView) v.findViewById(R.id.orgnaization);
		level = (TextView) v.findViewById(R.id.level);
		
		
		initData();
		return v;
		
	}
	
	private void getUserInfo() {
//		MobileLoginBean user = new MobileLoginBean();
//		user.setSid(AppVariables.sid);
//		HttpParams params = new HttpParams(user);
//		kjh.post(AppConstants.STAFF_INFO, params, new HttpCallBack(getActivity(), false) {
//			@Override
//			public void success(JSONObject ret) {
//				StaffOlrtBizDto staffOlrtBizDto = FormatUtils.jsonParse(ret.toString(), StaffOlrtBizDto.class);
//				CacheBean.getInstance().setStaffOlrtBizDto(staffOlrtBizDto);
//				name.setText(staffOlrtBizDto.getStaff().getStaffName());
//				orgnaization.setText(staffOlrtBizDto.getOrgStructureTree().getOrgName());
//				level.setText(staffOlrtBizDto.getStaffRank().getStaffRankName());
//				
//			}
//		});
	}
	

	/**
	 * 全部数据加载
	 */
	public void initData() {
		getUserInfo();
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
	}


	private DialogFragment1 confirmDialog;
	

	@Override
	public void refreshData() {
		if (!initialed) return;
		//开始切换刷新
//		initData();
	}
	

}
