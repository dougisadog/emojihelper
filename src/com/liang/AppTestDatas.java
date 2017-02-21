package com.liang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.liang.furniture.bean.jsonbean.Product;

/**
 * 描述：应用程序配置信息
 * <p/>
 * 作者: Liu wei
 * <p/>
 * 邮箱：i@liuwei.co
 * <p/>
 * 创建时间: 2013-1-16
 */
public class AppTestDatas {

    private static Map<String, String> TYPES = new HashMap<String, String>();
    
    private static List<Product> PRODUCTS = new ArrayList<Product>();
    
    public static Map<String, String> getTypes() {
    	if (TYPES.size() == 0) {
    		for (int i = 0; i < 6; i++) {
    			TYPES.put(i+ "", "类型" + i);
				
			}
    	}
    	return TYPES;
    }
    
    public static List<Product> getProducts() {
    	getTypes();
    	if (PRODUCTS.size() == 0) {
    		for (int i = 0; i < 16; i++) {
    			Product product = new Product();
    			product.setName("商品" + i);
    			product.setContent("简介~~~~~~~"+ i);
    			product.setDetail("详情~~~~~~~" + i);
    			product.setPrice(5 * i);
    			product.setPid(UUID.randomUUID().toString());
    			product.setType((int)(Math.random() * 6));
    			PRODUCTS.add(product);
				
			}
    	}
    	return PRODUCTS;
    }
    
    
    

}
