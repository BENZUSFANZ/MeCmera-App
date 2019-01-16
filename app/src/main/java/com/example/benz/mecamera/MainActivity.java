package com.example.benz.mecamera;

import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.benz.mecamera.BlockPost.BlockFragment;
import com.example.benz.mecamera.Home.HomeFragment;
import com.example.benz.mecamera.MyStore.OtherFragment;
import com.example.benz.mecamera.Notifications.NotificationsFragment;
import com.example.benz.mecamera.Search.SearchFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import q.rorbin.badgeview.QBadgeView;

public class MainActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    public static final int MY_BACKGROUND_JOB = 0;
    public View v;
    public String token = FirebaseInstanceId.getInstance().getToken();
    public QBadgeView qBadgeView;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        //Navigation Bar//
        BottomNavigationView bottomNav = findViewById(R.id.mainBar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //fix icon navigation bar  //
        BottomNavigationViewHelper.disableShiftMode(bottomNav);

        //set Home page//
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                new HomeFragment()).commit();

        /// set badge ให้อยู่ในไอคอนตัวทที่ 3 นับจาก 0
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) bottomNav.getChildAt(0);
        v = bottomNavigationMenuView.getChildAt(3); // number of menu from left

        JobService();   //ใช้งาน JobService

        Badge();    // นับค่า Badge ที่ไอคอน


    }



    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("คุณต้องการปิดแอพพลิเคชัน?");
        builder.setCancelable(true);
        builder.setNegativeButton("ไม่ใช่่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    //Navigation Bar//
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                    Fragment selectedFragment = null;


                    switch (item.getItemId()){

                        case R.id.icHome:
                            selectedFragment =  new HomeFragment();

                            setFragment(selectedFragment);
                            break;

                        case R.id.icBlock:
                            selectedFragment =  new BlockFragment();

                            setFragment(selectedFragment);
                            break;

                        case R.id.icSearch:
                            selectedFragment =  new SearchFragment();

                            setFragment(selectedFragment);
                            break;

                        case R.id.icNotifications:
                            selectedFragment =  new NotificationsFragment();

                            setFragment(selectedFragment);

                            //เมื่อคลิกที่ไอคอน จะซ่อน Badge
                            qBadgeView.setVisibility(View.INVISIBLE);

                            break;

                        case R.id.icOther:
                            selectedFragment =  new OtherFragment();

                            setFragment(selectedFragment);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,
                            selectedFragment).commit();

                    return true;
                }

            };

    private void setFragment(Fragment fragment){

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /// เปิด Job Sevice ให้ทำงานเบื้องหลัง
    public void JobService() {
        JobScheduler js =
                (JobScheduler) getSystemService(this.JOB_SCHEDULER_SERVICE);
        JobInfo job = new JobInfo.Builder(
                MY_BACKGROUND_JOB,
                new ComponentName(this, StartService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build();
        js.schedule(job);
    }

    // เปิด Badge นับไอคอน
    public void Badge(){
    sharedpreferences = this.getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
    final String id_user = sharedpreferences.getString(ID_user, "");

    ipConfig ip = new ipConfig();
    final String Url = ip.getUrlNotistore();

    Ion.with(MainActivity.this)

            .load(Url+ "NotificationStoreCount.php")

            .setMultipartParameter("id_user", id_user)

            .asString()
            .withResponse()
            .setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {

                    qBadgeView  = (QBadgeView) new QBadgeView(MainActivity.this).bindTarget(v).setBadgeNumber(Integer.parseInt(result.getResult()));

                }
            });
    }
}


