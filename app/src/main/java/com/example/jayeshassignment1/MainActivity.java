package com.example.jayeshassignment1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.jayeshassignment1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding; // accessing the layout UI elements using View Binding
    private Handler handler = new Handler(); // Handles timer updates
    private long startTime = 0L; // stores the time when the stopwatch started.
    private long elapsedTime = 0L; // time since pause.
    private boolean isRunning = false; // tracks whether the stopwatch is running.

    // Updates the timer regularly.
    private final Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long timeNow = System.currentTimeMillis() - startTime; // calculate elapsed time.
            binding.timerText.setText(formatTime(timeNow)); // update the timer text.
            handler.postDelayed(this, 10); // schedule the next update.
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // inflate the layout.
        setContentView(binding.getRoot()); // set the root view.

        setupUI(); // initialize UI components.
    }

    private void setupUI() {
        // Set up button click listeners.
        binding.startButton.setOnClickListener(v -> startStopwatch());
        binding.stopButton.setOnClickListener(v -> stopStopwatch());
        binding.resetButton.setOnClickListener(v -> resetStopwatch());

        updateButtonState(); // Set initial button states.
    }

    private void startStopwatch() {
        // start the stopwatch if it's not already running.
        if (!isRunning) {
            startTime = System.currentTimeMillis() - elapsedTime; // Adjust start time after pausing.
            handler.post(updateTimer);
            isRunning = true;
            updateButtonState(); // Update button UI.
        }
    }

    private void stopStopwatch() {
        // stop the stopwatch if it's running.
        if (isRunning) {
            handler.removeCallbacks(updateTimer);
            elapsedTime = System.currentTimeMillis() - startTime;
            isRunning = false;
            updateButtonState();
        }
    }

    private void resetStopwatch() {
        // Reset the stopwatch to zero.
        handler.removeCallbacks(updateTimer);
        elapsedTime = 0L;
        startTime = 0L;
        binding.timerText.setText(formatTime(0));
        isRunning = false;
        updateButtonState();
    }
    // Update button states based on stopwatch state.
    private void updateButtonState() {

        int disabledColor = ContextCompat.getColor(this, R.color.disabled_button);
        int enabledColorPrimary = ContextCompat.getColor(this, R.color.primary);
        int enabledColorSecondary = ContextCompat.getColor(this, R.color.secondary);

        if (isRunning) {
            binding.startButton.setEnabled(false);
            binding.startButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled_button));
            binding.stopButton.setEnabled(true);
            binding.stopButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
            binding.resetButton.setEnabled(false);
            binding.resetButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled_button));
        } else if (elapsedTime > 0) {
            binding.startButton.setEnabled(true);
            binding.startButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
            binding.stopButton.setEnabled(false);
            binding.stopButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled_button));
            binding.resetButton.setEnabled(true);
            binding.resetButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.secondary));
        } else {
            binding.startButton.setEnabled(true);
            binding.startButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.primary));
            binding.stopButton.setEnabled(false);
            binding.stopButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled_button));
            binding.resetButton.setEnabled(false);
            binding.resetButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.disabled_button));
        }
    }

    private String formatTime(long millis) {
        // format milliseconds into minutes, seconds, and milliseconds.
        int minutes = (int) (millis / 60000);
        int seconds = (int) (millis % 60000 / 1000);
        int milliseconds = (int) (millis % 1000 / 10);
        return String.format("%02d:%02d.%02d", minutes, seconds, milliseconds);
    }
}