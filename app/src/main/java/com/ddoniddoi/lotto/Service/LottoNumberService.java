package com.ddoniddoi.lotto.Service;


import com.ddoniddoi.lotto.Entity.LottoNumber;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LottoNumberService {
//
    @GET("common.do")
    Call<LottoNumber> getLottoNumber(@Query("method") String method, @Query("drwNo") int drwNo);



//
}
