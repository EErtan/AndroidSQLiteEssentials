package com.nullcognition.sqle2ndpass;
/**
 * Created by ersin on 26/11/14 at 12:54 AM
 */
public class SQLiteHelperClass {

   public static final int    VERSION_NUMBER = 1;
   public static final String TABLE_CONTACTS = "contacts";

   public static final String KEY_ID     = "_id";
   public static final String KEY_NAME   = "_name";
   public static final String KEY_NUMBER = "_primary_number";

   private android.database.sqlite.SQLiteDatabase   db;
   private android.database.sqlite.SQLiteOpenHelper sqlHelper;

   public SQLiteHelperClass(android.content.Context inContext){

	  sqlHelper = new android.database.sqlite.SQLiteOpenHelper(inContext, "ContactDatabase", null, VERSION_NUMBER) {

		 @Override
		 public synchronized void close(){
			super.close();
			android.util.Log.d("TAG", "Database closed");
		 }

		 @Override
		 public void onCreate(android.database.sqlite.SQLiteDatabase db){

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
			onCreate(db);
		 }

		 @Override
		 public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){

			String createContactsTable = "CREATE TABLE " +
										 TABLE_CONTACTS + "(" +
										 KEY_ID + " INTEGER PRIMANRY KEY," +
										 KEY_NAME + " TEXT," +
										 KEY_NUMBER + "INTEGER" + ")";

			try{
			   db.execSQL(createContactsTable);
			}
			catch(android.database.SQLException e){e.printStackTrace();}
		 }

		 @Override
		 public void onOpen(android.database.sqlite.SQLiteDatabase db){
			super.onOpen(db);
			android.util.Log.d("TAG", "Database opened");
		 }
	  };
   }

   public void insertToSimpleDataBase(){

	  db = sqlHelper.getWritableDatabase();

	  android.content.ContentValues cv = new android.content.ContentValues();

	  cv.put(KEY_NAME, "John");
	  cv.put(KEY_NUMBER, 00);

	  db.insert(TABLE_CONTACTS, null, cv);

	  cv.put(KEY_NAME, "Tom");
	  cv.put(KEY_NUMBER, 55);

	  db.insert(TABLE_CONTACTS, null, cv);
   }

   public void getDataFromDatabase(){
	  int count;
	  db = sqlHelper.getReadableDatabase();
	  android.database.Cursor cr = db.query(TABLE_CONTACTS, null, null, null, null, null, null);

	  int id;
	  if(cr != null){
		 count = cr.getCount();
		 cr.moveToFirst();
		 android.util.Log.d("DATABASE", "count is : " + count);
		 id = cr.getInt(cr.getColumnIndex(KEY_ID));
	  }

	  cr = db.rawQuery("select * from " + TABLE_CONTACTS, null);
	  if(cr != null){
		 count = cr.getCount();
		 android.util.Log.d("DATABASE", "count is : " + count);
	  }

   }

   public void delete(String name){

	  String whereClause = KEY_NAME + "=?";
	  String[] whereArgs = new String[]{name};

	  db = sqlHelper.getWritableDatabase();
	  db.delete(TABLE_CONTACTS, whereClause, whereArgs);
   }

   public void update(String name){

	  String whereClause = KEY_NAME + "=?";
	  String[] whereArgs = new String[]{name};

	  android.content.ContentValues cv = new android.content.ContentValues();

	  cv.put(KEY_NAME, "Betty");
	  cv.put(KEY_NUMBER, "999000");

	  db = sqlHelper.getWritableDatabase();
	  db.update(TABLE_CONTACTS, cv, whereClause, whereArgs);
   }

}
