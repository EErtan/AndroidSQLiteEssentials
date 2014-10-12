package com.nullcognition.androidsqliteessentials;
/**
 * Created by ersin on 10/10/14 at 1:05 PM
 */
public class CustomListAdapter extends android.widget.BaseAdapter {

  DatabaseManager                                                             databaseManager;
  java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact> ModelContactList;
  android.view.LayoutInflater                                                 inflater;
  android.content.Context                                                     _context;

  public CustomListAdapter(android.content.Context context){

	ModelContactList = new java.util.ArrayList<com.nullcognition.androidsqliteessentials.ModelContact>();
	_context = context;
	inflater = (android.view.LayoutInflater)context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	databaseManager = new DatabaseManager(_context);
	ModelContactList = databaseManager.getAllDataAlternative();

  }

  @Override
  public void notifyDataSetChanged(){
	super.notifyDataSetChanged();
	//refetching the new data from database
	ModelContactList = databaseManager.getAllDataAlternative();

  }

  public void delRow(int delPosition){

	databaseManager.deleteRowAlternative(ModelContactList.get(delPosition).getContactId());
	ModelContactList.remove(delPosition);

  }

  @Override
  public int getCount(){
	return ModelContactList.size();
  }

  @Override
  public Object getItem(int position){
	return ModelContactList.get(position);
  }

  @Override
  public long getItemId(int position){
	return 0;
  }

  @Override
  public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent){
	ViewHolder vHolder;
	if(convertView == null){
	  convertView = inflater.inflate(com.nullcognition.androidsqliteessentials.R.layout.activity_add_new_contact, null);
	  vHolder = new ViewHolder();

	  vHolder.contact_name = (android.widget.TextView)convertView.findViewById(com.nullcognition.androidsqliteessentials.R.id.contactName);
	  vHolder.contact_phone = (android.widget.TextView)convertView.findViewById(com.nullcognition.androidsqliteessentials.R.id.contactPhone);
	  vHolder.contact_email = (android.widget.TextView)convertView.findViewById(com.nullcognition.androidsqliteessentials.R.id.contactEmail);
	  vHolder.contact_photo = (android.widget.ImageView)convertView.findViewById(com.nullcognition.androidsqliteessentials.R.id.contactPhoto);
	  convertView.setTag(vHolder);
	}
	else{
	  vHolder = (ViewHolder)convertView.getTag();
	}

	ModelContact contactObj = ModelContactList.get(position);

	vHolder.contact_name.setText(contactObj.getContactName());
	vHolder.contact_phone.setText(contactObj.getContactPhoneNumber());
	vHolder.contact_email.setText(contactObj.getContactEmail());
	setImage(contactObj.getContactPhoto(), vHolder.contact_photo);

	return convertView;
  }

  /**
   * @return void
   * @brief: setImage
   * @detail: returns the Image in the Blob format (byte array format)
   */
  private void setImage(byte[] blob, android.widget.ImageView img){

	if(blob != null){
	  android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(blob, 0, blob.length);
	  img.setImageBitmap(bmp);
	}
  }

  class ViewHolder {

	android.widget.ImageView contact_photo;
	android.widget.TextView  contact_name, contact_phone, contact_email;
  }
}
