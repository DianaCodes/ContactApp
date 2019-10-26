package com.ebookfrenzy.contactapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Diana Arita on 10/12/2019.
 */

public class AddContact extends AppCompatActivity {
    EditText name, phone, email, address;
    Button saveBtn, cancelBtn;
    Intent intent;

    private boolean cancelled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.txtName);
        phone = findViewById(R.id.txtPhone);
        email = findViewById(R.id.txtEmail);
        address = findViewById(R.id.txtAddress);
        saveBtn = findViewById(R.id.btnSave);
        cancelBtn = findViewById(R.id.btnCancel);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelled = false;
                finish();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelled = true;
                finish();
            }
        });
    }

    //calling the finish method to return to the calling activity
    public void finish() {
        if (cancelled) {
            setResult(RESULT_CANCELED);
        }else {
            Intent data = new Intent();

            //link to the elements on the layout
            TextView tvName = findViewById(R.id.txtName);
            TextView tvPhone = findViewById(R.id.txtPhone);
            TextView tvEmail = findViewById(R.id.txtEmail);
            TextView tvAddress = findViewById(R.id.txtAddress);

            //populate the intent
            data.putExtra("NAME", tvName.getText().toString());
            data.putExtra("PHONE", tvPhone.getText().toString());
            data.putExtra("EMAIL", tvEmail.getText().toString());
            data.putExtra("ADDRESS", tvAddress.getText().toString());

            //setup the result to be returned to the calling function
            setResult(RESULT_OK, data);
        }

        //call the super class's finish method
        super.finish();
    }
}