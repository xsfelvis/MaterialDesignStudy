package xsf.athena.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xsf.athena.R;
import xsf.athena.adapter.base.RVBaseAdapter;
import xsf.athena.bean.GanHuoBean;
import xsf.athena.fragment.base.BaseRecyclerViewFragment;
import xsf.athena.utils.Apis;
import xsf.athena.utils.HttpUtil;
import xsf.athena.utils.LogUtil;
import xsf.athena.utils.ToastUtil;

/**
 * Author: xsf
 * Time: created at 2016/4/23.
 * Email: xsf_uestc_ncl@163.com
 */
public class StudyListFragment extends BaseRecyclerViewFragment {
    private String mType;
    private RVBaseAdapter mRVBaseAdapter;

    @Override
    protected List parseData(String result) {
        List<GanHuoBean> list = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            Gson gson = new Gson();
            // list = gson.fromJson(jsonObject.getString("results"), new TypeToken<List<GanHuoBean>>() {}.getType());
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
        mType = getArguments().getString("type");
        String url = Apis.GanHuo + "/" + mType + "/10/" + currentPageIndex;
        LogUtil.d(url);
        return url;
    }

    @Override
    protected RVBaseAdapter setAdapter() {
        mRVBaseAdapter = new RVBaseAdapter<GanHuoBean>(new ArrayList<GanHuoBean>(), getMyContext()) {

            protected void onBindDataToView(RvCommomViewHolder holder, GanHuoBean bean) {
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
                //super.onItemClick(position);
                ToastUtil.show("你点我我了", Toast.LENGTH_SHORT);
            }
        };

        return mRVBaseAdapter;
    }

    @Override
    protected RecyclerView.LayoutManager setLayoutManager() {
        return new LinearLayoutManager(getMyContext());
    }
}
