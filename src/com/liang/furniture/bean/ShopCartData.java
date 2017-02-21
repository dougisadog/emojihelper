package com.liang.furniture.bean;

import com.liang.furniture.bean.jsonbean.Product;

public class ShopCartData {
	
	
	private int amount;  //商品数量
	
	private double price;
	
	private Product product;
	
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
