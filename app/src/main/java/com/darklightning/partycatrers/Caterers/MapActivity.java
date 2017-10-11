package com.darklightning.partycatrers.Caterers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.darklightning.partycatrers.R;

/**
 * Created by chato on 10/11/2017.
 */


public class MapActivity extends AppCompatActivity {
ImageView locOne,locTwo,locThree,locFour,locFive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        locOne=(ImageView)findViewById(R.id.loc1) ;
        locTwo=(ImageView)findViewById(R.id.loc2) ;
        locThree=(ImageView)findViewById(R.id.loc3) ;
        locFour=(ImageView)findViewById(R.id.loc4) ;
        locFive=(ImageView)findViewById(R.id.loc5) ;

        locOne.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(this,MapCatrersList.class);
//                intent.putExtra("state_name","Bihar");
                //startActivity(new Intent(OtherActivity.this, YetAnotherActivity.class));
            }
        });
        locTwo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             //   Intent intent = new Intent(this,MapCatrersList.class);
              //  intent.putExtra("state_name","Uttar Pradesh");
                //startActivity(new Intent(OtherActivity.this, YetAnotherActivity.class));
            }
        });
        locThree.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
              //  Intent intent = new Intent(this,MapCatrersList.class);
               // intent.putExtra("state_name","Jammu and Kashmir");
                //startActivity(new Intent(OtherActivity.this, YetAnotherActivity.class));
            }
        });
        locFour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
             //   Intent intent = new Intent(this,MapCatrersList.class);
              //  intent.putExtra("state_name","Assam");
                //startActivity(new Intent(OtherActivity.this, YetAnotherActivity.class));
            }
        });

    }
}
