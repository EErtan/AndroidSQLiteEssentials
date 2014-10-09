package com.nullcognition.androidsqliteessentials;
/**
 * Created by ersin on 08/10/14 at 10:04 PM
 */
public class ModelContact {

  private int    contactId;
  private String contactName, contactPhoneNumber, contactEmail;
  private byte[] contactPhoto;

  public int getContactId(){
	return contactId;
  }

  public void setContactId(int inContactId){
	contactId = inContactId;
  }

  public String getContactName(){
	return contactName;
  }

  public void setContactName(String inContactName){
	contactName = inContactName;
  }

  public String getContactPhoneNumber(){
	return contactPhoneNumber;
  }

  public void setContactPhoneNumber(String inContactPhoneNumber){
	contactPhoneNumber = inContactPhoneNumber;
  }

  public String getContactEmail(){
	return contactEmail;
  }

  public void setContactEmail(String inContactEmail){
	contactEmail = inContactEmail;
  }

  public byte[] getContactPhoto(){
	return contactPhoto;
  }

  public void setContactPhoto(byte[] inContactPhoto){
	contactPhoto = inContactPhoto;
  }

}
