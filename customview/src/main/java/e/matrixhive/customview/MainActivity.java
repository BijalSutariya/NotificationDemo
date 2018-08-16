package e.matrixhive.customview;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.MessageFormat;

import e.matrixhive.customview.views.CustomView;

public class MainActivity extends AppCompatActivity {

    private CustomView view;
    private TextView tvBattery;
    private Handler handler;
    private Runnable runnable;
    private int scale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.view);
        tvBattery = findViewById(R.id.tv_battery);

        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();

                tvBattery.setText(MessageFormat.format("BATTERY:{0} %", level));

                view.showImage(level);

                handler.postDelayed(runnable, 2000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    public float batteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = 0;
        if (batteryIntent != null) {
            level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        if (level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float) level / (float) scale) * 100.0f;
    }
}
