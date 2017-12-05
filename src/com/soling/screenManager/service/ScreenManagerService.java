package com.soling.screenManager.service;

import com.soling.autosdk.multiscreen.IMultiScreenCallback;
import com.soling.autosdk.multiscreen.MultiScreenConst;
import com.soling.autosdk.util.LogUtil;
import com.soling.screenManager.ScreenManager;
import com.soling.screenManager.Util.ProjectCallBack;
import com.soling.screenManager.Util.ProjectUtil;
import com.soling.screenManager.blank.PresenttationManager;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;

public class ScreenManagerService extends Service implements ProjectCallBack {
	private final String TAG = ScreenManagerService.class.getSimpleName();
	ScreenStub screenStub;
	ScreenManager screenManager;

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d(TAG, "onCreate");
		
		ProjectUtil projectUtil = new ProjectUtil(this);
		screenManager = new ScreenManager(this);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		LogUtil.d(TAG, "onBind");
		return null;
	}

	@Override
	public void onInitCompleted() {
		LogUtil.d(TAG, "onInitCompleted");
		screenStub = new ScreenStub(ScreenManagerService.this);
		ServiceManager.addService(MultiScreenConst.IBINDER_MULTISCREEN, screenStub);
	}

	public void registerCmdCallback(IMultiScreenCallback iMultoScreenCallback) {
		LogUtil.d(TAG, "registerCmdCallback iMultoScreenCallback = " + iMultoScreenCallback);
		try {
			screenManager.registerCmdCallback(iMultoScreenCallback);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isBackScreenSwitch() {
		LogUtil.d(TAG, "isBackScreenSwitch  = " + screenManager.isBackScreenSwitch());
		return screenManager.isBackScreenSwitch();
	}

	public void setBackScreenSwitch(boolean screenSwitch) {
		LogUtil.d(TAG, "setBackScreenSwitch screenSwitch = " + screenSwitch);
		screenManager.setBackScreenSwitch(screenSwitch);
	}

	public void setHalfScreenmode() {
		LogUtil.d(TAG, "setHalfScreenmode ");
		screenManager.setHalfScreenmode();
	}

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				screenManager.showNavigation();
				break;

			default:
				break;
			}
			return false;
		}
	});

	public void showNavigation() {
		// Message message=new Message();
		handler.sendEmptyMessage(0);

	}

}
