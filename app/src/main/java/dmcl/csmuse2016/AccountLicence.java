package dmcl.csmuse2016;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.AccessControlContext;

public class AccountLicence extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.acceptaccount);

        WebView licenceWeb = (WebView) findViewById(R.id.licenceWeb); //initial webview
        licenceWeb.getSettings().setJavaScriptEnabled(true);
        licenceWeb.requestFocus();
        licenceWeb.setWebViewClient(new myWebViewClient());
        licenceWeb.loadUrl("https://play.google.com/intl/ALL_tw/about/developer-distribution-agreement.html");

        final Button licenceAcceptButton = (Button)findViewById(R.id.licenceAcceptButton);

        View.OnClickListener handler = new View.OnClickListener(){
            public void onClick(View v){
                if (v == licenceAcceptButton){
                    Intent intentNewAccount = new Intent(AccountLicence.this,
                                                        CreateNewAccount.class);
                    AccountLicence.this.startActivity(intentNewAccount);
                    finish();
                }
            }
        };
        licenceAcceptButton.setOnClickListener(handler);

    }


    private class myWebViewClient extends WebViewClient{ //定義webview用的東西
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    public void checkBoxchecked(View v){ //使用者需同意條款
        CheckBox seeifchecked = (CheckBox)findViewById(R.id.licenceCheckBox);
        Button nextstepbutton = (Button) findViewById(R.id.licenceAcceptButton);
        if (seeifchecked.isChecked()==true) {
            nextstepbutton.setEnabled(true);
        }
        else{
            nextstepbutton.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
