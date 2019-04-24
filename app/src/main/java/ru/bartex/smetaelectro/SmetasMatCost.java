package ru.bartex.smetaelectro;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.P;

public class SmetasMatCost extends AppCompatActivity {

    public static final String TAG = "33333";
    long file_id;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_mat_cost);
        Log.d(TAG, "//  SmetasMatCost onCreate // " );

        Intent intent = getIntent();
        if (intent!=null){
           file_id = getIntent().getIntExtra(P.ID_FILE,-1);
        }
        Log.d(TAG, "SmetasMatCost onCreate file_id =" + file_id);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_smetas_mat);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.setSelectedItemId(R.id.navigation_smetas_smetas_mat_cost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMat);
        setSupportActionBar(toolbar);
        //показываем заголовокмв заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasMatCost);
        toolbar.setTitleTextColor(Color.GREEN);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMatCost);
        tabLayout.setTabTextColors(Color.WHITE, Color.GREEN);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smetas_mat_cost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_smetas_mat_cost, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home_smetas_mat_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности MainActivity
                    Intent intentHome = new Intent(SmetasMatCost.this, MainActivity.class);
                    // установка флагов- создаём новую задачу и убиваем старую вместе
                    // со всеми предыдущими переходами - работает дёргано по сравнению с манифестом
                    //intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    //      Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentHome);
                    return true;
                case R.id.navigation_smetas_smetas_mat_cost:
                    if (file_id == -1){
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent_smetas = new Intent(SmetasMatCost.this, ListOfSmetasNames.class);
                        startActivity(intent_smetas);
                    }else {
                        // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                        Intent intent = new Intent(SmetasMatCost.this, Smetas.class);
                        intent.putExtra(P.ID_FILE, file_id);
                        startActivity(intent);
                    }
                    return true;
                case R.id.navigation_smetas_mat_cost:
                    // Для данного варианта в манифесте указан режим singlTask для активности ListOfSmetasNames
                    Intent intent_smetas = new Intent(SmetasMatCost.this, ListOfSmetasNames.class);
                    startActivity(intent_smetas);
                    return true;
            }
            return false;
        }
    };
}
