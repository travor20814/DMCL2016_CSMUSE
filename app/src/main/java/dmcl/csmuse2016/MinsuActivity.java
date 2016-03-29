package dmcl.csmuse2016;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
    EditText input_year,input_month,input_day;
    String which_sex="女";//性別，預設為女
    String which_yeartype="";//記錄哪一個(西元or民國or農曆)
    String hour="";
    private String[] hour_list = {"0:00~0:59(子時)","1:00~1:59(丑時)","2:00~2:59(丑時)","3:00~3:59(寅時)","4:00~4:59(寅時)","5:00~5:59(卯時)",
                                "6:00~6:59(卯時)","7:00~7:59(辰時)","8:00~8:59(辰時)","9:00~9:59(巳時)","10:00~10:59(巳時)","11:00~11:59(午時)",
                                "12:00~12:59(午時)","13:00~13:59(未時)","14:00~14:59(未時)","15:00~15:59(申時)","16:00~16:59(申時)","17:00~17:59(酉時)",
                                "18:00~18:59(酉時)","19:00~19:59(戌時)","20:00~20:59(戌時)","21:00~21:59(亥時)","22:00~22:59(亥時)","23:00~23:59(子時)",};
    private ArrayAdapter<String> listAdapter;
    private Spinner spinner;

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

        Fragment newFragment = new FragmentForMinsu();
        newFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.L2, newFragment, "first");
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();

    }
    //送出資料的button
    void ButtonSummit(){
        button_Submit2 = (Button)findViewById(R.id.button_Submit2);
        spinner = (Spinner)findViewById(R.id.spinner);

        listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, hour_list);
        listAdapter.setDropDownViewResource(R.layout.myspinner);
        spinner.setAdapter(listAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                hour = Integer.toString(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });


        button_Submit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                input_year = (EditText) findViewById(R.id.input_year_m);
                String s_year = input_year.getText().toString();
                input_month = (EditText) findViewById(R.id.input_month_m);
                String s_month = input_month.getText().toString();
                input_day = (EditText) findViewById(R.id.input_day_m);
                String s_day = input_day.getText().toString();

                //radioGroup
                Gruop_Sex2 = (RadioGroup) findViewById(R.id.Gruop_Sex2);
                int select_id = Gruop_Sex2.getCheckedRadioButtonId();
                Gruop_YearType = (RadioGroup) findViewById(R.id.Gruop_YearType);
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
                } else if (select_id == 2131493022) {
                    which_yeartype = "1"; //API上，國曆 = 1
                } else {
                    which_yeartype = "2"; //API上，農曆 = 2
                }
                if (!s_year.equals("") && !s_month.equals("") && !s_day.equals("")) {
                    if (time_check(s_year, s_month, s_day)) {
                        // 產生對映的url，使用Catch_say88_API_info函式
                        String url = Catch_say88_API_info(which_yeartype, s_year, s_month, s_day, hour, which_sex);
                        //產生異構Task，因為網路部分不能在main裡面進行，接著執行
                        RequestTask request = new RequestTask();
                        request.execute(url);
                    } else {
                        bundle.putString("Reslut_Star", "輸入時間有誤");
                        bundle.putString("Result_Good_Bad", "輸入時間有誤");
                        bundle.putString("Reslut_Issue", "輸入時間有誤");
                        bundle.putString("Reslut_Desc", "輸入時間有誤");
                        replaceFragment();
                    }
                } else {
                    bundle.putString("Reslut_Star", "輸入時間有誤");
                    bundle.putString("Result_Good_Bad", "輸入時間有誤");
                    bundle.putString("Reslut_Issue", "輸入時間有誤");
                    bundle.putString("Reslut_Desc", "輸入時間有誤");
                    replaceFragment();
                }
            }
        });
    }

    public boolean time_check(String s_year,String s_month,String s_day){
        int i_year, i_month, i_day;
        i_year = Integer.parseInt(s_year);
        i_month = Integer.parseInt(s_month);
        i_day = Integer.parseInt(s_day);

        if (i_year >= 1900 && i_year < 2032 && i_month > 0 && i_month < 13 && i_day > 0) {
            if (i_year % 4 == 0 && i_month == 2 && i_day == 29) {
                return true;
            } else if (i_month == 1 && i_day <= 31) {
                return true;
            } else if (i_month == 2 && i_day <= 28) {
                return true;
            } else if (i_month == 3 && i_day <= 31) {
                return true;
            } else if (i_month == 4 && i_day <= 30) {
                return true;
            } else if (i_month == 5 && i_day <= 31) {
                return true;
            } else if (i_month == 6 && i_day <= 30) {
                return true;
            } else if (i_month == 7 && i_day <= 31) {
                return true;
            } else if (i_month == 8 && i_day <= 31) {
                return true;
            } else if (i_month == 9 && i_day <= 30) {
                return true;
            } else if (i_month == 10 && i_day <= 31) {
                return true;
            } else if (i_month == 11 && i_day <= 30) {
                return true;
            } else if (i_month == 12 && i_day <= 31) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String Catch_say88_API_info(String birthType,String Year,String Month , String Day ,String Hour,String Sex){
        // token就是識別證
        String url = "http://newtest.88say.com/Api/product/Unit507.aspx?";
        url += "token=D5DF5A998BF46E8D37E3D600C022D8B0D76D68BABCF7CFC75304E8EF5168A48B";
        url += "&birthType=";
        url += birthType;
        url += "&year=";
        url += Year;
        url += "&month=";
        url += Month;
        url += "&day=";
        url += Day;
        url += "&hour=";
        url += Hour;
        url += "&sex=";
        url += Sex;
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

            JsonTrasnfer_output(text);
            replaceFragment();
        }

        @Override
        protected void onProgressUpdate(String... result){

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void JsonTrasnfer_output(String jsonInput){
        String TxnCode="";
        String TxnMsg="";
        String Reslut_Star="";
        String Reslut_Good="";
        String Reslut_Bad="";
        String Reslut_Issue="";
        String Reslut_Desc="";
        try {
            TxnCode = new JSONObject(jsonInput).getString("TxnCode");
            TxnMsg = new JSONObject(jsonInput).getString("TxnMsg");
            Reslut_Star =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Star");
            Reslut_Good =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Good");
            Reslut_Bad =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Bad");
            Reslut_Issue =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Issue");
            Reslut_Desc =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Desc");
            String Result_Good_Bad =  Reslut_Good + "\n" + Reslut_Bad;
            bundle.putString("Reslut_Star",Reslut_Star);
            bundle.putString("Result_Good_Bad",Result_Good_Bad);
            bundle.putString("Reslut_Issue",Reslut_Issue);
            bundle.putString("Reslut_Desc",Reslut_Desc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
