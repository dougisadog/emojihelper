package com.liang.furniture.ui.product;

import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import com.liang.furniture.R;
import com.liang.AppConstants;
import com.liang.furniture.bean.jsonbean.Product;
import com.liang.furniture.support.UIHelper;
import com.liang.furniture.utils.FormatUtils;
import com.liang.furniture.widget.SmartScrollView;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductDetailActivity extends KJActivity {
	
	@BindView(id = R.id.container)
	private RelativeLayout container;
	
	@BindView(id = R.id.img_right)
	private ImageView imgRight;
	
	private Product product;
	
	private KJHttp kjh;
	
	@BindView(id = R.id.name)
	private TextView name;
	
	@BindView(id = R.id.content)
	private TextView content;
	
	@BindView(id = R.id.detail)
	private TextView detail;
	
	@BindView(id = R.id.price)
	private TextView price;
	
	@BindView(id = R.id.pic)
	private ImageView pic;
	
	

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_product_detail);
		kjh = new KJHttp();
		product = (Product) getIntent().getSerializableExtra("product");
		if (null == product) {
			finish();
			return;
		}
		UIHelper.setTitleView(this, "", "商品详情");
		refreshData(product);
		getData(0);
	}
	

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
		case R.id.flright:
			break;
		}
	}
	
	private void getData(int page) {
		HttpParams params = new HttpParams(product);
		kjh.post(AppConstants.PRODUCT_DETAIL, params, new HttpCallBack(this) {
			@Override
			public void success(JSONObject ret) {
				String jsonStaffReq;
				String jsonSbb;
				try {
					if (ret.has("staffReq")) {
						jsonStaffReq = ret.getString("staffReq");
						Product current = FormatUtils.jsonParse(jsonStaffReq, Product.class);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
			
		});
	}
	
	private void refreshData(Product product) {
		name.setText(product.getName());
		content.setText(product.getContent());
		detail.setText(product.getDetail());
		Glide.with(this).load(product.getPicUrl()).into(pic);
	}
	

}
