package com.nullcognition.sqle2ndpass;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class ContactsMainActivity extends Activity implements android.view.View.OnClickListener,
															  android.app.LoaderManager.LoaderCallbacks<android.database.Cursor> {


   public final static  int    CONTACT_ADD_REQ_CODE    = 100;
   public final static  int    CONTACT_UPDATE_REQ_CODE = 101;
   public final static  String REQ_TYPE                = "req_type";
   public final static  String ITEM_POSITION           = "item_position";
   final static         String TAG                     = "MainActivity";
   private static final int    CUR_LOADER              = 0;
   private android.widget.ListView listContact;
   private android.widget.Button   addNewButton;

   com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager dm;
   private CustomCursorAdapter adapter;
   //private CustomListAdapter cAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);

	  setContentView(R.layout.activity_contacts_main);
	  bindViews();
	  setListeners();

//	  cAdapter = new CustomListAdapter(this);

	  dm = new com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager(this);

	  adapter = new CustomCursorAdapter(this, null);

	  displayListView();
	  getLoaderManager().initLoader(CUR_LOADER, null, this);
   }


   private void displayListView(){
	  listContact.setAdapter(adapter);
	  registerForContextMenu(listContact);

   }

   private void bindViews(){
	  addNewButton = (android.widget.Button)findViewById(R.id.addNew);
	  listContact = (android.widget.ListView)findViewById(R.id.list);
   }

   private void setListeners(){
	  addNewButton.setOnClickListener(this);
   }

   @Override
   public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo){
	  super.onCreateContextMenu(menu, v, menuInfo);
	  android.view.MenuInflater m = getMenuInflater();
	  m.inflate(R.menu.del_menu, menu);
   }

   @Override
   public boolean onContextItemSelected(MenuItem item){

	  android.widget.AdapterView.AdapterContextMenuInfo info = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  ContactModel contactObj = (ContactModel)adapter.getItem(info.position);

	  switch(item.getItemId()){
		 case R.id.delete_item:

			//adapter.delRow(info.position);
			//adapter.notifyDataSetChanged();
			return true;
		 case R.id.update_item:

//			android.content.Intent intent = new android.content.Intent(ContactsMainActivity.this, AddNewContactActivity.class);
//			intent.putExtra(ITEM_POSITION, contactObj.getId());
//			intent.putExtra(REQ_TYPE, CONTACT_UPDATE_REQ_CODE);
//			startActivityForResult(intent, CONTACT_ADD_REQ_CODE);

			break;
	  }
	  return super.onContextItemSelected(item);
   }

   //
   @Override
   protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
	  super.onActivityResult(requestCode, resultCode, data);

	  if(requestCode == CONTACT_ADD_REQ_CODE){
		 if(resultCode == RESULT_OK){
			// Notify the adapter that underlying data set has changed
			adapter.notifyDataSetChanged();
		 }
		 else if(resultCode == RESULT_CANCELED){

		 }
	  }
	  else if(requestCode == CONTACT_UPDATE_REQ_CODE){
		 if(resultCode == RESULT_OK){
			// Notify the adapter that underlying data set has changed
			adapter.notifyDataSetChanged();
		 }
		 else if(resultCode == RESULT_CANCELED){

		 }
	  }
   }

   @Override
   public void onClick(android.view.View v){

	  switch(v.getId()){
		 case R.id.addNew:
			android.content.Intent intent = new android.content.Intent(ContactsMainActivity.this, AddNewContactActivity.class);
			intent.putExtra(REQ_TYPE, CONTACT_ADD_REQ_CODE);
			startActivityForResult(intent, CONTACT_ADD_REQ_CODE);
			break;
	  }
   }

   public void provider(android.view.View v){ // usage example for other apps, or yours using an external content provider

	  android.net.Uri ur;

	  //insert
		/*ContentValues values = new ContentValues();
		values.put(PersonalContactContract.Columns.TABLE_ROW_NAME, "aakash");
		values.put(PersonalContactContract.Columns.TABLE_ROW_PHONENUM, "9910783325");
		values.put(PersonalContactContract.Columns.TABLE_ROW_EMAIL, "ak.com");
		values.put(PersonalContactContract.Columns.TABLE_ROW_PHOTOID,"abc");

		ur = PersonalContactContract.CONTENT_URI;
		ur = getContentResolver().insert(ur, values);*/

	  ur = android.content.ContentUris.withAppendedId(com.nullcognition.sqle2ndpass.provider.PersonalContactContract.CONTENT_URI, 2);

	  String[] projection = com.nullcognition.sqle2ndpass.provider.PersonalContactContract.PROJECTION_ALL;
	  String selection = com.nullcognition.sqle2ndpass.provider.PersonalContactContract.Columns.TABLE_ROW_NAME + "=?";
	  String[] selectionArgs = new String[]{"vikash"};

	  //query
		/*Cursor cr = getContentResolver().query(ur, projection,
				null, null, null);

		if(cr != null) {
			if(cr.getCount() > 0) {
				cr.moveToFirst();
				String name = cr.getString(cr.getColumnIndexOrThrow("contact_name"));
			}
		}*/

	  //delete
		/*ur = PersonalContactContract.CONTENT_URI;
		int count = getContentResolver().delete(ur, selection, selectionArgs);
		cAdapter.notifyDataSetChanged();*/

	  //update
		/*ContentValues values = new ContentValues();
		values.put(PersonalContactContract.Columns.TABLE_ROW_NAME, "aakash");
		values.put(PersonalContactContract.Columns.TABLE_ROW_PHONENUM, "9910783325");
		values.put(PersonalContactContract.Columns.TABLE_ROW_EMAIL, "ak.com");
		values.put(PersonalContactContract.Columns.TABLE_ROW_PHOTOID,"abc");

		int count = getContentResolver().update(ur, values, null, null);
		cAdapter.notifyDataSetChanged();*/

   }

   @Override
   public android.content.Loader<android.database.Cursor> onCreateLoader(int id, android.os.Bundle args){
	  switch(id){
		 case CUR_LOADER:
			// Returns a new CursorLoader
			return new android.content.CursorLoader(this, // Parent activity context
													com.nullcognition.sqle2ndpass.provider.PersonalContactContract.CONTENT_URI, // Table to query
													com.nullcognition.sqle2ndpass.provider.PersonalContactContract.PROJECTION_ALL, // Projection to
													// return
													null, // No selection clause
													null, // No selection arguments
													null // Default sort order
			);
		 default:
			// An invalid id was passed in
			return null;
	  }
   }


   @Override
   public void onLoadFinished(android.content.Loader<android.database.Cursor> loader, android.database.Cursor data){
	  this.adapter.changeCursor(data);
   }

   @Override
   public void onLoaderReset(android.content.Loader<android.database.Cursor> loader){
	  this.adapter.changeCursor(null);

   }
}
