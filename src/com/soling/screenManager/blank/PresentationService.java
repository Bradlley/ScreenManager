package com.soling.screenManager.blank;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Binder;
import android.os.IBinder;
import android.view.Display;

import java.util.ArrayList;
import java.util.List;

public class PresentationService extends Service {
	public final String TAG = getClass().getName();

	private IBinder mBinder = new LocalBinder();

	private List<DemoPresentation> mPresentations;

	private DemoPresentation mDemoPresentation;

	private boolean isSync = false;

	class LocalBinder extends Binder {
		public PresentationService getService() {
			return PresentationService.this;
		}
	}

	public PresentationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public void showPresentation(Context context, Display display) {
		if (mDemoPresentation == null)
			mDemoPresentation = new DemoPresentation(this, display);
		mDemoPresentation.show();
		isSync = true;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void disMiss() {
		if (mDemoPresentation != null) {
			mDemoPresentation.dismiss();
			stopForeground(true);
		}
		isSync = false;
	}
}
