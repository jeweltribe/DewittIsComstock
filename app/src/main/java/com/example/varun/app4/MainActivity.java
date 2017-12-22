package com.example.varun.app4;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TabHost tabHost;
    private final DBHelper db = new DBHelper(this, "movies.db",1);
    private List<Movies> movieListByDate = new ArrayList<>();
    private List<Movies> movieListByRating = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private MovieAdapter movieAdapter2;
    private ListView listByDate;
    private ListView listByRating;
    private static final int MOVIE_REQUEST = 1;
    private Movies originalMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        movieListByDate = db.getMovie(); // read data from database
        movieListByRating = db.getMovie();

        // sort the lists
        List<Movies> temp1 = movieListByDate;
        List<Movies> temp2 = movieListByRating;

        movieListByDate = sortByDate(temp1);
        movieListByRating = sortByRating(temp2);


        listByDate = (ListView) findViewById(R.id.movieList);
        movieAdapter = new MovieAdapter(this, movieListByDate);
        listByDate.setAdapter(movieAdapter);

        listByRating = (ListView) findViewById(R.id.ratingList);
        movieAdapter2 = new MovieAdapter(this, movieListByRating);
        listByRating.setAdapter(movieAdapter2);


        // Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Most Recent");
        tabHost.addTab(spec);

        // Tab 2
        spec = tabHost.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Highest Rated");
        tabHost.addTab(spec);


        listByDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies temp = movieListByDate.get(i);
                originalMovie = temp;
                Intent intent = new Intent(view.getContext(), AddEditActivity.class);
                intent.putExtra("Action", "Edit");
                intent.putExtra("Title", temp.getMovieName());
                intent.putExtra("Date", temp.getMovieDate());
                intent.putExtra("Rating", temp.getMovieRating());
                startActivityForResult(intent, MOVIE_REQUEST);
            }
        });

        listByRating.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movies temp = movieListByRating.get(i);
                originalMovie = temp;
                Intent intent = new Intent(view.getContext(), AddEditActivity.class);
                intent.putExtra("Action", "Edit");
                intent.putExtra("Title", temp.getMovieName());
                intent.putExtra("Date", temp.getMovieDate());
                intent.putExtra("Rating", temp.getMovieRating());
                startActivityForResult(intent, MOVIE_REQUEST);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        menu.findItem(R.id.cancel).setVisible(false);
        menu.findItem(R.id.saveMovie).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMovie:
                Intent i = new Intent(this, AddEditActivity.class);
                startActivityForResult(i, MOVIE_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // AsyncTask for DB storing, throwing exception
    private class DBTasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.storeMovie(movieListByDate);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MOVIE_REQUEST && resultCode == RESULT_OK) {
            String title = "", date = "";
            float rating = 0;
            if (data.hasExtra("Title")) {
                title = data.getExtras().getString("Title");
            }
            if (data.hasExtra("Date")) {
                date = data.getExtras().getString("Date");
            }
            if (data.hasExtra("Rating")) {
                rating = data.getExtras().getFloat("Rating");
            }


            // remove the duplicate
            if (originalMovie != null) {
                int index = movieListByDate.indexOf(originalMovie);
                int index2 = movieListByDate.indexOf(originalMovie);
                movieListByDate.remove(index);
                movieListByRating.remove(index2);
                movieListByDate.set(index, new Movies(0, title, date, rating));
                movieListByRating.set(index2, new Movies(0, title, date, rating));
            } else {
                movieListByDate.add(new Movies(0, title, date, rating));
                movieListByRating.add(new Movies(0, title, date, rating));
            }
            originalMovie = null;
            movieListByDate = sortByDate(movieListByDate);
            movieListByRating = sortByRating(movieListByRating);
            //new DBTasks().execute();
            db.storeMovie(movieListByDate);
            movieAdapter.notifyDataSetChanged();
            movieAdapter2.notifyDataSetChanged();
        } else {
            originalMovie = null;
        }

    }
    // uses selection sort algorithm
    public List<Movies> sortByDate(List<Movies> lst) {
        int maxIndex;

        for (int i = 0; i < lst.size() - 1; i++) {
            maxIndex = i;
            for (int j = i + 1; j < lst.size(); j++) {
                String date[] = lst.get(i).getMovieDate().split("/");
                String temp[] = lst.get(j).getMovieDate().split("/");
                if (Integer.valueOf(date[0]) <= Integer.valueOf(temp[0]) && Integer.valueOf(date[1]) <= Integer.valueOf(temp[1]) && Integer.valueOf(date[2]) <= Integer.valueOf(temp[2])) {
                    maxIndex = j;
                }
            }
            Movies tempObject = lst.get(maxIndex);
            lst.set(maxIndex, lst.get(i));
            lst.set(i, tempObject);
        }
        return lst;
    }

    public List<Movies> sortByRating(List<Movies> lst) {
        int maxIndex;

        for (int i = 0; i < lst.size() - 1; i++) {
            maxIndex = i;
            for (int j = i + 1; j < lst.size(); j++) {
                if (lst.get(maxIndex).getMovieRating() <= lst.get(j).getMovieRating()) {
                    maxIndex = j;
                }
            }
            Movies tempObject = lst.get(maxIndex);
            lst.set(maxIndex, lst.get(i));
            lst.set(i, tempObject);
        }
        return lst;
    }
}
