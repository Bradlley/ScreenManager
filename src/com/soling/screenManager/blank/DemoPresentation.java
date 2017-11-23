package com.soling.screenManager.blank;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/4/11.
 */

public class DemoPresentation extends Presentation {
	private LinearLayout mLayout;

	public DemoPresentation(Context outerContext, Display display) {
		super(outerContext, display);
		getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		// mContext = context;
	}

	public DemoPresentation(Context outerContext, Display display, int theme) {
		super(outerContext, display, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createLayout();
		setContentView(mLayout);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private void createLayout() {
		mLayout = new LinearLayout(getContext());
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		mLayout.setLayoutParams(p);

	}
}
