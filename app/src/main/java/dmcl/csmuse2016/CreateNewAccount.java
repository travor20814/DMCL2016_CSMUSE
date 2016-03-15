package dmcl.csmuse2016;

import android.app.Activity;
import java.util.ArrayList;
import org.apache.http.util.EntityUtils;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class CreateNewAccount extends Activity {

    int flag1 = 0;
    private ProgressDialog pDialog;
    InputStream is=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_account);
    }
    //抓edittext上使用者輸入的字
    public void applyNewAccount(View v){

        EditText EditTextname = (EditText)findViewById(R.id.newNameText);
        Editable nameName;
        nameName = EditTextname.getText();
        String username =nameName.toString();

        EditText EditTextaccount = (EditText)findViewById(R.id.newAccountText);
        Editable AccountName;
        AccountName = EditTextaccount.getText();
        String Account =AccountName.toString();

        EditText EditTextpassword = (EditText)findViewById(R.id.newpasswordText);
        Editable passwordName;
        passwordName = EditTextpassword.getText();
        String password =passwordName.toString();

        flag1 = 0; //用一個flag來確認帳號是否重複
        if (username.trim().length()==0){
            new AlertDialog.Builder(CreateNewAccount.this)
                    .setTitle("申請失敗")
                    .setMessage("請輸入姓名")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).show();
        }
        else if (Account.trim().length()==0){
            new AlertDialog.Builder(CreateNewAccount.this)
                    .setTitle("申請失敗")
                    .setMessage("請輸入帳號")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        else if (password.trim().length()==0){
            new AlertDialog.Builder(CreateNewAccount.this)
                    .setTitle("申請失敗")
                    .setMessage("請輸入密碼")
                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        else{
            Log.v("click_apply", "1");

            new checkAccount().execute(); //a new thread

        }

    }
    class checkAccount extends  AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute(){ //loading畫面
            super.onPreExecute();
            flag1 = 0; //initial
            pDialog = new ProgressDialog(CreateNewAccount.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) { //thread做的事:比對帳密是否重複
            // TODO Auto-generated method stub

            EditText EditTextaccount = (EditText)findViewById(R.id.newAccountText);
            Editable AccountName;
            AccountName = EditTextaccount.getText();
            String Account = AccountName.toString();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("Account", Account));

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://dmcl.twbbs.org/csmuse/checkAccount.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();

                String s = EntityUtils.toString(entity); //這邊會抓取php的echo
                System.out.println(s);
                Log.v("pass 1", "connection success ");
                if (s.equals("Available")){ //如果php echo"available"就可申請
                    flag1 = 1;
                }
                else if (s.equals("Assigned")){ //反之
                    flag1 = 2;
                }


            }
            catch(Exception e) //exception
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            return null ;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss(); //讓loading的提示消失
            if (flag1 == 1) { //申請成功
                new insertto().execute();
                Toast.makeText(getApplicationContext(), "Apply success",
                        Toast.LENGTH_SHORT).show();
                Intent intentmainview = new Intent(CreateNewAccount.this,
                                                    LoginActivity.class);
                CreateNewAccount.this.startActivity(intentmainview);
                finish();

            }
            else if(flag1 == 2){ //失敗
                new AlertDialog.Builder(CreateNewAccount.this)
                        .setTitle("申請失敗")
                        .setMessage("此帳號已經有人申請")
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        }).show();
            }
            else{ //如果程式發生奇怪狀況的default
                new AlertDialog.Builder(CreateNewAccount.this)
                        .setTitle("申請失敗")
                        .setMessage("problem undefined|flags unexpected")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }

        }
    }
    //把帳密輸入到資料庫 (結束了上一個thread後 執行的new thread)
    class insertto extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub

            EditText EditTextname = (EditText)findViewById(R.id.newNameText);
            Editable nameName;
            nameName = EditTextname.getText();
            String username =nameName.toString();

            EditText EditTextaccount = (EditText)findViewById(R.id.newAccountText);
            Editable AccountName;
            AccountName = EditTextaccount.getText();
            String Account =AccountName.toString();

            EditText EditTextpassword = (EditText)findViewById(R.id.newpasswordText);
            Editable passwordName;
            passwordName = EditTextpassword.getText();
            String Password =passwordName.toString();
            //new connect();

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("Account", Account));
            nameValuePairs.add(new BasicNameValuePair("Password",Password));
            //nameValuePairs.add(new BasicNameValuePair("Name", username));


            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://dmcl.twbbs.org/csmuse/insert_new_account.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.v("pass 1", "connection success ");

            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
