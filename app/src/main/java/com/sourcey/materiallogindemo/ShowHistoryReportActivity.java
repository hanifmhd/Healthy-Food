package com.sourcey.materiallogindemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ASUS on 5/22/2017.
 */

public class ShowHistoryReportActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_report);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_data_from_server(Integer.valueOf(SharedPrefManager.getInstance(this).getIdUser()));
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapter(this, data_list);
        recyclerView.setAdapter(adapter);
    }

    private void load_data_from_server(final int id) {

        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mobile.if.its.ac.id/healthfood/history_report.php?id="+id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray array = new JSONArray(response.body().string());
                    for (int i=0; i<array.length(); i++){
                        JSONObject object = array.getJSONObject(i);
                        MyData data = new MyData(object.getInt("staff"), object.getString("title"),
                                object.getString("description"), object.getString("longitude"),
                                object.getString("latitude"), object.getString("isvalidated"));
                        data_list.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }catch (JSONException e) {
                    System.out.println("End of Content");
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }
}
