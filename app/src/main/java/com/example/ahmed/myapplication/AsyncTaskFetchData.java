package com.example.ahmed.myapplication;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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


    class AsyncTaskFetchData extends AsyncTask<String, Void, Movie[]> {
        /**
         * For logging purposes
         */
        private final String LOG_TAG = AsyncTaskFetchData.class.getSimpleName();

        /**
         * TMDb API key
         */
        private final String mApiKey;

        /**
         * Interface / listener
         */
        private final CompletedTask mListener;

        /**
         * Constructor
         *0
         * @param listener UI listener
         * @param apiKey TMDb API key
         */
        public AsyncTaskFetchData(CompletedTask listener, String apiKey) {
            super();

            mListener = listener;
            mApiKey = apiKey;
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Holds data returned from the API
            String moviesJsonStr = null;

            try {
                URL url = getApiUrl(params);

                // Start connecting to get JSON
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Adds '\n' at last line if not already there.
                    // This supposedly makes it easier to debug.
                    builder.append(line).append("\n");
                }

                if (builder.length() == 0) {
                    // No data found. Nothing more to do here.
                    return null;
                }

                moviesJsonStr = builder.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                // Tidy up: release url connection and buffered reader
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                // Make sense of the JSON data
                return getMoviesDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Extracts data from the JSON object and returns an Array of movie objects.
         *
         * @param moviesJsonStr JSON string to be traversed
         * @return Array of Movie objects
         * @throws JSONException
         */
        private Movie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
            // JSON tags
            final String TAG_RESULTS = "results";
            final String TAG_ORIGINAL_TITLE = "original_title";
            final String TAG_POSTER_PATH = "poster_path";
            final String TAG_OVERVIEW = "overview";
            final String TAG_VOTE_AVERAGE = "vote_average";
            final String TAG_RELEASE_DATE = "release_date";

            // Get the array containing hte movies found
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);

            // Create array of Movie objects that stores data from the JSON string
            Movie[] movies = new Movie[resultsArray.length()];

            // Traverse through movies one by one and get data
            for (int i = 0; i < resultsArray.length(); i++) {
                // Initialize each object before it can be used
                movies[i] = new Movie();

                // Object contains all tags we're looking for
                JSONObject movieInfo = resultsArray.getJSONObject(i);

                // Store data in movie object
                movies[i].setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
                movies[i].setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
                movies[i].setOverview(movieInfo.getString(TAG_OVERVIEW));
                movies[i].setVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));
                movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
            }

            return movies;
        }

        /**
         * Creates and returns an URL.
         *
         * @param parameters Parameters to be used in the API call
         * @return URL formatted with parameters for the API
         * @throws MalformedURLException
         */
        private URL getApiUrl(String[] parameters) throws MalformedURLException {
            final String TMDB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String SORT_BY_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
.appendPath(parameters[0])
                    .appendQueryParameter(API_KEY_PARAM, mApiKey)
                    .build();

            return new URL(builtUri.toString());
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);

            // Notify UI
            mListener.onFetchMoviesTaskCompleted(movies);
        }
    }


