package xsf.athena.fragment.base;


import android.widget.Button;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import xsf.athena.R;

/**
 * Created by _SOLID
 * Date:2016/4/18
 * Time:17:36
 * <p/>
 * common fragment for list data display ,and you can extends this fragment for everywhere you want to display list data
 */
public abstract class BaseRecyclerViewFragment<T> extends BaseFragment {

    private static final int ACTION_REFRESH = 1;
    private static final int ACTION_LOAD_MORE = 2;
    private XRecyclerView mXRecyclerView;
    private LinearLayout mLLReloadWarp;
    private Button nBtnReload;


    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    protected void initView() {

    }
}
