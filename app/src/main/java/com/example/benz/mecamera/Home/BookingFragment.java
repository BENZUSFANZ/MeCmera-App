package com.example.benz.mecamera.Home;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.benz.mecamera.DatePickerFragment;
import com.example.benz.mecamera.Home.HomeFragment;
import com.example.benz.mecamera.Notifications.NotificationsFragment;
import com.example.benz.mecamera.R;
import com.example.benz.mecamera.ipConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionFragment;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class BookingFragment extends PermissionFragment {


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String ID_user = "userKey";            //save session (sharedpreferences)
    SharedPreferences sharedpreferences;

    public static String id_store, priceS;

    TextView tvDateIn, tvDateOut, tvPrice, tvDays, tvTotal;
    Button btnCancel, btnOk;

    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_booking, container, false);

        /////////// button back in fragment ////////////////////
        final Toolbar tbBooking = (Toolbar)view.findViewById(R.id.tbBooking);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbBooking);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbBooking.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new HomeFragment()); //fragment class
                fr.commit();
            }
        });
        /////////////////////////////////////////////////////////

        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button) view.findViewById(R.id.btnOk);
        tvPrice =(TextView) view.findViewById(R.id.tvPrice);

        tvDays = (TextView)view.findViewById(R.id.tvDays);
        tvTotal = (TextView)view.findViewById(R.id.tvTotal);

        tvDateIn = (TextView) view.findViewById(R.id.tvDate);
        tvDateOut = (TextView) view.findViewById(R.id.tvDateOut);


        try {
            Bundle Booking = getArguments();

            id_store = Booking.getString("id_store");
            priceS = Booking.getString("price");

        } catch (Exception e) {
            e.printStackTrace();
        }

        tvPrice.setText(priceS);

        tvDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setID(1);
                datePicker.show(getFragmentManager(),"date picker");

            }
        });


        tvDateOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setID(2);
                datePicker.show(getFragmentManager(),"date picker");

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new HomeFragment()); //fragment class
                fr.commit();

            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Booking();
            }

        });

       return view;

    }

    private void Booking() {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String id_user = sharedpreferences.getString(ID_user, "");

        String DateIn = tvDateIn.getText().toString();
        String DateOut = tvDateOut.getText().toString();
        String Price = tvTotal.getText().toString();
        String ttDays = tvDays.getText().toString();

        /////// ตรวจสอบค่าว่าง ////////////
        if (DateIn.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่วันที่จอง", Toast.LENGTH_SHORT).show();

        } else if (DateOut.isEmpty()) {

            Toast.makeText(getActivity(), "กรุณาใส่วันที่คืน", Toast.LENGTH_SHORT).show();


        } else {
            //////////////////////////////////

            ipConfig ip = new ipConfig();
            final String UrlBooking = ip.getUrlBooking();

            Ion.with(this)

                    .load(UrlBooking + "Booking.php")

                    .setMultipartParameter("id_user", id_user)
                    .setMultipartParameter("id_store", id_store)

                    .setMultipartParameter("DateIn", DateIn)
                    .setMultipartParameter("DateOut", DateOut)
                    .setMultipartParameter("Price", Price)
                    .setMultipartParameter("ttDays", ttDays)

                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {


                            if (result.endsWith("กรุณารอยืนยันจากผู้ปล่อยเช่า")) {

                                /////// start fragment//////
                                FragmentTransaction fr = getFragmentManager().beginTransaction();
                                fr.replace(R.id.mainFrame,new NotificationsFragment()); //fragment class
                                fr.commit();
                                ////////////////////////////

                                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                alert.setTitle("กำลังดำเนินการ");
                                alert.setMessage("กรุณารอยืนยันจากผู้ปล่อยเช่า");
                                alert.setNegativeButton("ปิด", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        dialogInterface.cancel();

                                    }
                                });

                                alert.show();


                            } else {

                                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }
}
