package com.example.fruits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruits.api.FruitsAPI;
import com.example.fruits.utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    public static final String ITEM_CLICKED = "ITEM_CLICKED";
    private EditText editText;
    private ListView listView;
    private LinkedList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        listView = findViewById(R.id.listView);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LinkedList<String> newList = filterListByString(editText.getText().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_list_item_1, newList);
                listView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FetchItemsTask task = new FetchItemsTask();
        task.execute();

    }

    private LinkedList<String> filterListByString(String filter) {
        LinkedList<String> newList = new LinkedList();
        for (String item: list) {
           if (item.toLowerCase().indexOf(filter.toLowerCase()) != -1) {
               newList.add(item);
           }
        }
        return newList;
    }

    public void setDataFromAPI(JSONObject jsonObject) {
        try {
            list = new LinkedList<>();
            if (jsonObject != null) {
                Log.d("FRUITS", "Json Object is not null");
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                Log.d("FRUITS", "Value in array: " + jsonArray.toString());
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    Log.d("FRUITS", "Value in array: " + item.toString());
                    String name = item.getString("tfvname");
                    list.add(name);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this,  android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String)parent.getItemAtPosition(position);
                        Toast.makeText(getApplicationContext(), "Item " + item + "has been clicked", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                        intent.putExtra(ITEM_CLICKED, item);

                        startActivity(intent);

                    }

                });

            } else {
                Log.d("FRUITS", "Json Object is null");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {

        private JSONObject items;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setDataFromAPI(items);

        }

        @Override
        protected Void doInBackground(Void... errors) {

            items = getAllItems();
            return null;
        }

        private JSONObject getAllItems() {
            Log.d("FRUITS", "Start OnCreate");
            if (Connection.isDataConnectionAvailable(getApplicationContext())) {
                Log.d("FRUITS", "Connection is Available");
                return FruitsAPI.getItems();
            } else {
                Log.d("FRUITS", "Connection is not Available");
            }
            return null;
        }

    }


}
