package xsf.athena.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.Tools;

/**
 * Author: xsf
 * Time: created at 2016/4/24.
 * Email: xsf_uestc_ncl@163.com
 */
public abstract class RVXBaseAdapter<T> extends RecyclerView.Adapter<RVXBaseAdapter.RvCommomViewHolder> {
    protected List<T> mBeans;
    protected Context mContext;
    protected boolean mAnimateItems = false;
    protected int mLastAnimatedPosition = -1;

    public RVXBaseAdapter(List<T> beans, Context context) {
        mBeans = beans;
        mContext = context;
    }

    //创建viewholder
    @Override
    public RVXBaseAdapter.RvCommomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(getItemLayoutID(viewType), parent, false);
        RvCommomViewHolder rvholder = new RvCommomViewHolder(view);
        return rvholder;
    }

    //绑定数据到视图
    @Override
    public void onBindViewHolder(RVXBaseAdapter.RvCommomViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        onBindData(holder, mBeans.get(position));//抽象方法交给子类去实现
    }

    //获取item大小
    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    /**
     * 绑定数据到Item的控件中去
     *
     * @param holder
     * @param bean
     */
    protected abstract void onBindData(RvCommomViewHolder holder, T bean);

    /**
     * 取得ItemView的布局文件
     *
     * @return
     */
    public abstract int getItemLayoutID(int viewType);


    /***
     * item的加载动画
     * 透明度发生变化
     *
     * @param view
     * @param position
     */
    private void runEnterAnimation(View view, int position) {
        if (!mAnimateItems) {
            return;
        }
        if (position > mLastAnimatedPosition) {
            mLastAnimatedPosition = position;
            view.setTranslationY((Tools.getScreenHeight(mContext)));
            view.animate()
                    .alpha(50)
                    .setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(300)
                    .start();
        }
    }

/*    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(getActivity()));
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }*/


    /**
     * 抽象出来viewholder具体支持
     */
    public class RvCommomViewHolder extends RecyclerView.ViewHolder {
        private final SparseArray<View> mViews;
        public View itemView;

        public RvCommomViewHolder(View itemView) {
            super(itemView);
            this.mViews = new SparseArray<>();
            this.itemView = itemView;
            //添加item点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(getAdapterPosition());
                }
            });
        }

        public <T extends View> T getView(int viewId) {

            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public void setText(int viewId, String text) {
            TextView tv = getView(viewId);
            tv.setText(text);
        }

        /**
         * 设置drawable中的图片
         *
         * @param viewId
         * @param resId
         */
        public void setImage(int viewId, int resId) {
            ImageView iv = getView(viewId);
            iv.setImageResource(resId);
        }

        /**
         * 加载网络上的图片
         *
         * @param viewId
         * @param url
         */
        public void setImageFromInternet(int viewId, String url) {
            ImageView iv = getView(viewId);
            HttpUtil.getInstance().loadImage(url, iv);//这里可根据自己的需要变更
        }


    }

    /**
     * ItemView的单击事件(如果需要，重写此方法就行)
     *
     * @param position
     */
    protected void onItemClick(int position) {

    }

    public void add(T bean) {
        mBeans.add(bean);
        notifyDataSetChanged();
    }

    public void addAll(List<T> beans) {
        mBeans.addAll(beans);
        notifyDataSetChanged();
    }

    public void clear() {
        mBeans.clear();
        notifyDataSetChanged();
    }
}
