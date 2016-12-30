package com.doug.component.ui;

import com.baidu.mapapi.map.Text;
import com.baidu.tts.BaiduTTSManager;
import com.doug.component.support.UIHelper;
import com.doug.flashmailer.R;
import com.doug.flashmailer.R.color;
import com.louding.frame.KJActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @author doug
 *
 */
public class AtyTTS extends KJActivity implements OnClickListener {
	
	private EditText txtContent,delay;
	private TextView btn, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        UIHelper.setTitleView(this, "", "百度语音测试");
        txtContent = (EditText) findViewById(R.id.txtContent);
        delay = (EditText) findViewById(R.id.delay);
        btn = (TextView) findViewById(R.id.btn);
        btn.setOnClickListener(this);
        btnStop = (TextView) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);
    }
    
    private void refreshBtns(boolean start) {
    	if (start) {
    		btn.setBackgroundResource(R.drawable.btn_blue);
    		btn.setEnabled(true);
    		btnStop.setBackgroundColor(color.grey_btn);
    		btnStop.setEnabled(false);
    	}
    	else {
    		btn.setBackgroundColor(color.grey_btn);
    		btn.setEnabled(false);
    		btnStop.setBackgroundResource(R.drawable.btn_blue);
    		btnStop.setEnabled(true);
    	}
    }
    
    private Handler handle = new Handler();
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
            	refreshBtns(false);
            	final int delayTime = Integer.parseInt(delay.getText().toString());
            	final int content = Integer.parseInt(txtContent.getText().toString());
            	
            	handle.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						int c = (int) (Math.random() * content);
						if (BaiduTTSManager.getInstance().isFinish()) 
						BaiduTTSManager.getInstance().speak("当前数字为" + c);
						handle.postDelayed(this, delayTime);
						
					}
				}, delayTime);
            	
                break;
            case R.id.btnStop:
            	refreshBtns(true);
            	BaiduTTSManager.getInstance().stop();
            	handle.removeCallbacksAndMessages(null);
            default:
                break;
        }

    }

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}
    
}
