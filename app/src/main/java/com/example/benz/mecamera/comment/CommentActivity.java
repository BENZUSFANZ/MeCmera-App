package com.example.benz.mecamera.comment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CommentActivity extends AppCompatActivity {

    EditText edtComment;
    ImageView icOk;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    String id_store;

    private ListView mListView;
    final ArrayList<CommentList> itemArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        /////////////////set button back ActionBar////////////////
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("แสดงความคิดเห็น");
        ///////////////////////////////////

        edtComment = (EditText)findViewById(R.id.edtComment);
        icOk = (ImageView) findViewById(R.id.icOk);

        id_store = String.valueOf(getIntent().getExtras().get("id_store"));

        mListView = (ListView) findViewById(R.id.LvComment);

        itemArray.clear();  //clear array เพื่อที่จะวนรอบใหม่

        LvComment(0);

        icOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddComment(mListView);

                /// hide keyboard ปิดเเป้มพิมพ์
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

    }

    // back activity toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // custom font
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }


    private void toAddComment(final ListView listView) {

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        final String Comment = edtComment.getText().toString();

        if (Comment.isEmpty()) {

            Toast.makeText(getApplicationContext(), "กรุณาใส่ข้อความ", Toast.LENGTH_SHORT).show();

        }else {

            ipConfig ip = new ipConfig();
            final String Url = ip.getUrlLikeComment();

            Ion.with(this)

                    .load(Url + "AddComment.php")

                    .setMultipartParameter("id_store", id_store)
                    .setMultipartParameter("id_user", id_user)
                    .setMultipartParameter("Comment", Comment)

                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {

                            edtComment.setText(""); //Clear Text

                            itemArray.clear();
                            LvComment(1);

                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }

    private void LvComment(final int Check) {


        ipConfig ip = new ipConfig();
        final String Url = ip.getUrlLikeComment();

        Ion.with(this)

                .load(Url+"ListViewComment.php")

                .setMultipartParameter("id_store", id_store)

                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {

                        JsonObject jsonObject;

                        for(int i = 0; i < result.size(); i++){

                            CommentList item = new CommentList();

                            jsonObject = (JsonObject)result.get(i);

                            item.setName(jsonObject.get("name").getAsString());
                            item.setComment(jsonObject.get("comment").getAsString());
                            item.setImProfile(jsonObject.get("im_profile").getAsString());

                            itemArray.add(item);
                        }

                      ListViewAdapterComment listViewAdapterComment = new ListViewAdapterComment(getApplicationContext(), itemArray, mListView);

                        mListView.setAdapter(listViewAdapterComment);
                        mListView.setClickable(false);

                        // Check ข้อมุลล่าสุด
                        if (Check == 1 ){
                            mListView.setSelection(itemArray.size()); //ข้อมุลล่าสุด
                        }



                    }
                });
    }
}
