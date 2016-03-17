package dmcl.csmuse2016;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    private ProgressDialog pDialog;
    private boolean check=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        /*按鈕偵測*/
        final Button sign_in_button = (Button)findViewById(R.id.sign_in_button);
        //暫時沒用到登入按鈕的偵測 因為我寫在layout onclick上 (要處理的事情較多)
        final Button guest_button = (Button)findViewById(R.id.guestlogin);
        final Button newAccount = (Button)findViewById(R.id.newAccount);

        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v){
                if (v == guest_button){
                    Intent intentHome = new Intent(LoginActivity.this,
                                                    HomePageActivity.class);
                    LoginActivity.this.startActivity(intentHome);
                    finish(); //using ondestroy to replace "finish" function
                }
                if (v == newAccount){
                    Intent intentNewAccount = new Intent(LoginActivity.this,
                                                        AccountLicence.class);
                    LoginActivity.this.startActivity(intentNewAccount);
                    Log.i("Content","layout to newaccount");
                }
            }
        };

        guest_button.setOnClickListener(handler);
        newAccount.setOnClickListener(handler);

        /*設定輸入帳密時,提示的字會消失*/
        final EditText account = (EditText)findViewById(R.id.Account);
        final EditText password = (EditText)findViewById(R.id.password);
        account.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                account.setHint("");
                password.setHint("密碼");
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View v,boolean hasFocus){
                password.setHint("");
                account.setHint("帳號");
            }
        });

    }

    @Override //原本就有的source code
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void Login(View view) { //按下登入按鈕
        Log.v("click","1");
        new loginbuttonclick().execute(); //給他一個執行緒(main thread)
    }
    //執行緒code
    class loginbuttonclick extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute(){ //執行緒開始前做的
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) { //進行中做的

            EditText EditTextAccount = (EditText)findViewById(R.id.Account);
            Editable AccountName;
            AccountName = EditTextAccount.getText();
            String Account =AccountName.toString();

            EditText EditTextPassword = (EditText)findViewById(R.id.password);
            Editable PasswordName;
            PasswordName = EditTextPassword.getText();
            String Password =PasswordName.toString();

            //sending to php
            String command="select * from Account where Account ='"+Account+"' and Password = '"+Password+"'";
            check=new connect(command).LoginCheck();
            Log.v("check", "check=" + String.valueOf(check));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) { //完成後
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss(); //waiting dismiss
            if(check)
            { //set page to homepage
                Log.v("check", "1");
                Intent intentNewAccount = new Intent(LoginActivity.this,
                        HomePageActivity.class);
                LoginActivity.this.startActivity(intentNewAccount);
                finish();
            }else{    // login denied by entering wrong account/password
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login denied!!")
                        .setMessage("The account or the password is wrong!!")
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        }).show();
            }
        }

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }
}

