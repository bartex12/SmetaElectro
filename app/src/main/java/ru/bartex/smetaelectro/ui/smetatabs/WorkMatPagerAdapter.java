package ru.bartex.smetaelectro.ui.smetatabs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class WorkMatPagerAdapter extends FragmentPagerAdapter {

    long file_id;

    public WorkMatPagerAdapter(@NonNull FragmentManager fm, long file_id) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.file_id = file_id;
    }

    //можно не добавлять фрагменты  в список в методе addFragment из Activity,
    //а добавлять их прямо в адаптере в этом методе
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SmetasFrag_Tab1Rab.newInstance(file_id, 0);
            case 1:
                return SmetasFrag_Tab2Mat.newInstance(file_id, 1);

            default:
                return  SmetasFrag_Tab1Rab.newInstance(file_id, 0);
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Работа";
            case 1:
                return "Материалы";
            default:
                return "Работа";
        }
    }

}
