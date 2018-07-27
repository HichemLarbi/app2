package com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hlarbi.firebaseappbeta1.R;

class MyRecycleAdapter extends RecyclerView.Adapter{
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pollu_recyclev,parent,false);
        return new MyRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MyRecyclerViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return Pollu_Recycle_Data.title_pollu.length;
    }

    private class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private TextView mItemTile;
        private TextView mItemNumber;
        private TextView mItemUnity;

        public MyRecyclerViewHolder(View itemView){
            super(itemView);
            mItemTile = (TextView) itemView.findViewById(R.id.pollu_subject);
            mItemNumber = (TextView) itemView.findViewById(R.id.pollu_number);
            mItemUnity = (TextView) itemView.findViewById(R.id.pollu_unity);
            itemView.setOnClickListener(this);
        }
        public void bindView(int position){
            mItemTile.setText(Pollu_Recycle_Data.title_pollu[position]);
            mItemNumber.setText(Pollu_Recycle_Data.number_pollu[position]);
            mItemUnity.setText(Pollu_Recycle_Data.unity_pollu[position]);

        }
        public void onClick(View view){

      }
    }
}
