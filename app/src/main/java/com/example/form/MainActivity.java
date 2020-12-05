package com.example.form;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.form.Model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    EditText date, fullname, email, phno, pass;
    RadioGroup gender;
    CheckBox java, android, sql;
    Button submit;
    SharedPreferences sp1;
    String strgender = "Nil";
    boolean checkJava = false, checkAndroid = false, checkSql = false;
    private String PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    private String NUMBER = "(0/91)?[7-9][0-9]{9}";
    List<Contact> contactList= new ArrayList<>();
    private String url = "https://api.androidhive.info/contacts/";
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        contactList = new ArrayList<>();
        // binding
        fullname = findViewById(R.id.etfullname);
        email = findViewById(R.id.et_email);
        phno = findViewById(R.id.et_phno);
        pass = findViewById(R.id.et_pass);
        gender = findViewById(R.id.rggender);
        submit = findViewById(R.id.btn_submit);
        date = findViewById(R.id.et_date);
        java = findViewById(R.id.cb_java);
        android = findViewById(R.id.cb_android);
        sql = findViewById(R.id.cb_sql);

        java.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkJava = true;
                } else {
                    checkJava = false;
                }
            }
        });
        android.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkAndroid = true;
                } else {
                    checkAndroid = false;
                }
            }
        });
        sql.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkSql = true;
                } else {
                    checkSql = false;
                }
            }
        });
        RadioButton male = findViewById(R.id.rbtn_male);
        RadioButton female = findViewById(R.id.rbtn_female);
        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strgender = "Male";
                }
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    strgender = "Female";
                }
            }
        });
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.YEAR, -18);
                    DatePickerDialog pickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            date.setText(dayOfMonth + "/" + month + "/" + year);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    //pickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                    pickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    pickerDialog.show();
                }
            }
        });
        sp1 = getSharedPreferences("MyP1", MODE_PRIVATE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = fullname.getText().toString();
                String emailm = email.getText().toString();
                String datem = date.getText().toString();
                String phnom = phno.getText().toString();
                String passm = pass.getText().toString();
                int g1 = gender.getCheckedRadioButtonId();
                RadioButton rbgender = findViewById(g1);


                if (name.length() == 0) {
                    fullname.setError("This field is Empty");
                    fullname.requestFocus();
                    return;
                }
                if (email.length() == 0) {
                    email.setError("This Field is required");
                    email.requestFocus();
                    return;
                }
                if (!pass.getText().toString().matches(PASSWORD)) {
//                    Toast.makeText(MainActivity.this, "Pasword incompatible.", Toast.LENGTH_SHORT).show();
                    pass.setError("Pasword incompatible.");
                    return;
                }
                if (!phno.getText().toString().matches(NUMBER)) {
                    phno.setError("Invalid Number");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailm).matches()) {
                    email.setError("Invalid Email Address");
                    email.requestFocus();
                    return;
                }
                if (date.length() == 0) {
                    date.setError("Enter DOB");
                    date.requestFocus();
                    return;
                }
                if (phno.length() == 0) {
                    phno.setError("Enter Phone Number");
                    phno.requestFocus();
                    return;
                }
                int isSelected = gender.getCheckedRadioButtonId();
                if (isSelected == -1) {
                    Toast.makeText(MainActivity.this, "You have not Selected any Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (checkJava == false && checkAndroid == false && checkSql == false) {
                    Toast.makeText(MainActivity.this, "You have Not Selected any Course", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() == 0) {
                    pass.setError("PassWord is Required");
                    pass.requestFocus();
                    return;
                }


                if (name.length() != 0 && emailm.length() != 0 && datem.length() != 0 && phnom.length() != 0 && passm.length() != 0) {
                    String msg = "Name :" + name + "\n" + "Email Address :" + emailm + "\n" + "Birth Date :"
                            + datem + "\n" + "Phone Number :" + phnom + "\n" + "Gender :"
                            + rbgender.getText().toString() + "\n";
                    if (checkJava) {
                        msg += "You are selected JAVA\n";
                    }
                    if (checkAndroid) {
                        msg += "You are selected ANDROID\n";
                    }
                    if (checkSql) {
                        msg += "You are selected SQL";
                    }
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
                SharedPreferences.Editor editor = sp1.edit();
                editor.putString("n", name);
                editor.putString("e", emailm);
                editor.putString("d", datem);
                editor.putString("p", phnom);
                editor.putString("g", strgender);
                editor.putBoolean("java", checkJava);
                editor.putBoolean("android", checkAndroid);
                editor.putBoolean("sql", checkSql);
                editor.putString("p1", passm);
                editor.commit();
                fullname.setText("");
                email.setText("");
                date.setText("");
                phno.setText("");
                pass.setText("");
                startActivity(new Intent(MainActivity.this,ListExample.class));
                finish();
/*
                JsonObjectRequest contactRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        try {
                            JSONArray contacts = response.getJSONArray("contacts");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject singleContact = contacts.getJSONObject(i);
                                String name = singleContact.getString("name");
                                String email = singleContact.getString("email");
                                JSONObject phone = singleContact.getJSONObject("phone");
                                String strphone = phone.getString("mobile");
                                System.out.println(name + "\n" + email);
                                Contact singlecontact = new Contact(name, email, strphone);
                                */
/*new Contact();
                                singlecontact.setName(name);
                                singlecontact.setEmail(email);
                                singlecontact.setPhone(strphone);*//*

                                contactList.add(singlecontact);
                            }
                            JSONObject object = contacts.getJSONObject(contacts.length() - 1);
                            object.getString("name");

                        } catch (JSONException e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                });
*/
//                queue.add(contactRequest);
            }
        });
    }
}