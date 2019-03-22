package ru.bartex.smetaelectro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SmetasTab2Materialy extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_smetas_tab2_materialy, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
         //textView.setText(getString(R.string.section_format, 2));
        return rootView;
    }
}
