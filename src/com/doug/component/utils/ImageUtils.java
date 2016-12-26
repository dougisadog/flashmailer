package com.doug.component.utils;

import java.io.File;
import java.net.URLDecoder;

import com.doug.component.error.DebugPrinter;
import com.doug.component.file.CacheDisk;
import com.doug.component.file.FileUtils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;

public class ImageUtils {

	public static Matrix getMatrix(Bitmap bitmap, int width, int height) {
		Matrix matrix = new Matrix();
		float size = 1f;
		if (bitmap.getWidth() > bitmap.getHeight()) {
			size = (float) height / (float) bitmap.getHeight();
		} 
		else {
			size = (float) width / (float) bitmap.getWidth();
		}
		matrix.postScale(size, size);
		return matrix;
	}

	public static Bitmap readNormalPic(String filePath, int reqWidth, int reqHeight) {
        try {
            if (TextUtils.isEmpty(filePath)) {
                return null;
            }
            boolean fileExist = new File(filePath).exists();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            if (reqHeight > 0 && reqWidth > 0) {
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            }
            options.inJustDecodeBounds = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inDither = false;  
//            options.inPreferredConfig = Config.RGB_565;
            options.inPreferredConfig = Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            if (null == bitmap) {
                //this picture is broken,so delete it
            	DebugPrinter.e(filePath + "文件无法识别");
                new File(filePath).delete();
                return null;
            }
            if (reqHeight > 0 && reqWidth > 0) {
                int[] size = calcResize(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
                if (size[0] > 0 && size[1] > 0) {
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size[0], size[1], true);
                    if (scaledBitmap != bitmap) {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                }
            }
			System.gc();
            return bitmap;
        } catch (OutOfMemoryError ignored) {
            ignored.printStackTrace();
            return null;
        }
    }
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (height > reqHeight && reqHeight != 0) {
                inSampleSize = (int) Math.round((double) height / (double) reqHeight);
            }

            int tmp = 0;

            if (width > reqWidth && reqWidth != 0) {
                tmp = (int) Math.round((double) width / (double) reqWidth);
            }

            inSampleSize = Math.max(inSampleSize, tmp);
        }
        int roundedSize;
        if (inSampleSize <= 8) {
            roundedSize = 1;
            while (roundedSize < inSampleSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (inSampleSize + 7) / 8 * 8;
        }
        return roundedSize;
    }
	
	private static int[] calcResize(int actualWidth, int actualHeight, int reqWidth, int reqHeight) {

        int height = actualHeight;
        int width = actualWidth;

        float betweenWidth = ((float) reqWidth) / (float) actualWidth;
        float betweenHeight = ((float) reqHeight) / (float) actualHeight;

        float min = Math.max(betweenHeight, betweenWidth);

        height = (int) (min * actualHeight);
        width = (int) (min * actualWidth);

        return new int[] {width, height};
    }
	
	/**
     * returns the bytesize of the give bitmap
     */
    @SuppressLint("NewApi")
	public static int byteSizeOf(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            return bitmap.getAllocationByteCount();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return bitmap.getByteCount();
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
    
    public static String getImagePath(String remoteUrl) {
		String imageName= remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1, remoteUrl.length());
		imageName = URLDecoder.decode(imageName);
		String path = CacheDisk.getPictureDir() + File.separator + imageName;
        return path;
	}
	
	
}
