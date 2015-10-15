package edu.temple.androidthreads;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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

                new TimerAsyncTask().execute();
            }
        });
    }

    private class TimerAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 10; i >= 0; i--){
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress){
            timerTextView.setText(String.valueOf(progress[0]));
        }

        @Override
        protected void onPostExecute(Void result){
            Toast.makeText(MainActivity.this, "Time's up!", Toast.LENGTH_SHORT).show();
        }
    }
}
