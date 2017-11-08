package com.sebastian.appcuentanos.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sebastian.appcuentanos.Adapters.SeccionesAdapter;
import com.sebastian.appcuentanos.Clases.Utilidades;
import com.sebastian.appcuentanos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguracionFragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    View view;

    public ConfiguracionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(Utilidades.rotacion == 0){
            appBar.removeView(pestanas);
        }

    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_configuracion, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new ContenedorFragment(),"PRINCIPAL").commit();
                return true;
            }
        });

        Button button = (Button)rootView.findViewById(R.id.bBotton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"hola",Toast.LENGTH_SHORT).show();
            }
        });

        if(true) {
            View parent = (View) container.getParent();
            if (true) {
                appBar = (AppBarLayout) parent.findViewById(R.id.appBar);
                appBar.setBackgroundColor(Color.parseColor("#FFF28887"));
                pestanas = new TabLayout(getActivity());
                pestanas.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
                appBar.addView(pestanas);
                viewPager = (ViewPager) rootView.findViewById(R.id.ViewPagerConfiguracion);
                llenarViewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);
            }
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Configuración");
    }


    private void llenarViewPager(ViewPager viewPager) {
        SeccionesAdapter adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragment(new NotificacionesFragment(),"Notificaciones");
        adapter.addFragment(new AlarmasFragment(),"Alarmas");
        viewPager.setAdapter(adapter);
    }
}
