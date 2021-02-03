package com.example.jaydave_sanchitshah_comp304_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView lv_cuisines;
    String[] itemList={"Indian","Chinese","Thai","Italian","Mexican"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        lv_cuisines=findViewById(R.id.lv_cuisines);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);

        lv_cuisines.setAdapter(adapter);
        lv_cuisines.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(MainActivity.this, ""+itemList[i], Toast.LENGTH_SHORT).show();
                int position=i;
                Intent intent=new Intent(MainActivity.this,RestaurantListActivity.class);
                intent.putExtra("cuisines",""+itemList[position]);
                startActivity(intent);

            }
        });


    }
}