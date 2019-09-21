package workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.workmanagerdemo.MainActivity;
import com.example.workmanagerdemo.R;

public class OneTimeWork extends Worker {
    private Context context;

    public OneTimeWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String title = data.getString(MainActivity.TITLE_KEY);
        String description = data.getString(MainActivity.TEXT_KEY);
        sendNotification(title, description);
        return Result.success();
    }

    private void sendNotification(String title, String text) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "work-manager-demo";
        String CHANNEL_NAME = "WorkManagerDemo";
        int NOTIFICATION_ID = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
