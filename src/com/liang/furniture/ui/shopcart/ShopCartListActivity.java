package com.liang.furniture.ui.shopcart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.liang.furniture.R;
import com.liang.furniture.adapter.ShopCartAdapter;
import com.liang.furniture.adapter.ShopCartAdapter.ItemCallBack;
import com.liang.furniture.bean.ShopCartData;
import com.liang.furniture.cache.CacheBean;
import com.liang.furniture.support.UIHelper;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class ShopCartListActivity extends KJActivity {

	@BindView(id = R.id.container)
	private RelativeLayout container;

	@BindView(id = R.id.img_right)
	private ImageView imgRight;

	@BindView(id = R.id.listview)
	private ListView listview;

	private ShopCartAdapter adapter;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_shopcart_list);
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

	private void getData() {
		Map<String, ShopCartData> datas = CacheBean.getInstance().getShopCartDatas();
		List<ShopCartData> currentDatas = new ArrayList<ShopCartData>();
		currentDatas.addAll(datas.values());
		adapter.setDatas(currentDatas);
	}

	@Override
	public void initWidget() {
		super.initWidget();
		adapter = new ShopCartAdapter(this);
		adapter.setItemCallBack(new ItemCallBack() {
			@Override
			public void onMinusClick(int position, ShopCartData item) {
				Map<String, ShopCartData> currentDatas = CacheBean.getInstance().getShopCartDatas();
				String pid = item.getProduct().getPid();
				int amount = currentDatas.get(pid).getAmount();
				amount--;
				if (amount == 0) {
					CacheBean.getInstance().getShopCartDatas().remove(pid);
				}
				else {
					currentDatas.get(position).setAmount(amount);
					CacheBean.getInstance().setShopCartDatas(currentDatas);
				}
				getData();
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				getProductDetail(adapter.getDatas().get(position));
			}
			
		});
		listview.setAdapter(adapter);
		listview.setEmptyView(findViewById(R.id.empty));
		getData();

	}
	
}
