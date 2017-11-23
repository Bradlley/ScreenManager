package com.soling.screenManager;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.RemoteException;

import com.soling.autosdk.multiscreen.IMultoScreenCallback;
import com.soling.autosdk.multiscreen.MultoScreenConst;
import com.soling.autosdk.source.Source;
import com.soling.screenManager.Util.BackScreenManagerUtil;
import com.soling.screenManager.Util.HalfScreenManagerUtil;

public class ScreenManager {

	public static final int MSEEAGE_SET_BACKSCREEN = 1;
	public static final int MSEEAGE_SET_HALFSCREEN = 2;
	private IMultoScreenCallback callback;

	private int CurrentBackScreenCustom = MultoScreenConst.CurrentBackScreenCustom.APP_Blank;
	private int CurrentHalfScreenCustom = MultoScreenConst.CurrentHalfScreenCustom.APP_Blank;
	// private Context context;
	private Source source = new Source();
	private BackScreenManagerUtil backScreenManagerUtil;
	private HalfScreenManagerUtil halfScreenManagerUtil;

	public ScreenManager(Context context) {
		// this.context = context;
		backScreenManagerUtil = new BackScreenManagerUtil(this);
		halfScreenManagerUtil = new HalfScreenManagerUtil(context, this);
	}

	public void registerCmdCallback(IMultoScreenCallback callback) {
		this.callback = callback;
	}

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message message) {
			try {
				switch (message.what) {
				case MSEEAGE_SET_BACKSCREEN:
					CurrentBackScreenCustom = message.arg1;
					callback.onBackScreenResponse(CurrentBackScreenCustom);
					//TODO 判断空白
					break;
				case MSEEAGE_SET_HALFSCREEN:
					CurrentHalfScreenCustom = message.arg1;
					callback.onHalfScreenResponse(CurrentHalfScreenCustom);
					break;
				default:
					break;
				}
			} catch (RemoteException e) {
				e.printStackTrace();
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
		multoHalfScreenCallback(halfScreenManagerUtil
				.getHalfScreenCustom(source.getCurrentAudioSource()));
	}

	public void closeHalfScreen() {
		multoHalfScreenCallback(MultoScreenConst.CurrentHalfScreenCustom.APP_Blank);
	}

	public void setHalfScreenmode() {
		halfScreenManagerUtil.setHalfScreen();
	}

	public void onNaviOpenAlready() {
		if (CurrentHalfScreenCustom == MultoScreenConst.CurrentHalfScreenCustom.APP_Blank) {
			halfScreenManagerUtil.setFullScreenWithNoVideo();
		} else {
			halfScreenManagerUtil.setFullScreen();
		}

	}

	// 半屏部分
	// ///////////////////////////////////////////

	// ///////////////////////////////////////////
	// 后屏部分

	public void MultoBackScreenCallback() {
		Message message = new Message();
		message.what = MSEEAGE_SET_BACKSCREEN;
		message.arg1 = backScreenManagerUtil.getBackScreenCustom(source
				.getCurrentAudioSource());
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

}
