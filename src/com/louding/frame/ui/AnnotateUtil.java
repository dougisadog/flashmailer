/*
 * Copyright (c) 2014,KJFrameForAndroid Open Source Project,张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.louding.frame.ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;

import com.doug.component.file.FileUtils;
import com.doug.emojihelper.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 注解工具类<br>
 * 
 * <b>创建时间</b> 2014-6-5
 * 
 * @author kymjs(kymjs123@gmail.com)
 * @version 1.1
 */
public class AnnotateUtil {
    /**
     * @param currentClass
     *            当前类，一般为Activity或Fragment
     * @param sourceView
     *            待绑定控件的直接或间接父控件
     */
    public static void initBindView(Object currentClass, View sourceView) {
        // 通过反射获取到全部属性，反射的字段可能是一个类（静态）字段或实例字段
        Field[] fields = currentClass.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                // 返回BindView类型的注解内容
                BindView bindView = field.getAnnotation(BindView.class);
                if (bindView != null) {
                    int viewId = bindView.id();
                    boolean clickLis = bindView.click();
                    try {
                        field.setAccessible(true);
                        if (clickLis) {
                        	View v = sourceView.findViewById(viewId);
                            sourceView.findViewById(viewId).setOnClickListener(
                                    (OnClickListener) currentClass);
                        }
                        // 将currentClass的field赋值为sourceView.findViewById(viewId)
                        field.set(currentClass, sourceView.findViewById(viewId));
                    } catch (Exception e) {
                    	getWrongViewName(currentClass, viewId);
                    	//16位码的粗略查找
//                    	DebugPrinter.e("Class :" + currentClass + ",R中ID ： 0x" + Long.toHexString(viewId).toUpperCase());
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * 精密查找bindview报错位置
     * @param currentClass
     * @param viewId
     */
    private static void getWrongViewName(Object currentClass, int viewId) {
		try {
			Class clazz = Class.forName(R.id.class.getName());
			Field[] f = clazz.getDeclaredFields();
			for (int i = 0; i < f.length; i++) {
					int g = f[i].getInt(clazz);
					if (g == viewId) {
						String errMessage = currentClass + ",View name ：R.id." + f[i].getName();
						FileUtils.writeErr(errMessage, "bindViewErr");
					}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    

    /**
     * 必须在setContentView之后调用
     * 
     * @param aty
     */
    public static void initBindView(Activity aty) {
        initBindView(aty, aty.getWindow().getDecorView());
    }

    /**
     * 必须在setContentView之后调用
     * 
     * @param view
     *            侵入式的view，例如使用inflater载入的view
     */
    public static void initBindView(View view) {
        Context cxt = view.getContext();
        if (cxt instanceof Activity) {
            initBindView((Activity) cxt);
        } else {
            throw new RuntimeException("view must into Activity");
        }
    }

    /**
     * 必须在setContentView之后调用
     * 
     * @param frag
     */
    public static void initBindView(Fragment frag) {
        initBindView(frag, frag.getActivity().getWindow().getDecorView());
    }
}
