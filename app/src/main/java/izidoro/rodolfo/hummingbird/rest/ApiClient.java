package izidoro.rodolfo.hummingbird.rest;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import izidoro.rodolfo.hummingbird.adapter.MoviesAdapter;
import izidoro.rodolfo.hummingbird.model.Movie;


public class ApiClient extends AsyncTask<String,String, List<Movie> > {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line ="";
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject(finalJson);
            //results é a chave dos resultados do Json
            JSONArray parentArray = parentObject.getJSONArray("results");

            List<Movie> movieModelList = new ArrayList<>();

            Gson gson = new Gson();
            for(int i=0; i<parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);

                Movie movieModel = gson.fromJson(finalObject.toString(), Movie.class);
                movieModelList.add(movieModel);
            }
            return movieModelList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
            try {
                if(reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    @Override
    protected void onPostExecute(final List<Movie> result) {
        super.onPostExecute(result);
    }
}
