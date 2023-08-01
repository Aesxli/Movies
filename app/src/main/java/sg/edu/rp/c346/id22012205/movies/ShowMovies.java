package sg.edu.rp.c346.id22012205.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class ShowMovies extends AppCompatActivity {

    ListView lvMovies;
    CustomAdapter adapter, aaMovies;
    Button btnBack;
    ArrayList<Movie>  alMovie, filteredMovie;
    Spinner spnFilter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movies);
        this.setTitle("My Movies - Show Movies");

        lvMovies = findViewById(R.id.lv);
        spnFilter = findViewById(R.id.spinner);
        btnBack = findViewById(R.id.buttonback);

        DBHelper dbHelper = new DBHelper(this);
        alMovie = dbHelper.getMovies();
        dbHelper.close();

        adapter = new CustomAdapter(this, R.layout.row, alMovie);
        lvMovies.setAdapter(adapter);

        spnFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRating = parent.getItemAtPosition(position).toString();

                filteredMovie = new ArrayList<>();
                for (int i = 0; i < alMovie.size(); i++) {
                    if (String.valueOf(alMovie.get(i).getRating()).equals(selectedRating)) {
                        filteredMovie.add(alMovie.get(i));
                    }
                }

                // Check if the filteredMovie list is empty
                if (filteredMovie.isEmpty()) {
                    // Clear the ListView by setting an empty adapter
                    lvMovies.setAdapter(null);
                } else {
                    // Show the filtered movies
                    aaMovies = new CustomAdapter(ShowMovies.this, R.layout.row, filteredMovie);
                    lvMovies.setAdapter(aaMovies);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowMovies.this, MainActivity.class);
                startActivity(intent);

            }
        });

        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie selectedMovie;
                if (filteredMovie != null && !filteredMovie.isEmpty()) {
                    selectedMovie = filteredMovie.get(position);
                } else {
                    selectedMovie = alMovie.get(position);
                }
                Intent intent = new Intent(ShowMovies.this, ModifyMovie.class);
                intent.putExtra("selectedMovie", selectedMovie);
                startActivity(intent);
            }
        });

    }
}