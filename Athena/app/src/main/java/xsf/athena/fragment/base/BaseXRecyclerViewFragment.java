package xsf.athena.fragment.base;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;
import java.util.List;

import xsf.athena.R;
import xsf.athena.adapter.base.RVXBaseAdapter;
import xsf.athena.utils.FileUtils;
import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.LogUtil;
import xsf.athena.utils.Tools;

/**
 * Created by _SOLID
 * Date:2016/4/18
 * Time:17:36
 * <p>
 * common fragment for list data display ,and you can extends this fragment for everywhere you want to display list data
 */
public abstract class BaseXRecyclerViewFragment<T> extends BaseFragment {
    private static final String TAG = "BaseRvFrament";

    private static final int ACTION_REFRESH = 1;
    private static final int ACTION_LOAD_MORE = 2;

    private XRecyclerView mRecyclerView;
    private LinearLayout mLLReloadWarp;
    private Button mBtnReload;
    private RVXBaseAdapter<T> mAdapter;


    private int mCurrentAction = ACTION_REFRESH;
    private int mCurrentPageIndex = 1;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_base_recycler_view;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        //网络异常
        mLLReloadWarp = IFindViewById(R.id.ll_reload_wrap);
        mBtnReload = IFindViewById(R.id.btn_reload);

        mRecyclerView = IFindViewById(R.id.recyclerview);
        //mRecyclerView.hasFixedSize();
        mRecyclerView.setLayoutManager(setLayoutManager());
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallClipRotatePulse);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);

        //设置适配器
        mAdapter = setAdapter();
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void initData() {

        //下拉刷新监听
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                swiActionLoadData(ACTION_REFRESH);
            }

            @Override
            public void onLoadMore() {
                swiActionLoadData(ACTION_LOAD_MORE);

            }
        });
        //重新加载按钮
        mBtnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLLReloadWarp.setVisibility(View.GONE);
                mRecyclerView.setRefreshing(true);
            }
        });

        //设置recycleView为可以下拉刷新,放置位置靠后会自动刷新一下
        mRecyclerView.setRefreshing(true);
    }


    /**
     * 刷新或者加载更多
     *
     * @param action
     */
    private void swiActionLoadData(int action) {
        mCurrentAction = action;
        switch (mCurrentAction) {
            case ACTION_REFRESH:
                // mAdapter.clear();
                mCurrentPageIndex = 1;
                break;
            case ACTION_LOAD_MORE:
                mCurrentPageIndex++;
                break;
        }
        //加载数据
        loadData();


    }

    /**
     * 加载数据
     */
    private void loadData() {


        final String reqUrl = getUrl(mCurrentPageIndex);
        if (!Tools.isNetworkConnected(getMyContext())) {
            //无网络则获取缓存数据
            String result = obtainOfflineData(getUrl(1));
            LogUtil.d("无网络" + result);
            onDataSuccessReceived(result);
            // ToastUtil.show("当前无网络连接", Toast.LENGTH_SHORT);
        } else {
            HttpUtil.getInstance().loadString(reqUrl, new HttpUtil.HttpCallBack() {
                @Override
                public void onLoading() {
                    Log.d(TAG, "onLoading");
                }

                @Override
                public void onSuccess(String result) {
                    Log.d(TAG, "onSucess");
                    //LogUtil.d("onSuccess");
                    if (mCurrentAction == ACTION_REFRESH) {
                        storeOfflineData(getUrl(1), result);
                    }
                    onDataSuccessReceived(result);

                }

                @Override
                public void onError(Exception e) {
                    onDataErrorReceived();
                }
            });
        }


    }

    /**
     * 存储离线数据
     *
     * @param url
     * @param result
     */
    private void storeOfflineData(String url, String result) {
        try {
            FileUtils.writeFile(getOfflineDir(url), result, "UTF-8", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 成功接收消息
     *
     * @param result
     */
    private void onDataSuccessReceived(String result) {
        if (!Tools.isNullOrEmpty(result)) {
            List<T> list = parseData(result);
            mAdapter.addAll(list);
            loadComplete();
            mLLReloadWarp.setVisibility(View.GONE);
        } else {
            onDataErrorReceived();
        }
    }

    /**
     * 解析处理数据
     *
     * @param result
     * @return
     */
    protected abstract List<T> parseData(String result);

    /**
     * 通知数据加载完毕
     */
    private void loadComplete() {
        if (mCurrentAction == ACTION_REFRESH)
            mRecyclerView.refreshComplete();
        if (mCurrentAction == ACTION_LOAD_MORE)
            mRecyclerView.loadMoreComplete();
    }

    /**
     * 设置加载错误的布局
     */
    private void onDataErrorReceived() {
        // LogUtil.d("onDataErrorReceived");
        mLLReloadWarp.setVisibility(View.VISIBLE);
        loadComplete();
    }

    /**
     * 获取离线json数据
     *
     * @param url
     * @return
     */
    private String obtainOfflineData(String url) {
        String result = null;
        try {
            result = FileUtils.readFile(getOfflineDir(url), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * 获取文件离线数据目录
     *
     * @param url
     * @return
     */
    private String getOfflineDir(String url) {
        LogUtil.d(FileUtils.getCacheDir(getMyContext()) + File.separator + "offline_gan_huo_cache" + File.separator + Tools.md5(url));
        return FileUtils.getCacheDir(getMyContext()) + File.separator + "offline_gan_huo_cache" + File.separator + Tools.md5(url);

    }

    /**
     * 对外提供
     *
     * @param currentPageIndex
     * @return
     */
    protected abstract String getUrl(int currentPageIndex);


    /**
     * 对外提供设置adapter方法
     *
     * @return
     */
    protected abstract RVXBaseAdapter<T> setAdapter();


    /**
     * 对外提供layoutManager
     *
     * @return
     */
    protected abstract RecyclerView.LayoutManager setLayoutManager();
}
