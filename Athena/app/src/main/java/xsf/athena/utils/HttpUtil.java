package xsf.athena.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xsf.athena.AthenaApplication;
import xsf.athena.R;

/**
 * Author: xsf
 * Time: created at 2016/4/24.
 * Email: xsf_uestc_ncl@163.com
 */
public class HttpUtil {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private Handler mHandler;
    private static volatile HttpUtil mInstance;
    private static final OkHttpClient mOkHttpClient;

    static {
        mOkHttpClient = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
    }

    private HttpUtil() {
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static HttpUtil getInstance() {
        if (mInstance == null)
            synchronized (HttpUtil.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtil();
                }
            }
        return mInstance;
    }

    /**
     * 不开启异步线程
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     *
     * @param request
     * @param responseCallBack
     */
    public static void enqueue(Request request, okhttp3.Callback responseCallBack) {
        mOkHttpClient.newCall(request).enqueue(responseCallBack);

    }

    public void loadString(final String url, final HttpCallBack callBack) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onLoading();
                        }
                    });
                    final Response response = execute(request);

                    if (response.isSuccessful()) {
                        //请求成功
                        final String result = response.body().string();
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(result);
                            }
                        });
                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(new Exception("请求失败"));
                            }
                        });
                    }
                } catch (final IOException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onError(e);
                        }
                    });
                }

            }
        }).start();

    }


    /**
     * post JSON data to server
     *
     * @param url
     * @param json
     * @param callBack
     */
    public void post(String url, String json, final HttpCallBack callBack) {

        RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = null;
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                callBack.onSuccess(response.body().string());
            } else {
                callBack.onError(new Exception("Unexpected code " + response));
            }
        } catch (IOException e) {
            e.printStackTrace();
            callBack.onError(e);
        }

    }

    /**
     * 加载图片
     *
     * @param url the url of image
     * @param iv  ImageView
     */
    public void loadImage(String url, ImageView iv) {
        loadImage(url, iv, false);
    }

    public void loadImage(String url, ImageView iv, boolean isCenterCrop) {
        loadImageWithHolder(url, iv, R.mipmap.ic_default, isCenterCrop);
    }

    /**
     * 加载图片
     *
     * @param url              the url of image
     * @param iv               ImageView
     * @param placeholderResID default image
     */
    public void loadImageWithHolder(String url, ImageView iv, int placeholderResID, boolean isCenterCrop) {
        Picasso.with(AthenaApplication.getApplication()).load(url).placeholder(placeholderResID).fit().into(iv);
        RequestCreator creator = Picasso.with(AthenaApplication.getApplication()).load(url).placeholder(R.mipmap.ic_default).error(R.mipmap.ic_image_loadfail);
        if (isCenterCrop) {
            creator.centerCrop();
        }
        creator.fit().into(iv);
    }

    public interface HttpCallBack {
        void onLoading();

        void onSuccess(String result);

        void onError(Exception e);
    }


}
