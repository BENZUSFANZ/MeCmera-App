package com.example.benz.mecamera.BlockPost;

import android.content.Context;
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

public class RecyclerViewAdapterBlogPost extends RecyclerView.Adapter<RecyclerViewAdapterBlogPost.ViewHolder> {

    private ArrayList<BlogPostList> mBlogPostList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    //  private static FragmentManager fragmentManager;



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvCaption;
        ImageView imPost;
        CircleImageView imProfile;

        View layout;

        public ViewHolder(View itemView) {
            super(itemView);

            layout = itemView;

            tvName = itemView.findViewById(R.id.tvName);
            tvCaption = itemView.findViewById(R.id.tvCaption);

            imProfile = itemView.findViewById(R.id.imProfile);
            imPost = itemView.findViewById(R.id.imPost);


        }
    }


    public RecyclerViewAdapterBlogPost(ArrayList<BlogPostList> myDataset, Context context, RecyclerView recyclerView){

        mBlogPostList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;

    }



    @Override
    public RecyclerViewAdapterBlogPost.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.custom_block_post, parent, false);

        RecyclerViewAdapterBlogPost.ViewHolder viewHolder = new RecyclerViewAdapterBlogPost.ViewHolder(view);

        return viewHolder;
    }



    @Override
    public void onBindViewHolder(RecyclerViewAdapterBlogPost.ViewHolder holder, int position) {

        final BlogPostList Post = mBlogPostList.get(position);


        holder.tvName.setText(Post.getName());
        holder.tvCaption.setText(Post.getCaption());

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        Glide.with(mContext)
                .load(Url+ "upload-Profile/" +Post.getimProfile() )
                .into(holder.imProfile);


        Glide.with(mContext)
                .load(Url +"upload-Post/" +Post.getimPost())
                .into(holder.imPost);


    }



    @Override
    public int getItemCount() {
        return mBlogPostList.size();
    }


}


