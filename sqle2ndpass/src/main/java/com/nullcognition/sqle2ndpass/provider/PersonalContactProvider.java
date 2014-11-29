package com.nullcognition.sqle2ndpass.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class PersonalContactProvider extends ContentProvider {

   // int constants use with urimatcher
   private static final int CONTACTS_TABLE      = 1;
   private static final int CONTACTS_TABLE_ITEM = 2;

   private static final android.content.UriMatcher mmURIMatcher = new android.content.UriMatcher(android.content.UriMatcher.NO_MATCH);

   private com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager dbm;

   static{
	  mmURIMatcher.addURI(com.nullcognition.sqle2ndpass.provider.PersonalContactContract.AUTHORITY, com.nullcognition.sqle2ndpass.provider.PersonalContactContract.BASE_PATH, CONTACTS_TABLE);

	  mmURIMatcher.addURI(com.nullcognition.sqle2ndpass.provider.PersonalContactContract.AUTHORITY, com.nullcognition.sqle2ndpass.provider.PersonalContactContract.BASE_PATH + "/#", CONTACTS_TABLE_ITEM);
   }

   public PersonalContactProvider(){}

   @Override
   public boolean onCreate(){
	  dbm = new com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager(getContext());
	  return false;
   }

   @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){

	  int uriType = mmURIMatcher.match(uri);

	  switch(uriType){
		 case CONTACTS_TABLE:
			break;
		 case CONTACTS_TABLE_ITEM:
			if(android.text.TextUtils.isEmpty(selection)){
			   selection = com.nullcognition.sqle2ndpass.provider.PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment();
			}
			else{
			   selection = com.nullcognition.sqle2ndpass.provider.PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment() + " and " + selection;
			}
			break;
		 default:
			android.util.Log.e("Switch uriType", "Default parameter invalid");
			throw new IllegalArgumentException();
	  }

	  Cursor cursor = dbm.getAllCursor(projection, selection, selectionArgs, sortOrder);
	  return cursor;
   }

   @Override
   public String getType(Uri uri){

	  int uriType = mmURIMatcher.match(uri);
	  switch(uriType){
		 case CONTACTS_TABLE:
			return com.nullcognition.sqle2ndpass.provider.PersonalContactContract.CONTENT_TYPE;
		 case CONTACTS_TABLE_ITEM:
			return com.nullcognition.sqle2ndpass.provider.PersonalContactContract.CONTENT_ITEM_TYPE;
		 default:
			android.util.Log.e("Switch uriType", "Default parameter invalid");
			throw new java.security.InvalidParameterException();
	  }

   }

   @Override
   public Uri insert(Uri uri, ContentValues values){

	  int uriType = mmURIMatcher.match(uri);
	  long id;

	  switch(uriType){
		 case CONTACTS_TABLE:
			id = dbm.addRow(values);
			break;
		 default:
			android.util.Log.e("Switch uriType", "Default parameter invalid");
			throw new java.security.InvalidParameterException();
	  }

	  Uri ur = android.content.ContentUris.withAppendedId(uri, id);
	  return ur;
   }

   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs){

	  int uriType = mmURIMatcher.match(uri);

	  switch(uriType){
		 case CONTACTS_TABLE:
			break;
		 case CONTACTS_TABLE_ITEM:
			if(android.text.TextUtils.isEmpty(selection)){
			   selection = PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment();
			}
			else{
			   selection = PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment() + " and " + selection;
			}
			break;
		 default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
	  }

	  int count = dbm.deleteRow(selection, selectionArgs);
	  return count;
   }

   @Override
   public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){

	  int uriType = mmURIMatcher.match(uri);

	  switch(uriType){
		 case CONTACTS_TABLE:
			break;
		 case CONTACTS_TABLE_ITEM:
			if(android.text.TextUtils.isEmpty(selection)){
			   selection = PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment();
			}
			else{
			   selection = PersonalContactContract.Columns.TABLE_ROW_ID + "=" + uri.getLastPathSegment() + " and " + selection;
			}
			break;
		 default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
	  }

	  int count = dbm.updateRow(values, selection, selectionArgs);
	  return count;
   }
}
