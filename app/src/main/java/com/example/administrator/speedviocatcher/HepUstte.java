/*
package com.example.administrator.speedviocatcher;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class HepUstte extends Service {
    HUDView mView;

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(getBaseContext(),"onCreate", Toast.LENGTH_LONG).show();

        final Bitmap kangoo = BitmapFactory.decodeResource(getResources(),
                R.drawable.add);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                kangoo.getWidth(),
                kangoo.getHeight(),
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        |WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        |WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT);






        params.gravity = Gravity.LEFT | Gravity.BOTTOM;
        params.setTitle("Load Average");
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);



        mView = new HUDView(this,kangoo);

        mView.setOnTouchListener(new OnTouchListener() {


            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                // TODO Auto-generated method stub
                //Log.e("kordinatlar", arg1.getX()+":"+arg1.getY()+":"+display.getHeight()+":"+kangoo.getHeight());
                if(arg1.getX()<kangoo.getWidth() & arg1.getY()>0)
                {
                    Log.d("tıklandı", "touch me");
                }
                return false;
            }
        });


        wm.addView(mView, params);



    }



    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

}



@SuppressLint("DrawAllocation")
class HUDView extends ViewGroup {


    Bitmap kangoo;

    public HUDView(Context context,Bitmap kangoo) {
        super(context);

        this.kangoo=kangoo;



    }


    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);


        // delete below line if you want transparent back color, but to understand the sizes use back color
        canvas.drawColor(Color.BLACK);

        canvas.drawBitmap(kangoo,0 , 0, null);


        //canvas.drawText("Hello World", 5, 15, mLoadPaint);

    }


    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        // Toast.makeText(getContext(),"onTouchEvent", Toast.LENGTH_LONG).show();

        return true;
    }
}*/
