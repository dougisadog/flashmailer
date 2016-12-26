package com.doug.component.widget;
import com.doug.component.error.DebugPrinter;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 监听ScrollView滚动到顶部或者底部做相关事件拦截
 */
public class SmartScrollView extends ScrollView {

	public SmartScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	private boolean isScrolledToTop = true; // 初始化的时候设置一下值
	private boolean isScrolledToBottom = false;
	public SmartScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private ISmartScrollChangedListener mSmartScrollChangedListener;

	/** 定义监听接口 */
	public interface ISmartScrollChangedListener {
		void onScrolledToBottom();
		void onScrolledToTop();
	}

	public void setScanScrollChangedListener(
			ISmartScrollChangedListener smartScrollChangedListener) {
		mSmartScrollChangedListener = smartScrollChangedListener;
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if (scrollY == 0) {
			isScrolledToTop = clampedY;
			isScrolledToBottom = false;
		}
		else {
			isScrolledToTop = false;
			isScrolledToBottom = clampedY;
		}
		notifyScrollChangedListeners();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (android.os.Build.VERSION.SDK_INT < 9) { // API
													// 9及之后走onOverScrolled方法监听
			if (getScrollY() == 0) { // 小心踩坑1: 这里不能是getScrollY() <= 0
				isScrolledToTop = true;
				isScrolledToBottom = false;
			}
			else if (getScrollY() + getHeight() - getPaddingTop()
					- getPaddingBottom() == getChildAt(0).getHeight()) {
				// 小心踩坑2: 这里不能是 >= //
				// 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding
				isScrolledToBottom = true;
				isScrolledToTop = false;
			}
			else {
				isScrolledToTop = false;
				isScrolledToBottom = false;
			}
			notifyScrollChangedListeners();
		}
		// 有时候写代码习惯了，为了兼容一些边界奇葩情况，上面的代码就会写成<=,>=的情况，结果就出bug了
		// 我写的时候写成这样：getScrollY() + getHeight() >= getChildAt(0).getHeight()
		// 结果发现快滑动到底部但是还没到时，会发现上面的条件成立了，导致判断错误
		// 原因：getScrollY()值不是绝对靠谱的，它会超过边界值，但是它自己会恢复正确，导致上面的计算条件不成立
		// 仔细想想也感觉想得通，系统的ScrollView在处理滚动的时候动态计算那个scrollY的时候也会出现超过边界再修正的情况
	}

	private void notifyScrollChangedListeners() {
		if (isScrolledToTop) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToTop();
			}
		}
		else if (isScrolledToBottom) {
			if (mSmartScrollChangedListener != null) {
				mSmartScrollChangedListener.onScrolledToBottom();
			}
		}
	}

	public boolean isScrolledToTop() {
		return isScrolledToTop;
	}

	public boolean isScrolledToBottom() {
		return isScrolledToBottom;
	}

	public boolean canScroll() {
		View child = getChildAt(0);
		if (child != null) {
			int childHeight = child.getHeight();
			return getHeight() < childHeight;
		}
		return false;
	}

	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//			// 记录按下时候的坐标
//			// 切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//			downP.x = ev.getX();
//			downP.y = ev.getY();
//		}
//
//		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//			curP.x = ev.getX();
//			curP.y = ev.getY();
//			
//			float distanceX = curP.x - downP.x;
//			float distanceY = curP.y - downP.y;
//			// 接近竖直滑动，ViewPager控件捕获手势，竖直滚动
//			if (Math.abs(distanceX) < Math.abs(distanceY)) {
//				if (!canScroll() || (canScroll() && isScrolledToTop)) {
//					getParent().requestDisallowInterceptTouchEvent(false);
//					return false;
//				}
//			}
//		}
//		return super.onInterceptTouchEvent(ev);
//
//	}
	
//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		DebugPrinter.d(ev.getAction() + "ssv");
//		return false;
//		
//	}

}