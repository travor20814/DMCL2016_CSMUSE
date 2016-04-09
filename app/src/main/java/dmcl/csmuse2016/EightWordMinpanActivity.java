package dmcl.csmuse2016;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//八字命盤的activity
public class EightWordMinpanActivity extends AppCompatActivity {

    EditText year , month , day ;
    RadioGroup yearTpye , sexType;
    Button Send;
    String s_yearTpye,s_sexTpye;
    Bundle bundle_charater = new Bundle();
    RadioButton west , guo , non , male , female;
    private String[] hour_list = {"0:00~0:59(子時)","1:00~1:59(丑時)","2:00~2:59(丑時)","3:00~3:59(寅時)","4:00~4:59(寅時)","5:00~5:59(卯時)",
            "6:00~6:59(卯時)","7:00~7:59(辰時)","8:00~8:59(辰時)","9:00~9:59(巳時)","10:00~10:59(巳時)","11:00~11:59(午時)",
            "12:00~12:59(午時)","13:00~13:59(未時)","14:00~14:59(未時)","15:00~15:59(申時)","16:00~16:59(申時)","17:00~17:59(酉時)",
            "18:00~18:59(酉時)","19:00~19:59(戌時)","20:00~20:59(戌時)","21:00~21:59(亥時)","22:00~22:59(亥時)","23:00~23:59(子時)",};

    private String[] year_list = {"1901","1902","1903","1904","1905","1906","1907","1908","1909",
            "1910","1911","1912","1913","1914","1915","1916","1917","1918","1919",
            "1920","1921","1922","1923","1924","1925","1926","1927","1928","1929",
            "1930","1931","1932","1933","1934","1935","1936","1937","1938","1939",
            "1940","1941","1942","1943","1944","1945","1946","1947","1948","1949",
            "1950","1951","1952","1953","1954","1955","1956","1957","1958","1959",
            "1960","1961","1962","1963","1964","1965","1966","1967","1968","1969",
            "1970","1971","1972","1973","1974","1975","1976","1977","1978","1979",
            "1980","1981","1982","1983","1984","1985","1986","1987","1988","1989",
            "1990","1991","1992","1993","1994","1995","1996","1997","1998","1999",
            "2000","2001","2002","2003","2004","2005","2006","2007","2008","2009",
            "2010","2011","2012","2013","2014","2015","2016","2017","2018","2019",
            "2020","2021","2022","2023","2024","2025","2026","2027","2028","2029"};

    private  String[] month_list = {"1","2","3","4","5","6","7","8","9","10","11","12"};

    private  String[] day_list = {"1","2","3","4","5","6","7","8","9","10",
            "11","12","13","14","15","16","17","18","19","20",
            "21","22","23","24","25","26","27","28","29","30",
            "31"};
    private ArrayAdapter<String> listAdapter , listAdapter_year,listAdapter_month,listAdapter_day;
    private Spinner spinner , spinner_year , spinner_month , spinner_day;
    private final String filename="account.txt";
    private boolean loginornot;
    String hour="",s_year="",s_month="",s_day="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eightwordminpan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_eightwordminpan);
        setSupportActionBar(toolbar);

        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("八字命盤");
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
        onClickButton();
        //加入fragment的函式
        //addFragment();
        loginornot = new Write_and_Read(filename,getFilesDir()).ifLogin();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(loginornot)
            getMenuInflater().inflate(R.menu.mainmenu, menu);
        else
            getMenuInflater().inflate(R.menu.guestmenu, menu);
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
            Intent tologin =new Intent();
            switch (menuItem.getItemId()) {
                case R.id.action_home: //home鍵被按時
                    finish();
                    break;
                case R.id.action_settings: //setting鍵
                    if(isNetwork()) {
                        String fromfile = new Write_and_Read(filename, getFilesDir()).ReadFromFile();
                        String[] fromfileArray = fromfile.split("###");
                        Intent intentMember = new Intent(getApplicationContext(), MemberActivity.class);
                        intentMember.putExtra("mail", fromfileArray[2]); //send mail to next activity
                        EightWordMinpanActivity.this.startActivity(intentMember);
                        finish();
                    }
                    else {
                        notNetwork_dialogFragment EditNameDialog = new notNetwork_dialogFragment();
                        EditNameDialog.show(getFragmentManager(), "EditNameDialog");
                    }
                    break;
                case R.id.action_designer://製作群
                    msg+="designer clicked";
                    break;
                case R.id.action_logout://登出
                    new Write_and_Read(filename,getFilesDir()).WritetoFile_clear("");
                    tologin.setClass(EightWordMinpanActivity.this,LoginActivity.class);
                    startActivity(tologin);
                    finish();
                    break;
                case R.id.action_login://訪客登入
                    tologin.setClass(EightWordMinpanActivity.this,LoginActivity.class);
                    startActivity(tologin);
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(EightWordMinpanActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };
    private boolean isNetwork()
    {
        boolean result = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
        {
            result = false;
        }
        else
        {
            if (!info.isAvailable())
            {
                result =false;
            }
            else
            {
                result = true;
            }
        }

        return result;
    }
    //送出資料的button
    public void onClickButton(){

        Send = (Button)findViewById(R.id.button_Submit3);
        yearTpye = (RadioGroup)findViewById(R.id.Gruop_YearType_char);
        sexType = (RadioGroup)findViewById(R.id.Gruop_Sex3);

        // 時辰
        spinner = (Spinner)findViewById(R.id.spinner_char);
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

        // 年
        spinner_year = (Spinner)findViewById(R.id.spinner_year_char);
        listAdapter_year = new ArrayAdapter<String>(this, R.layout.myspinner, year_list);
        listAdapter_year.setDropDownViewResource(R.layout.myspinner);
        spinner_year.setAdapter(listAdapter_year);
        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                s_year = year_list[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        // 月
        spinner_month = (Spinner)findViewById(R.id.spinner_month_char);
        listAdapter_month = new ArrayAdapter<String>(this, R.layout.myspinner, month_list);
        listAdapter_month.setDropDownViewResource(R.layout.myspinner);
        spinner_month.setAdapter(listAdapter_month);
        spinner_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                s_month =month_list[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        // 日
        spinner_day = (Spinner)findViewById(R.id.spinner_day_char);
        listAdapter_day = new ArrayAdapter<String>(this, R.layout.myspinner, day_list);
        listAdapter_day.setDropDownViewResource(R.layout.myspinner);
        spinner_day.setAdapter(listAdapter_day);
        spinner_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                s_day = day_list[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 找性別ID，經過ID判斷是男是女
                if (isNetwork()) {
                    int select_year_type = yearTpye.getCheckedRadioButtonId();
                    int select_sex_tpye = sexType.getCheckedRadioButtonId();

                    west = (RadioButton) findViewById(R.id.radio_west_char);
                    guo = (RadioButton) findViewById(R.id.radio_guo_char);
                    non = (RadioButton) findViewById(R.id.radio_non_char);

                    male = (RadioButton) findViewById(R.id.radio_Male_char);
                    female = (RadioButton) findViewById(R.id.radio_Female_char);

                    if (select_year_type == west.getId()) {
                        s_yearTpye = "0";
                    } else if (select_year_type == guo.getId()) {
                        s_yearTpye = "1";
                    } else {
                        s_yearTpye = "2";
                    }

                    if (select_sex_tpye == female.getId()) {
                        s_sexTpye = "0";
                    } else {
                        s_sexTpye = "1";
                    }

                    String s_hour = hour;


                    String url = Catch_say88_API_info(s_yearTpye, s_year, s_month, s_day, s_hour, s_sexTpye);

                    RequestTask request = new RequestTask();
                    request.execute(url);
                } else {
                    notNetwork_dialogFragment editNameDialog = new notNetwork_dialogFragment();
                    editNameDialog.show(getFragmentManager(), "EditNameDialog");
                }
            }
        });

    }

    public String Catch_say88_API_info(String birthType,String Year,String Month , String Day ,String Hour,String Sex){
        // token就是識別證
        String url = "http://newtest.88say.com/Api/product/Unit374.aspx?";
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
            Intent intent = new Intent();
            intent.setClass(EightWordMinpanActivity.this, EightWordMinpanCatchActivity.class);
            intent.putExtras(bundle_charater);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... result){

        }
    }

    public void JsonTrasnfer_output(String jsonInput) {
        String TxnCode="";
        String TxnMsg="";
        String Result_Sex="";
        String Result_Age="";
        String Result_Birth="";
        String Result_LunarBirth="";

        String all="";

        String Result_GanZhi_Gan1="";
        String Result_GanZhi_GanInfo1="";
        String Result_GanZhi_Zhi1="";
        int count1;

        String Result_GanZhi_Gan2="";
        String Result_GanZhi_GanInfo2="";
        String Result_GanZhi_Zhi2="";
        int count2;

        String Result_GanZhi_Gan3="";
        String Result_GanZhi_GanInfo3="";
        String Result_GanZhi_Zhi3="";
        int count3;

        String Result_GanZhi_Gan4="";
        String Result_GanZhi_GanInfo4="";
        String Result_GanZhi_Zhi4="";
        int count4;

        String[] Result_GanZhi_ZhiInfo1= new String[5];
        String[] Result_GanZhi_ZhiInfo2= new String[5];
        String[] Result_GanZhi_ZhiInfo3= new String[5];
        String[] Result_GanZhi_ZhiInfo4= new String[5];

        String Result_Decade_GanZhi_Gan1="";
        String Result_Decade_GanZhi_GanInfo1="";
        String Result_Decade_GanZhi_Zhi1="";
        int count_de_1;

        String Result_Decade_GanZhi_Gan2="";
        String Result_Decade_GanZhi_GanInfo2="";
        String Result_Decade_GanZhi_Zhi2="";
        int count_de_2;

        String Result_Decade_GanZhi_Gan3="";
        String Result_Decade_GanZhi_GanInfo3="";
        String Result_Decade_GanZhi_Zhi3="";
        int count_de_3;

        String Result_Decade_GanZhi_Gan4="";
        String Result_Decade_GanZhi_GanInfo4="";
        String Result_Decade_GanZhi_Zhi4="";
        int count_de_4;

        String Result_Decade_GanZhi_Gan5="";
        String Result_Decade_GanZhi_GanInfo5="";
        String Result_Decade_GanZhi_Zhi5="";
        int count_de_5;

        String Result_Decade_GanZhi_Gan6="";
        String Result_Decade_GanZhi_GanInfo6="";
        String Result_Decade_GanZhi_Zhi6="";
        int count_de_6;

        String Result_Decade_GanZhi_Gan7="";
        String Result_Decade_GanZhi_GanInfo7="";
        String Result_Decade_GanZhi_Zhi7="";
        int count_de_7;

        String Result_Decade_GanZhi_Gan8="";
        String Result_Decade_GanZhi_GanInfo8="";
        String Result_Decade_GanZhi_Zhi8="";
        int count_de_8;

        String Result_Decade_GanZhi_Gan9="";
        String Result_Decade_GanZhi_GanInfo9="";
        String Result_Decade_GanZhi_Zhi9="";
        int count_de_9;

        String[] Result_Decade_GanZhi_ZhiInfo1= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo2= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo3= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo4= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo5= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo6= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo7= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo8= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo9= new String[5];


        String Result_SolarTerm_Name1 = "";
        String Result_SolarTerm_Date1 = "";
        String Result_SolarTerm_Diff1 = "";

        String Result_SolarTerm_Name2 = "";
        String Result_SolarTerm_Date2 = "";
        String Result_SolarTerm_Diff2 = "";

        try{
            TxnCode = new JSONObject(jsonInput).getString("TxnCode");
            TxnMsg = new JSONObject(jsonInput).getString("TxnMsg");
            Result_Sex =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Sex");
            Result_Age =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Age");
            Result_Birth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Birth");
            Result_LunarBirth =new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("LunarBirth");
            all = Result_Sex +" " + Result_Age + " " + Result_Birth + " "+Result_LunarBirth +"\n";
            bundle_charater.putString("Result_Sex",Result_Sex);
            bundle_charater.putString("Result_Age", Result_Age);
            bundle_charater.putString("Result_Birth",Result_Birth);
            bundle_charater.putString("Result_LunarBirth",Result_LunarBirth);

            Result_GanZhi_Gan1 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(0).getString("Gan");
            Result_GanZhi_GanInfo1 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(0).getString("GanInfo");
            Result_GanZhi_Zhi1  = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(0).getString("Zhi");
            count1 = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(0).getString("ZhiInfo")).length();
            all = all +  Result_GanZhi_Gan1 + " " +  Result_GanZhi_GanInfo1 + " " + Result_GanZhi_Zhi1 + " ";
            bundle_charater.putString("Result_GanZhi_Gan1",Result_GanZhi_Gan1);
            bundle_charater.putString("Result_GanZhi_GanInfo1",Result_GanZhi_GanInfo1);
            bundle_charater.putString("Result_GanZhi_Zhi1", Result_GanZhi_Zhi1);
            bundle_charater.putInt("count1", count1);

            all += "\n";
            Result_GanZhi_Gan2 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(1).getString("Gan");
            Result_GanZhi_GanInfo2 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(1).getString("GanInfo");
            Result_GanZhi_Zhi2  = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(1).getString("Zhi");
            count2 = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(1).getString("ZhiInfo")).length();
            all = all +  Result_GanZhi_Gan2 + " " +  Result_GanZhi_GanInfo2 + " " + Result_GanZhi_Zhi2 + " ";
            bundle_charater.putString("Result_GanZhi_Gan2",Result_GanZhi_Gan2);
            bundle_charater.putString("Result_GanZhi_GanInfo2",Result_GanZhi_GanInfo2);
            bundle_charater.putString("Result_GanZhi_Zhi2", Result_GanZhi_Zhi2);
            bundle_charater.putInt("count2", count2);

            all += "\n";
            Result_GanZhi_Gan3 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(2).getString("Gan");
            Result_GanZhi_GanInfo3 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(2).getString("GanInfo");
            Result_GanZhi_Zhi3  = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(2).getString("Zhi");
            count3 = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(2).getString("ZhiInfo")).length();
            all = all +  Result_GanZhi_Gan3 + " " +  Result_GanZhi_GanInfo3 + " " + Result_GanZhi_Zhi3 + " ";
            bundle_charater.putString("Result_GanZhi_Gan3",Result_GanZhi_Gan3);
            bundle_charater.putString("Result_GanZhi_GanInfo3",Result_GanZhi_GanInfo3);
            bundle_charater.putString("Result_GanZhi_Zhi3", Result_GanZhi_Zhi3);
            bundle_charater.putInt("count3", count3);

            all += "\n";
            Result_GanZhi_Gan4 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(3).getString("Gan");
            Result_GanZhi_GanInfo4 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(3).getString("GanInfo");
            Result_GanZhi_Zhi4  = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(3).getString("Zhi");
            count4 = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(3).getString("ZhiInfo")).length();
            all = all +  Result_GanZhi_Gan4 + " " +  Result_GanZhi_GanInfo4 + " " + Result_GanZhi_Zhi4 + " ";
            bundle_charater.putString("Result_GanZhi_Gan4",Result_GanZhi_Gan4);
            bundle_charater.putString("Result_GanZhi_GanInfo4",Result_GanZhi_GanInfo4);
            bundle_charater.putString("Result_GanZhi_Zhi4", Result_GanZhi_Zhi4);
            bundle_charater.putInt("count4", count4);

            all += "\n";
            all += "\n";

            for(int i=0;i<count1;i++){
                Result_GanZhi_ZhiInfo1[i] = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(0).getString("ZhiInfo")).getString(i);
                all = all + Result_GanZhi_ZhiInfo1[i] + " ";
                bundle_charater.putString("Result_GanZhi_Gan1_"+Integer.toString(i),Result_GanZhi_ZhiInfo1[i]);
            }
            all += "\n";
            for(int i=0;i<count2;i++){
                Result_GanZhi_ZhiInfo2[i] = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(1).getString("ZhiInfo")).getString(i);
                all = all + Result_GanZhi_ZhiInfo2[i] + " ";
                bundle_charater.putString("Result_GanZhi_Gan2_"+Integer.toString(i),Result_GanZhi_ZhiInfo2[i]);
            }
            all += "\n";
            for(int i=0;i<count3;i++){
                Result_GanZhi_ZhiInfo3[i] = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(2).getString("ZhiInfo")).getString(i);
                all = all + Result_GanZhi_ZhiInfo3[i] + " ";
                bundle_charater.putString("Result_GanZhi_Gan3_"+Integer.toString(i),Result_GanZhi_ZhiInfo3[i]);
            }
            all += "\n";
            for(int i=0;i<count4;i++){
                Result_GanZhi_ZhiInfo4[i] = new JSONArray(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("GanZhi")).getJSONObject(3).getString("ZhiInfo")).getString(i);
                all = all + Result_GanZhi_ZhiInfo4[i] + " ";
                bundle_charater.putString("Result_GanZhi_Gan4_"+Integer.toString(i),Result_GanZhi_ZhiInfo4[i]);
            }
            all += "\n";


            Result_Decade_GanZhi_Gan1 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(0).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo1 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(0).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi1 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(0).getString("GanZhi")).getString("Zhi");
            count_de_1 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(0).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan1 + " " +  Result_Decade_GanZhi_GanInfo1 + " " + Result_Decade_GanZhi_Zhi1 + " "+ Integer.toString(count_de_1);
            bundle_charater.putString("Result_Decade_GanZhi_Gan1",Result_Decade_GanZhi_Gan1);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo1",Result_Decade_GanZhi_GanInfo1);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi1", Result_Decade_GanZhi_Zhi1);
            bundle_charater.putInt("count_de_1", count_de_1);
            all += "\n";

            Result_Decade_GanZhi_Gan2 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(1).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo2 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(1).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi2 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(1).getString("GanZhi")).getString("Zhi");
            count_de_2 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(1).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan2 + " " +  Result_Decade_GanZhi_GanInfo2 + " " + Result_Decade_GanZhi_Zhi2 + " "+ Integer.toString(count_de_2);
            bundle_charater.putString("Result_Decade_GanZhi_Gan2",Result_Decade_GanZhi_Gan2);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo2",Result_Decade_GanZhi_GanInfo2);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi2", Result_Decade_GanZhi_Zhi2);
            bundle_charater.putInt("count_de_2", count_de_2);
            all += "\n";

            Result_Decade_GanZhi_Gan3 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(2).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo3 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(2).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi3 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(2).getString("GanZhi")).getString("Zhi");
            count_de_3 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(2).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan3 + " " +  Result_Decade_GanZhi_GanInfo3 + " " + Result_Decade_GanZhi_Zhi3 + " "+ Integer.toString(count_de_3);
            bundle_charater.putString("Result_Decade_GanZhi_Gan3",Result_Decade_GanZhi_Gan3);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo3",Result_Decade_GanZhi_GanInfo3);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi3", Result_Decade_GanZhi_Zhi3);
            bundle_charater.putInt("count_de_3", count_de_3);
            all += "\n";

            Result_Decade_GanZhi_Gan4 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(3).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo4 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(3).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi4 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(3).getString("GanZhi")).getString("Zhi");
            count_de_4 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(3).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan4 + " " +  Result_Decade_GanZhi_GanInfo4 + " " + Result_Decade_GanZhi_Zhi4 + " "+ Integer.toString(count_de_4);
            bundle_charater.putString("Result_Decade_GanZhi_Gan4",Result_Decade_GanZhi_Gan4);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo4",Result_Decade_GanZhi_GanInfo4);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi4", Result_Decade_GanZhi_Zhi4);
            bundle_charater.putInt("count_de_4", count_de_4);
            all += "\n";

            Result_Decade_GanZhi_Gan5 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(4).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo5 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(4).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi5 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(4).getString("GanZhi")).getString("Zhi");
            count_de_5 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(4).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan5 + " " +  Result_Decade_GanZhi_GanInfo5 + " " + Result_Decade_GanZhi_Zhi5 + " "+ Integer.toString(count_de_5);
            bundle_charater.putString("Result_Decade_GanZhi_Gan5",Result_Decade_GanZhi_Gan5);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo5",Result_Decade_GanZhi_GanInfo5);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi5", Result_Decade_GanZhi_Zhi5);
            bundle_charater.putInt("count_de_5", count_de_5);
            all += "\n";

            Result_Decade_GanZhi_Gan6 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(5).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo6 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(5).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi6 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(5).getString("GanZhi")).getString("Zhi");
            count_de_6 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(5).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan6 + " " +  Result_Decade_GanZhi_GanInfo6 + " " + Result_Decade_GanZhi_Zhi6 + " "+ Integer.toString(count_de_6);
            bundle_charater.putString("Result_Decade_GanZhi_Gan6",Result_Decade_GanZhi_Gan6);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo6",Result_Decade_GanZhi_GanInfo6);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi6", Result_Decade_GanZhi_Zhi6);
            bundle_charater.putInt("count_de_6", count_de_6);
            ;
            all += "\n";

            Result_Decade_GanZhi_Gan7 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(6).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo7 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(6).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi7 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(6).getString("GanZhi")).getString("Zhi");
            count_de_7 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(6).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan7 + " " +  Result_Decade_GanZhi_GanInfo7 + " " + Result_Decade_GanZhi_Zhi7 + " "+ Integer.toString(count_de_7);
            bundle_charater.putString("Result_Decade_GanZhi_Gan7",Result_Decade_GanZhi_Gan7);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo7",Result_Decade_GanZhi_GanInfo7);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi7", Result_Decade_GanZhi_Zhi7);
            bundle_charater.putInt("count_de_7", count_de_7);
            all += "\n";

            Result_Decade_GanZhi_Gan8 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(7).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo8 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(7).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi8 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(7).getString("GanZhi")).getString("Zhi");
            count_de_8 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(7).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan8 + " " +  Result_Decade_GanZhi_GanInfo8 + " " + Result_Decade_GanZhi_Zhi8 + " "+ Integer.toString(count_de_8);
            bundle_charater.putString("Result_Decade_GanZhi_Gan8",Result_Decade_GanZhi_Gan8);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo8",Result_Decade_GanZhi_GanInfo8);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi8", Result_Decade_GanZhi_Zhi8);
            bundle_charater.putInt("count_de_8", count_de_8);
            all += "\n";

            Result_Decade_GanZhi_Gan9= new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(8).getString("GanZhi")).getString("Gan");
            Result_Decade_GanZhi_GanInfo9 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(8).getString("GanZhi")).getString("GanInfo");
            Result_Decade_GanZhi_Zhi9 = new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(8).getString("GanZhi")).getString("Zhi");
            count_de_9 = new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(8).getString("GanZhi")).getString("ZhiInfo")).length();
            all = all +  Result_Decade_GanZhi_Gan9 + " " +  Result_Decade_GanZhi_GanInfo9 + " " + Result_Decade_GanZhi_Zhi9 + " "+ Integer.toString(count_de_9);
            bundle_charater.putString("Result_Decade_GanZhi_Gan9",Result_Decade_GanZhi_Gan9);
            bundle_charater.putString("Result_Decade_GanZhi_GanInfo9",Result_Decade_GanZhi_GanInfo9);
            bundle_charater.putString("Result_Decade_GanZhi_Zhi9", Result_Decade_GanZhi_Zhi9);
            bundle_charater.putInt("count_de_9", count_de_9);
            all += "\n";
            all += "\n";

            for(int i=0;i<count_de_1;i++){
                Result_Decade_GanZhi_ZhiInfo1[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(0).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo1[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo1_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo1[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_2;i++){
                Result_Decade_GanZhi_ZhiInfo2[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(1).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo2[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo2_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo2[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_3;i++){
                Result_Decade_GanZhi_ZhiInfo3[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(2).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo3[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo3_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo3[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_4;i++){
                Result_Decade_GanZhi_ZhiInfo4[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(3).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo4[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo4_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo4[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_5;i++){
                Result_Decade_GanZhi_ZhiInfo5[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(4).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo5[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo5_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo5[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_6;i++){
                Result_Decade_GanZhi_ZhiInfo6[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(5).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo6[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo6_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo6[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_7;i++){
                Result_Decade_GanZhi_ZhiInfo7[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(6).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo7[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo7_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo7[i]);
            }
            all += "\n";

            for(int i=0;i<count_de_8;i++){
                Result_Decade_GanZhi_ZhiInfo8[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(7).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo8[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo8_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo8[i]);
            }
            all += "\n";
            for(int i=0;i<count_de_9;i++){
                Result_Decade_GanZhi_ZhiInfo9[i] =new JSONArray(new JSONObject(new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("Decade")).getJSONObject(8).getString("GanZhi")).getString("ZhiInfo")).getString(i);
                all = all + Result_Decade_GanZhi_ZhiInfo9[i] + " ";
                bundle_charater.putString("Result_Decade_GanZhi_ZhiInfo9_"+Integer.toString(i),Result_Decade_GanZhi_ZhiInfo9[i]);
            }
            all += "\n";


            Result_SolarTerm_Name1 =new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(0).getString("Name");
            Result_SolarTerm_Date1 =new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(0).getString("Date");
            Result_SolarTerm_Diff1 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(0).getString("Diff");
            all = all + Result_SolarTerm_Name1  + " " +Result_SolarTerm_Date1 +" "+Result_SolarTerm_Diff1;
            bundle_charater.putString("Result_SolarTerm_Name1",Result_SolarTerm_Name1);
            bundle_charater.putString("Result_SolarTerm_Date1",Result_SolarTerm_Date1);
            bundle_charater.putString("Result_SolarTerm_Diff1",Result_SolarTerm_Diff1);

            Result_SolarTerm_Name2 =new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(1).getString("Name");
            Result_SolarTerm_Date2 =new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(1).getString("Date");
            Result_SolarTerm_Diff2 = new JSONArray(new JSONObject(new JSONObject(jsonInput).getString("Result")).getString("SolarTerm")).getJSONObject(1).getString("Diff");
            all = all + Result_SolarTerm_Name2  + " " +Result_SolarTerm_Date2 +" "+Result_SolarTerm_Diff2;
            bundle_charater.putString("Result_SolarTerm_Name2",Result_SolarTerm_Name2);
            bundle_charater.putString("Result_SolarTerm_Date2",Result_SolarTerm_Date2);
            bundle_charater.putString("Result_SolarTerm_Diff2", Result_SolarTerm_Diff2);



        }catch (JSONException e){
            e.printStackTrace();
        }


    }

}
