package util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpUtil {

	/**
	 * 部分学院会出现 contentEncoding:-1 调用这个方法
	 * 获取要抓取页面的所有内容
	 * @param url  网页地址
	 * @return
	 */
	public static String doGet(String url, String format) {

		
		// 通过HttpClientBuilder创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();

		HttpGet httpGet = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).build();
		// 配置HttpGet参数
		//httpGet.setConfig(requestConfig);

		System.out.println(httpGet.getRequestLine());

		try {
			HttpResponse httpResponse = client.execute(httpGet);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			// 响应状态
			System.out.println("status:" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				// 获取响应的消息实体
				HttpEntity entity = httpResponse.getEntity();
				InputStream is = entity.getContent();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i = -1;
				while ((i = is.read()) != -1) {
					baos.write(i);
				}
				return baos.toString(format);

				/*InputStreamReader iReader = new InputStreamReader(is, format);

				return is;
*/
			} else if (statusCode == HttpStatus.SC_NOT_FOUND) {
				client.close();
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				client.close();
				/*
				 * if (in != null) { in.close(); }
				 */
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 获取要抓取页面的所有内容
	 * @param url 网页地址
	 * @return
	 */
/*	public static String sendGet(String url, String format) {

		// 从字符输入流中读取文本，缓冲各个字符，从而实现字符、数组和行的高效读取。
		BufferedReader in = null;
		String result = "";
		// 通过HttpClientBuilder创建HttpClient
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient client = httpClientBuilder.build();

		HttpGet httpGet = new HttpGet(url);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(5000).build();
		// 配置HttpGet参数
		httpGet.setConfig(requestConfig);

		System.out.println(httpGet.getRequestLine());

		try {
			HttpResponse httpResponse = client.execute(httpGet);

			int statusCode = httpResponse.getStatusLine().getStatusCode();

			// 响应状态
			System.out.println("status:" + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				// 获取响应的消息实体
				HttpEntity entity = httpResponse.getEntity();

				// 判断实体是否为空
				if (entity != null) {
					System.out.println("contentEncoding:"
							+ entity.getContentLength());
					// InputStreamReader 是字节流通向字符流的桥梁：它使用指定的 charset
					// 读取字节并将其解码为字符。
					in = new BufferedReader(new InputStreamReader(
							entity.getContent(), format));

					String line;
					while ((line = in.readLine()) != null) {
						// 遍历抓取到的每一行并将其存储到result里面
						result += line +"\n";
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				client.close();
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}*/

	
}
