package com.example.esdi.amelkyodai;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {


    public Menu mMenu;
    public MainActivity instance;

    private boolean soundlanched = false;

    private MediaPlayer mMediaPlayerbckgnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);

        //  bouttons du menu

        final Button ButtonJouer = (Button) findViewById(R.id.buttonJouer);
        final Button ButtonQuitter = (Button) findViewById(R.id.buttonQuitter);
        final Button ButtonSon = (Button) findViewById(R.id.buttonSound);
        final Button about = (Button) findViewById(R.id.about);

        mMediaPlayerbckgnd = MediaPlayer.create(instance.getBaseContext(), R.raw.music);
        mMediaPlayerbckgnd.setLooping(true);

        ButtonQuitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMediaPlayerbckgnd.pause();


                finish();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAbout();
            }
        });

		/*! ACTIVER OU DESACTIVER du son  */
        ButtonSon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soundlanched = !soundlanched;
                if (soundlanched) {
                    ButtonSon.setText(R.string.btn_sonoui);
                    mMediaPlayerbckgnd.start();
                } else {
                    ButtonSon.setText(R.string.btn_sonnon);
                    if (mMediaPlayerbckgnd.isPlaying()) {
                        mMediaPlayerbckgnd.pause();

                    }
                }
            }
        });

        ButtonJouer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(instance, KyodaiActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                showAbout();
                return true;

            default:
                return false;
        }
    }

    /*la page d'apropos  */
    private void showAbout() {
        AlertDialog.Builder about = new AlertDialog.Builder(this);
        about.setTitle(
                Html.fromHtml(
                        "<b>About AmelKyodai</b>")
        );

        TextView l_viewabout = new TextView(this);
        l_viewabout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        l_viewabout.setPadding(30, 10, 30, 10);
        l_viewabout.setTextSize(20);
        l_viewabout.setText(
                Html.fromHtml(

                        "<b><center>Kyodai developper par:</center></b>" +
                                "<br/>" +
                                "<center><small>- Amel REKKAB </small>" + "<br/></center>" +
                                "<center><small>- Etudiante a Paris8 </small>" + "<br/></center>" +
                                "<center><small>- rekkabamel@yahoo.fr:\n" +
                                "<br/>" + "<br/>" +
                                "<br/>"
                )

        );

        about.setView(l_viewabout);
        about.setPositiveButton(
                "OK",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }
        );

        about.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

                                      @Override
                                      public void onCancel(DialogInterface dialog) {

                                      }
                                  }
        );

        about.show();
    }
}




