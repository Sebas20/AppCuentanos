package com.sebastian.appcuentanos.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sebastian.appcuentanos.Adapters.adapter_RecyclerView;
import com.sebastian.appcuentanos.Clases.PromoVo;
import com.sebastian.appcuentanos.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PromocionesFragment extends Fragment {

    ArrayList<PromoVo> listapromo;
    RecyclerView recyclerpromo;

    public PromocionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_promociones, container, false);
        listapromo = new ArrayList<>();
        recyclerpromo = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerpromo.setLayoutManager(new LinearLayoutManager(getContext()));
        final adapter_RecyclerView adapter = new adapter_RecyclerView(listapromo,this.getActivity());
        recyclerpromo.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("promociones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listapromo.removeAll(listapromo);
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    PromoVo promo = snapshot.getValue(PromoVo.class);
                    listapromo.add(promo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }

}
