package dmcl.csmuse2016;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jack on 2016/4/6.
 */
public class EightWordMinpanActivity extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.eightwordminpan, container, false);
        Button b= (Button) v.findViewById(R.id.button_Submit3);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });
        return v;
    }


}
