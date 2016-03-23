package dmcl.csmuse2016;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

//命盤的activity
public class FreeActivity extends AppCompatActivity {

    private Button button_Submit,button_Title,button_Result,button_Comment;
    private TextView content;
    private RadioGroup Gruop_Sex;
    private EditText editText_Question;//輸入問題的地方
    String question="";
    String which_sex="女";//性別，預設為女

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_free);
        setSupportActionBar(toolbar);

        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("免費卜卦");
        toolbar.setTitleTextColor(Color.BLACK);
        // Sub Title
        toolbar.setSubtitle("88Say幫您及時掌握未來");
        toolbar.setSubtitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);

        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back bottom
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        // Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //加入fragment的函式
        addFragment();
        //函式
        ButtonSummit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //toolbar按鈕被按時
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_home: //home鍵被按時
                    Intent intent = new Intent(FreeActivity.this,HomePageActivity.class);
                    FreeActivity.this.startActivity(intent);
                    finish();
                    break;
                case R.id.action_settings: //setting鍵
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(FreeActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    void addFragment(){
        //建立一個 MyFirstFragment 的實例(Instantiate)
        Fragment newFragment = new FragmentForFree();
        //使用getFragmentManager()獲得FragmentTransaction物件，並呼叫 beginTransaction() 開始執行Transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //使用FragmentTransaction物件add()的方法將Fragment增加到Activity中
        //add()有三個參數，第一個是Fragment的ViewGroup；第二個是Fragment 的實例(Instantiate)；第三個是Fragment 的Tag
        ft.add(R.id.L1, newFragment, "first");
        //一旦FragmentTransaction出現變化，必須要呼叫commit()使之生效
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    //送出資料的button
    void ButtonSummit(){
        button_Submit = (Button)findViewById(R.id.button_Submit);
        editText_Question = (EditText)findViewById(R.id.editText_Question);
        Gruop_Sex = (RadioGroup)findViewById(R.id.Gruop_Sex);

        //為Group_Sex設置一個監聽器:setOnCheckedChanged()
        //預設which_sex為"女"，若有改變則會透過監聽器去改
        Gruop_Sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                which_sex = radbtn.getText().toString();
            }
        });

        button_Submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                question = editText_Question.getText().toString();//記錄問題
                //which_sex已經在上面記錄過了
            }
        });
    }

}
