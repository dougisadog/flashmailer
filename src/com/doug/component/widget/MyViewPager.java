package com.doug.component.widget;

import com.doug.component.error.DebugPrinter;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/** 
 * Uses a combination of a PageTransformer and swapping X & Y coordinates 
 * of touch events to create the illusion of a vertically scrolling ViewPager.  
 *  
 * Requires API 11+ 
 *  
 */  
public class MyViewPager extends ViewPager {  
  
    public MyViewPager(Context context) {  
        super(context);  
    }  
  
    public MyViewPager(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
  
    PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();
	
//    @Override  
//    public boolean onTouchEvent(MotionEvent ev) {
//    	DebugPrinter.d(ev.getAction() + "vp");
//		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//		// 记录按下时候的坐标
//		// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//		downP.x = ev.getX();
//		downP.y = ev.getY();
//	}
//
//	if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//		curP.x = ev.getX();
//		curP.y = ev.getY();
//		
//		float distanceX = curP.x - downP.x;
//		float distanceY = curP.y - downP.y;
//		// 接近竖直滑动，ViewPager控件捕获手势，竖直滚动
//		if (Math.abs(distanceX) < Math.abs(distanceY)) {
//			DebugPrinter.d(ev.getAction() + "vertical vp");
//			getParent().requestDisallowInterceptTouchEvent(false);
//			return false;
//		}
//	}
//	else if(ev.getAction() == MotionEvent.ACTION_UP) {
//		getParent().requestDisallowInterceptTouchEvent(false);
//		return false;
//	}
//        return super.onTouchEvent(ev);  
//    }
    
//    @Override  
//    public boolean onInterceptTouchEvent(MotionEvent ev){
//    
//        boolean intercepted = super.onInterceptTouchEvent(ev);
//        return intercepted;  
//    }  
    
    
}  