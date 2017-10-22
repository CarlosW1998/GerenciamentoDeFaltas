package Strategy;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import projectp3.studio.com.gerenciamentodefaltas.AddFalta;
import projectp3.studio.com.gerenciamentodefaltas.R;
import projectp3.studio.com.gerenciamentodefaltas.SituDaMat;

/**
 * Created by Lucas on 17/10/2017.
 */
public class StrategyFuncs extends Activity {

    private Context context;

    public StrategyFuncs(Context c){
        this.context = c;
    }


    public String calcStatus(Integer fA, Integer maxF){
        //ACEITAVEL -> ate 50% [0, 50)
        //PERIGOSO -> entre 50% e 90% [50, 90)
        //CRITICO -> mais de 90% [90, 100]
        //ULTRAPASSADO -> mais de 100% (100, +inf)

        int nvl = (int)((fA*100)/maxF);
        if( nvl < 50 ){
            return "ACEITÁVEL";
        }else if (nvl >= 50 && nvl < 80){
            return "PERIGOSO!";
        }else if (nvl >= 80 && nvl <= 100){
            return "CRÍTICO!!!";
        }else{
            return "LIMITE ULTRAPASSADO!";
        }
    }


    public boolean hasNext (Cursor c){
        try{
            c.moveToNext();
            return true;
        }catch (CursorIndexOutOfBoundsException e){
            return false;
        }
    }

}
