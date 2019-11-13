package com.ddoniddoi.lotto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ddoniddoi.lotto.Dialog.LottoSelectDialog;
import com.ddoniddoi.lotto.Entity.LottoNumber;
import com.ddoniddoi.lotto.Listener.LottoSelectDialogListener;
import com.ddoniddoi.lotto.Retrofit.LottoRetrofit;
import com.ddoniddoi.lotto.Util.SrcollViewNoTouch;
import com.ddoniddoi.lotto.Database.Database;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    SensorManager sensorManager;
    Sensor accelerometer;
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500; //Shake(흔들림)가 되고 0.5가 지난 경우는 무시
    private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F; //중력가속도

    TextView[][] count = new TextView[5][6]; //랜덤으로 돌리는 숫자
    SrcollViewNoTouch[][] svnt = new SrcollViewNoTouch[5][6]; //랜덤으로 돌리는 숫자 스크롤 제거
    LinearLayout[] randomLayout = new LinearLayout[5];

    LinearLayout shakeImgLayout; //shake 이미지
    TextView selectNumbersBtn;
    Animation bottomRandom;
    ImageView qrCode; //QR 코드 버튼
    TextView times,timesDate, newNumber1, newNumber2, newNumber3, newNumber4, newNumber5, newNumber6, bounsNumber;
    RadioGroup rg;

    LottoSelectDialog lottoSelectDialog; //번호 선택 다이얼로그
    List<Integer> selectNumbers; //선택한 번호들 등록

    //DB
    Database db;
    //통신
    LottoRetrofit lottoRetrofit;
    //최신 로또 번호
    LottoNumber lottoNumber;
    //랜덤 돌릴 숫자
    int selectRandomNumber;

    //qr code scanner object
    IntentIntegrator qrScan;

    Boolean shakeFlag = true; //흔들었을 시 번호가 나와야만 다시 돌릴 수 있음
    String numbers = ""; //기본 숫자

    //흔들기 3번이면 광고
    int shakeNum = 1;
    //광고 연령
    AdRequest adRequest;
    //전면 광고
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //db 접속 & 테이블 생성
        db = new Database(MainActivity.this);
        db.connectDatabase();
        db.createTable();

        lottoRetrofit = new LottoRetrofit(this);
        lottoNumber = lottoRetrofit.lottoNumbers();

        selectRandomNumber = 1;
        selectNumbers = new ArrayList<>();
        shakeImgLayout = findViewById(R.id.shakeImgLayout);

        //전면광고
        MobileAds.initialize(this, getString(R.string.admob_id));
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));

        //배너 광고
        AdView mAdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G"); // 앱이 3세 이상 사용가능이라면 광고레벨을 설정해줘야 한다
        adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();
        mAdView.loadAd(adRequest);

        //라디오 버튼
        rg = findViewById(R.id.selectRandomNumberRg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                shakeImgLayout.setVisibility(View.INVISIBLE);
                for (TextView[] items : count) {
                    for (TextView item : items) {
                        item.setText(numbers);
                    }
                }
                for (SrcollViewNoTouch[] items : svnt) {
                    for (SrcollViewNoTouch item : items) {
                        GradientDrawable gd = (GradientDrawable)item.getBackground();
                        gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorPink));
                    }
                }
                RadioButton rb = findViewById(checkedId);
                selectRandomNumber = Integer.valueOf(rb.getText().toString());
                Log.i("선택한 숫자는?",selectRandomNumber+"");
                for (int i = 0 ; i < selectRandomNumber ; i++)
                    randomLayout[i].setVisibility(View.VISIBLE);
                for (int j = selectRandomNumber; j < randomLayout.length ; j++)
                    randomLayout[j].setVisibility(View.INVISIBLE);
            }
        });

        newNumber1 = findViewById(R.id.newNumber1);
        newNumber2 = findViewById(R.id.newNumber2);
        newNumber3 = findViewById(R.id.newNumber3);
        newNumber4 = findViewById(R.id.newNumber4);
        newNumber5 = findViewById(R.id.newNumber5);
        newNumber6 = findViewById(R.id.newNumber6);
        bounsNumber = findViewById(R.id.bonusNumber);
        times = findViewById(R.id.times);
        timesDate = findViewById(R.id.timesDate);
        if(lottoNumber != null) {
            times.setText(lottoNumber.getDrwNo() + "회차");
            timesDate.setText( "(" + lottoNumber.getDrwNoDate() + ")");
            newNumber1.setText(String.valueOf(lottoNumber.getDrwtNo1()));
            newNumber2.setText(String.valueOf(lottoNumber.getDrwtNo2()));
            newNumber3.setText(String.valueOf(lottoNumber.getDrwtNo3()));
            newNumber4.setText(String.valueOf(lottoNumber.getDrwtNo4()));
            newNumber5.setText(String.valueOf(lottoNumber.getDrwtNo5()));
            newNumber6.setText(String.valueOf(lottoNumber.getDrwtNo6()));
            bounsNumber.setText(String.valueOf(lottoNumber.getBnusNo()));

            numberBackgroudColor(newNumber1.getText().toString(), newNumber1);
            numberBackgroudColor(newNumber2.getText().toString(), newNumber2);
            numberBackgroudColor(newNumber3.getText().toString(), newNumber3);
            numberBackgroudColor(newNumber4.getText().toString(), newNumber4);
            numberBackgroudColor(newNumber5.getText().toString(), newNumber5);
            numberBackgroudColor(newNumber6.getText().toString(), newNumber6);
            numberBackgroudColor(bounsNumber.getText().toString(), bounsNumber);

        }
        //QR 코드 버튼
        qrCode = findViewById(R.id.qrCode);
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("QR코드를 사각형안에 비춰주세요");
                qrScan.initiateScan();
            }
        });
        //랜덤으로 돌릴 번호들 등록
        Log.i("count",""+count[0].length);
        for (int i = 0 ; i < count.length ; i ++){
            randomLayout[i] = findViewById(getResources().getIdentifier("random" + (i+1), "id", getPackageName()));
            for (int j = 0 ; j < count[i].length ; j++){
                count[i][j] = findViewById(getResources().getIdentifier("count" + (i+1)+(j+1), "id", getPackageName()));
                svnt[i][j] = findViewById(getResources().getIdentifier("sv" + (i+1)+(j+1), "id", getPackageName()));
            }
        }

        selectNumbersBtn = findViewById(R.id.select_numbers_btn);
        selectNumbersBtn.setOnClickListener(this);

        numbers = count[0][0].getText().toString();

        //번호 돌리는 애니메이션
        bottomRandom = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bottom_anim);
    }

    //QR코드 스캔 시 출력
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if(result.getContents() == null){//qrcode가 없으면
                Toast.makeText(getApplicationContext(),"취소",Toast.LENGTH_SHORT).show();
            }else { //qrcode가 결과가 있으면
                Log.i("데이터는?", result.getContents());
                String resultData = result.getContents();
                if(resultData.contains("lott") || resultData.contains("645lotto") || resultData.contains("nlotto")) {
                    Toast.makeText(getApplicationContext(), "스캔완료", Toast.LENGTH_SHORT).show();
//                    resultData.setText(result.getContents());
                    //인터넷으로 열기
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getContents()));
                    Intent intent = new Intent(this, LottoLuckActivity.class);
                    intent.putExtra("URL", result.getContents());
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "유효한 QR코드가 아닙니다.", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //화면이 보여지고 사용자가 사용자가 볼 수 있는 화면 호출
    @Override
    protected void onResume() {
        super.onResume();
        //흔들림 리스너 등록
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    //다른 엑티비티 호출 전에 실행 됨
    @Override
    protected void onPause() {
        super.onPause();
        //흔들림 리스너 해제
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX * gravityY * gravityY + gravityZ + gravityZ;
            double squaredD = Math.sqrt(f.doubleValue());
            float gForce = (float) squaredD;
            if(gForce > SHAKE_THRESHOLD_GRAVITY) {
                long currentTime = System.currentTimeMillis();
                if(mShakeTime + SHAKE_SKIP_TIME > currentTime || !shakeFlag){
                    return;
                }
                shakeFlag = false;

                mShakeTime = currentTime;
                //라디오버튼에 선택된 숫자만큼 돌리기
                shakeImgLayout.setVisibility(View.INVISIBLE); //흔들었을 시 이미지 삭제

                for (int i = 0 ; i < selectRandomNumber ; i ++){
                    for (TextView item : count[i]){
                        item.setText(numbers);
                        item.startAnimation(bottomRandom);
                    }

                }
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        //여기에 딜레이 후 시작할 작업들을 입력
                        List<List<Integer>> randomNumber = new ArrayList<>();  //중복숫자처리
                        int i = 0;
                        if(selectNumbers != null && selectNumbers.size() != 0) { //선택한 숫자 고정 넣기
                            for (int k = 0 ; k < selectRandomNumber ; k++){
                                List<Integer> radomNum = new ArrayList<>();
                                for (int j = 0 ; j < selectNumbers.size() ; j++){
                                    radomNum.add(selectNumbers.get(j));
                                    count[k][j].setText(String.valueOf(selectNumbers.get(j)));
                                }
                                randomNumber.add(radomNum); //선택한 숫자들 넣기
                            }

                        }
                        for (int k = 0 ; k < selectRandomNumber ; k++) {
                            i = selectNumbers.size();
                            List<Integer> randomNum = new ArrayList<>();
                            if(randomNumber.size() > 0)
                                randomNum = randomNumber.get(k);
                            while (i < 6) {
                                int item = (int) (Math.random() * 45) + 1;
                                if (!randomNum.contains(item)) {
                                    randomNum.add(item);
                                    count[k][i].setText(String.valueOf(item));
                                    i++;
                                }
                            }
                        }
                        //작은 숫자로 나열
                        for (int t = 0 ;t < selectRandomNumber ; t++) {
                            for (int j = 0; j < count[t].length; j++) {
                                for (int k = j + 1; k < count[t].length; k++) {
                                    int first = Integer.valueOf(count[t][j].getText().toString());
                                    int second = Integer.valueOf(count[t][k].getText().toString());
                                    if (first > second) {
                                        int change = first;
                                        count[t][j].setText(String.valueOf(second));
                                        count[t][k].setText(String.valueOf(change));
                                    }
                                }
                                numberBackgroudColor(count[t][j].getText().toString(), svnt[t][j]);
                            }
                        }
                        shakeFlag = true;

                        if(shakeNum == 3){
                            shakeNum = 1;
                            mInterstitialAd.loadAd(new AdRequest.Builder()
                                    .build());
                            mInterstitialAd.setAdListener(new AdListener() {
                                public void onAdLoaded(){
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    } else {
                                        Log.d("asd", "The interstitial wasn't loaded yet.");
                                    }
                                }
                            });
                            shakeFlag = true;
                            return;
                        }
                        shakeNum ++;
                        Log.i("shakeNum",shakeNum + "");
//                        for (TextView item : count){
//                            item.setText(String.valueOf((int) (Math.random() * 45) + 1));
//                        }
                    }
                }, 2000);// 2초 정도 딜레이를 준 후 시작
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //번호 색깔
    private void numberBackgroudColor(String number, View view){
        int scBackground = Integer.valueOf(number);
        GradientDrawable gd = (GradientDrawable)view.getBackground();
        //번호에 맞는 색
        if(scBackground < 11){
            gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorSingle));
        }else if(scBackground < 21){
            gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorTen));
        }else if(scBackground < 31){
            gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorTwenty));
        }else if(scBackground < 41){
            gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorThirty));
        }else{
            gd.setColor(ContextCompat.getColor(getApplication(),R.color.colorForty));
        }
    }

    //추가한 번호 보여주기
    private void textViewCreate(List<Integer> selectNumbers){
        LinearLayout selectNumbersLayout = findViewById(R.id.select_numbers_layout);
        selectNumbersLayout.removeAllViews();
        if(selectNumbers != null && selectNumbers.size() > 0) {
            for (Integer number : selectNumbers) {
                TextView tv = new TextView(this);
                LinearLayout.LayoutParams tvLayoutParams = new LinearLayout.LayoutParams((int)getResources().getDimension(R.dimen.addTextSize), (int)getResources().getDimension(R.dimen.addTextSize));
                tvLayoutParams.leftMargin = 10;
                tv.setLayoutParams(tvLayoutParams);
                tv.setBackground(ContextCompat.getDrawable(getApplication(), R.drawable.round_background));
                tv.setTextColor(ContextCompat.getColor(getApplication(), R.color.colorWhite));
                numberBackgroudColor(String.valueOf(number),tv);
                tv.setGravity(Gravity.CENTER);

                tv.setText(String.valueOf(number));
                selectNumbersLayout.addView(tv);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_numbers_btn :
                lottoSelectDialog = new LottoSelectDialog(this);
                lottoSelectDialog.setLottoSelectDialogListener(new LottoSelectDialogListener() {
                    @Override
                    public void onSelectNumbers(List<Integer> numbers) {
                        textViewCreate(numbers);
                        selectNumbers = numbers;
                    }
                });
                lottoSelectDialog.show();
        }
    }
}
