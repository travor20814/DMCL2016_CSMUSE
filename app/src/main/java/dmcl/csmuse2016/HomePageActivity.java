package dmcl.csmuse2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;


public class HomePageActivity extends Activity {

    private ImageButton imagebutton01;
    private ImageButton imagebutton02;
    private ImageButton imagebutton03;
    private ImageButton imagebutton04;
    private ImageButton imagebutton05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.homepage);

        imagebutton01 = (ImageButton)findViewById(R.id.button_topleft);
        imagebutton02 = (ImageButton)findViewById(R.id.button_topright);
        imagebutton03 = (ImageButton)findViewById(R.id.button_middle);
        imagebutton04 = (ImageButton)findViewById(R.id.button_bottomleft);
        imagebutton05 = (ImageButton)findViewById(R.id.button_bottomright);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                if (v == imagebutton01){ //命盤畫面
                    Intent intent = new Intent(HomePageActivity.this,MinpanActivity.class);
                    HomePageActivity.this.startActivity(intent);
                }
                if (v == imagebutton02){ //卜卦畫面

                }
                if (v == imagebutton03){//每日算命

                }
                if (v == imagebutton04){//萬年曆

                }
                if (v == imagebutton05){//紫微精論

                }
            }
        };
        imagebutton01.setOnClickListener(handler);
        imagebutton02.setOnClickListener(handler);
        imagebutton03.setOnClickListener(handler);
        imagebutton04.setOnClickListener(handler);
        imagebutton05.setOnClickListener(handler);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
