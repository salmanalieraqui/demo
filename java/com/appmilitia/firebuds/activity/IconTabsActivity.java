package com.appmilitia.firebuds.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.appmilitia.firebuds.R;
import com.appmilitia.firebuds.fragments.OneFragment;
import com.appmilitia.firebuds.fragments.ThreeFragment;
import com.appmilitia.firebuds.fragments.TwoFragment;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


public class IconTabsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<String> myUserDataList;
    public static Double double_latti,double_longi;
    DatabaseReference mydatabaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        myUserDataList = (ArrayList<String>) getIntent().getSerializableExtra("myUserDataList");
        double_latti=getIntent().getDoubleExtra("double_latti",0.0);
        double_longi=getIntent().getDoubleExtra("double_longi",0.0);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FireBuds");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

    }

    private void setupTabIcons() {
        int[] tabIcons = {
                R.drawable.ic_tab_contacts,
                R.drawable.ic_tab_call,
                R.drawable.chats
        };

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle bundle = new Bundle();//create bundle instance
        bundle.putStringArrayList("myUserDataList",myUserDataList);
        OneFragment oneFragment=new OneFragment();
        ThreeFragment threeFragment=new ThreeFragment();
        oneFragment.setArguments(bundle);
        threeFragment.setArguments(bundle);
        adapter.addFrag(oneFragment, "Profile");
        adapter.addFrag(new TwoFragment(), "News Feed");
        adapter.addFrag(threeFragment, "Notifications");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

