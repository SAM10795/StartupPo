package ecellapp.sam10795.com.startuppo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Created by SAM10795 on 06-03-2016.
 */

public class NotifReceiver extends BroadcastReceiver{

    public static final String NOTIFICATION_ID = "Notif";
    private Context context;

    private static final int ID_POKEBALL = 0;
    private static final int ID_GREATBALL = 1;
    private static final int ID_ULTRABALL = 2;
    private static final int ID_MASTERBALL = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.i("Notif","Received");

        int id = intent.getIntExtra(NOTIFICATION_ID,0);
        String ball = intent.getStringExtra("BALL NAME");
        SharedPreferences sharedPreferences = context.getSharedPreferences("PKDT",context.MODE_PRIVATE);
        sharedPreferences.getInt(ball,0);
        switch (id)
        {
            case ID_POKEBALL:
            {
                sharedPreferences.edit().putInt(ball,sharedPreferences.getInt(ball,0)+5).apply();
                Notify(R.drawable.cbag,"You have 5 more Pokeballs",ID_POKEBALL);
                break;
            }
            case ID_GREATBALL:
            {
                sharedPreferences.edit().putInt(ball,sharedPreferences.getInt(ball,0)+5).apply();
                Notify(R.drawable.gbag,"You have 5 more Great balls",ID_GREATBALL);
                break;
            }
            case ID_ULTRABALL:
            {
                sharedPreferences.edit().putInt(ball,sharedPreferences.getInt(ball,0)+5).apply();
                Notify(R.drawable.ubag,"You have 5 more Ultraballs",ID_ULTRABALL);
                break;
            }
            case ID_MASTERBALL:
            {
                sharedPreferences.edit().putInt(ball,sharedPreferences.getInt(ball,0)+1).apply();
                Notify(R.drawable.mbag,"You have 1 more Master ball",ID_MASTERBALL);
                break;
            }
        }

    }

    private void Notify(int drawable,String content,int id)
    {

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, Mainu.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        android.support.v4.app.NotificationCompat.Builder notifbuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Pokeballs Updated")
                .setContentIntent(pendingIntent)
                .setSmallIcon(drawable)
                .setContentText(content)
                .setSound(alarmSound);

        notificationManager.notify(id,notifbuilder.build());

    }
}