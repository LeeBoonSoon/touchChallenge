package my.edu.utar.touchchallenge;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextView[] views;
    private int currentViewIndex;
    private long startTime;
    private long endTime;
    private int currentLevel = 1;
    private int score = 0;
    private int delayTime= 500;//initial delay time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TextView array with the Views
        views = new TextView[] {
                findViewById(R.id.view1),
                findViewById(R.id.view2),
                findViewById(R.id.view3),
                findViewById(R.id.view4)
        };

        // Set onTouchListener for each View
        for (TextView view : views) {
            view.setOnTouchListener(this);
        }

        // Start the game
        startGame();
    }

    // Start the game with the first level
    private void startGame() {
        setRandomView();
        startTime = System.currentTimeMillis();
    }

    // Set a random View to highlight
    private void setRandomView() {
        // Reset all Views to default state
        for (TextView view : views) {
            view.setBackgroundResource(R.drawable.default_view);
        }

        // Choose a random View index to highlight
        Random random = new Random();
        currentViewIndex = random.nextInt(views.length);
        views[currentViewIndex].setBackgroundResource(R.drawable.highlighted_view);

        // Update the levelTextView with the current level
        TextView levelTextView = findViewById(R.id.levelTextView);
        levelTextView.setText("Level: " + currentLevel);
    }

    // Handle onTouch events for Views
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // Check if the touched View is the current highlighted View
        if (view == views[currentViewIndex]) {
            // Calculate the score based on the time taken to touch the View
            endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            score += 500 - timeTaken;
            if (score < 0) {
                score = 0;
            }

            // Proceed to the next level or end the game if all levels completed
            if (currentLevel < 5) {//original level is 5
                currentLevel++;
                delayTime -=50;// decrease delay time by 50 milliseconds for each level completed
                setRandomView();
                startTime = System.currentTimeMillis();
            } else {
                endGame();
            }
        } else {
            // Penalize the player for touching the wrong View
            score -= 100;
            if (score < 0) {
                score = 0;
            }
        }

        // Update the score on the screen
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Score: " + score);

        // Add a delay before highlighting the next View
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRandomView();
                startTime = System.currentTimeMillis();
            }
        }, delayTime);//speed original is set 500

        return false;
    }

    // End the game and display the final score
    private void endGame() {
        // Reset the level and score
        currentLevel = 1;
        score = 0;

        // Show the final score on the screen
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Final Score: " + score);
    }
}
