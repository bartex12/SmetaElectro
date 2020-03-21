package ru.bartex.smetaelectro.ui.smetas2tabs;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

//PagerAdapter для класса SmetasTab
//в макете которого TabLayout и ViewPager
public class SmetasTabPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();

    public SmetasTabPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    public void addFragment(Fragment fragment, String tabTitle){
        this.fragments.add(fragment);
        this.tabTitles.add(tabTitle);
    }

    public void remove(int position){
       //получаем фрагмент из списка фрагментов в зависимости от позиции вкладки
       Fragment fragment = getItem(position);
       switch (position){
           case 0:
               ((SmetasTabWork) fragment).getAdapter().removeElement(position);
               break;
           case 1:
               ((SmetasTabMat) fragment).getAdapter().removeElement(position);
               break;
       }
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

}
