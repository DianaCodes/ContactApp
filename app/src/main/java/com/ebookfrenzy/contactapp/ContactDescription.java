package com.ebookfrenzy.contactapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactDescription extends AppCompatActivity {
    EditText nameDesc, phoneDesc, emailDesc, addressDesc;
    Button updateBtn, cancelBtn;

    public static final int PHONE_CALL_REQUEST_CODE = 315;
    private int currentId; //save the currentId
    private boolean cancelled = true;  //what we if don't want to save any changes?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_info);

        //link local variables to the corresponding elements on the layout
        nameDesc = findViewById(R.id.txtNameDesc);
        phoneDesc = findViewById(R.id.txtPhoneDesc);
        emailDesc = findViewById(R.id.txtEmailDesc);
        addressDesc = findViewById(R.id.txtAddressDesc);
        updateBtn = findViewById(R.id.btnUpdate);
        cancelBtn = findViewById(R.id.btnCancel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentId = extras.getInt("ID");
            nameDesc.setText(extras.getString("NAME"));
            phoneDesc.setText(extras.getString("PHONE"));
            emailDesc.setText(extras.getString("EMAIL"));
            addressDesc.setText(extras.getString("ADDRESS"));
        }

        //setup the call button
        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //or use ACTION_DIAL - a ton easier
                //ACTION_CALL is a bit trickier
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneDesc.getText().toString()));
                requestPermission(Manifest.permission.CALL_PHONE, PHONE_CALL_REQUEST_CODE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i); //you will get a compiler warning here, its OK
                }

            }
        });

        //setup the email button
        Button btnEmail = findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("*/*");
                i.putExtra(Intent.EXTRA_EMAIL, emailDesc.getText().toString());
                i.putExtra(Intent.EXTRA_SUBJECT, "TEST MESSAGE");
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivity(i);
                }
            }
        });

        //we made changes and want to save them
        Button btnUpd = findViewById(R.id.btnUpdate);
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelled = false;
                finish();
            }
        });

        //no changes made or want to cancel the changes
        Button btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelled = true;
                finish();
            }
        });
    }

    //get permission if needed
    protected void requestPermission(String permisssionType, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permisssionType}, requestCode);
    }

    //method actually called when permission are requested
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PHONE_CALL_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Unable to show location - permission required", Toast.LENGTH_LONG).show();
                } else {

                }
            }

        }
    }

    //required finish method
    public void finish() {

        if (cancelled) {
            setResult(RESULT_CANCELED);
        } else { //only bother adding date if we are going to send it back
            Intent data = new Intent();
            data.putExtra("ID", currentId);
            data.putExtra("NAME", nameDesc.getText().toString());
            data.putExtra("PHONE", phoneDesc.getText().toString());
            data.putExtra("EMAIL", emailDesc.getText().toString());
            data.putExtra("ADDRESS", addressDesc.getText().toString());
            setResult(RESULT_OK, data);
        }

        //call the super class's finish method
        super.finish();
    }
}

