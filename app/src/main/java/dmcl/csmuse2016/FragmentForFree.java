package dmcl.csmuse2016;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by jack on 2016/3/23.
 */
public class FragmentForFree extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    private Button button_Title,button_Result,button_Comment;
    private TextView content;
    String return_Title,return_Result,return_Comment;//這三個用來記錄回傳的資料
    @Override
    //資料送出後，顯示的資料應該要在這裡
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmentforfree, container, false);

        return_Title = getArguments().getString("Title");
        return_Result = getArguments().getString("All_result");
        return_Comment = getArguments().getString("Comment");

        button_Title = (Button)v.findViewById(R.id.button_Title);
        button_Result = (Button)v.findViewById(R.id.button_Result);
        button_Comment = (Button)v.findViewById(R.id.button_Comment);
        content = (TextView)v.findViewById(R.id.content);
        content.setText(return_Title);
        button_Title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText(return_Title);//把回傳的Title顯示出來
                //content.setBackgroundColor(Color.parseColor("#fcf7c9"));
            }
        });
        button_Result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText(return_Result);//把回傳的Result顯示出來
                //content.setBackgroundColor(Color.parseColor("#fcf39d"));
            }
        });
        button_Comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content.setText(return_Comment);//把回傳的Comment顯示出來
                //content.setBackgroundColor(Color.parseColor("#fff370"));
            }
        });

        return v;
    }
}
