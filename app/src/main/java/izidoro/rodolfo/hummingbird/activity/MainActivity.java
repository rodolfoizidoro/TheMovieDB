package izidoro.rodolfo.hummingbird.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import izidoro.rodolfo.hummingbird.R;
import izidoro.rodolfo.hummingbird.adapter.MoviesAdapter;
import izidoro.rodolfo.hummingbird.helper.GetUrl;
import izidoro.rodolfo.hummingbird.model.Movie;
import izidoro.rodolfo.hummingbird.rest.ApiClient;

import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private ListView lvMovies;

    private MoviesAdapter adapter;
    public int currentPage = 1;
    private SearchView searchMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMovies = (ListView) findViewById(R.id.lvMovies);
        searchMovie = (SearchView) findViewById(R.id.searchMovie);


        //Inicializa os resultados quando abre o app
        init();

        lvMovies.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //verifico se carregou os 20 primeiros
                //verifico se foi feito uma busca caso sim carrega mais de acordo com a busca
                //caso não carrego as proximas paginas dos mais populares
                if (view.getLastVisiblePosition() == totalItemCount - 1 && lvMovies.getCount() >= 20) {
                    if (!searchMovie.getQuery().toString().isEmpty())
                        getSeachMovie(searchMovie.getQuery().toString());
                    else
                        getMoreData();
                }
            }
        });

        searchMovie.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //começo sempre na pagina 0 as busca e conforme vou rolando a pagina vai incrementando
                currentPage = 0;
                getSeachMovie(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    //quando limpa o texto eu seto a busca padrao de inicio do app
                    init();
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });


    }

        //Busca os mais populares
    public void getMoreData() {
        try {
            List<Movie> lstResult = new ApiClient().execute(GetUrl.getUrlMoviePopular(++currentPage)).get();
            if (lstResult != null) {
                adapter.addListItemToAdapter(lstResult);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //Faz busca pelo nome digitado
    public void getSeachMovie(String query) {
        try {

            query = URLEncoder.encode(query, "UTF-8");
            ++currentPage;
            List<Movie> lstResult = new ApiClient().execute(GetUrl.getUrlSeachMovie(query,currentPage)).get();
            if (lstResult != null) {
                if (currentPage > 1) {
                    adapter.addListItemToAdapter(lstResult);
                } else {
                    adapter = new MoviesAdapter(getApplicationContext(), lstResult);
                    lvMovies.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //iniciliza com os 20 mais populares
    private void init() {
        try {
            List<Movie> lstResult = new ApiClient().execute(GetUrl.getUrlMoviePopular(1)).get();
            if (lstResult != null) {
                adapter = new MoviesAdapter(getApplicationContext(), lstResult);
                lvMovies.setAdapter(adapter);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
