package com.nullcognition.sqle2ndpass;
/**
 * Created by ersin on 26/11/14 at 4:13 PM
 */
public class ContactModel {

   private int id;
   private String name, contactNo, email;
   private byte[] byteArray;

   public int getId(){
	  return id;
   }

   public void setId(int inId){
	  id = inId;
   }

   public String getName(){
	  return name;
   }

   public void setName(String inName){
	  name = inName;
   }

   public String getContactNo(){
	  return contactNo;
   }

   public void setContactNo(String inContactNo){
	  contactNo = inContactNo;
   }

   public String getEmail(){
	  return email;
   }

   public void setEmail(String inEmail){
	  email = inEmail;
   }

   public byte[] getPhoto(){
	  return byteArray;
   }

   public void setPhoto(byte[] inByteArray){
	  byteArray = inByteArray;
   }
}
