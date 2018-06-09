package com.alejandro.clase.dogypark;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * Created by clase on 12/05/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Parquesfavoritos> mDataset;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.txtListItem);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("changetab", "onclick");
            Parquesfavoritos park= mDataset.get(getAdapterPosition());
            Intent intent = new Intent(mContext, SecondActivity.class);
            intent.putExtra("parque", park.toString());
            intent.putExtra("doShowMap", true);
            mContext.startActivity(intent);

        }
    }

    public MyAdapter(List<Parquesfavoritos> myDataset, Context context) {

        mDataset = myDataset;
        mContext=context;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
       View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.mTextView.setText(mDataset.get(position).getnombreparque());
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
