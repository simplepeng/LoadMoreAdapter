package me.simple.loadmoreadapter.demo;

import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        TabLayout.Tab tab1 = tabLayout.newTab();
        tab1.setText("Linear");
        TabLayout.Tab tab2 = tabLayout.newTab();
        tab2.setText("Grid");
        TabLayout.Tab tab3 = tabLayout.newTab();
        tab3.setText("Sta");
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.addTab(tab3);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return new LinearFragment();
                    case 1:
                        return new GridFragment();
                    case 2:
                        return new StaggeredFragment();
                }
                return new LinearFragment();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Linear";
                    case 1:
                        return "Grid";
                    case 2:
                        return "Sta";
                }
                return "";
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }
}
