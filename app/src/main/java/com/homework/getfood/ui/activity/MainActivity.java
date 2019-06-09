package com.homework.getfood.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.homework.getfood.ui.adapter.MainFragmentPagerAdapter;
import com.homework.getfood.ui.fragment.MakeFragment;
import com.homework.getfood.ui.fragment.OrderFragment;
import com.homework.getfood.R;
import com.homework.getfood.util.NoScrollViewPager;

/**
 * 主界面Activity
 */
public class MainActivity extends AppCompatActivity {
    private static MakeFragment fragment_make;
    private static OrderFragment fragment_order;
    private TabLayout mTabLayout;
    static private NoScrollViewPager mViewPager;
    private MainFragmentPagerAdapter FragmentPagerAdapter;
    private long time = 0;
    final int[] unselectedIcon = new int[]{
            R.drawable.ic_menu_unselected,
            R.drawable.ic_list_unselected
    };
    final int[] selectedIcon = new int[]{
            R.drawable.ic_menu_selected,
            R.drawable.ic_list_selected
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        initViews();

    }
    private void initViews(){
        mViewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        mViewPager.setScroll(false);
        FragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };
        mViewPager.setAdapter(FragmentPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        super.onTabSelected(tab);
                        int id = tab.getPosition();
                        mTabLayout.getTabAt(id).setIcon(selectedIcon[id]);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int id = tab.getPosition();
                        mTabLayout.getTabAt(id).setIcon(unselectedIcon[id]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        super.onTabReselected(tab);
                    }
                }
        );

        mTabLayout.getTabAt(0).setIcon(selectedIcon[0]);
        mTabLayout.getTabAt(1).setIcon(unselectedIcon[1]);
        fragment_order = (OrderFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_order_id);
        fragment_make = (MakeFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragment_make_id);
    }

    public static void setViewPagerID(int id){
        mViewPager.setCurrentItem(id);
    }
    public static OrderFragment getFragment_order(){
        return fragment_order;
    }

    public static void setFragment_order(OrderFragment fragment_order) {
        MainActivity.fragment_order = fragment_order;
    }
    public static void setFragment_make(MakeFragment fragment_make){
        MainActivity.fragment_make = fragment_make;
    }
    public static MakeFragment getFragment_make(){
        return fragment_make;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /* 点击的为返回键 */
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();// 退出方法
        }
        return true;
    }

    /**
     * 连按返回键退出应用
     */
    private void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            showToast("再点击一次退出应用程序");
        }else{
            this.finishAffinity();
            System.exit(0);
        }
    }
    @SuppressLint("WrongConstant")
    public void showToast(String text) {
        Toast.makeText(this, text, 2000).show();
    }

}
