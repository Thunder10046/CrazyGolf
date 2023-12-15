package com.example.crazygolf;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class Golf_Game extends AppCompatActivity {

    private ImageView golfBall;

    private int[] playerScores;
    private ImageView compass;
    private ImageView hole;
    private TextView angleTextView;
    private float finalRotation = 0;
    private ObjectAnimator rotationAnimator;

    private int playerScore = 0;
    private int chancesRemaining = 3;

    private boolean isBallMoving = true;

    private float initialBallX;
    private float initialBallY;

    private int currentPlayer = 1;
    private int totalPlayers = 4;
    private int currentPlayerGameCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_golf_game);

        golfBall = findViewById(R.id.imageViewGolfBall);
        compass = findViewById(R.id.imageViewCompass);
        angleTextView = findViewById(R.id.Angle);
        hole = findViewById(R.id.hole);

        golfBall.setVisibility(View.VISIBLE);

        // Get the initial position of the golf ball
        initialBallX = golfBall.getX();
        initialBallY = golfBall.getY();

        Log.d("GolfGameActivity", "Initial Ball X: " + initialBallX);
        Log.d("GolfGameActivity", "Initial Ball Y: " + initialBallY);

        compass.setOnClickListener(v -> {
            stopRotation();
            stopCompassOnAngleClick();
        });

        rotationAnimator = ObjectAnimator.ofFloat(compass, View.ROTATION, 45f, 135f);
        rotationAnimator.setDuration(2500);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        // Initialize playerScores array
        playerScores = new int[totalPlayers];

        startGame(); // Start the first game
    }

    private void startGame() {
        currentPlayerGameCount = 0;
        playerScore = 0;
        chancesRemaining = 3;
        updateScoreTextView();
        startRotation();
    }

    private void switchPlayer() {
        currentPlayerGameCount = 0;

        // Store the player score in the array
        playerScores[currentPlayer - 1] = playerScore;

        if (currentPlayer > totalPlayers) {
            currentPlayer = 1;
            Log.d("GolfGameActivity", "End of Round. Total Score for Player " + currentPlayer + ": " + playerScore);

            if (currentPlayer == totalPlayers) {
                // All players have played, go to the score activity
                Toast.makeText(Golf_Game.this, "Game Over! Going to Score Activity", Toast.LENGTH_SHORT).show();

                // Pass the player scores array to the score activity
                Intent intent = new Intent(getApplicationContext(), score.class);
                intent.putExtra("playerScores", playerScores);
                startActivity(intent);
            }
            // You can perform any end-of-round actions here
        } else {
            Log.d("GolfGameActivity", "End of Game " + currentPlayer + ". Total Score: " + playerScore);
            currentPlayer++;

            if (currentPlayer > totalPlayers) {
                // All players have played, go to the score activity
                Toast.makeText(Golf_Game.this, "Game Over! Bad Luck for Losers !!", Toast.LENGTH_SHORT).show();

                // Pass the player scores array to the score activity
                Intent intent = new Intent(getApplicationContext(), score.class);
                intent.putExtra("playerScores", playerScores);
                startActivity(intent);
            } else {
                // Start a new game for the next player
                startGame();
            }
        }
    }

    private void stopRotation() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            rotationAnimator.cancel();
            finalRotation = compass.getRotation();
        }
    }

    private void stopCompassOnAngleClick() {
        float distance = 1850f;
        float adjustedAngle = finalRotation + 180;
        float deltaX = (float) Math.cos(Math.toRadians(adjustedAngle)) * distance;
        float deltaY = (float) Math.sin(Math.toRadians(adjustedAngle)) * distance;

        moveBallTowardsHole(finalRotation, deltaX, deltaY);
    }

    private void moveBallTowardsHole(float angle, float deltaX, float deltaY) {
        float currentX = golfBall.getX();
        float currentY = golfBall.getY();

        float newX = currentX + deltaX;
        float newY = currentY + deltaY;

        reflectIfOutOfBounds(newX, newY, deltaX, deltaY);

        golfBall.animate()
                .translationXBy(deltaX)
                .translationYBy(deltaY)
                .setDuration(1000)
                .withEndAction(() -> {
                    float holeX = hole.getX();
                    float holeY = hole.getY();

                    double distance = Math.sqrt(Math.pow(holeX - newX, 2) + Math.pow(holeY - newY, 2));

                    if (distance < 130) {
                        isBallMoving = false;
                        playerScore++;
                        updateScoreTextView();
                    }

                    golfBall.setVisibility(View.INVISIBLE);
                    resetBallPosition();

                    chancesRemaining--;

                    if (chancesRemaining > 0) {
                        startRotation();
                    } else {
                        Log.d("GolfGameActivity", "Game over. Player Score: " + playerScore);
                        switchPlayer(); // Switch to the next player after completing three games
                    }

                    Log.d("GolfGameActivity", "Ball reached the hole!");
                })
                .start();
    }


    private void reflectIfOutOfBounds(float newX, float newY, float deltaX, float deltaY) {
        // Adjust the screen coordinates based on your measurements
        // ... (existing code)
    }

    private void updateScoreTextView() {
        runOnUiThread(() -> angleTextView.setText("Player " + currentPlayer + " Score: " + playerScore));
    }

    private void startRotation() {
        rotationAnimator.start();
    }

    private void resetBallPosition() {
        runOnUiThread(() -> {
            golfBall.setX(initialBallX + 615);
            golfBall.setY(initialBallY + 2114);
            Log.d("GolfGameActivity", "Initial Ball X: " + initialBallX);
            Log.d("GolfGameActivity", "Initial Ball Y: " + initialBallY);
            golfBall.setVisibility(View.VISIBLE);  // Show the golf ball after resetting
            golfBall.clearAnimation();  // Clear any ongoing animation
            isBallMoving = true;
        });
    }
}
