package com.soling.screenManager.Util;

import com.soling.autosdk.multiscreen.MultoScreenConst;
import com.soling.autosdk.source.SourceConst.App;
import com.soling.screenManager.ScreenManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class HalfScreenManagerUtil {
	private Context context;
	ScreenManager manager;
	private static final String ACTION_NAVI_READY = "jp.incrementp.car.navi.NAVIGATION_IS_READY";
	private static final String ACTION_NAVI_OPEN_HALF = "jp.intent.action.open.AVSIDEVIEW";
	private static final String ACTION_NAVI_CLOSE_HALF = "jp.intent.action.close.AVSIDEVIEW";
	private static final String ACTION_NAVI_CLOSE_ALREADY = "jp.incrementp.car.navi.NAVIGATION_IS_READY_TO_FINISH";

	public HalfScreenManagerUtil(Context context, ScreenManager manager) {
		this.context = context;
		this.manager = manager;
		registBroadcastReceiver();
	}

	public void registBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_NAVI_READY);// 导航启动完成
		intentFilter.addAction(ACTION_NAVI_OPEN_HALF);
		intentFilter.addAction(ACTION_NAVI_CLOSE_HALF);
		intentFilter.addAction(ACTION_NAVI_CLOSE_ALREADY);
		intentFilter.addAction("com.incrementp.broadcast.VOICE_GUIDANCE_START");
		intentFilter.addAction("com.incrementp.broadcast.VOICE_GUIDANCE_END");
		intentFilter.addAction(ACTION_APP_OPEN_HALF);
		intentFilter.addAction(ACTION_APP_CLOSE_HALF_NOVIDEO);
		intentFilter.addAction(ACTION_APP_CLOSE_HALF);
		intentFilter.addAction(ACTION_APP_CLOSE_NAVI);
		context.registerReceiver(broadcastReceiver, intentFilter);
	}

	private static final String ACTION_APP_OPEN_HALF = "com.incrementp.broadcast.sethalfscreen";// 开启半屏
	private static final String ACTION_APP_CLOSE_HALF = "com.incrementp.video.setfullscreen";// 关闭半屏
	private static final String ACTION_APP_CLOSE_HALF_NOVIDEO = "com.incrementp.novideo.setfullscreen";// 关闭半屏无空白
	private static final String ACTION_APP_CLOSE_NAVI = "jp.incrementp.ceam.EXTEND_ACTION_NAVI_SUSPEND";// 关闭导航

	/**
	 * app请求开启半屏幕显示模式
	 */
	public void setHalfScreen() {
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_OPEN_HALF);
		context.sendBroadcast(intent);
	}

	/**
	 * app请求开启全屏幕显示模式
	 */
	public void setFullScreen() {
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_CLOSE_HALF);
		context.sendBroadcast(intent);
	}

	/**
	 * app请求开启全屏幕显示模式无av模式
	 */
	public void setFullScreenWithNoVideo() {
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_CLOSE_HALF_NOVIDEO);
		context.sendBroadcast(intent);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String name = intent.getAction();
			Log.d("wangyong", "###########name:" + name);
			if (ACTION_NAVI_READY.equals(name)) {
				manager.onNaviOpenAlready();
			} else if (ACTION_NAVI_OPEN_HALF.equals(name)) {
				manager.setHalfScreenToApp();
			} else if (ACTION_NAVI_CLOSE_HALF.equals(name)) {
				manager.closeHalfScreen();
			} else if (ACTION_NAVI_CLOSE_ALREADY.equals(name)) {
				manager.closeHalfScreen();
			}
		}
	};

	public int getHalfScreenCustom(App app) {
		if (app != null) {
			switch (app) {
			case USB_VIDEO:
				return MultoScreenConst.CurrentBackScreenCustom.APP_USB_VIDEO;
			case DECK:
				return MultoScreenConst.CurrentBackScreenCustom.APP_DVD_VIDEO;
			case AUXIN:
				return MultoScreenConst.CurrentBackScreenCustom.APP_AUX;
			case SDCARD_VIDEO:
				return MultoScreenConst.CurrentBackScreenCustom.APP_SD_VIDEO;
			case DTV:
				return MultoScreenConst.CurrentBackScreenCustom.APP_TV;
			default:
				return MultoScreenConst.CurrentBackScreenCustom.APP_Blank;
			}
		}
		return MultoScreenConst.CurrentBackScreenCustom.APP_Blank;
	}

}
