package com.ddoniddoi.lotto.Entity;

public class LottoNumber {

    String drwNoDate; //당첨일자
    int bnusNo; //보너스
    int drwNo; //당첨번호
    int drwtNo1; //1
    int drwtNo2; //2
    int drwtNo3; //3
    int drwtNo4; //4
    int drwtNo5; //5
    int drwtNo6; //6
    String returnValue; //성공여부

    public String getDrwNoDate() {
        return drwNoDate;
    }

    public void setDrwNoDate(String drwNoDate) {
        this.drwNoDate = drwNoDate;
    }

    public int getBnusNo() {
        return bnusNo;
    }

    public void setBnusNo(int bnusNo) {
        this.bnusNo = bnusNo;
    }

    public int getDrwNo() {
        return drwNo;
    }

    public void setDrwNo(int drwNo) {
        this.drwNo = drwNo;
    }

    public int getDrwtNo1() {
        return drwtNo1;
    }

    public void setDrwtNo1(int drwtNo1) {
        this.drwtNo1 = drwtNo1;
    }

    public int getDrwtNo2() {
        return drwtNo2;
    }

    public void setDrwtNo2(int drwtNo2) {
        this.drwtNo2 = drwtNo2;
    }

    public int getDrwtNo3() {
        return drwtNo3;
    }

    public void setDrwtNo3(int drwtNo3) {
        this.drwtNo3 = drwtNo3;
    }

    public int getDrwtNo4() {
        return drwtNo4;
    }

    public void setDrwtNo4(int drwtNo4) {
        this.drwtNo4 = drwtNo4;
    }

    public int getDrwtNo5() {
        return drwtNo5;
    }

    public void setDrwtNo5(int drwtNo5) {
        this.drwtNo5 = drwtNo5;
    }

    public int getDrwtNo6() {
        return drwtNo6;
    }

    public void setDrwtNo6(int drwtNo6) {
        this.drwtNo6 = drwtNo6;
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public String toString() {
        return "LottoNumber{" +
                "drwNoDate='" + drwNoDate + '\'' +
                ", bnusNo='" + bnusNo + '\'' +
                ", drwNo='" + drwNo + '\'' +
                ", drwtNo1='" + drwtNo1 + '\'' +
                ", drwtNo2='" + drwtNo2 + '\'' +
                ", drwtNo3='" + drwtNo3 + '\'' +
                ", drwtNo4='" + drwtNo4 + '\'' +
                ", drwtNo5='" + drwtNo5 + '\'' +
                ", drwtNo6='" + drwtNo6 + '\'' +
                ", returnValue='" + returnValue + '\'' +
                '}';
    }
}
