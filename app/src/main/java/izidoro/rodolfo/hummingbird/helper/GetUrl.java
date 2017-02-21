package izidoro.rodolfo.hummingbird.helper;

import izidoro.rodolfo.hummingbird.BuildConfig;
import izidoro.rodolfo.hummingbird.R;

/**
 * Created by Kethu on 21/02/2017.
 */

public class GetUrl {

    //Classe para os GetsURL, da chamadas do Rest

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    static final String LANGUAGE = "pt-BR";
    static final String BASE_URL = BuildConfig.BASE_URL;
    public static final String IMAGE_BASE_URL = BuildConfig.IMAGE_BASE_URL;

    public static String getUrlMoviePopular(int page){
        String Url = "";

        Url += BASE_URL+"movie/popular?";
        Url += "api_key=" + API_KEY;
        Url += "&language=" + LANGUAGE;
        Url += "&page=" + page;
        return Url;

    }

    public static String getUrlSeachMovie(String q, int page){
        String Url = "";

        Url += BASE_URL + "search/movie?";
        Url += "api_key=" + API_KEY;
        Url += "&language=" + LANGUAGE;
        Url += "&query=" + q;
        Url += "&page=" + page;
        Url += "&include_adult=false";
        return Url;

    }
}
