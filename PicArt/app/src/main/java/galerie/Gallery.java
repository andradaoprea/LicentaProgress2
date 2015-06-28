package galerie;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;
import com.example.andrada.picart.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Gallery extends ListActivity {

    public static final String PHOTO_BASE_URL =
            "http://andradapickart.esy.es/webservice/images/";

    ProgressBar pb;
    public  ArrayList<Picture> tablouriList = new ArrayList<>();
    private ListView listView;
    public PictureAdapter adapter;
    public String uri ="http://andradapickart.esy.es/webservice/jsonautori.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

       /* ActionBar actionBar = getActionBar();
        //actionBar.show();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ExTabListener tabListener = new ExTabListener();
        ActionBar.Tab galerie = actionBar.newTab();
        galerie.setText("Galerie");
        galerie.setTabListener(tabListener);
        actionBar.addTab(galerie);


        ActionBar.Tab favorite = actionBar.newTab();
        galerie.setText("Tablouri Favorite");
        galerie.setTabListener(tabListener);
        actionBar.addTab(favorite);*/


        makeJsonObjectRequest(uri);


        pb = (ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);


        // listView = (ListView) findViewById(R.id.list);
        listView = getListView();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent ii = new Intent(Gallery.this,DisplayActivity.class);
                ii.putExtra("picture_id",tablouriList.get(position).getPictureId());
                ii.putExtra("picture_src",tablouriList.get(position).getPhoto());
                ii.putExtra("picture_name",tablouriList.get(position).getNamePic());
                ii.putExtra("picture_description",tablouriList.get(position).getDescriptionPic());
                ii.putExtra("autor_name",tablouriList.get(position).getNumeAutor());
                ii.putExtra("autor_description",tablouriList.get(position).getDescriereAutor());
                startActivity(ii);

            }
        });


        //if(isOnline()) {

        //else{
        //Toast.makeText(this,"Network isn't available",Toast.LENGTH_LONG).show();
        // }


    }


    //@Override
    // public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState){
    //    View v = inflater.inflate(R.layout.item_autor,container,false);


    // }
    public void makeJsonObjectRequest(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tablouriList = APJsonParser.parseFeed(response);

                        updateDisplay();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(Gallery.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    /*private void requestData(String uri) {

        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tablouriList = APJsonParser.parseFeed(response);
                        updateDisplay();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(Gallery.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void updateDisplay(){

        adapter = new PictureAdapter(Gallery.this,tablouriList);
        setListAdapter(adapter);

    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo!=null && netInfo.isConnectedOrConnecting()){
            return true;

        }else{
            return false;
        }
    }

       /* private class MyTask extends AsyncTask<String, String, List<Picture>>
    {

        @Override
        protected void onPreExecute() {
           // updateDisplay();

            if(tasks.size()==0) {
                pb.setVisibility(View.VISIBLE);
            }
            tasks.add(this);
        }

        @Override
        protected List<Picture> doInBackground(String... params) {

           String content = HttpManager.getData(params[0]);
            tablouriList = APJsonParser.parseFeed(content);

            for(Picture tablo : tablouriList)
            {
                try{
                    String imageUrl = PHOTO_BASE_URL + tablo.getPhotoPic();
                    InputStream in = (InputStream) new URL(imageUrl).getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    tablo.setBitmap(bitmap);

                    in.close();

                }catch(Exception e)
                {
                    e.printStackTrace();

                }

            }

            return tablouriList;
        }


        @Override
        protected void onPostExecute(List<Picture> result) {
           //

           updateDisplay();
            tasks.remove(this);
            if(tasks.size()==0) {
                pb.setVisibility(View.INVISIBLE);
            }

            if(result == null)
            {
                Toast.makeText(Gallery.this,"Can't connect to wek service",Toast.LENGTH_LONG);
                return;
            }

            String content = HttpManager.getData(PHOTO_BASE_URL);
           tablouriList = APJsonParser.parseFeed(content);


        }

        @Override
        protected void onProgressUpdate(String... values) {
           // updateDisplay(values[0]);
        }
    }*/
}
