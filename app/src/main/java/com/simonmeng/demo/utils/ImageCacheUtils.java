package com.simonmeng.demo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author andong
 * 图片工具类, 用来抓取图片, 缓存图片.
 */
public class ImageCacheUtils {
	
	public static final int SUCCESS = 0; // 请求成功
	public static final int FAILED = 1; // 请求失败
	
	private Handler handler;
	private LruCache<String, Bitmap> mMemoryCache;
	private File cacheDir; // 本地缓存的目录
	private ExecutorService mExecutorService;
	
	public ImageCacheUtils(Context context, Handler handler) {
		this.handler = handler;
		
		cacheDir = context.getCacheDir();
		
		// 获取模拟器运行时可以使用的内存大小 / 8
		long maxMemory = Runtime.getRuntime().maxMemory() / 8;
		mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {

			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 返回当前图片的大小, 给Lrucache去计算当前缓存的内存大小
				return value.getRowBytes() * value.getHeight();
			}
		};

		// 获得一个固定线程为5个的线程池对象.
		mExecutorService = Executors.newFixedThreadPool(5);
	}

	/**
	 * 根据url请求图片
	 * 取图片的顺序: 
	 * 				1. 从内存取.
	 * 				2. 从本地取.
	 * 				3. 从网络取.
	 * @param url
	 * @param tag 当前请求的标识
	 * @return
	 */
	public Bitmap getBitmapFromUrl(String url, int tag) {
		// 1. 从内存取.
		Bitmap bm = mMemoryCache.get(url);
		if(bm != null) {
			System.out.println("从内存中取");
			return bm;
		}
		
		// 2. 从本地取.
		bm = getBitmapFromLocal(url);
		if(bm != null) {
			System.out.println("从本地取");
			return bm;
		}
		
		// 3. 从网络取.
		System.out.println("从网络中取");
		getBitmapFromNet(url, tag);
		return null;
	}

	/**
	 * 根据url, 从本地取图片
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromLocal(String url) {
		try {
			String fileName = MD5Encoder.encode(url).substring(0, 10);
			File file = new File(cacheDir, fileName);
			if(file.exists()) {
				return BitmapFactory.decodeFile(file.getPath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据url请求网络得到图片, 使用子线程执行.
	 * @param url
	 */
	private void getBitmapFromNet(String url, int tag) {
//		new Thread(new RequestNetRunnable(url, tag)).start();
		mExecutorService.execute(new RequestNetRunnable(url, tag));
	}

	/**
	 * @author andong
	 * 请求网络的任务类
	 */
	class RequestNetRunnable implements Runnable {
		
		private String url;
		private int tag; // 当前这次请求, 得到的图片需要设置给某个ImageView(身上有个tag和当前tag一样的ImageView) 
		
		public RequestNetRunnable(String url, int tag) {
			this.url = url;
			this.tag = tag;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			try {
				URL mURL = new URL(url);
				conn = (HttpURLConnection) mURL.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000); // 设置连接超时时间为: 5秒钟
				conn.setReadTimeout(5000); // 设置读取超时时间为: 5秒钟
//				conn.connect();
				int responseCode = conn.getResponseCode();
				if(responseCode == 200) {
					InputStream is = conn.getInputStream();
					// 把流转换成Bitmap对象
					Bitmap bm = BitmapFactory.decodeStream(is);
					
					// 把图片发送到主线程.
					Message msg = handler.obtainMessage();
					msg.obj = bm;
					msg.arg1 = tag;
					msg.what = SUCCESS;
					msg.sendToTarget(); // 把消息发送给PhotosMenuPager中消息处理器
					
					// 向内存中存储一个.
					mMemoryCache.put(url, bm);
					
					// 向本地中存储一个.
					writeToLocal(bm, url);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if(conn != null) {
					conn.disconnect(); // 断开连接.
				}
			}
			handler.obtainMessage(FAILED).sendToTarget();
		}
	}

	/**
	 * 把图片缓存在本地
	 * @param bm
	 * @param url
	 */
	public void writeToLocal(Bitmap bm, String url) {
		try {
			String fileName = MD5Encoder.encode(url).substring(0, 10);
			FileOutputStream fos = new FileOutputStream(new File(cacheDir, fileName));
			bm.compress(CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
