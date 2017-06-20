package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * 一个列表测试界面，测试测试
 * @author qtds
 *
 */
public class ListviewTestActivity extends BaseActivity {
	private static String TAG = "ListviewTestActivity";
	private List<String> dataList = new ArrayList<String>();
	private ListView listView;
	private ArrayAdapter<String> arrayAdapter;
	private PullToRefreshListView pullToRefreshListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testlistview);
		((MyApplication) getApplication()).addActivity(this);
	}

	public void setdata(int index) {

		for (int i = index; i < index + 5; i++) {
			dataList.add("测试       测试    这个是第" + i);

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() {

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listView1);
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_listview_empty_show_congzhi, null);
		pullToRefreshListView.setEmptyView(view);
		pullToRefreshListView.setMode(Mode.BOTH);
		ILoadingLayout startLabels = pullToRefreshListView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在加载...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onPullDownToRefresh");

				dataList.clear();
				arrayAdapter.notifyDataSetChanged();// 这里下拉重新加载数据时，清楚原来数据的动作放在那？需要考虑
				setdata(10);
				arrayAdapter.notifyDataSetChanged();

				pullToRefreshListView.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

							@Override
							public void run() {
								// TODO Auto-generated method stub
								pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
								// 我们已经更新完成
							}
						}, 1000);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub
				Log.d(TAG, "onPullUpToRefresh");
				setdata(10);
				arrayAdapter.notifyDataSetChanged();
				pullToRefreshListView.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
						// 我们已经更新完成
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("11", (String) ((TextView) arg1).getText());
				Log.d("11", "" + arg2);
				Log.d("11", "" + arg3);
			}
		});
	}
}
