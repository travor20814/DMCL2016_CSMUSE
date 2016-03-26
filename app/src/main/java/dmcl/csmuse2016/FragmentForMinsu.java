package dmcl.csmuse2016;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FragmentForMinsu extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    private Button button_A,button_B,button_C,button_D;//A命宮主星 B優缺 C課題 D風采
    private TextView content2;
    String return_A,return_B,return_C,return_D;//這4個用來記錄回傳的資料
    @Override
    //資料送出後，顯示的資料應該要在這裡
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentforminsu, container, false);
//這裡我有改

        return_A = getArguments().getString("Reslut_Star");
        return_B = getArguments().getString("Result_Good_Bad");
        return_C = getArguments().getString("Reslut_Issue");
        return_D = getArguments().getString("Reslut_Desc");

        button_A = (Button)v.findViewById(R.id.button_A);
        button_B = (Button)v.findViewById(R.id.button_B);
        button_C = (Button)v.findViewById(R.id.button_C);
        button_D = (Button)v.findViewById(R.id.button_D);
        content2 = (TextView)v.findViewById(R.id.content2);
        content2.setText(return_A);
        button_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content2.setText(return_A);//把回傳的A命宮主星顯示出來
                content2.setBackgroundColor(Color.parseColor("#fcf7c9"));
            }
        });
        button_B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content2.setText(return_B);//把回傳的B優缺顯示出來
                content2.setBackgroundColor(Color.parseColor("#fcf39d"));
            }
        });
        button_C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content2.setText(return_C);//把回傳的C課題顯示出來
                content2.setBackgroundColor(Color.parseColor("#fff370"));
            }
        });
        button_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content2.setText(return_D);//把回傳的A命宮主星顯示出來
                content2.setBackgroundColor(Color.parseColor("#fff320"));
            }
        });

        return v;
    }
}
