package ecellapp.sam10795.com.startuppo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by SAM10795 on 19-02-2016.
 */
public class SUPDB extends SQLiteOpenHelper {

    public static final String TABLE_SUP = "startups";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_STARTUP_NAME = "sup_name";
    public static final String COLUMN_STARTUP_LVL = "sup_lvl";
    public static final String COLUMN_STARTUP_FNDT = "sup_fndt";
    public static final String COLUMN_STARTUP_FOUNDERS = "sup_fnd";
    public static final String COLUMN_STARTUP_FUNDING = "sup_fndng";
    public static final String COLUMN_STARTUP_INFO = "sup_info";
    public static final String COLUMN_STARTUP_EVAL = "sup_eval";
    public static final String COLUMN_STARTUP_TRADED = "sup_trade";
    public static final String COLUMN_STARTUP_ISLEGEND = "sup_legend";
    public static final String COLUMN_STARTUP_ZONE = "sup_zone";
    public static final String COLUMN_STARTUP_TYPE = "sup_type";
    public static final String COLUMN_STARTUP_BAG = "sup_bag";


    private static final String DATABASE_NAME = "startup.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_SUP + "(" + COLUMN_ID + " integer primary key autoincrement, "
            +COLUMN_STARTUP_BAG + " integer, "
            +COLUMN_STARTUP_EVAL+ " text, "
            +COLUMN_STARTUP_NAME + " text not null, "
            +COLUMN_STARTUP_INFO + " text, "
            +COLUMN_STARTUP_LVL + " integer, "
            +COLUMN_STARTUP_FNDT + " text, "
            +COLUMN_STARTUP_TRADED + " integer, "
            +COLUMN_STARTUP_ISLEGEND+ " integer, "
            +COLUMN_STARTUP_ZONE+ " text, "
            +COLUMN_STARTUP_TYPE+ " text, "
            +COLUMN_STARTUP_FOUNDERS+ " text, "
            +COLUMN_STARTUP_FUNDING +" text );";

    public SUPDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i("Database","Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);

        Log.i("Database","Created2");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SUPDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUP);
        onCreate(db);
    }

}
