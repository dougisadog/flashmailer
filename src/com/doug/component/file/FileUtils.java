package com.doug.component.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.doug.FlashApplication;
import com.doug.component.error.DebugPrinter;
import com.doug.flashmailer.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * User: qii Date: 12-8-3
 */
public class FileUtils {

	public static final int TYPE_FILE = 0;
	public static final int TYPE_FILE_PIC = 1;
	public static final int TYPE_FILE_MP3 = 2;
	public static final int TYPE_FILE_TXT = 3;
	public static final int TYPE_FILE_XML = 4;
	public static final int TYPE_FILE_ZIP = 5;

	private static final String TEMP = "temp";

	/**
	 * install weiciyuan, open app and login in, Android system will create
	 * cache dir. then open cache dir (/sdcard
	 * dir/Android/data/org.qii.weiciyuan) with Root Explorer, uninstall
	 * weiciyuan and reinstall it, the new weiciyuan app will have the bug it
	 * can't read cache dir again, so I have to tell user to delete that cache
	 * dir
	 */
	private static volatile boolean cantReadBecauseOfAndroidBugPermissionProblem = false;

	/**
	 * SD Card 本项目的根目录(android/data/项目/cache)
	 * 
	 * @return
	 */
	public static String getSdCardPath() {
		if (isExternalStorageMounted()) {
			File path = FlashApplication.getInstance().getExternalCacheDir();
			if (path != null) {
				return path.getAbsolutePath();
			}
			if (!cantReadBecauseOfAndroidBugPermissionProblem) {
				cantReadBecauseOfAndroidBugPermissionProblem = true;
				final Activity activity = FlashApplication.getInstance().getActivity();
				if (activity == null || activity.isFinishing()) {
					FlashApplication.getInstance().getUiHandler().post(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(FlashApplication.getInstance(),
									"权限出错，无法读取存储空间", Toast.LENGTH_SHORT).show();
						}

					});
					return "";
				}
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						new AlertDialog.Builder(activity)
								.setTitle(R.string.something_error)
								.setMessage(R.string.please_deleted_cache_dir)
								.setPositiveButton(R.string.ok,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {

											}

										}).show();
					}
				});
			}
		}
		return "";
	}

	/**
	 * 相册地址
	 * 
	 * @param albumName
	 * @return
	 */
	public File getAlbumStorageDir(String albumName) {
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				albumName);
		if (!file.mkdirs()) {
			DebugPrinter.e("Directory not created");
		}
		return file;
	}

	public static boolean isExternalStorageMounted() {
		/*
		 * 
		 * getDataDirectory() 获得android data的目录。
		 * 
		 * getDownloadCacheDirectory() 获得下载缓存目录。
		 * 
		 * getExternalStorageDirectory() 或者外部存储媒体目录。
		 * 
		 * getExternalStoragePublicDirectory(String type) Get a top-level public
		 * external storage directory for placing files of a particular type.
		 * 
		 * getExternalStorageState() 获得当前外部储存媒体的状态。
		 * 
		 * getRootDirectory() 获得android的跟目录。
		 */
		// 获取外部存储目录即 SDCard
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED);
		return !(!canRead || onlyRead || unMounted);
	}

	@SuppressWarnings({ "resource", "unused" })
	public static byte[] getAllBytesFromZipFile(String zipFilePath,
			String fileName) {
		byte[] bytes = null;
		try {
			File file = new File(zipFilePath);
			ZipFile zip = new ZipFile(zipFilePath);
			ZipEntry entry = zip.getEntry(fileName);
			InputStream is = zip.getInputStream(entry);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count;
			while ((count = is.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
			}
			bytes = baos.toByteArray();
			is.close();
			baos.close();
		} catch (IOException e) {
			DebugPrinter.e(e.toString());
		}
		return bytes;
	}

	public static File createNewFileInSDCard(String absolutePath) {
		if (!isExternalStorageMounted()) {
			DebugPrinter.e("sdcard unavailiable");
			return null;
		}
		if (TextUtils.isEmpty(absolutePath)) {
			return null;
		}
		File file = new File(absolutePath);
		if (file.exists())
			return file;
		File dir = file.getParentFile();
		if (!dir.exists())
			dir.mkdirs();
		try {
			if (file.createNewFile()) {
				return file;
			}
		} catch (IOException e) {
			DebugPrinter.d(e.getMessage());
			return null;
		}
		return null;
	}

	public static File createNewTempFileByUrl(String url) {
		return createNewFileInSDCard(getTempPath() + File.separator
				+ getFileName(url));
	}

	public static String getFileName(String url) {
		String[] strs = url.split(File.separator);
		return strs[strs.length - 1];
	}

	public static String getTempPath() {
		if (isExternalStorageMounted()) {
			return getSdCardPath() + File.separator + TEMP;
		}
		return "";
	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return path.delete();
	}
	
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} finally {
			if (inBuff != null) {
				inBuff.close();
			}
			if (outBuff != null) {
				outBuff.close();
			}
		}
	}

	/**
	 * @功能描述 从sd卡或者文件系统读取文件数据内容
	 */
	public static List<String[]> getFileContent(String fileName)
			throws FileNotFoundException, IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader bf = new BufferedReader(isr);
		String content = "";

		List<String[]> contentList = new ArrayList<String[]>();
		while (content != null) {
			content = bf.readLine();
			if (content == null) {
				break;
			}

			if (!content.trim().equals("")) {
				contentList.add(content.trim().split("\\t+"));// \\s

			}

		}

		bf.close();

		return contentList;
	}

	/**
	 * @功能描述 从assert文件下面读取文件数据
	 * @param context
	 * @return
	 */

	public static List<String[]> getAssertFile(Context context, String filePath) {
		List<String[]> list = new ArrayList<String[]>();
		try {
			InputStream in = context.getResources().getAssets().open(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(in,
					"UTF-8"));
			String content = "";
			List<String[]> contentList = new ArrayList<String[]>();
			while (content != null) {
				content = br.readLine();
				if (content == null) {
					break;
				}
				if (!content.trim().equals("")) {
					contentList.add(content.trim().split("\\s+"));
				}
			}
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @功能描述 直接读取zip文件下面的数据
	 * @param zipPath
	 *            zip包存放的SDK路径
	 * @param fileName
	 *            需要读取的文件名称 如phrase.txt
	 * @return
	 */
	@SuppressWarnings("unused")
	public static List<String[]> getTxtContentFromZip(String zipPath,
			String fileName) throws FileNotFoundException, IOException {
		// 将所有读到的文件放到数组里面
		List<String[]> list = new ArrayList<String[]>();
		String content = "";
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			for (Enumeration<? extends ZipEntry> e = zipFile.entries(); e
					.hasMoreElements();) {
				ZipEntry entry = e.nextElement();
				// System.out.println("文件名:" + entry.getName() + ", 内容如下:");
				String[] name = entry.getName().split("\\/");
				for (int i = 0; i < name.length; i++) {
					if (name[i].equals(fileName)) {
						InputStream is = null;
						is = zipFile.getInputStream(entry);
						InputStreamReader isr = new InputStreamReader(is,
								"utf-8");
						BufferedReader bf = new BufferedReader(isr);
						while (content != null) {
							content = bf.readLine();
							if (content == null) {
								break;
							}
							//Log.v("content--->", content);
							if (!content.trim().equals("")) {
								list.add(content.trim().split("\t"));// \\t
																		// 匹配一个
																		// \t制表符

							}

						}

						bf.close();
						if (is != null) {
							is.close();
						}
						break;
					}

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	   /**
     * 写入错误日志
     * @param errMessage
     */
    public static void writeErr(String errMessage, String errFile) {
        try {
            String logDir = FileUtils.getSdCardPath() +  File.separator + "log";
            if (TextUtils.isEmpty(logDir)) {
                return;
            }
            String path = logDir + File.separator + errFile + ".stacktrace";
            File f = new File(path);
            if (!f.exists()) {
            	f.createNewFile();
            }
            
            String orgin = new String(); //原有txt内容  
            String fresh = new String();//内容更新  
            BufferedReader input = new BufferedReader(new FileReader(f));  
            while ((orgin = input.readLine()) != null) {  
                fresh += orgin + "\n";  
            }  
            input.close();  
            fresh += errMessage;  
  
            BufferedWriter output = new BufferedWriter(new FileWriter(f));  
            output.write(fresh);  
            output.close();  
        } 
        catch (Exception another) {
        	another.printStackTrace();
        } 
    }

}
