package edu.temple.androidthreads;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    TextView timerTextView;

    int countdownTimer = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timerTextView.setText(String.valueOf(countdownTimer));

                new TimerAsyncTask().execute(countdownTimer);
            }
        });
    }

    private class TimerAsyncTask extends AsyncTask<Integer, Integer, Void> {

        // The action that should be performed in the worker thread. In this case our intermittent
        // countdown.
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i = params[0]; i >= 0; i--){

                // Invokes onProgressUpdate(...)
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        // An update action that occurs while the background task is still operating
        @Override
        protected void onProgressUpdate(Integer... progress){
            timerTextView.setText(String.valueOf(progress[0]));
        }

        // Executed after the background task has returned
        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
        }
    }
}
