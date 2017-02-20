package izidoro.rodolfo.hummingbird;

import android.content.Context;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import izidoro.rodolfo.hummingbird.adapter.MoviesAdapter;
import izidoro.rodolfo.hummingbird.model.Movie;
import izidoro.rodolfo.hummingbird.model.MovieResponse;
import izidoro.rodolfo.hummingbird.rest.ApiClient;
import izidoro.rodolfo.hummingbird.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.os.Handler;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "10ad52a9e31212500752532dddaa9df5";
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/original";

    private ListView lvMovies;

    private MoviesAdapter adapter;
    private List<Movie> mProductList;
    public Handler mHandler;
    public View ftView;
    public boolean isLoading = false;
    public int currentPage=1;
    private List<Movie> lst = new ArrayList<>();
    private SearchView searchMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        searchMovie = (SearchView) findViewById(R.id.searchMovie);

        LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ftView = li.inflate(R.layout.footer_view, null);
        mHandler = new MyHandler();


        init();

        lvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {


                if(view.getLastVisiblePosition() == totalItemCount-1 && lvMovies.getCount() >=10 && isLoading == false && !searchMovie.getQuery().toString().isEmpty()){

                }
                if(view.getLastVisiblePosition() == totalItemCount-1 && lvMovies.getCount() >=10 && isLoading == false) {
                    isLoading = true;
                    Thread thread = new ThreadGetMoreData();
                    thread.start();
                }

            }
        });

        searchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentPage = 0;
                Thread thread = new ThreadSearchMovie();
                //Start thread
                thread.start();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    init();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });


    }

    private void init() {
        ApiInterface servis = ApiClient.getClient().create(ApiInterface.class);
        currentPage = 1;

        Call<MovieResponse> call = servis.getTopRatedMovies(API_KEY,getString(R.string.language),currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                int statusCode = response.code();
                lst = response.body().getResults();
                adapter = new MoviesAdapter( getApplicationContext(),lst);
                lvMovies.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Log.i("MainActivity::onFailure", "Hata : " + t.toString());
            }
        });
    }

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //Add loading view during search processing
                    lvMovies.addFooterView(ftView);
                    break;
                case 1:
                    //Update data adapter and UI
                    adapter.addListItemToAdapter((List<Movie>)msg.obj);
                    //Remove loading view after update listview
                    lvMovies.removeFooterView(ftView);
                    isLoading=false;
                    break;
                default:
                    break;
            }
        }
    }

//    private List<Movie> getMoreData() {
//        lst = new ArrayList<>();
//        ApiInterface servis = ApiClient.getClient().create(ApiInterface.class);
//
//
//        Call<MovieResponse> call = servis.getTopRatedMovies(API_KEY,"pt-br",2);
//        call.enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                int statusCode = response.code();
//                lst = response.body().getResults();
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//
//                Log.i("MainActivity::onFailure", "Hata : " + t.toString());
//            }
//        });
//
//
//        return lst;
//    }
    public class ThreadGetMoreData extends Thread {
        @Override
        public void run() {
            //Add footer view after get data
            mHandler.sendEmptyMessage(0);
            //Search more data
            ApiInterface servis = ApiClient.getClient().create(ApiInterface.class);


            Call<MovieResponse> call = servis.getTopRatedMovies(API_KEY,getString(R.string.language),++currentPage);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    int statusCode = response.code();
                    List<Movie> lstResult = response.body().getResults();
//                    //Delay time to show loading footer when debug, remove it when release
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }


                  //  Send the result to Handle
                    Message msg = mHandler.obtainMessage(1, lstResult);
                    mHandler.sendMessage(msg);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.i("MainActivity::onFailure", "Hata : " + t.toString());
                }
            });




        }
    }

    public class ThreadSearchMovie extends Thread {
        @Override
        public void run() {

            ApiInterface servis = ApiClient.getClient().create(ApiInterface.class);


            Call<MovieResponse> call = servis.getSearchMovies(API_KEY,getString(R.string.language),searchMovie.getQuery().toString(),++currentPage);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    int statusCode = response.code();
                        List<Movie> lstResult = response.body().getResults();
                    adapter = new MoviesAdapter( getApplicationContext(),lstResult);
                    lvMovies.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {

                    Log.i("MainActivity::onFailure", "Hata : " + t.toString());
                }
            });




        }
    }
}
