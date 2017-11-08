package com.sebastian.appcuentanos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sebastian.appcuentanos.Clases.DialogoAlerta;
import com.sebastian.appcuentanos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriasFragment extends Fragment implements View.OnClickListener {

    LinearLayout lRestaurante, lHoteles, lBares, lHospitales, lUniversidades, lBarberias, lCompras, lVeterinarias, lCines;

    public CategoriasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view) {
        String categoria;
        switch (view.getId()) {
            case R.id.llRestaurante:
                 categoria = "Restaurantes";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llHoteles:
                categoria = "Hoteles";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llBares:
                categoria = "Bares";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llHospitales:
                categoria = "Hospitales";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llUniversidades:
                categoria = "Universidades";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llBarberias:
                categoria = "Barberias";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llVeterinarias:
                categoria = "Veterinarias";
                mCallback.onItemCategoria(categoria);
                break;
            case R.id.llCine:
                categoria = "Cines";
                mCallback.onItemCategoria(categoria);
                break;
            default: {

            }
        }

    }

    public interface MyInterfaceCategorias {
        /**
         * Called by HeadlinesFragment when a list item is selected
         */
        public void onItemCategoria(String categoria);
    }

    MyInterfaceCategorias mCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_categorias, container, false);
        lRestaurante = (LinearLayout) rootView.findViewById(R.id.llRestaurante);
        lHoteles = (LinearLayout) rootView.findViewById(R.id.llHoteles);
        lBares = (LinearLayout) rootView.findViewById(R.id.llBares);
        lHospitales = (LinearLayout) rootView.findViewById(R.id.llHospitales);
        lUniversidades = (LinearLayout) rootView.findViewById(R.id.llUniversidades);
        lBarberias = (LinearLayout) rootView.findViewById(R.id.llBarberias);
        lVeterinarias = (LinearLayout) rootView.findViewById(R.id.llVeterinarias);
        lCines = (LinearLayout) rootView.findViewById(R.id.llCine);

        lRestaurante.setOnClickListener(this);
        lHoteles.setOnClickListener(this);
        lBares.setOnClickListener(this);
        lHospitales.setOnClickListener(this);
        lUniversidades.setOnClickListener(this);
        lBarberias.setOnClickListener(this);
        lVeterinarias.setOnClickListener(this);
        lCines.setOnClickListener(this);

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (CategoriasFragment.MyInterfaceCategorias) context;
    }

}
