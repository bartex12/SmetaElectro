package ru.bartex.smetaelectro;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import ru.bartex.smetaelectro.ru.bartex.smetaelectro.data.SmetaOpenHelper;

public class SmetasMat extends AppCompatActivity implements SmetasMatTab2Type.OnClickTypeMatListener{

    public static final String TAG = "33333";
    long file_id;
    boolean isSelectedType = false;
    long type_id;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    SmetasMatTab3Mat tab3Mat;



    @Override
    public void typeAndCatTransmit(long cat_mat_id, long type_mat_id, boolean isSelectedTypeMat) {
        Log.d(TAG, "//  SmetasMat  typeAndCatTransmit  // " );
        this.isSelectedType = isSelectedTypeMat;
        this.type_id = type_mat_id;
        Log.d(TAG, "SmetasMat  typeAndCatTransmit type_id =" +
                type_id + "  isSelectedType = " + isSelectedType);
        //гениально простой способ заставить обновляться соседнюю вкладку
        //http://qaru.site/questions/683149/my-fragments-in-viewpager-tab-dont-refresh
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smetas_mat);

        file_id = getIntent().getExtras().getLong(P.ID_FILE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMat);
        setSupportActionBar(toolbar);
        //показываем заголовокмв заголовке экрана
        toolbar.setTitle(R.string.title_activity_SmetasMat);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorFab));

        //Создаём адаптер для фрагментов
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Привязываем ViewPager к адаптеру
        mViewPager = (ViewPager) findViewById(R.id.containerMat);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //средняя вкладка открыта
        mViewPager.setCurrentItem(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsMat);
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
        getMenuInflater().inflate(R.menu.menu_smeta_mat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 0: " );
                    SmetasMatTab1Category tab1Category = SmetasMatTab1Category.NewInstance(file_id,position);
                    return tab1Category;
                case 1:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 1: " );
                    SmetasMatTab2Type  tab2Type = SmetasMatTab2Type.NewInstance(file_id,position);
                    return tab2Type;
                case 2:
                    Log.d(TAG, "SmetasMat  Fragment getItem case 2/1: " );
                    //передаём во фрагмент данные (и способ их обработки) в зависимости от isSelectedType
                    tab3Mat = SmetasMatTab3Mat.NewInstance(file_id, position, isSelectedType, type_id);
                    Log.d(TAG, "SmetasMat  Fragment getItem case 2/2: isSelectedType = " +
                            isSelectedType + "  type_id = " +  type_id);
                    return tab3Mat;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }

}
