package maus.mausprojekt.contact;

import java.util.ArrayList;

public class ContactDAO {


    private ArrayList<ContactDataModel> contactList;
    public static String serializedFile;

    public ContactDAO() {

    }

    public ArrayList<ContactDataModel> getContactList() {
        return contactList;
    }

    public void setContactList(ArrayList<ContactDataModel> contactList) {
        this.contactList = contactList;
    }

    public static String getSerializedFile() {
        return serializedFile;
    }

    public void setSerializedFile(String serializeFile) {
        this.serializedFile = serializeFile;
    }

}
