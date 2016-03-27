package dmcl.csmuse2016;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//命盤的activity
public class MinpanActivity extends AppCompatActivity {

    Bundle bundle1 = new Bundle();
    private Button button_Submit2;
    private RadioGroup Gruop_Sex2,Gruop_YearType;
    EditText input_year,input_month,input_day;
    String which_sex="女";//性別，預設為女
    String which_yeartype="";//記錄哪一個(西元or民國or農曆)
    int count;
    String hour="";
    private String[] hour_list = {"0:00~0:59(子時)","1:00~1:59(丑時)","2:00~2:59(丑時)","3:00~3:59(寅時)","4:00~4:59(寅時)","5:00~5:59(卯時)",
            "6:00~6:59(卯時)","7:00~7:59(辰時)","8:00~8:59(辰時)","9:00~9:59(巳時)","10:00~10:59(巳時)","11:00~11:59(午時)",
            "12:00~12:59(午時)","13:00~13:59(未時)","14:00~14:59(未時)","15:00~15:59(申時)","16:00~16:59(申時)","17:00~17:59(酉時)",
            "18:00~18:59(酉時)","19:00~19:59(戌時)","20:00~20:59(戌時)","21:00~21:59(亥時)","22:00~22:59(亥時)","23:00~23:59(子時)",};
    private ArrayAdapter<String> listAdapter;
    private Spinner spinner;
    public static TextView errorMs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.minpan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_minpan);
        setSupportActionBar(toolbar);

        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("紫微命盤");
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
                    Intent intent = new Intent(MinpanActivity.this,HomePageActivity.class);
                    MinpanActivity.this.startActivity(intent);
                    finish();

                    break;
                case R.id.action_settings: //setting鍵
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MinpanActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };




    void ButtonSummit(){
        button_Submit2 = (Button)findViewById(R.id.button_minpan_Submit2);
        spinner = (Spinner)findViewById(R.id.minpan_spinner);

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
                errorMs = (TextView)findViewById(R.id.textViewError);
                input_year = (EditText) findViewById(R.id.input_minpan_year);
                String s_year = input_year.getText().toString();
                input_month = (EditText) findViewById(R.id.input_minpan_month);
                String s_month = input_month.getText().toString();
                input_day = (EditText) findViewById(R.id.input_minpan_day);
                String s_day = input_day.getText().toString();

                //radioGroup
                Gruop_Sex2 = (RadioGroup) findViewById(R.id.Gruop_minpan_Sex2);
                int select_id = Gruop_Sex2.getCheckedRadioButtonId();
                Gruop_YearType = (RadioGroup) findViewById(R.id.Gruo_minpanp_YearType);
                int select_type = Gruop_YearType.getCheckedRadioButtonId();//記錄選了哪一個(西元or民國or農曆)
                //editText_Question2.setText(String.valueOf(select_type));//測試用

                // 問題輸入轉換為string
                if (select_id == 2131493027) {
                    which_sex = "0"; //API上，女 = 0
                } else {
                    which_sex = "1"; //API上，男 = 0
                }

                if (select_type == 2131493020) {
                    which_yeartype = "0"; //API上，西元 = 0
                } else if (select_id == 2131493021) {
                    which_yeartype = "1"; //API上，國曆 = 1
                } else {
                    which_yeartype = "2"; //API上，農曆 = 2
                }
                if (!s_year.equals("") && !s_month.equals("") && !s_day.equals("")) {
                    if (time_check(s_year, s_month, s_day)) {
                        // 產生對映的url，使用Catch_say88_API_info函式
                        errorMs.setText("資料讀取中，請稍候.....");
                        String url = Catch_say88_API_info(which_yeartype, s_year, s_month, s_day, hour, which_sex);
                        //產生異構Task，因為網路部分不能在main裡面進行，接著執行
                        RequestTask request = new RequestTask();
                        request.execute(url);
                    } else {

                        errorMs.setText("輸入時間有誤");
                    }
                } else {

                    errorMs.setText("輸入時間有誤");
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
        String url = "http://newtest.88say.com/Api/product/Unit306.aspx?";
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

        }

        @Override
        protected void onProgressUpdate(String... result){

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void JsonTrasnfer_output(String jsonInput){
        String TxnCode="";
        String TxnMsg="";
        String Reslut_Sex="";
        String Reslut_Age="";
        String Reslut_Birth="";
        String Reslut_LunarBirth="";
        String Reslut_FateManner="";
        String Reslut_FateSex="";
        //12個框格
        //1
        String Reslut_Houses_Name1="";
        String Reslut_Houses_Decade1="";
        String Reslut_Houses_GanZhi1="";
        //2
        String Reslut_Houses_Name2="";
        String Reslut_Houses_Decade2="";
        String Reslut_Houses_GanZhi2="";
        //3
        String Reslut_Houses_Name3="";
        String Reslut_Houses_Decade3="";
        String Reslut_Houses_GanZhi3="";
        //4
        String Reslut_Houses_Name4="";
        String Reslut_Houses_Decade4="";
        String Reslut_Houses_GanZhi4="";
        //5
        String Reslut_Houses_Name5="";
        String Reslut_Houses_Decade5="";
        String Reslut_Houses_GanZhi5="";
        //6
        String Reslut_Houses_Name6="";
        String Reslut_Houses_Decade6="";
        String Reslut_Houses_GanZhi6="";
        //7
        String Reslut_Houses_Name7="";
        String Reslut_Houses_Decade7="";
        String Reslut_Houses_GanZhi7="";
        //8
        String Reslut_Houses_Name8="";
        String Reslut_Houses_Decade8="";
        String Reslut_Houses_GanZhi8="";
        //9
        String Reslut_Houses_Name9="";
        String Reslut_Houses_Decade9="";
        String Reslut_Houses_GanZhi9="";
        //10
        String Reslut_Houses_Name10="";
        String Reslut_Houses_Decade10="";
        String Reslut_Houses_GanZhi10="";
        //11
        String Reslut_Houses_Name11="";
        String Reslut_Houses_Decade11="";
        String Reslut_Houses_GanZhi11="";
        //12
        String Reslut_Houses_Name12="";
        String Reslut_Houses_Decade12="";
        String Reslut_Houses_GanZhi12="";
        // 12個匡格
        // 1
        String[] Reslut_Houses_Stars_Name1=new String[6];
        String[] Reslut_Houses_Stars_Flag1=new String[6];
        //2
        String[] Reslut_Houses_Stars_Name2=new String[6];
        String[] Reslut_Houses_Stars_Flag2=new String[6];
        //3
        String[] Reslut_Houses_Stars_Name3=new String[6];
        String[] Reslut_Houses_Stars_Flag3=new String[6];
        //4
        String[] Reslut_Houses_Stars_Name4=new String[6];
        String[] Reslut_Houses_Stars_Flag4=new String[6];
        //5
        String[] Reslut_Houses_Stars_Name5=new String[6];
        String[] Reslut_Houses_Stars_Flag5=new String[6];
        //6
        String[] Reslut_Houses_Stars_Name6=new String[6];
        String[] Reslut_Houses_Stars_Flag6=new String[6];
        //7
        String[] Reslut_Houses_Stars_Name7=new String[6];
        String[] Reslut_Houses_Stars_Flag7=new String[6];
        //8
        String[] Reslut_Houses_Stars_Name8=new String[6];
        String[] Reslut_Houses_Stars_Flag8=new String[6];
        //9
        String[] Reslut_Houses_Stars_Name9=new String[6];
        String[] Reslut_Houses_Stars_Flag9=new String[6];
        //10
        String[] Reslut_Houses_Stars_Name10=new String[6];
        String[] Reslut_Houses_Stars_Flag10=new String[6];
        //11
        String[] Reslut_Houses_Stars_Name11=new String[6];
        String[] Reslut_Houses_Stars_Flag11=new String[6];
        //12
        String[] Reslut_Houses_Stars_Name12=new String[6];
        String[] Reslut_Houses_Stars_Flag12=new String[6];


            try {
                TxnCode = new JSONObject(jsonInput).getString("TxnCode");
                TxnMsg = new JSONObject(jsonInput).getString("TxnMsg");
                Reslut_Sex =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Sex");
                Reslut_Age =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Age");
                Reslut_Birth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Birth");
                Reslut_LunarBirth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("LunarBirth");
                Reslut_FateManner =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("FateManner");
                Reslut_FateSex =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("FateSex");

                bundle1.putString("Reslut_Sex",Reslut_Sex);
                bundle1.putString("Reslut_Age",Reslut_Age);
                bundle1.putString("Reslut_Birth",Reslut_Birth);
                bundle1.putString("Reslut_LunarBirth",Reslut_LunarBirth);
                bundle1.putString("Reslut_FateManner",Reslut_FateManner);
                bundle1.putString("Reslut_FateSex",Reslut_FateSex);

                //1
                Reslut_Houses_Name1 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Name");
                Reslut_Houses_Decade1  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Decade");
                Reslut_Houses_GanZhi1  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name1",Reslut_Houses_Name1);
                bundle1.putString("Reslut_Houses_Decade1",Reslut_Houses_Decade1);
                bundle1.putString("Reslut_Houses_GanZhi1",Reslut_Houses_GanZhi1);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Stars")).length();
                bundle1.putInt("count1", count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name1[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag1[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(0).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name1"+Integer.toString(i),Reslut_Houses_Stars_Name1[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag1"+Integer.toString(i), Reslut_Houses_Stars_Flag1[i]);
                }
                //2
                Reslut_Houses_Name2 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("Name");
                Reslut_Houses_Decade2  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("Decade");
                Reslut_Houses_GanZhi2  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name2",Reslut_Houses_Name2);
                bundle1.putString("Reslut_Houses_Decade2",Reslut_Houses_Decade2);
                bundle1.putString("Reslut_Houses_GanZhi2",Reslut_Houses_GanZhi2);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("Stars")).length();
                bundle1.putInt("count2",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name2[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag2[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(1).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name2"+Integer.toString(i),Reslut_Houses_Stars_Name2[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag2"+Integer.toString(i), Reslut_Houses_Stars_Flag2[i]);
                }
                //3
                Reslut_Houses_Name3 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("Name");
                Reslut_Houses_Decade3  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("Decade");
                Reslut_Houses_GanZhi3  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name3",Reslut_Houses_Name3);
                bundle1.putString("Reslut_Houses_Decade3",Reslut_Houses_Decade3);
                bundle1.putString("Reslut_Houses_GanZhi3",Reslut_Houses_GanZhi3);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("Stars")).length();
                bundle1.putInt("count3",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name3[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag3[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(2).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name3"+Integer.toString(i),Reslut_Houses_Stars_Name3[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag3"+Integer.toString(i), Reslut_Houses_Stars_Flag3[i]);
                }
                //4
                Reslut_Houses_Name4 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("Name");
                Reslut_Houses_Decade4  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("Decade");
                Reslut_Houses_GanZhi4  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name4",Reslut_Houses_Name4);
                bundle1.putString("Reslut_Houses_Decade4",Reslut_Houses_Decade4);
                bundle1.putString("Reslut_Houses_GanZhi4",Reslut_Houses_GanZhi4);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("Stars")).length();
                bundle1.putInt("count4",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name4[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag4[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(3).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name4"+Integer.toString(i),Reslut_Houses_Stars_Name4[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag4"+Integer.toString(i), Reslut_Houses_Stars_Flag4[i]);
                }
                //5
                Reslut_Houses_Name5 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("Name");
                Reslut_Houses_Decade5  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("Decade");
                Reslut_Houses_GanZhi5  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name5",Reslut_Houses_Name5);
                bundle1.putString("Reslut_Houses_Decade5",Reslut_Houses_Decade5);
                bundle1.putString("Reslut_Houses_GanZhi5",Reslut_Houses_GanZhi5);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("Stars")).length();
                bundle1.putInt("count5",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name5[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag5[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(4).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name5"+Integer.toString(i),Reslut_Houses_Stars_Name5[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag5"+Integer.toString(i), Reslut_Houses_Stars_Flag5[i]);
                }
                //6
                Reslut_Houses_Name6 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("Name");
                Reslut_Houses_Decade6  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("Decade");
                Reslut_Houses_GanZhi6  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name6",Reslut_Houses_Name6);
                bundle1.putString("Reslut_Houses_Decade6",Reslut_Houses_Decade6);
                bundle1.putString("Reslut_Houses_GanZhi6",Reslut_Houses_GanZhi6);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("Stars")).length();
                bundle1.putInt("count6",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name6[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag6[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(5).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name6"+Integer.toString(i),Reslut_Houses_Stars_Name6[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag6"+Integer.toString(i), Reslut_Houses_Stars_Flag6[i]);
                }
                //7
                Reslut_Houses_Name7 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("Name");
                Reslut_Houses_Decade7  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("Decade");
                Reslut_Houses_GanZhi7  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name7",Reslut_Houses_Name7);
                bundle1.putString("Reslut_Houses_Decade7",Reslut_Houses_Decade7);
                bundle1.putString("Reslut_Houses_GanZhi7",Reslut_Houses_GanZhi7);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("Stars")).length();
                bundle1.putInt("count7",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name7[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag7[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(6).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name7"+Integer.toString(i),Reslut_Houses_Stars_Name7[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag7"+Integer.toString(i), Reslut_Houses_Stars_Flag7[i]);
                }
                //8
                Reslut_Houses_Name8 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("Name");
                Reslut_Houses_Decade8 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("Decade");
                Reslut_Houses_GanZhi8  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name8",Reslut_Houses_Name8);
                bundle1.putString("Reslut_Houses_Decade8",Reslut_Houses_Decade8);
                bundle1.putString("Reslut_Houses_GanZhi8",Reslut_Houses_GanZhi8);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("Stars")).length();
                bundle1.putInt("count8",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name8[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag8[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(7).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name8"+Integer.toString(i),Reslut_Houses_Stars_Name8[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag8"+Integer.toString(i), Reslut_Houses_Stars_Flag8[i]);
                }
                //9
                Reslut_Houses_Name9 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("Name");
                Reslut_Houses_Decade9  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("Decade");
                Reslut_Houses_GanZhi9  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name9",Reslut_Houses_Name9);
                bundle1.putString("Reslut_Houses_Decade9",Reslut_Houses_Decade9);
                bundle1.putString("Reslut_Houses_GanZhi9",Reslut_Houses_GanZhi9);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("Stars")).length();
                bundle1.putInt("count9",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name9[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag9[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(8).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name9"+Integer.toString(i),Reslut_Houses_Stars_Name9[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag9"+Integer.toString(i), Reslut_Houses_Stars_Flag9[i]);
                }
                //10
                Reslut_Houses_Name10 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("Name");
                Reslut_Houses_Decade10  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("Decade");
                Reslut_Houses_GanZhi10  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name10",Reslut_Houses_Name10);
                bundle1.putString("Reslut_Houses_Decade10",Reslut_Houses_Decade10);
                bundle1.putString("Reslut_Houses_GanZhi10",Reslut_Houses_GanZhi10);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("Stars")).length();
                bundle1.putInt("count10",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name10[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag10[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(9).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name10"+Integer.toString(i),Reslut_Houses_Stars_Name10[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag10"+Integer.toString(i), Reslut_Houses_Stars_Flag10[i]);
                }
                //11
                Reslut_Houses_Name11 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("Name");
                Reslut_Houses_Decade11  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("Decade");
                Reslut_Houses_GanZhi11  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name11",Reslut_Houses_Name11);
                bundle1.putString("Reslut_Houses_Decade11",Reslut_Houses_Decade11);
                bundle1.putString("Reslut_Houses_GanZhi11",Reslut_Houses_GanZhi11);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("Stars")).length();
                bundle1.putInt("count11",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name11[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag11[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(10).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name11"+Integer.toString(i),Reslut_Houses_Stars_Name11[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag11"+Integer.toString(i), Reslut_Houses_Stars_Flag11[i]);
                }
                //12
                Reslut_Houses_Name12 =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("Name");
                Reslut_Houses_Decade12  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("Decade");
                Reslut_Houses_GanZhi12  =  new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("GanZhi");
                bundle1.putString("Reslut_Houses_Name12",Reslut_Houses_Name12);
                bundle1.putString("Reslut_Houses_Decade12",Reslut_Houses_Decade12);
                bundle1.putString("Reslut_Houses_GanZhi12",Reslut_Houses_GanZhi12);

                count = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("Stars")).length();
                bundle1.putInt("count12",count);
                for (int i = 0; i < count; i++) {
                    Reslut_Houses_Stars_Name12[i]  =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("Stars")).getJSONObject(i).getString("Name");
                    Reslut_Houses_Stars_Flag12[i] =  new JSONArray( new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Houses")).getJSONObject(11).getString("Stars")).getJSONObject(i).getString("Flag");
                    bundle1.putString("Reslut_Houses_Stars_Name12"+Integer.toString(i),Reslut_Houses_Stars_Name12[i]);
                    bundle1.putString("Reslut_Houses_Stars_Flag12"+Integer.toString(i), Reslut_Houses_Stars_Flag12[i]);
                }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClass(MinpanActivity.this, MinpanCatchActivity.class);
        intent.putExtras(bundle1);
        startActivity(intent);
    }

}
