package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasMatRecyclerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasWorkRecyclerAdapter;

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

    public void updateWorkType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasWorkRecyclerAdapter adapter = ((WorkType) fragment).getAdapter();
        adapter.updateType(cat_id);
    }

    public void updateWorkName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasWorkRecyclerAdapter adapter = ((WorkName) fragment).getAdapter();
        adapter.updateName(cat_id, type_id);
    }

    public void updateMatType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasMatRecyclerAdapter adapter = ((MatType) fragment).getAdapter();
        adapter.updateType(cat_id);
    }

    public void  updateMatName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasMatRecyclerAdapter adapter = ((MatName) fragment).getAdapter();
        adapter.updateName(cat_id, type_id);
    }

}
