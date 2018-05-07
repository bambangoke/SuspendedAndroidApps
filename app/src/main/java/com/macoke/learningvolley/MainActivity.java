package com.macoke.learningvolley;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private TextView textViewIP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textViewIP = findViewById(R.id.textViewIP);

        mRequestQueue = Volley.newRequestQueue(this);

        VolleyLog.DEBUG = true;

        fetchJsonResponse();


    }

    private void fetchJsonResponse() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, "https://api.jsonbin.io/b/5af04730c83f6d4cc7348f53", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean appSuspended = response.getBoolean("appSuspended");
                            String newApk = response.getString("newApk");

                            if (appSuspended) {
                                textViewIP.setText(newApk);
                                dialogNewApk(newApk);
                            } else {
                                textViewIP.setText("Aplikasi Aman");
                            }

//                            Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        mRequestQueue.add(req);
    }

    private void dialogNewApk(final String newapk) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_background)
                .setTitle(newapk)
                .setMessage("Aplikasi yang lama sudah di banned, install aplikasi yang terbaru.")
                .setPositiveButton("INSTALL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + newapk));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Nanti",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
        dialog.show();
    }
}
