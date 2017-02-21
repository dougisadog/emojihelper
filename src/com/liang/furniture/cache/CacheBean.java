package com.liang.furniture.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.kymjs.kjframe.http.Cache;

import com.liang.AppVariables;
import com.liang.furniture.bean.CItem;
import com.liang.furniture.bean.MainAD;
import com.liang.furniture.bean.ShopADData;
import com.liang.furniture.bean.ShopCartData;
import com.liang.furniture.bean.database.UserSearch;
import com.liang.furniture.bean.jsonbean.Account;
import com.liang.furniture.bean.jsonbean.Product;
import com.liang.furniture.bean.jsonbean.User;
import com.liang.furniture.support.ApkInfo;
import com.liang.furniture.utils.ApplicationUtil;
import com.louding.frame.KJDB;

import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

public class CacheBean {
	
	private static CacheBean instance = null;
	
	public static CacheBean getInstance() {
		if (null == instance) {
			instance = new CacheBean();
		}
		return instance;
	}
	
	public void init(Context context) {
		KJDB kjdb = KJDB.create(context);
		List<UserSearch> searches = kjdb.findAll(UserSearch.class);
		this.searches = searches;
	}
	
	public void clear() {
		user = null;
		account = null;
	}
	
	//清空webcookie数据
	public static void syncCookie(Context context) {
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();//移除  
	    CookieSyncManager.getInstance().sync();  
	}
	
	/**
	 * 个人信息是否需要更新
	 * @return
	 */
	public static boolean checkNeedUpdate() {
		User user = getInstance().getUser();
		if (null == user || AppVariables.uid != Integer.parseInt(user.getUid())) return true;
		return System.currentTimeMillis()
				- user.getLastModTime() > AppVariables.cacheLiveTime;
	}
	
	private User user;
	
	private Account account;
	
	private List<UserSearch> searches = new ArrayList<UserSearch>();
	
	private List<CItem> items = new ArrayList<CItem>(); //全部表情页面数据
	
	private List<CItem> currentItems = new ArrayList<CItem>(); //当前表情页面数据
	
	public List<UserSearch> getSearches() {
		return searches;
	}

	public void setSearches(List<UserSearch> searches) {
		this.searches = searches;
	}

	private ApkInfo apkInfo;
	
	private List<MainAD> adDatas;
	
	private List<ShopADData> shopADDatas;
	
	private MainAD ad; //主页广告绝对地址（图片src直接调用）
	
	private Map<String, ShopCartData> shopCartDatas = new HashMap<String, ShopCartData>();
	
	//现金券背景bitmap缓存
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		user.setLastModTime(new Date().getTime());
		this.user = user;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public ApkInfo getApkInfo() {
		return apkInfo;
	}

	public void setApkInfo(ApkInfo apkInfo) {
		this.apkInfo = apkInfo;
	}


	public List<MainAD> getAdDatas() {
		return adDatas;
	}

	public void setAdDatas(List<MainAD> adDatas) {
		this.adDatas = adDatas;
	}

	public MainAD getAd() {
		return ad;
	}

	public void setAd(MainAD ad) {
		this.ad = ad;
	}

	public List<ShopADData> getShopADDatas() {
		return shopADDatas;
	}

	public void setShopADDatas(List<ShopADData> shopADDatas) {
		this.shopADDatas = shopADDatas;
	}

	public List<CItem> getItems() {
		return items;
	}

	public void setItems(List<CItem> items) {
		this.items = items;
	}

	public List<CItem> getCurrentItems() {
		return currentItems;
	}

	public void setCurrentItems(List<CItem> currentItems) {
		this.currentItems = currentItems;
	}

	public Map<String, ShopCartData> getShopCartDatas() {
		return shopCartDatas;
	}

	public void setShopCartDatas(Map<String, ShopCartData> shopCartDatas) {
		this.shopCartDatas = shopCartDatas;
	}


}
