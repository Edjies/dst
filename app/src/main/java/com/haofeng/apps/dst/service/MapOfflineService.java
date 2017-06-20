package com.haofeng.apps.dst.service;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.haofeng.apps.dst.httptools.Network;
import com.haofeng.apps.dst.utils.PublicUtil;

/**
 * 后台下载离线地图
 * 
 * @author Administrator
 * 
 */
public class MapOfflineService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private MKOfflineMap mOffline = null;
	private int cityid = -1;
	private ArrayList<MKOLUpdateElement> localMapList = null;
	private boolean hasload = false;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		if (Network.checkNetWorkType(getApplication()) == Network.WIFI) {

			mOffline = new MKOfflineMap();
			mOffline.init(mkOfflineMapListener);
			localMapList = mOffline.getAllUpdateInfo();
			String city = PublicUtil.getStorage_string(getApplication(),
					"mycity", "");

			if (city != null && !TextUtils.isEmpty(city)) {
				ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
				if (records == null || records.size() != 1) {
					stopSelf();
					return;
				}
				cityid = records.get(0).cityID;
				PublicUtil.logDbug("@@@", cityid + "", 0);

				if (localMapList != null) {
					for (int i = 0; i < localMapList.size(); i++) {
						PublicUtil.logDbug(
								"@@@",
								localMapList.get(i).cityName
										+ localMapList.get(i).cityID
										+ localMapList.get(i).update, 0);
						if (cityid == localMapList.get(i).cityID
								&& !localMapList.get(i).update
								&& localMapList.get(i).ratio == 100) {
							hasload = true;
							break;

						}

					}
				}
				PublicUtil.logDbug("@@@", hasload + "", 0);
				if (!hasload) {
					mOffline.start(cityid);
				} else {
					stopSelf();
				}

			}
		} else {
			stopSelf();
		}

	}

	@Override
	public void onDestroy() {
		if (mOffline != null) {
			mOffline.destroy();
		}

		super.onDestroy();
	}

	private MKOfflineMapListener mkOfflineMapListener = new MKOfflineMapListener() {

		@Override
		public void onGetOfflineMapState(int arg0, int arg1) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
				MKOLUpdateElement update = mOffline.getUpdateInfo(arg1);

				PublicUtil.logDbug("OfflineDemo", String.format("%s : %d%%",
						update.cityName, update.ratio), 0);
				// 处理下载进度更新提示

			}
				break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				// 有新离线地图安装
				PublicUtil.logDbug("OfflineDemo",
						String.format("add offlinemap num:%d", arg1), 0);
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// 版本更新提示
				// MKOLUpdateElement e = mOffline.getUpdateInfo(state);

				break;
			default:
				break;
			}
		}
	};

}
