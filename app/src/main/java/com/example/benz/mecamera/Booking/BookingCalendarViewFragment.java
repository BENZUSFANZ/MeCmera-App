package com.example.benz.mecamera.Booking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benz.mecamera.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingCalendarViewFragment extends Fragment {


    public BookingCalendarViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_calendar_view, container, false);



        return view;
    }

}
