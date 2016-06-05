package com.example.esdi.amelkyodai;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class KyodaiActivity extends Activity {

    private KyodaiActivityView kyodAttView;
    Button btn_quitter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        kyodAttView = (KyodaiActivityView) findViewById(R.id.KyodaiActivityView);

        kyodAttView.setVisibility(View.VISIBLE);
        btn_quitter = (Button) findViewById(R.id.QuitterJeu);
        btn_quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("-> Quitter <-", "Quitter le jeux");
                switch (view.getId()) {
                    case R.id.QuitterJeu:
                        finish();
                        break;
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        kyodAttView.onResume();
    }


    public void onPause() {
        super.onPause();
        kyodAttView.onPause();
    }
}
