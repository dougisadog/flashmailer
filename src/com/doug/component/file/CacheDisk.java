package com.doug.component.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import android.os.Environment;
import android.os.StatFs;

public class CacheDisk {

	/**
	 * 默认缓存位置
	 */
	public static String CACHE_DEFAULT_DIR = FileUtils.getSdCardPath() +  File.separator + "default";
	/**
	 * 图片缓存位置
	 */
	public static String CACHE_PICTURE_DIR = FileUtils.getSdCardPath() +  File.separator + "picture";
	/**
	 * 音频缓存位置
	 */
	public static String CACHE_SOUND_DIR = FileUtils.getSdCardPath() +  File.separator + "sound";
	/**
	 * 课程文件位置
	 */
	public static String CACHE_LESSON_DIR = FileUtils.getSdCardPath() +  File.separator + "lesson";
	/**
	 * 课程文件位置
	 */
	public static String CACHE_WORD_DIR = FileUtils.getSdCardPath() +  File.separator + "word";
	/**
	 * 日志位置
	 */
	public static String CACHE_LOG_DIR = FileUtils.getSdCardPath() +  File.separator + "log";
	
	/**
	 * 缓存文件后缀
	 */
	private static final String CACHE_FILE_SUFFIX = ".c";

	private static final int MB = 1024 * 1024;
	private static final int CACHE_SIZE = 1024; // 1024M
	private static final int SDCARD_CACHE_THRESHOLD = 100; //100M

	public CacheDisk() {
		// 清理文件缓存
		removeCache();
	}
	
	public void removeCache() {
		removeCache(CACHE_PICTURE_DIR);
		removeCache(CACHE_SOUND_DIR);
	}

	
	/**
	 * 从磁盘缓存中获取MediaPlayer
	 */
	public boolean getSoundFromDisk(String url) {
		String path = getPictureDir() + File.separator + getCacheFileNameByUrl(url);
		File file = new File(path);
		if (file.exists() || MediaFile.isAudioFileType(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(".") - 1))) {
			updateLastModified(file);
			return true;
//			file.delete();
		}
		return false;
	}
	
	/**
	 * 将二进制写入文件缓存
	 */
	public String addFileToCache(String url, int fileType, byte[] bytes) {
		if (null == bytes || bytes.length == 0) {
			return null;
		}
		// 判断当前SDCard上的剩余空间是否足够用于文件缓存
		if (SDCARD_CACHE_THRESHOLD > calculateFreeSpaceOnSd()) {
			return null;
		}
		String fileName = getCacheFileNameByUrl(url);
		String dir = null;
		switch (fileType) {
			case FileUtils.TYPE_FILE_PIC:
				dir = getPictureDir();
				break;
			case FileUtils.TYPE_FILE_MP3:
				dir = getSoundDir();
				break;
			case FileUtils.TYPE_FILE_ZIP:
				dir = getLessonDir();
				break;
			default:
				dir = getDefaultDir();
				break;
		}
		File dirFile = new File(dir);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		File file = new File(dir + File.separator + fileName);
		FileOutputStream out = null;
		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
		} 
		catch (FileNotFoundException e) {
		} 
		catch (IOException e) {
		}
		finally {
		}
		return dir + File.separator + fileName;
	}

	/**
	 * 清理文件缓存 当缓存文件总容量超过CACHE_SIZE或SDCard的剩余空间小于SDCARD_CACHE_THRESHOLD时，将删除40%
	 * 最近没有被使用的文件
	 * @param dirPath
	 * @return
	 */
	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return true;
		}
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return false;
		}
		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(CACHE_FILE_SUFFIX)) {
				dirSize += files[i].length();
			}
		}
		if (dirSize > CACHE_SIZE * MB || SDCARD_CACHE_THRESHOLD > calculateFreeSpaceOnSd()) {
			int removeFactor = (int) (0.4 * files.length + 1);
			Arrays.sort(files, new FileLastModifiedSort());
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(CACHE_FILE_SUFFIX)) {
					files[i].delete();
				}
			}
		}
		if (calculateFreeSpaceOnSd() <= SDCARD_CACHE_THRESHOLD) {
			return false;
		}
		return true;
	}

	/**
	 * 更新文件的最后修改时间
	 */
	private void updateLastModified(File file) {
		long time = System.currentTimeMillis();
		file.setLastModified(time);
	}

	/**
	 * 计算SDCard上的剩余空间
	 * 
	 * @return
	 */
	private int calculateFreeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		@SuppressWarnings("deprecation")
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/**
	 * 生成统一的磁盘文件后缀便于维护 从URL中得到源文件名称，并为它追加缓存后缀名.cache
	 * 
	 * @param url
	 * @return 文件存储后的名称
	 */
	private String getCacheFileNameByUrl(String url) {
		String[] strs = url.split(File.separator);
		if (strs.length > 2) {
			return strs[strs.length - 2] + "_" + strs[strs.length - 1] + CACHE_FILE_SUFFIX;
		}
		return strs[strs.length - 1] + CACHE_FILE_SUFFIX;
	}

	/**
	 * 根据文件最后修改时间进行排序
	 */
	private class FileLastModifiedSort implements Comparator<File> {
		@Override
		public int compare(File lhs, File rhs) {
			if (lhs.lastModified() > rhs.lastModified()) {
				return 1;
			} else if (lhs.lastModified() == rhs.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}
	
	/********************************************************************************************************************************/
	
	public static String getCacheSize() {
		if (!FileUtils.isExternalStorageMounted()) {
            String path = FileUtils.getSdCardPath() + File.separator;
            FileSize size = new FileSize(new File(path));
            return size.toString();
        }
        return "0MB";
    }
	
	/********************************************************************************************************************************/
	
	public static String getDefaultDir() {
        if (!FileUtils.isExternalStorageMounted()) {
        	return "";
        }
        String path = CACHE_DEFAULT_DIR;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }
	
	public static String getLogDir() {
        if (!FileUtils.isExternalStorageMounted()) {
        	return "";
        }
        String path = CACHE_LOG_DIR;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }
	
	public static String getPictureDir() {
        if (!FileUtils.isExternalStorageMounted()) {
        	return "";
        }
        String path = CACHE_PICTURE_DIR;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }
	
	public static String getSoundDir() {
        if (!FileUtils.isExternalStorageMounted()) {
        	return "";
        }
        String path = CACHE_SOUND_DIR;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }
	
	public static String getLessonDir() {
        if (!FileUtils.isExternalStorageMounted()) {
        	return "";
        }
        String path = CACHE_LESSON_DIR;
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        return path;
    }
	
	public static File createNewLessonFileByPath(String path) {
    	return FileUtils.createNewFileInSDCard(getLessonPath(path));
    }
	
	public static File createNewLessonFileByUrl(String url) {
		return FileUtils.createNewFileInSDCard(FileUtils.getFileName(url));
	}
	
	public static File getLessonFileByPath(String path) {
		return new File(getLessonPath(path));
	}
	
	public static String getLessonPath(String path) {
		return CACHE_LESSON_DIR + File.separator + path;
	}
	
	public static String getWordPath() {
		return CACHE_WORD_DIR + File.separator;
	}
	
}
