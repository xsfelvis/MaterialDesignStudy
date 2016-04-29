package xsf.athena.laughImages;


import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import xsf.athena.R;
import xsf.athena.bean.ImageBean;
import xsf.athena.fragment.base.BaseFragment;
import xsf.athena.laughImages.presenter.ImagePresenter;
import xsf.athena.laughImages.presenter.ImagePresenterImpl;
import xsf.athena.laughImages.view.ImgView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends BaseFragment implements ImgView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ImageFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ImageAdapter mAdapter;
    private List<ImageBean> mData;
    private ImagePresenter mImagePresenter;

    @Override
    protected void init() {
        mImagePresenter = new ImagePresenterImpl(this);
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_image;
    }

    @Override
    protected void initView() {
        mSwipeRefreshWidget = IFindViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = IFindViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new ImageAdapter(getMyContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        onRefresh();

    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                //加载更多
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout), "加载完了╮(╯▽╰)╭", Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onRefresh() {
        if (mData != null) {
            mData.clear();
        }
        mImagePresenter.loadImageList();

    }

    @Override
    public void addImages(List<ImageBean> list) {
        if (mData == null) {
            mData = new ArrayList<ImageBean>();
        }
        mData.addAll(list);
        mAdapter.setmDate(mData);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);

    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);

    }

    @Override
    public void loadFailMsg() {
        View view = getActivity() == null ? mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, "加载失败，请检查网络", Snackbar.LENGTH_SHORT).show();
    }
}
