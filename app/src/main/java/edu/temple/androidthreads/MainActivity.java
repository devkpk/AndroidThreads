package edu.temple.androidthreads;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView timerTextView;

    int countdownTimer = 10;

    volatile int state; // 0 - Not started, 1 - Running, 2 - Paused

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = (TextView) findViewById(R.id.timerTextView);

        timerTextView.setText(String.valueOf(countdownTimer));

        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (state == 0) {
                    state = 1;
                    // Thread to run background operation
                    Thread t = new Thread() {
                        @Override
                        public void run() {
                            for (int i = countdownTimer; i >= 0; i--) {

                                // The line below throws an exception. You cannot interact with the
                                // UI thread from a worker thread
                                /* timerTextView.setText(String.valueOf(i)); */

                                // Obtain a message from the global message pool
                                Message msg = timerHandler.obtainMessage();

                                // Assign a value to the 'what' variable. Could have also used
                                // overloaded obtainMessage(int what) method
                                msg.what = i;

                                // Send message to the handler
                                timerHandler.sendMessage(msg);

                                try {
                                    // Sleep for 1 second
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // Spinlock to pause timer
                                //noinspection StatementWithEmptyBody
                                while (state == 2);

                                Log.d("Thread timer value", String.valueOf(i));
                            }

                            state = 0;
                        }
                    };

                    // Start the thread
                    t.start();
                } else if (state == 1) {
                    state = 2; // paused
                } else {
                    state = 1;
                }
            }
        });
    }

    // Handler that will received and process messages in the UI thread
    Handler timerHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            // Retrieve the message and update the textview
            timerTextView.setText(String.valueOf(message.what));
            return false;
        }
    });
}
