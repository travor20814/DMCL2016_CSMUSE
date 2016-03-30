package dmcl.csmuse2016;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TravorLee on 16/3/27.
 */
public class MemberActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> userDatas;
    private String[] collectDatas;
    private String Surname ="";
    private String Name ="";
    private String Email="";
    private String Password="";
    private String sex ="";
    private String yyyy ="";
    private String MM ="";
    private String dd ="";
    private String hh ="";
    private ProgressDialog pDialog;

    //connect getValues = new connect("1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle getExtra = getIntent().getExtras();
        if (getExtra != null){
            Email = getExtra.getString("mail");
        }

        setContentView(R.layout.member_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member);
        setSupportActionBar(toolbar);

        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("會員專區");
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

        new getDatas().execute();

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
                Toast.makeText(MemberActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    class getDatas extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            String command = "select * from Account where Account='" +Email + "'";
            String ans = new connect(command).getServerConnect();
            collectDatas = ans.toString().split("###");
            Log.v("getL", ans);
            if(ans.equals("Warning")){
                Log.v("get","get an error from server");
            }else{
                userDatas = new ItemSeparate(ans).getItem();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            TextView Surname = (TextView)findViewById(R.id.SurName_content);
            TextView Name = (TextView)findViewById(R.id.Name_content);
            TextView E_mail = (TextView)findViewById(R.id.mail_content);
            TextView Password = (TextView)findViewById(R.id.password_content);
            TextView Sex = (TextView)findViewById(R.id.sex_content);
            TextView yyyy = (TextView)findViewById(R.id.BirthYY);
            TextView MM = (TextView)findViewById(R.id.BirthMM);
            TextView DD = (TextView)findViewById(R.id.BirthDD);
            TextView TT = (TextView)findViewById(R.id.BirthTT);

            Surname.setText(collectDatas[0]);
            Name.setText(collectDatas[1]);
            E_mail.setText(collectDatas[2]);
            Password.setText(collectDatas[3]);
            
            if(collectDatas[4].equals("1")){
                Sex.setText("男");
            }
            else{
                Sex.setText("女");
            }

            yyyy.setText(collectDatas[6]);
            MM.setText(collectDatas[7]);
            DD.setText(collectDatas[8]);
            switch (collectDatas[9]){
                case "0":
                    TT.setText("00:00~00:59(子時)");
                    break;
                case "1":
                    TT.setText("1:00~1:59(丑時)");
                    break;
                case "2":
                    TT.setText("2:00~2:59(丑時)");
                    break;
                case "3":
                    TT.setText("3:00~3:59(寅時)");
                    break;
                case "4":
                    TT.setText("4:00~4:59(寅時)");
                    break;
                case "5":
                    TT.setText("5:00~5:59(卯時)");
                    break;
                case "6":
                    TT.setText("6:00~6:59(卯時)");
                    break;
                case "7":
                    TT.setText("7:00~7:59(辰時)");
                    break;
                case "8":
                    TT.setText("8:00~8:59(辰時)");
                    break;
                case "9":
                    TT.setText("9:00~9:59(巳時)");
                    break;
                case "10":
                    TT.setText("10:00~10:59(巳時)");
                    break;
                case "11":
                    TT.setText("11:00~11:59(午時)");
                    break;
                case "12":
                    TT.setText("12:00~12:59(午時)");
                    break;
                case "13":
                    TT.setText("13:00~13:59(未時)");
                    break;
                case "14":
                    TT.setText("14:00~14:59(未時)");
                    break;
                case "15":
                    TT.setText("15:00~15:59(申時)");
                    break;
                case "16":
                    TT.setText("16:00~16:59(申時)");
                    break;
                case "17":
                    TT.setText("17:00~17:59(酉時)");
                    break;
                case "18":
                    TT.setText("18:00~18:59(酉時)");
                    break;
                case "19":
                    TT.setText("19:00~19:59(戌時)");
                    break;
                case "20":
                    TT.setText("20:00~20:59(戌時)");
                    break;
                case "21":
                    TT.setText("21:00~21:59(亥時)");
                    break;
                case "22":
                    TT.setText("22:00~22:59(亥時)");
                    break;
                case "23":
                    TT.setText("23:00~23:59(子時)");
                    break;
                default:
                    TT.setText("-----Error-----");
                    break;
            }

            // Dismiss the progress dialog
            //	if (pDialog.isShowing())
            //		pDialog.dismiss();

        }

    }


}
