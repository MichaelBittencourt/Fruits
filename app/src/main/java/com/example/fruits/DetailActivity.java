package com.example.fruits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fruits.api.FruitsAPI;
import com.example.fruits.utils.Connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class DetailActivity extends AppCompatActivity {

    private TextView textViewName;
    private TextView textViewBotName;
    private TextView textViewOtherNames;
    private TextView tvName;
    private TextView tvBotName;
    private TextView tvOtherNames;
    private ImageView imageView;
    private ScrollView scrollView;
    private TextView textViewScroll;

    private String detailItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textViewName = findViewById(R.id.textViewName);
        textViewBotName = findViewById(R.id.textViewBotName);
        textViewOtherNames = findViewById(R.id.textViewOtherNames);
        tvName = findViewById(R.id.name);
        tvBotName = findViewById(R.id.botName);
        tvOtherNames = findViewById(R.id.otherNames);
        imageView = findViewById(R.id.imageView);
        scrollView = findViewById(R.id.scrollView);
        textViewScroll = findViewById(R.id.descriptionText);

        Intent intent = getIntent();
        detailItem = intent.getStringExtra(MainActivity.ITEM_CLICKED);

        FetchDetailItemTask task = new FetchDetailItemTask();
        task.execute();
    }

    private void setDataFromAPI(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = null;
            jsonArray = jsonObject.getJSONArray("results");

            Log.d("FRUITS", "Value in array: " + jsonArray.toString());
            JSONObject item = jsonArray.getJSONObject(0);
            Log.d("FRUITS", "Value in array: " + item.toString());
            String name = item.getString("tfvname");
            String botName=item.getString("botname");
            String otherName = item.getString("othname");
            String description = item.getString("description");
            String imageURL = item.getString("imageurl");
            tvName.setText(name);
            tvBotName.setText(botName);
            tvOtherNames.setText(otherName);
            textViewScroll.setText(description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class FetchDetailItemTask extends AsyncTask<Void, Void, Void> {

        private JSONObject item;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            setDataFromAPI(item);

        }

        @Override
        protected Void doInBackground(Void... errors) {

            item = getAllDetails();
            return null;

        }

        private JSONObject getAllDetails() {
            Log.d("FRUITS", "Start OnCreate");
            if (Connection.isDataConnectionAvailable(getApplicationContext())) {
                Log.d("FRUITS", "Connection is Available");
                return FruitsAPI.getItemDetails(detailItem);
            } else {
                Log.d("FRUITS", "Connection is not Available");
            }
            return null;
        }

    }
}
