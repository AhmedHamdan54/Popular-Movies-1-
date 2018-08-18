package com.example.ahmed.myapplication;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;

public class MoviesDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = MoviesDetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView tv_OriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ImageView iv_Poster = (ImageView) findViewById(R.id.imageview_poster);
        TextView tv_OverView = (TextView) findViewById(R.id.textview_overview);
        TextView tv_VoteAverage = (TextView) findViewById(R.id.textview_vote_average);
        TextView tv_ReleaseDate = (TextView) findViewById(R.id.textview_release_date);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.parcel_movie));

        tv_OriginalTitle.setText(movie.getOriginalTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(iv_Poster);

        String overView = movie.getOverview();
        if (overView == null) {
            tv_OverView.setTypeface(null, Typeface.ITALIC);
            overView = getResources().getString(R.string.no_summary_found);
        }
        tv_OverView.setText(overView);
        tv_VoteAverage.setText(movie.getDetailedVoteAverage());

        // First get the release date from the object - to be used if something goes wrong with
        // getting localized release date (catch).
        String releaseDate = movie.getReleaseDate();
        if(releaseDate != null) {
            try {
                releaseDate =DataTime.getLocalizedDate(this,
                        releaseDate, movie.getDateFormat());
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error with parsing movie release date", e);
            }
        } else {
            tv_ReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.no_release_date_found);
        }
        tv_ReleaseDate.setText(releaseDate);
    }
}
