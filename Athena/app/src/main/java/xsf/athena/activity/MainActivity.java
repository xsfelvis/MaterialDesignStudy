package xsf.athena.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.widget.Toast;

import xsf.athena.R;
import xsf.athena.activity.base.BaseActvity;
import xsf.athena.utils.ToastUtil;


public class MainActivity extends BaseActvity {
    private static String TAG = "MainActvity";
    private static int mSelectMenuIndex = 0;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentManager mFragmentManager;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        mFragmentManager = getSupportFragmentManager();
        //TODO 注册双击退出


    }

    @Override
    protected void initView() {
        initToolBar();
        setToobarTitle(getString(R.string.navigation_main));

        mDrawerLayout = IfindViewById(R.id.drawer_layout);
        mNavigationView = IfindViewById(R.id.navigation_view);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        handleNavigationItemClick();


    }

    private void handleNavigationItemClick() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nvItem_main:
                        // TODO: 2016/4/22
                        // ToastUtil.show("点到我啦", Toast.LENGTH_SHORT);
                        ToastUtil.showWithImg("点到我啦", Toast.LENGTH_SHORT);
                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;//返回值表示该Item是否处于选中状态
            }
        });
    }



}

