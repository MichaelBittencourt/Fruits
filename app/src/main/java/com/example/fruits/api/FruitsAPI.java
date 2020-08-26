package com.example.fruits.api;

import android.util.Log;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class FruitsAPI {
    private final static String API_URL = "https://tropicalfruitandveg.com/api/tfvjsonapi.php";
    private final static String SEARCH_COMMAND = "?search=";
    private final static String DETAILS_COMMAND = "?tfvitem=";
    public final static String ALL = "all";

    private static String getSearchString(String item) {
        return API_URL + SEARCH_COMMAND + item;
    }

    private static String getDetailsString(String item) {
        return API_URL + DETAILS_COMMAND + item;
    }

    public static JSONObject getItems() {
        return getItems(ALL);
    }

    public static JSONObject getItems(String searchString) {
        return getJSON(getSearchString(searchString));
    }

    public static JSONObject getItemDetails(String item) {
        return getJSON(getDetailsString(item));
    }

    private static JSONObject getJSON(String queryString) {
        try {
            Log.d("FRUITS", "Query string" + queryString);
            Document doc = Jsoup.connect(queryString).get();
//            Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.android.chrome").get();
            String text = doc.body().text();
            Log.d("FRUITS", "Value Returned: " + text);
            return new JSONObject(text);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

