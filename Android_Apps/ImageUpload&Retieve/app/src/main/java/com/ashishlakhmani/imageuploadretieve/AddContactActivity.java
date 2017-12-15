package com.ashishlakhmani.imageuploadretieve;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddContactActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    ImageView profilePic;
    Bitmap img;
    Gson gson;
    Contact contact;
    Uri path;
    private static final int img_requestCode = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        nameText = (EditText) findViewById(R.id.name);
        emailText = (EditText) findViewById(R.id.email);
    }

    public void onUpload(View view) {
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        img = BitmapFactory.decodeResource(getResources(),R.drawable.user_default);
        String pic = imgToString();

        if (!name.isEmpty() && !email.isEmpty()) {
            contact = new Contact(name, email, pic);
            gson = new Gson();
            String jsonString = gson.toJson(contact);
            Log.i("json", jsonString);

            UploadBackground background = new UploadBackground(this);
            background.execute(jsonString);
        } else {
            Toast.makeText(this, "Please Provide Name and Email.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onProfilePic(View view) {
        Toast.makeText(this, "Please Select a Square Image.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, img_requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == img_requestCode && resultCode == RESULT_OK && data != null) {
            path = data.getData();
            if (path != null)
                Toast.makeText(this, path.toString(), Toast.LENGTH_SHORT).show();

            try {
                img = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                profilePic.setImageBitmap(img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imgToString() {
        if (img != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] arr = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(arr, Base64.DEFAULT);
        }
        return "";
    }
}