package util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import domain.News;
import domain.Notice;

public class MessageUtilsJF {
	/**
	 * 获取所有的url地址
	 * 
	 * @param url 首页url
	 * @param pattenUrl 匹配url的正则表达式
	 * @param nextPageHead 下一页新闻或者通知的前段地址*** Canceled***
	 * @param nextPageEnd 下一页末端地址*** Canceled***
	 * @param headUrl 通知和新闻的地址头部
	 * @param format 编码方式
	 * @return
	 */
	
	private static String TARGET_URL_NEWS = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=1";
	private static String TARGET_URL_NOTICE = "http://jcfy.scu.edu.cn/NewsSubClass.aspx?NewsSubClassId=2";
	
	public static List<String> getUrls(String url, String pattenUrl, String headUrl, String format, int type) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		List<String> urls = new ArrayList<>();
		
		//使用HtmlUnit模拟浏览器
		WebClient webClient = new WebClient();
		//webclient参数载体
		WebClientOptions clientOptions = webClient.getOptions();
		// 设置webClient的相关参数
		clientOptions.setJavaScriptEnabled(true);
		clientOptions.setCssEnabled(false);
		clientOptions.setTimeout(35000);
		clientOptions.setThrowExceptionOnScriptError(false);
		clientOptions.setThrowExceptionOnFailingStatusCode(true);
		HtmlPage rootPage;
		HtmlPage otherPage;
		HtmlElement onClick;
		
		String htmlStr = HttpUtil.doGet(url, format);
		// 使用正则表达式匹配详细地址的url
		Pattern pattern = Pattern.compile(pattenUrl);
		Matcher matcher = pattern.matcher(htmlStr);

		String usefulUrl = "";
		boolean isFind = matcher.find();
		
		//0为新闻，1为通知
		if(type == 0) {
			
			rootPage = webClient.getPage(TARGET_URL_NEWS);
			
			//获得前五页新闻的内容
			
			for(int i=0; i<=4; i++) {
				for(int k=16; k<=25; k++) {
					onClick = (HtmlElement) rootPage.getElementsByTagName("a").get(k);
					otherPage = (HtmlPage) onClick.click();
					usefulUrl = otherPage.getUrl().toString();
					urls.add(usefulUrl);
				}
				onClick = (HtmlElement) rootPage.getElementsByTagName("a").get(28+i);
				rootPage = (HtmlPage) onClick.click();
			}
			
		} else if(type == 1){
			
			rootPage = webClient.getPage(TARGET_URL_NOTICE);
			
			//获得前两页通知的内容
			for(int i=0; i<=1; i++) {
				for(int k=16; k<=25; k++) {
					onClick = (HtmlElement) rootPage.getElementsByTagName("a").get(k);
					otherPage = (HtmlPage) onClick.click();
					usefulUrl = otherPage.getUrl().toString();
					urls.add(usefulUrl);
				}
				onClick = (HtmlElement) rootPage.getElementsByTagName("a").get(28+i);
				rootPage = (HtmlPage) onClick.click();
			}
		}
		//关闭浏览器
		webClient.close();
		return urls;
	}
	
	/**
	 * 获取所有的新闻返回list集合
	 * @param urllList 所有的新闻地址
	 * @param patTitle 标题正则表达式
	 * @param patTime 时间正则表达式
	 * @param patPic 图片正则表达式
	 * @param picHeadUrl 图片url的头部分
	 * @param patContent 内容正则表达式
	 * @param format 编码格式
	 * @param count 内容文本名递增方式存放  例如:"D://computer/"+count +".html"
	 * @return
	 * @throws ParseException 
	 */
	public static List<News> getNews(List<String> urllList , String patTitle, String patTime,
			String patPic,String picHeadUrl, String patContent, String format,int count) throws ParseException{
		List<News> list = new ArrayList<News>();
		
		for(String url : urllList){
			
			list.add(getNew(url, patTitle, patTime, patPic, picHeadUrl, patContent, format, count++));
		}
		
		return list;
		
	}
	
	/**
	 * 获取其中的一条新闻，返回News对象
	 * @param url  新闻地址
	 * @param patTitle 标题正则表达式
	 * @param patTime 时间正则表达式
	 * @param patPic 图片正则表达式
	 * @param picHeadUrl 图片url的头部分
	 * @param patContent 内容正则表达式
	 * @param format 编码格式
	 * @return
	 * @throws ParseException 
	 */
	public static News getNew(String url, String patTitle, String patTime,
			String patPic, String picHeadUrl, String patContent, String format, int count) throws ParseException {
		News news = new News();
		//获取具体新闻页的内容
		String htmlStr = HttpUtil.doGet(url, format);
		//获取标题
		String title = getTitle(htmlStr, patTitle);
		//获取时间
		String time = getTime(htmlStr, patTime);
		//获取图片
		String pic = getPic(htmlStr, patPic, picHeadUrl);
		//获取新闻主体
		String content = getContent(htmlStr, patContent);
		
		//处理新闻主体，添加完整的图片url
		content = content.replaceAll("<img src=\"",
				"<img src=\""+picHeadUrl);
		//设置图片大小，以适应任意客户端
		content = content.replaceAll(".jpg\".+?>", ".jpg\" width=90%");
		
		//给新闻内容主体添加头部，在html文件中显示
		content = content
				.replace(
						content,
						"<html><head> <base target=\"_blank\" /> <meta"
								+ " http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
								+ content + "</html>");
								
		//将新闻内容主体存储到硬盘
		FileUtil.writeIntoFile(content,"D://computer/news/"+count +".html", false);
		news.setTitle(title);
		news.setAddress(url);
		news.setContent(content);
		news.setPic(pic);
		news.setTime(time);
		System.out.println(title + "\n" + url + "\n" + pic + "\n" + time);
		return news;
	}

	//******************************************通知****************************************************//
	
	public static List<Notice> getNotices(List<String> urllList , String patTitle, String patTime,
			String patContent, String format,int count) throws ParseException{
		List<Notice> list = new ArrayList<Notice>();
		
		for(String url : urllList){
			
			list.add(getNotice(url, patTitle, patTime, patContent, format, count++));
		}
		
		return list;
		
	}
	
	public static Notice getNotice(String url, String patTitle, String patTime,
			     String patContent, String format, int count) throws ParseException {
		Notice notice = new Notice();
		//获取具体新闻页的内容
		String htmlStr = HttpUtil.doGet(url, format);
		//获取标题
		String title = getTitle(htmlStr, patTitle);
		//获取时间
		String time = getTime(htmlStr, patTime);
		//获取新闻主体
		String content = getContent(htmlStr, patContent);

		//给新闻内容主体添加头部，在html文件中显示
		content = content
				.replace(
						content,
						"<html><head> <base target=\"_blank\" /> <meta"
								+ " http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>"
								+ content + "</html>");
								
		//将新闻内容主体存储到硬盘
		FileUtil.writeIntoFile(content,"D://computer/notice/"+count +".html", false);
		notice.setTitle(title);
		notice.setContent(content);
		notice.setTime(time);
		System.out.println(title + "\n" + time);
		return notice;
	}
	
	/**
	 * 获取标题
	 * @param htmlStr
	 * @param patTitle
	 * @return
	 */
	public static String getTitle(String htmlStr, String patTitle) {
		Pattern pattern = Pattern.compile(patTitle);
		Matcher matcher = pattern.matcher(htmlStr);
		String title = "";
		if (matcher.find()) {
			title = matcher.group(1);
		}

		return title;

	}

	/**
	 * 获取日期
	 * @param htmlStr
	 * @param patTime
	 * @return
	 */
	public static String getTime(String htmlStr, String patTime) throws ParseException {
		// 获取日期
		Pattern pattern = Pattern.compile(patTime);
		Matcher matcher = pattern.matcher(htmlStr);
		Date date = new Date();
		String realDate = "";
		if (matcher.find()) {// 设置日期
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");// 设置日期格式
			// 将字符串转换成date类型
			//date = format.parse(matcher.group(1));
			
			//基法网站文章只有年月日，因此需要转换成字符串输出
			realDate = format.format(date);
		}

		return realDate;
	}
	/**
	 * 获取图片
	 * @param htmlStr
	 * @param patPic
	 * @param picHeadUrl
	 * @return
	 */
	public static String getPic(String htmlStr, String patPic, String picHeadUrl) {
		Pattern pattern = Pattern.compile(patPic);
		Matcher matcher = pattern.matcher(htmlStr);
		String pic = "";
		if (matcher.find()) {// 设置图片

			pic = picHeadUrl + matcher.group(1);
		}
		return pic;
	}
	
	/**
	 * 获取内容
	 * @param html
	 * @param patContent
	 * @return
	 */
	public static String getContent(String html, String patContent) {

		Pattern pattern = Pattern.compile(patContent);
		Matcher matcher = pattern.matcher(html);
		String content = "";
		if (matcher.find()) {
			content = matcher.group(0);
		}

		return content;
	}

}
