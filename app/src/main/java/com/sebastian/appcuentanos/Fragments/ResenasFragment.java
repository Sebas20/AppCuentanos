package com.sebastian.appcuentanos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebastian.appcuentanos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResenasFragment extends Fragment {


    SearchView searchView;

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public ResenasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_resenas, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new ContenedorFragment(),"PRINCIPAL").commit();
                return true;
            }
        });



        searchView = (SearchView)rootView.findViewById(R.id.searchView_resenas);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Reseñas");
    }


}
