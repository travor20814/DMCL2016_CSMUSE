package dmcl.csmuse2016;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.util.DisplayMetrics;


public class AnimationActivity extends ActionBarActivity {
    //開頭動畫
    private ImageView image = null;
    private int duration = 0;
    private final String filename="account.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);

        DisplayMetrics monitorsize = new DisplayMetrics(); //偵測螢幕大小
        getWindowManager().getDefaultDisplay().getMetrics(monitorsize);

        int monitorWidth = monitorsize.widthPixels;   //偵測螢幕大小 暫時還沒用到
        int monitorHeight = monitorsize.heightPixels;


        getSupportActionBar().hide(); //隱藏title

        image = (ImageView)findViewById(R.id.demo);
        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        image.setBackgroundResource(R.drawable.frame);
        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable anim = (AnimationDrawable) image.getBackground();
        //calculate the time for animation
        for(int i=0;i<anim.getNumberOfFrames();i++)
            duration+=anim.getDuration(i);
        // Start the animation (looped playback by default).
        anim.start();
        //when the animation finish,...
        checkIfAnimationDone(anim);
    }
    public void checkIfAnimationDone(AnimationDrawable anim){
        //Log.v("time",duration+"");
        initialFile();
        Handler handler = new Handler();
        boolean a=new Write_and_Read(filename,getFilesDir()).ifLogin();
        //Log.e("aa",Boolean.toString(a));
        if(!new Write_and_Read(filename,getFilesDir()).ifLogin()){
        handler.postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(AnimationActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, duration);
        }
        else{
            handler.postDelayed(new Runnable(){
                public void run(){
                    Intent intent = new Intent(AnimationActivity.this,HomePageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, duration);
        }
        }

    public void initialFile(){
        new Write_and_Read(filename,getFilesDir()).WritetoFile("");//if file exit writ,and not creat and write
    }
}