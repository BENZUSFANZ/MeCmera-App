package com.example.benz.mecamera.MyStore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterMyStore extends RecyclerView.Adapter<RecyclerViewAdapterMyStore.ViewHolder>{


    private List<MyStoreList> mStoreList;
    private Context mContext;
    private RecyclerView mRecyclerV;



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPrice, tvCaption, icMore;
        ImageView imStore;
        CircleImageView imProfile;





        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView;

            icMore = itemView.findViewById(R.id.icMore);

            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvCaption = itemView.findViewById(R.id.tvCaption);

            imProfile = itemView.findViewById(R.id.imProfile);
            imStore = itemView.findViewById(R.id.imStore);

        }
    }


    public RecyclerViewAdapterMyStore(ArrayList<MyStoreList> myDataset, Context context, RecyclerView recyclerView){

        mStoreList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;

    }


    @Override
    public RecyclerViewAdapterMyStore.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.custom_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final MyStoreList store = mStoreList.get(position);

        holder.tvName.setText(store.getName());
        holder.tvPrice.setText(store.getPrice());
        holder.tvCaption.setText(store.getCaption());

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        Glide.with(mContext)
                .load(Url+ "upload-Profile/" +store.getimProfile() )
                .into(holder.imProfile);


        Glide.with(mContext)
                .load(Url +"upload-Store/" +store.getimStore())
                .into(holder.imStore);



        holder.icMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setMessage("การตั้งค่า");
                alert.setCancelable(true);
                alert.setNegativeButton("ลบโพสต์", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ipConfig ip = new ipConfig();
                        final String UrlrcvMyStore = ip.getUrlrcvMyStore();

                        int id = store.getId();
                        String id_store = Long.toString(id);

                        Ion.with(mContext)
                                .load(UrlrcvMyStore+"DeleteMyStore.php")
                                .setBodyParameter("id_store", id_store)
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        if (result.endsWith("คุณได้ลบโพสต์เรีบยร้อย")) {

                                            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                    }
                });

                alert.setPositiveButton("เเก้ไขโพสต์", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            UpadateMyStore(store.getId(), store.getCaption(), store.getPrice(), store.getimStore());

                    }

                });

                alert.show();

            }
        });

    }

    private void UpadateMyStore(int id, String caption, String price, String imStore) {

        Bundle DetailStore = new Bundle();

        DetailStore.putInt("id_store", id);
        DetailStore.putString("caption", caption);
        DetailStore.putString("price", price);
        DetailStore.putString("imStore", imStore);

        UpdateMyStoreFragment frag = new UpdateMyStoreFragment();
        frag.setArguments(DetailStore);

        android.support.v4.app.FragmentTransaction fr = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
        fr.replace(R.id.mainFrame, frag);
        fr.commit();


    }


    @Override
    public int getItemCount() {
        return mStoreList.size();
    }


}
