package com.nullcognition.sqle2ndpass;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TestMainActivity extends Activity {

   public final String AUTHORITY = "com.personalcontactmanager.provider";
   public final String BASE_PATH = "contacts";
   private android.widget.TextView queryT, insertT;

   public class Columns {

	  public final static String TABLE_ROW_ID       = "_id";
	  public final static String TABLE_ROW_NAME     = "contact_name";
	  public final static String TABLE_ROW_PHONENUM = "contact_number";
	  public final static String TABLE_ROW_EMAIL    = "contact_email";
	  public final static String TABLE_ROW_PHOTOID  = "photo_id";
   }

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_test_main);

	  queryT = (android.widget.TextView)findViewById(com.nullcognition.sqle2ndpass.R.id.textQuery);
	  insertT = (android.widget.TextView)findViewById(com.nullcognition.sqle2ndpass.R.id.textInsert);

   }

   public void query(android.view.View inView){

	  android.net.Uri contentUri = android.net.Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	  android.database.Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);

	  if(cursor != null){
		 if(cursor.getCount() > 0){
			cursor.moveToFirst();
			String name = cursor.getString(cursor.getColumnIndexOrThrow(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_NAME));
			queryT.setText(name);
		 }
	  }

	  android.net.Uri rowUri = contentUri = android.content.ContentUris.withAppendedId(contentUri, getFirstRowId());

	  String[] projection = new String[]{Columns.TABLE_ROW_NAME, Columns.TABLE_ROW_PHONENUM, Columns.TABLE_ROW_EMAIL, Columns.TABLE_ROW_PHOTOID};

	  cursor = getContentResolver().query(contentUri, projection, null, null, null);

	  if(cursor != null){
		 if(cursor.getCount() > 0){
			cursor.moveToFirst();
			String name = cursor.getString(cursor.getColumnIndexOrThrow(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_NAME));
			queryT.setText(name);
		 }
	  }
   }

   public void insert(android.view.View inView){

	  String name = getRandomName();
	  String number = getRandomNumber();

	  android.content.ContentValues contentValues = new android.content.ContentValues();
	  contentValues.put(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_NAME, name);
	  contentValues.put(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_PHONENUM, number);
	  contentValues.put(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_EMAIL, name + "@gmail.com");
	  contentValues.put(com.nullcognition.sqle2ndpass.TestMainActivity.Columns.TABLE_ROW_PHOTOID, "abc");

	  String[] projection = new String[]{Columns.TABLE_ROW_NAME, Columns.TABLE_ROW_PHONENUM, Columns.TABLE_ROW_EMAIL, Columns.TABLE_ROW_PHOTOID};

	  android.net.Uri contentUri = android.net.Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
	  android.net.Uri insertedRowUri = getContentResolver().insert(contentUri, contentValues);

	  android.database.Cursor cr = getContentResolver().query(insertedRowUri, projection, null, null, null);

	  if(cr != null){
		 if(cr.getCount() > 0){
			cr.moveToFirst();
			name = cr.getString(cr.getColumnIndexOrThrow(Columns.TABLE_ROW_NAME));
			insertT.setText(name);
		 }
	  }
   }

   public void update(android.view.View v){

	  String name = getRandomName();
	  String number = getRandomNumber();

	  android.content.ContentValues values = new android.content.ContentValues();
	  values.put(Columns.TABLE_ROW_NAME, name);
	  values.put(Columns.TABLE_ROW_PHONENUM, number);
	  values.put(Columns.TABLE_ROW_EMAIL, name + "@gmail.com");
	  values.put(Columns.TABLE_ROW_PHOTOID, " ");

	  android.net.Uri contentUri = android.net.Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	  android.net.Uri rowUri = android.content.ContentUris.withAppendedId(contentUri, getFirstRowId());

	  int count = getContentResolver().update(rowUri, values, null, null);
   }

   public void delete(android.view.View v){

	  android.net.Uri contentUri = android.net.Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	  android.net.Uri rowUri = contentUri = android.content.ContentUris.withAppendedId(contentUri, getFirstRowId());

	  int count = getContentResolver().delete(rowUri, null, null);
   }


   private String getRandomName(){

	  java.util.Random rand = new java.util.Random();

	  //generating a random name
	  String name = "" + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26)) + (char)(122 - rand.nextInt(26));

	  return name;
   }

   public String getRandomNumber(){
	  java.util.Random rand = new java.util.Random();
	  String number = rand.nextInt(98989) * rand.nextInt(59595) + "";
	  return number;
   }

   private int getFirstRowId(){

	  int id = 1;
	  android.net.Uri contentUri = android.net.Uri.parse("content://" + AUTHORITY + "/" + "contacts");
	  android.database.Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
	  if(cursor != null){
		 if(cursor.getCount() > 0){
			cursor.moveToFirst();
			id = cursor.getInt(cursor.getColumnIndexOrThrow(Columns.TABLE_ROW_ID));
		 }
	  }
	  return id;
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu){
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.menu_test_main, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item){
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  //noinspection SimplifiableIfStatement
	  if(id == R.id.action_settings){
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }
}
