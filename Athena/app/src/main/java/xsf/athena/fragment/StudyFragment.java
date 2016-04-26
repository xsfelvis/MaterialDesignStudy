package xsf.athena.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import xsf.athena.R;
import xsf.athena.adapter.StudyPagerAdapter;
import xsf.athena.fragment.base.BaseFragment;


/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public class StudyFragment extends BaseFragment {
    private String[] mTitles;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private StudyPagerAdapter mStudyPagerAdapter;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initView() {
        mTitles = new String[]{"all", "Android", "休息视频", "福利", "iOS", "拓展资源", "前端", "瞎推荐"};
        mTabLayout = IFindViewById(R.id.tl_study);
        mViewPager = IFindViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        //Fragment中嵌套使用Fragment一定要使用getChildFragmentManager(),否则会有问题
        mStudyPagerAdapter = new StudyPagerAdapter(getChildFragmentManager(), mTitles);

        for (int i = 0; i < mTitles.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles[i]));
        }
    }

    @Override
    protected void initData() {
        //super.initData();
        if (mStudyPagerAdapter != null) {
            mViewPager.setAdapter(mStudyPagerAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }


    }

}
