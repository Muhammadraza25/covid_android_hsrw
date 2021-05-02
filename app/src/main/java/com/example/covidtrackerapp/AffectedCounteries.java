package com.example.covidtrackerapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCounteries extends AppCompatActivity {
    EditText edtText;
    ListView listView;
    SimpleArcLoader simpleArcloader;

    public static List<CountryModel> countryModelList= new ArrayList<>();
    CountryModel countryModel;
    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_counteries);

        edtText =findViewById(R.id.edtsearch);
        listView=findViewById(R.id.listview);
        simpleArcloader=findViewById(R.id.loader);
        
        fetchdata();

    }
    private void fetchdata() {
        String url="https://corona.lmao.ninja/v2/countries/";
        simpleArcloader.start();

        StringRequest request= new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                     public void onResponse(String response) {
                        try {
                            JSONArray jsonArray=new JSONArray(response);
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);

                                String countryName=jsonObject.getString("country");
                                String cases=jsonObject.getString("cases");
                                String todayCases=jsonObject.getString("todayCases");
                                String deaths=jsonObject.getString("deaths");
                                String todayDeaths=jsonObject.getString("todayDeaths");
                                String recovered=jsonObject.getString("recovered");
                                String active=jsonObject.getString("active");
                                String critical=jsonObject.getString("critical");

                                JSONObject object=jsonObject.getJSONObject("countryInfo");
                                String flagUrl=object.getString("flag");
                                countryModel=new CountryModel(flagUrl,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical);
                                countryModelList.add(countryModel);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AffectedCounteries.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

}
