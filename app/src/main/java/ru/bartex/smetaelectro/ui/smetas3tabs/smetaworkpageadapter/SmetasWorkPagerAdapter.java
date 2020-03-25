package ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkpageadapter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatCat;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamat.MatType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost.MatNameCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetamatcost.MatTypeCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkCat;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkName;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetawork.WorkType;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkNameCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkcost.WorkTypeCost;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.KindWork;
import ru.bartex.smetaelectro.ui.smetas3tabs.smetaworkrecycleradapter.SmetasCostRecyclerAdapter;
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


    //**************************** методы адаптера*********************
    public void updateWorkCategoryAdd(){
        Fragment fragment = getItem(0);
        SmetasWorkRecyclerAdapter adapter = ((WorkCat) fragment).getAdapter();
        adapter.updateWorkCategoryAdd();
    }

    public void updateMatCategoryAdd(){
        Fragment fragment = getItem(0);
        SmetasWorkRecyclerAdapter adapter = ((MatCat) fragment).getAdapter();
        adapter.updateMatCategoryAdd();
    }

    //----------------

    public void updateWorkTypeAdd(long cat_id){
        Fragment fragment = getItem(1);
        SmetasWorkRecyclerAdapter adapter = ((WorkType) fragment).getAdapter();
        adapter.updateWorkTypeAdd(cat_id);
    }

    public void updateMatTypeAdd(long cat_id){
        Fragment fragment = getItem(1);
        SmetasWorkRecyclerAdapter adapter = ((MatType) fragment).getAdapter();
        adapter.updateMatTypeAdd(cat_id);
    }

    //--------------

    public void updateWorkNameAdd(long type_id){
        Fragment fragment = getItem(2);
        SmetasWorkRecyclerAdapter adapter = ((WorkName) fragment).getAdapter();
        adapter.updateWorkNameAdd(type_id);
    }

    public void updateMatNameAdd(long type_id){
        Fragment fragment = getItem(2);
        SmetasWorkRecyclerAdapter adapter = ((MatName) fragment).getAdapter();
        adapter.updateMatNameAdd(type_id);
    }

//----------------

    public void updateWorkType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasWorkRecyclerAdapter adapter = ((WorkType) fragment).getAdapter();
        adapter.updateWorkType(cat_id);
    }

    public void updateWorkName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasWorkRecyclerAdapter adapter = ((WorkName) fragment).getAdapter();
        adapter.updateWorkName(cat_id, type_id);
    }

    public void updateWorkCostType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasCostRecyclerAdapter adapter = ((WorkTypeCost) fragment).getAdapter();
        adapter.updateWorkCostType(cat_id);
    }

    public void updateWorkCostName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasCostRecyclerAdapter adapter = ((WorkNameCost) fragment).getAdapter();
        adapter.updateWorkCostName(cat_id, type_id);
    }

    public void updateMatType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasWorkRecyclerAdapter adapter = ((MatType) fragment).getAdapter();
        adapter.updateMatType(cat_id);
    }

    public void  updateMatName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasWorkRecyclerAdapter adapter = ((MatName) fragment).getAdapter();
        adapter.updateMatName(cat_id, type_id);
    }

    public void updateMatCostType(long cat_id){
        Fragment fragment = getItem(1);
        SmetasCostRecyclerAdapter adapter = ((MatTypeCost) fragment).getAdapter();
        adapter.updateMatCostType(cat_id);
    }

    public void updateMatCostName(long cat_id, long type_id){
        Fragment fragment = getItem(2);
        SmetasCostRecyclerAdapter adapter = ((MatNameCost) fragment).getAdapter();
        adapter.updateMatCostName(cat_id, type_id);
    }


    public void  showDetails(int position, KindWork kind){
        //получаем фрагмент из списка фрагментов в зависимости от позиции вкладки
        Fragment fragment = getItem(position);
        switch (position){
            case 0:
                switch (kind){
                    case WORK:
                        ((WorkCat) fragment).getAdapter().showDetails(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatCat) fragment).getAdapter().showDetails(position, KindWork.MAT);
                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        ((WorkType) fragment).getAdapter().showDetails(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatType) fragment).getAdapter().showDetails(position, KindWork.MAT);
                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        ((WorkName) fragment).getAdapter().showDetails(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatName) fragment).getAdapter().showDetails(position, KindWork.MAT);
                        break;
                }
                break;
        }
    }

    public void  changeName(int position, KindWork kind){
        //получаем фрагмент из списка фрагментов в зависимости от позиции вкладки
        Fragment fragment = getItem(position);
        switch (position){
            case 0:
                switch (kind){
                    case WORK:
                        ((WorkCat) fragment).getAdapter().changeName(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatCat) fragment).getAdapter().changeName(position, KindWork.MAT);
                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        ((WorkType) fragment).getAdapter().changeName(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatType) fragment).getAdapter().changeName(position, KindWork.MAT);
                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        ((WorkName) fragment).getAdapter().changeName(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatName) fragment).getAdapter().changeName(position, KindWork.MAT);
                        break;
                }
                break;
        }
    }

    public void deleteItem(int position, KindWork kind){
        //получаем фрагмент из списка фрагментов в зависимости от позиции вкладки
        Fragment fragment = getItem(position);
        switch (position){
            case 0:
                switch (kind){
                    case WORK:
                        ((WorkCat) fragment).getAdapter().deleteItem(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatCat) fragment).getAdapter().deleteItem(position, KindWork.MAT);
                        break;
                }
                break;

            case 1:
                switch (kind){
                    case WORK:
                        ((WorkType) fragment).getAdapter().deleteItem(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatType) fragment).getAdapter().deleteItem(position, KindWork.MAT);
                        break;
                }
                break;

            case 2:
                switch (kind){
                    case WORK:
                        ((WorkName) fragment).getAdapter().deleteItem(position, KindWork.WORK);
                        break;
                    case MAT:
                        ((MatName) fragment).getAdapter().deleteItem(position, KindWork.MAT);
                        break;
                }
                break;
        }
    }

}
