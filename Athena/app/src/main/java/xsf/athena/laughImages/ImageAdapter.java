package xsf.athena.laughImages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import xsf.athena.R;
import xsf.athena.bean.ImageBean;
import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.Tools;

/**
 * Author: xsf
 * Time: created at 2016/4/28.
 * Email: xsf_uestc_ncl@163.com
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ItemViewHolder> {

    private List<ImageBean> mData;
    private Context mContext;
    private int mMaxWidth;
    private int mMaxHeight;

    private OnItemClickListener mOnItemClickListener;

    public ImageAdapter(Context context) {
        this.mContext = context;
        mMaxWidth = Tools.getWidthInPx(mContext) - 20;
        mMaxHeight = Tools.getHeightInPx(mContext) - Tools.getStatusHeight(mContext) - Tools.dip2px(mContext, 150);
    }

    public void setmDate(List<ImageBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ImageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.ItemViewHolder holder, int position) {
        ImageBean imageBean = mData.get(position);
        if (imageBean == null) {
            return;
        }
        holder.mTitle.setText(imageBean.title);
        float scale = (float) imageBean.width / (float) mMaxWidth;
        int height = (int) (imageBean.height / scale);
        if (height > mMaxHeight) {
            height = mMaxHeight;
        }
        holder.mImage.setLayoutParams(new LinearLayout.LayoutParams(mMaxWidth, height));
        HttpUtil.getInstance().loadImage(imageBean.thumburl, holder.mImage, true);
        //  ImageLoaderUtils.display(mContext, holder.mImage, imageBean.thumburl);

    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public ImageBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTitle;
        public ImageView mImage;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mImage = (ImageView) v.findViewById(R.id.ivImage);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

}
