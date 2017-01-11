package com.doug.component.ui;

import com.doug.component.cache.CacheBean;
import com.doug.component.support.UIHelper;
//import com.doug.component.support.mail.Mail;
import com.doug.component.utils.KeyboardUitls;
import com.doug.flashmailer.R;
import com.louding.frame.KJActivity;
import com.louding.frame.ui.BindView;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AtyReport extends KJActivity {
	
	@BindView(id = R.id.report)
	private EditText report;
	@BindView(id = R.id.submit, click = true)
	private TextView submit;
	
	private static final String TAG = "MAIL_TASK";
	
	@Override
	public void setRootView() {
		setContentView(R.layout.activity_report);
		UIHelper.setTitleView(this, "", "意见反馈");
		
	}
	
	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
		switch (v.getId()) {
			case R.id.submit :
				submit.setEnabled(false);
				KeyboardUitls.hideKeyboard(AtyReport.this);
				final String content = report.getText().toString();
//				Intent data = new Intent(Intent.ACTION_SENDTO);
//				data.setData(Uri.parse("mailto:service@xiaomanjf.com"));
//				data.putExtra(Intent.EXTRA_SUBJECT, "小满金服APP意见反馈");
//				data.putExtra(Intent.EXTRA_TEXT, content);
//				startActivity(Intent.createChooser(data, "请选择邮件类应用"));
				MyTask mTask = new MyTask();
				mTask.execute(content);
				
				break;

			default :
				break;
		}
	}
	
	private class MyTask extends AsyncTask<String, Integer, String> {  
        //onPreExecute方法用于在执行后台任务前做一些UI操作  
        @Override  
        protected void onPreExecute() {  
        }  
  
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI  
        @Override  
        protected String doInBackground(String... params) {  
            Log.i(TAG, "doInBackground(Params... params) called");  
            try {  
//            	Mail.sendMsg("developer@xiaomanjf.com", CacheBean.getInstance().getUser().getPhone(), params[0], AtyReport.this);
            } catch (Exception e) {  
                Log.e(TAG, e.getMessage());  
            }  
            return null;
        }  
  
        //onProgressUpdate方法用于更新进度信息  
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
        }  
  
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
        @Override  
        protected void onPostExecute(String result) {  
            Log.i(TAG, "onPostExecute(Result result) called"); 
            submit.setEnabled(true);
            Toast.makeText(AtyReport.this, "发送完成", Toast.LENGTH_LONG).show();
            finish();
        }  
  
        //onCancelled方法用于在取消执行中的任务时更改UI  
        @Override  
        protected void onCancelled() {  
            Log.i(TAG, "onCancelled() called");  
            submit.setEnabled(true);
        }  
    }  

}
