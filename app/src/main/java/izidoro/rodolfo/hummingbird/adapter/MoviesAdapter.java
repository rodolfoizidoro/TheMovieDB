package izidoro.rodolfo.hummingbird.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import izidoro.rodolfo.hummingbird.activity.MainActivity;
import izidoro.rodolfo.hummingbird.R;
import izidoro.rodolfo.hummingbird.helper.Date;
import izidoro.rodolfo.hummingbird.helper.DownloadImage;
import izidoro.rodolfo.hummingbird.helper.GetUrl;
import izidoro.rodolfo.hummingbird.model.Movie;



public class MoviesAdapter extends BaseAdapter {
    private Context mContext;
    private List<Movie> mMovieList;
    private DownloadImage DownloadImage;

    //Constructor

    public MoviesAdapter(Context mContext, List<Movie> mMovieList) {
        this.mContext = mContext;
        this.mMovieList = mMovieList;
    }

    public void addListItemToAdapter(List<Movie> list) {
        //Add list to current array list of data
        mMovieList.addAll(list);
        //Notify UI
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View v = View.inflate(mContext, R.layout.list_item_movie, null);

        Movie movie = mMovieList.get(position);
        String uriPath = GetUrl.IMAGE_BASE_URL + movie.getPosterPath();


        TextView tvTitle = (TextView) v.findViewById(R.id.tv_name);
        TextView tvDescription = (TextView)v.findViewById(R.id.tv_description);
        TextView tvData = (TextView)v.findViewById(R.id.tv_date);
        ImageView ivBack = (ImageView) v.findViewById(R.id.iv_back);
        Picasso.with(mContext).load(uriPath).resize(240,350).into(ivBack);

        //Habilitar para ativar o modo nativo de imagem
//        DownloadImage = new DownloadImage();
//            DownloadImage.downloadfile(uriPath,ivBack);

        tvTitle.setText(movie.getTitle());
        tvDescription.setText(movie.getOverview());
        tvData.setText(Date.formateDateFromstring("yyyy-mm-dd","yyyy",movie.getReleaseDate()));

        return v;
    }


}
