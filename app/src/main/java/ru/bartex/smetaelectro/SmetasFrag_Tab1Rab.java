package ru.bartex.smetaelectro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import ru.bartex.smetaelectro.ru.bartex.smetaelectro.database.P;

public class SmetasFrag_Tab1Rab extends SmetasFrag {

    public static SmetasFrag_Tab1Rab newInstance(long file_id, int position) {
        SmetasFrag_Tab1Rab fragment = new SmetasFrag_Tab1Rab();
        Bundle args = new Bundle();
        args.putLong(P.ID_FILE, file_id);
        args.putInt(P.TAB_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        behaviorWorkOrMat = new BehaviorWorkOrMat_Work(getActivity(),
                tableControllerSmeta, lvSmetas, file_id);
        Log.d(TAG, "SmetasFrag_Tab1Rab onViewCreated tableControllerSmeta ="+ tableControllerSmeta+
                "  lvSmetas = " + lvSmetas + "  file_id = " + file_id);
    }
}
