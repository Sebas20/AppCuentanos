package com.sebastian.appcuentanos.Fragments;


import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sebastian.appcuentanos.PrincipalActivity;
import com.sebastian.appcuentanos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottonNavigationFragment extends Fragment {

    BottomNavigationView bottomNavigationView;

    public BottonNavigationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_botton_navigation, container, false);
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main,new ContenedorFragment(),"PRINCIPAL").commit();
                return true;
            }
        });
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.my_fragment, new CategoriasFragment()).commit();

        View parent = (View) container.getParent();
        bottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.bottonNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_categorias:
                        CategoriasFragment categoriasFragment = (CategoriasFragment) getActivity().getSupportFragmentManager()
                                .findFragmentByTag("CATEGORIAS");

                        if (categoriasFragment != null && categoriasFragment.isVisible()) {
                        } else {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.my_fragment, new CategoriasFragment(), "CATEGORIAS")
                                    .commit();
                        }
                        break;
                    case R.id.nav_promociones:
                        PromocionesFragment promocionesFragment = (PromocionesFragment) getActivity().getSupportFragmentManager()
                                .findFragmentByTag("PROMOCIONES");
                        if (promocionesFragment != null && promocionesFragment.isVisible()) {
                        } else {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.my_fragment, new PromocionesFragment(), "PROMOCIONES")
                                    .commit();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Principal_Botton");
    }


}
