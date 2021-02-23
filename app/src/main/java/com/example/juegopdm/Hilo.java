package com.example.juegopdm;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Hilo extends Thread  {

        private GameView gameView;
        private SurfaceHolder surfaceHolder;
        private boolean running;
        public static Canvas canvas;
        private int FPS = 60;
        private double averageFPS;
        public boolean pausar;//Pausa el hilo cuando es true
        public boolean suspender;//Lo suspende cuando es true

        public Hilo(SurfaceHolder surfaceHolder, GameView gameView) {
            super();
            pausar = false;
            suspender = false;
            this.surfaceHolder = surfaceHolder;
            this.gameView = gameView;

        }
        synchronized void pausarhilo(){
        pausar=true;
        //lo siguiente garantiza que un hilo suspendido puede detenerse.
        suspender=false;
        notify();
        }
        //Suspender un hilo
        synchronized void suspenderhilo(){
        suspender=true;
        }
     //Renaudar un hilo
         synchronized void renaudarhilo(){
        suspender=false;
        notify();
        }

    @Override
        public void run() {
            long start;
            long tiempoMilisegundos;
            long espera;
            long tiempoTotal = 0;
            int cantidadFrames =0;
            long tiempo = 1000/FPS;


            while(running) {
                start = System.nanoTime();
                canvas = null;

                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gameView.update();
                        this.gameView.draw(canvas);
                    }

                } catch (Exception e) {
                }
                finally{
                    if(canvas!=null)
                    {
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                        catch(Exception e){e.printStackTrace();}
                    }
                }

                tiempoMilisegundos = (System.nanoTime() - start) / 1000000;
                espera = tiempo-tiempoMilisegundos;

                try{
                    this.sleep(espera);
                }catch(Exception e){}

                tiempoTotal += System.nanoTime()-start;
                cantidadFrames++;
                if(cantidadFrames == FPS)
                {
                    averageFPS = 1000/((tiempoTotal/cantidadFrames)/1000000);
                    cantidadFrames =0;
                    tiempoTotal = 0;
                }
            }

        }

        public void setRunning(boolean isRunning) {
            running = isRunning;
        }
    }

