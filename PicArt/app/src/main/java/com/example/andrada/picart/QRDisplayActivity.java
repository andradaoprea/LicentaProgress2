package com.example.andrada.picart;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import galerie.APJsonParser;
import galerie.AppController;
import galerie.Gallery;
import galerie.Picture;


public class QRDisplayActivity extends Activity {

    ImageLoader imgLoad;
    public ArrayList<Picture> toateTablourile = new ArrayList<>();
    public String uri ="http://andradapickart.esy.es/webservice/jsonautori.php";
    SlidingDrawer slided;
    TextView tv55,tv77,tv99,tv1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdisplay);

        tv55 =(TextView)findViewById(R.id.textView55);
        tv77 =(TextView)findViewById(R.id.textView77);
        tv99 =(TextView)findViewById(R.id.textView99);
        tv1111 =(TextView)findViewById(R.id.textView1111);
        slided = (SlidingDrawer) findViewById(R.id.slidingDrawer2);


        makeJsonObjectRequest(uri);

        Intent intent = new Intent(
                "com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent,0);
    }



    public void makeJsonObjectRequest(String uri) {
        StringRequest request = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        toateTablourile = APJsonParser.parseFeed(response);


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError ex) {
                        Toast.makeText(QRDisplayActivity.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrdisplay, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                final String contents = intent.getStringExtra("SCAN_RESULT");
                final NetworkImageView image = (NetworkImageView) findViewById(R.id.imageView33);
                imgLoad = AppController.getInstance().getImageLoader();
                image.setImageUrl(Gallery.PHOTO_BASE_URL+contents, imgLoad);

                slided.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
                    @Override
                    public void onDrawerOpened() {
                        for(int i=0; i< toateTablourile.size(); i++)
                        {
                            if((toateTablourile.get(i).getPhoto().equals(contents))){
                                tv55.setText(toateTablourile.get(i).getNamePic());
                                tv77.setText(toateTablourile.get(i).getDescriptionPic());
                                tv99.setText(toateTablourile.get(i).getNumeAutor());
                                tv1111.setText(toateTablourile.get(i).getDescriereAutor());
                            }
                        }

                    }
                });


                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cannot display picture", Toast.LENGTH_LONG).show();
            }
        }

    }
}
