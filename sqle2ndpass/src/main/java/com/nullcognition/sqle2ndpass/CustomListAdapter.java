package com.nullcognition.sqle2ndpass;

public class CustomListAdapter extends android.widget.BaseAdapter {

   com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager   dm;
   java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel> contactModelList;
   android.view.LayoutInflater                                     inflater;
   android.content.Context                                         _context;

   public CustomListAdapter(android.content.Context context){

	  contactModelList = new java.util.ArrayList<com.nullcognition.sqle2ndpass.ContactModel>();
	  _context = context;
	  inflater = (android.view.LayoutInflater)context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE);
	  dm = new com.nullcognition.sqle2ndpass.databasemanager.DatabaseManager(_context);
	  contactModelList = dm.getAllDataAlternative();

   }

   @Override
   public void notifyDataSetChanged(){
	  super.notifyDataSetChanged();

	  contactModelList = dm.getAllDataAlternative();

   }

   public void delRow(int delPosition){

	  dm.deleteRowAlternative(contactModelList.get(delPosition).getId());
	  contactModelList.remove(delPosition);

   }

   @Override
   public int getCount(){
	  return contactModelList.size();
   }

   @Override
   public Object getItem(int position){
	  return contactModelList.get(position);
   }

   @Override
   public long getItemId(int position){
	  return 0;
   }

   @Override
   public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent){
	  ViewHolder vHolder;
	  if(convertView == null){
		 convertView = inflater.inflate(R.layout.contact_list_row, null);
		 vHolder = new ViewHolder();

		 vHolder.contact_name = (android.widget.TextView)convertView.findViewById(R.id.contact_name);
		 vHolder.contact_phone = (android.widget.TextView)convertView.findViewById(R.id.contact_phone);
		 vHolder.contact_email = (android.widget.TextView)convertView.findViewById(R.id.contact_email);
		 vHolder.contact_photo = (android.widget.ImageView)convertView.findViewById(R.id.contact_photo);
		 convertView.setTag(vHolder);
	  }
	  else{
		 vHolder = (ViewHolder)convertView.getTag();
	  }

	  ContactModel contactObj = contactModelList.get(position);

	  vHolder.contact_name.setText(contactObj.getName());
	  vHolder.contact_phone.setText(contactObj.getContactNo());
	  vHolder.contact_email.setText(contactObj.getEmail());
	  setImage(contactObj.getPhoto(), vHolder.contact_photo);

	  return convertView;
   }

   class ViewHolder {

	  android.widget.ImageView contact_photo;
	  android.widget.TextView  contact_name, contact_phone, contact_email;
   }

   private void setImage(byte[] blob, android.widget.ImageView img){

	  if(blob != null){
		 android.graphics.Bitmap bmp = android.graphics.BitmapFactory.decodeByteArray(blob, 0, blob.length);
		 img.setImageBitmap(bmp);
	  }
   }
}
