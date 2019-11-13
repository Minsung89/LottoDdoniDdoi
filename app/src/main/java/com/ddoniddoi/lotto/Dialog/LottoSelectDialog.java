package com.ddoniddoi.lotto.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ddoniddoi.lotto.Listener.LottoSelectDialogListener;
import com.ddoniddoi.lotto.R;

import java.util.ArrayList;
import java.util.List;

public class LottoSelectDialog extends Dialog {

    Button selectNumOkBtn, selectNumCancelBtn; //확인, 취소
    List<CheckBox> checkBoxes; // 45개 체크 박스

    LottoSelectDialogListener listener;  //전달할 데이터
    List<Integer> numbers; //체크한 번호들


    public LottoSelectDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT)); //다이얼로그 백그라운드 제거

        setContentView(R.layout.lotto_select_dialog);

        numbers = new ArrayList<>();

        checkBoxes = new ArrayList<>();
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox1));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox2));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox3));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox4));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox5));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox6));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox7));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox8));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox9));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox10));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox11));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox12));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox13));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox14));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox15));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox16));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox17));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox18));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox19));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox20));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox21));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox22));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox23));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox24));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox25));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox26));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox27));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox28));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox29));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox30));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox31));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox32));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox33));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox34));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox35));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox36));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox37));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox38));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox39));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox40));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox41));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox42));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox43));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox44));
        checkBoxes.add((CheckBox) findViewById(R.id.checkBox45));

        for (CheckBox checkBox : checkBoxes)
            checkBox.setOnClickListener(checkboxOnClickListener);

        selectNumOkBtn = findViewById(R.id.select_num_ok_btn);
        selectNumCancelBtn = findViewById(R.id.select_num_cancel_btn);
        selectNumOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int j = 0 ; j < numbers.size() ; j ++){
                    for (int k = j + 1 ; k < numbers.size() ; k ++){
                        int first = numbers.get(j);
                        int second = numbers.get(k);
                        if(first > second){
                            int change = first;
                            numbers.set(j,second);
                            numbers.set(k,change);
                        }
                    }
                }
                listener.onSelectNumbers(numbers);
                dismiss();
            }
        });
        selectNumCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setLottoSelectDialogListener(LottoSelectDialogListener listener){
        this.listener = listener;
    }

    CheckBox.OnClickListener checkboxOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            for (int i = 0 ; i < checkBoxes.size() ; i++){  //체크박스 전부 비교
                CheckBox cb = checkBoxes.get(i);
                if(cb.getId() == v.getId()){// 체크박스 선택
                    if(cb.isChecked()){ // 채크 박스 체크중인지 아닌지 확인
                        if(numbers.size() > 5){ //최대 6개 번호체크가능
                            cb.setChecked(false);
                            Toast.makeText(getContext(),"최대 6개 번호만 선택할 수 있습니다.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        numbers.add(Integer.valueOf(cb.getText().toString()));
                        Toast.makeText(getContext(),cb.getText() + "번 선택", Toast.LENGTH_LONG).show();
                    }
                    else{
                        numbers.remove(Integer.valueOf(cb.getText().toString()));
                    }
                    break;
                }
            }


        }
    };

}
