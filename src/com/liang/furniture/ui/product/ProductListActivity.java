package com.liang.furniture.ui.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.liang.furniture.R;
import com.liang.AppConstants;
import com.liang.furniture.adapter.CommonAdapter;
import com.liang.furniture.adapter.ProductUserAdapter;
import com.liang.furniture.adapter.ProductUserAdapter.ItemCallBack;
import com.liang.furniture.adapter.ViewHolder;
import com.liang.furniture.bean.ShopCartData;
import com.liang.furniture.bean.jsonbean.Product;
import com.liang.furniture.cache.CacheBean;
import com.liang.furniture.support.UIHelper;
import com.liang.furniture.utils.FormatUtils;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.ui.BindView;
import com.louding.frame.widget.KJListView;
import com.louding.frame.widget.KJRefreshListener;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductListActivity extends KJActivity {

	@BindView(id = R.id.container)
	private RelativeLayout container;

	@BindView(id = R.id.img_right)
	private ImageView imgRight;

	@BindView(id = R.id.listview)
	private KJListView listview;
	
	@BindView(id = R.id.shopCart)
	private TextView shopCart;
	
	

	private int page = 1;

	private ProductUserAdapter adapter;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_product_list);
		UIHelper.setTitleView(this, "", "商品列表");
//		UIHelper.setBtnRight(aty, R.drawable.icon_search_off, this);
	}


	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.flright :

				break;
		}
	}

	private void getData(final int pageIndex) {
//		HttpParams params = new HttpParams(dto);
//		kjh.post(AppConstants.CUSTOMER_LIST, params, new HttpCallBack(this) {
//			@Override
//			public void success(JSONObject ret) {
//				page = pageIndex;
//				String users;
//					//个人业绩相关
//					try {
//						
//						users = ret.getString("users");
//						List<Product> customerBizDtos = FormatUtils.getListJson(users, Product.class);
//						if (null == customerBizDtos || customerBizDtos.size() == 0) {
//							noMoreData = true;
//							listview.hideFooter();
//						}
//						else {
//							noMoreData = false;
//							listview.showFooter();
//						}
//						if (page == 1) {
//							adapter.setList(customerBizDtos);
//						}
//						else {
//							adapter.getList().addAll(customerBizDtos);
//							adapter.notifyDataSetChanged();
//						}
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					
//			}
//			
//			@Override
//			public void onFinish() {
//				listview.stopRefreshData();
//				super.onFinish();
//			}
//		});
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new ProductUserAdapter(this);
		adapter.setItemCallBack(new ItemCallBack() {
			
			@Override
			public void onPlusClick(int position, Product item) {
				Map<String, ShopCartData> currentDatas = CacheBean.getInstance().getShopCartDatas();
				String pid = item.getPid();
				if (currentDatas.containsKey(pid)) {
					currentDatas.get(pid).setAmount(currentDatas.get(pid).getAmount() + 1);
				}
				else {
					ShopCartData shopCartData = new ShopCartData();
					shopCartData.setAmount(1);
					shopCartData.setProduct(item);
					shopCartData.setPrice(item.getPrice());
					currentDatas.put(pid, shopCartData);
					CacheBean.getInstance().setShopCartDatas(currentDatas);
				}
				//类型数量
				shopCart.setText(currentDatas.size() + "");
			}

		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getProductDetail(adapter.getDatas().get(position));
			}
			
		});
		listview.setAdapter(adapter);
		listview.setOnRefreshListener(refreshListener);
		listview.setEmptyView(findViewById(R.id.empty));
		getData(1);

	}
	
	/**
	 * 获取用户详情
	 * @param dto
	 */
	private void getProductDetail(Product dto) {
		HttpParams params = new HttpParams(dto);
		KJHttp kjh = new KJHttp();
		kjh.post(AppConstants.PRODUCT_LIST, params, new HttpCallBack(this) {
			
			@Override
			public void success(JSONObject ret) {
				super.success(ret);
				try {
					Product dto = FormatUtils.jsonParse(ret.toString(), Product.class);
					Intent i = new Intent(ProductListActivity.this, ProductDetailActivity.class);
					i.putExtra("product", dto);
					startActivity(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
	}

	private boolean noMoreData = false;

	private KJRefreshListener refreshListener = new KJRefreshListener() {
		@Override
		public void onRefresh() {
			getData(1);
		}

		@Override
		public void onLoadMore() {
			if (!noMoreData) {
				getData(page + 1);
			}
		}
	};

}
