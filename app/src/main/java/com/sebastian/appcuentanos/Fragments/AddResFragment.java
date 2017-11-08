package com.sebastian.appcuentanos.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sebastian.appcuentanos.Clases.Reseñas;
import com.sebastian.appcuentanos.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddResFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener{

    RatingBar bar;
    TextView tvReseña;
    Button bAgregar,bCancelar;
    float puntaje=5.0f;
    String idSite;

    public AddResFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        idSite = getArguments().getString("idSite");
        View view = inflater.inflate(R.layout.fragment_add_res, container, false);
        bar = (RatingBar)view.findViewById(R.id.rating);
        tvReseña = (TextView)view.findViewById(R.id.tvReseña);
        bAgregar = (Button)view.findViewById(R.id.bAgregar);
        bCancelar = (Button)view.findViewById(R.id.bCancelar);
        bAgregar.setOnClickListener(this);
        bCancelar.setOnClickListener(this);
        bar.setOnRatingBarChangeListener(this);
        bar.setRating(5.0f);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.bAgregar:
                String text = tvReseña.getText().toString();
                if(!text.isEmpty()){
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference();
                    Reseñas reseñas = new Reseñas("Felipe",idSite,text,puntaje);
                    myRef.child("reseñas").push().setValue(reseñas);

                    getActivity().onBackPressed();
                }else{
                    Toast.makeText(getContext(),"Llene el campo de reseña", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bCancelar:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
        puntaje=v;
    }
}
