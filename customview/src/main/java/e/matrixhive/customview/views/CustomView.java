package e.matrixhive.customview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View {
    private int id;
    private Paint paintFill;
    private Path pathFill;


    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);

    }

    private void init(@Nullable AttributeSet attrs) {
        paintFill = new Paint();
        paintFill.setAntiAlias(true);

        pathFill = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
        paintFill.setStyle(Paint.Style.FILL);
        if (id > 90) {
            pathFill.addRect(0, 0, getWidth(), getHeight(), Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id > 75 && id <= 90){
            pathFill.addRect(0, 0, getWidth(), (float) (getHeight()/1.1), Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id > 60 && id <= 75){
            pathFill.addRect(0, 0, getWidth(), (float) (getHeight()/1.5), Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id > 45 && id <= 60){
            pathFill.addRect(0, 0, getWidth(), getHeight()/2, Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id > 30 && id <= 45){
            pathFill.addRect(0, 0, getWidth(), (float) (getHeight()/2.5), Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id > 20 && id <= 30){
            pathFill.addRect(0, 0, getWidth(), getHeight()/5, Path.Direction.CW);
            paintFill.setColor(Color.GREEN);
        }
        if (id <= 20){
            pathFill.addRect(0, 0, getWidth(), getHeight()/10, Path.Direction.CW);
            paintFill.setColor(Color.RED);
        }
        canvas.save();
        canvas.rotate(
                180, // degrees
                canvas.getWidth() / 2,
                canvas.getHeight() / 2
        );
        canvas.drawPath(pathFill, paintFill);
        canvas.restore();



        paintFill.setColor(Color.BLACK);
        paintFill.setTextSize(50);

        paintFill.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(id)+"%", getWidth()/2, getHeight()/2, paintFill);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    public void showImage(int level) {
        id = level;
    }
}


