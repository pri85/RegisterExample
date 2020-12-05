package com.example.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.form.Adapter.ContactAdapter;
import com.example.form.Model.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListExample extends AppCompatActivity {
    List<Contact> contactList = new ArrayList<>();

    private String TAG = getClass().getSimpleName();
    private String url = "https://api.androidhive.info/volley/person_array.json";
    RequestQueue queue;
    RecyclerView contactrecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_example);
        contactrecycler = findViewById(R.id.contact_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListExample.this);
        contactrecycler.setLayoutManager(manager);

        queue = Volley.newRequestQueue(ListExample.this);

      /*   JsonObjectRequest contactRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
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
                        new Contact();
                        singlecontact.setName(name);
                        singlecontact.setEmail(email);
                        singlecontact.setPhone(strphone);

                        contactList.add(singlecontact);
                    }
                    ContactAdapter adapter = new ContactAdapter(ListExample.this,contactList);
                    contactrecycler.setAdapter(adapter);

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
        }); */
      //  queue.add(contactRequest);

        JsonArrayRequest contactRequest= new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                try {
                    for (int i=0;i<response.length();i++){
                        JSONObject person= response.getJSONObject(i);
                        String name =person.getString("name");
                        String email = person.getString("email");
                        JSONObject phone=person.getJSONObject("phone");
                        String strphone=phone.getString("mobile");
                        System.out.println(name + "\n" + email);
                        Contact singlecontact=new Contact(name ,email ,strphone);
                        new Contact();
                        singlecontact.setName(name);
                        singlecontact.setEmail(email);
                        singlecontact.setPhone(strphone);

                        contactList.add(singlecontact);
                    }
                    ContactAdapter adapter=new ContactAdapter(ListExample.this,contactList);
                    contactrecycler.setAdapter(adapter);

                    JSONObject object=response.getJSONObject(response.length()-1);
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
        queue.add(contactRequest);
    }
}