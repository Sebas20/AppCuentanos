package com.sebastian.appcuentanos.Adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sebastian.appcuentanos.Clases.PromoVo;
import com.sebastian.appcuentanos.R;

import java.util.ArrayList;

/**
 * Created by Usuario on 21/10/2017.
 */

public class adapter_RecyclerView extends RecyclerView.Adapter<adapter_RecyclerView.viewHoldPromo>{

    ArrayList<PromoVo> listapromo;
    Context context;

    public adapter_RecyclerView(ArrayList<PromoVo> listapromo, Context context) {
        this.listapromo = listapromo;
        this.context = context;
    }

    public class viewHoldPromo extends RecyclerView.ViewHolder {
        TextView tNombre,tDireccion,tTelefono;
        ImageView iFoto;
        public viewHoldPromo(View itemView) {
            super(itemView);
            tNombre = (TextView)itemView.findViewById(R.id.tNombre);
            tDireccion = (TextView)itemView.findViewById(R.id.tDireccion);
            tTelefono = (TextView)itemView.findViewById(R.id.tTelefono);
            iFoto = (ImageView)itemView.findViewById(R.id.iFoto);
        }
    }

    @Override
    public viewHoldPromo onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new viewHoldPromo(view);
    }

    @Override
    public void onBindViewHolder(viewHoldPromo holder, int position) {
        holder.tNombre.setText(listapromo.get(position).getNombre_Restaurante());
        holder.tDireccion.setText(listapromo.get(position).getDescripcion());
        holder.tTelefono.setText(listapromo.get(position).getValidez());
        Glide.with(context)
                .load(listapromo.get(position).getFoto())
                .into(holder.iFoto);
    }

    @Override
    public int getItemCount() {
        return listapromo.size();
    }
}
