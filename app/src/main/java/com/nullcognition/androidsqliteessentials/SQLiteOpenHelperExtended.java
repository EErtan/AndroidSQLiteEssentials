package com.nullcognition.androidsqliteessentials;
/**
 * Created by ersin on 08/10/14 at 8:55 PM
 */
public class SQLiteOpenHelperExtended extends android.database.sqlite.SQLiteOpenHelper {

  public SQLiteOpenHelperExtended(android.content.Context context, String name, android.database.sqlite.SQLiteDatabase.CursorFactory factory,
								  int version){
	super(context, name, factory, version);
  }

  @Override
  public void onCreate(android.database.sqlite.SQLiteDatabase db){
	// only place for custom logic, as we get the values from the DatabaseManager class and build the query statementsq
	String newTableQueryString = "create table " + DatabaseManager.getTableName() +
								 " (" + DatabaseManager.get_rowId() + " integer primary key autoincrement not null," +
								 DatabaseManager.get_contactName() + " toxt not null," +
								 DatabaseManager.get_contactPhoneNumber() + " text not null," +
								 DatabaseManager.get_contactEmail() + " text not null" +
								 DatabaseManager.get_contactPhoto() + " BLOB" + ");";
	db.execSQL(newTableQueryString);
  }

  @Override
  public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion){
	String dropTable = "DROP TABLE IF EXISTS" + DatabaseManager.getTableName();
	db.execSQL(dropTable);
	onCreate(db);
  }
}
