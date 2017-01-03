package com.doug.component.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import com.baidu.tts.BaiduTTSManager;
import com.doug.component.file.FileUtils;
import com.doug.component.support.UIHelper;
import com.doug.component.utils.ImageUtils;
import com.doug.flashmailer.R;
import com.doug.flashmailer.R.color;
import com.louding.frame.KJActivity;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author doug
 *
 */
public class AtyTTS extends KJActivity implements OnClickListener {
	
	private EditText txtContent,delay;
	private TextView btn, btnStop, btnImgSub, btnPhoto;
	private ImageView pickImg;

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
        
        btnImgSub = (TextView) findViewById(R.id.btnImgSub);
        btnImgSub.setOnClickListener(this);
        pickImg = (ImageView) findViewById(R.id.pickImg);
        pickImg.setOnClickListener(this);
        btnPhoto = (TextView) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(this);
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
    
	private final static int REQUEST_FILE_PICKER = 11000;
	private final static int CROP_PHOTO = 11001;
	
    
    private void fileChoose() {
    	Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
    	albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    	startActivityForResult(albumIntent, REQUEST_FILE_PICKER);
    }
    
    private void takePhoto() {
    	Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    	filePhotoPath = ImageUtils.getImagePath("temple_photo_img.jpg");
    	File pre = new File(filePhotoPath);
    	// 下面这句指定调用相机拍照后的照片存储的路径
    	if (pre.exists()) {
    	    pre.delete();
    	}
    	try {
    	    pre.createNewFile();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    	cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(pre));
    	startActivityForResult(cameraIntent, CROP_PHOTO);
    }
    
    private void uploadImg() {
    	if (TextUtils.isEmpty(filePickPath)) return;
		// 一步任务获取图片
		KJHttp kjh = new KJHttp();
		HttpParams params = new HttpParams();
		File file = new File(filePickPath);
		try {
			params.put("iconImg", file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params.put("pp", 1);
		kjh.post("http://nangua.webok.net:9973/rest/fileupload", params, new HttpCallBack(this, false) {

			@Override
			public void success(JSONObject ret) {
				super.success(ret);
			}
			
		});
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
            	break;
            case R.id.btnImgSub:
            	uploadImg();
            	break;
            case R.id.btnPhoto:
            	takePhoto();
            	break;
            case R.id.pickImg:
            	fileChoose();
            	break;
            default:
                break;
        }

    }

	@Override
	public void setRootView() {
		// TODO Auto-generated method stub
		
	}
    
	private String filePhotoPath;
	private String filePickPath;
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
	    	switch (requestCode) {
	            /**
	             * 拍照的请求标志
	             */
	            case CROP_PHOTO:
	                if (resultCode==RESULT_OK) {
	                    try {
	                    	if (TextUtils.isEmpty(filePhotoPath)) return;
	                    	FileOutputStream fout = null;
	            			File filename = new File(filePhotoPath);
	            			Bitmap bit = BitmapFactory.decodeFile(filePhotoPath);
	                        try {
	                         fout = new FileOutputStream(filename);
	                         bit.compress(Bitmap.CompressFormat.JPEG, 100, fout);
	                        } catch (FileNotFoundException e) {
	                         e.printStackTrace();
	                        }finally{
	                         try {
	                          fout.flush();
	                          fout.close();
	                         } catch (IOException e) {
	                          e.printStackTrace();
	                         }
	                        }
	                        pickImg.setImageBitmap(bit);
	                    } catch (Exception e) {
	                        Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
	                    }
	                }
	     
	                break;
	            /**
	             * 从相册中选取图片的请求标志
	             */
	     
	            case REQUEST_FILE_PICKER:
	                if (resultCode == RESULT_OK) {
	                    try {
	                        /**
	                         * 该uri是上一个Activity返回的
	                         */
	                        Uri uri = intent.getData();
	                        Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));


	                        FileOutputStream fout = null;
	                        filePickPath = ImageUtils.getImagePath("temple_pick_img.jpg");
	            			File filename = new File(filePickPath);
	                        try {
	                         fout = new FileOutputStream(filename);
	                         bit.compress(Bitmap.CompressFormat.JPEG, 100, fout);
	                        } catch (FileNotFoundException e) {
	                         e.printStackTrace();
	                        }finally{
	                         try {
	                          fout.flush();
	                          fout.close();
	                         } catch (IOException e) {
	                          e.printStackTrace();
	                         }
	                        }
	                        pickImg.setImageBitmap(bit);
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                        Log.d("tag",e.getMessage());
	                        Toast.makeText(this,"程序崩溃",Toast.LENGTH_SHORT).show();
	                    }
	                }
	                else{
	                    Log.i("liang", "失败");
	                }
	     
	                break;
	     
	            default:
	                break;
	        }
	}
}
