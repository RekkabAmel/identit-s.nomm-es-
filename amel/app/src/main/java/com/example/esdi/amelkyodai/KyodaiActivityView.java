package com.example.esdi.amelkyodai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class KyodaiActivityView extends SurfaceView implements SurfaceHolder.Callback, Runnable {


    /*!\brief Déclaration de variables et constantes nécessaires au fonctionnement du jeu */


    /*!\brief Compteur  */
    private int occ = 0;

    /*!\brief Image qui represente un vide */
    private Bitmap vide;

    /*!\brief Nombre de niveaus proposés */
    int nbLevel = 3;

    /*!\brief Tableau de type image  */
    private Bitmap img[];

    /*!\brief Importation des images */
    private Resources memores;

    private Context memocontext;

    /*!\brief Boolean qui indique le cycle de vie du jeu, tant qu'il est vrai, le jeu tourne */
    private boolean in = true;

    private Thread cv_thread;


    SurfaceHolder holder;

    /*!\brief Timer utilsé comme chronomètre */
    Chronometer timer;

    /*!\brief Compteur utilé pour le timper  */
    CountDownTimer count, countTmp;

    /*!\brief Le temps de jeu */
    long time;

    /*!\brief La couleur du fond d'ecran */
    int backGroundColor = 0x901B01;

    /*!\brief Variables qui sert à récupérer les repères des cases actuels et précédents  */
    int oldI, oldJ;

    /*!\brief Variables qui indiques les repères des images stockées dans un tableau */
    int image, image2;

    /*!\brief Le nombre d'essaie */
    int numberOfTry;

    int score;
    /*!\brief Variable qui accumule le nombre de succession d'accumulation de cases identiques */
    int successfull;

    /*!\brief Variable qui détermine le niveau suivant à jouer */
    int Next_Level = 1;

    /*!\briefVariable qui determine si le joueur a gagné ou a perdu */
    int isWin = 0;

    /*!\brief repères de dimensions */
    int mapTopAnchor;
    int mapLeftAnchor;

    int mapTopAnchor_tmp;
    int mapLeftAnchor_tmp;

    int deckeys;

    Toast toast;

    /*!\brief Variable qui stock le meilleur score */
    public static int bestScore = 0;

    /*!\brief Les dimensions de la carte de jeu  */
    static final int mapWidth = 6;//6
    static final int mapHeight =7;//7

    /*!\brief La dimensions de chaque case */
    static final int mapTileSize = 50;//50

    /*!\brief Le tableau qui represente la map sur laquelle se déroulera le jeu */
    public int[][] map;
	/*!\brief Map de stockage de données temporaire */

    public boolean[][] mem = new boolean[mapHeight][mapWidth];

    /*!\brief Tableau qui stock les indexes de photos  */
    public int[] index;

    /*!\brief Le temps à jouer */
    static final long TIMER = 100000;

    private boolean keyused = false;

    /*!\briefIndique si y'a eu un clique ou non */
    boolean clic = false;

    /*!\brief Pour remplissage de tableau aléatoire */
    private Random rd;


    /*!\brief Création du context */
    public KyodaiActivityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.e(">>> Kyodai", "KyodaiActivityView");
        holder = getHolder();
        holder.addCallback(this);
        memocontext = context;
        memores = memocontext.getResources();
        Log.e(">>> Kyodai", "KyodaiActivityView 1 ");
        loadimages(memores);
        Log.e(">>> Kyodai", "KyodaiActivityView 2 ");
        rd = new Random();
        cv_thread = new Thread(this);
        setFocusable(true);

    }

    /*!\briefChargement des images */
    private void loadimages(Resources res) {
        img = new Bitmap[10];

        vide = BitmapFactory.decodeResource(res, R.drawable.img09);

        img[0] = BitmapFactory.decodeResource(res, R.drawable.img08);
        img[1] = BitmapFactory.decodeResource(res, R.drawable.papillon1);
        img[2] = BitmapFactory.decodeResource(res, R.drawable.papillon2);
        img[3] = BitmapFactory.decodeResource(res, R.drawable.papillon4);
        img[4] = BitmapFactory.decodeResource(res, R.drawable.papillon5);
        img[5] = BitmapFactory.decodeResource(res, R.drawable.papillon6);
        img[6] = BitmapFactory.decodeResource(res, R.drawable.img08);
        img[7] = BitmapFactory.decodeResource(res, R.drawable.img07);

    }



	/*!\brief Les niveau du jeu  */

    public void level1() {

        map[0][2] = 3;
        map[1][2] = 2;
        map[2][2] = 1;
        map[3][2] = 5;
        map[4][2] = 3;
        map[4][3] = 3;
        map[5][2] = 5;
        map[5][3] = 2;
        map[6][1] = 2;
        map[6][2] = 1;
        map[6][3] = 5;
        map[6][4] = 1;
    }

    public void level2() {

        map[0][3] = 5;
        map[1][3] = 2;
        map[2][3] = 1;
        map[3][3] = 3;
        map[4][1] = 5;
        map[4][2] = 5;
        map[4][3] = 3;
        map[5][1] = 2;
        map[5][2] = 3;
        map[5][3] = 2;
        map[5][4] = 2;
        map[6][1] = 1;
        map[6][2] = 3;
        map[6][3] = 1;
        map[6][4] = 1;
    }



    /*!\brief Initialisation des paramètres   */
    public void initparameters() {// INITIALISATION DES VARIABLES

        map = new int[mapHeight][mapWidth];
        mapTopAnchor = (getHeight() - mapHeight * mapTileSize) / 2;
        mapLeftAnchor = (getWidth() - mapWidth * mapTileSize) / 2;

        mapTopAnchor_tmp = mapTopAnchor;
        mapLeftAnchor_tmp = mapLeftAnchor;

        keyused = false;
        score = 0;
        numberOfTry = 20;
        successfull = 0;
        clic = false;


        initmap();
        recupScore();

        if ((cv_thread != null) && (!cv_thread.isAlive())) {
            cv_thread.start();
        }


    }


    /*!\briefinitialisation de la map  */
    private void initmap() {

        int rd;

        // création de tableau pour l'utiliser comme ressourse de chargement de photo dans la map du jeu
        index = new int[10];

        // Initialisation de la map du jeu
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++)

                map[i][j] = 0;

        }


        // Chargement du niveau de jeu

        if (Next_Level > nbLevel)
            Next_Level = 1;

        if (Next_Level == 1) {
            messageNiveau();
            level1();

        }
        if (Next_Level == 2) {
            messageNiveau();
            level2();
        }


    }

    /*!\brief Compteur  */
    public void temps() {
        count = new CountDownTimer(TIMER, 10) {// COMPTE A REBOURS

            public void onTick(long millisUntilFinished) {

                time = millisUntilFinished;


            }

            public void onFinish() {
                lost();
            }
        };
        count.start();
    }


	/*!\brief Affichage d'une fenêtre de type alert qui affiche un message qui indique le niveau
	 *	de jeu et validation avec un bouton ok
	 */

    public void messageNiveau() {

        AlertDialog.Builder alert = new AlertDialog.Builder(memocontext);
        alert.setTitle("Level");
        alert.setIcon(R.drawable.back);

        TextView l_viewabout = new TextView(memocontext);

        l_viewabout.setText("Level: " + Next_Level);
        l_viewabout.setGravity(FOCUS_UP);
        l_viewabout.setBackgroundColor(Color.LTGRAY);
        l_viewabout.setTextColor(Color.RED);
        l_viewabout.setTextSize(25);
        alert.setView(l_viewabout);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                temps();
                // do something when the OK button is clicked
            }
        });
	/*	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface arg0, int arg1) {
				// do something when the Cancel button is clicked
			}});
			*/
        alert.show();
    }


    /*!\brief La boucle qui sert à maintenir le jeu tant sa valeur est vraie  */
    public void run() {
        Canvas c = null;

        while (in) {
            try {

                cv_thread.sleep(40);
                if (numberOfTry <= 0 && isWin == 0) {
                    lost();
                    count.cancel();
                }


                try {
                    c = holder.lockCanvas(null);
                    dessin(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            } catch (Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
            }
        }
    }


    /*!\brief Dessin de la map   */
    public void paintMap(final Canvas canvas) {

        Bitmap tmpimg = img[9];

        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                switch (map[i][j]) {

                    case 0:
                        tmpimg = img[0];
                        break;

                    case 1:
                        tmpimg = img[1];
                        break;
                    case 2:
                        tmpimg = img[2];
                        break;

                    case 3:
                        tmpimg = img[3];
                        break;

                    case 4:
                        tmpimg = img[4];
                        break;

                    case 5:
                        tmpimg = img[5];
                        break;

                    case 6:
                        tmpimg = img[6];
                        break;

                    case 7:
                        tmpimg = img[7];
                        break;

                    case 8:
                        tmpimg = img[8];
                        break;

                    case 9:
                        tmpimg = img[9];
                        break;

                }
                if (mem[i][j]) {
                    canvas.drawBitmap(tmpimg, mapLeftAnchor + j * mapTileSize,
                            mapTopAnchor + i * mapTileSize, null);
                } else {
                    canvas.drawBitmap(tmpimg, mapLeftAnchor + j * mapTileSize,
                            mapTopAnchor + i * mapTileSize, null);

                }

            }
        }

    }

    /*!\brief Dessin du timer  */
    private void paintTimer(Canvas canvas) {
        Log.e("-> Paint <-", "dessin du timer");
        final Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(25);
        float currentTime = time;
        if ((float) (currentTime / TIMER) > 0.66) {
            paint.setColor(Color.GREEN);
        } else if ((float) (currentTime / TIMER) > 0.33) {
            paint.setColor(Color.YELLOW);
        } else {
            paint.setColor(Color.RED);
        }

        canvas.drawRect(0, mapTopAnchor / 2, getWidth() * (currentTime / TIMER), mapTopAnchor / 2 + 10, paint);
    }

    /*!\brief Dessiner et afficher la barre de temps  */
    private void dessin(Canvas canvas) {

        canvas.drawRGB(0, 0, 0);
        paintMap(canvas);
        paintTimer(canvas);


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initparameters();

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceDestroyed");
    }

    /*!\brief La gestion du tactile et du jeu  */
    public boolean onTouchEvent(MotionEvent event)
    {
        Log.e("-> Ontouche <-", "ontouche des icones");
        int posX = (int) (event.getX() / img[0].getWidth());
        int posY = (int) ((event.getY() - mapTopAnchor) / img[0].getHeight());
        if ((posY >= mapHeight || (event.getY() - mapTopAnchor) <= 0) || (posX >= mapWidth || (event.getX() - mapLeftAnchor) <= 0)) {


            return super.onTouchEvent(event);
        }


        occ++;
        if (occ > 2)
            occ = 1;

        if (occ == 1 && posX !=0 && posY!=0)
        {
            oldI = posY;
            oldJ = posX;
            image2 = map[oldI][oldJ];

        }

        if (occ == 2 && posX !=0 && posY!=0)
        {
            image = map[posY][posX];
            map[oldI][oldJ] = image;
            map[posY][posX] = image2;

        }

        if (!clic)
        {
            if( posX !=0 && posY !=0)
            {
                image = map[posY][posX];
                clic = true;

                if (oldI != posY)
                    oldI = posY;
                if (oldJ != posX)
                    oldJ = posX;
                image2 = map[oldI][oldJ];

            }
            if(clic)
                numberOfTry--;
        }
        else
        {
            sleep();


        }




        for (int acc = 0; acc < 10; acc++) {

            decalerVersBasSiVide();
            collapserLesCases();
            decalerVersBasSiVide();

        }

        calculerScore();
        savescore();
        // Vérifier si toutes les cases sont vides, et si le joueur gagne ou perd
        siToutesLesCasesSontVides();
        if( posX==0 && posY==0) {

            pauseAlerte();

            posX=oldJ;
            posY=oldI;
        }
        numberOfTry--;
        if (numberOfTry<=0)
            lost();

        return super.onTouchEvent(event);

    }

    int j = 0;

    /*!\brief Calculer le score  */
    public void calculerScore()

    {
        score = (int) (successfull  * ((float) time / TIMER));
    }

    /*!\brief Vérifier si le jeu est terminé et si le joueur gagne ou perd  */
    public void siToutesLesCasesSontVides() {

        int estVide = 0;
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++)

                if (map[i][j] == 0 || map[i][j] == 7 )
                    estVide++;

        }
        if (estVide == mapHeight * mapWidth) {
            isWin = 1;
            Next_Level++;
            if (isWin == 1 && time > 0)

                won();
            else {
                isWin = 0;
                lost();
            }
        } else
            isWin = 0;


    }

	/*!\brief Supprimer les cases vides verticalement   */

    public void supprimerCasesSiPlus3vertical() {


        for (int i = 1; i < mapHeight - 2; i++)

        {
            for (int j = 0; j < mapWidth; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i + 1][j] && map[i + 1][j] == map[i + 2][j]) {
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i + 2][j] = 0;
                    sleep();
                }
            }

        }
        successfull = +3;

    }

    public void supprimerCasesSiPlus4vertical() {

        for (int i = 1; i < mapHeight - 3; i++)

        {
            for (int j = 0; j < mapWidth; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i + 1][j] && map[i + 1][j] == map[i + 2][j] && map[i + 2][j] == map[i + 3][j]) {
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i + 2][j] = 0;
                    map[i + 3][j] = 0;
                    sleep();
                }


            }
        }
        successfull = +4;

    }

    public void supprimerCasesSiPlus5vertical() {

        for (int i = 1; i < mapHeight - 4; i++)

        {
            for (int j = 0; j < mapWidth; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i + 1][j] && map[i + 1][j] == map[i + 2][j] && map[i + 2][j] == map[i + 3][j] && map[i + 3][j] == map[i + 4][j]) {
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i + 2][j] = 0;
                    map[i + 3][j] = 0;
                    map[i + 4][j] = 0;

                    sleep();
                }


            }
        }
        successfull = +5;

    }

    public void supprimerCasesSiPlus6vertical() {

        for (int i = 1; i < mapHeight - 5; i++)

        {
            for (int j = 0; j < mapWidth; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i + 1][j] && map[i + 1][j] == map[i + 2][j] && map[i + 2][j] == map[i + 3][j] && map[i + 3][j] == map[i + 4][j] && map[i + 4][j] == map[i + 5][j]) {
                    map[i][j] = 0;
                    map[i + 1][j] = 0;
                    map[i + 2][j] = 0;
                    map[i + 3][j] = 0;
                    map[i + 4][j] = 0;
                    map[i + 5][j] = 0;
                    sleep();
                }


            }
        }
        successfull = +6;

    }


    /*!\brief Supprimer les vides horizontallement  */
    public void suppLine(int i, int j, int nb) {


        for (; nb < 0; nb--) {
            map[i][j--] = 0;
            successfull = +1;
        }

        decalerVersBasSiVide();
    }


    public void suprimerCasesSiPlus3Horizontal() {

        for (int i = 1; i < mapHeight; i++)

        {
            for (int j = 0; j < mapWidth - 2; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i][j + 1] && map[i][j + 1] == map[i][j + 2]) {

                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                    map[i][j + 2] = 0;
                    sleep();

                }
            }
        }
    }

    public void suprimerCasesSiPlus4Horizontal() {

        for (int i = 1; i < mapHeight; i++)

        {
            for (int j = 0; j < mapWidth - 3; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i][j + 1] && map[i][j + 1] == map[i][j + 2] && map[i][j + 2] == map[i][j + 3]) {

                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                    map[i][j + 2] = 0;
                    map[i][j + 3] = 0;
                    sleep();

                }
            }
        }
    }

    public void suprimerCasesSiPlus5Horizontal() {

        for (int i = 1; i < mapHeight; i++)

        {
            for (int j = 0; j < mapWidth - 4; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i][j + 1] && map[i][j + 1] == map[i][j + 2] && map[i][j + 2] == map[i][j + 3] && map[i][j + 3] == map[i][j + 4]) {

                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                    map[i][j + 2] = 0;
                    map[i][j + 3] = 0;
                    map[i][j + 4] = 0;
                    sleep();

                }
            }
        }
    }

    public void suprimerCasesSiPlus6Horizontal() {

        for (int i = 1; i < mapHeight; i++)

        {
            for (int j = 0; j < mapWidth - 5; j++) {

                if (map[i][j] != 0 && map[i][j] == map[i][j + 1] && map[i][j + 1] == map[i][j + 2] && map[i][j + 2] == map[i][j + 3] && map[i][j + 3] == map[i][j + 4] && map[i][j + 4] == map[i][j + 5]) {

                    map[i][j] = 0;
                    map[i][j + 1] = 0;
                    map[i][j + 2] = 0;
                    map[i][j + 3] = 0;
                    map[i][j + 4] = 0;
                    map[i][j + 5] = 0;
                    sleep();

                }
            }
        }
    }

    /*!\brief Collapser les cases horizontallement  */
    public void collapserHorizontalement() {

        decalerVersBasSiVide();

        suprimerCasesSiPlus6Horizontal();
        suprimerCasesSiPlus5Horizontal();
        suprimerCasesSiPlus4Horizontal();
        suprimerCasesSiPlus3Horizontal();
    }

    /*!\brief Collapser les cases verticalement  */
    public void collapserVerticalement() {

        decalerVersBasSiVide();

        supprimerCasesSiPlus6vertical();
        supprimerCasesSiPlus5vertical();
        supprimerCasesSiPlus4vertical();
        supprimerCasesSiPlus3vertical();

    }

    /*!\brief Collapser les cases   */
    public void collapserLesCases() {

        decalerVersBasSiVide();

        collapserHorizontalement();

        decalerVersBasSiVide();

        collapserVerticalement();

        decalerVersBasSiVide();

    }

    /*!\brief Déplacer les cases vers le bas si elle se trouvent sur un vide  */
    public void decalerVersBasSiVide() {

        // Décalage vers le bas si y'a un vide
        for (int i = 1; i < mapHeight; i++)
        {
            for (int j = 1; j < mapWidth; j++)
            {

                if (map[i][j] == 0 && map[i - 1][j] != 0 ) {
                    for (int c = i; c > 0; c--) {
                        map[c][j] = map[c - 1][j];
                        map[c - 1][j] = 0;

                    }
                    sleep();
                }

            }
            j=0;

        }


    }

    /*!\brief Faire des pauses  */
    public void sleep() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /*!\brief Afficher fenre d'alerte du joueur perdant  */
    public void lost() {

        count.cancel();
        AlertDialog.Builder alert = new AlertDialog.Builder(memocontext);
        alert.setTitle("GAME OVER");
        alert.setIcon(R.drawable.gameover);

        TextView l_viewabout = new TextView(memocontext);

        l_viewabout.setText("Your score: " + score+ "Moves left " +numberOfTry/2);
        l_viewabout.setGravity(FOCUS_UP);
        l_viewabout.setBackgroundColor(Color.LTGRAY);
        l_viewabout.setTextColor(Color.RED);
        l_viewabout.setTextSize(25);
        alert.setView(l_viewabout);
        alert.setPositiveButton("replay",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        initparameters();

                    }
                });
        alert.setNegativeButton("menu",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count.cancel();
                        Intent intent = new Intent(memocontext,
                                MainActivity.class);

                        memocontext.startActivity(intent);

                    }
                });

        alert.show();
    }

    /*!\brief Afficher une fenêtre d'alerte du joueur gagnant  */
    public void won() {


        AlertDialog.Builder alert = new AlertDialog.Builder(memocontext);
        alert.setTitle("congratulation ");
        //alert.setIcon(R.drawable.back);
        count.cancel();
        TextView l_viewabout = new TextView(memocontext);

        //l_viewabout.setText("Your score: " + score);

        l_viewabout.setText("Bravo Level " + (Next_Level - 1) + " won Your score: " + score);

        l_viewabout.setGravity(FOCUS_UP);

        l_viewabout.setBackgroundColor(Color.LTGRAY);

        l_viewabout.setTextColor(Color.BLUE);

        l_viewabout.setTextSize(18);

        alert.setView(l_viewabout);

        alert.setPositiveButton("Next Level ",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count.cancel();
                        initparameters();

                    }
                });


        alert.setOnCancelListener(new android.content.DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        alert.show();
    }

    /*!\brief Sauvegarde du meilleure  score  */
    public void savescore() {
        int a = this.score;
        if (a > bestScore) {

            SharedPreferences Prefs = memocontext.getSharedPreferences("prefs",
                    0);
            SharedPreferences.Editor prefsEditor = Prefs.edit();
            prefsEditor.putInt("bestscore", score);
            prefsEditor.commit();
        } else {
            SharedPreferences Prefs = memocontext.getSharedPreferences("prefs",
                    0);
            SharedPreferences.Editor prefsEditor = Prefs.edit();
            prefsEditor.putInt("bestscore", bestScore);
            prefsEditor.commit();
        }
    }

    /*!\brief Récupération du score  */
    public void recupScore() {
        SharedPreferences Prefs = memocontext.getSharedPreferences("prefs", 0);
        SharedPreferences.Editor prefsEditor = Prefs.edit();
        bestScore = Prefs.getInt("bestscore", 0);

    }


    public void onPause() {
        in = false;

        while (in != true) {

            try {
                cv_thread.join();
            } catch (InterruptedException i) {
                i.printStackTrace();
            }
            break;
        }
        cv_thread = null;
    }

    public void onResume() {
        in = true;

        cv_thread = new Thread(this);
        cv_thread.start();

    }

    public void onCancel() {

        this.onPause();

        toast.cancel();
    }



    public void pause()
    {
        in = false;

        while (in != true) {

            try {
                cv_thread.join();
            } catch (InterruptedException i)
            {
                i.printStackTrace();
            }
            break;
        }
        cv_thread = null;
    }


    /*!\brief Afficher fenre d'alerte du joueur perdant  */
    public void pauseAlerte() {

        AlertDialog.Builder alert = new AlertDialog.Builder(memocontext);
        alert.setTitle("Pause");


        TextView l_viewabout = new TextView(memocontext);

        count.cancel();
        l_viewabout.setGravity(FOCUS_UP);
        l_viewabout.setBackgroundColor(Color.LTGRAY);
        l_viewabout.setTextColor(Color.RED);
        l_viewabout.setTextSize(25);
        alert.setView(l_viewabout);
        alert.setPositiveButton("Continue",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count.start();


                    }
                });
        alert.setNegativeButton("menu",
                new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count.cancel();
                        Intent intent = new Intent(memocontext,
                                MainActivity.class);

                        memocontext.startActivity(intent);

                    }
                });

        alert.show();
    }
}