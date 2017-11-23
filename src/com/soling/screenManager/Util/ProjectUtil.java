package com.soling.screenManager.Util;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.soling.autosdk.os.Soc;
import com.soling.autosdk.os.constant.ParaConst;
import com.soling.autosdk.os.constant.Project;

public class ProjectUtil {

	private static Map<String, String> properMap = new HashMap<String, String>();
	private static String TAG = "DtvService_PropertiesUtil";
	private static int projectId = 0;
	Context mContext;
	Soc mSoc;
	private static int MSG_UPDATE_INFO = 1;
	ProjectCallBack projectCallBack;
	Handler mThreadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (!getPropertiesFromAutoCore()) {
				mThreadHandler.sendEmptyMessageDelayed(MSG_UPDATE_INFO, 1000);
			}
		}
	};

	public ProjectUtil(ProjectCallBack projectCallBack) {

		mSoc = new Soc();
		this.projectCallBack = projectCallBack;
		mThreadHandler.sendEmptyMessage(MSG_UPDATE_INFO);

	}

	private Boolean getPropertiesFromAutoCore() {
		HashMap<String, String> tempProperMap = mSoc.getProperties();
		if (tempProperMap != null) {
			properMap = tempProperMap;
			Project.setCurrentProject(mSoc.getModel());
			if (projectCallBack != null)
				projectCallBack.onInitCompleted();
			return true;
		}
		return false;
	}

	/**
	 * 根据key获取int型参数值
	 * 
	 * @param para
	 *            key值
	 * @return 读取失败返回-1
	 */
	public static int getIntProperties(String para) {
		if (para.isEmpty()) {
			return -1;
		}
		if (properMap.containsKey(para)) {
			String value = properMap.get(para);

			if (value.matches("[0-9]+")) {
				return Integer.valueOf(value);
			} else {
				return -1;
			}
		}
		return -1;
	}

	/**
	 * 根据key获取String参数值
	 * 
	 * @param para
	 *            key值
	 * @return 读取失败返回null或者""
	 */
	public static String getStringProperties(String para) {
		if (para.isEmpty()) {
			return "";
		}
		if (properMap.containsKey(para)) {
			return properMap.get(para);
		}
		return "";
	}

	/**
	 * 根据key获取Boolean参数值
	 * 
	 * @param para
	 *            key值
	 * @return 读取失败返回false
	 */
	public static Boolean getBooleanProperties(String para) {
		if (para.isEmpty()) {
			return false;
		}
		if (properMap.containsKey(para)) {
			return ParaConst.TRUE.equals(properMap.get(para));
		}
		return false;
	}

	public static Map<String, String> getProperties() {
		// TODO Auto-generated method stub
		return properMap;
	}

	public static int getProjectId() {
		return Project.getProjectId();
	}
}
