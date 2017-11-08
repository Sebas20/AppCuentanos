package com.sebastian.appcuentanos.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sebastian.appcuentanos.Adapters.RecyclerAdapter;
import com.sebastian.appcuentanos.Clases.Lugar;
import com.sebastian.appcuentanos.Interfaces.InterfaceSite;
import com.sebastian.appcuentanos.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class SitesFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    ArrayList<Lugar> lugares;
    InterfaceSite listSite;
    int i;

    public SitesFragment() {
        // Required empty public constructor
        i=0;
    }

    public static SitesFragment newInstance(FragmentManager fm){
        SitesFragment sitesFragment = new SitesFragment();
        Bundle args = new Bundle();
        args.putParcelable("fm",(Parcelable)fm);
        sitesFragment.setArguments(args);
        return sitesFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listSite = (InterfaceSite) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement communicator");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sites, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        conseguirLugares("Compras");
        adapter = new RecyclerAdapter(getContext(),lugares,listSite);
        try{
            recyclerView.setAdapter(adapter);
        }catch (Exception e){
            Toast.makeText(getContext(),"Empty", Toast.LENGTH_SHORT).show();
        }

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    void conseguirLugares(String categoria){
        lugares = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://recyclerview-bab2e.firebaseio.com").
                addConverterFactory(GsonConverterFactory.create()).build();
        Firebase firebase = retrofit.create(Firebase.class);
        Call call = firebase.getJSON("%22Categoria%22","%22"+categoria+"%22");
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String stringJson =  new Gson().toJson(response.body());
                ArrayList<Lugar> temp = new ArrayList<Lugar>();
                try{

                    JsonObject json = new JsonParser().parse(stringJson).getAsJsonObject();

                    for (Map.Entry<String, JsonElement> entry: json.entrySet()) {
                        JsonElement element = json.get(entry.getKey());
                        lugares.add(new Gson().fromJson(element,Lugar.class));
                        System.out.println("prueba"+(new Gson().fromJson(element,Lugar.class)).getId());

                    }

                    ArrayList<String> names = new ArrayList<String>();
                    for(Lugar l:lugares){
                        names.add(l.getNombre());
                    }
                    for(String name:names){
                        float scoreprom=0;
                        int reseñas=0;
                        ArrayList<Lugar> indexes=new ArrayList<Lugar>();
                        for(Lugar lugar:lugares) {
                            if(lugar.getNombre().equals(lugares.get(0).getNombre())){ indexes.add(lugar);}
                        }
                        Log.e("json","a"+lugares.size());

                        float i =0;
                        for(Lugar rem:indexes){
                            scoreprom=scoreprom+rem.getPuntaje();
                            reseñas=reseñas+rem.getReseñas();
                            lugares.remove(rem);
                            i++;
                        }

                        if(indexes.isEmpty()){
                            break;
                        }
                        Lugar nw = indexes.get(0);
                        scoreprom=scoreprom/i;
                        nw.setReseñas(reseñas);
                        nw.setPuntaje(scoreprom);
                        temp.add(nw);
                    }
                    for(Lugar x:temp){
                        lugares.add(x);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    public interface Firebase{
        @GET("/lugares.json")
        Call<Object> getJSON(@Query("orderBy") String key, @Query("equalTo") String value);
        //https://recyclerview-bab2e.firebaseio.com/lugares.json?orderBy=%22Id%22&equalTo=%22cat1%22
    }

    private <T> Iterable<T> iteratorToIterable(final Iterator<T> iterator) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return iterator;
            }
        };
    }
}

   /* void conseguirLugares(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("lugares");
        lugares = new ArrayList<Lugar>();

        myRef.orderByChild("Categoria").equalTo("Compras").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        lugares.add(data.getValue(Lugar.class));
                        Toast.makeText(getContext(),"#"+(++i),Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/


