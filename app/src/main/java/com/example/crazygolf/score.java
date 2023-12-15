package com.example.crazygolf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class score extends AppCompatActivity {

    private static int[] totalScores;  // Static variable to keep track of scores

    private TextView textViewPlayerScores;
    private Button homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Initialize the TextView
        textViewPlayerScores = findViewById(R.id.textViewPlayerScores);
        homepage = findViewById(R.id.home);

        // Receive the player scores array from Golf_Game
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("playerScores")) {
            int[] playerScores = extras.getIntArray("playerScores");

            // Initialize or update the totalScores array
            if (totalScores == null) {
                totalScores = Arrays.copyOf(playerScores, playerScores.length);
            } else {
                for (int i = 0; i < playerScores.length; i++) {
                    totalScores[i] += playerScores[i];
                }
            }

            // Use the player scores as needed in the score activity
            displayPlayerScores(playerScores);
        } else {
            // If there are no player scores, display an error or handle it appropriately
            Toast.makeText(this, "No player scores found", Toast.LENGTH_SHORT).show();
        }

        homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(score.this, "Thank you for Playing", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayPlayerScores(int[] playerScores) {
        // Display the player scores in a TextView or any other way you prefer

        StringBuilder scoresStringBuilder = new StringBuilder();

        boolean isDraw = true;  // Flag to check if all scores are equal

        for (int i = 0; i < playerScores.length; i++) {
            int playerNumber = i + 1;
            int score = playerScores[i];
            scoresStringBuilder.append("Player ").append(playerNumber).append(": ").append(score).append("\n");

            // Check if current score is not equal to the first player's score
            if (score != playerScores[0]) {
                isDraw = false;
            }
        }

        // Check if it's a draw and no player has scored
        if (isDraw && playerScores[0] == 0) {
            scoresStringBuilder.append("Draw ! Aren't you crazed by Crazy Golf?!!!");
        } else {
            // Append the winner information
            int winnerIndex = findWinnerIndex(totalScores);
            scoresStringBuilder.append("\nWinner: Player ").append(winnerIndex + 1);
        }

        textViewPlayerScores.setText(scoresStringBuilder.toString());
    }


    // Method to find the index of the maximum value in an array
    private int findWinnerIndex(int[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
