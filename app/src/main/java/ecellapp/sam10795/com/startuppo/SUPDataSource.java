package ecellapp.sam10795.com.startuppo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by SAM10795 on 19-02-2016.
 */
public class SUPDataSource {

    // Database fields
    private SQLiteDatabase database;
    private SUPDB dbHelper;
    private String[] allColumns = { SUPDB.COLUMN_ID,SUPDB.COLUMN_STARTUP_BAG,
            SUPDB.COLUMN_STARTUP_EVAL,SUPDB.COLUMN_STARTUP_NAME, SUPDB.COLUMN_STARTUP_INFO,
            SUPDB.COLUMN_STARTUP_LVL,SUPDB.COLUMN_STARTUP_FNDT, SUPDB.COLUMN_STARTUP_TRADED,
            SUPDB.COLUMN_STARTUP_ISLEGEND, SUPDB.COLUMN_STARTUP_ZONE, SUPDB.COLUMN_STARTUP_TYPE,
            SUPDB.COLUMN_STARTUP_FOUNDERS,SUPDB.COLUMN_STARTUP_FUNDING,};

    public SUPDataSource(Context context) {
        dbHelper = new SUPDB(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SUP createSUP(SUP sup) {
        ContentValues values = new ContentValues();
        values.put(SUPDB.COLUMN_STARTUP_BAG, sup.getBag());
        values.put(SUPDB.COLUMN_STARTUP_EVAL, sup.getEvaluation());
        values.put(SUPDB.COLUMN_STARTUP_NAME, sup.getName());
        values.put(SUPDB.COLUMN_STARTUP_INFO, sup.getInfo());
        values.put(SUPDB.COLUMN_STARTUP_LVL, sup.getLv());
        values.put(SUPDB.COLUMN_STARTUP_FNDT, sup.getFoundate());
        values.put(SUPDB.COLUMN_STARTUP_TRADED, sup.isTraded());
        values.put(SUPDB.COLUMN_STARTUP_ISLEGEND,sup.isLegend());
        values.put(SUPDB.COLUMN_STARTUP_ZONE,sup.getZone());
        values.put(SUPDB.COLUMN_STARTUP_TYPE,sup.getType());
        values.put(SUPDB.COLUMN_STARTUP_FOUNDERS,sup.getFounders());
        values.put(SUPDB.COLUMN_STARTUP_FUNDING,sup.getFunding());
        long insertId = database.insert(SUPDB.TABLE_SUP, null,
                values);
        Cursor cursor = database.query(SUPDB.TABLE_SUP,
                allColumns, SUPDB.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        SUP newSup = cursorToSUP(cursor);
        cursor.close();
        return newSup;
    }

    public void releaseSUP(SUP sup) {
        long id = sup.getId();
        System.out.println(sup.getName()+" released");
        database.delete(SUPDB.TABLE_SUP, SUPDB.COLUMN_ID
                + " = " + id, null);
    }

    public void updateSUP(SUP sup)
    {
        long id = sup.getId();
        ContentValues values = new ContentValues();
        values.put(SUPDB.COLUMN_STARTUP_BAG, sup.getBag());
        values.put(SUPDB.COLUMN_STARTUP_EVAL, sup.getEvaluation());
        values.put(SUPDB.COLUMN_STARTUP_NAME, sup.getName());
        values.put(SUPDB.COLUMN_STARTUP_INFO, sup.getInfo());
        values.put(SUPDB.COLUMN_STARTUP_LVL, sup.getLv());
        values.put(SUPDB.COLUMN_STARTUP_FNDT, sup.getFoundate());
        values.put(SUPDB.COLUMN_STARTUP_TRADED, sup.isTraded());
        values.put(SUPDB.COLUMN_STARTUP_ISLEGEND,sup.isLegend());
        values.put(SUPDB.COLUMN_STARTUP_ZONE,sup.getZone());
        values.put(SUPDB.COLUMN_STARTUP_TYPE,sup.getType());
        values.put(SUPDB.COLUMN_STARTUP_FOUNDERS,sup.getFounders());
        values.put(SUPDB.COLUMN_STARTUP_FUNDING,sup.getFunding());
        database.update(SUPDB.TABLE_SUP,values,SUPDB.COLUMN_ID + " = " + id, null);
    }

    public ArrayList<SUP> getAllSUP() {
        ArrayList<SUP> startups = new ArrayList<>();

        Cursor cursor = database.query(SUPDB.TABLE_SUP,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SUP sup = cursorToSUP(cursor);
            startups.add(sup);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return startups;
    }

    private SUP cursorToSUP(Cursor cursor) {
        SUP sup = new SUP();
        /*sup.setId(cursor.getInt(14));
        sup.setBag(cursor.getInt(0));
        sup.setEvaluation(cursor.getString(1));
        sup.setName(cursor.getString(2));
        sup.setEvolveLv(cursor.getInt(7));
        sup.setEvolveTo(cursor.getString(10));
        sup.setZone(cursor.getString(9));
        sup.setType(cursor.getString(11));
        sup.setTraded(cursor.getInt(6)==1);
        sup.setLv(cursor.getInt(4));
        sup.setLegend(cursor.getInt(8)==1);
        sup.setInfo(cursor.getString(3));
        sup.setFunding(cursor.getString(13));
        sup.setFounders(cursor.getString(12));
        sup.setFoundate(cursor.getString(5));
*/
        sup.setId(cursor.getInt(0));
        sup.setBag(cursor.getInt(1));
        sup.setEvaluation(cursor.getString(2));
        sup.setName(cursor.getString(3));
        sup.setZone(cursor.getString(9));
        sup.setType(cursor.getString(10));
        sup.setTraded(cursor.getInt(7)==1);
        sup.setLv(cursor.getInt(5));
        sup.setLegend(cursor.getInt(8)==1);
        sup.setInfo(cursor.getString(4));
        sup.setFunding(cursor.getString(12));
        sup.setFounders(cursor.getString(11));
        sup.setFoundate(cursor.getString(6));
        return sup;
    }
}
