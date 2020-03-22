package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ru.bartex.smetaelectro.ui.smetas2tabs.SmetasTabWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkType;

public class SmetasWorkPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();


    public SmetasWorkPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, String tabTitle){
        this.fragments.add(fragment);
        this.tabTitles.add(tabTitle);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
       return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    public void updateType(long cat_id){
        Fragment fragment = getItem(1);
        ((WorkType) fragment).getAdapter().updateType(cat_id);
    }

    public void updateName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        ((WorkName) fragment).getAdapter().updateName(cat_id, type_id);
    }

}
