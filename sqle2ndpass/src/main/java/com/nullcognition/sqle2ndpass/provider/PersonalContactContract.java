package com.nullcognition.sqle2ndpass.provider;
/**
 * Created by ersin on 28/11/14 at 4:37 PM
 */
public final class PersonalContactContract implements com.nullcognition.sqle2ndpass.databasemanager.DatabaseConstants {

   // authority of provider
   public static final String          AUTHORITY         = "com.nullcognition.sqle2ndpass.provider";
   //mime type of directory items
   public static final String          CONTENT_TYPE      = android.content.ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" + AUTHORITY + ".table";
   // mime type of single item
   public static final String          CONTENT_ITEM_TYPE = android.content.ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd" + AUTHORITY + ".table_item";
   public static final String          BASE_PATH         = "contacts";
   // uri for top level provider authority
   public static final android.net.Uri CONTENT_URI       = android.net.Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
   // projection of all columns in the items table
   public static final String[]        PROJECTION_ALL    = {"_id", "contact_name", "contact_number", "contact_email", "photo_id"};


   public static final class Columns {

	  public static String TABLE_ROW_ID       = "_id";
	  public static String TABLE_ROW_NAME     = "contact_name";
	  public static String TABLE_ROW_PHONENUM = "contact_number";
	  public static String TABLE_ROW_EMAIL    = "contact_email";
	  public static String TABLE_ROW_PHOTOID  = "photo_id";
   }

   // default sort order for queries containing NAME fields
   //public static final String SORT_ORDER_DEFAULT = NAME + " ASC";

}
