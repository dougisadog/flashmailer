package com.doug.component.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.doug.AppConstants;
import com.doug.component.bean.CItem;
import com.doug.component.bean.jsonbean.Account;
import com.doug.component.bean.jsonbean.User;
import com.doug.component.cache.CacheBean;
import com.doug.component.support.InfoManager.TaskCallBack;
import com.doug.component.utils.FormatUtils;
import com.doug.emojihelper.R;
import com.louding.frame.KJHttp;
import com.louding.frame.http.HttpCallBack;
import com.louding.frame.http.HttpParams;
import com.louding.frame.utils.KJLoger;

import android.content.Context;
import android.widget.Toast;

/**
 * 处理登录  个人信息拉取
 * @author Doug
 *
 */
public class HtmlParser {
	
	private KJHttp http;
	private HttpParams params;

	private static HtmlParser instance = null;
	
	public static HtmlParser getInstance() {
		if (null == instance) {
			instance = new HtmlParser();
		}
		return instance;
	}
	
	private HtmlParser() {
		http = new KJHttp();
	}
	
	public static interface ParserCallBack {
		public void parseSearchDone(List<CItem> items);
	};
	
	public void parseSearch(String search, Context context, final ParserCallBack parserCallBack) {
		parseSearch(search, context, parserCallBack, true);
	}
	
	/**
	 * U表情网站解析
	 * @param search
	 * @param context
	 * @param parserCallBack
	 * @param loading
	 */
	public void parseSearch(String search, Context context, final ParserCallBack parserCallBack, boolean loading) {
		http.get(AppConstants.UBIAOQING + "search/" + search, new HttpCallBack(context, loading) {
			
			@Override
			public void onSuccess(String t) {
				List<CItem> items = new ArrayList<CItem>();
				Document doc = Jsoup.parse(t);
				
				Elements contents = doc.getElementsByClass("list-unstyled");//ul
				if (contents.size() == 0) return ;
				Elements lis = contents.get(0).getElementsByTag("li");
				for (Element li : lis) {
					try {
						Elements imgs = li.getElementsByTag("img");
						String imgUrl = imgs.get(0).attr("src");
						String nameAll = imgs.get(0).attr("alt");
//						int nameIndex = 0;
//						if (nameAll.indexOf("表情包") != -1) {
//							nameIndex = nameAll.indexOf("表情包");
//						}
//						String name = nameAll.substring(nameIndex, nameAll.length());
						Elements as = li.getElementsByTag("a");
						String link = as.get(0).attr("href");
						int index = link.indexOf("biaoqingbao/");
						String imgId = link.substring(index + "biaoqingbao/".length(), link.length());
						
						CItem item = new CItem(nameAll, imgUrl, imgId);
						items.add(item);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				parserCallBack.parseSearchDone(items);
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				parserCallBack.parseSearchDone(new ArrayList<CItem>());
				super.onFailure(t, errorNo, strMsg);
			}
			
		});

	}
	
	public void parseSearchMD(String search, Context context, final ParserCallBack parserCallBack) {
		parseSearch(search, context, parserCallBack, true);
	}
	
	public void parseSearchMD(String search, Context context, final ParserCallBack parserCallBack, boolean loading) {
		String s = "";
		try {
			s = URLEncoder.encode(search, "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		http.get(AppConstants.MADAN + "plus/search.php?"
				+ "pagesize=20&kwtype=1&"
				+ "searchtype=titlekeyword&q=" + s, new HttpCallBack(context, loading) {
			
			@Override
			public void onSuccess(String t) {
				List<CItem> items = new ArrayList<CItem>();
				Document doc = Jsoup.parse(t);
				
				Elements contents = doc.getElementsByClass("pic2");//ul
				if (contents.size() == 0) return ;
				Elements lis = contents.get(0).getElementsByTag("li");
				for (Element li : lis) {
					try {
						Elements imgs = li.getElementsByTag("img");
						String imgUrl = imgs.get(0).attr("src");
						String imgId = imgs.get(0).attr("id");
						Elements as = li.getElementsByTag("a");
						String nameAll = as.get(0).attr("title");
						CItem item = new CItem(nameAll, imgUrl, imgId);
						items.add(item);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
				parserCallBack.parseSearchDone(items);
				super.onSuccess(t);
			}

			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				parserCallBack.parseSearchDone(new ArrayList<CItem>());
				super.onFailure(t, errorNo, strMsg);
			}
			
		});
	}

}
