package com.soling.screenManager.Util;

import android.os.RemoteException;

import com.soling.autosdk.multiscreen.MultoScreenConst;
import com.soling.autosdk.source.SourceConst.App;
import com.soling.autosdk.systemui.Ui;
import com.soling.autosdk.systemui.imp.UiListenerImp;
import com.soling.screenManager.ScreenManager;

public class BackScreenManagerUtil {

	public boolean screenSwitch;
	private ScreenManager manager;

	public BackScreenManagerUtil(ScreenManager screenManager) {
		this.manager = screenManager;
		try {
			Ui.getInstance().registerListener(new UiListenerImp() {
				@Override
				public void updateMenuAppResponse(App app) {
					manager.MultoBackScreenCallback();
				}
			});
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public boolean isScreenSwitch() {
		return screenSwitch;
	}

	public void setScreenSwitch(boolean screenSwitch) {
		this.screenSwitch = screenSwitch;
	}

	public int getBackScreenCustom(App app) {
		if (screenSwitch && app != null) {
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
