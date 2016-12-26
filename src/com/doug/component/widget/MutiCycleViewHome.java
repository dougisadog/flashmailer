package com.doug.component.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.doug.AppConstants;
import com.doug.component.bean.ADCycleItem;
import com.doug.component.bean.CycleData;
import com.doug.component.bean.ShopADData;
import com.doug.component.cache.CacheBean;
import com.doug.emojihelper.R;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 广告图片自动轮播控件</br>
 * 
 * <pre>
 *   集合ViewPager和指示器的一个轮播控件，主要用于一般常见的广告图片轮播，具有自动轮播和手动轮播功能 
 *   使用：只需在xml文件中使用{@code <com.minking.imagecycleview.ImageCycleView/>} ，
 *   然后在页面中调用  {@link #setImageResources(ArrayList, ImageCycleViewListener) }即可!
 *   
 *   另外提供{@link #startImageCycle() } \ {@link #pushImageCycle() }两种方法，用于在Activity不可见之时节省资源；
 *   因为自动轮播需要进行控制，有利于内存管理
 * </pre>
 * 
 * @author minking
 */

//使用 4 1234 1模型
public class MutiCycleViewHome extends LinearLayout {

	/**
	 * 上下文
	 */
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;

	/**
	 * 滚动图片视图适配器
	 */
	private ImageCycleAdapter mAdvAdapter;

	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示器-个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示器-视图列表
	 */
	private ImageView[] mImageViews = null;
	
//	private int pointResourceFocus = R.drawable.dot_blue;
//	private int pointResourceBlur = R.drawable.dot_white;
	private int pointResourceFocus = R.drawable.banner_dian_focus_home;
	public int getPointResourceBlur() {
		return pointResourceBlur;
	}

	public void setPointResourceBlur(int pointResourceBlur) {
		this.pointResourceBlur = pointResourceBlur;
	}

	public void setPointResourceFocus(int pointResourceFocus) {
		this.pointResourceFocus = pointResourceFocus;
	}

	private int pointResourceBlur = R.drawable.banner_dian_blur_home;

	/**
	 * 图片滚动当前图片下标 412341 从 第二位开始
	 */
	private int mImageIndex = 1;
	

	/**
	 * 手机密度
	 */
	private float mScale;
	
	private int srollSpeed = 500; //切换速度
	private int timeFix = 50; //轮播位置修正时间 31231  1,3位置互转的在500后的时间间隔 用于主动touch 可调整


	/**
	 * 重写事件分发防止sildemenu的横滑阻挡 轮播的滚动
	 */
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent event) {
//		Activity activity =  GlobalApp.getInstance().getCurrentRunningActivity();
//		if (activity instanceof MenuActivity) {
//			if (event.getAction() == MotionEvent.ACTION_MOVE) {
//				((MenuActivity) activity).setTouchEnabled(false);
//			}
//			if (event.getAction() == MotionEvent.ACTION_UP) {
//				((MenuActivity) activity).setTouchEnabled(true);
//			}
//		}
//		return super.dispatchTouchEvent(event);
//	}


	/**
	 * @param context
	 */
	public MutiCycleViewHome(Context context) {
		super(context);
		init();
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public MutiCycleViewHome(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		mScale = getContext().getResources().getDisplayMetrics().density;
		LayoutInflater.from(getContext()).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		
		try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(mAdvPager.getContext(),
                    new AccelerateInterpolator());
            field.set(mAdvPager, scroller);
            scroller.setmDuration(srollSpeed);
        } catch (Exception e) {
        }
		
		mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		mAdvPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						// 开始图片滚动
						startImageTimerTask();
						isTouch = true;
						break;
					default:
						// 停止图片滚动
						stopImageTimerTask();
						break;
				}
				return false;
			}
		});
		// 滚动图片右下指示器视图
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);
	}

	/**
	 * 装填图片数据
	 * 
	 * @param cycleData
	 * @param imageCycleViewListener
	 */
	public void setImageResources(List<CycleData> cycleData, ImageCycleViewListener imageCycleViewListener) {
		// 清除所有子视图
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = cycleData.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(getContext());
			mImageView.setScaleType(ScaleType.FIT_CENTER);
			int imageParams = (int) (mScale * 15 + 0.5f)/2;// XP与DP转换，适应不同分辨率
			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams p = new LayoutParams(imageParams, imageParams);
			p.setMargins(10, 0, 10, 0);
			mImageView.setLayoutParams(p);
			mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);
			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(pointResourceFocus);
			} else {
				mImageViews[i].setBackgroundResource(pointResourceBlur);
			}
			//补充下面的圆圈的
			final int index = i;
			mImageViews[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mAdvPager.setCurrentItem(index);
				}
			});
			mGroup.addView(mImageViews[i]);
		}
		mAdvAdapter = new ImageCycleAdapter(getContext(), cycleData, imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		
		mAdvPager.setCurrentItem(1 , true);
	}

	/**
	 * 开始轮播(手动控制自动轮播与否，便于资源控制)
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播——用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 开始图片滚动任务
	 */
	public void startImageTimerTask() {
		stopImageTimerTask();
		// 图片每3秒滚动一次
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	//TODO 报错
	private Runnable mImageTimerTask = new Runnable() {

		@Override
		public void run() {
//			synchronized (mImageViews) {
				if (mImageViews != null) {
					// 下标等于图片列表长度说明已滚动到最后一张图片,重置下标
//					if ((++mImageIndex) < mImageViews.length) {
					isTouch = false;
					if (mImageIndex == mImageViews.length + 1) {
						isAuto = true;
						mAdvPager.setCurrentItem(1 , false);
					}
					else {
						++mImageIndex;
						mAdvPager.setCurrentItem(mImageIndex , true);
					}
//					}
					startImageTimerTask();
				}
//			}
		}
	};
	
	private boolean isAuto = true; //true 执行强行对位瞬间跳转 31231  3->3 1->1
	private boolean isTouch = false; // 手势滑动

	/**
	 * 轮播图片状态监听器
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		
		

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask(); // 开始下次计时
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			mImageIndex = index;
			// 设置当前显示的图片下标
			if (!isAuto) return;
			if (index == 0) {
				index = mImageViews.length -1;
				//手势
				if (isTouch) {
					changeView(false, srollSpeed + timeFix, mImageViews.length);
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							//使用非强制跳转 550 在500速度基础上完成跳转后50 进行瞬间位置替换 下同
//							isAuto = false;
//							mAdvPager.setCurrentItem(mImageViews.length , false);
//							isAuto = true;
//						}
//					},srollSpeed + timeFix);
				}
				else {
					mImageIndex = mImageViews.length;
					isAuto = false;
					mAdvPager.setCurrentItem(mImageIndex + 1 , false);
					changeView(true, timeFix, mImageIndex);
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							isAuto = true;
//							mAdvPager.setCurrentItem(mImageIndex, true);
//						}
//					},timeFix);
				} 
			}
			else if (index <= mImageViews.length) {
				index = index - 1;
			}
			else if (index == mImageViews.length + 1) {
				index = 0;
				if (isTouch) {
					changeView(false, srollSpeed + timeFix, 1);
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							//使用非强制跳转 550 在500速度基础上完成跳转后50 进行瞬间位置替换 下同
//							isAuto = false;
//							mAdvPager.setCurrentItem(1 , false);
//							isAuto = true;
//						}
//					},srollSpeed + timeFix);
				}
				else {
					mImageIndex = 1;
					isAuto = false;
					mAdvPager.setCurrentItem(0 , false);
					changeView(true, timeFix + timeFix, 1);
//					new Handler().postDelayed(new Runnable() {
//						
//						@Override
//						public void run() {
//							isAuto = true;
//							mAdvPager.setCurrentItem(1, true);
//						}
//					},timeFix);
				}
			}
			// 设置图片滚动指示器背景
			mImageViews[index].setBackgroundResource(pointResourceFocus);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(pointResourceBlur);
				}
			}

		}

	}
	
	 
	/**
	 * 
	 * @param force 是否强制 false为强制 true 非强制
	 * @param delay 延迟时间 
	 * @param index 目标跳转
	 */
	private void changeView(final boolean force, int delay, final int index) {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//使用非强制跳转 550 在500速度基础上完成跳转后50 进行瞬间位置替换 下同
				isAuto = force;
				mAdvPager.setCurrentItem(index , force);
				isAuto = true;
			}
		},delay);
	}

	private class ImageCycleAdapter extends PagerAdapter {
		
		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<ADCycleItem> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private List<CycleData> cycleDatas = new ArrayList<CycleData>();

		/**
		 * 广告图片点击监听器
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;

		public ImageCycleAdapter(Context context, List<CycleData> cycleDatas, ImageCycleViewListener imageCycleViewListener) {
			mContext = context;
			this.cycleDatas = cycleDatas;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ADCycleItem>();
		}

		@Override
		public int getCount() {
			return cycleDatas.size() + 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
				if (position == 0) {
					position = cycleDatas.size() -1;
				}
				else if (position <= cycleDatas.size()) {
					position = position - 1;
				}
				else {
					position = 0;
				}
			final int index = position;
			CycleData cycleData = cycleDatas.get(index);
			ADCycleItem adCycleItem = null;
			if (mImageViewCacheList.isEmpty()) {
				adCycleItem = new ADCycleItem(mContext,cycleData);
				adCycleItem.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
				
//				adCycleItem.setScaleType(ImageView.ScaleType.FIT_XY);
				
			} else {
				adCycleItem = mImageViewCacheList.remove(0);
			}
			// 设置图片点击监听
			adCycleItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mImageCycleViewListener.onImageClick(index, v);
				}
			});
			adCycleItem.setTag(cycleData);
			adCycleItem.getImageView().setScaleType(ScaleType.FIT_CENTER);
			container.addView(adCycleItem);
			mImageCycleViewListener.displayImage(cycleData, adCycleItem);
			return adCycleItem;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ADCycleItem view = (ADCycleItem) object;
			container.removeView(view);
			mImageViewCacheList.add(view);
		}

	}

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 加载图片资源
		 * 
		 * @param goodsData
		 * @param adCycleItem
		 */
		public void displayImage(CycleData cycleData, ADCycleItem adCycleItem);

		/**
		 * 单击图片事件
		 * 
		 * @param position
		 * @param imageView
		 */
		public void onImageClick(int position, View imageView);
	}
	
	
	public class FixedSpeedScroller extends Scroller {
	    private int mDuration = 500;
	                                                                                                           
	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }
	                                                                                                           
	    public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }
	                                                                                                           
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	                                                                                                           
	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	                                                                                                           
	    public void setmDuration(int time) {
	        mDuration = time;
	    }
	                                                                                                           
	    public int getmDuration() {
	        return mDuration;
	    }
	}

}
