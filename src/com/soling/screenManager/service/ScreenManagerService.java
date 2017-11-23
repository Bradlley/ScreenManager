package com.soling.screenManager.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.ServiceManager;

import com.soling.autosdk.multiscreen.IMultoScreenCallback;
import com.soling.autosdk.multiscreen.MultoScreenConst;
import com.soling.screenManager.ScreenManager;
import com.soling.screenManager.Util.ProjectCallBack;
import com.soling.screenManager.Util.ProjectUtil;

public class ScreenManagerService extends Service implements ProjectCallBack {
	ScreenStub screenStub;
	ScreenManager screenManager;

	@Override
	public void onCreate() {
		super.onCreate();
		ProjectUtil projectUtil = new ProjectUtil(this);
		screenManager = new ScreenManager(this);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onInitCompleted() {
		screenStub = new ScreenStub(ScreenManagerService.this);
		ServiceManager.addService(MultoScreenConst.IBINDER_MULTOSCREEN,
				screenStub);
	}

	public void registerCmdCallback(IMultoScreenCallback iMultoScreenCallback) {
		screenManager.registerCmdCallback(iMultoScreenCallback);
	}

	public boolean isBackScreenSwitch() {
		return screenManager.isBackScreenSwitch();
	}

	public void setBackScreenSwitch(boolean screenSwitch) {
		screenManager.setBackScreenSwitch(screenSwitch);
	}

	public void setHalfScreenmode() {
		screenManager.setHalfScreenmode();
	}

}
