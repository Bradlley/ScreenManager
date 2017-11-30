package com.soling.screenManager.service;

import com.soling.autosdk.multiscreen.IMultiScreenCallback;
import com.soling.autosdk.multiscreen.imp.IMultiScreenImp;

import android.os.RemoteException;

public class ScreenStub extends IMultiScreenImp.Stub {
	private ScreenManagerService screenManagerService;

	public ScreenStub(ScreenManagerService screenManagerService) {
		this.screenManagerService = screenManagerService;
	}


	@Override
	public boolean getCurrentBackScreenSwitch() throws RemoteException {
		return screenManagerService.isBackScreenSwitch();
	}

	@Override
	public void setCurrentBackScreenSwitch(boolean arg0) throws RemoteException {
		screenManagerService.setBackScreenSwitch(arg0);

	}

	@Override
	public void setHalfScreenmode() throws RemoteException {
		screenManagerService.setHalfScreenmode();

	}

	@Override
	public void registerCmdCallback(IMultiScreenCallback iMultoScreenCallback) throws RemoteException {
		screenManagerService.registerCmdCallback(iMultoScreenCallback);
		
	}

	@Override
	public void unregisterCmdCallback(IMultiScreenCallback arg0) throws RemoteException {
		
	}


	@Override
	public void showNavigation() throws RemoteException {
		screenManagerService.showNavigation();
		
	}

}
