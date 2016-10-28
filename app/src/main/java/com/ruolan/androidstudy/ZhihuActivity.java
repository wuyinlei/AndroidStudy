/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ruolan.androidstudy;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;


import com.ruolan.androidstudy.behavior.ScaleDownShowBehavior;
import com.ruolan.androidstudy.fragment.BookShelfFragment;
import com.ruolan.androidstudy.fragment.CategoryFragment;
import com.ruolan.androidstudy.fragment.RankingFragment;
import com.ruolan.androidstudy.fragment.RecommendFragment;
import com.ruolan.androidstudy.widget.FragmentTabHost;

import java.util.ArrayList;
import java.util.List;

public class ZhihuActivity extends AppCompatActivity {

    private FloatingActionButton FAB;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhihu_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initTab();

        FAB = (FloatingActionButton) findViewById(R.id.fab);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(FAB, "点宝宝干啥", Snackbar.LENGTH_SHORT).show();
            }
        });

        ScaleDownShowBehavior scaleDownShowFab = ScaleDownShowBehavior.from(FAB);
        scaleDownShowFab.setOnStateChangedListener(onStateChangedListener);

        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.tab_layout));
    }

    private ScaleDownShowBehavior.OnStateChangedListener onStateChangedListener = new ScaleDownShowBehavior.OnStateChangedListener() {
        @Override
        public void onChanged(boolean isShow) {
            mBottomSheetBehavior.setState(isShow ? BottomSheetBehavior.STATE_EXPANDED : BottomSheetBehavior.STATE_COLLAPSED);
        }
    };


    private boolean initialize = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!initialize) {
            initialize = true;
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    //底部tab
    private List<Tab> mTabs = new ArrayList<>(5);

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private ImageView img;  //底部图片
    private TextView text;  //底部标题
    /**
     * 初始化底部的几个菜单栏
     */
    private void initTab() {
        Tab home = new Tab(R.string.selef_book, R.drawable.selector_icon_selef, BookShelfFragment.class);
        Tab hot = new Tab(R.string.book_care, R.drawable.selector_icon_care, RankingFragment.class);
        Tab category = new Tab(R.string.book_stack, R.drawable.selector_icon_stack, RecommendFragment.class);
        Tab cart = new Tab(R.string.book_disc, R.drawable.selector_icon_discover, CategoryFragment.class);

        mTabs.add(home);
        mTabs.add(category);
        mTabs.add(hot);
        mTabs.add(cart);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
            }
        });

        int i = -1;

        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));
            tabSpec.setIndicator(builderIndiator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null, ++i);
        }

        if (Build.VERSION.SDK_INT >= 11) {
            //去掉分割线
            mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        }
        //设置初始显示的是第几个tab
        mTabHost.setCurrentTab(0);

    }

    /**
     * 创建indiator
     *
     * @param tab  tab
     * @return  view
     */
    private View builderIndiator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);

        img = (ImageView) view.findViewById(R.id.icon_tab);
        text = (TextView) view.findViewById(R.id.text_indicator);
        img.setBackgroundResource(tab.getImage());
        text.setText(tab.getTitle());

        return view;
    }



}
