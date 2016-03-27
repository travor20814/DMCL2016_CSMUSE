package dmcl.csmuse2016;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Boyu on 2016/3/27.
 */
public class MinpanCatchActivity  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minpan_catch);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_catch);
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

        getInfo();
        ReturnButton();
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
                    Intent intent = new Intent(MinpanCatchActivity.this,HomePageActivity.class);
                    MinpanCatchActivity.this.startActivity(intent);
                    finish();

                    break;
                case R.id.action_settings: //setting鍵
                    msg += "Click setting";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MinpanCatchActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public void getInfo() {
        Bundle info_bundle = this.getIntent().getExtras();
        String Reslut_Sex = info_bundle.getString("Reslut_Sex");
        String Reslut_Age = info_bundle.getString("Reslut_Age");
        String Reslut_Birth = info_bundle.getString("Reslut_Birth");
        String Reslut_LunarBirth = info_bundle.getString("Reslut_LunarBirth");
        String Reslut_FateManner = info_bundle.getString("Reslut_FateManner");
        String Reslut_FateSex = info_bundle.getString("Reslut_FateSex");

        TextView Sex_Age = (TextView)findViewById(R.id.Sex_Age);
        Sex_Age.setText(Reslut_Sex+" "+Reslut_Age+"歲"+"\n"+Reslut_Birth);
        TextView Birth = (TextView)findViewById(R.id.Birth);
        Birth.setText(Reslut_LunarBirth);
        TextView FateManner = (TextView)findViewById(R.id.Fate_Manner);
        FateManner.setText(Reslut_FateManner);
        TextView FateSex = (TextView)findViewById(R.id.Fate_Sex);
        FateSex.setText(Reslut_FateSex);

        //all tpye of onfo
        String Reslut_Houses_Name1 = info_bundle.getString("Reslut_Houses_Name1");
        String Reslut_Houses_Decade1 = info_bundle.getString("Reslut_Houses_Decade1");
        String Reslut_Houses_GanZhi1 = info_bundle.getString("Reslut_Houses_GanZhi1");
        TextView Name_Decade1 = (TextView)findViewById(R.id.name_decade1);
        Name_Decade1.setText(Reslut_Houses_Name1+"\n"+Reslut_Houses_Decade1);
        TextView GanZhi1 = (TextView)findViewById(R.id.GanZhi1);
        GanZhi1.setText(Reslut_Houses_GanZhi1);

        String Reslut_Houses_Name2 = info_bundle.getString("Reslut_Houses_Name2");
        String Reslut_Houses_Decade2 = info_bundle.getString("Reslut_Houses_Decade2");
        String Reslut_Houses_GanZhi2 = info_bundle.getString("Reslut_Houses_GanZhi2");
        TextView Name_Decade2 = (TextView)findViewById(R.id.name_decade2);
        Name_Decade2.setText(Reslut_Houses_Name2+"\n"+Reslut_Houses_Decade2);
        TextView GanZhi2 = (TextView)findViewById(R.id.GanZhi2);
        GanZhi2.setText(Reslut_Houses_GanZhi2);

        String Reslut_Houses_Name3 = info_bundle.getString("Reslut_Houses_Name3");
        String Reslut_Houses_Decade3 = info_bundle.getString("Reslut_Houses_Decade3");
        String Reslut_Houses_GanZhi3 = info_bundle.getString("Reslut_Houses_GanZhi3");
        TextView Name_Decade3 = (TextView)findViewById(R.id.name_decade3);
        Name_Decade3.setText(Reslut_Houses_Name3+"\n"+Reslut_Houses_Decade3);
        TextView GanZhi3 = (TextView)findViewById(R.id.GanZhi3);
        GanZhi3.setText(Reslut_Houses_GanZhi3);

        String Reslut_Houses_Name4 = info_bundle.getString("Reslut_Houses_Name4");
        String Reslut_Houses_Decade4 = info_bundle.getString("Reslut_Houses_Decade4");
        String Reslut_Houses_GanZhi4 = info_bundle.getString("Reslut_Houses_GanZhi4");
        TextView Name_Decade4 = (TextView)findViewById(R.id.name_decade4);
        Name_Decade4.setText(Reslut_Houses_Name4+"\n"+Reslut_Houses_Decade4);
        TextView GanZhi4 = (TextView)findViewById(R.id.GanZhi4);
        GanZhi4.setText(Reslut_Houses_GanZhi4);

        String Reslut_Houses_Name5 = info_bundle.getString("Reslut_Houses_Name5");
        String Reslut_Houses_Decade5 = info_bundle.getString("Reslut_Houses_Decade5");
        String Reslut_Houses_GanZhi5 = info_bundle.getString("Reslut_Houses_GanZhi5");
        TextView Name_Decade5 = (TextView)findViewById(R.id.name_decade5);
        Name_Decade5.setText(Reslut_Houses_Name5+"\n"+Reslut_Houses_Decade5);
        TextView GanZhi5 = (TextView)findViewById(R.id.GanZhi5);
        GanZhi5.setText(Reslut_Houses_GanZhi5);

        String Reslut_Houses_Name6 = info_bundle.getString("Reslut_Houses_Name6");
        String Reslut_Houses_Decade6 = info_bundle.getString("Reslut_Houses_Decade6");
        String Reslut_Houses_GanZhi6 = info_bundle.getString("Reslut_Houses_GanZhi6");
        TextView Name_Decade6 = (TextView)findViewById(R.id.name_decade6);
        Name_Decade6.setText(Reslut_Houses_Name6+"\n"+Reslut_Houses_Decade6);
        TextView GanZhi6 = (TextView)findViewById(R.id.GanZhi6);
        GanZhi6.setText(Reslut_Houses_GanZhi6);

        String Reslut_Houses_Name7 = info_bundle.getString("Reslut_Houses_Name7");
        String Reslut_Houses_Decade7 = info_bundle.getString("Reslut_Houses_Decade7");
        String Reslut_Houses_GanZhi7 = info_bundle.getString("Reslut_Houses_GanZhi7");
        TextView Name_Decade7 = (TextView)findViewById(R.id.name_decade7);
        Name_Decade7.setText(Reslut_Houses_Name7+"\n"+Reslut_Houses_Decade7);
        TextView GanZhi7 = (TextView)findViewById(R.id.GanZhi7);
        GanZhi7.setText(Reslut_Houses_GanZhi7);

        String Reslut_Houses_Name8 = info_bundle.getString("Reslut_Houses_Name8");
        String Reslut_Houses_Decade8 = info_bundle.getString("Reslut_Houses_Decade8");
        String Reslut_Houses_GanZhi8 = info_bundle.getString("Reslut_Houses_GanZhi8");
        TextView Name_Decade8 = (TextView)findViewById(R.id.name_decade8);
        Name_Decade8.setText(Reslut_Houses_Name8+"\n"+Reslut_Houses_Decade8);
        TextView GanZhi8 = (TextView)findViewById(R.id.GanZhi8);
        GanZhi8.setText(Reslut_Houses_GanZhi8);

        String Reslut_Houses_Name9 = info_bundle.getString("Reslut_Houses_Name9");
        String Reslut_Houses_Decade9 = info_bundle.getString("Reslut_Houses_Decade9");
        String Reslut_Houses_GanZhi9 = info_bundle.getString("Reslut_Houses_GanZhi9");
        TextView Name_Decade9 = (TextView)findViewById(R.id.name_decade9);
        Name_Decade9.setText(Reslut_Houses_Name9+"\n"+Reslut_Houses_Decade9);
        TextView GanZhi9 = (TextView)findViewById(R.id.GanZhi9);
        GanZhi9.setText(Reslut_Houses_GanZhi9);

        String Reslut_Houses_Name10 = info_bundle.getString("Reslut_Houses_Name10");
        String Reslut_Houses_Decade10 = info_bundle.getString("Reslut_Houses_Decade10");
        String Reslut_Houses_GanZhi10 = info_bundle.getString("Reslut_Houses_GanZhi10");
        TextView Name_Decade10 = (TextView)findViewById(R.id.name_decade10);
        Name_Decade10.setText(Reslut_Houses_Name10+"\n"+Reslut_Houses_Decade10);
        TextView GanZhi10 = (TextView)findViewById(R.id.GanZhi10);
        GanZhi10.setText(Reslut_Houses_GanZhi10);

        String Reslut_Houses_Name11 = info_bundle.getString("Reslut_Houses_Name11");
        String Reslut_Houses_Decade11 = info_bundle.getString("Reslut_Houses_Decade11");
        String Reslut_Houses_GanZhi11 = info_bundle.getString("Reslut_Houses_GanZhi11");
        TextView Name_Decade11 = (TextView)findViewById(R.id.name_decade11);
        Name_Decade11.setText(Reslut_Houses_Name11+"\n"+Reslut_Houses_Decade11);
        TextView GanZhi11 = (TextView)findViewById(R.id.GanZhi11);
        GanZhi11.setText(Reslut_Houses_GanZhi11);

        String Reslut_Houses_Name12 = info_bundle.getString("Reslut_Houses_Name12");
        String Reslut_Houses_Decade12 = info_bundle.getString("Reslut_Houses_Decade12");
        String Reslut_Houses_GanZhi12 = info_bundle.getString("Reslut_Houses_GanZhi12");
        TextView Name_Decade12 = (TextView)findViewById(R.id.name_decade12);
        Name_Decade12.setText(Reslut_Houses_Name12+"\n"+Reslut_Houses_Decade12);
        TextView GanZhi12 = (TextView)findViewById(R.id.GanZhi12);
        GanZhi12.setText(Reslut_Houses_GanZhi12);

        // all type of count
        Integer count1 = info_bundle.getInt("count1");
        Integer count2 = info_bundle.getInt("count2");
        Integer count3 = info_bundle.getInt("count3");
        Integer count4 = info_bundle.getInt("count4");
        Integer count5 = info_bundle.getInt("count5");
        Integer count6 = info_bundle.getInt("count6");
        Integer count7 = info_bundle.getInt("count7");
        Integer count8 = info_bundle.getInt("count8");
        Integer count9 = info_bundle.getInt("count9");
        Integer count10 = info_bundle.getInt("count10");
        Integer count11 = info_bundle.getInt("count11");
        Integer count12 = info_bundle.getInt("count12");

        // all type of result
        String[] Reslut_Houses_Stars_Name1 = new String[6];
        String[] Reslut_Houses_Stars_Flag1 = new String[6];

        String[] Reslut_Houses_Stars_Name2 = new String[6];
        String[] Reslut_Houses_Stars_Flag2 = new String[6];

        String[] Reslut_Houses_Stars_Name3 = new String[6];
        String[] Reslut_Houses_Stars_Flag3 = new String[6];

        String[] Reslut_Houses_Stars_Name4 = new String[6];
        String[] Reslut_Houses_Stars_Flag4 = new String[6];

        String[] Reslut_Houses_Stars_Name5 = new String[6];
        String[] Reslut_Houses_Stars_Flag5 = new String[6];

        String[] Reslut_Houses_Stars_Name6 = new String[6];
        String[] Reslut_Houses_Stars_Flag6 = new String[6];

        String[] Reslut_Houses_Stars_Name7 = new String[6];
        String[] Reslut_Houses_Stars_Flag7 = new String[6];

        String[] Reslut_Houses_Stars_Name8 = new String[6];
        String[] Reslut_Houses_Stars_Flag8 = new String[6];

        String[] Reslut_Houses_Stars_Name9 = new String[6];
        String[] Reslut_Houses_Stars_Flag9 = new String[6];

        String[] Reslut_Houses_Stars_Name10 = new String[6];
        String[] Reslut_Houses_Stars_Flag10 = new String[6];

        String[] Reslut_Houses_Stars_Name11 = new String[6];
        String[] Reslut_Houses_Stars_Flag11 = new String[6];

        String[] Reslut_Houses_Stars_Name12 = new String[6];
        String[] Reslut_Houses_Stars_Flag12 = new String[6];

        String nu = "null";
        String star1="";
        for (int i = 0; i < count1; i++) {
            Reslut_Houses_Stars_Name1[i] = info_bundle.getString("Reslut_Houses_Stars_Name1" + Integer.toString(i));
            star1 += Reslut_Houses_Stars_Name1[i];
            Reslut_Houses_Stars_Flag1[i] = info_bundle.getString("Reslut_Houses_Stars_Flag1" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag1[i].equals(nu)){
                star1+=" ";
                star1+= Reslut_Houses_Stars_Flag1[i];
            }
            else{
                star1+="     ";
            }
            if(i!=count1-1){
                star1+="\n";
            }
        }
        TextView star_1 = (TextView)findViewById(R.id.typestar1);
        star_1.setText(star1);

        String star2="";
        for (int i = 0; i < count2; i++) {
            Reslut_Houses_Stars_Name2[i] = info_bundle.getString("Reslut_Houses_Stars_Name2" + Integer.toString(i));
            star2 += Reslut_Houses_Stars_Name2[i];
            Reslut_Houses_Stars_Flag2[i] = info_bundle.getString("Reslut_Houses_Stars_Flag2" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag2[i].equals(nu)){
                star2+=" ";
                star2+= Reslut_Houses_Stars_Flag2[i];
            }
            else{
                star2+="     ";
            }
            if(i!=count2-1){
                star2+="\n";
            }
        }
        TextView star_2 = (TextView)findViewById(R.id.typestar2);
        star_2.setText(star2);

        String star3="";
        for (int i = 0; i < count3; i++) {
            Reslut_Houses_Stars_Name3[i] = info_bundle.getString("Reslut_Houses_Stars_Name3" + Integer.toString(i));
            star3 += Reslut_Houses_Stars_Name3[i];
            Reslut_Houses_Stars_Flag3[i] = info_bundle.getString("Reslut_Houses_Stars_Flag3" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag3[i].equals(nu)){
                star3+=" ";
                star3+= Reslut_Houses_Stars_Flag3[i];
            }
            else{
                star3+="     ";
            }
            if(i!=count3-1){
                star3+="\n";
            }
        }
        TextView star_3 = (TextView)findViewById(R.id.typestar3);
        star_3.setText(star3);

        String star4="";
        for (int i = 0; i < count4; i++) {
            Reslut_Houses_Stars_Name4[i] = info_bundle.getString("Reslut_Houses_Stars_Name4" + Integer.toString(i));
            star4 += Reslut_Houses_Stars_Name4[i];
            Reslut_Houses_Stars_Flag4[i] = info_bundle.getString("Reslut_Houses_Stars_Flag4" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag4[i].equals(nu)){
                star4+=" ";
                star4+= Reslut_Houses_Stars_Flag4[i];
            }
            else{
                star4+="     ";
            }
            if(i!=count4-1){
                star4+="\n";
            }
        }
        TextView star_4 = (TextView)findViewById(R.id.typestar4);
        star_4.setText(star4);

        String star5="";
        for (int i = 0; i < count5; i++) {
            Reslut_Houses_Stars_Name5[i] = info_bundle.getString("Reslut_Houses_Stars_Name5" + Integer.toString(i));
            star5 += Reslut_Houses_Stars_Name5[i];
            Reslut_Houses_Stars_Flag5[i] = info_bundle.getString("Reslut_Houses_Stars_Flag5" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag5[i].equals(nu)){
                star5+=" ";
                star5+= Reslut_Houses_Stars_Flag5[i];
            }
            else{
                star5+="     ";
            }
            if(i!=count5-1){
                star5+="\n";
            }
        }
        TextView star_5 = (TextView)findViewById(R.id.typestar5);
        star_5.setText(star5);

        String star6="";
        for (int i = 0; i < count6; i++) {
            Reslut_Houses_Stars_Name6[i] = info_bundle.getString("Reslut_Houses_Stars_Name6" + Integer.toString(i));
            star6 += Reslut_Houses_Stars_Name6[i];
            Reslut_Houses_Stars_Flag6[i] = info_bundle.getString("Reslut_Houses_Stars_Flag6" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag6[i].equals(nu)){
                star6+=" ";
                star6+= Reslut_Houses_Stars_Flag6[i];
            }
            else{
                star6+="     ";
            }
            if(i!=count6-1){
                star6+="\n";
            }
        }
        TextView star_6 = (TextView)findViewById(R.id.typestar6);
        star_6.setText(star6);

        String star7="";
        for (int i = 0; i < count7; i++) {
            Reslut_Houses_Stars_Name7[i] = info_bundle.getString("Reslut_Houses_Stars_Name7" + Integer.toString(i));
            star7 += Reslut_Houses_Stars_Name7[i];
            Reslut_Houses_Stars_Flag7[i] = info_bundle.getString("Reslut_Houses_Stars_Flag7" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag7[i].equals(nu)){
                star7+=" ";
                star7+= Reslut_Houses_Stars_Flag7[i];
            }
            else{
                star7+="     ";
            }
            if(i!=count7-1){
                star7+="\n";
            }
        }
        TextView star_7 = (TextView)findViewById(R.id.typestar7);
        star_7.setText(star7);

        String star8="";
        for (int i = 0; i < count8; i++) {
            Reslut_Houses_Stars_Name8[i] = info_bundle.getString("Reslut_Houses_Stars_Name8" + Integer.toString(i));
            star8 += Reslut_Houses_Stars_Name8[i];
            Reslut_Houses_Stars_Flag8[i] = info_bundle.getString("Reslut_Houses_Stars_Flag8" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag8[i].equals(nu)){
                star8+=" ";
                star8+= Reslut_Houses_Stars_Flag8[i];
            }
            else{
                star8+="     ";
            }
            if(i!=count8-1){
                star8+="\n";
            }
        }
        TextView star_8 = (TextView)findViewById(R.id.typestar8);
        star_8.setText(star8);

        String star9="";
        for (int i = 0; i < count9; i++) {
            Reslut_Houses_Stars_Name9[i] = info_bundle.getString("Reslut_Houses_Stars_Name9" + Integer.toString(i));
            star9 += Reslut_Houses_Stars_Name9[i];
            Reslut_Houses_Stars_Flag9[i] = info_bundle.getString("Reslut_Houses_Stars_Flag9" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag9[i].equals(nu)){
                star9+=" ";
                star9+= Reslut_Houses_Stars_Flag9[i];
            }
            else{
                star9+="     ";
            }
            if(i!=count9-1){
                star9+="\n";
            }
        }
        TextView star_9 = (TextView)findViewById(R.id.typestar9);
        star_9.setText(star9);

        String star10="";
        for (int i = 0; i < count10; i++) {
            Reslut_Houses_Stars_Name10[i] = info_bundle.getString("Reslut_Houses_Stars_Name10" + Integer.toString(i));
            star10 += Reslut_Houses_Stars_Name10[i];
            Reslut_Houses_Stars_Flag10[i] = info_bundle.getString("Reslut_Houses_Stars_Flag10" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag10[i].equals(nu)){
                star10+=" ";
                star10+= Reslut_Houses_Stars_Flag10[i];
            }
            else{
                star10+="     ";
            }
            if(i!=count10-1){
                star10+="\n";
            }
        }
        TextView star_10 = (TextView)findViewById(R.id.typestar10);
        star_10.setText(star10);

        String star11="";
        for (int i = 0; i < count11; i++) {
            Reslut_Houses_Stars_Name11[i] = info_bundle.getString("Reslut_Houses_Stars_Name11" + Integer.toString(i));
            star11 += Reslut_Houses_Stars_Name11[i];
            Reslut_Houses_Stars_Flag11[i] = info_bundle.getString("Reslut_Houses_Stars_Flag11" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag11[i].equals(nu)){
                star11+=" ";
                star11+= Reslut_Houses_Stars_Flag11[i];
            }
            else{
                star11+="     ";
            }
            if(i!=count11-1){
                star11+="\n";
            }
        }
        TextView star_11 = (TextView)findViewById(R.id.typestar11);
        star_11.setText(star11);

        String star12="";
        for (int i = 0; i < count12; i++) {
            Reslut_Houses_Stars_Name12[i] = info_bundle.getString("Reslut_Houses_Stars_Name12" + Integer.toString(i));
            star12 += Reslut_Houses_Stars_Name12[i];
            Reslut_Houses_Stars_Flag12[i] = info_bundle.getString("Reslut_Houses_Stars_Flag12" + Integer.toString(i));
            if(!Reslut_Houses_Stars_Flag12[i].equals(nu)){
                star12+=" ";
                star12+= Reslut_Houses_Stars_Flag12[i];
            }
            else{
                star12+="     ";
            }
            if(i!=count12-1){
                star12+="\n";
            }
        }
        TextView star_12 = (TextView)findViewById(R.id.typestar12);
        star_12.setText(star12);

    }

    public void ReturnButton(){
        Button ReturnButton = (Button)findViewById(R.id.ButtonReturn);
        ReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent();
                // intent.setClass(CatchActivity.this, MainActivity.class);
                // startActivity(intent);
                MinpanActivity.errorMs.setText("請輸入內容");
                MinpanCatchActivity.this.finish();
            }
        });

    }
}
