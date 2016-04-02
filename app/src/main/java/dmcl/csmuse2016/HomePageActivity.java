package dmcl.csmuse2016;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import java.lang.reflect.Member;

public class HomePageActivity extends Activity {
    private String userMail="";
    private String userName="";
    private ImageButton imagebutton01;
    private ImageButton imagebutton02;
    private ImageButton imagebutton03;
    private ImageButton imagebutton04;
    private ImageButton imagebutton05;
    private ImageButton imagebutton06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //傳值確認帳號
        //////////////////////////////////////////////////////
        Bundle getExtra = getIntent().getExtras();
        if (getExtra != null){
            userMail = getExtra.getString("mail");
        }
        /////////////////////////////////////////////////////

        setContentView(R.layout.homepage);
        imagebutton01 = (ImageButton)findViewById(R.id.button_topleft);
        imagebutton02 = (ImageButton)findViewById(R.id.button_topright);
        imagebutton03 = (ImageButton)findViewById(R.id.button_middle);
        imagebutton04 = (ImageButton)findViewById(R.id.button_bottomleft);
        imagebutton05 = (ImageButton)findViewById(R.id.button_bottomright);
        //new for 八卦命盤
        imagebutton06 = (ImageButton)findViewById(R.id.button_middle_bottom);

        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {

                if (v == imagebutton01){ //卜卦畫面
                    Intent intent = new Intent(HomePageActivity.this,FreeActivity.class);
                    HomePageActivity.this.startActivity(intent);
                }
                if (v == imagebutton02){ //命書畫面
                    Intent intent = new Intent(HomePageActivity.this,MinsuActivity.class);
                    HomePageActivity.this.startActivity(intent);
                }
                if (v == imagebutton03){//命盤畫面
                    Intent intent = new Intent(HomePageActivity.this,MinpanActivity.class);
                    HomePageActivity.this.startActivity(intent);
                }
                if (v == imagebutton04){//付費專區
                    Intent intent = new Intent(HomePageActivity.this,fufay.class);
                    HomePageActivity.this.startActivity(intent);
                }
                if (v == imagebutton05){//會員專區
                    Intent intentMember = new Intent(getApplicationContext(),MemberActivity.class);
                    intentMember.putExtra("mail","test@gmail.com"); //send mail to next activity
                    HomePageActivity.this.startActivity(intentMember);
                }
                if (v == imagebutton06){//八卦命盤
                    Intent intent = new Intent(HomePageActivity.this,EightWordMinpanActivity.class);
                    HomePageActivity.this.startActivity(intent);
                }
            }
        };
        imagebutton01.setOnClickListener(handler);
        imagebutton02.setOnClickListener(handler);
        imagebutton03.setOnClickListener(handler);
        imagebutton04.setOnClickListener(handler);
        imagebutton05.setOnClickListener(handler);
        imagebutton06.setOnClickListener(handler);
    }
    public void back_doNegativeClick() {
        // Do stuff here.
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            HomePageActivity_back_dialogFragment editNameDialog =  HomePageActivity_back_dialogFragment.newInstance("確定要離開嗎？", "?????", "取消", "確定");
            editNameDialog.show(getFragmentManager(), "EditNameDialog");
        }
        return false;
    }
}
