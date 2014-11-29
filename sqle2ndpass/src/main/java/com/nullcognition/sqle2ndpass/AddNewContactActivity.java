package com.nullcognition.sqle2ndpass;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddNewContactActivity extends Activity implements android.view.View.OnClickListener {

   // obseletes the casting code
   // public <T> T findById(int id) { return (T) findViewById(id); }

   public static final int PICK_PHOTO_FROM_GALLERY   = 1001;
   public static final int CAPTURE_PHOTO_FROM_CAMERA = 1002;
   public static final  int    PICK_CONTACT = 1003;
   private final static String TAG          = "adnewcontactactvity";
   android.widget.ImageButton getContact;
   private android.widget.TextView contactName, contactPhone, contactEmail, contactPhoto;
   private android.widget.Button doneButton, pickPhotoBtn;
   private android.widget.ImageView capturedImg;
   private android.graphics.Bitmap imageBitmap;
   private byte[]                  blob;
   private boolean photoPicked = false;
   private int reqType;
   private int rowId;

   @Override
   protected void onCreate(Bundle savedInstanceState){
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_add_new);

	  reqType = getIntent().getIntExtra(ContactsMainActivity.REQ_TYPE, ContactsMainActivity.CONTACT_ADD_REQ_CODE);

	  setContentView(com.nullcognition.sqle2ndpass.R.layout.activity_add_new);
	  bindViews();
	  setListeners();

	  if(reqType == ContactsMainActivity.CONTACT_UPDATE_REQ_CODE){

		 rowId = getIntent().getIntExtra(ContactsMainActivity.ITEM_POSITION, 1);
		 initialize(rowId);
	  }
   }

   private void initialize(int position){

	  com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager dm = new com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager(this);
	  ContactModel contactObj = dm.getRowAsObjectAlternative(position);

	  contactName.setText(contactObj.getName());
	  contactPhone.setText(contactObj.getContactNo());
	  contactEmail.setText(contactObj.getEmail());

	  setImage(contactObj.getPhoto(), capturedImg);
   }

   private void setImage(byte[] inBlob, android.widget.ImageView inImageView){
	  if(inBlob != null){
		 android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(inBlob, 0, inBlob.length);
		 inImageView.setImageBitmap(bmp);
	  }
   }

   private void bindViews(){

	  contactName = (android.widget.TextView)findViewById(R.id.contactName);
	  contactPhone = (android.widget.TextView)findViewById(R.id.contactPhone);
	  contactEmail = (android.widget.TextView)findViewById(R.id.contactEmail);
	  contactPhoto = (android.widget.TextView)findViewById(R.id.contactPhoto);
	  doneButton = (android.widget.Button)findViewById(R.id.doneBtn);
	  pickPhotoBtn = (android.widget.Button)findViewById(R.id.pickPhotoBtn);
	  capturedImg = (android.widget.ImageView)findViewById(R.id.capturedImg);
	  getContact = (android.widget.ImageButton)findViewById(R.id.getContact);

   }

   private void setListeners(){
	  doneButton.setOnClickListener(this);
	  pickPhotoBtn.setOnClickListener(this);
	  getContact.setOnClickListener(this);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu){
	  getMenuInflater().inflate(R.menu.menu_add_new, menu);
	  return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item){
	  int id = item.getItemId();

	  if(id == R.id.action_settings){
		 return true;
	  }

	  return super.onOptionsItemSelected(item);
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
	  super.onActivityResult(requestCode, resultCode, data);

	  String path;

	  android.net.Uri uri = null;

	  try{
		 uri = data.getData();

	  }
	  catch(Exception e){ e.printStackTrace(); }

	  if(requestCode == PICK_PHOTO_FROM_GALLERY){

		 if(resultCode == RESULT_OK){
			if(uri != null){
			   path = getImagePath(data.getData());
			   imageBitmap = getScaledBitmap(path, 256);
			   capturedImg.setImageBitmap(imageBitmap);
			   blob = getBlob();

			   photoPicked = true;
			}
			else{
			   android.widget.Toast.makeText(this, "Could not load image", android.widget.Toast.LENGTH_LONG).show();
			}
		 }
		 else if(resultCode == RESULT_CANCELED){

			photoPicked = false;
		 }
	  }
	  else if(requestCode == CAPTURE_PHOTO_FROM_CAMERA){

		 if(resultCode == RESULT_OK){

			if(uri != null){
			   path = getImagePath(uri);
			   imageBitmap = getScaledBitmap(path, 256);
			   capturedImg.setImageBitmap(imageBitmap);
			   blob = getBlob();
			   photoPicked = true;
			}
			else{
			   android.widget.Toast.makeText(this, "Could not load image", android.widget.Toast.LENGTH_LONG).show();
			}

		 }
		 else if(resultCode == RESULT_CANCELED){

			photoPicked = false;
		 }
	  }
	  else if(requestCode == PICK_CONTACT){
		 if(resultCode == Activity.RESULT_OK)

		 {
			android.net.Uri contactData = data.getData();
			android.database.Cursor c = getContentResolver().query(contactData, null, null, null, null);
			if(c.moveToFirst()){
			   String id = c.getString(c.getColumnIndexOrThrow(android.provider.ContactsContract.Contacts._ID));

			   String hasPhone = c.getString(c.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER));

			   if(hasPhone.equalsIgnoreCase("1")){
				  android.database.Cursor phones = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
				  phones.moveToFirst();
				  contactPhone.setText(phones.getString(phones.getColumnIndex("data1")));

				  contactName.setText(phones.getString(phones.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME)));

			   }

			}
		 }

	  }

   }

   private byte[] getBlob(){

	  java.io.ByteArrayOutputStream boas = new java.io.ByteArrayOutputStream();
	  imageBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, boas);

	  byte[] byteArray = boas.toByteArray();

	  return byteArray;
   }

   private String getImagePath(android.net.Uri inUri){

	  String[] projection = new String[]{android.provider.MediaStore.MediaColumns.DATA};
	  android.database.Cursor cursor = this.managedQuery(inUri, projection, null, null, null);

	  int column_index = cursor.getColumnIndexOrThrow(android.provider.MediaStore.MediaColumns.DATA);
	  cursor.moveToFirst();

	  String path = cursor.getString(column_index);

	  return path;
   }

   private android.graphics.Bitmap getScaledBitmap(String path, int maxSize){

	  android.graphics.Bitmap bmp = null;
	  int width, height, inSampleSize;

	  android.graphics.BitmapFactory.Options op = new android.graphics.BitmapFactory.Options();
	  op.inJustDecodeBounds = true;
	  op.inPurgeable = true;

	  android.graphics.BitmapFactory.decodeFile(path, op);

	  width = op.outWidth;
	  height = op.outHeight;

	  if(width == - 1){

		 return null;
	  }

	  int max = Math.max(width, height);
	  inSampleSize = 1;

	  while(max > maxSize){
		 inSampleSize *= 2;
		 max /= 2;
	  }

	  op.inJustDecodeBounds = false;
	  op.inSampleSize = inSampleSize;

	  bmp = android.graphics.BitmapFactory.decodeFile(path, op);

	  return bmp;
   }

   @Override
   public void onClick(android.view.View v){

	  switch(v.getId()){
		 case R.id.doneBtn:
			prepareSendData();
			break;
		 case R.id.pickPhotoBtn:
			pickPhoto();
			break;
		 case R.id.getContact:
			pickContact();
			break;
	  }
   }

   public void pickContact(){
	  try{
		 android.content.Intent cIntent = new android.content.Intent(android.content.Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI);
		 startActivityForResult(cIntent, PICK_CONTACT);
	  }
	  catch(Exception e){
		 e.printStackTrace();
		 android.util.Log.i(TAG, "Exception while picking contact");
	  }
   }

   private void pickPhoto(){

	  final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

	  android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

	  builder.setTitle("Pick Photo");
	  builder.setItems(items, new android.content.DialogInterface.OnClickListener() {

		 @Override
		 public void onClick(android.content.DialogInterface dialog, int which){
			if(items[which].equals("Capture Photo")){

			   android.content.Intent intent = new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			   startActivityForResult(intent, CAPTURE_PHOTO_FROM_CAMERA);
			}
			else if(items[which].equals("Choose from Gallery")){

			   android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			   intent.setType("image/*");
			   startActivityForResult(android.content.Intent.createChooser(intent, "Select Photo"), PICK_PHOTO_FROM_GALLERY);
			}
			else if(items[which].equals("cancel")){

			   dialog.dismiss();
			   photoPicked = false;
			}
		 }
	  });

	  builder.show();

   }

   private void prepareSendData(){

	  if(android.text.TextUtils.isEmpty(contactName.getText().toString()) || android.text.TextUtils.isEmpty((contactPhone.getText().toString()))){

		 android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(AddNewContactActivity.this);
		 alertDialogBuilder.setTitle("Empty fields");
		 alertDialogBuilder.setMessage("Please fill phone number and name").setCancelable(true);
		 alertDialogBuilder.setNegativeButton("OK", new android.content.DialogInterface.OnClickListener() {

			public void onClick(android.content.DialogInterface dialog, int id){
			   dialog.cancel();
			}
		 });
		 alertDialogBuilder.show();
	  }
	  else{
		 ContactModel contact = new ContactModel();
		 contact.setName(contactName.getText().toString());
		 contact.setContactNo(contactPhone.getText().toString());
		 contact.setEmail(contactEmail.getText().toString());

		 if(photoPicked){
			contact.setPhoto(blob);
		 }
		 else{
			contact.setPhoto(null);
		 }

		 com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager dm = new com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager(this);

		 if(reqType == ContactsMainActivity.CONTACT_UPDATE_REQ_CODE){
			dm.updateRowAlternative(rowId, contact);
		 }
		 else{
			dm.addRowAlternative(contact);
		 }

		 setResult(RESULT_OK);
		 finish();

	  }

   }

}
