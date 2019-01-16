package com.example.benz.mecamera.Notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class ListViewAdapterNotificationStore extends BaseAdapter {

    private List<NotiList> mNotiList;
    private Context mContext;
    private ListView mListview;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    public ListViewAdapterNotificationStore(Context context, ArrayList<NotiList> myDataset, ListView listView){
    mNotiList= myDataset;
    mContext = context;
    mListview = listView;
    }

    @Override
    public int getCount() {

        return mNotiList.size();
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

        ipConfig ip = new ipConfig();
        final String Url = ip.getUrl();

        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final Integer id_user = Integer.valueOf(sharedpreferences.getString(ID_user, ""));

        final String UrlNoti = ip.getUrlNotistore();

        final ViewHolder  viewHolder ;

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.custom_notifications_store, parent, false);

            viewHolder.tvName = convertView.findViewById(R.id.tvName);
            viewHolder.tvStatus = convertView.findViewById(R.id.tvStatus);
            viewHolder.icMore = convertView.findViewById(R.id.icMore);
            viewHolder.imProfile = convertView.findViewById(R.id.imProfile);

            convertView.setTag(viewHolder);

        }else {

            viewHolder = (ViewHolder) convertView.getTag();
        }

        // store
        if (mNotiList.get(position).getUser_fk() == id_user ){

            if ( mNotiList.get(position).getStatus() == 0){

                viewHolder.tvStatus.setText("ได้ทำกาารเช่าอุปกรณ์ของคุณ");

            }else if (mNotiList.get(position).getStatus() == 1){

                viewHolder.tvStatus.setText("คุณได้ตอบรับการเช่าเเล้ว");
                convertView.setBackgroundResource(R.color.colorNotiMe); //Background Color

            }else {

                viewHolder.tvStatus.setText("คุณได้ยกเลิกการเช่าเเล้ว");
                convertView.setBackgroundResource(R.color.colorNotiMe); //Background Color
            }


        }

        //Customer
        else if (mNotiList.get(position).getCustomer_fk() == id_user ){

            Ion.with(mContext)
                    .load(UrlNoti + "NotificationStoreProfile.php")
                    .setMultipartParameter("User_fk", String.valueOf(mNotiList.get(position).getUser_fk()))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String imPro = result.get("im_profile").getAsString();

                            Glide.with(mContext)
                                    .load(Url + "upload-Profile/" + imPro)  //รูปภาพโปรไฟล์
                                    .into(viewHolder.imProfile);

                            viewHolder.tvName.setText(result.get("name").getAsString());    //ชื่อยูสเซอร์

                        }
                    });

            if ( mNotiList.get(position).getStatus() == 0){

                viewHolder.tvStatus.setText("คุณกำลังรอยืนยัน");

            }else if (mNotiList.get(position).getStatus() == 1){

                viewHolder.tvStatus.setText("ได้ตอบรับการเช่าของคุณเเล้ว");
                convertView.setBackgroundResource(R.color.colorNoti); //Background Color

            }else {

                viewHolder.tvStatus.setText("ได้ยกเลิกการเช่าของคุณ");
                convertView.setBackgroundResource(R.color.colorNoti); //Background Color
            }


        }

        ////// get มาจาก NotiStoreList ////////
        viewHolder.tvName.setText(mNotiList.get(position).getName());

        Glide.with(mContext)
                .load(Url + "upload-Profile/" + mNotiList.get(position).getIm_profile())
                .into(viewHolder.imProfile);

        return convertView;
    }

    private class ViewHolder {

        TextView tvName, tvStatus, icMore;

        CircleImageView imProfile;

    }
}
