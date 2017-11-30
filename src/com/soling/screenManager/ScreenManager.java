package com.soling.screenManager;

import com.soling.autosdk.multiscreen.IMultiScreenCallback;
import com.soling.autosdk.multiscreen.MultiScreenConst;
import com.soling.autosdk.source.Source;
import com.soling.autosdk.source.SourceConst.App;
import com.soling.autosdk.util.LogUtil;
import com.soling.screenManager.Util.BackScreenManagerUtil;
import com.soling.screenManager.Util.HalfScreenManagerUtil;
import com.soling.screenManager.blank.PresenttationManager;

import android.R.bool;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

public class ScreenManager {

	public static final int MSEEAGE_SET_BACKSCREEN = 1;
	public static final int MSEEAGE_SET_HALFSCREEN = 2;
	private RemoteCallbackList<IMultiScreenCallback> mListCallback = new RemoteCallbackList<IMultiScreenCallback>();

	private int CurrentBackScreenCustom = MultiScreenConst.CurrentBackScreenCustom.APP_Blank;
	private int CurrentHalfScreenCustom = MultiScreenConst.CurrentHalfScreenCustom.APP_Blank;
	// private Context context;
	private Source source = new Source();
	private BackScreenManagerUtil backScreenManagerUtil;
	private HalfScreenManagerUtil halfScreenManagerUtil;

	Context context;

	public ScreenManager(Context context) {
		this.context = context;
		backScreenManagerUtil = new BackScreenManagerUtil(this);
		halfScreenManagerUtil = new HalfScreenManagerUtil(context, this);
		PresenttationManager.getInstence(null).showAllDisPlay();// 默認blank显示
	}

	public void registerCmdCallback(IMultiScreenCallback callback) throws RemoteException {
		if (null != callback) {
			mListCallback.register(callback);
		}
		LogUtil.v("ScreenManager",
				"registerLibCallback ac : " + ((callback == null) ? null : callback.getClass().getName()));
	}

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message message) {
			switch (message.what) {
			case MSEEAGE_SET_BACKSCREEN:
				CurrentBackScreenCustom = message.arg1;
				if (CurrentBackScreenCustom == MultiScreenConst.CurrentBackScreenCustom.APP_Blank) {
					PresenttationManager.getInstence(null).showAllDisPlay();
				}
				onBackScreenResponse(CurrentBackScreenCustom);

				// TODO 判断空白
				break;
			case MSEEAGE_SET_HALFSCREEN:
				CurrentHalfScreenCustom = message.arg1;
				onHalfScreenResponse(CurrentHalfScreenCustom);
				break;
			default:
				break;
			}
			return true;
		}
	});

	// ///////////////////////////////////////////
	// 半屏部分

	public void multoHalfScreenCallback(int app) {
		Message message = new Message();
		message.what = MSEEAGE_SET_HALFSCREEN;
		message.arg1 = app;
		handler.sendMessage(message);
	}

	public void setHalfScreenToApp() {
		setShowMode(NAVI_MODE_HALF);
		multoHalfScreenCallback(halfScreenManagerUtil.getHalfScreenCustom(source.getCurrentMediaAudioApp()));
	}

	public void closeHalfScreen() {
		setShowMode(NAVI_MODE_MEDIA);
		multoHalfScreenCallback(MultiScreenConst.CurrentHalfScreenCustom.APP_Blank);
	}

	public void setHalfScreenmode() {
		halfScreenManagerUtil.setHalfScreen();
	}

	public void onNaviOpenAlready() {
		if (halfScreenManagerUtil.getHalfScreenCustom(
				source.getCurrentMediaAudioApp()) == MultiScreenConst.CurrentHalfScreenCustom.APP_Blank) {
			halfScreenManagerUtil.setFullScreenWithNoVideo();
		} else {
			halfScreenManagerUtil.setFullScreen();
		}
	}

	public final int NAVI_MODE_DEFAULT = 0;
	public final int NAVI_MODE_MEDIA = 1;
	public final int NAVI_MODE_NO_MEDIA = 2;
	public final int NAVI_MODE_HALF = 3;
	private int showMode = NAVI_MODE_DEFAULT;// mode =0第一次启动模式 mode =1 初始化界面
												// mode=2 全屏带有novideo模式 mode =3
												// 半屏显示

	public void setShowMode(int showMode) {
		this.showMode = showMode;
	}

	private void showNavigationMediaMode() {
		if (halfScreenManagerUtil.getHalfScreenCustom(
				source.getCurrentMediaAudioApp()) == MultiScreenConst.CurrentHalfScreenCustom.APP_Blank) {
			halfScreenManagerUtil.setFullScreenWithNoVideo();
		} else {
			halfScreenManagerUtil.setFullScreen();
		}
	}

	private void showNavigationNoMediaMode() {
		if (halfScreenManagerUtil.getHalfScreenCustom(
				source.getCurrentMediaAudioApp()) == MultiScreenConst.CurrentHalfScreenCustom.APP_Blank) {
			halfScreenManagerUtil.setFullScreenWithNoVideo();
		} else {
			halfScreenManagerUtil.setFullScreen();
		}
	}

	private void showNavigationHalf() {
		if (halfScreenManagerUtil.getHalfScreenCustom(
				source.getCurrentMediaAudioApp()) == MultiScreenConst.CurrentHalfScreenCustom.APP_Blank) {
			halfScreenManagerUtil.setFullScreenWithNoVideo();
		} else {
			halfScreenManagerUtil.setHalfScreen();
		}
	}

	private final String NAVIGATION_PACKAGE_NAME = "jp.pioneer.car.navi";

	public void showNavigation() {
		if (halfScreenManagerUtil.isProessRunning(NAVIGATION_PACKAGE_NAME)) {
			switch (showMode) {
			// case NAVI_MODE_DEFAULT:
			// halfScreenManagerUtil.doStartApplicationWithPackageName("jp.pioneer.car.navi");
			// break;
			case NAVI_MODE_MEDIA:
				showNavigationMediaMode();
				break;
			case NAVI_MODE_NO_MEDIA:
				showNavigationNoMediaMode();
				break;
			case NAVI_MODE_HALF:
				showNavigationHalf();
				break;

			default:
				break;
			}
		} else {
			halfScreenManagerUtil.doStartApplicationWithPackageName(NAVIGATION_PACKAGE_NAME);
		}
	}

	// 半屏部分
	// ///////////////////////////////////////////

	// 倒车特殊处理部分

	public void onReverseStateResponse(boolean response) {
		if (response) {
			// MultoBackScreenCallback();
			Message message = new Message();
			message.what = MSEEAGE_SET_BACKSCREEN;
			message.arg1 = MultiScreenConst.CurrentBackScreenCustom.APP_Blank;
			// 获取当前音频源
			handler.sendMessage(message);
		} else {
			MultoBackScreenCallback();
			if (source.getCurrentSource().equals(App.MX_NAVI)) {
				showNavigation();
			}
		}

	}

	// ///////////////////////////////////////////
	// 后屏部分
	public void registScreenSource() {
		try {
			backScreenManagerUtil.registAutoCore();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void MultoBackScreenCallback() {
		Message message = new Message();
		message.what = MSEEAGE_SET_BACKSCREEN;
		message.arg1 = backScreenManagerUtil.getBackScreenCustom(source.getCurrentMediaAudioApp());
		// 获取当前音频源
		handler.sendMessage(message);
	}

	public int getCurrentBackScreenCustom() {
		return CurrentBackScreenCustom;
	}

	public boolean isBackScreenSwitch() {
		return backScreenManagerUtil.screenSwitch;
	}

	public void setBackScreenSwitch(boolean screenSwitch) {
		backScreenManagerUtil.screenSwitch = screenSwitch;
		MultoBackScreenCallback();// 設置完回掉
	}
	// 后屏部分
	// ///////////////////////////////////////////

	public synchronized void onBackScreenResponse(int source) {
		final int N = mListCallback.beginBroadcast();
		LogUtil.d("ScreenManager", "onBackScreenResponse source =: " + source);
		for (int i = 0; i < N; i++) {
			try {
				mListCallback.getBroadcastItem(i).onBackScreenResponse(source);
			} catch (RemoteException e) {

			}
		}
		mListCallback.finishBroadcast();
	}

	public synchronized void onHalfScreenResponse(int source) {
		final int N = mListCallback.beginBroadcast();
		LogUtil.d("ScreenManager", "onHalfScreenResponse source =: " + source);
		for (int i = 0; i < N; i++) {
			try {
				mListCallback.getBroadcastItem(i).onHalfScreenResponse(source);
			} catch (RemoteException e) {

			}
		}
		mListCallback.finishBroadcast();
	}

}
