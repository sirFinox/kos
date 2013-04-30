package com.kos.cvut;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KosDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "kos.db";
	public static final String STUDENT = "student";
	public static final String TEACHER = "teacher";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String ID = "id";
	private static final String KOS_TABLE_CREATE = "CREATE TABLE KOS ("
			+ "idKOS INTEGER PRIMARY KEY AUTOINCREMENT," + "student BOOL,"
			+ "teacher BOOL," + "username VARCHAR(45));";

	private static final String COURSES_TABLE_CREATE = "CREATE TABLE COURSES ("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "credits VARCHAR(3)    ,"
			+ "code VARCHAR(45)  NOT NULL  ," + "name VARCHAR(45)  NOT NULL  ,"
			+ "completion VARCHAR    ," + "lang VARCHAR(5)    ,"
			+ "classesLang VARCHAR    ," + "classesType VARCHAR    ,"
			+ "description TEXT    ," + "homepage VARCHAR    ,"
			+ "keywords VARCHAR    ," + "lecturesContents TEXT    ,"
			+ "literature TEXT    ," + "objectives TEXT    ,"
			+ "range VARCHAR    ," + "requirements TEXT    ,"
			+ "season VARCHAR    ," + "tutorialsContents TEXT      ,"
			+ "department VARCHAR      ," + "note TEXT      );";
	
	public KosDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(KOS_TABLE_CREATE);
		db.execSQL(COURSES_TABLE_CREATE);
		// db.execSQL(COURSES_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS kos");
		onCreate(db);
	}

}
