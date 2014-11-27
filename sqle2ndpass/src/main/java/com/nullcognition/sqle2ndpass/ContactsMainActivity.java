package com.nullcognition.sqle2ndpass;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

public class ContactsMainActivity extends Activity implements android.view.View.OnClickListener {

   final static        String TAG                     = "MainActivity";
   public final static int    CONTACT_ADD_REQ_CODE    = 100;
   public final static int    CONTACT_UPDATE_REQ_CODE = 101;
   public final static String REQ_TYPE                = "req_type";
   public final static String ITEM_POSITION           = "item_position";

   private android.widget.ListView listReminder;
   private android.widget.Button   addNewButton;

   private CustomListAdapter cAdapter;

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);

	  setContentView(R.layout.activity_contacts_main);
	  bindViews();
	  setListeners();

	  cAdapter = new CustomListAdapter(this);
	  listReminder.setAdapter(cAdapter);

	  registerForContextMenu(listReminder);

   }

   private void bindViews(){
	  addNewButton = (android.widget.Button)findViewById(R.id.addNew);
	  listReminder = (android.widget.ListView)findViewById(R.id.list);
   }

   private void setListeners(){
	  addNewButton.setOnClickListener(this);
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

   @Override
   public void onCreateContextMenu(android.view.ContextMenu menu, android.view.View v, android.view.ContextMenu.ContextMenuInfo menuInfo){
	  super.onCreateContextMenu(menu, v, menuInfo);
	  android.view.MenuInflater m = getMenuInflater();
	  m.inflate(R.menu.del_menu, menu);
   }

   @Override
   public boolean onContextItemSelected(MenuItem item){

	  android.widget.AdapterView.AdapterContextMenuInfo info = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	  ContactModel contactObj = (ContactModel)cAdapter.getItem(info.position);

	  switch(item.getItemId()){
		 case R.id.delete_item:

			cAdapter.delRow(info.position);
			cAdapter.notifyDataSetChanged();
			return true;
		 case R.id.update_item:

			android.content.Intent intent = new android.content.Intent(ContactsMainActivity.this, AddNewContactActivity.class);
			intent.putExtra(ITEM_POSITION, contactObj.getId());
			intent.putExtra(REQ_TYPE, CONTACT_UPDATE_REQ_CODE);
			startActivityForResult(intent, CONTACT_ADD_REQ_CODE);

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
			cAdapter.notifyDataSetChanged();
		 }
		 else if(resultCode == RESULT_CANCELED){

		 }
	  }
	  else if(requestCode == CONTACT_UPDATE_REQ_CODE){
		 if(resultCode == RESULT_OK){
			// Notify the adapter that underlying data set has changed
			cAdapter.notifyDataSetChanged();
		 }
		 else if(resultCode == RESULT_CANCELED){

		 }
	  }
   }
}
