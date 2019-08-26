package com.example.administrator.speedviocatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
//import java.awt.Color;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

public class RoundedTransformation implements com.squareup.picasso.Transformation {
    private final int radius;
    private final int margin;  // dp

    private final int ORANGE = 0xffffa500;//0xFFFF3300;
    // radius is corner radii in dp
    // margin is the board in dp
    public RoundedTransformation(final int radius, final int margin) {
        this.radius = radius;
        this.margin = margin;
    }

    @Override
    public Bitmap transform(final Bitmap source) {
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        trianglePaint.setStrokeWidth(1/2);//2
        trianglePaint.setColor(ORANGE);
        trianglePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        trianglePaint.setAntiAlias(true);

        Path triangle = new Path();
        triangle.setFillType(Path.FillType.EVEN_ODD);
        triangle.moveTo(radius, source.getHeight() / 2);
        triangle.lineTo(source.getWidth()/2,source.getHeight());
        triangle.lineTo((source.getWidth()-radius)-((radius*3)/2),source.getHeight()/2);
        triangle.close();

        canvas.drawPath(triangle, trianglePaint);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        canvas.drawCircle((source.getWidth() - margin)/2, (source.getHeight() - margin)/2, radius-2, paint);


        if (source != output) {
            source.recycle();
        }

        Paint paint1 = new Paint();
        paint1.setColor(ORANGE);
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);
        paint1.setStrokeWidth(5);
        canvas.drawCircle((source.getWidth() - margin)/2, (source.getHeight() - margin)/2, radius-2, paint1);


        return output;
    }

    @Override
    public String key() {
        return "rounded";
    }
}