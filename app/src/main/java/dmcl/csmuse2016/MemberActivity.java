package dmcl.csmuse2016;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MemberActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> userDatas;
    private String[] hour_list = {"0:00~0:59(子時)","1:00~1:59(丑時)","2:00~2:59(丑時)","3:00~3:59(寅時)","4:00~4:59(寅時)","5:00~5:59(卯時)",
            "6:00~6:59(卯時)","7:00~7:59(辰時)","8:00~8:59(辰時)","9:00~9:59(巳時)","10:00~10:59(巳時)","11:00~11:59(午時)",
            "12:00~12:59(午時)","13:00~13:59(未時)","14:00~14:59(未時)","15:00~15:59(申時)","16:00~16:59(申時)","17:00~17:59(酉時)",
            "18:00~18:59(酉時)","19:00~19:59(戌時)","20:00~20:59(戌時)","21:00~21:59(亥時)","22:00~22:59(亥時)","23:00~23:59(子時)",};

    int y = 1911; //年
    int M = 1; //月
    int d = 1; //日
    int h = 0; //時辰

    private String hidePassword="";

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
    private Button editSurname;
    private Button editName;
    private Button editPassword;
    private Button editSex;
    private Button editBirthdays;
    private Button editBirthTime;

    //connect getValues = new connect("1");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //接收帳號傳給php當key 抓資料庫
        Bundle getExtra = getIntent().getExtras();
        if (getExtra != null){
            Email = getExtra.getString("mail");
        }

        setContentView(R.layout.member_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_member);
        setSupportActionBar(toolbar);

        toolbar.setBackgroundColor(0xFFFFFFFF);
        // App Logo
        toolbar.setLogo(R.mipmap.title02);
        // Title
        toolbar.setTitle("會員專區");
        toolbar.setTitleTextColor(Color.BLACK);
        // Sub Title
        toolbar.setSubtitle("88Say幫您及時掌握未來");
        toolbar.setSubtitleTextColor(Color.BLACK);

        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(onMenuItemClick);
        //thread call 抓資料庫
        new getDatas().execute();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.membermemu, menu);
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
                case R.id.action_designer://製作群
                    msg+="designer clicked";
                    break;
                case R.id.action_logout://登出
                    msg+="logout clicked";
                    break;
            }

            if(!msg.equals("")) {
                Toast.makeText(MemberActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    };

    public void editSurnameOnClick(View v){
        editSurname = (Button)findViewById(R.id.edit_surname);
        String name = "您的姓:";
        int id = 1;
        showPopup(v , name, id);
    }
    public void editNameOnClick(View v){
        editName = (Button)findViewById(R.id.edit_name);
        String name = "您的大名:";
        int id = 2;
        showPopup(v, name, id);
    }
    public void editPasswordOnClick(View v){
        editPassword = (Button)findViewById(R.id.edit_password);
        String name = "原密碼:";
        int id = 3;
        showPopup(v, name,id);
    }
    public void editSexOnClick(View v){
        editSex = (Button)findViewById(R.id.edit_sex);
        String name = "您的性別:";
        int id = 4;
        showPopup(v, name,id);
    }
    public void editBirthDaysOnClick(View v){
        editBirthdays = (Button)findViewById(R.id.edit_birthday);
        String name = "生日:";
        int id = 5;
        showPopup(v, name,id);
    }
    public void editBirthTimesOnClick(View v){
        editBirthTime = (Button)findViewById(R.id.edit_birthtime);
        String name = "時辰:";
        int id = 6;
        showPopup(v, name,id);
    }

    public void showPopup(View anchorView , String titles, int id) {
        switch (id){
            case 3:
                View popupPassword = getLayoutInflater().inflate(R.layout.fragment_editpassword, null);

                PopupWindow popupCaseThree = new PopupWindow(popupPassword,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView opassword = (TextView) popupPassword.findViewById(R.id.opassword);
                TextView npassword = (TextView) popupPassword.findViewById(R.id.npassword);
                TextView cpassword = (TextView) popupPassword.findViewById(R.id.cpassword);

                opassword.setText(titles);
                npassword.setText("新密碼:");
                cpassword.setText("確認新密碼:");
                // If the PopupWindow should be focusable
                popupCaseThree.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupCaseThree.setBackgroundDrawable(new ColorDrawable());

                int locationCaseThree[] = new int[2];

                // Get the View's(the one that was clicked in the Fragment) location
                anchorView.getLocationOnScreen(locationCaseThree);

                // Using location, the PopupWindow will be displayed right under anchorView
                popupCaseThree.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        locationCaseThree[0], locationCaseThree[1] + anchorView.getHeight());
                break;
            case 4:
                View popupSex = getLayoutInflater().inflate(R.layout.fragment_editsex, null);

                PopupWindow popupCaseFour = new PopupWindow(popupSex,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView sext = (TextView) popupSex.findViewById(R.id.titlesex);
                RadioButton Man = (RadioButton) popupSex.findViewById(R.id.manradio);
                RadioButton Woman = (RadioButton) popupSex.findViewById(R.id.womanradio);
                RadioGroup rgroup = (RadioGroup) popupSex.findViewById(R.id.editsexgroup);

                rgroup.setOnCheckedChangeListener(listener); // 監聽radio button

                sext.setText(titles);
                // If the PopupWindow should be focusable
                popupCaseFour.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupCaseFour.setBackgroundDrawable(new ColorDrawable());

                int locationCaseFour[] = new int[2];

                // Get the View's(the one that was clicked in the Fragment) location
                anchorView.getLocationOnScreen(locationCaseFour);

                // Using location, the PopupWindow will be displayed right under anchorView
                popupCaseFour.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        locationCaseFour[0], locationCaseFour[1] + anchorView.getHeight());

                if (collectDatas[4].equals("1")){
                   Man.setChecked(true); //initial state
                } else{
                    Woman.setChecked(true); //initial state
                }
                break;
            case 5:
                View popupBirth = getLayoutInflater().inflate(R.layout.fragment_editbirthday, null);

                PopupWindow popupCaseFive = new PopupWindow(popupBirth,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView bd = (TextView) popupBirth.findViewById(R.id.birth);
                NumberPicker year = (NumberPicker)popupBirth.findViewById(R.id.edityypicker);
                NumberPicker month = (NumberPicker)popupBirth.findViewById(R.id.editmmpicker);
                final NumberPicker day = (NumberPicker)popupBirth.findViewById(R.id.editddpicker);
                bd.setText(titles);
                //設定上限下限
                year.setMaxValue(2016);
                year.setMinValue(1911);
                //設定初始值為使用者的原本year
                int userOriginYear = Integer.parseInt(collectDatas[6]);
                year.setValue(userOriginYear);
                year.setWrapSelectorWheel(true);
                y = Integer.parseInt(collectDatas[6]);

                month.setMaxValue(12);
                month.setMinValue(1);
                //設定初始值為使用者的原本year
                int userOriginMonth = Integer.parseInt(collectDatas[7]);
                month.setValue(userOriginMonth);
                month.setWrapSelectorWheel(true);
                M = Integer.parseInt(collectDatas[7]);

                day.setMaxValue(31);
                day.setMinValue(1);
                //設定初始值為使用者的原本year
                int userOriginDay = Integer.parseInt(collectDatas[8]);
                d = Integer.parseInt(collectDatas[8]);

                day.setValue(userOriginDay);
                day.setWrapSelectorWheel(true);
                // If the PopupWindow should be focusable
                popupCaseFive.setFocusable(true);
                // If you need the PopupWindow to dismiss when when touched outside
                popupCaseFive.setBackgroundDrawable(new ColorDrawable());
                int locationCaseFive[] = new int[2];
                // Get the View's(the one that was clicked in the Fragment) location
                anchorView.getLocationOnScreen(locationCaseFive);
                // Using location, the PopupWindow will be displayed right under anchorView
                popupCaseFive.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        locationCaseFive[0], locationCaseFive[1] + anchorView.getHeight());

                year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        //Display the newly selected number from picker
                        y = newVal;
                        if (M == 2) {
                            if (y % 4 == 0) { //算閏年 但並不完整
                                day.setMaxValue(29);
                                day.setMinValue(1);
                            } else {
                                day.setMaxValue(28);
                                day.setMinValue(1);
                            }
                        }
                    }
                });
                month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        M = newVal;
                        day.setWrapSelectorWheel(true);
                        if (M == 2) {
                            if (y % 4 == 0) { //算閏年 但並不完整
                                day.setMaxValue(29);
                                day.setMinValue(1);
                            } else {
                                day.setMaxValue(28);
                                day.setMinValue(1);
                            }
                        } else if (M == 4 || M == 6 || M == 9 || M == 11) {
                            day.setMaxValue(30);
                            day.setMinValue(1);
                        } else {
                            day.setMaxValue(31);
                            day.setMinValue(1);
                        }
                        //tv.setText("Selected month : " + MM);
                    }
                });
                day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        d = newVal;
                        //tv.setText("Selected day : " + dd);
                    }
                });

                break;
            case 6:
                View popupTime = getLayoutInflater().inflate(R.layout.fragment_edittime, null);

                PopupWindow popupCaseSix = new PopupWindow(popupTime,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView bt = (TextView) popupTime.findViewById(R.id.birtht);
                NumberPicker time = (NumberPicker)popupTime.findViewById(R.id.edittimePick);
                bt.setText(titles);
                //設定上限下限
                time.setMaxValue(23);
                time.setMinValue(0);
                //設定初始值為使用者的原本時辰
                int userOriginTime = Integer.parseInt(collectDatas[9]);
                time.setValue(userOriginTime);

                time.setDisplayedValues(hour_list);
                time.setWrapSelectorWheel(true);
                // If the PopupWindow should be focusable
                popupCaseSix.setFocusable(true);
                // If you need the PopupWindow to dismiss when when touched outside
                popupCaseSix.setBackgroundDrawable(new ColorDrawable());
                int locationCaseSix[] = new int[2];
                // Get the View's(the one that was clicked in the Fragment) location
                anchorView.getLocationOnScreen(locationCaseSix);
                // Using location, the PopupWindow will be displayed right under anchorView
                popupCaseSix.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        locationCaseSix[0], locationCaseSix[1] + anchorView.getHeight());

                time.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                        //hh = newVal;
                        //tv.setText("Selected hour : " + hh);
                    }
                });

                break;
            default:
                View popupView = getLayoutInflater().inflate(R.layout.fragment_edit_member, null);

                PopupWindow popupWindow = new PopupWindow(popupView,
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                TextView tv = (TextView) popupView.findViewById(R.id.somewords);
                EditText edit = (EditText) popupView.findViewById(R.id.editor);
                tv.setText(titles);
                if (id == 1){ //表示現在的畫面是“姓”
                    edit.setText(collectDatas[0]); //initial
                } else if (id == 2){ //畫面為“名"
                    edit.setText(collectDatas[1]); //initial
                }
                // If the PopupWindow should be focusable
                popupWindow.setFocusable(true);

                // If you need the PopupWindow to dismiss when when touched outside
                popupWindow.setBackgroundDrawable(new ColorDrawable());

                int location[] = new int[2];

                // Get the View's(the one that was clicked in the Fragment) location
                anchorView.getLocationOnScreen(location);

                // Using location, the PopupWindow will be displayed right under anchorView
                popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,
                        location[0], location[1] + anchorView.getHeight());
                break;
        }
    }
    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.manradio:
                    //sex = 1;
                    Log.v("sex","man");
                    break;
                case R.id.womanradio:
                    //sex = 0;
                    Log.v("sex","woman");
                    break;
            }

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
            //將密碼蓋成"*"
            for (int i = 0 ; i<collectDatas[3].length() ; i++){
                hidePassword += "*";
            }
            Password.setText(hidePassword);
            
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
        }

    }


}
