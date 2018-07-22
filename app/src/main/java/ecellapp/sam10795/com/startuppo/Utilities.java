package ecellapp.sam10795.com.startuppo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by SAM10795 on 03-03-2017.
 */

public class Utilities {

    public Utilities()
    {
    }


    ArrayList<Tower> towers;

    public List<String[]> getRecords(Context context)
    {
        List<String[]> records = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.startups_2);
            CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(is)),'\t');
            records = reader.readAll();
            reader.close();
        }
        catch (IOException io)
        {
            Log.e("IOException","GetRecords");
        }
        return records;
    }

    public User getUser(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PKDT", MODE_PRIVATE);
        User user = new User();
        user.setHostel(sharedPreferences.getString("Hostel",""));
        user.setName(sharedPreferences.getString("Name",""));
        user.setRollno(sharedPreferences.getString("RollNo",""));
        user.setScore(sharedPreferences.getInt("Score",100));
        user.setWins(sharedPreferences.getInt("Win",0));
        return user;
    }

    public void setUser(User user, Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences("PKDT", MODE_PRIVATE).edit();
        editor.putString("Hostel",user.getHostel());
        editor.putString("Name",user.getName());
        editor.putString("RollNo",user.getRollno());
        editor.putInt("Score",user.getScore());
        editor.putInt("Win",user.getWins());
        editor.apply();
    }

    public String formatname(String name)
    {
        String nameL = name.toLowerCase();
        String ret = "";
        for(int i=0;i<name.length();i++)
        {
            if(nameL.charAt(i)>=97&&nameL.charAt(i)<=122)
            {
                ret+=nameL.charAt(i);
            }
        }
        return ret;
    }


    public int getvaluation(String s, List<String []> records)
    {
        for(String record[] : records)
        {
            if(record[0].equalsIgnoreCase(s))
            {
                return Integer.parseInt(record[3]);
            }
            // Name - Type - Founding Date - Valuation - Info - Founders - Funding - Legendary
        }
        return 0;
    }

    public int islegend(String s, List<String []> records)
    {
        for(String record[] : records)
        {
            if(record[0].equalsIgnoreCase(s))
            {
                return Integer.parseInt(record[7]);
            }
            // Name - Type - Founding Date - Valuation - Info - Founders - Funding - Legendary
        }
        return 0;
    }

    public void updatescore(Context context)
    {
        SUPDataSource supDataSource = new SUPDataSource(context);
        supDataSource.open();
        ArrayList<SUP> sups = supDataSource.getAllSUP();
        supDataSource.close();
        int legendcount=0,tradecount=0,pkcount;
        Set<String> strings  = new LinkedHashSet<>();
        int lvtot = 0;
        for(SUP p:sups)
        {
            strings.add(p.getName());
            lvtot+=p.getLv();
            if(p.isLegend())
            {
                legendcount++;
            }
            if(p.isTraded())
            {
                tradecount++;
            }
        }
        pkcount = strings.size();
        User user = getUser(context);
        DatabaseReference userbase = FirebaseDatabase.getInstance().getReference(context.getResources().getString(R.string.Users)).child(user.getRollno());
        SharedPreferences sharedPreferences = context.getSharedPreferences("PKDT", MODE_PRIVATE);
        int win = sharedPreferences.getInt("Win",0);
        int score = 250*legendcount+5*tradecount+15*pkcount+lvtot+150*win + 100;//TODO: 100*legendcount+5*tradecount+10*pkcount+lvtot+50*win + 100;
        user.setScore(score);
        sharedPreferences.edit().putInt("Score",score).apply();
        userbase.setValue(user);
    }

}
