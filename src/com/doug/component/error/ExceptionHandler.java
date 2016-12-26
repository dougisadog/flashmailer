
package com.doug.component.error;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.doug.FlashApplication;
import com.doug.component.bean.jsonbean.User;
import com.doug.component.cache.CacheBean;
import com.doug.component.file.FileUtils;
import com.qq.taf.jce.dynamic.StringField;

import android.text.TextUtils;


/**
 * 自定义异常处理器
 * @author Doug
 *
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler previousHandler;

    public ExceptionHandler(Thread.UncaughtExceptionHandler handler) {
        this.previousHandler = handler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        final Date now = new Date();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        try {
            String logDir = FileUtils.getSdCardPath() +  File.separator + "log";
            if (TextUtils.isEmpty(logDir)) {
                return;
            }
            java.text.DateFormat format1 = new java.text.SimpleDateFormat("yyyy-MM-dd_hh-mm-ss");
            String date = format1.format(new Date());
            String filename = date;
            String path = logDir + File.separator + filename + ".stacktrace";
            BufferedWriter write = new BufferedWriter(new FileWriter(path));
            write.write(CacheBean.getInstance().getApkInfo().toString() + "\n");
            StringBuffer pathActivity = new StringBuffer();
            List<String> names = FlashApplication.getInstance().getStackActivities();
            for (int i = 0; i < names.size(); i++) {
            	pathActivity.append(names.get(i));
			}
            write.write("path:" + names.toString() + "\n");
            User user = CacheBean.getInstance().getUser();
            if (null != user) {
            	 write.write("user:" + user.getName() + " and tel:" + user.getPhone());
            }
            write.write(result.toString());
            write.flush();
            write.close();
        } 
        catch (Exception another) {

        } 
        finally {
        }
        previousHandler.uncaughtException(thread, exception);
    }
    
    
}