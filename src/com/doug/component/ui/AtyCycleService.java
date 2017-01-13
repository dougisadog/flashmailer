package com.doug.component.ui;

import com.doug.component.support.UIHelper;
import com.doug.component.support.service.CycleRequestService;
import com.doug.component.utils.ServiceUtils;
import com.doug.component.utils.ServiceUtils.ServiceIntentCallBack;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 
 * @author doug
 *
 */
public class AtyCycleService extends KJActivity implements OnClickListener {
	
	@BindView(id = R.id.content)
	private TextView content;
	@BindView(id = R.id.btnStart, click = true)
	private TextView btnStart;
	@BindView(id = R.id.btnStop, click = true)
	private TextView btnStop;
	
	private MyReceiver receiver;
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_cycle_service);
        UIHelper.setTitleView(this, "", "请选择你所在的市区");
        initReciever();
		
	}
	
	private void initReciever() {
		 receiver=new MyReceiver();
         IntentFilter filter=new IntentFilter();
         filter.addAction(CycleRequestService.ACTION);
         registerReceiver(receiver,filter);
	}
	
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
            	ServiceUtils.checkService(this, CycleRequestService.class,
						new ServiceIntentCallBack() {
					@Override
					public void setServiceIntent(Intent intent) {
					}
				});
                break;
            case R.id.btnStop:
            	ServiceUtils.stopService(this, CycleRequestService.class);
                break;
            default:
                break;
        }

    }
    
    
    
    
    
	@Override
	protected void onDestroy() {
		if (null != receiver) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		super.onDestroy();
	}





	public class MyReceiver extends BroadcastReceiver {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (null != context && null != intent
					&& CycleRequestService.ACTION.equals(intent.getAction())) {
				String text = intent.getStringExtra("text");
				content.setText(text);
			}

		}

	}
}
