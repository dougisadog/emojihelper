package com.liang.furniture.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.liang.furniture.R;
import com.liang.AppConfig;
import com.liang.AppVariables;
import com.liang.MyApplication;
import com.liang.furniture.receiver.AppReceiver;
import com.liang.furniture.ui.mainfragment.AccountFragment;
import com.liang.furniture.ui.mainfragment.MainHomeFragment;
import com.liang.furniture.ui.myabstract.HomeFragment;
import com.louding.frame.KJHttp;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private MyPagerAdapter mPagerAdapter;
	private TabWidget mTabWidget;
	private TextView accountTab;
	private TextView MainHomeTab;

	private KJHttp kjh;
	private AppReceiver receiver;
	// 再按一次退出程序
	private long firstTime = 0;
	// 缓存上次所属page
	private int cacheCurrentPage = 0;

	public static final int REQUEST_CODE = 10001;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		kjh = new KJHttp();

		MyApplication.getInstance().setActivity(this);
		MyApplication.getInstance().addStackActivity(this);

		setContentView(R.layout.activity_main_v2);

		receiver = new AppReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SCREEN_OFF");
		filter.addAction("android.intent.action.SCREEN_ON");
		registerReceiver(receiver, filter);
		String sid = AppConfig.getAppConfig(this).get(AppConfig.SID);
		if (null != sid) {
			AppVariables.sid = sid;
		}
		mTabWidget = (TabWidget) findViewById(R.id.tabwidget);
		MainHomeTab = (TextView) getTvTab(R.string.home_v2,
				R.drawable.tab_main_home);
		mTabWidget.addView(MainHomeTab);
		MainHomeTab.setOnClickListener(mTabClickListener);

		accountTab = (TextView) getTvTab(R.string.account_v2,
				R.drawable.tab_account_selecter);
		mTabWidget.addView(accountTab);
		accountTab.setOnClickListener(mTabClickListener);

	
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		// 初始化
		List<HomeFragment> fragments = new ArrayList<HomeFragment>();
		fragments.add(new MainHomeFragment());
		fragments.add(new AccountFragment());

		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(),
				fragments);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mPageChangeListener);

		mTabWidget.setCurrentTab(0);
		mViewPager.setCurrentItem(0);
		mViewPager.setOffscreenPageLimit(2);
		
	}
	
	
	private void loginout() {
		AppVariables.clear();
		AppVariables.isSignin = false;
		startActivity(new Intent(this, SigninActivity.class));
		finish();
	}
	

	private OnClickListener mTabClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v == accountTab) {
				mViewPager.setCurrentItem(1);
			}
			else if (v == MainHomeTab) {
				mViewPager.setCurrentItem(0);
			}
		}
	};

	/**
	 * 翻页逻辑
	 */
	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int current) {
			mPagerAdapter.getItem(current).refreshData();
			mTabWidget.setCurrentTab(current);
			cacheCurrentPage = current;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private TextView getTvTab(int txtId, int resId) {
		TextView tv = new TextView(this);
		tv.setFocusable(true);
		tv.setText(getString(txtId));
		tv.setTextColor(
				getResources().getColorStateList(R.drawable.tab_font_selecter));
		tv.setTextSize(14);
		Drawable icon = getResources().getDrawable(resId);
		icon.setBounds(0, 0, icon.getMinimumWidth(), icon.getMinimumHeight());
		tv.setCompoundDrawables(null, icon, null, null); // 设置上图标
		tv.setCompoundDrawablePadding(5);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		private List<HomeFragment> fragments;
		public MyPagerAdapter(FragmentManager fm,
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
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		MyApplication.getInstance().setCurrentRunningActivity(this);
		int currentItem = mViewPager.getCurrentItem();
		mPagerAdapter.getItem(currentItem).refreshData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (MyApplication.getInstance().getCurrentRunningActivity()
				.equals(this)) {
			MyApplication.getInstance().setCurrentRunningActivity(null);
		}
		AppVariables.lastTime = new Date().getTime();
	}

	// 再按一次退出程序
	@Override
	public void onBackPressed() {
		long secondTime = System.currentTimeMillis();
		if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			firstTime = secondTime;// 更新firstTime
		}
		else { // 两次按键小于2秒时，退出应用
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}


}
