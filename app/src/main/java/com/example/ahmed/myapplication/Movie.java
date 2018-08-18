package com.example.ahmed.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String OriginalTitle;
    private String PosterPath;
    private String Overview;
    private Double VoteAverage;
    private String ReleaseDate;

    /**
     * Constructor for a movie object
     */
    public Movie() {
    }

    /**
     * Sets the original title of the movie
     *
     * @param originalTitle Original title of the movie
     */
    public void setOriginalTitle(String originalTitle) {
        OriginalTitle = originalTitle;
    }

    /**
     * Sets the path to the movie poster
     *
     * @param posterPath Path to the movie poster
     */
    public void setPosterPath(String posterPath) {
        PosterPath = posterPath;
    }

    /**
     * Sets the overview of the movie. If the overview is 'null' it will default to null.
     * @param overview Overview (description) of the movie
     */
    public void setOverview(String overview) {
        if(!overview.equals("null")) {
            Overview = overview;
        }
    }

    /**
     * Sets the vote average of the movie
     *
     * @param voteAverage Vote average of the movie
     */
    public void setVoteAverage(Double voteAverage) {
        VoteAverage = voteAverage;
    }

    /**
     * Sets the release date of the movie.
     *
     * @param releaseDate Release date of the movie. If value is "null" the release date will remain
     *                    null
     */
    public void setReleaseDate(String releaseDate) {
        if(!releaseDate.equals("null")) {
            ReleaseDate = releaseDate;
        }
    }

    /**
     * Gets the original title of the movie
     *
     * @return Original title of the movie
     */
    public String getOriginalTitle() {
        return OriginalTitle;
    }

    /**
     * Returns URL string to where the poster can be loaded
     *
     * @return URL string to where the poster can be loaded
     */
    public String getPosterPath() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + PosterPath;
    }

    /**
     * Gets the TMDb movie description (plot)
     *
     * @return TMDb movie description (plot)
     */
    public String getOverview() {
        return Overview;
    }

    /**
     * Gets the TMDb vote average score
     *
     * @return TMDb vote average score
     */
    private Double getVoteAverage() {
        return VoteAverage;
    }

    /**
     * Gets the release date of the movie
     *
     * @return Release date of the movie
     */
    public String getReleaseDate() {
        return ReleaseDate;
    }

    /**
     * Gets the TMDb way of scoring the movie: <score>/10
     *
     * @return TMDb way of scoring the movie: <score>/10
     */
    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }

    /**
     * Returns the format of the date.
     *
     * @return Format of the date
     */
    public String getDateFormat() {
        return DATE_FORMAT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(OriginalTitle);
        dest.writeString(PosterPath);
        dest.writeString(Overview);
        dest.writeValue(VoteAverage);
        dest.writeString(ReleaseDate);
    }

    private Movie(Parcel in) {
        OriginalTitle = in.readString();
        PosterPath = in.readString();
        Overview = in.readString();
        VoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        ReleaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
