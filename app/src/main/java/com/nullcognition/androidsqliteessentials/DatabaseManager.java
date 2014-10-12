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
  private static final String _contactPhotoId = "contactPhoto";
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

  public static String get_contactPhotoId(){
	return _contactPhotoId;
  }

  public static String get_rowId(){
	return _rowId;
  }

  public void updateRow(int inRowId, ModelContact inModelContact){

	android.content.ContentValues values = prepareData(inModelContact);

	String whereClause = _rowId + "=?";
	String whereArgs[] = new String[]{String.valueOf(inRowId)};

	sqLiteDatabase.update(tableName, values, whereClause, whereArgs);

  }

  private android.content.ContentValues prepareData(com.nullcognition.androidsqliteessentials.ModelContact inModelContact){
	android.content.ContentValues contentValues = new android.content.ContentValues();

	contentValues.put(_contactName, inModelContact.getContactName());
	contentValues.put(_contactPhoneNumber, inModelContact.getContactPhoneNumber());
	contentValues.put(_contactEmail, inModelContact.getContactEmail());
	contentValues.put(_contactPhotoId, inModelContact.getContactPhoto());

	return contentValues;
  }

  public void updateRowAlternative(int inRowId, ModelContact inModelContact){

	String updateStatement = "UPDATE " + tableName + " SET " + _contactName + "=?," + _contactPhoneNumber + "=?," + _contactEmail + "=?," + _contactPhotoId + "=?" + " WHERE " + _rowId + "=?";

	android.database.sqlite.SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(updateStatement);

	sqLiteStatement.bindString(1, inModelContact.getContactName());
	sqLiteStatement.bindString(2, inModelContact.getContactPhoneNumber());
	sqLiteStatement.bindString(3, inModelContact.getContactEmail());
	if(inModelContact.getContactPhoto() != null){ sqLiteStatement.bindBlob(4, inModelContact.getContactPhoto()); }
	sqLiteStatement.bindLong(5, inRowId);

	sqLiteStatement.executeUpdateDelete();
  }

  public void deleteRow(int inRowId){
	sqLiteDatabase.delete(tableName, _rowId + "=" + inRowId, null);
  }

  public void deleteRowAlternative(int inRowId){

	String deleteStatement = "DELETE FROM " + tableName + " WHERE " + _rowId + "=?";
	android.database.sqlite.SQLiteStatement s = sqLiteDatabase.compileStatement(deleteStatement);
	s.bindLong(1, inRowId);
	s.executeUpdateDelete();
  }

  public void addRow(com.nullcognition.androidsqliteessentials.ModelContact inModelContact){
	android.content.ContentValues contentValues = prepareData(inModelContact);

	sqLiteDatabase.insert(tableName, null, contentValues); // protect with a try-catch
  }

  public void addRowAlternative(com.nullcognition.androidsqliteessentials.ModelContact inModelContact){
	String insertStatment = "INSELT INTO " + tableName +
							" (" + _contactName + ", " +
							_contactPhoneNumber + ", " +
							_contactEmail + ", " +
							_contactPhotoId + ") " +
							" VALUES " + "(?,?,?,?)";

	android.database.sqlite.SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(insertStatment);
	sqLiteStatement.bindString(1, inModelContact.getContactName());
	sqLiteStatement.bindString(2, inModelContact.getContactPhoneNumber());
	sqLiteStatement.bindString(3, inModelContact.getContactEmail());
	if(inModelContact.getContactPhoto() != null){
	  sqLiteStatement.bindBlob(4, inModelContact.getContactPhoto());
	}

	sqLiteStatement.execute();
  }

  public com.nullcognition.androidsqliteessentials.ModelContact getRowAsObject(int inRowId){
	ModelContact modelContact = new ModelContact();
	android.database.Cursor cursor = null;

	cursor = sqLiteDatabase
	  .query(tableName, new String[]{_rowId, _contactName, _contactPhoneNumber, _contactEmail, _contactPhotoId}, _rowId + "=" + inRowId, null, null,
			 null, null, null);

	cursor.moveToFirst();
	prepareSendObject(modelContact, cursor);

	return modelContact;
  }

  private void prepareSendObject(ModelContact inRowObj, android.database.Cursor inCursor){
	// if we know the column we can use the exact number instead of the inCursor
	inRowObj.setContactId(inCursor.getInt(inCursor.getColumnIndexOrThrow(_rowId)));
	inRowObj.setContactName(inCursor.getString(inCursor.getColumnIndexOrThrow(_contactName)));
	inRowObj.setContactPhoneNumber(inCursor.getString(inCursor.getColumnIndexOrThrow(_contactPhoneNumber)));
	inRowObj.setContactEmail(inCursor.getString(inCursor.getColumnIndexOrThrow(_contactEmail)));
	inRowObj.setContactPhoto(inCursor.getBlob(inCursor.getColumnIndexOrThrow(_contactPhotoId)));
  }

  public ModelContact getRowAsObjectAlternative(int inRowId){

	ModelContact modelContact = new ModelContact();
	android.database.Cursor cursor = null;

	String queryStatement = "SELECT * FROM " + tableName + " WHERE " + _rowId + "=?";

	cursor = sqLiteDatabase.rawQuery(queryStatement, new String[]{String.valueOf(inRowId)});
	cursor.moveToFirst();

	// if (!cursor.isAfterLast()) { // for multiple
	modelContact = new ModelContact();
	modelContact.setContactId(cursor.getInt(0));
	prepareSendObject(modelContact, cursor);
	// }

	return modelContact;
  }

  public java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact> getAllData(){

	java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact> allRowsObj = new java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact>();
	android.database.Cursor cursor;
	ModelContact rowContactObj;

	String[] columns = new String[]{_rowId, tableName, _contactPhoneNumber, _contactEmail, _contactPhotoId};

	try{

	  cursor = sqLiteDatabase.query(tableName, columns, null, null, null, null, null);
	  cursor.moveToFirst();

	  if(! cursor.isAfterLast()){
		do{
		  rowContactObj = new ModelContact();
		  rowContactObj.setContactId(cursor.getInt(0));
		  prepareSendObject(rowContactObj, cursor);
		  allRowsObj.add(rowContactObj);

		}
		while(cursor.moveToNext()); // try to move the cursor's
		// pointer forward one position.
	  }
	}
	catch(android.database.SQLException e){
	  android.util.Log.e("DB ERROR", e.toString());
	  e.printStackTrace();
	}

	return allRowsObj;

  }

  public java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact> getAllDataAlternative(){

	java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact> allRowsObj = new java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact>();
	android.database.Cursor cursor;
	ModelContact rowContactObj;

	try{

	  // query to fetch all the columns and rows of the table
	  String queryStatement = "SELECT" + " * " + "FROM " + tableName;

	  cursor = sqLiteDatabase.rawQuery(queryStatement, null);

	  cursor.moveToFirst();
	  if(! cursor.isAfterLast()){
		do{
		  rowContactObj = new ModelContact();
		  rowContactObj.setContactId(cursor.getInt(0));
		  prepareSendObject(rowContactObj, cursor);
		  allRowsObj.add(rowContactObj);

		}
		while(cursor.moveToNext());
		// try to move the cursor's
		// pointer forward one position.
	  }
	}
	catch(android.database.SQLException e){
	  android.util.Log.e("DB ERROR", e.toString());
	  e.printStackTrace();
	}
	return allRowsObj;
  }
}
