package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;

import util.MessageUtilsJF;

public class Test2 {

	public static void main(String[] args) throws ParseException, FailingHttpStatusCodeException, MalformedURLException, IOException  {
		
		//华西基法
		//学院新闻url
		String urlNews = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1";
		//学院通知url
		String urlNotice = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=2";
		//新闻匹配正则表达式
		//Sample : <a target="_blank" href="News.aspx?NewsId=20230" title="华西基础医学与法医学院本科教学督导工作简报(2016年第2期)">
		String pattenUrl = "<a target=\"_blank\" href=\"(News.aspx.+?NewsId=[1-9][0-9]{1,})\" title=\".*?\">";
		
		/*翻页由Javascript控件完成，原方法在这里不适用
		//下一页地址的前半部分 
		String nextPageHead = "" ;
		//下一页地址的尾部分
		String nextPageEnd = "";
		*/
		
		//新闻完整地址的前部分
		String headUrl = "http://jcfy.scu.edu.cn/";
		
		//获得新闻地址
		List<String> urlsNews = MessageUtilsJF.getUrls(urlNews, pattenUrl, headUrl,"UTF-8", 0);
		System.out.println("新闻网页地址:");
		for(String s : urlsNews){
			System.out.println(s);
		}
		
		//获得新闻地址
		List<String> urlsNotice = MessageUtilsJF.getUrls(urlNotice, pattenUrl, headUrl,"UTF-8", 1);
		System.out.println("通知网页地址:");
		for(String s : urlsNotice){
			System.out.println(s);
		}
				
						
		int count = 1;
						
		//标题的正则表达式 
		//Title sample : <span id="ctl00_MainContent_lblTitle">华西基础医学与法医学院本科教学督导工作简报(2016年第2期)</span>
		String patTitle = "<span id=\"ctl00_MainContent_lblTitle\">(.*)</span>";
		
		//时间的正则表达式
		//Time sample : <span id="ctl00_MainContent_lblReleaseTime">2016/10/10</span>
		String patTime = "<span id=\"ctl00_MainContent_lblReleaseTime\">(.*?)</span>";
		
		//图片的正则表达式
		//Pic sample: <img src="/Account/SystemMangment/thirdparty/ueditor/net/upload/image/20161010/6361171424097488124256286.jpg" title="1.jpg" alt="1.jpg">
	    String patPic = "<img src=\"(.+?.jpg)\"";
	    
		//内容的正则表达式
		String patContent = "<ul class=\"a_content\">([\\s\\S]*) <ul>" ;
						
		MessageUtilsJF.getNews(urlsNews, patTitle, patTime, patPic, "http://jcfy.scu.edu.cn", patContent, "UTF-8", count);
		MessageUtilsJF.getNotices(urlsNotice, patTitle, patTime, patContent, "UTF-8", count);
	}
}
