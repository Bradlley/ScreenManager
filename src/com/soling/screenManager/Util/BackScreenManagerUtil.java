package com.soling.screenManager.Util;

import com.soling.autosdk.multiscreen.MultiScreenConst;
import com.soling.autosdk.source.SourceConst.App;
import com.soling.autosdk.systemui.Ui;
import com.soling.autosdk.systemui.imp.UiListenerImp;
import com.soling.autosdk.vehicle.AVMInfo;
import com.soling.autosdk.vehicle.AmbientLightInfo;
import com.soling.autosdk.vehicle.CarBodyStatusInfo;
import com.soling.autosdk.vehicle.RadarInfo;
import com.soling.autosdk.vehicle.TpmsInfo;
import com.soling.autosdk.vehicle.Vehicle;
import com.soling.autosdk.vehicle.imp.VehicleListenerImp;
import com.soling.screenManager.ScreenManager;

import android.os.RemoteException;

public class BackScreenManagerUtil {

	public boolean screenSwitch=true;
	private ScreenManager manager;

	public BackScreenManagerUtil(ScreenManager screenManager) {
		this.manager = screenManager;
//		try {
//			registAutoCore();
//		} catch (Exception e) {
//			try {
//				Thread.sleep(1000);
//				registAutoCore();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		}
	}

	
	
	public void registAutoCore() throws RemoteException{
		Ui.getInstance().registerListener(new UiListenerImp() {
			@Override
			public void updateMenuAppResponse(App app) {
				manager.MultoBackScreenCallback();
			}
		});
		
		Vehicle.getInstance().registerListener(new VehicleListenerImp() {
			

			
			@Override
			public void onReverseStateResponse(boolean response) {
				manager.onReverseStateResponse(response);
			}
			
		});

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

}
