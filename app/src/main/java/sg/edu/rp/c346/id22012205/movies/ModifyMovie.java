package sg.edu.rp.c346.id22012205.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ModifyMovie extends AppCompatActivity {
    TextView movieID;
    EditText etMovieTitle, etGenre, etYear;
    Spinner spnRating;
    Button btnUpdate, btnDelete, btnBack;
    Movie data;
    String[] ratings = {"G", "PG", "PG13", "NC16", "M18", "R21"};
    int selectedRatingPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_movie);
        this.setTitle("My Movies - Modify Movie");

        movieID = findViewById(R.id.textViewID);
        etMovieTitle = findViewById(R.id.editTextID);
        etGenre = findViewById(R.id.editTextG2);
        etYear = findViewById(R.id.editTextY2);
        spnRating = findViewById(R.id.spinner2);
        btnUpdate = findViewById(R.id.buttonupdate);
        btnDelete = findViewById(R.id.buttondelete);
        btnBack = findViewById(R.id.buttonc);

        Intent intent = getIntent();
        data = (Movie) intent.getSerializableExtra("selectedMovie");

        movieID.setText(String.valueOf(data.getId())); // Convert movie ID to a string
        etMovieTitle.setText(data.getTitle());
        etGenre.setText(data.getGenre());
        etYear.setText(String.valueOf(data.getYear()));

        // Set up the Spinner with the ratings array
        spnRating.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ratings));

        // Find the position of the current movie's rating in the ratings array
        selectedRatingPosition = findRatingPosition(data.getRating());

        // Set the initial selection for the Spinner
        if (selectedRatingPosition != -1) {
            spnRating.setSelection(selectedRatingPosition);
        }

        // Set the initial image based on the selected rating
        updateRatingImage(selectedRatingPosition);

        // Add a listener to the Spinner to update the image when a different rating is selected
        spnRating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRatingPosition = position;
                updateRatingImage(selectedRatingPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(ModifyMovie.this);
                data.setTitle(etMovieTitle.getText().toString());
                data.setGenre(etGenre.getText().toString());
                data.setYear(Integer.parseInt(etYear.getText().toString()));

                // Get the selected rating from the Spinner
                String selectedRating = spnRating.getSelectedItem().toString();
                data.setRating(selectedRating);

                dbh.updateMovie(data);
                Intent i = new Intent(ModifyMovie.this, ShowMovies.class);
                startActivity(i);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(ModifyMovie.this);
                dbh.deleteMovie(data.getId());
                Intent i = new Intent(ModifyMovie.this, ShowMovies.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Find the position of the rating in the ratings array
    private int findRatingPosition(String rating) {
        for (int i = 0; i < ratings.length; i++) {
            if (ratings[i].equals(rating)) {
                return i;
            }
        }
        return -1; // Rating not found in the array
    }

    // Method to update the image based on the selected rating
    private void updateRatingImage(int position) {
        if (position >= 0 && position < ratings.length) {
            String selectedRating = ratings[position];
            switch (selectedRating) {
                case "G":
                    spnRating.setBackgroundResource(R.drawable.rating_g);
                    break;
                case "PG":
                    spnRating.setBackgroundResource(R.drawable.rating_pg);
                    break;
                case "PG13":
                    spnRating.setBackgroundResource(R.drawable.rating_pg13);
                    break;
                case "NC16":
                    spnRating.setBackgroundResource(R.drawable.rating_nc16);
                    break;
                case "M18":
                    spnRating.setBackgroundResource(R.drawable.rating_m18);
                    break;
                case "R21":
                    spnRating.setBackgroundResource(R.drawable.rating_r21);
                    break;
                default:
                    // Use a default image if the rating is not recognized
                    spnRating.setBackgroundResource(R.drawable.rating_m18);
                    break;
            }
        }
    }
}