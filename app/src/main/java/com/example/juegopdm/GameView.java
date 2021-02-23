package com.example.juegopdm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameView extends SurfaceView  {
    private Hilo hilo;
    boolean win = false;
    private CharacterSprites characterSprite;
    private Fondo fondo;
    private boolean collision;
    public static int altura = 500;
    private puntaje puntaje;
    private boolean gameOver;
    //Velocidad para controlar movimiento de la pantalla
    public static int velocity = 20;

    public Obstaculos obstaculo1, obstaculo2, obstaculo3;

    private int altoPantalla = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int anchoPantalla = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int puntajes;

    private MediaPlayer point;
    private MediaPlayer golpe;
    private MediaPlayer pierde;
    private MediaPlayer victoria;


    public GameView(Context context, AttributeSet attributes) {
        super(context, attributes);
        inicioSonidos();
        hilo = new Hilo(getHolder(), this);
        setFocusable(true);
        gameOver = false;
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                makeLevel();
                hilo.setRunning(true);
                hilo.start();

            }
            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                boolean retry = true;
                while (retry) {
                    try {
                        hilo.setRunning(false);
                        hilo.join();
                       characterSprite.manager.unregisterListener(characterSprite);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    retry = false;
                }
            }
        });


    }

    public void inicioSonidos(){
        point = MediaPlayer.create(getContext(), R.raw.point);
        golpe = MediaPlayer.create(getContext(), R.raw.hit);
        pierde = MediaPlayer.create(getContext(), R.raw.die);
        victoria = MediaPlayer.create(getContext(), R.raw.victoria);
    }


    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // Matriz para manipular
        Matrix matrix = new Matrix();
        // Reescalado de la imagen con las nuevas medidas
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, true);
        bm.recycle();
        return resizedBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void makeLevel() {
        puntaje = new puntaje(getResources(), altoPantalla, anchoPantalla);
        puntajes=0;
        puntaje.collision(getContext().getSharedPreferences("Best", Context.MODE_PRIVATE));
        fondo = new Fondo(getResources(), altoPantalla);
        characterSprite = new CharacterSprites
                (getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.spritenormal), 300, 240), this.getContext());
        characterSprite.x = anchoPantalla/3;
        Bitmap bmp;
        Bitmap bmp2;
        bmp = getResizedBitmap(BitmapFactory.decodeResource
                (getResources(), R.drawable.obstaculo_arriba), 500, altoPantalla/2);
        bmp2 = getResizedBitmap
                (BitmapFactory.decodeResource(getResources(), R.drawable.obstaculos_abajo), 500, altoPantalla/2 );

        obstaculo1 = new Obstaculos(bmp, bmp2, 2000, 100);
        obstaculo2 = new Obstaculos(bmp, bmp2, 4500, 100);
        obstaculo3 = new Obstaculos(bmp, bmp2, 3200, 100);
        puntajes = 0;

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void update() {
        if(!gameOver) {
            controlColisiones();
            obstaculo1.update();
            obstaculo2.update();
            obstaculo3.update();
        }

        }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            fondo.draw(canvas);
            if (!gameOver && !win) {
                characterSprite.draw(canvas);
                obstaculo1.draw(canvas);
                obstaculo2.draw(canvas);
                obstaculo3.draw(canvas);
                puntaje.draw(canvas);
            } if(gameOver==true) {
                puntaje.draw(canvas);
            } if(win==true){

            }
        }
    }
    //Logica para la colisión de tubos.
    public void controlColisiones() {
        List<Obstaculos> tubos = new ArrayList<>();
        tubos.add(obstaculo1);
        tubos.add(obstaculo2);
        tubos.add(obstaculo3);

        for (int i = 0; i < tubos.size(); i++) {
            collision = false;
            if (characterSprite.y < obstaculo1.yY + (altoPantalla / 2) - (altura / 2)
                    && characterSprite.x + 200 > obstaculo1.xX && characterSprite.x < obstaculo1.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }

            if (characterSprite.y < obstaculo2.yY + (altoPantalla / 2) - (altura / 2)
                    && characterSprite.x + 200 > obstaculo2.xX && characterSprite.x < obstaculo2.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }

            if (characterSprite.y < obstaculo3.yY + (altoPantalla / 2) - (altura / 2)
                    && characterSprite.x + 200 > obstaculo3.xX && characterSprite.x < obstaculo3.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }

            if (characterSprite.y + 240 > (altoPantalla / 2) + (altura / 2) + obstaculo1.yY
                    && characterSprite.x + 200 > obstaculo1.xX && characterSprite.x < obstaculo1.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }

            if (characterSprite.y + 240 > (altoPantalla / 2) + (altura / 2) + obstaculo2.yY
                    && characterSprite.x + 200 > obstaculo2.xX && characterSprite.x < obstaculo2.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }

            if (characterSprite.y + 240 > (altoPantalla / 2) + (altura / 2) + obstaculo3.yY
                    && characterSprite.x + 200 > obstaculo3.xX && characterSprite.x < obstaculo3.xX + 500) {
                golpe.start();
                pierde.start();
                gameOver = true;
            }
            //comprobamos si los tubos estan fuera de la pantalla y los volvemos a crear con un numero aleatorio con maximo en 500.
            //Cuando sale fuera actualizamos nuestro contador en +1
            if (!gameOver) {
                if (tubos.get(i).xX + 500 < 0) {
                    puntajes++;
                    puntaje.updateScore(puntajes);
                    point.start();
                    if (puntajes % 2 == 0) {
                        velocity++;
                    }
                    if (puntajes == 30){
                        win =true;
                        golpe.stop();
                        pierde.stop();
                        victoria.start();
                    }
                    Random r = new Random();
                    int value2 = r.nextInt(500);
                    tubos.get(i).xX = anchoPantalla + 300 + 1000;
                    tubos.get(i).yY = value2 - 250;
                }
            }

        }
        //Detect if the character has gone off the bottom or top of the screen
        if (characterSprite.y< 0) {
            golpe.start();
            pierde.start();
            gameOver=true;}

        if (characterSprite.y > altoPantalla) {
            golpe.start();
            pierde.start();
            gameOver=true; }
    }
    public void resetLevel() {
        win = false;
        collision = true;
        gameOver = false;
        puntajes = 0;
        if(collision){
            puntaje.collision(getContext().getSharedPreferences("Best", Context.MODE_PRIVATE));
        }
        characterSprite.y = altoPantalla/2;
        characterSprite.x = anchoPantalla/3;
        obstaculo1.xX = 2000;
        obstaculo1.yY = 0;
        obstaculo2.xX = 4500;
        obstaculo2.yY = 100;
        obstaculo3.xX = 3500;
        obstaculo3.yY = 250;
        puntaje.updateScore(puntajes);
        velocity = 20;
    }
}
