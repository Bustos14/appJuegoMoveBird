package com.example.juegopdm;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

class CharacterSprites implements SensorEventListener {
    private Bitmap subida;
    private Bitmap bajada;
    private Bitmap image;
    public int x, y;
    private int width;
    private int height;
    final int X = 0;
    final int Y = 1;
    Sensor accelerometer;
    SensorManager manager;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CharacterSprites (Bitmap bmp, Context context) {
        image = bmp;
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //comprobamos si hay acelerometro y si lo hay que coja el primero
        if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
            accelerometer = manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            if (!manager.registerListener(this, accelerometer, 200)) {
                Toast.makeText(context, "No dispone de acelerometro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        y +=event.values[Y];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


}