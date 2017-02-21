package com.liang.furniture.bean.jsonbean;

import java.io.Serializable;

import android.text.TextUtils;

public class Product implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2906239737030869980L;

	private String pid;
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	private String name;
	
	private long price;
	
	private String picUrl;
	
	private int type;//类型
	
	private String content; //介绍
	
	private String detail; //详情

	/**
	 * 用于list remove的删除
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Product) {
			if (!TextUtils.isEmpty(pid)) {
				return pid.equals(((Product) o).getPid());
			}
		}
		// TODO Auto-generated method stub
		return false;
	}
	
	

}
