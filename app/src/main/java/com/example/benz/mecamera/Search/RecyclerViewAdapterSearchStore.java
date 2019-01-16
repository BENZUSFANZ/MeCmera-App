package com.example.benz.mecamera.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterSearchStore extends RecyclerView.Adapter<RecyclerViewAdapterSearchStore.ViewHolder> {

    private ArrayList<SearchList> mSearchList;
    private Context mContext;
    private RecyclerView mRecyclerV;

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice, tvCaption;
        ImageView imStore;
        CircleImageView imProfile;

        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView;

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCaption = itemView.findViewById(R.id.tvCaption);

            imProfile = itemView.findViewById(R.id.imProfile);
            imStore = itemView.findViewById(R.id.imStore);

        }
    }

    public RecyclerViewAdapterSearchStore(ArrayList<SearchList> myDataset, Context context, RecyclerView recyclerView){

        mSearchList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;

    }

    @Override
    public RecyclerViewAdapterSearchStore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.custom_view, parent, false);

        RecyclerViewAdapterSearchStore.ViewHolder viewHolder = new RecyclerViewAdapterSearchStore.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerViewAdapterSearchStore.ViewHolder holder, int position) {

        final SearchList store = mSearchList.get(position);

        holder.tvName.setText(store.getName());
        holder.tvPrice.setText(store.getPrice());
        holder.tvCaption.setText(store.getCaption());

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        Glide.with(mContext)
                .load(Url+ "upload-Profile/" +store.getImProfile() )
                .into(holder.imProfile);

        Glide.with(mContext)
                .load(Url +"upload-Store/" +store.getImStore())
                .into(holder.imStore);

        holder.imProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DetailStore(store.getId(), store.getImProfile(), store.getName(), store.getCaption(),
                        store.getPrice(), store.getImStore());
            }
        });

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailStore(store.getId(), store.getImProfile(), store.getName(), store.getCaption(),
                        store.getPrice(), store.getImStore());
            }
        });

    }

    private void DetailStore(int id, String imProfile, String name, String caption, String price, String imStore) {

        Bundle DetailStore = new Bundle();

        DetailStore.putInt("id_store", id);
        DetailStore.putString("imProfile", imProfile);
        DetailStore.putString("name", name);
        DetailStore.putString("caption", caption);
        DetailStore.putString("price", price);
        DetailStore.putString("imStore", imStore);

        DetailShowSearchFragment frag = new DetailShowSearchFragment();
        frag.setArguments(DetailStore);

       android.support.v4.app.FragmentTransaction fr = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, frag);
        fr.commit();

    }


    @Override
    public int getItemCount() {
        return mSearchList.size();
    }
}
