package com.doug.component.ui;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.bumptech.glide.Glide;
import com.doug.component.bean.MainAD;
import com.doug.component.cache.CacheBean;
import com.doug.component.utils.ImageUtils;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.utils.StringUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author doug
 *
 */
public class AtyMainAD extends KJActivity implements OnClickListener {


	private TextView leftTime;
	
	private Timer mTimer;
    private TimerTask mTimerTask;
    private int count = 3;
    
    private MainAD mainAD;
    
    public static String GOODS_ID = "goodsId";
    
    private ImageView ad;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ad);
        
        mainAD = CacheBean.getInstance().getAd();
        findViewById(R.id.avoidAD).setOnClickListener(this);
        ad = (ImageView) findViewById(R.id.AD);
        ad.setOnClickListener(this);
        leftTime = (TextView) findViewById(R.id.leftTime);
        leftTime.setText(count + "s");
        initUI();
        startTimeTask();
    }
    
	private void initUI() {
		String filePath = ImageUtils.getImagePath(mainAD.getImg());
		if (StringUtils.isEmpty(filePath) || !new File(filePath).exists()) {
			goToNextView(false);
		}
		else {
			Glide.with(this).load(new File(filePath)).into(ad);
		}
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avoidAD:
            	goToNextView(false);
                break;
            case R.id.AD:
            	goForGoods();
                break;
            default:
                break;
        }

    }
    
    /**
     * 执行跳转
     * @param ForGoods
     */
	private void goToNextView(boolean ForGoods) {
    	mTimer.cancel();
		Intent intent = null;
		intent = new Intent(this, AtyHome.class);
		if (ForGoods && !TextUtils.isEmpty(mainAD.getLink())) {
			intent.putExtra(GOODS_ID, mainAD.getLink());
		}
		startActivity(intent);
		this.finish();
	}
    
    private void goForGoods() {
    	if (mainAD.getLinkType() == MainAD.GOODS) {
    		goToNextView(true);
        }
        else if (mainAD.getLinkType() == MainAD.LINK){
        	Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_VIEW);  
            intent.addCategory(Intent.CATEGORY_BROWSABLE); 
        	intent.setData(Uri.parse(mainAD.getLink()));
        	startActivity(intent);
        }
    }
    

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != mTimer)
			mTimer.cancel();
		mTimer = null;
	}



	/**
	 * 更新时间
	 */
	private Handler handler = new Handler() {
		
		@Override 
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				leftTime.setText(count + "s");
			}
			else if (msg.what == 1) {
				goToNextView(false);
			}
		};
		
	};
    
	/**
	 * 开启条跳转倒计时
	 */
    private void startTimeTask() {
    	
            mTimer = new Timer();
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                	if (activityState != ActivityState.RESUME) return;
                    count--;
                    Message msg = new Message(); 
                    //更新时间
                    msg.what = 0;
                    if (count == 0) {
                    	//执行跳转
                    	msg.what = 1;
                    }
                    handler.sendMessage(msg);
                }
            };
            //开始一个定时任务
            mTimer.schedule(mTimerTask, 1000, 1000);
    }

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}
    
}
