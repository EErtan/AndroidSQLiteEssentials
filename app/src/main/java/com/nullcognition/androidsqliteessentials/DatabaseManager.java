package com.nullcognition.androidsqliteessentials;
/**
 * Created by ersin on 08/10/14 at 8:18 PM
 */
public class DatabaseManager {

  private static final String                                 databaseName        = "DatabaseOfContacts";
  private static final int                                    databaseVersion     = 1;
  private static final String                                 tableName           = "TableOfContacts";
  // Contact Model fields
  private static final String                                 _rowId              = "_id";
  private static final String                                 _contactId          = "contactId";
  private static final String                                 _contactName        = "contactName";
  private static final String                                 _contactPhoneNumber = "contactPhoneNumber";
  private static final String                                 _contactEmail       = "contactEmail";
  private static final String                                 _contactPhoto       = "contactPhoto";
  private              android.content.Context                context             = null;
  private              android.database.sqlite.SQLiteDatabase sqLiteDatabase      = null;

  public DatabaseManager(android.content.Context inContext){
	context = inContext;

	final SQLiteOpenHelperExtended sqLiteOpenHelperExtended = new SQLiteOpenHelperExtended(context, databaseName, null, databaseVersion);
	sqLiteDatabase = sqLiteOpenHelperExtended.getWritableDatabase();
  }

  public static String getTableName(){
	return tableName;
  }

  public static String get_contactId(){
	return _contactId;
  }

  public static String get_contactName(){
	return _contactName;
  }

  public static String get_contactPhoneNumber(){
	return _contactPhoneNumber;
  }

  public static String get_contactEmail(){
	return _contactEmail;
  }

  public static String get_contactPhoto(){
	return _contactPhoto;
  }

  public static String get_rowId(){
	return _rowId;
  }
}
