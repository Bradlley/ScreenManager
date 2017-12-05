package com.soling.screenManager;

import android.app.Application;
import android.content.Intent;
import android.os.Process;

import com.soling.autosdk.util.LogUtil;
import com.soling.screenManager.blank.PresenttationManager;
import com.soling.screenManager.service.ScreenManagerService;

/**
 * This class replaces boot receiver mechanism.
 * @author sl Middleware Team
 */
public final class SolingApplication extends Application {
	
	private static final String TAG = SolingApplication.class.getSimpleName();    
	
	@Override
	public void onCreate() {
		super.onCreate();
		startService(new Intent(this, ScreenManagerService.class));
		LogUtil.i(TAG, "DtvService start! Pid="+Process.myPid());
		PresenttationManager.getInstence(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
