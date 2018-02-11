package com.ashishlakhmani.imageuploadretieve;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ViewContactActiivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_actiivity);


        ViewBackground viewBackground = new ViewBackground(this, this);
        viewBackground.execute();

    }
}