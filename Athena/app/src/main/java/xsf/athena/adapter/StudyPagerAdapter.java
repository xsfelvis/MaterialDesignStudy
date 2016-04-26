package xsf.athena.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import xsf.athena.fragment.StudyListFragment;
import xsf.athena.utils.FragmentUtil;

/**
 * Author: xsf
 * Time: created at 2016/4/22.
 * Email: xsf_uestc_ncl@163.com
 */
public class StudyPagerAdapter extends FragmentStatePagerAdapter {
    private static String[] mTitles;

    public StudyPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        mTitles = titles;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = FragmentUtil.createFragment(StudyListFragment.class, false);
        Bundle bundle = new Bundle();
        bundle.putString("type", mTitles[position]);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }


}
