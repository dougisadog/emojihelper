package com.liang;

import android.content.Intent;
import android.text.TextUtils;

import java.util.Date;
import java.util.List;

import com.louding.frame.KJActivity;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.doug.emojihelper.R;
import com.liang.furniture.bean.database.UserConfig;
import com.liang.furniture.cache.CacheBean;
import com.liang.furniture.support.InfoManager;
import com.liang.furniture.support.InfoManager.TaskCallBack;
import com.liang.furniture.ui.AtyMainAD;

public class StartApplication extends KJActivity {
	
	private KJHttp kjh;
	private KJDB kjdb;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_start);
	}
	
	@Override
	public void initData() {
		kjh = new KJHttp();
		super.initData();
		CacheBean.getInstance().init(this);
		login();
	}

	
	private UserConfig userConfig;
	
	private void login() {
		AppVariables.tel = AppConfig.getAppConfig(this).get(AppConfig.TEL);
		if (TextUtils.isEmpty(AppVariables.tel)) {
			updateDone(false);
			return;
		}
		String tel = "";
		String pwd = "";
		//账号数据本地数据库存储
		List<UserConfig> userConfigs = kjdb.findAllByWhere(UserConfig.class, "tel= '" + AppVariables.tel + "'");
		if (userConfigs.size() > 0) {
			userConfig = userConfigs.get(0);
			tel = userConfig.getTel();
			pwd = userConfig.getPwd();
		}
		try {
			InfoManager.getInstance().loginNormal(this, tel, pwd, new TaskCallBack() {
				
				@Override
				public void taskSuccess() {
					if (null != userConfig) {
						userConfig.setLastLogin(new Date().getTime());
						kjdb.update(userConfig);
					}
					updateDone(true);
					finish();
				}
				
				@Override
				public void taskFail(String err, int type) {
					updateDone(false);
				}
				
				@Override
				public void afterTask() {
					
				}
			});
		} catch (Exception e) {
			updateDone(false);
			e.printStackTrace();
		}
	}


	/**
	 * 无更新或者 完成更新的正常进入流程
	 */
	private void updateDone(boolean loginSuccess) {
		Intent mainIntent = null;
		mainIntent = new Intent(StartApplication.this, AtyMainAD.class);
		mainIntent.putExtra("login", loginSuccess);
		mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		StartApplication.this.startActivity(mainIntent);
		StartApplication.this.finish();
	}
	
	public static boolean parse = false;

	@Override
	protected void onResume() {
		if (parse) {
			login();
		}
		super.onResume();
	}
	
	


}
