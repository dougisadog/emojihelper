package com.liang.furniture.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.liang.furniture.R;
import com.liang.furniture.bean.CItem;
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


public class ProductUserAdapter extends ArrayAdapter<Product> {
	
	private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Product> datas = new ArrayList<Product>();
	
    private ItemCallBack itemCallBack;
    
    public void setItemCallBack(ItemCallBack itemCallBack) {
    	this.itemCallBack = itemCallBack;
    }
    
    public interface ItemCallBack {
    	public void onPlusClick(int position, Product item);
    }
	
	private Context context;
	public ProductUserAdapter(Context context) {
		super(context, 0);
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	public ProductUserAdapter(Context context, int resource) {
		super(context, resource);
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Product getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/** 书中详细解释该方法 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ProductViewHolder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.item_product, null, true);
			holder = new ProductViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.imgView = (ImageView) convertView.findViewById(R.id.imgView);
			holder.plus = (ImageView) convertView.findViewById(R.id.plus);
			convertView.setTag(holder);
		} 
		else {
			holder = (ProductViewHolder) convertView.getTag();
		}
		
		holder.price.setVisibility(View.GONE);
		
		Product entity = datas.get(position);
		if (!TextUtils.isEmpty(entity.getName()))
			holder.name.setText(entity.getName().toString());
		if (!TextUtils.isEmpty(entity.getContent()))
			holder.content.setText(entity.getContent().toString());
		holder.price.setText(entity.getPrice() + "");
		if (!TextUtils.isEmpty(entity.getPicUrl())) {
			Glide.with(context).load(entity.getPicUrl()).placeholder(R.drawable.image_default).into(holder.imgView);
		}
		if (null != itemCallBack) {
			holder.plus.setTag(position);
			holder.plus.setOnClickListener(listener);
		}
		return convertView;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int position = Integer.parseInt(v.getTag().toString());
			itemCallBack.onPlusClick(position, datas.get(position));
		}
	};
	
	public List<Product> getDatas() {
		return datas;
	}
	
	public final class ProductViewHolder {
		
		private TextView name, content, price;
		private ImageView imgView, plus;

	}

	public void setDatas(List<Product> products) {
		this.datas = products;
		notifyDataSetChanged();
		
	}

}
