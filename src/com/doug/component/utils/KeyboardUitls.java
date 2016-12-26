package com.doug.component.utils;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * 键盘控制
 * @author Jeffrey
 *
 */
public class KeyboardUitls {

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
            	imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
	
	public static void closeInputMethod(Context context, View ...vs) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	for (View v : vs) {
	    		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
	    		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
	    }
	}

	public static void showInputMethod(Context context, View ...vs) {
	    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
	    boolean isOpen = imm.isActive();
	    if (isOpen) {
	    	for (View v : vs) {
	    		// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);//没有显示则显示
	    		imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);  
			}
	    }
	}
	
	public static void observeSoftKeyboard(final Activity activity, final OnSoftKeyboardChangeListener listener, final boolean save) {  
        final View decorView = activity.getWindow().getDecorView();  
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {  
            int previousKeyboardHeight = 0;
            int minHeight = -1;
            		
            @Override  
            public void onGlobalLayout() {  
                Rect rect = new Rect();  
                decorView.getWindowVisibleDisplayFrame(rect);  
                int displayHeight = rect.bottom - rect.top;  
                int height = decorView.getHeight();  
                int keyboardHeight = height - displayHeight;  
                if (previousKeyboardHeight != keyboardHeight) {  
                    boolean hide = (double) displayHeight / height > 0.8;
                    if (minHeight == -1)
                    	minHeight = keyboardHeight;
                    minHeight = Math.min(minHeight, keyboardHeight);
                    if (!hide && save)
                    	setSoftInputHeight(activity, keyboardHeight - minHeight);
                    if (null != listener)
                    	listener.onSoftKeyBoardChange(keyboardHeight - minHeight, !hide);
                }  
  
                previousKeyboardHeight = height;  
  
            }  
        });  
    }  
  
    public interface OnSoftKeyboardChangeListener {  
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);  
    }
    
    public static final String KEYBOARD_HEIGHT = "keyboard height";
    
    public static int getSoftInputHeight(Context context) {
    	return SettingHelper.getSharedPreferences(context, KEYBOARD_HEIGHT, 10);
    }
    
    private static void setSoftInputHeight(Activity context, int height) {
    	SettingHelper.setEditor(context, KEYBOARD_HEIGHT, height);
    }
    
}
