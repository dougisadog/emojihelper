package com.liang;

import java.util.ArrayList;
import java.util.List;

import com.liang.furniture.error.ErrLogManager;
import com.liang.furniture.support.AppActivityLifecycleCallbacks;
import com.liang.furniture.support.CrashHandler;
import com.liang.furniture.support.ScreenObserver;
import com.liang.furniture.utils.ApplicationUtil;
import com.louding.frame.ui.AnnotateUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MyApplication extends Application {


	@Override
	public void onCreate() {
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		instance = this;
		ErrLogManager.registerHandler();
		if (Build.VERSION.SDK_INT >= 14) {
			registerActivityLifecycleCallbacks(new AppActivityLifecycleCallbacks());
		}
		//开启webview chrome调试
		if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {  
			   WebView.setWebContentsDebuggingEnabled(true);  
		} 

		// 监听屏幕
		observer = new ScreenObserver();

	}


	/**
	 * Application
	 * 用于 数据传递，数据共享 等,数据缓存等操作。
	 * 
	 * @author Doug
	 *
	 */
		private static final int SHOW_CUSTOM_ERR_MSG = 0;
		private static final int SHOW_CONNECT_SERVER_ERR_MSG = 10;
		private static final int SHOW_SERVER_ERR_MSG = 11;
		private static final int PUSH_MSG = 20;
		private static final int RESET_TO_LOGIN = 100;

	    private static MyApplication instance = null;
	    
	    /**
	     * app性格信息
	     */
	    private Activity activity = null;
	    private Activity currentRunningActivity = null;
	    private List<Activity> stackActivities = new ArrayList<Activity>();
	    private ScreenObserver observer = null;

	    @SuppressLint("HandlerLeak")
		private Handler uiHandler = new Handler() {
	    	
	    	public void handleMessage(Message msg) {
	    		switch (msg.what) {
				case SHOW_CONNECT_SERVER_ERR_MSG:
					Toast.makeText(MyApplication.getInstance().getActivity(), "connect fail", Toast.LENGTH_SHORT).show();
					break;
				case SHOW_SERVER_ERR_MSG:
					String serverMsg = (String) msg.obj;
					Toast.makeText(MyApplication.getInstance().getActivity(), serverMsg, Toast.LENGTH_SHORT).show();
					break;
				case SHOW_CUSTOM_ERR_MSG:
					String errMsg = (String) msg.obj;
					Toast.makeText(MyApplication.getInstance().getActivity(), errMsg, Toast.LENGTH_SHORT).show();
					break;
				case RESET_TO_LOGIN:
//					startActivity(new Intent(getActivity(),SigninActivity.class));
					ApplicationUtil.restartApplication(getActivity());
					clearStackActivities();
					break;
				}
	    	};
	    	
	    };
	    
	    /**********************************************************************************************************************************************************/
	    
	    public void showCustomErrMsg(String errMsg) {
	    	Message msg = new Message();
	    	msg.what = SHOW_CUSTOM_ERR_MSG;
	    	msg.obj = errMsg;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void showConnectServerErrMsg() {
	    	Message msg = new Message();
	        msg.what = SHOW_CONNECT_SERVER_ERR_MSG;
	        uiHandler.sendMessage(msg);
		}
	    
	    
	    public void showErrMsg(String errorMsg) {
	    	Message msg = new Message();
	    	msg.what = SHOW_SERVER_ERR_MSG;
	    	msg.obj = errorMsg;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void pushMsg() {
	    	Message msg = new Message();
	    	msg.what = PUSH_MSG;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    public void resetToLogin() {
	    	Message msg = new Message();
	    	msg.what = RESET_TO_LOGIN;
	    	uiHandler.sendMessage(msg);
	    }
	    
	    
	    /**********************************************************************************************************************************************************/
	    
	    public static MyApplication getInstance() {
	        return instance;
	    }
	    
		public Activity getActivity() {
	        return activity;
	    }

	    public void setActivity(Activity activity) {
	        this.activity = activity;
	    }

	    public Activity getCurrentRunningActivity() {
	        return currentRunningActivity;
	    }

	    public void setCurrentRunningActivity(Activity currentRunningActivity) {
	        this.currentRunningActivity = currentRunningActivity;
	    }
		
		public Handler getUiHandler() {
			return uiHandler;
		}

		public List<Activity> getStackActivities() {
			return stackActivities;
		}
		
		public void addStackActivity(Activity activity) {
			stackActivities.add(activity);
		}
		
		public void removeStackActivity(Activity activity) {
			if (stackActivities.contains(activity))
				stackActivities.remove(activity);
		}
		
		public void clearStackActivities() {
			for (Activity activity : stackActivities) {
				if (activity.isFinishing())
					continue;
				activity.finish();
			}
			stackActivities.clear();
		}
		
		public Activity getLastActivity() {
			Activity activity = null;
			List<Activity> activities = getStackActivities();
			if (null != activities && null != currentRunningActivity && activities.size() > 1) {
				activity =  activities.get(1);
			}
			return activity;
		}	

		public ScreenObserver getObserver() {
			return observer;
		}
		
	}

