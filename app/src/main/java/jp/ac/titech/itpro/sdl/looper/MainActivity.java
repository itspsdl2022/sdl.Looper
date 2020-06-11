package jp.ac.titech.itpro.sdl.looper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickTest1(View v) {
        looperTest1();
    }

    public void onClickTest2(View v) {
        looperTest2();
    }

    private void looperTest1() {
        Log.d(TAG, "looperTest1: main task in thread=" + Thread.currentThread());
        HandlerThread th = new HandlerThread("HandlerThread1");
        th.start();
        Handler handler = new Handler(th.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "looperTest1: posted task in thread=" + Thread.currentThread());
            }
        });
    }

    private void looperTest2() {
        Log.d(TAG, "looperTest2: main task in thread=" + Thread.currentThread());
        MyHandlerThread th = new MyHandlerThread("HandlerThread2");
        th.start();
        Handler handler = new Handler(th.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "looperTest2: posted task in thread=" + Thread.currentThread());
            }
        });
    }

    private static class MyHandlerThread extends Thread {
        private Looper looper;

        MyHandlerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Looper.prepare();
            synchronized (this) {
                looper = Looper.myLooper();
                notifyAll();
            }
            Looper.loop();
        }

        Looper getLooper() {
            if (!isAlive()) {
                return null;
            }
            synchronized (this) {
                while (isAlive() && looper == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return looper;
        }
    }

}
