package com.studio.parseviewpaises;


import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterListaPaises extends RecyclerView.Adapter<AdpterListaPaises.ViewHolder>{
    private List<Paises> list;
    private LayoutInflater layoutInflater;
    private static RecyclerViewOnClickListener recyclerViewOnClickListener;

    public AdpterListaPaises(List<Paises> list, LayoutInflater layoutInflater) {
        this.list = list;
        this.layoutInflater = layoutInflater;
    }


    public void setRecyclerOnClickListener(RecyclerViewOnClickListener recyclerViewOnClickListener){
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.listpaises,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.nome.setText(list.get(position).getNome());


        //Glide.with(layoutInflater.getContext()).load(Uri.parse(list.get(position).getBandeira()))
              //  .into(holder.bandeira);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView bandeira;
        public TextView nome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bandeira = (CircleImageView) itemView.findViewById(R.id.Bandeira);
            nome = (TextView) itemView.findViewById(R.id.txtNome);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(recyclerViewOnClickListener != null){
                recyclerViewOnClickListener.onClickListener(v,getLayoutPosition());
            }
        }
    }
}
