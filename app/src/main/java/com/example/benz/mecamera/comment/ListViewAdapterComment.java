package com.example.benz.mecamera.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListViewAdapterComment extends BaseAdapter {

    private List<CommentList> mCommentList;
    private Context mContext;
    private ListView mListview;

    public ListViewAdapterComment(Context context, ArrayList<CommentList> myDataset, ListView listView){
        mCommentList = myDataset;
        mContext = context;
        mListview = listView;
    }

    @Override
    public int getCount() {
       return mCommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ListViewAdapterComment.ViewHolder viewHolder ;

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        if (convertView == null){

            viewHolder = new ListViewAdapterComment.ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_view_comment, parent, false);

            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvComment = convertView.findViewById(R.id.tvComment);
            viewHolder.imProfile = convertView.findViewById(R.id.imProfile);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ListViewAdapterComment.ViewHolder) convertView.getTag();

        }


        // get มาจาก CommentList
        viewHolder.tvName.setText(mCommentList.get(position).getName());
        viewHolder.tvComment.setText(mCommentList.get(position).getComment());

        Glide.with(mContext)
                .load(Url + "upload-Profile/" + mCommentList.get(position).getImProfile())
                .into(viewHolder.imProfile);

        return convertView;

    }

    public class ViewHolder {

        TextView tvName, tvComment;

        CircleImageView imProfile;
    }
}
