package izidoro.rodolfo.hummingbird.helper;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.Toast;

public class DownloadImage {

    Bitmap bmImg;
   public void downloadfile(String fileurl,ImageView img)
    {
        URL myfileurl =null;
        try
        {
            myfileurl= new URL(fileurl);

        }
        catch (MalformedURLException e)
        {

            e.printStackTrace();
        }

        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
            HttpURLConnection conn= (HttpURLConnection)myfileurl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            int length = conn.getContentLength();
            int[] bitmapData =new int[length];
            byte[] bitmapData2 =new byte[length];
            InputStream is = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();

            bmImg = BitmapFactory.decodeStream(is,null,options);

            img.setImageBitmap(Bitmap.createScaledBitmap(bmImg, 120, 120, false));

        }
        catch(IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


}