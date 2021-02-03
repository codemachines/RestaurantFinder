package com.example.jaydave_sanchitshah_comp304_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantListActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs" ;
    ListView lv_restaurant;
    SharedPreferences sharedpreferences;
    String res_type;
    FusedLocationProviderClient fusedLocationProviderClient;
    String[] address={" 61 Claireville Dr, Etobicoke, ON M9W 5Z7","795 Markham Rd, Scarborough, ON M1H 2Y2"," 1131 Markham Rd, Scarborough, ON M1H 2Y5","671 Markham Rd, Scarborough, ON M1H 2A7"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        lv_restaurant=findViewById(R.id.lv_restaurant);
        res_type=getIntent().getStringExtra("cuisines");
        String currentLat=sharedpreferences.getString("latitude","");
        String currentLong=sharedpreferences.getString("longitude","");
       // fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        String url= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + currentLat + "," + currentLong + "&radius=5000" + "&type=restaurant" + "&keyword= "+res_type+" &sensor=true" + "&key=" + getResources().getString(R.string.google_maps_key);
        new PlaceTask().execute(url);

    }

    private class PlaceTask extends AsyncTask<String,Integer,String> {
        @Override
        protected String doInBackground(String... strings) {
            String data=null;
            try {
                data=downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException {
        URL url=new URL(string);
        HttpURLConnection connection= (HttpURLConnection) url.openConnection();
        connection.connect();
        InputStream stream=connection.getInputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder=new StringBuilder();
        String line="";
        while ((line=reader.readLine()) != null){
            builder.append(line);
        }
        String data=builder.toString();
        reader.close();
        return data;

    }

    private class ParserTask extends AsyncTask<String,Integer, List<HashMap<String,String>>>{
        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JsonParser jsonParser=new JsonParser();
            List<HashMap<String,String>> mapList=null;
            JSONObject object=null;
            try {
                 object=new JSONObject(strings[0]);
                 mapList=jsonParser.parseResult(object);
            }catch (JSONException e){
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(final List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            //hashMaps.clear();
            List<String> nameList = new ArrayList<String>();

            for(Map hashMap : hashMaps){
                nameList.add(hashMap.get("name").toString());
            }


            ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,nameList);

            lv_restaurant.setAdapter(adapter);

            lv_restaurant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    int position=i;
                    Intent intent=new Intent(RestaurantListActivity.this,MapsActivity.class);
                    intent.putExtra("lat",""+hashMaps.get(i).get("lat"));
                    intent.putExtra("lng",""+hashMaps.get(i).get("lng"));
                    intent.putExtra("add",""+hashMaps.get(i).get("name"));
                    intent.putExtra("vicinity",""+hashMaps.get(i).get("vicinity"));
                    startActivity(intent);

                }
            });
            for (int i=0; i<hashMaps.size();i++){
                HashMap<String,String> hashMapList=hashMaps.get(i);


            }
        }
    }


}