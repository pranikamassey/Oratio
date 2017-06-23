package com.darklightning.partycatrers.Caterers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.darklightning.partycatrers.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rikki on 6/12/17.
 */

public class CatrersListAdapter extends RecyclerView.Adapter<CatrersListAdapter.CatrersViewHolder>
{
    Context mContext;
    ArrayList<CatrersListItems> catrersList;

    public CatrersListAdapter(Context mContext,ArrayList<CatrersListItems> catrersList)
    {
        this.mContext = mContext;
        this.catrersList = catrersList;
    }
    @Override
    public CatrersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.catrers_list_cardview,parent,false);
        CatrersViewHolder catrersViewHolder = new CatrersViewHolder(view);
        return  catrersViewHolder;
    }

    @Override
    public void onBindViewHolder(CatrersViewHolder holder, int position) {
        CatrersListItems catrersListItems = catrersList.get(position);
        holder.catrerName.setText(catrersListItems.catrers_name);
        Picasso.with(mContext).load(catrersListItems.catrers_logo_pic).into(holder.catrers_logo_pic);

    }

    @Override
    public int getItemCount() {
        return catrersList.size();
    }

    public class CatrersViewHolder extends RecyclerView.ViewHolder {
        ImageView catrers_logo_pic;
        TextView catrerName;
        public CatrersViewHolder(View itemView) {
            super(itemView);
            catrers_logo_pic = (ImageView) itemView.findViewById(R.id.catrer_pic);
            catrerName = (TextView) itemView.findViewById(R.id.catrer_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context item_context  = v.getContext();
                    Intent intent = new Intent(item_context, CatrerDetailsActivity.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
