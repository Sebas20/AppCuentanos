package com.sebastian.appcuentanos.Adapters;

import android.content.Context;
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
 * Created by felipe on 4/11/17.
 */

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.MyViewHolder>{
    private final Context context;
    private InterfaceSite searchInterface;
    private LayoutInflater inflater;
    private List<Lugar> lugares;
    public FragmentAdapter(Context context, List<Lugar> lugares, InterfaceSite searchInterface) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lugares = lugares;
        this.searchInterface = searchInterface;
    }



    @Override
    public FragmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_view,parent,false);
        FragmentAdapter.MyViewHolder holder = new FragmentAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FragmentAdapter.MyViewHolder holder, int position) {
        if(!lugares.isEmpty()){
            holder.tvTitle.setText(lugares.get(position).getNombre());
            holder.tvResenas.setText(Integer.toString(lugares.get(position).getReseñas()));
            Glide.with(context).load(lugares.get(position).getImagen()).into(holder.imagePlace);
            holder.ratingBar.setRating(lugares.get(position).getPuntaje());
            holder.tvDireccion.setText(lugares.get(position).getDireccion());
            holder.tvCategoria.setText(lugares.get(position).getCategoria());
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
                    String id = lugares.get(getAdapterPosition()).getId();
                    //Toast.makeText(context,id, Toast.LENGTH_SHORT).show();
                    searchInterface.searchSite(id);
                }
            });
        }
    }
}
