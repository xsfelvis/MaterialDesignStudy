package xsf.athena.fragment.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private View mContentView;
    private Context mContext;
    protected ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(setLayoutResourceID(), container, false);
        mContext = getContext();

        init();
        initView();
        initData();

        return mContentView;
    }

    protected abstract int setLayoutResourceID();

    protected void init() {
    }


    protected abstract void initView();

    protected void initData() {
    }


    protected <T extends View> T IFindViewById(int id) {
        return (T) mContentView.findViewById(id);
    }

    public Context getMyContext() {
        return mContext;
    }

    protected View getContentView() {
        return mContentView;
    }

    protected ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    protected void statrProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getMyContext());
            mProgressDialog.setCanceledOnTouchOutside(false);//触摸其他区域diaog消息,防止4.0系统崩溃
            mProgressDialog.show();
        }
    }

    protected void stopProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
