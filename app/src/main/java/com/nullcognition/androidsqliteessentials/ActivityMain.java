package com.nullcognition.androidsqliteessentials;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityMain extends Activity {

  public final static int    contactAddReqCode    = 100;
  public final static int    contactUpdateReqCode = 101;
  public final static String reqType              = "reqType";
  public final static String itemPosition         = "itemPosition";
  final static        String tag                  = "ActivityMain";
  private android.widget.ListView listReminder;
  private android.widget.Button   buttonAddNewContact;

  private CustomListAdapter customListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_main, menu);
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

  @Override
  public boolean onContextItemSelected(MenuItem item){

	android.widget.AdapterView.AdapterContextMenuInfo info = (android.widget.AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	ModelContact contactObj = (ModelContact)customListAdapter.getItem(info.position);

	switch(item.getItemId()){
	  case R.id.delete_item:

		customListAdapter.delRow(info.position);
		customListAdapter.notifyDataSetChanged();
		return true;
	  case R.id.update_item:

		android.content.Intent intent = new android.content.Intent(ActivityMain.this, ActivityAddNewContact.class);
		intent.putExtra(itemPosition, contactObj.getContactId());
		intent.putExtra(reqType, contactUpdateReqCode);
		startActivityForResult(intent, contactAddReqCode);

		break;
	}
	return super.onContextItemSelected(item);
  }

  //
  @Override
  protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
	super.onActivityResult(requestCode, resultCode, data);

	if(requestCode == contactAddReqCode){
	  if(resultCode == RESULT_OK){
		// Notify the adapter that underlying data set has changed
		customListAdapter.notifyDataSetChanged();
	  }
	  else if(resultCode == RESULT_CANCELED){

	  }
	}
	else if(requestCode == contactUpdateReqCode){
	  if(resultCode == RESULT_OK){
		// Notify the adapter that underlying data set has changed
		customListAdapter.notifyDataSetChanged();
	  }
	  else if(resultCode == RESULT_CANCELED){

	  }
	}
  }
}
