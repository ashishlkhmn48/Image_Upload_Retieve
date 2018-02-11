package com.ashishlakhmani.imageuploadretieve;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends AppCompatActivity {

    EditText nameText;
    EditText emailText;
    ImageView profilePic;
    Bitmap img;
    Gson gson;
    Contact contact;
    Uri path;

    private static final int img_requestCode = 13;
    private static final int MY_PERMISSIONS_REQUEST = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        nameText = (EditText) findViewById(R.id.name);
        emailText = (EditText) findViewById(R.id.email);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    uploadTask();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onUpload(View view) {

        if (checkAndRequestPermissions()) {
            uploadTask();
        }
    }

    private void uploadTask() {
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        img = BitmapFactory.decodeResource(getResources(), R.drawable.user_default);
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
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, img_requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == img_requestCode && resultCode == RESULT_OK && data != null) {
            path = data.getData();
            Toast.makeText(this, getMimeType(path), Toast.LENGTH_SHORT).show();
            if (path != null && (getMimeType(path).endsWith("jpeg") || getMimeType(path).endsWith("jpg") || getMimeType(path).endsWith("png"))) {
                try {
                    img = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                    profilePic.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please Select Proper Image Format.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
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


    //Permission check
    public boolean checkAndRequestPermissions() {
        int readStoragePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        int writeStoragePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (readStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (writeStoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST);
            return false;
        }

        return true;
    }

}