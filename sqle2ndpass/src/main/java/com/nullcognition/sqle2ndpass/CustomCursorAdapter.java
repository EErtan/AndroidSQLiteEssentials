package com.nullcognition.sqle2ndpass;
/**
 * Created by ersin on 30/11/14 at 1:07 AM
 */
public class CustomCursorAdapter extends android.widget.CursorAdapter {

   android.content.Context context;

   public CustomCursorAdapter(android.content.Context context, android.database.Cursor c){
	  super(context, c);
	  this.context = context;
   }

   @Override
   public void bindView(android.view.View view, android.content.Context arg1, android.database.Cursor cursor){
	  final android.widget.ImageView contact_photo = (android.widget.ImageView)view.findViewById(R.id.contact_photo);
	  final android.widget.TextView contact_name = (android.widget.TextView)view.findViewById(R.id.contact_name);
	  final android.widget.TextView contact_phone = (android.widget.TextView)view.findViewById(R.id.contact_phone);
	  final android.widget.TextView contact_email = (android.widget.TextView)view.findViewById(R.id.contact_email);
	  contact_name.setText(cursor.getString(cursor.getColumnIndexOrThrow(com.nullcognition.sqle2ndpass.databasemanager.DatabaseConstants.TABLE_ROW_NAME)));
	  contact_phone.setText(cursor.getString(cursor.getColumnIndexOrThrow(com.nullcognition.sqle2ndpass.databasemanager.DatabaseConstants.TABLE_ROW_PHONENUM)));
	  contact_email.setText(cursor.getString(cursor.getColumnIndexOrThrow(com.nullcognition.sqle2ndpass.databasemanager.DatabaseConstants.TABLE_ROW_EMAIL)));
	  setImage(cursor.getBlob(cursor.getColumnIndex(com.nullcognition.sqle2ndpass.databasemanager.DatabaseConstants.TABLE_ROW_PHOTOID)), contact_photo);
   }

   @Override
   public android.view.View newView(android.content.Context arg0, android.database.Cursor arg1, android.view.ViewGroup arg2){
	  final android.view.View view = android.view.LayoutInflater.from(context).inflate(R.layout.contact_list_row, null, false);
	  return view;
   }

   private void setImage(byte[] blob, android.widget.ImageView img){

	  if(blob != null){
		 android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(blob, 0, blob.length);
		 img.setImageBitmap(bmp);
	  }
   }

}
