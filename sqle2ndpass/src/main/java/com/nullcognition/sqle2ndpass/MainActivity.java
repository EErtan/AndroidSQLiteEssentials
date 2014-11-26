package com.nullcognition.sqle2ndpass;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity implements android.view.View.OnClickListener {


   private android.widget.Button insertBtn, readBtn, deleteBtn, updateBtn;
   private SQLiteHelperClass sqlHelper;


   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(com.nullcognition.sqle2ndpass.R.layout.activity_main);

	  bindViews();
	  setListeners();

   }

   private void setListeners(){
	  insertBtn.setOnClickListener(this);
	  readBtn.setOnClickListener(this);
	  deleteBtn.setOnClickListener(this);
	  updateBtn.setOnClickListener(this);
   }

   private void bindViews(){
	  insertBtn = (android.widget.Button)findViewById(com.nullcognition.sqle2ndpass.R.id.insertBtn);
	  readBtn = (android.widget.Button)findViewById(com.nullcognition.sqle2ndpass.R.id.readBtn);
	  deleteBtn = (android.widget.Button)findViewById(com.nullcognition.sqle2ndpass.R.id.deleteBtn);
	  updateBtn = (android.widget.Button)findViewById(com.nullcognition.sqle2ndpass.R.id.updateBtn);
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu){
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(com.nullcognition.sqle2ndpass.R.menu.menu_main, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item){
	  // Handle action bar item clicks here. The action bar will
	  // automatically handle clicks on the Home/Up button, so long
	  // as you specify a parent activity in AndroidManifest.xml.
	  int id = item.getItemId();

	  //noinspection SimplifiableIfStatement
	  if(id == com.nullcognition.sqle2ndpass.R.id.action_settings){
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }

   @Override
   public void onClick(android.view.View v){
	  switch(v.getId()){
		 case com.nullcognition.sqle2ndpass.R.id.insertBtn:

			break;
		 case com.nullcognition.sqle2ndpass.R.id.readBtn:

			break;
		 case com.nullcognition.sqle2ndpass.R.id.deleteBtn:

			break;
		 case com.nullcognition.sqle2ndpass.R.id.updateBtn:

			break;
		 default:
			android.util.Log.e("Switch v", "Default parameter invalid");
			throw new java.security.InvalidParameterException();
	  }

   }
}
