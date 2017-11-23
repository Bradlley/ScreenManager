package com.soling.screenManager.service;

import android.os.RemoteException;

import com.soling.autosdk.multiscreen.IMultoScreen;
import com.soling.autosdk.multiscreen.IMultoScreenCallback;

public class ScreenStub extends IMultoScreen.Stub {
	private ScreenManagerService screenManagerService;

	public ScreenStub(ScreenManagerService screenManagerService) {
		this.screenManagerService = screenManagerService;
	}

	@Override
	public void registerCmdCallback(IMultoScreenCallback iMultoScreenCallback)
			throws RemoteException {
		screenManagerService.registerCmdCallback(iMultoScreenCallback);
	}

	@Override
	public void unregisterCmdCallback(IMultoScreenCallback arg0)
			throws RemoteException {

	}

	@Override
	public boolean getCurrentBackScreenSwitch() throws RemoteException {
		screenManagerService.isBackScreenSwitch();
		return false;
	}

	@Override
	public void setCurrentBackScreenSwitch(boolean arg0) throws RemoteException {
		screenManagerService.setBackScreenSwitch(arg0);

	}

	@Override
	public void setHalfScreenmode() throws RemoteException {
		screenManagerService.setHalfScreenmode();

	}

}
