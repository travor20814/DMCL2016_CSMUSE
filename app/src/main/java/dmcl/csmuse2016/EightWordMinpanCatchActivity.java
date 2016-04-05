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
import org.json.JSONException;
import org.json.JSONObject;

//八字命盤的activity
public class EightWordMinpanCatchActivity extends AppCompatActivity {


    private final String filename="account.txt";
    private boolean loginornot;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eightwordcatch);

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_eightwordminpan);
      //  setSupportActionBar(toolbar);

        // App Logo
     //   toolbar.setLogo(R.mipmap.title02);
        // Title
     //   toolbar.setTitle("八字命盤");
     //   toolbar.setTitleTextColor(Color.BLACK);
        // Sub Title
     //   toolbar.setSubtitle("88Say幫您及時掌握未來");
     //   toolbar.setSubtitleTextColor(Color.BLACK);

     //   setSupportActionBar(toolbar);

        // Navigation Icon 要設定在 setSupoortActionBar 才有作用
        // 否則會出現 back bottom
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        // Menu item click 的監聽事件一樣要設定在 setSupportActionBar 才有作用
     //   toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //函式
        getInfo();
        ReturnButton();
        //加入fragment的函式
        //addFragment();
      //  loginornot = new Write_and_Read(filename,getFilesDir()).ifLogin();
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
                    String fromfile =  new Write_and_Read(filename,getFilesDir()).ReadFromFile();
                    String[] fromfileArray = fromfile.split("###");
                    Intent intentMember = new Intent(getApplicationContext(),MemberActivity.class);
                    intentMember.putExtra("mail", fromfileArray[2]); //send mail to next activity
                    EightWordMinpanCatchActivity.this.startActivity(intentMember);
                    finish();
                    break;
                case R.id.action_designer://製作群
                    msg+="designer clicked";
                    break;
                case R.id.action_logout://登出
                    new Write_and_Read(filename,getFilesDir()).WritetoFile_clear("");
                    tologin.setClass(EightWordMinpanCatchActivity.this,LoginActivity.class);
                    startActivity(tologin);
                    finish();
                    break;
                case R.id.action_login://訪客登入
                    tologin.setClass(EightWordMinpanCatchActivity.this,LoginActivity.class);
                    startActivity(tologin);
                    finish();
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(EightWordMinpanCatchActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public void getInfo() {
        Bundle info_bundle = this.getIntent().getExtras();
        String Reslut_Sex = info_bundle.getString("Result_Sex");
        String Reslut_Age = info_bundle.getString("Result_Age");
        String Reslut_Birth = info_bundle.getString("Result_Birth");
        String Reslut_LunarBirth = info_bundle.getString("Result_LunarBirth");

        TextView character_Birth = (TextView)findViewById(R.id.character_Birth);
        character_Birth.setText(Reslut_Birth);
        TextView character_LunarBirth = (TextView)findViewById(R.id.character_LunarBirth);
        character_LunarBirth.setText(Reslut_LunarBirth);
        TextView character_Sex = (TextView)findViewById(R.id.character_Sex);
        character_Sex.setText(Reslut_Sex );
        TextView character_Age = (TextView)findViewById(R.id.character_Age);
        character_Age.setText(Reslut_Age);

        String Result_GanZhi_Gan1 = info_bundle.getString("Result_GanZhi_Gan1");
        String Result_GanZhi_GanInfo1 = info_bundle.getString("Result_GanZhi_GanInfo1");
        String Result_GanZhi_Zhi1 = info_bundle.getString("Result_GanZhi_Zhi1");
        int count1 = info_bundle.getInt("count1");
        TextView GanInfo1 = (TextView)findViewById(R.id.GanInfo1);
        GanInfo1.setText(Result_GanZhi_GanInfo1);
        TextView GanZhi1 = (TextView)findViewById(R.id.GanZhi1);
        GanZhi1.setText(Result_GanZhi_Gan1+" "+Result_GanZhi_Zhi1);

        String Result_GanZhi_Gan2 = info_bundle.getString("Result_GanZhi_Gan2");
        String Result_GanZhi_GanInfo2 = info_bundle.getString("Result_GanZhi_GanInfo2");
        String Result_GanZhi_Zhi2 = info_bundle.getString("Result_GanZhi_Zhi2");
        int count2 = info_bundle.getInt("count2");
        TextView GanInfo2 = (TextView)findViewById(R.id.GanInfo2);
        GanInfo2.setText(Result_GanZhi_GanInfo2);
        TextView GanZhi2 = (TextView)findViewById(R.id.GanZhi2);
        GanZhi2.setText(Result_GanZhi_Gan2+" "+Result_GanZhi_Zhi2);

        String Result_GanZhi_Gan3 = info_bundle.getString("Result_GanZhi_Gan3");
        String Result_GanZhi_GanInfo3 = info_bundle.getString("Result_GanZhi_GanInfo3");
        String Result_GanZhi_Zhi3 = info_bundle.getString("Result_GanZhi_Zhi3");
        int count3 = info_bundle.getInt("count3");
        TextView GanInfo3 = (TextView)findViewById(R.id.GanInfo3);
        GanInfo3.setText(Result_GanZhi_GanInfo3);
        TextView GanZhi3 = (TextView)findViewById(R.id.GanZhi3);
        GanZhi3.setText(Result_GanZhi_Gan3+" "+Result_GanZhi_Zhi3);

        String Result_GanZhi_Gan4 = info_bundle.getString("Result_GanZhi_Gan4");
        String Result_GanZhi_GanInfo4 = info_bundle.getString("Result_GanZhi_GanInfo4");
        String Result_GanZhi_Zhi4 = info_bundle.getString("Result_GanZhi_Zhi4");
        int count4 = info_bundle.getInt("count4");
        TextView GanInfo4 = (TextView)findViewById(R.id.GanInfo4);
        GanInfo4.setText(Result_GanZhi_GanInfo4);
        TextView GanZhi4 = (TextView)findViewById(R.id.GanZhi4);
        GanZhi4.setText(Result_GanZhi_Gan4+" "+Result_GanZhi_Zhi4);

        String[] Result_GanZhi_ZhiInfo1= new String[5];
        String[] Result_GanZhi_ZhiInfo2= new String[5];
        String[] Result_GanZhi_ZhiInfo3= new String[5];
        String[] Result_GanZhi_ZhiInfo4= new String[5];

        String GanZhi_ZhiInfo1 = "";
        for(int i=0;i<count1;i++){
            Result_GanZhi_ZhiInfo1[i] = info_bundle.getString("Result_GanZhi_Gan1_"+Integer.toString(i));
            GanZhi_ZhiInfo1+= Result_GanZhi_ZhiInfo1[i];
            if(count1-1 != i){
                GanZhi_ZhiInfo1+=" ";
            }
        }
        TextView ZhiInfo1 = (TextView)findViewById(R.id.ZhiInfo1);
        ZhiInfo1.setText(GanZhi_ZhiInfo1);

        String GanZhi_ZhiInfo2 = "";
        for(int i=0;i<count2;i++){
            Result_GanZhi_ZhiInfo2[i] = info_bundle.getString("Result_GanZhi_Gan2_"+Integer.toString(i));
            GanZhi_ZhiInfo2+= Result_GanZhi_ZhiInfo2[i];
            if(count2-1 != i) {
                GanZhi_ZhiInfo2 +=" ";
            }
        }
        TextView ZhiInfo2 = (TextView)findViewById(R.id.ZhiInfo2);
        ZhiInfo2.setText(GanZhi_ZhiInfo2);

        String GanZhi_ZhiInfo3 = "";
        for(int i=0;i<count3;i++){
            Result_GanZhi_ZhiInfo3[i] = info_bundle.getString("Result_GanZhi_Gan3_"+Integer.toString(i));
            GanZhi_ZhiInfo3+= Result_GanZhi_ZhiInfo3[i];
            if(count3-1 != i){
                GanZhi_ZhiInfo3+=" ";
            }
        }
        TextView ZhiInfo3 = (TextView)findViewById(R.id.ZhiInfo3);
        ZhiInfo3.setText(GanZhi_ZhiInfo3);

        String GanZhi_ZhiInfo4 = "";
        for(int i=0;i<count4;i++){
            Result_GanZhi_ZhiInfo4[i] = info_bundle.getString("Result_GanZhi_Gan4_"+Integer.toString(i));
            GanZhi_ZhiInfo4+= Result_GanZhi_ZhiInfo4[i];
            if(count4-1 != i){
                GanZhi_ZhiInfo4+=" ";
            }
        }
        TextView ZhiInfo4 = (TextView)findViewById(R.id.ZhiInfo4);
        ZhiInfo4.setText(GanZhi_ZhiInfo4);

        String[] Result_Decade_GanZhi_ZhiInfo1= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo2= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo3= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo4= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo5= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo6= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo7= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo8= new String[5];
        String[] Result_Decade_GanZhi_ZhiInfo9= new String[5];

        String  Result_Decade_GanZhi_Gan1 = info_bundle.getString("Result_Decade_GanZhi_Gan1");
        String Result_Decade_GanZhi_GanInfo1 = info_bundle.getString("Result_Decade_GanZhi_GanInfo1");
        String Result_Decade_GanZhi_Zhi1 = info_bundle.getString("Result_Decade_GanZhi_Zhi1");
        int count_de_1 = info_bundle.getInt("count_de_1");
        TextView D_GanInfo1 = (TextView)findViewById(R.id.D_GanInfo1);
        D_GanInfo1.setText(Result_Decade_GanZhi_GanInfo1);
        TextView D_GanZhi1 = (TextView)findViewById(R.id.D_GanZhi1);
        D_GanZhi1.setText(Result_Decade_GanZhi_Gan1 + " " +  Result_Decade_GanZhi_Zhi1);

        String  Result_Decade_GanZhi_Gan2 = info_bundle.getString("Result_Decade_GanZhi_Gan2");
        String Result_Decade_GanZhi_GanInfo2 = info_bundle.getString("Result_Decade_GanZhi_GanInfo2");
        String Result_Decade_GanZhi_Zhi2 = info_bundle.getString("Result_Decade_GanZhi_Zhi2");
        int count_de_2 = info_bundle.getInt("count_de_2");
        TextView D_GanInfo2 = (TextView)findViewById(R.id.D_GanInfo2);
        D_GanInfo2.setText(Result_Decade_GanZhi_GanInfo2);
        TextView D_GanZhi2 = (TextView)findViewById(R.id.D_GanZhi2);
        D_GanZhi2.setText(Result_Decade_GanZhi_Gan2 + " " +  Result_Decade_GanZhi_Zhi2);

        String  Result_Decade_GanZhi_Gan3 = info_bundle.getString("Result_Decade_GanZhi_Gan3");
        String Result_Decade_GanZhi_GanInfo3 = info_bundle.getString("Result_Decade_GanZhi_GanInfo3");
        String Result_Decade_GanZhi_Zhi3 = info_bundle.getString("Result_Decade_GanZhi_Zhi3");
        int count_de_3 = info_bundle.getInt("count_de_3");
        TextView D_GanInfo3 = (TextView)findViewById(R.id.D_GanInfo3);
        D_GanInfo3.setText(Result_Decade_GanZhi_GanInfo3);
        TextView D_GanZhi3 = (TextView)findViewById(R.id.D_GanZhi3);
        D_GanZhi3.setText(Result_Decade_GanZhi_Gan3 + " " +  Result_Decade_GanZhi_Zhi3);

        String  Result_Decade_GanZhi_Gan4 = info_bundle.getString("Result_Decade_GanZhi_Gan4");
        String Result_Decade_GanZhi_GanInfo4 = info_bundle.getString("Result_Decade_GanZhi_GanInfo4");
        String Result_Decade_GanZhi_Zhi4 = info_bundle.getString("Result_Decade_GanZhi_Zhi4");
        int count_de_4 = info_bundle.getInt("count_de_4");
        TextView D_GanInfo4 = (TextView)findViewById(R.id.D_GanInfo4);
        D_GanInfo4.setText(Result_Decade_GanZhi_GanInfo4);
        TextView D_GanZhi4 = (TextView)findViewById(R.id.D_GanZhi4);
        D_GanZhi4.setText(Result_Decade_GanZhi_Gan4 + " " +  Result_Decade_GanZhi_Zhi4);

        String  Result_Decade_GanZhi_Gan5 = info_bundle.getString("Result_Decade_GanZhi_Gan5");
        String Result_Decade_GanZhi_GanInfo5 = info_bundle.getString("Result_Decade_GanZhi_GanInfo5");
        String Result_Decade_GanZhi_Zhi5 = info_bundle.getString("Result_Decade_GanZhi_Zhi5");
        int count_de_5 = info_bundle.getInt("count_de_5");
        TextView D_GanInfo5 = (TextView)findViewById(R.id.D_GanInfo5);
        D_GanInfo5.setText(Result_Decade_GanZhi_GanInfo5);
        TextView D_GanZhi5 = (TextView)findViewById(R.id.D_GanZhi5);
        D_GanZhi5.setText(Result_Decade_GanZhi_Gan5 + " " +  Result_Decade_GanZhi_Zhi5);

        String  Result_Decade_GanZhi_Gan6 = info_bundle.getString("Result_Decade_GanZhi_Gan6");
        String Result_Decade_GanZhi_GanInfo6 = info_bundle.getString("Result_Decade_GanZhi_GanInfo6");
        String Result_Decade_GanZhi_Zhi6 = info_bundle.getString("Result_Decade_GanZhi_Zhi6");
        int count_de_6 = info_bundle.getInt("count_de_6");
        TextView D_GanInfo6 = (TextView)findViewById(R.id.D_GanInfo6);
        D_GanInfo6.setText(Result_Decade_GanZhi_GanInfo6);
        TextView D_GanZhi6 = (TextView)findViewById(R.id.D_GanZhi6);
        D_GanZhi6.setText(Result_Decade_GanZhi_Gan6 + " " +  Result_Decade_GanZhi_Zhi6);

        String  Result_Decade_GanZhi_Gan7 = info_bundle.getString("Result_Decade_GanZhi_Gan7");
        String Result_Decade_GanZhi_GanInfo7 = info_bundle.getString("Result_Decade_GanZhi_GanInfo7");
        String Result_Decade_GanZhi_Zhi7 = info_bundle.getString("Result_Decade_GanZhi_Zhi7");
        int count_de_7 = info_bundle.getInt("count_de_7");
        TextView D_GanInfo7 = (TextView)findViewById(R.id.D_GanInfo7);
        D_GanInfo7.setText(Result_Decade_GanZhi_GanInfo7);
        TextView D_GanZhi7 = (TextView)findViewById(R.id.D_GanZhi7);
        D_GanZhi7.setText(Result_Decade_GanZhi_Gan7 + " " +  Result_Decade_GanZhi_Zhi7);

        String  Result_Decade_GanZhi_Gan8 = info_bundle.getString("Result_Decade_GanZhi_Gan8");
        String Result_Decade_GanZhi_GanInfo8 = info_bundle.getString("Result_Decade_GanZhi_GanInfo8");
        String Result_Decade_GanZhi_Zhi8 = info_bundle.getString("Result_Decade_GanZhi_Zhi8");
        int count_de_8 = info_bundle.getInt("count_de_8");
        TextView D_GanInfo8 = (TextView)findViewById(R.id.D_GanInfo8);
        D_GanInfo8.setText(Result_Decade_GanZhi_GanInfo8);
        TextView D_GanZhi8 = (TextView)findViewById(R.id.D_GanZhi8);
        D_GanZhi8.setText(Result_Decade_GanZhi_Gan8 + " " +  Result_Decade_GanZhi_Zhi8);


        String  Result_Decade_GanZhi_Gan9 = info_bundle.getString("Result_Decade_GanZhi_Gan9");
        String Result_Decade_GanZhi_GanInfo9 = info_bundle.getString("Result_Decade_GanZhi_GanInfo9");
        String Result_Decade_GanZhi_Zhi9 = info_bundle.getString("Result_Decade_GanZhi_Zhi9");
        int count_de_9 = info_bundle.getInt("count_de_9");
        TextView D_GanInfo9 = (TextView)findViewById(R.id.D_GanInfo9);
        D_GanInfo9.setText(Result_Decade_GanZhi_GanInfo9);
        TextView D_GanZhi9 = (TextView)findViewById(R.id.D_GanZhi9);
        D_GanZhi9.setText(Result_Decade_GanZhi_Gan9 + " " +  Result_Decade_GanZhi_Zhi9);


        String Decade_ZhiInfo1="";
        for(int i=0;i<count_de_1;i++){
            Result_Decade_GanZhi_ZhiInfo1[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo1_"+Integer.toString(i));
            Decade_ZhiInfo1+=Result_Decade_GanZhi_ZhiInfo1[i];
            if(i!=count_de_1-1){
                Decade_ZhiInfo1+="\n";
            }
        }
        TextView D_ZhiInfo1 = (TextView)findViewById(R.id.D_ZhiInfo1);
        D_ZhiInfo1.setText(Decade_ZhiInfo1);

        String Decade_ZhiInfo2="";
        for(int i=0;i<count_de_2;i++){
            Result_Decade_GanZhi_ZhiInfo2[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo2_"+Integer.toString(i));
            Decade_ZhiInfo2+=Result_Decade_GanZhi_ZhiInfo2[i];
            if(i!=count_de_2-1){
                Decade_ZhiInfo2+="\n";
            }
        }
        TextView D_ZhiInfo2 = (TextView)findViewById(R.id.D_ZhiInfo2);
        D_ZhiInfo2.setText(Decade_ZhiInfo2);

        String Decade_ZhiInfo3="";
        for(int i=0;i<count_de_3;i++){
            Result_Decade_GanZhi_ZhiInfo3[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo3_"+Integer.toString(i));
            Decade_ZhiInfo3+=Result_Decade_GanZhi_ZhiInfo3[i];
            if(i!=count_de_3-1){
                Decade_ZhiInfo3+="\n";
            }
        }
        TextView D_ZhiInfo3 = (TextView)findViewById(R.id.D_ZhiInfo3);
        D_ZhiInfo3.setText(Decade_ZhiInfo3);

        String Decade_ZhiInfo4="";
        for(int i=0;i<count_de_4;i++){
            Result_Decade_GanZhi_ZhiInfo4[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo4_"+Integer.toString(i));
            Decade_ZhiInfo4+=Result_Decade_GanZhi_ZhiInfo4[i];
            if(i!=count_de_4-1){
                Decade_ZhiInfo4+="\n";
            }
        }
        TextView D_ZhiInfo4 = (TextView)findViewById(R.id.D_ZhiInfo4);
        D_ZhiInfo4.setText(Decade_ZhiInfo4);

        String Decade_ZhiInfo5="";
        for(int i=0;i<count_de_5;i++){
            Result_Decade_GanZhi_ZhiInfo5[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo5_"+Integer.toString(i));
            Decade_ZhiInfo5+=Result_Decade_GanZhi_ZhiInfo5[i];
            if(i!=count_de_5-1){
                Decade_ZhiInfo5+="\n";
            }
        }
        TextView D_ZhiInfo5 = (TextView)findViewById(R.id.D_ZhiInfo5);
        D_ZhiInfo5.setText(Decade_ZhiInfo5);

        String Decade_ZhiInfo6="";
        for(int i=0;i<count_de_6;i++) {
            Result_Decade_GanZhi_ZhiInfo6[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo6_"+Integer.toString(i));
            Decade_ZhiInfo6+=Result_Decade_GanZhi_ZhiInfo6[i];
            if(i!=count_de_6-1){
                Decade_ZhiInfo6+="\n";
            }
        }
        TextView D_ZhiInfo6 = (TextView)findViewById(R.id.D_ZhiInfo6);
        D_ZhiInfo6.setText(Decade_ZhiInfo6);

        String Decade_ZhiInfo7="";
        for(int i=0;i<count_de_7;i++){
            Result_Decade_GanZhi_ZhiInfo7[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo7_"+Integer.toString(i));
            Decade_ZhiInfo7+=Result_Decade_GanZhi_ZhiInfo7[i];
            if(i!=count_de_7-1){
                Decade_ZhiInfo7+="\n";
            }
        }
        TextView D_ZhiInfo7 = (TextView)findViewById(R.id.D_ZhiInfo7);
        D_ZhiInfo7.setText(Decade_ZhiInfo7);

        String Decade_ZhiInfo8="";
        for(int i=0;i<count_de_8;i++){
            Result_Decade_GanZhi_ZhiInfo8[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo8_"+Integer.toString(i));
            Decade_ZhiInfo8+=Result_Decade_GanZhi_ZhiInfo8[i];
            if(i!=count_de_8-1){
                Decade_ZhiInfo8+="\n";
            }
        }
        TextView D_ZhiInfo8 = (TextView)findViewById(R.id.D_ZhiInfo8);
        D_ZhiInfo8.setText(Decade_ZhiInfo8);

        String Decade_ZhiInfo9="";
        for(int i=0;i<count_de_9;i++){
            Result_Decade_GanZhi_ZhiInfo9[i] = info_bundle.getString("Result_Decade_GanZhi_ZhiInfo9_"+Integer.toString(i));
            Decade_ZhiInfo9+=Result_Decade_GanZhi_ZhiInfo9[i];
            if(i!=count_de_9-1){
                Decade_ZhiInfo9+="\n";
            }
        }
        TextView D_ZhiInfo9 = (TextView)findViewById(R.id.D_ZhiInfo9);
        D_ZhiInfo9.setText(Decade_ZhiInfo9);

        String Result_SolarTerm_Name1 = info_bundle.getString("Result_SolarTerm_Name1");
        String Result_SolarTerm_Date1 = info_bundle.getString("Result_SolarTerm_Date1");
        String Result_SolarTerm_Diff1 = info_bundle.getString("Result_SolarTerm_Diff1");
        TextView SolarName1 = (TextView)findViewById(R.id.SolarName1);
        SolarName1.setText(Result_SolarTerm_Name1);
        TextView SolarDate1 = (TextView)findViewById(R.id.SolarDate1);
        SolarDate1.setText(Result_SolarTerm_Date1);
        TextView SolarDiff1 = (TextView)findViewById(R.id.SolarDiff1);
        SolarDiff1.setText(Result_SolarTerm_Diff1+"天");

        String Result_SolarTerm_Name2 = info_bundle.getString("Result_SolarTerm_Name2");
        String Result_SolarTerm_Date2 = info_bundle.getString("Result_SolarTerm_Date2");
        String Result_SolarTerm_Diff2 = info_bundle.getString("Result_SolarTerm_Diff2");
        TextView SolarName2 = (TextView)findViewById(R.id.SolarName2);
        SolarName2.setText(Result_SolarTerm_Name2);
        TextView SolarDate2 = (TextView)findViewById(R.id.SolarDate2);
        SolarDate2.setText(Result_SolarTerm_Date2);
        TextView SolarDiff2 = (TextView)findViewById(R.id.SolarDiff2);
        SolarDiff2.setText(Result_SolarTerm_Diff2+"天");

    }
    public void ReturnButton(){
        Button ReturnButton = (Button)findViewById(R.id.button2);
        ReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                // intent.setClass(CatchActivity.this, MainActivity.class);
                // startActivity(intent);
                EightWordMinpanCatchActivity.this.finish();
            }
        });

    }
}
