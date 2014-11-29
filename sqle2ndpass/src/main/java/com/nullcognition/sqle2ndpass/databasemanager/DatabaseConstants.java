package com.nullcognition.sqle2ndpass.databasemanager;
/**
 * Created by ersin on 28/11/14 at 1:35 PM
 */
public interface DatabaseConstants {

   // public static final by default*
   String DB_NAME    = "contact";
   int    DB_VERSION = 1;

   String TABLE_NAME         = "contact_table";
   String TABLE_ROW_ID       = "_id";
   String TABLE_ROW_NAME     = "contact_name";
   String TABLE_ROW_PHONENUM = "contact_number";
   String TABLE_ROW_EMAIL    = "contact_email";
   String TABLE_ROW_PHOTOID  = "photo_id";
}
