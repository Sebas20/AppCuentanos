package com.sebastian.appcuentanos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sebastian.appcuentanos.Adapters.FragmentAdapter;
import com.sebastian.appcuentanos.Clases.Lugar;
import com.sebastian.appcuentanos.Interfaces.InterfaceSite;
import com.sebastian.appcuentanos.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends DialogFragment {

    InterfaceSite searchSite;
    FragmentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Lugar> lugares;


    public static ListFragment newInstance(String name){
        ListFragment listFragment = new ListFragment();
        Bundle args = new Bundle();
        args.putString("name",name);
        listFragment.setArguments(args);
        return listFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            searchSite = (InterfaceSite) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement communicator");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String name = getArguments().getString("name");
        Toast.makeText(getContext(),name, Toast.LENGTH_SHORT).show();
        View view = inflater.inflate(R.layout.fragment_list,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        conseguirLugares(name);
        adapter = new FragmentAdapter(getContext(),lugares,searchSite);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    void conseguirLugares(String name){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lugares");
        lugares = new ArrayList<Lugar>();
        Toast.makeText(getContext(),"hijo"+myRef.child("cat3").getKey(), Toast.LENGTH_SHORT).show();
        myRef.orderByChild("Nombre").equalTo(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        lugares.add(data.getValue(Lugar.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
