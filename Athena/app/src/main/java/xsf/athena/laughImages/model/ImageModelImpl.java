package xsf.athena.laughImages.model;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import xsf.athena.bean.ImageBean;
import xsf.athena.utils.Apis;
import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.LogUtil;

/**
 * Description :
 * Author : lauren
 * Email  : lauren.liuling@gmail.com
 * Blog   : http://www.liuling123.com
 * Date   : 15/12/22
 */
public class ImageModelImpl implements ImageModel {

    /**
     * 获取图片列表
     *
     * @param listener
     */
    @Override
    public void loadImageList(final OnLoadImageListListener listener) {
        String url = Apis.IMAGES_URL;
        LogUtil.d(url);
        HttpUtil.getInstance().loadString(url, new HttpUtil.HttpCallBack() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onSuccess(String result) {
                List<ImageBean> list = new ArrayList<>();
                try {
                    Gson gson = new Gson();
                    list = gson.fromJson(result, new TypeToken<List<ImageBean>>() {
                    }.getType());
                    listener.onSuccess(list);
                } catch (Exception e) {
                    //listener.onFailure("解析失败", e);
                    LogUtil.d("解析出错");

                }
            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public interface OnLoadImageListListener {
        void onSuccess(List<ImageBean> list);

        void onFailure(String msg, Exception e);
    }
}
