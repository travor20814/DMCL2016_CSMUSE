package dmcl.csmuse2016;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

//命盤的activity
public class MinsuActivity extends AppCompatActivity {

    Bundle bundle = new Bundle();
    private Button button_Submit2;
    private RadioGroup Gruop_Sex2,Gruop_YearType;
    private EditText editText_Question2;//輸入問題的地方
    String question="";
    String which_sex="女";//性別，預設為女
    String which_yeartype="";//記錄哪一個(西元or民國or農曆)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minsu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_minsu);
        setSupportActionBar(toolbar);

        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("紫微命書");
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
        //函式
        ButtonSummit();
        //加入fragment的函式
        addFragment();
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
                    Intent intent = new Intent(MinsuActivity.this,HomePageActivity.class);
                    MinsuActivity.this.startActivity(intent);
                    finish();
                    break;
                case R.id.action_settings: //setting鍵
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MinsuActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    void addFragment(){
        //建立一個 MyFirstFragment 的實例(Instantiate)
        Fragment newFragment = new FragmentForMinsu();
        newFragment.setArguments(bundle);
        //使用getFragmentManager()獲得FragmentTransaction物件，並呼叫 beginTransaction() 開始執行Transaction
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //使用FragmentTransaction物件add()的方法將Fragment增加到Activity中
        //add()有三個參數，第一個是Fragment的ViewGroup；第二個是Fragment 的實例(Instantiate)；第三個是Fragment 的Tag
        ft.add(R.id.L2, newFragment, "first");
        //一旦FragmentTransaction出現變化，必須要呼叫commit()使之生效
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
    public void replaceFragment(){

        Fragment newFragment = new FragmentForFree();
        newFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.L2, newFragment, "first");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }
    //送出資料的button
    void ButtonSummit(){
        button_Submit2 = (Button)findViewById(R.id.button_Submit2);

        button_Submit2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editText_Question2 = (EditText) findViewById(R.id.editText_Question2);
                question = editText_Question2.getText().toString();//記錄問題
                Gruop_Sex2 = (RadioGroup) findViewById(R.id.Gruop_Sex2);
                int select_id = Gruop_Sex2.getCheckedRadioButtonId();
                Gruop_YearType = (RadioGroup)findViewById(R.id.Gruop_YearType);
                int select_type = Gruop_YearType.getCheckedRadioButtonId();//記錄選了哪一個(西元or民國or農曆)
                //editText_Question2.setText(String.valueOf(select_type));//測試用
                // 問題輸入轉換為string
                if (select_id == 2131493022) {
                    which_sex = "0"; //API上，女 = 0
                } else {
                    which_sex = "1"; //API上，男 = 0
                }

                if (select_type == 2131493021) {
                    which_yeartype = "0"; //API上，西元 = 0
                } else if (select_id == 2131493022){
                    which_yeartype = "1"; //API上，國曆 = 1
                }else {
                    which_yeartype = "2"; //API上，農曆 = 2
                }
                // 產生對映的url，使用Catch_say88_API_info函式
//                String url = Catch_say88_API_info(question, which_sex);
//                //產生異構Task，因為網路部分不能在main裡面進行，接著執行
//                RequestTask request = new RequestTask();
//                request.execute(url);
            }
        });
    }

/*    public String Catch_say88_API_info(String ques,String sex){
        // token就是識別證，question和sex為使用者輸入
        String url = "http://newtest.88say.com/Api/FreeGua.aspx?";
        url += "token=D5DF5A998BF46E8D37E3D600C022D8B0D76D68BABCF7CFC75304E8EF5168A48B";
        url += "&question=";
        url += ques;
        url += "&sex=";
        url += sex;

        return url;

    }

    public class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) { // 任務輸入的值
            this.publishProgress(null);
            try {
                // 連接httpget輸入
                HttpGet httpget = new HttpGet(params[0]);           // 剛剛輸入的url
                HttpClient client = new DefaultHttpClient();        //建立Httpclinet
                HttpResponse response = client.execute(httpget);    //建立response
                HttpEntity resEntityGet = response.getEntity();     //抓取回傳值
                if (resEntityGet != null) {
                    return EntityUtils.toString(resEntityGet, "utf-8"); // 有抓取到回傳值，並用utf-8編碼回傳
                }

            } catch (Exception e) {
                return e.toString();
            }
            return null;  // 沒抓到任何東西
        }

        @Override
        protected void onPostExecute(String text){  // doInBackground的結果會傳至這個涵式

            // 字串，記取回傳資料，這裡有些回傳東西的結構沒有仔細用好，但是執行上不會出錯，可能會少一些結果之類，我會在進行調整。
            String TxnCode="";
            String TxnMsg="";
            String  Title="";
            String Result_love="";
            String Result_marriage="";
            String Result_wish="";
            String Comment="";

            // 把JSON架構變成String
            try {
                TxnCode = new JSONObject(text).getString("TxnCode");
                TxnMsg = new JSONObject(text).getString("TxnMsg");
                Title = new JSONObject(new JSONObject(text).getString("Result")).getString("Title");
                Result_love = new JSONObject(new JSONObject(text).getString("Result")).getJSONArray("Result").getString(0);
                Result_marriage=new JSONObject(new JSONObject(text).getString("Result")).getJSONArray("Result").getString(1);
                Result_wish=new JSONObject(new JSONObject(text).getString("Result")).getJSONArray("Result").getString(2);
                Comment = new JSONObject(new JSONObject(text).getString("Result")).getString("Comment");
                String All_result = Result_love +"\n" +  Result_marriage + "\n" +  Result_wish;
                bundle.putString("TxnCode", TxnCode);
                bundle.putString("TxnMsg",  TxnMsg);
                bundle.putString("Title", Title);
                bundle.putString("All_result", All_result);
                bundle.putString("Comment",  Comment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            replaceFragment();
        }

        @Override
        protected void onProgressUpdate(String... result){

        }
    }
*/
}
