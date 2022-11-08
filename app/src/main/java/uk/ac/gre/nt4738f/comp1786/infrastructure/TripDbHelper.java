package uk.ac.gre.nt4738f.comp1786.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;

import uk.ac.gre.nt4738f.comp1786.core.entities.Trip;

public class TripDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TripDbHelper.db";
    public static final String TABLE_TRIP = "Trips";
    public static final String TABLE_EXPENSE = "Expenses";

    public TripDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

//    @Override
//    public void onConfigure(SQLiteDatabase db) {
//        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_TRIP + " (" +
                        " \"Id\" INTEGER NOT NULL CONSTRAINT \"PK_Trips\" PRIMARY KEY AUTOINCREMENT," +
                        " \"Name\" TEXT NOT NULL," +
                        " \"Destination\" TEXT NOT NULL," +
                        " \"Date\" TEXT NOT NULL,\n" +
                        " \"IsRiskAssessment\" INTEGER NOT NULL," +
                        " \"Description\" TEXT" +
                        ");"
        );
        db.execSQL(
                "CREATE TABLE " + TABLE_EXPENSE + " (" +
                        " \"Id\" INTEGER NOT NULL CONSTRAINT \"PK_Expenses\" PRIMARY KEY AUTOINCREMENT," +
                        " \"TripId\" INTEGER NOT NULL," +
                        " \"Type\" TEXT NOT NULL," +
                        " \"Time\" TEXT NOT NULL," +
                        " \"Amount\" REAL NOT NULL," +
                        " CONSTRAINT \"FK_Expenses_Trips_TripId\" FOREIGN KEY (\"TripId\") REFERENCES \"Trips\" (\"Id\") ON DELETE CASCADE" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
        onCreate(db);
    }

    public ArrayList<Trip> listTrips() {
        String sql = "select * from " + TABLE_TRIP;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Trip> storeTrips = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String destination = cursor.getString(2);
                LocalDate date = LocalDate.parse(cursor.getString(3));
                Boolean isRiskAssessment = cursor.getInt(4) == 1;
                String description = cursor.getString(5);
                storeTrips.add(new Trip(id, name, destination, date, isRiskAssessment, description));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeTrips;
    }

    public long insertTripOrThrow(Trip trip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = trip.toContentValues();

        return db.insertOrThrow(TripDbHelper.TABLE_TRIP, null, cv);
    }
}