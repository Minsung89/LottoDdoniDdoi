package com.ddoniddoi.lotto.Retrofit;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.ddoniddoi.lotto.Database.LottoNumberHelper;
import com.ddoniddoi.lotto.Entity.LottoNumber;
import com.ddoniddoi.lotto.Service.LottoNumberService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LottoRetrofit {

    Activity activity;
    String URL = "http://www.nlotto.co.kr/";
    LottoNumberHelper lnh;
    //SQLite(DB 이름)
    String DATABASE_NAME = "LOTTO";
    Boolean isNumber = true;

    LottoNumber lottoNumber;

    public LottoRetrofit(Activity activity){
        this.activity = activity;
        lnh = new LottoNumberHelper(activity.getApplicationContext(),DATABASE_NAME);

    }

    private void lottoNumberInsert(int number){

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            LottoNumberService lottoNumberService = retrofit.create(LottoNumberService.class);
            final Call<LottoNumber> call = lottoNumberService.getLottoNumber("getLottoNumber", number);
            //동기
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        lottoNumber = call.execute().body();
                        if(!lottoNumber.getReturnValue().equals("fail")) {
                            Log.i(lottoNumber.getDrwNo() + "회차번호", lottoNumber.toString());
                            isNumber = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        isNumber = false;

                    }
                }
            });
            thread.start();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"인터넷 연결이 필요합니다.",Toast.LENGTH_LONG).show();
            isNumber = false;
        }

    }

    //로또 번호 가져오기
    public LottoNumber lottoNumbers() {

        int newNumber = getNextEpisodeBasedonDate();
        while (isNumber) {
            lottoNumberInsert(newNumber);
            try {
                Thread.sleep(500);
                newNumber--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return lottoNumber;
    }

    //로또 번호 가져오기
    public int getNextEpisodeBasedonDate() {
        String startDate = "2002-12-07 23:59:59";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date cDate = new Date();
        Date sDate = null;
        try {
            sDate = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = cDate.getTime() - sDate.getTime();

        long nextEpi = (diff / (86400 * 1000 * 7))+2;
        return (int) nextEpi;
    }



}
