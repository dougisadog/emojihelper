package com.doug.emojihelper.ui;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.doug.EmojiApplication;
import com.doug.emojihelper.R;
import com.doug.emojihelper.adapter.ImageGridAdapter;
import com.doug.emojihelper.adapter.ImageGridAdapter.ItemCallBack;
import com.doug.emojihelper.adapter.ImagePagerAdapter;
import com.doug.emojihelper.adapter.ImagePagerAdapter.LongClickCallBack;
import com.doug.emojihelper.bean.CItem;
import com.doug.emojihelper.bean.database.UserSearch;
import com.doug.emojihelper.cache.CacheBean;
import com.doug.emojihelper.dialog.ImagePager;
import com.doug.emojihelper.dialog.ImagePager.CallBackDialogConfirm;
import com.doug.emojihelper.receiver.AppReceiver;
import com.doug.emojihelper.support.HtmlParser;
import com.doug.emojihelper.support.HtmlParser.ParserCallBack;
import com.doug.emojihelper.utils.ImageUtils;
import com.doug.emojihelper.widget.LoudingDialogIOS;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.louding.frame.KJDB;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.utils.StringUtils;
import com.doug.emojihelper.utils.KeyboardUitls;

public class MainActivity extends FragmentActivity {

	private KJHttp kjh;
	private AppReceiver receiver;
	// 再按一次退出程序
	private long firstTime = 0;

	public static final int REQUEST_CODE = 10001;

	private EditText keywords;
	private Button search;
	private PullToRefreshGridView emoji;
	private LinearLayout historyContainer;
	private LinearLayout container;

	private ImageGridAdapter adapter;
	private List<CItem> items = new ArrayList<CItem>();

	private boolean isRefreshing = false;
	private int currentPage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		EmojiApplication.getInstance().setActivity(this);
		EmojiApplication.getInstance().addStackActivity(this);

		kjh = new KJHttp();

		setContentView(R.layout.activity_main_v2);
		container = (LinearLayout) findViewById(R.id.container);

		keywords = (EditText) findViewById(R.id.keywords);
		search = (Button) findViewById(R.id.search);
		emoji = (PullToRefreshGridView) findViewById(R.id.emoji);
		emoji.setMode(Mode.PULL_FROM_END);
		emoji.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				if (!isRefreshing) {
					isRefreshing = true;
					if (emoji.isHeaderShown()) {
						// refreshOnlineStatus(true);
					}
					else if (emoji.isFooterShown()) {
//						loadNextPage();
						loadMore();
					}
				}
				else {
					emoji.onRefreshComplete();
					isRefreshing = false;
				}
			}

		});

		items = new ArrayList<CItem>();
		adapter = new ImageGridAdapter(this, items);
		adapter.setItemCallBack(new ItemCallBack() {

			@Override
			public void onItemClick(int position, final CItem item) {
				showImagePager(position);
			}
		});
		emoji.setAdapter(adapter);

		search.setOnClickListener(listener);
		
		historyContainer = (LinearLayout) findViewById(R.id.historyContainer);
		List<UserSearch> searches = CacheBean.getInstance().getSearches();
		LinearLayout.LayoutParams parms= new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		for (int i = searches.size() - 1; i >= Math.max(0, searches.size() - 5); i--) {
			TextView tv = new TextView(this);
			tv.setLayoutParams(parms);
			tv.setText(searches.get(i).getContent());
			tv.setPadding(5, 0, 5, 0);
			tv.setGravity(Gravity.CENTER);
			tv.setOnClickListener(searchListener);
			historyContainer.addView(tv);
		}

		receiver = new AppReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.SCREEN_OFF");
		filter.addAction("android.intent.action.SCREEN_ON");
		registerReceiver(receiver, filter);

	}
	
	private OnClickListener searchListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			TextView tv = (TextView) v;
			keywords.setText(tv.getText().toString());
			
		}
	};
	
	private List<String> doFilter(String filter) {
		List<String> names = new ArrayList<String>();
		List<UserSearch> searches = CacheBean.getInstance().getSearches();
		for (UserSearch userSearch : searches) {
			if (StringUtils.isEmpty(filter) || userSearch.getContent().contains(filter)) {
				names.add(userSearch.getContent());
			}
		}
		return names;
		
	}
	
	private String searchContent;

	private void loadNextPage() {
		HtmlParser.getInstance().parseSearch(searchContent + "/" + (currentPage + 1),
				MainActivity.this, new ParserCallBack() {

					@Override
					public void parseSearchDone(List<CItem> datas) {
						CacheBean.getInstance().getItems().addAll(datas);
						adapter.setDatas(CacheBean.getInstance().getItems());
						currentPage++;
						emoji.onRefreshComplete();
						isRefreshing = false;
					}
				}, false);

	}
	
	private boolean noMore = false;
	private int pageSize = 18;
	private int loadPage = 1;
	
	private void loadMore() {
		if (noMore) {
			emoji.onRefreshComplete();
			isRefreshing = false;
			return;
		}
		List<CItem> allDatas  = CacheBean.getInstance().getItems();
		List<CItem> current  = CacheBean.getInstance().getCurrentItems();
		if (allDatas.size() - current.size() < pageSize) {
			HtmlParser.getInstance().parseSearch(searchContent + "/" + (loadPage + 1),
					MainActivity.this, new ParserCallBack() {

						@Override
						public void parseSearchDone(List<CItem> datas) {
							if (datas.size() == 0) {
								noMore = true;
							}
							loadPage++;
							CacheBean.getInstance().getItems().addAll(datas);
							finishLoad();
						}
					}, false);
		}
		else {
			finishLoad();
		}
	}
	
	private void finishLoad() {
		List<CItem> current  = CacheBean.getInstance().getCurrentItems();
		List<CItem> allDatas = new ArrayList<CItem>();
		allDatas.addAll(CacheBean.getInstance().getItems());
		List<CItem> newCurrent = allDatas.subList(0, Math.min(current.size() + pageSize, allDatas.size()));
		CacheBean.getInstance().setCurrentItems(newCurrent);
		currentPage++;
		emoji.onRefreshComplete();
		isRefreshing = false;
		adapter.setDatas(newCurrent);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.search :
					searchContent = keywords.getText().toString();
					HtmlParser.getInstance().parseSearch(searchContent,
							MainActivity.this, new ParserCallBack() {

								@Override
								public void parseSearchDone(List<CItem> datas) {
									items = datas;
//									adapter.setDatas(datas);
									currentPage = 1;
									
									CacheBean.getInstance().setItems(datas);
									List<CItem> newCurrent = datas.subList(0, Math.min(datas.size(), pageSize));
									CacheBean.getInstance().setCurrentItems(newCurrent);
									adapter.setDatas(newCurrent);
								}
							});
					saveSearch(searchContent);
					KeyboardUitls.hideKeyboard(MainActivity.this);
					break;

				default :
					break;
			}

		}
	};
	
	private void saveSearch(String text) {
		KJDB kjdb = KJDB.create(this);
		List<UserSearch> searches = kjdb.findAllByWhere(UserSearch.class, "content= '" + text + "'");
		if (searches.size() > 0) return;
		UserSearch s = new UserSearch();
		s.setContent(text);
		kjdb.save(s);
		CacheBean.getInstance().getSearches().add(s);
		
	}

	private void downloadEmoji(CItem item) {
		String filePath = ImageUtils.getImagePath(item.getValue());
		File path = new File(filePath);
		kjh.download(item.getValue(), path, new HttpCallBack(this, false) {
			@Override
			public void onSuccess(File f) {
				Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_LONG)
						.show();

			}
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				Toast.makeText(MainActivity.this, "下载失败：" + strMsg,
						Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private ImagePager imagePager;
	private ImagePagerAdapter imagePagerAdapter;
	
	private void showImagePager(int position) {
		if (null == imagePager) {
			if (null == imagePagerAdapter) {
				imagePagerAdapter = new ImagePagerAdapter(this, CacheBean.getInstance().getItems(), new LongClickCallBack() {
					
					@Override
					public void longClick(int position) {
						final CItem item = CacheBean.getInstance().getItems().get(position);
						if (null != item) {
							final LoudingDialogIOS ldsignout = new LoudingDialogIOS(
									MainActivity.this);
							ldsignout.showOperateMessage("确定下载？");
							ldsignout.setPositiveButton("确定",
									R.drawable.dialog_positive_btn, new OnClickListener() {
								@Override
								public void onClick(View v) {
									downloadEmoji(item);
									ldsignout.dismiss();
								}
							});
						}
						
					}
				});
			}
			imagePager = new ImagePager((ViewGroup) container.getParent(), new CallBackDialogConfirm() {
				
				@Override
				public void onSubmit(HashMap<String, String> values) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onKeyBack() {
					imagePager.hide(getSupportFragmentManager());
				}
			}, imagePagerAdapter);
		}
		List<CItem> items = CacheBean.getInstance().getItems();
		imagePager.setDatas(items);
		imagePager.show(getSupportFragmentManager(),position);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// 再按一次退出程序
	@Override
	public void onBackPressed() {
		if (null != imagePager && imagePager.isShowed()) {
			imagePager.hide(getSupportFragmentManager());
		}
		else {
			long secondTime = System.currentTimeMillis();
			if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				firstTime = secondTime;// 更新firstTime
			}
			else { // 两次按键小于2秒时，退出应用
				finish();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void finish() {
		super.finish();
	}


}
