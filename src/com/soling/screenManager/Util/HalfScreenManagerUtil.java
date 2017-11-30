package com.soling.screenManager.Util;

import java.util.List;

import com.soling.autosdk.multiscreen.MultiScreenConst;
import com.soling.autosdk.os.SocConst;
import com.soling.autosdk.source.SourceConst.App;
import com.soling.screenManager.ScreenManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.util.Log;

public class HalfScreenManagerUtil {
	private Context context;
	private ScreenManager manager;
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
		intentFilter.addCategory("android.intent.category.DEFAULT");
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
		intentFilter.addAction(SocConst.ACTION_AUTO_CORE);
		intentFilter.addAction(JP_NAVI_START);
		context.registerReceiver(broadcastReceiver, intentFilter);
	}

	private static final String ACTION_APP_OPEN_HALF = "com.incrementp.broadcast.sethalfscreen";// 开启半屏
	private static final String ACTION_APP_CLOSE_HALF = "com.incrementp.video.setfullscreen";// 关闭半屏
	private static final String ACTION_APP_CLOSE_HALF_NOVIDEO = "com.incrementp.novideo.setfullscreen";// 关闭半屏无空白
	private static final String ACTION_APP_CLOSE_NAVI = "jp.incrementp.ceam.EXTEND_ACTION_NAVI_SUSPEND";// 关闭导航
	private static final String ACTION_APP_POSITION = "jp.incrementp.car.navi.SHOW_CURRENT_VEHICLE_POSITION";// 显示当前地图位置

	private static final String JP_NAVI_START = "com.soling.JP_NAVI_START";


	/**
	 * app请求开启半屏幕显示模式
	 */
	public void setHalfScreen() {
		manager.setShowMode(manager.NAVI_MODE_HALF);
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_OPEN_HALF);
		context.sendBroadcast(intent);
		setStatusToAutoCore();
	}

	/**
	 * app请求开启全屏幕显示模式
	 */
	public void setFullScreen() {
		manager.setShowMode(manager.NAVI_MODE_MEDIA);
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_CLOSE_HALF);
		context.sendBroadcast(intent);
		setStatusToAutoCore();
	}

	/**
	 * app请求开启全屏幕显示模式无av模式
	 */
	public void setFullScreenWithNoVideo() {
		manager.setShowMode(manager.NAVI_MODE_NO_MEDIA);
		Intent intent = new Intent();
		intent.setAction(ACTION_APP_CLOSE_HALF_NOVIDEO);
		context.sendBroadcast(intent);
		setStatusToAutoCore();
	}
	
	/**
	 * app请求开启全屏幕显示模式无av模式
	 */
	public void setStatusToAutoCore() {
		Intent intent = new Intent();
		intent.setAction(JP_NAVI_START);
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
				manager.setShowMode(manager.NAVI_MODE_DEFAULT);
			} else if (SocConst.ACTION_AUTO_CORE.equals(name)) {
				manager.registScreenSource();
			}
		}
	};

	public int getHalfScreenCustom(App app) {
		if (app != null) {
			switch (app) {
			case USB_VIDEO:
				return MultiScreenConst.CurrentBackScreenCustom.APP_USB_VIDEO;
			case DECK:
				return MultiScreenConst.CurrentBackScreenCustom.APP_DVD_VIDEO;
			case AUXIN:
				return MultiScreenConst.CurrentBackScreenCustom.APP_AUX;
			case SDCARD_VIDEO:
				return MultiScreenConst.CurrentBackScreenCustom.APP_SD_VIDEO;
			case DTV:
				return MultiScreenConst.CurrentBackScreenCustom.APP_TV;
			default:
				return MultiScreenConst.CurrentBackScreenCustom.APP_Blank;
			}
		}
		return MultiScreenConst.CurrentBackScreenCustom.APP_Blank;
	}

	public void doStartApplicationWithPackageName(String packagename) {

		// 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
		PackageInfo packageinfo = null;
		try {
			packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (packageinfo == null) {
			return;
		}

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packageinfo.packageName);

		// 通过getPackageManager()的queryIntentActivities方法遍历
		List<ResolveInfo> resolveinfoList = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

		ResolveInfo resolveinfo = resolveinfoList.iterator().next();
		if (resolveinfo != null) {
			// packagename = 参数packname
			String packageName = resolveinfo.activityInfo.packageName;
			// 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
			String className = resolveinfo.activityInfo.name;
			// LAUNCHER Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			// 设置ComponentName参数1:packagename参数2:MainActivity路径
			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}
    public boolean isProessRunning(String proessName) {  
        
        boolean isRunning = false;  
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);  
      
        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();  
        for(RunningAppProcessInfo info : lists){  
            if(info.processName.equals(proessName)){  
                //Log.i("Service2进程", ""+info.processName);  
                isRunning = true;  
            }  
        }  
          
        return isRunning;  
    }  

}
