package com.qylk.code.widgets;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.qylk.code.R;

public class testAutoCompleteText extends Activity implements Validator {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autocomplete);
        AutoCompleteTextView act1 = (AutoCompleteTextView) findViewById(R.id.act1);
        act1.setThreshold(3);
        act1.setValidator(this);

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        // android.R.layout.simple_dropdown_item_1line,
        // COUNTRIES);
        ContactsAdapter adapter = new ContactsAdapter(this, null);
        act1.setAdapter(adapter);
    }

    private class ContactsAdapter extends CursorAdapter {
        private static final int COLUMN_DISPLAY_NAME = 1;
        private ContentResolver mContentResolver;

        @SuppressWarnings("deprecation")
        public ContactsAdapter(Context context, Cursor c) {
            super(context, c);
            mContentResolver = context.getContentResolver();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final TextView view = (TextView) inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false);
            view.setText(cursor.getString(COLUMN_DISPLAY_NAME));
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ((TextView) view).setText(cursor.getString(COLUMN_DISPLAY_NAME));
        }

        @Override
        public String convertToString(Cursor cursor) {// ����ѡ�к���ʾ��EditTextView��
            return cursor.getString(COLUMN_DISPLAY_NAME);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            // FilterQueryProvider filter = getFilterQueryProvider();
            // if (filter != null) {
            // return filter.runQuery(constraint);
            // }
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_FILTER_URI,
                    constraint.toString());
            return mContentResolver.query(uri, PROJECTION, null, null, null);
        }
    }

    static final String[] PROJECTION = new String[]{
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};

    // static final String[] COUNTRIES = new String[] { "Afghanistan",
    // "Albania",
    // "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
    // "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia",
    // "Aruba", "Australia", "Austria", "Azerbaijan", "Bahrain",
    // "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
    // "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina",
    // "Botswana", "Bouvet Island", "Brazil",
    // "British Indian Ocean Territory", "British Virgin Islands",
    // "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cote d'Ivoire",
    // "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands",
    // "Central African Republic", "Chad", "Chile", "China",
    // "Christmas Island", "Cocos (Keeling) Islands", "Colombia",
    // "Comoros", "Congo", "Cook Islands", "Costa Rica", "Croatia",
    // "Cuba", "Cyprus", "Czech Republic",
    // "Democratic Republic of the Congo", "Denmark", "Djibouti",
    // "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt",
    // "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
    // "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji",
    // "Finland", "Former Yugoslav Republic of Macedonia", "France",
    // "French Guiana", "French Polynesia", "French Southern Territories",
    // "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
    // "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala",
    // "Guinea", "Guinea-Bissau", "Guyana", "Haiti",
    // "Heard Island and McDonald Islands", "Honduras", "Hong Kong",
    // "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq",
    // "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
    // "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
    // "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya",
    // "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Madagascar",
    // "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
    // "Marshall Islands", "Martinique", "Mauritania", "Mauritius",
    // "Mayotte", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia",
    // "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
    // "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
    // "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria",
    // "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
    // "Norway", "Oman", "Pakistan", "Palau", "Panama",
    // "Papua New Guinea", "Paraguay", "Peru", "Philippines",
    // "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
    // "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe",
    // "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia",
    // "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines",
    // "Samoa", "San Marino", "Saudi Arabia", "Senegal", "Seychelles",
    // "Sierra Leone", "Singapore", "Slovakia", "Slovenia",
    // "Solomon Islands", "Somalia", "South Africa",
    // "South Georgia and the South Sandwich Islands", "South Korea",
    // "Spain", "Sri Lanka", "Sudan", "Suriname",
    // "Svalbard and Jan Mayen", "Swaziland", "Sweden", "Switzerland",
    // "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand",
    // "The Bahamas", "The Gambia", "Togo", "Tokelau", "Tonga",
    // "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan",
    // "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
    // "Ukraine", "United Arab Emirates", "United Kingdom",
    // "United States", "United States Minor Outlying Islands", "Uruguay",
    // "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam",
    // "Wallis and Futuna", "Western Sahara", "Yemen", "Yugoslavia",
    // "Zambia", "Zimbabwe" };

    @Override
    public boolean isValid(CharSequence text) {
        return true;
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
        // TODO Auto-generated method stub
        return null;
    }

}
