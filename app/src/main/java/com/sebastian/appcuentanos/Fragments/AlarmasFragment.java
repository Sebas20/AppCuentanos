package com.sebastian.appcuentanos.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastian.appcuentanos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmasFragment extends Fragment {


    public AlarmasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_alarmas, container, false);

        return rootView;

    }

}
