package com.darklightning.partycatrers.Caterers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darklightning.partycatrers.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by chato on 10/11/2017.
 */


public class MapCatrersList extends Fragment {
    RecyclerView recyclerView;
    ArrayList<CatrersListItems> mCatrersList;
    CatrersListAdapter mAdapter;
    DatabaseReference mRootRef;
    DatabaseReference mCatrersListRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_list,container,false);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCatrersListRef = mRootRef.child("CatrersList");
        mCatrersList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        mAdapter = new CatrersListAdapter(getActivity(),mCatrersList);
        recyclerView.setAdapter(mAdapter);
        fetchCatrersList();
        return view;
    }


    private void fetchCatrersList()
    {
        mCatrersListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCatrersList.clear();
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : dataSnapshot.getChildren())
                {
                    CatrersListItems listItems = child.getValue(CatrersListItems.class);
                    mCatrersList.add(listItems);

                    if (listItems != null) {
                        Log.e("show_items_name",""+listItems.catrers_name);
                    }

                }

                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static Fragment newInstance() {
        CaterersList catrersList = new CaterersList();
        return  catrersList;
    }
}
