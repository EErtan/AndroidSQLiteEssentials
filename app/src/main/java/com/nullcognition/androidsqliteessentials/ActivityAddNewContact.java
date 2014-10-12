package com.nullcognition.androidsqliteessentials;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityAddNewContact extends Activity implements android.view.View.OnClickListener {

  public final static int pickPhotoFromGallery   = 1001;
  public final static int capturePhotoFromCamera = 1002;

  private android.widget.TextView  contactName     = null;
  private android.widget.TextView  contactPhone    = null;
  private android.widget.TextView  contactEmail    = null;
  private android.widget.TextView  contactPhoto    = null;
  private android.widget.Button    buttonDone      = null;
  private android.widget.Button    buttonPickPhoto = null;
  private android.widget.ImageView capturedImage   = null;
  private android.graphics.Bitmap  bitmap          = null;
  private byte[]                   blob            = null;
  private boolean                  photoPicked     = false;
  private int                      reqType         = 0;
  private int                      rowId           = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState){
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_add_new_contact);

	reqType = getIntent().getIntExtra(ActivityMain.reqType, ActivityMain.contactAddReqCode);

	bindViewsListeners();

	if(reqType == com.nullcognition.androidsqliteessentials.ActivityMain.contactUpdateReqCode){

	  rowId = getIntent().getIntExtra(ActivityMain.itemPosition, 1);
	  initialize(rowId);
	}
  }

  private void bindViewsListeners(){

	contactName = (android.widget.TextView)findViewById(com.nullcognition.androidsqliteessentials.R.id.contactName);
	contactPhone = (android.widget.TextView)findViewById(com.nullcognition.androidsqliteessentials.R.id.contactPhone);
	contactEmail = (android.widget.TextView)findViewById(com.nullcognition.androidsqliteessentials.R.id.contactEmail);
	contactPhoto = (android.widget.TextView)findViewById(com.nullcognition.androidsqliteessentials.R.id.contactPhoto);
	buttonDone = (android.widget.Button)findViewById(com.nullcognition.androidsqliteessentials.R.id.buttonDone);
	buttonPickPhoto = (android.widget.Button)findViewById(com.nullcognition.androidsqliteessentials.R.id.buttonPickPhoto);
	capturedImage = (android.widget.ImageView)findViewById(com.nullcognition.androidsqliteessentials.R.id.contactPhoto);

	buttonDone.setOnClickListener(this);
	buttonPickPhoto.setOnClickListener(this);

  }

  private void initialize(int position){

	DatabaseManager databaseManager = new DatabaseManager(this);
	ModelContact contactObj = databaseManager.getRowAsObjectAlternative(position);

	contactName.setText(contactObj.getContactName());
	contactPhone.setText(contactObj.getContactPhoneNumber());
	contactEmail.setText(contactObj.getContactEmail());

	setImage(contactObj.getContactPhoto(), capturedImage);
  }

  private void setImage(byte[] blob, android.widget.ImageView img){

	if(blob != null){
	  android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(blob, 0, blob.length);
	  img.setImageBitmap(bmp);
	}
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu){
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.activity_add_new_contact, menu);
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
  protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
	super.onActivityResult(requestCode, resultCode, data);

	String path;
	android.net.Uri uri = data.getData();

	if(requestCode == pickPhotoFromGallery){

	  if(resultCode == RESULT_OK){
		if(uri != null){
		  path = getImagePath(data.getData());
		  bitmap = getScaledBitmap(path, 256);
		  capturedImage.setImageBitmap(bitmap);
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
	else if(requestCode == capturePhotoFromCamera){

	  if(resultCode == RESULT_OK){

		if(uri != null){
		  path = getImagePath(uri);
		  bitmap = getScaledBitmap(path, 256);
		  capturedImage.setImageBitmap(bitmap);
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
  }

  private String getImagePath(android.net.Uri uri){

	String[] projection = new String[]{android.provider.MediaStore.MediaColumns.DATA};
	android.database.Cursor cursor = this.managedQuery(uri, projection, null, null, null);
	// android.app.LoaderManager loaderManager = getLoaderManager(); // look into the depretiation upgrade with loaderManager

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

  private byte[] getBlob(){

	java.io.ByteArrayOutputStream byteArrayOutputStream = new java.io.ByteArrayOutputStream();

	bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

	return byteArrayOutputStream.toByteArray();
  }

  @Override
  public void onClick(android.view.View v){

	switch(v.getId()){
	  case R.id.buttonDone:
		prepareSendData();
		break;
	  case R.id.buttonPickPhoto:
		pickPhoto();
		break;
	  default:
		android.util.Log.e("Switch v.getId()", "Default parameter invalid");
		throw new java.security.InvalidParameterException();
	}

  }

  private void prepareSendData(){
	// isEmpty returns true if null or of length zero
	if(android.text.TextUtils.isEmpty(contactName.getText().toString()) || android.text.TextUtils.isEmpty(contactPhone.getText().toString())){

	  android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ActivityAddNewContact.this);
	  alertDialogBuilder.setTitle("Empty fields");
	  alertDialogBuilder.setMessage("Please fill phone number and name").setCancelable(true);
	  alertDialogBuilder.setNegativeButton("OK", new android.content.DialogInterface.OnClickListener() {

		public void onClick(android.content.DialogInterface dialog, int id){
		  // if this button is clicked, just close
		  // the dialog box and do nothing
		  dialog.cancel();
		}
	  });
	  alertDialogBuilder.show();
	}
	else{

	  ModelContact contact = new ModelContact();
	  contact.setContactName(contactName.getText().toString());
	  contact.setContactPhoneNumber(contactPhone.getText().toString());
	  contact.setContactEmail(contactEmail.getText().toString());

	  if(photoPicked){
		contact.setContactPhoto(blob);
	  }
	  else{
		contact.setContactPhoto(null);
	  }

	  DatabaseManager databaseManager = new DatabaseManager(this);

	  if(reqType == com.nullcognition.androidsqliteessentials.ActivityMain.contactUpdateReqCode){
		databaseManager.updateRowAlternative(rowId, contact);
	  }
	  else{
		databaseManager.addRowAlternative(contact);
	  }

	  setResult(Activity.RESULT_OK);
	  finish();

	}

  }

  private void pickPhoto(){

	final CharSequence[] items = {"Capture Photo", "Choose from Gallery", "Cancel"};

	android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ActivityAddNewContact.this);
	builder.setTitle("Pick Photo");
	builder.setItems(items, new android.content.DialogInterface.OnClickListener() {

	  @Override
	  public void onClick(android.content.DialogInterface dialog, int item){

		if(items[item].equals("Capture Photo")){

		  android.content.Intent intent = new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		  startActivityForResult(intent, capturePhotoFromCamera);
		}
		else if(items[item].equals("Choose from Gallery")){

		  android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK,
																	 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		  intent.setType("image/*");
		  startActivityForResult(android.content.Intent.createChooser(intent, "Select Photo"), pickPhotoFromGallery);

		}
		else if(items[item].equals("Cancel")){

		  dialog.dismiss();
		  photoPicked = false;
		}
	  }

	});
	builder.show();
  }
}
