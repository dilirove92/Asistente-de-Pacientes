package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.alarmas.EventoContract.Alarm;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class EventoDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "eventosclock.db";

    
	private static final String SQL_CREATE_ALARM = "CREATE TABLE " + Alarm.TABLE_NAME + " (" +
			Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			Alarm.COLUMN_NAME_ALARM_YEAR + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_MONTH + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_DAY + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_HOUR + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_MINUTES + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_IDUSER + " LONG," +
			Alarm.COLUMN_NAME_ALARM_TIPOUSER + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_USER + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_DATE + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_LOCATION + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_DESCRIPTION + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_TONE + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" +
	    " )";
	
	private static final String SQL_DELETE_ALARM =
		    "DROP TABLE IF EXISTS " + Alarm.TABLE_NAME;
    
	public EventoDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ALARM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ALARM);
        onCreate(db);
	}
	
	private EventoModel populateModel(Cursor c) {
		EventoModel model = new EventoModel();
		model.Id = c.getLong(c.getColumnIndex(Alarm._ID));
		model.Year = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_YEAR));
		model.Month = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_MONTH));
		model.Day = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_DAY));
		model.Hour = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_HOUR));
		model.Minutes = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_MINUTES));
		model.IdUser = c.getLong(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_IDUSER));
		model.TipoUser = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIPOUSER));
		model.User = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_USER));
		model.Name = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME));
		model.Date = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_DATE));
		model.Location = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_LOCATION));
		model.Description = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_DESCRIPTION));		
		model.AlarmTone = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE));
		model.IsEnabled = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true;
		
		return model;
	}
	
	private ContentValues populateContent(EventoModel model) {
		ContentValues values = new ContentValues();
        values.put(Alarm.COLUMN_NAME_ALARM_YEAR, model.Year);
        values.put(Alarm.COLUMN_NAME_ALARM_MONTH, model.Month);
        values.put(Alarm.COLUMN_NAME_ALARM_DAY, model.Day);
        values.put(Alarm.COLUMN_NAME_ALARM_HOUR, model.Hour);
        values.put(Alarm.COLUMN_NAME_ALARM_MINUTES, model.Minutes);
		values.put(Alarm.COLUMN_NAME_ALARM_IDUSER, model.IdUser);
		values.put(Alarm.COLUMN_NAME_ALARM_TIPOUSER, model.TipoUser);
		values.put(Alarm.COLUMN_NAME_ALARM_USER, model.User);
        values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.Name);
        values.put(Alarm.COLUMN_NAME_ALARM_DATE, model.Date);
        values.put(Alarm.COLUMN_NAME_ALARM_LOCATION, model.Location);
        values.put(Alarm.COLUMN_NAME_ALARM_DESCRIPTION, model.Description);        
        values.put(Alarm.COLUMN_NAME_ALARM_TONE, model.AlarmTone);
        values.put(Alarm.COLUMN_NAME_ALARM_ENABLED, model.IsEnabled);
                
        return values;
	}
	
	public long createAlarm(EventoModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().insert(Alarm.TABLE_NAME, null, values);
	}
	
	public long updateAlarm(EventoModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().update(Alarm.TABLE_NAME, values, Alarm._ID + " = ?", new String[] { String.valueOf(model.Id) });
	}
	
	public EventoModel getAlarm(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Alarm.TABLE_NAME + " WHERE " + Alarm._ID + " = " + id;
		
		Cursor c = db.rawQuery(select, null);
		
		if (c.moveToNext()) {
			return populateModel(c);
		}		
		return null;
	}
	
	public List<EventoModel> getAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Alarm.TABLE_NAME;
		
		Cursor c = db.rawQuery(select, null);
		
		List<EventoModel> alarmList = new ArrayList<EventoModel>();
		
		while (c.moveToNext()) {
			alarmList.add(populateModel(c));
		}
		
		if (!alarmList.isEmpty()) {
			return alarmList;
		}
		return null;
	}
	
	public int deleteAlarm(long id) {
		return getWritableDatabase().delete(Alarm.TABLE_NAME, Alarm._ID + " = ?", new String[] { String.valueOf(id) });
	}
	
	
}