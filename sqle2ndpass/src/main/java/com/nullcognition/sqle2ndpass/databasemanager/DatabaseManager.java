package com.nullcognition.sqle2ndpass.databasemanager;

public class DatabaseManager {

   private static final String DB_NAME    = "contact";
   public static final  int    DB_VERSION = 1;

   public static final  String TABLE_NAME         = "contact_table";
   private static final String TABLE_ROW_ID       = "_id";
   private static final String TABLE_ROW_NAME     = "contact_name";
   private static final String TABLE_ROW_PHONENUM = "contact_number";
   private static final String TABLE_ROW_EMAIL    = "contact_email";
   private static final String TABLE_ROW_PHOTOID  = "photo_id";

   private android.content.Context                context;
   private android.database.sqlite.SQLiteDatabase db;

   public DatabaseManager(android.content.Context inContext){

	  this.context = inContext;
	  CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
	  this.db = helper.getWritableDatabase();
   }

   private class CustomSQLiteOpenHelper extends android.database.sqlite.SQLiteOpenHelper {

	  public CustomSQLiteOpenHelper(android.content.Context inContext){
		 super(context, DB_NAME, null, DB_VERSION);
	  }

	  @Override
	  public void onCreate(android.database.sqlite.SQLiteDatabase db){

		 String newTableQueryString = "create table " + TABLE_NAME + " (" +
									  " integer primary key autoincrement not null," +
									  TABLE_ROW_NAME + " text nut null," +
									  TABLE_ROW_PHONENUM + " text not null," +
									  TABLE_ROW_EMAIL + " text not null," +
									  TABLE_ROW_PHOTOID + " BLOB" + ");";

		 db.execSQL(newTableQueryString);
	  }

	  @Override
	  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){

		 String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
		 db.execSQL(DROP_TABLE);
		 onCreate(db);
	  }

   }

   private android.content.ContentValues prepareData(com.nullcognition.sqle2ndpass.ContactModel contactObj){

	  android.content.ContentValues values = new android.content.ContentValues();
	  values.put(TABLE_ROW_NAME, contactObj.getName());
	  values.put(TABLE_ROW_PHONENUM, contactObj.getContactNo());
	  values.put(TABLE_ROW_EMAIL, contactObj.getEmail());
	  values.put(TABLE_ROW_PHOTOID, contactObj.getPhoto());

	  return values;
   }

   private void prepareSendObject(com.nullcognition.sqle2ndpass.ContactModel rowObj, android.database.Cursor cursor){

	  rowObj.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TABLE_ROW_ID)));
	  rowObj.setName(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_NAME)));
	  rowObj.setContactNo(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_PHONENUM)));
	  rowObj.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(TABLE_ROW_EMAIL)));
	  rowObj.setPhoto(cursor.getBlob(cursor.getColumnIndexOrThrow(TABLE_ROW_PHOTOID)));
   }

   public void updateRow(int rowID, com.nullcognition.sqle2ndpass.ContactModel contactObj){

	  android.content.ContentValues values = prepareData(contactObj);

	  String whereClause = TABLE_ROW_ID + "=?";
	  String whereArgs[] = new String[]{String.valueOf(rowID)};

	  db.update(TABLE_NAME, values, whereClause, whereArgs);
   }

   public void updateRowAlternative(int rowId, com.nullcognition.sqle2ndpass.ContactModel contactObj){

	  String updateStatement = "UPDATE " + TABLE_NAME + " SET " + TABLE_ROW_NAME + "=?," + TABLE_ROW_PHONENUM + "=?," + TABLE_ROW_EMAIL + "=?," + TABLE_ROW_PHOTOID + "=?" + " WHERE " + TABLE_ROW_ID + "=?";

	  android.database.sqlite.SQLiteStatement s = db.compileStatement(updateStatement);
	  s.bindString(1, contactObj.getName());
	  s.bindString(2, contactObj.getContactNo());
	  s.bindString(3, contactObj.getEmail());
	  if(contactObj.getPhoto() != null){ s.bindBlob(4, contactObj.getPhoto()); }
	  s.bindLong(5, rowId);

	  s.executeUpdateDelete();
   }

   public void deleteRow(int rowID){
	  try{
		 db.delete(TABLE_NAME, TABLE_ROW_ID + "=" + rowID, null);
	  }
	  catch(Exception e){ e.printStackTrace(); }
   }

   public void deleteRowAlternative(int rowId){

	  String deleteStatement = "DELETE FROM " + TABLE_NAME + " WHERE " + TABLE_ROW_ID + "=?";
	  android.database.sqlite.SQLiteStatement s = db.compileStatement(deleteStatement);
	  s.bindLong(1, rowId);
	  s.executeUpdateDelete();
   }

   public void addRow(com.nullcognition.sqle2ndpass.ContactModel inContactModel){

	  android.content.ContentValues values = prepareData(inContactModel);

	  try{
		 db.insert(TABLE_NAME, null, values);
	  }
	  catch(Exception e){ e.printStackTrace(); }
   }

   public void addRowAlternative(com.nullcognition.sqle2ndpass.ContactModel contactObj){

	  String insertStatment = "INSERT INTO " +
							  TABLE_NAME + " (" +
							  TABLE_ROW_NAME + "," +
							  TABLE_ROW_PHONENUM + "," +
							  TABLE_ROW_EMAIL + "," +
							  TABLE_ROW_PHOTOID + ") " +
							  " VALUES " + "(?,?,?,?)";

	  android.database.sqlite.SQLiteStatement s = db.compileStatement(insertStatment);

	  s.bindString(1, contactObj.getName());
	  s.bindString(2, contactObj.getContactNo());
	  s.bindString(3, contactObj.getEmail());

	  if(contactObj.getPhoto() != null){ s.bindBlob(4, contactObj.getPhoto()); }

	  s.execute();
   }

   public com.nullcognition.sqle2ndpass.ContactModel getRowAsObject(int rowID){

	  com.nullcognition.sqle2ndpass.ContactModel rowContactObj = new com.nullcognition.sqle2ndpass.ContactModel();
	  android.database.Cursor cursor;

	  try{
		 cursor = db.query(TABLE_NAME, new String[]{TABLE_ROW_ID, TABLE_ROW_NAME, TABLE_ROW_PHONENUM, TABLE_ROW_EMAIL, TABLE_ROW_PHOTOID}, TABLE_ROW_ID + "=" + rowID, null, null, null, null, null);

		 cursor.moveToFirst();
		 prepareSendObject(rowContactObj, cursor);

	  }
	  catch(android.database.SQLException e){
		 android.util.Log.e("DB ERROR", e.toString());
		 e.printStackTrace();
	  }

	  return rowContactObj;
   }

   public com.nullcognition.sqle2ndpass.ContactModel getRowAsObjectAlternative(int rowID){

	  com.nullcognition.sqle2ndpass.ContactModel rowContactObj = new com.nullcognition.sqle2ndpass.ContactModel();
	  android.database.Cursor cursor;

	  try{

		 String queryStatement = "SELECT * FROM " + TABLE_NAME + " WHERE " + TABLE_ROW_ID + "=?";

		 cursor = db.rawQuery(queryStatement, new String[]{String.valueOf(rowID)});
		 cursor.moveToFirst();

		 // if (!cursor.isAfterLast()) { // to travers through the whole
		 rowContactObj = new com.nullcognition.sqle2ndpass.ContactModel();
		 rowContactObj.setId(cursor.getInt(0));
		 prepareSendObject(rowContactObj, cursor);
		 // }

	  }
	  catch(android.database.SQLException e){
		 android.util.Log.e("DB ERROR", e.toString());
		 e.printStackTrace();
	  }

	  return rowContactObj;
   }

   public java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel> getAllData(){
	  java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel> allRowsObj = new java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel>();
	  android.database.Cursor cursor;
	  com.nullcognition.sqle2ndpass.ContactModel rowContactObj;

	  String[] columns = new String[]{TABLE_ROW_ID, TABLE_ROW_NAME, TABLE_ROW_PHONENUM, TABLE_ROW_EMAIL, TABLE_ROW_PHOTOID};

	  try{

		 cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
		 cursor.moveToFirst();

		 if(! cursor.isAfterLast()){
			do{
			   rowContactObj = new com.nullcognition.sqle2ndpass.ContactModel();
			   rowContactObj.setId(cursor.getInt(0));
			   prepareSendObject(rowContactObj, cursor);
			   allRowsObj.add(rowContactObj);

			}
			while(cursor.moveToNext());
		 }
	  }
	  catch(android.database.SQLException e){
		 android.util.Log.e("DB ERROR", e.toString());
		 e.printStackTrace();
	  }

	  return allRowsObj;
   }

   public java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel> getAllDataAlternative(){

	  java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel> allRowsObj = new java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel>();
	  android.database.Cursor cursor;
	  com.nullcognition.sqle2ndpass.ContactModel rowContactObj;

	  try{

		 String queryStatement = "SELECT" + " * " + "FROM " + TABLE_NAME;

		 cursor = db.rawQuery(queryStatement, null);

		 cursor.moveToFirst();
		 if(! cursor.isAfterLast()){
			do{
			   rowContactObj = new com.nullcognition.sqle2ndpass.ContactModel();
			   rowContactObj.setId(cursor.getInt(0));
			   prepareSendObject(rowContactObj, cursor);
			   allRowsObj.add(rowContactObj);

			}
			while(cursor.moveToNext());
		 }
	  }
	  catch(android.database.SQLException e){
		 android.util.Log.e("DB ERROR", e.toString());
		 e.printStackTrace();
	  }
	  return allRowsObj;
   }

}
