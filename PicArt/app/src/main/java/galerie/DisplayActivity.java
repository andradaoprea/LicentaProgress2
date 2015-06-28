package galerie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.andrada.picart.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DisplayActivity extends Activity {

    private LruCache<Integer,Bitmap> imageCache;
    ImageLoader mImageLoader;
    SlidingDrawer sd;
    TextView tv5, tv7, tv9, tv11;

    //private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        tv5 =(TextView)findViewById(R.id.textView5);
        tv7 =(TextView)findViewById(R.id.textView7);
        tv9 =(TextView)findViewById(R.id.textView9);
        tv11 =(TextView)findViewById(R.id.textView11);


       final Intent inte = getIntent();
        String src = inte.getStringExtra("picture_src");


            String imageUrl = Gallery.PHOTO_BASE_URL + src;
            final NetworkImageView image = (NetworkImageView) findViewById(R.id.imageView2);
            mImageLoader = AppController.getInstance().getImageLoader();
            image.setImageUrl(imageUrl, mImageLoader);

        sd = (SlidingDrawer)findViewById(R.id.slidingDrawer);
        sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                tv5.setText(inte.getStringExtra("picture_name"));
                tv7.setText(inte.getStringExtra("picture_description"));
                tv9.setText(inte.getStringExtra("autor_name"));
                tv11.setText(inte.getStringExtra("autor_description"));
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
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


}
