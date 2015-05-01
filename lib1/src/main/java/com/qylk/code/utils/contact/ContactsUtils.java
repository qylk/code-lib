package com.qylk.code.utils.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;

import java.util.ArrayList;
import java.util.List;

public class ContactsUtils {

    public static List<ContactEntity> readContacts(Context context) {
        List<ContactEntity> contacts = new ArrayList<ContactEntity>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Contacts.CONTENT_URI,
                new String[]{Contacts._ID,
                        Contacts.DISPLAY_NAME}, Contacts.HAS_PHONE_NUMBER + "=1", null,
                null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            ContactEntity contact = new ContactEntity();
            contact.setId(Integer.valueOf(contactId));
            contact.setDisplayName(cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME)));
            Cursor phonesCursor = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactId}, null);
            List<String> phones = new ArrayList<String>();
            while (phonesCursor != null && phonesCursor.moveToNext()) {
                phones.add(cursor.getString(0));
            }
            contact.setPhones(phones);
            contacts.add(contact);
            if (phonesCursor != null)
                phonesCursor.close();
        }
        if (cursor != null)
            cursor.close();
        return contacts;
    }
}
