package xsf.athena.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.KeyEvent;
import android.view.MenuItem;

import xsf.athena.R;
import xsf.athena.activity.base.BaseActvity;
import xsf.athena.fragment.AboutFragment;
import xsf.athena.fragment.StudyFragment;
import xsf.athena.laughImages.ImageFragment;
import xsf.athena.utils.AppManager;
import xsf.athena.utils.DoubleClickExitHelper;
import xsf.athena.utils.FragmentManagerUtil;
import xsf.athena.utils.LogUtil;


public class MainActivity extends BaseActvity {
    private static String TAG = "MainActvity";
    private static String KEY = "currentSelectMenuIndex";
    private static int mSelectMenuIndex = 0;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FragmentManager mFragmentManager;
    private Fragment mDefaultFragment;
    private DoubleClickExitHelper DoubleClickExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mSelectMenuIndex = savedInstanceState.getInt(KEY, 0);
        }
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        initToolBar();
        mFragmentManager = getSupportFragmentManager();
        DoubleClickExit = new DoubleClickExitHelper(this);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, mSelectMenuIndex);
    }

    @Override
    protected void initView() {

        setToobarTitle("干货集中营");
        mDrawerLayout = IfindViewById(R.id.drawer_layout);
        mNavigationView = IfindViewById(R.id.navigation_view);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mActionBarDrawerToggle.syncState();//该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        handleNavigationItemClick();
        initDefalueFragment();


    }

    /**
     * 第一个默认加载的fragment
     */
    private void initDefalueFragment() {
        mDefaultFragment = FragmentManagerUtil.createFragment(StudyFragment.class);
        mFragmentManager.beginTransaction().add(R.id.frame_content, mDefaultFragment).commit();
        mNavigationView.getMenu().getItem(mSelectMenuIndex).setChecked(true);

    }

    /**
     * 处理侧滑栏
     */
    private void handleNavigationItemClick() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nvItem_main:
                        // setToobarTitle(getString(R.string.navigation_main));
                        setToobarTitle("干货集中营");
                        switchFragment(StudyFragment.class);
                        //ToastUtil.showWithImg("点到我啦", Toast.LENGTH_SHORT);
                        break;
                    case R.id.nvItem_news:
                        //  setToobarTitle("新闻");
                        setToobarTitle("搞笑图片");
                        switchFragment(ImageFragment.class);
                        break;
                    case R.id.nvsub_exit:
                        AppManager.getAppManager().AppExit(MainActivity.this);
                        break;
                    case R.id.sub_about:
                        switchFragment(AboutFragment.class);

                        break;

                }
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return false;//返回值表示该Item是否处于选中状态
            }
        });
    }

    //防止被重复实例化
    private void switchFragment(Class<?> clazz) {
        Fragment switchTo = FragmentManagerUtil.createFragment(clazz);
        if (switchTo.isAdded()) {
            LogUtil.d("already add");
            mFragmentManager.beginTransaction().hide(mDefaultFragment).show(switchTo).commitAllowingStateLoss();
        } else {
            LogUtil.d("not add");
            mFragmentManager.beginTransaction().hide(mDefaultFragment).add(R.id.frame_content, switchTo).commitAllowingStateLoss();
        }
        mDefaultFragment = switchTo;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
                return true;
            }

            return DoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}

