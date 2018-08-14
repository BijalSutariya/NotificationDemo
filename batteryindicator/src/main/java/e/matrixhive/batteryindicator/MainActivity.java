package e.matrixhive.batteryindicator;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {

    private ImageView ivBattery;
    private TextView tvBattery;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivBattery = findViewById(R.id.iv_battery);
        tvBattery = findViewById(R.id.tv_battery);

        runnable = new Runnable() {
            @Override
            public void run() {
                int level = (int) batteryLevel();

                tvBattery.setText(MessageFormat.format("BATTERY:{0} %", level));

                if (level > 90) {
                    ivBattery.setImageResource(R.drawable.ic_battery_90_black_24dp);
                }
                if (level > 60 && level <= 89) {
                    ivBattery.setImageResource(R.drawable.ic_battery_60_black_24dp);
                }
                if (level > 50 && level <= 59) {
                    ivBattery.setImageResource(R.drawable.ic_battery_50_black_24dp);
                }
                if (level > 30 && level <= 49) {
                    ivBattery.setImageResource(R.drawable.ic_battery_30_black_24dp);
                }
                if (level > 5 && level <= 29) {
                    ivBattery.setImageResource(R.drawable.ic_battery_20_black_24dp);
                }
                if (level <= 5) {
                    ivBattery.setImageResource(R.drawable.ic_battery_alert_black_24dp);
                }
                handler.postDelayed(runnable, 2000);
            }
        };
        handler = new Handler();
        handler.postDelayed(runnable, 0);
    }

    public float batteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }
}
