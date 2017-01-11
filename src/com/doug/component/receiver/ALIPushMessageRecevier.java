package com.doug.component.receiver;

import java.util.Map;

import com.alibaba.sdk.android.push.notification.CPushMessage;

import android.content.Context;
import android.content.Intent;

public class ALIPushMessageRecevier extends com.alibaba.sdk.android.push.MessageReceiver{

	@Override
	public void onHandleCall(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		super.onHandleCall(arg0, arg1);
	}

	@Override
	protected void onMessage(Context arg0, CPushMessage arg1) {
		// TODO Auto-generated method stub
		super.onMessage(arg0, arg1);
	}

	@Override
	protected void onNotification(Context arg0, String arg1, String arg2,
			Map<String, String> arg3) {
		// TODO Auto-generated method stub
		super.onNotification(arg0, arg1, arg2, arg3);
	}

	@Override
	protected void onNotificationClickedWithNoAction(Context arg0, String arg1,
			String arg2, String arg3) {
		// TODO Auto-generated method stub
		super.onNotificationClickedWithNoAction(arg0, arg1, arg2, arg3);
	}

	@Override
	protected void onNotificationOpened(Context arg0, String arg1, String arg2,
			String arg3) {
		// TODO Auto-generated method stub
		super.onNotificationOpened(arg0, arg1, arg2, arg3);
	}

	@Override
	protected void onNotificationReceivedInApp(Context arg0, String arg1,
			String arg2, Map<String, String> arg3, int arg4, String arg5,
			String arg6) {
		// TODO Auto-generated method stub
		super.onNotificationReceivedInApp(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	@Override
	protected void onNotificationRemoved(Context arg0, String arg1) {
		// TODO Auto-generated method stub
		super.onNotificationRemoved(arg0, arg1);
	}
	
	

}
