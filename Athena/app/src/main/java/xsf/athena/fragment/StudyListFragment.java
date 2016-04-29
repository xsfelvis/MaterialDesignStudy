package xsf.athena.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xsf.athena.R;
import xsf.athena.activity.WebViewActivity;
import xsf.athena.adapter.StudyPagerAdapter;
import xsf.athena.adapter.base.RVXBaseAdapter;
import xsf.athena.bean.GanHuoBean;
import xsf.athena.fragment.base.BaseXRecyclerViewFragment;
import xsf.athena.utils.Apis;
import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.LogUtil;

/**
 * Author: xsf
 * Time: created at 2016/4/23.
 * Email: xsf_uestc_ncl@163.com
 */
public class StudyListFragment extends BaseXRecyclerViewFragment {
    private String mType;
    private RVXBaseAdapter mRVBaseAdapter;

    @Override
    protected List parseData(String result) {
        //LogUtil.d(result);
        List<GanHuoBean> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            Gson gson = new Gson();
            list = gson.fromJson(jsonObject.getString("results"), new TypeToken<List<GanHuoBean>>() {
            }.getType());

        } catch (JSONException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    protected String getUrl(int currentPageIndex) {
        mType = getArguments().getString(StudyPagerAdapter.TYPE_KEY);
        String url = Apis.GanHuo + "/" + mType + "/10/" + currentPageIndex;
        LogUtil.d(url);
        return url;
    }

    @Override
    protected RVXBaseAdapter setAdapter() {
        mRVBaseAdapter = new RVXBaseAdapter<GanHuoBean>(new ArrayList<GanHuoBean>(), getMyContext()) {

            protected void onBindData(RvCommomViewHolder holder, GanHuoBean bean) {
                holder.getView(R.id.tv_desc).setVisibility(View.GONE);
                holder.getView(R.id.iv_img).setVisibility(View.GONE);
                holder.getView(R.id.fl_head_date_wrap).setVisibility(View.GONE);

                if (bean.url.endsWith(".jpg")) {
                    holder.getView(R.id.iv_img).setVisibility(View.VISIBLE);
                    ImageView imageView = holder.getView(R.id.iv_img);
                    HttpUtil.getInstance().loadImage(bean.url, imageView, true);
                } else {
                    holder.getView(R.id.tv_desc).setVisibility(View.VISIBLE);
                    holder.setText(R.id.tv_desc, bean.desc);
                }
                holder.setText(R.id.tv_source, bean.source);
                holder.setText(R.id.tv_people, bean.who);
                holder.setText(R.id.tv_time, bean.publishedAt.substring(0, 10));
                holder.setText(R.id.tv_tag, bean.type);
            }

            @Override
            public int getItemLayoutID(int viewType) {
                return R.layout.item_ganhuo;
            }

            @Override
            protected void onItemClick(int position) {
                Intent intent = new Intent(getMyContext(), WebViewActivity.class);
                intent.putExtra(WebViewActivity.URL, mBeans.get(position - 1).url);
                getMyContext().startActivity(intent);
                //ToastUtil.show("你点到我了", Toast.LENGTH_SHORT);
            }
        };

        return mRVBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getMyContext());
    }
}
