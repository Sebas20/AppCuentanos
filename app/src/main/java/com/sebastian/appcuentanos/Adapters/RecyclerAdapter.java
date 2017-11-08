package com.sebastian.appcuentanos.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sebastian.appcuentanos.Clases.Lugar;
import com.sebastian.appcuentanos.Interfaces.InterfaceSite;
import com.sebastian.appcuentanos.R;

import java.util.List;

/**
 * Created by felipe on 24/10/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private final Context context;
    private InterfaceSite listSite;
    private LayoutInflater inflater;
    private List<Lugar> lugares;
    private FragmentManager fm;
    public RecyclerAdapter(Context context, List<Lugar> lugares, InterfaceSite listSite) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lugares = lugares;
        this.listSite = listSite;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(!lugares.isEmpty()){
            holder.tvTitle.setText(lugares.get(position).getNombre());
            holder.tvResenas.setText(Integer.toString(lugares.get(position).getReseñas()));
            Glide.with(context).load(lugares.get(position).getImagen()).into(holder.imagePlace);
            holder.ratingBar.setRating(lugares.get(position).getPuntaje());
            holder.tvCategoria.setText(lugares.get(position).getCategoria());
            holder.tvDireccion.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lugares.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvResenas, tvDireccion,tvCategoria, tvTitle;
        ImageView imagePlace;
        RatingBar ratingBar;
        public MyViewHolder(final View itemView) {
            super(itemView);
            tvResenas = (TextView) itemView.findViewById(R.id.tvReseñas);
            tvCategoria = (TextView) itemView.findViewById(R.id.tvCategorias);
            tvDireccion = (TextView) itemView.findViewById(R.id.tvDireccion);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            imagePlace = (ImageView) itemView.findViewById(R.id.imagePlace);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = lugares.get(getAdapterPosition()).getNombre();
                    listSite.listSite(name);
                }
            });
        }
    }
}
