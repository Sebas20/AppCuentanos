package com.sebastian.appcuentanos.Fragments;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;

import com.sebastian.appcuentanos.Adapters.SeccionesAdapter;
import com.sebastian.appcuentanos.Clases.Utilidades;
import com.sebastian.appcuentanos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContenedorFragment extends Fragment {

    private AppBarLayout appBar;
    private TabLayout pestanas;
    private ViewPager viewPager;
    View view;


    public ContenedorFragment() {
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
        Log.d("cristian", "oncreateView()");
        view = inflater.inflate(R.layout.fragment_contenedor, container, false);
        if(true) {
            View parent = (View) container.getParent();
            if (true) {
                appBar = (AppBarLayout) parent.findViewById(R.id.appBar);
                appBar.setBackgroundColor(Color.parseColor("#FFF28887"));
                pestanas = new TabLayout(getActivity());
                pestanas.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));
                appBar.addView(pestanas);
                viewPager = (ViewPager) view.findViewById(R.id.ViewPagerInformacion);
                llenarViewPager(viewPager);
                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);
            }
        }else{
            Utilidades.rotacion = 1;
        }

        return  view;
    }

    private void llenarViewPager(ViewPager viewPager) {
        SeccionesAdapter adapter = new SeccionesAdapter(getFragmentManager());
        adapter.addFragment(new CategoriasFragment(),"Categorias");
        adapter.addFragment(new PromocionesFragment(),"Promociones");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Principal");
    }





}
