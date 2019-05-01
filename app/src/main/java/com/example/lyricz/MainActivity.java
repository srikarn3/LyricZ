package com.example.lyricz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LyricZ: ";

    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> startAPICall());

    }

    void startAPICall() {
        final EditText songText = findViewById(R.id.song_name);
        final EditText artistText = findViewById(R.id.artist_name);
        // Example of how to pull a field off the returned JSON object
        final TextView lyricsText = findViewById(R.id.lyrics_textview);
        lyricsText.setText("Searching...");
        Log.d(TAG, "Searching...");
        String song = songText.getText().toString();
        String artist = artistText.getText().toString();
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.lyrics.ovh/v1/" + artist + "/" + song,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            apiCallDone(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            apiCallFail();
                            Log.e(TAG, error.toString());
                        }
                    });
            jsonObjectRequest.setShouldCache(false);
            queue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object
            final TextView lyricsText = findViewById(R.id.lyrics_textview);
            lyricsText.setText(response.get("lyrics").toString());
        } catch (JSONException ignored) { }
    }

    void apiCallFail() {
        final TextView lyricsText = findViewById(R.id.lyrics_textview);
        lyricsText.setText("Error retrieving lyrics");
        Log.i(TAG, "Error retrieving lyrics");
    }
}
