package com.example.lepaking_system;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProfileSetupActivity extends AppCompatActivity {

    private Button finishButton; //initialize button
    private EditText userName, userIc, userPhone, streetName, poscode, city; //initialize edittext
    private Spinner state;
    private String id; //initialize id
    private RadioGroup gender; //initialize radio group
    private RadioButton option; //initialize radio button

    DatabaseReference custDb; //initialize database reference to connect with firebase realtime database
    DatabaseReference custRecDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        //initialize widgets in xml file
        finishButton = (Button) findViewById(R.id.finishButton);
        gender = findViewById(R.id.radioGroup);
        userName = (EditText) findViewById(R.id.userName);
        userIc = (EditText) findViewById(R.id.icNumber);
        userPhone = (EditText) findViewById(R.id.phoneNumber);
        streetName = (EditText) findViewById(R.id.streetName);
        poscode = (EditText) findViewById(R.id.poscodeNumber);
        city = (EditText) findViewById(R.id.cityName);
        state = (Spinner) findViewById(R.id.state);

        //when save button is clicked
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCustomer();
            }
        });
    }

    //function to add student in firebase database
    private void addCustomer() {

        //Add Customer
        custDb = FirebaseDatabase.getInstance().getReference("Customer");

        int radioID = gender.getCheckedRadioButtonId(); //to save radio id
        option = (RadioButton) findViewById(radioID);

        String enteredName = (String) userName.getText().toString();
        String enteredIC = (String) userIc.getText().toString();
        String enteredMobile = (String) userPhone.getText().toString();
        String enteredGender = (String) option.getText().toString();
        String enteredStreetName = (String) streetName.getText().toString();
        String enteredPoscode = (String) poscode.getText().toString();
        String enteredCity = (String) city.getText().toString();
        String enteredState = (String) state.getSelectedItem().toString();

        //check the textfield
        if (!TextUtils.isEmpty(enteredName) && !TextUtils.isEmpty(enteredIC) && !TextUtils.isEmpty(enteredMobile) &&
                !TextUtils.isEmpty(enteredGender) && !TextUtils.isEmpty(enteredStreetName) && !TextUtils.isEmpty(enteredPoscode)
                && !TextUtils.isEmpty(enteredCity)  && !TextUtils.isEmpty(enteredState)) {

            //to get user id and email
            FirebaseUser cust = FirebaseAuth.getInstance().getCurrentUser();
            id = cust.getUid();

            String enteredRest = "null";

            //validate ic, phone, poscode
            boolean validateIC = icValidation(enteredIC);
            boolean validatePhone = phoneValidation(enteredMobile);
            boolean validatePoscode = poscodeValidation(enteredPoscode);

            if(validateIC && validatePhone && validatePoscode){
                //create student object
                CustomerInfo customer = new CustomerInfo(enteredName, enteredIC, enteredMobile, enteredGender, enteredStreetName,
                        enteredPoscode, enteredCity, enteredState, enteredRest);

                //save student in firebase
                custDb.child(id).setValue(customer);

                //Add recommendation customer
                custRecDb = FirebaseDatabase.getInstance().getReference("Recommendation_Customer").child(id);

                custRecDb.child("P1").child("rating").setValue("0");
                custRecDb.child("P1").child("restaurant_number").setValue("0");

                custRecDb.child("P2").child("rating").setValue("0");
                custRecDb.child("P2").child("restaurant_number").setValue("0");

                custRecDb.child("P3").child("rating").setValue("0");
                custRecDb.child("P3").child("restaurant_number").setValue("0");

                custRecDb.child("T1").child("rating").setValue("0");
                custRecDb.child("T1").child("restaurant_number").setValue("0");

                custRecDb.child("T2").child("rating").setValue("0");
                custRecDb.child("T2").child("restaurant_number").setValue("0");

                custRecDb.child("T3").child("rating").setValue("0");
                custRecDb.child("T3").child("restaurant_number").setValue("0");

                custRecDb.child("T4").child("rating").setValue("0");
                custRecDb.child("T4").child("restaurant_number").setValue("0");

                //display toast
                Toast.makeText(this, "Profile Success", Toast.LENGTH_LONG).show();

                openMain();
            }

        } else {
            Toast.makeText(this, "Profile Error", Toast.LENGTH_LONG).show();
        }
    }

    //function to change to main page
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //function for ic validation
    public Boolean icValidation(String enteredIC){
        boolean test = true;
        if(enteredIC.length() != 12){
            userIc.setError("Invalid IC");
            test = false;
        }
        else{
            userIc.setError(null);
            test=true;
        }

        return test;
    }

    //function for ic validation
    public Boolean phoneValidation(String enteredPhone){
        boolean test = true;
        if(enteredPhone.length() < 10 || enteredPhone.length() > 11){
            userPhone.setError("Invalid Phone Number");
            test = false;
        }
        else{
            userPhone.setError(null);
            test=true;
        }

        return test;
    }

    //function for ic validation
    public Boolean poscodeValidation(String enteredPoscode){
        boolean test = true;
        if(enteredPoscode.length() != 5){
            poscode.setError("Invalid Poscode");
            test = false;
        }
        else{
            poscode.setError(null);
            test=true;
        }

        return test;
    }

}
