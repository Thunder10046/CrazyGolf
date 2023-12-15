package com.example.crazygolf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button startGameButton;

    private Button prev; // Give the Button a name

    public EditText player1;
    public EditText player2;
    public EditText player3;
    public EditText player4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGameButton = findViewById(R.id.buttonNewGame);
        prev = findViewById(R.id.buttonPreviousGames);
        player1 = findViewById(R.id.editTextPlayer1);
        player2 = findViewById(R.id.editTextPlayer2);
        player3 = findViewById(R.id.editTextPlayer3);
        player4 = findViewById(R.id.editTextPlayer4);


        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Game is Starting !!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Golf_Game.class);
                startActivity(intent);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You want to see previous score !!!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), score.class);
                startActivity(intent);
            }
        });
    }


}
