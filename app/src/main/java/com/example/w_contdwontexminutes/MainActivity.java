package com.example.w_contdwontexminutes;
//開發中經常會遇到一些和倒計時有關的場景，比如傳送驗證碼的按鈕，會在點選傳送後，顯示倒計時間，倒計時結束後才能夠重新整理按鈕，再次允許點選。
//為了不阻塞軟體的執行，又要實時重新整理介面，我們通常會用到 Handler 或者 AsyncTask 等技術，自己寫邏輯實現。
//其實 Android 中已經封裝好了一套 CountDownTimer 來實現這個功能需求。
//CountDownTimer(long millisInFuture, long countDownInterval)
//CountDownTimer的兩個引數分別表示倒計時的總時間 millisInFuture 和間隔時間 countDownInterval。

//CountDownTimer(long millisInFuture, long countDownInterval)://倒數計時器(1.預備倒數的時間,2.倒數時間的間隔)
//CountDownTimer.start()://開始啟動倒數計時器
//CountDownTimer.cancel()://取消解除計時器
//CountDownTimer.onFinish()://關掉掉此Activity畫面
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static com.example.w_contdwontexminutes.R.id.btn_reset;

public class MainActivity extends AppCompatActivity {
    private static final long START_TINE_IN_MILLIS = 600000; //一開始時間

    private TextView txtViewCountDown; //顯示時間的txt
    private Button btnStartPause, btnReset;


    private CountDownTimer countDownTimer; //倒數計時器物件

    private boolean isTimeRunning; //是否正在倒數

    private long timeLeftInMillis = START_TINE_IN_MILLIS; //剩餘時間

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewCountDown = findViewById(R.id.text_view_countdown);
        btnStartPause = findViewById(R.id.button_start_pause);
        btnReset = findViewById(R.id.btn_reset);

        //1.Start按鈕設定
        btnStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果正在跑的話,設定暫停計時器方法
                if (isTimeRunning) {
                    pauseTimer(); //暫停按鈕
                } else {//如果沒有正在跑,設定開始計時器方法
                    statrTimer();
                }
            }
        });


        //2.Reset按鈕設定
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    //時間重新計算
    private void resetTimer() {
        timeLeftInMillis = START_TINE_IN_MILLIS; //將一開始設定時間600000更新
        updateCountDownText();
        btnReset.setVisibility(View.INVISIBLE); //按下Rest後設定為不可見


    }

    //按下暫停鍵停止時間,是否正在跑flase.按鈕顯示為Start,多一個Reset按鈕讓你重製時間
    private void pauseTimer() {
        countDownTimer.cancel();
        isTimeRunning = false;
        btnStartPause.setText("Start");
        btnReset.setVisibility(View.VISIBLE);
    }


    //1.開始倒數功能
    private void statrTimer() {
        countDownTimer = new CountDownTimer(
                timeLeftInMillis, //1.預備倒數的時間
                1000 //2.倒數時間的間隔
        ) {
            //onTick:當時間滴答到數時, millisUntilFinished:倒數的時間
            @Override
            public void onTick(long millisUntilFinished) { //倒數的時間
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                Log.v("hank", "statrTimer():" + "onTick: => millisUntilFinished: " + millisUntilFinished);// 599991
            }

            //當倒數時間歸0時
            @Override
            public void onFinish() {
                isTimeRunning = false;
                btnStartPause.setText("Start");
                btnStartPause.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.VISIBLE);

                Log.v("hank", "statrTimer():" + "onFinish:");
            }
        }.start();


        isTimeRunning = true;  //正在執行改成true
        btnStartPause.setText("pause");
        btnReset.setVisibility(View.INVISIBLE);//設定為不可見

    }

    //更新計時器時間
    private void updateCountDownText() {
        //取得分鐘跟秒
        int munutes = (int) (timeLeftInMillis / 1000) / 60;  //600000 /1000 /60 拿到分鐘
        int seconds = (int) (timeLeftInMillis / 1000) % 60;  //取得秒數

        //將分秒格式化為十至近位置  Ex:int year=5; => 結果為05
        /**
         ％是格式化輸入接受參數的標記
         0格式化命令：結果將用零來填充
         6：填充數字
         d：代表十二進制數據数据
         **/

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", munutes, seconds);
        txtViewCountDown.setText(timeLeftFormatted);
        Log.v("hank", "updateCountDownText()=> munutes: " + munutes + "/seconds:" + seconds);
    }

}
