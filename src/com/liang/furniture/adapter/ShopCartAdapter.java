package com.liang.furniture.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.liang.furniture.R;
import com.liang.furniture.bean.ShopCartData;
import com.liang.furniture.bean.jsonbean.Product;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ShopCartAdapter extends ArrayAdapter<ShopCartData> {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<ShopCartData> datas = new ArrayList<ShopCartData>();
	
    private ItemCallBack itemCallBack;
    
    public void setItemCallBack(ItemCallBack itemCallBack) {
    	this.itemCallBack = itemCallBack;
    }
    
    public interface ItemCallBack {
    	public void onMinusClick(int position, ShopCartData item);
    }
	
	private Context context;
	public ShopCartAdapter(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public ShopCartAdapter(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public ShopCartData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ShopCartDataViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_shopcart, null, true);
			holder = new ShopCartDataViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			holder.minus = (ImageView) convertView.findViewById(R.id.plus);
			holder.amount = (TextView) convertView.findViewById(R.id.amount);
			convertView.setTag(holder);
		} 
		else {
			holder = (ShopCartDataViewHolder) convertView.getTag();
		}
		if (null !=  datas.get(position) &&  null != datas.get(position).getProduct()) {
			Product entity = datas.get(position).getProduct();
			if (!TextUtils.isEmpty(entity.getName()))
				holder.name.setText(entity.getName().toString());
			if (!TextUtils.isEmpty(entity.getContent()))
				holder.content.setText(entity.getContent().toString());
			if (!TextUtils.isEmpty(entity.getContent()))
				holder.content.setText(entity.getContent().toString());
			holder.amount.setText(datas.get(position).getAmount() + "");
			holder.price.setText(entity.getPrice() + "");
			if (!TextUtils.isEmpty(entity.getPicUrl())) {
				Glide.with(context).load(entity.getPicUrl()).placeholder(R.drawable.image_default).into(holder.imgView);
			}
			if (null != itemCallBack) {
				holder.minus.setTag(position);
				holder.minus.setOnClickListener(listener);
			}
		}
		return convertView;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int position = Integer.parseInt(v.getTag().toString());
			itemCallBack.onMinusClick(position, datas.get(position));
		}
	};
	
	public List<ShopCartData> getDatas() {
		return datas;
	}
	
	public void setDatas(List<ShopCartData> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}
	
	public final class ShopCartDataViewHolder {
		
		private TextView name, content, price, amount;
		private ImageView imgView, minus;

	}

}
