package ru.bartex.smetaelectro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;

public class SmetasFrag_Tab2Mat extends SmetasFrag {

    public static SmetasFrag_Tab2Mat newInstance(long file_id, int position) {
        SmetasFrag_Tab2Mat fragment = new SmetasFrag_Tab2Mat();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        behaviorWorkOrMat = new BehaviorWorkOrMat_Mat(getActivity(),
                 lvSmetas, file_id);
        Log.d(TAG, "BehaviorWorkOrMat_Mat onViewCreated tableControllerSmeta =" +
                "  lvSmetas = " + lvSmetas + "  file_id = " + file_id);
    }
}
