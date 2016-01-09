package com.epollomes.volleytest;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.epollomes.volleytest.app.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


// Based on the following tutorial
// http://www.androidhive.info/2014/05/android-working-with-volley-library-1/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "XXXXX";
    Button btnJsonObjectRequest;
    Button btnJsonArrayRequest;
    Button btnStringRequest;
    Button btnPostParameters;
    Button btnLoadNetworkImageView;
    Button btnLoadNormalImageView;
    Button btnLoadImageViewWithErrorAndLoading;
    Button btnClearCache;

    TextView txtViewResults;
    NetworkImageView networkImageView1;
    ImageView normalImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJsonObjectRequest = (Button) findViewById(R.id.btn_json_request);
        btnJsonObjectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJSONObjectRequest();
            }
        });

        btnJsonArrayRequest = (Button) findViewById(R.id.btn_json_array_request);
        btnJsonArrayRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeJSONArrayRequest();
            }
        });

        btnStringRequest = (Button) findViewById(R.id.btn_string_request);
        btnStringRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStringRequest();
            }
        });

        btnPostParameters = (Button) findViewById(R.id.btn_post_with_params);
        btnPostParameters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postRequestWithParametersAndHeaders();
            }
        });

        btnLoadNetworkImageView = (Button) findViewById(R.id.btn_post_with_arguments);
        btnLoadNetworkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNetworkImageView();
            }
        });

        btnLoadNormalImageView = (Button) findViewById(R.id.btn_load_normal_image_view);
        btnLoadNormalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIntoNormalImageView();
            }
        });

        btnLoadImageViewWithErrorAndLoading = (Button) findViewById(R.id.btn_load_image_with_placeholder_anderror);
        btnLoadImageViewWithErrorAndLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAnImageWithPreloadingPlaceholder();
            }
        });

        btnClearCache = (Button) findViewById(R.id.btn_clear_cache);
        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCache();
            }
        });

        networkImageView1 = (NetworkImageView) findViewById(R.id.network_image_view1);
        txtViewResults = (TextView) findViewById(R.id.txt_view_results);
        normalImageView2 = (ImageView) findViewById(R.id.image_view2);
    }


    /**
     *
     *    Turning off cache: If you want disable the cache for a particular url, you can use setShouldCache() method as below.
     *   // String request
     *   StringRequest stringReq = new StringRequest(....);
     *   // Disable cache
     *   stringReq.setShouldCache(false);
     *
     *
     */


    /**
     * Method to fetch a simple JSON object with Volley
     * Following code will make a json object request where the json response will start with object notation ‘{‘
     */
    private void makeJSONObjectRequest() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://api.androidhive.info/volley/person_object.json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                        txtViewResults.setText(response.toString());
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    /**
     * Method to fetch a simple JSON array with Volley
     * Following will make json array request where the json response starts with array notation ‘[‘
     */

    private void makeJSONArrayRequest() {
        String tag_json_arry = "json_array_req";
        String url = "http://api.androidhive.info/volley/person_array.json";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                        txtViewResults.setText(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }


    /**
     * Method to fetch a string text file with Volley
     * Following will make json array request where the json response starts with array notation ‘[‘
     */
    public void makeStringRequest() {
        // Tag used to cancel the request
        String tag_string_req = "string_req";

        String url = "http://api.androidhive.info/volley/string_response.html";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                pDialog.hide();
                txtViewResults.setText(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Method to post with parameters with Volley
     */
    public void postRequestWithParametersAndHeaders() {

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        String url = "http://www.epollomes.com/imgupload/test2525.php";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                pDialog.hide();
                txtViewResults.setText(response.toString());

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("site", "code");
                params.put("network", "EPOLLOMES PARAMS EPOLLOMES");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("apiKey", "EPOLLOMES PARAMS EPOLLOMES");
                return headers;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);

    }


    /**
     * Method to load image into networkImageView
     */
    public void loadNetworkImageView() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        // If you are using NetworkImageView
        networkImageView1.setImageUrl("http://www.epollomes.com/images/about2.jpg", imageLoader);
    }


    /**
     * Method to load image normally ImageView
     */
    public void loadIntoNormalImageView() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();

        imageLoader.get("http://www.epollomes.com/images/Sugar-Monster/sugar-monster-1.jpg", new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    normalImageView2.setImageBitmap(response.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Image Load Error: " + error.getMessage());
            }
        });
    }

    /**
     * Method to load image normally ImageView
     */
    public void loadingAnImageWithPreloadingPlaceholder() {
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get("http://www.epollomes.com/images/Straight-Penta/Straight-Penta-Feature.png", ImageLoader.getImageListener(
                normalImageView2, R.drawable.loading, R.drawable.sadface));
    }


    private void clearCache() {
        // clear single URL = single delete
        AppController.getInstance().getRequestQueue().getCache().remove("http://www.epollomes.com/images/Straight-Penta/Straight-Penta-Feature.png");
        AppController.getInstance().getRequestQueue().getCache().remove("http://www.epollomes.com/images/Sugar-Monster/sugar-monster-1.jpg");
        AppController.getInstance().getRequestQueue().getCache().remove("http://www.epollomes.com/images/about2.jpg");
        // clearing all cache = delete all cache
        AppController.getInstance().getRequestQueue().getCache().clear();
    }


    private void cancelRequest() {
        //Cancel Single Request
        String tag_json_arry = "json_req";
        AppController.getInstance().getRequestQueue().cancelAll("feed_request");

        //Cancel All Request
        AppController.getInstance().getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });

        //  This cancels all Requests from all activities/fragments, and doesn’t work favorably with the Activity Lifecycle.
        //  The best way to manage this is to add a String tag unique to your fragment.


    }

}
