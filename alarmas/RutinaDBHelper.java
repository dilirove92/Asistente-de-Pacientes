package com.Notifications.patientssassistant.alarmas;


import com.Notifications.patientssassistant.alarmas.RutinaContract.Alarm;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RutinaDBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rutinasclock.db";
	
    
	private static final String SQL_CREATE_ALARM = "CREATE TABLE " + Alarm.TABLE_NAME + " (" +
			Alarm._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
			Alarm.COLUMN_NAME_ALARM_IDUSER + " LONG," +
			Alarm.COLUMN_NAME_ALARM_TIPOUSER + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_USER + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_NAME + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_TIME_HOUR + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_TIME_MINUTE + " INTEGER," +
			Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS + " TEXT," +			
			Alarm.COLUMN_NAME_ALARM_TONE + " TEXT," +
			Alarm.COLUMN_NAME_ALARM_ENABLED + " BOOLEAN" +
	    " )";
	
	private static final String SQL_DELETE_ALARM = "DROP TABLE IF EXISTS " + Alarm.TABLE_NAME;
    
	public RutinaDBHelper(Context context) {
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
	
	private RutinaModel populateModel(Cursor c) {
		RutinaModel model = new RutinaModel();
		model.IdUser = c.getLong(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_IDUSER));
		model.TipoUser = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIPOUSER));
		model.User = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_USER));
		model.Name = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_NAME));
		model.TimeHour = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_HOUR));
		model.TimeMinute = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE));		
		model.AlarmTone = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_TONE));
		model.IsEnabled = c.getInt(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_ENABLED)) == 0 ? false : true;
		
		String[] repeatingDays = c.getString(c.getColumnIndex(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS)).split(",");
		for (int i = 0; i < repeatingDays.length; ++i) {
			model.setRepeatingDay(i, repeatingDays[i].equals("false") ? false : true);
		}
		
		return model;
	}
	
	private ContentValues populateContent(RutinaModel model) {
		ContentValues values = new ContentValues();
		values.put(Alarm.COLUMN_NAME_ALARM_IDUSER, model.IdUser);
		values.put(Alarm.COLUMN_NAME_ALARM_TIPOUSER, model.TipoUser);
		values.put(Alarm.COLUMN_NAME_ALARM_USER, model.User);
        values.put(Alarm.COLUMN_NAME_ALARM_NAME, model.Name);
        values.put(Alarm.COLUMN_NAME_ALARM_TIME_HOUR, model.TimeHour);
        values.put(Alarm.COLUMN_NAME_ALARM_TIME_MINUTE, model.TimeMinute);        
        values.put(Alarm.COLUMN_NAME_ALARM_TONE, model.AlarmTone);
        values.put(Alarm.COLUMN_NAME_ALARM_ENABLED, model.IsEnabled);
        
        String repeatingDays = "";
        for (int i = 0; i < 7; ++i) {
        	repeatingDays += model.getRepeatingDay(i) + ",";
        }
        values.put(Alarm.COLUMN_NAME_ALARM_REPEAT_DAYS, repeatingDays);
        
        return values;
	}
	
	public long createAlarm(RutinaModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().insert(Alarm.TABLE_NAME, null, values);
	}
	
	public long updateAlarm(RutinaModel model) {
		ContentValues values = populateContent(model);
        return getWritableDatabase().update(Alarm.TABLE_NAME, values, Alarm._ID + " = ?", new String[] { String.valueOf(model.Id) });
	}
	
	public RutinaModel getAlarm(long id) {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Alarm.TABLE_NAME + " WHERE " + Alarm._ID + " = " + id;
		
		Cursor c = db.rawQuery(select, null);
		
		if (c.moveToNext()) {
			return populateModel(c);
		}
		
		return null;
	}
	
	public List<RutinaModel> getAlarms() {
		SQLiteDatabase db = this.getReadableDatabase();
		
        String select = "SELECT * FROM " + Alarm.TABLE_NAME;
		
		Cursor c = db.rawQuery(select, null);
		
		List<RutinaModel> alarmList = new ArrayList<RutinaModel>();
		
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