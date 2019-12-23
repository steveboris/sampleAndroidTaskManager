package maus.mausprojekt.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import maus.mausprojekt.AddTodoActivity;
import maus.mausprojekt.R;

import java.util.ArrayList;

public class ContactViewActivity extends AppCompatActivity {

    private CustomContactAdapter adapter;
    private ListView cv;
    private ArrayList<ContactDataModel> associatedContacts = new ArrayList<>();
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);
        //
        cv = (ListView)findViewById(R.id.contact_view);
        tv = (TextView) findViewById(R.id.tv_contact);
        //
        try {
            loadAssociatedContacts();
        } catch (NullPointerException npe) {

        }

        // set a context menu for the long click action
        registerForContextMenu(cv);
    }

    private void loadAssociatedContacts() {
        //Intent intent = getIntent();
        Gson gson = new Gson();
        // get the serialized contact file
        String serializedFile = ContactDAO.serializedFile;
        // deserialize the file to get all contacts information
        associatedContacts = gson.fromJson(serializedFile, new TypeToken<ArrayList<ContactDataModel>>(){}.getType());
        //display information
        if (associatedContacts != null) {
            adapter = new CustomContactAdapter(this, R.layout.view_contact_row, associatedContacts);
            cv.setAdapter(adapter);
            // In case that the user delete some contacts, we set the serialized file in the DAO to apply the change
            // list of contact
            // serialize and set the access file
            ContactDAO.serializedFile = gson.toJson(associatedContacts);
        }

        //on item click
        cv.setOnItemClickListener(itemDescription);
    }

    // Action for a click on item
    public AdapterView.OnItemClickListener itemDescription = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ContactDataModel obj = (ContactDataModel) adapter.getItem(position);
            AlertDialog dialog = new AlertDialog.Builder(ContactViewActivity.this)
                    .setTitle(obj.getName())
                    .setMessage("Phone: " + obj.getPhone() +"\nEmail: " + obj.getEmail())
                    .setNegativeButton("OK", null)
                    .create();
            dialog.show();
        }
    };

    // Context menu for long click action
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.contact_view) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo mInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
            ContactDataModel obj = (ContactDataModel) listView.getItemAtPosition(mInfo.position);
            //Retreive the task name
            String name = obj.getName();
            //Set this name as title for the context menu
            menu.setHeaderTitle(name);
            //Adding actions
            menu.add(0, v.getId(), 0, "Remove");
            menu.add(0, v.getId(), 0, "Send mail");
            menu.add(0, v.getId(), 0, "Send sms");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo mInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getTitle().toString()) {
            case "Remove":
                deleteSelectedItem(mInfo.position);
                break;
            case "Send mail":
                sendEmail(mInfo.position);
                break;
            case "Send sms":
                sendSms(mInfo.position);
                break;
        }
        return true;
    }

    private void sendSms(int position) {
        ContactDataModel contact = (ContactDataModel) adapter.getItem(position);
        String phone = contact.getPhone();
        if (!phone.equals("")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phone));
            intent.putExtra("sms_body", "");
            startActivity(intent);
        } else {
            Toast.makeText(this, "No number associated to this contact", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(int position) {
        ContactDataModel contact = (ContactDataModel) adapter.getItem(position);
        String[] TO = {contact.getEmail()};
        String[] CC = {""};
        if (!TO.equals("")) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("mailto:"));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, TO);
            intent.putExtra(Intent.EXTRA_CC, CC);
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."));
                finish();
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No email associated to this contact", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteSelectedItem(int position) {
        ContactDataModel contact = (ContactDataModel) adapter.getItem(position);
        associatedContacts.remove(contact);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, contact.getName() + " removed", Toast.LENGTH_SHORT).show();
        //
        adapter = new CustomContactAdapter(this , R.layout.view_contact_row, associatedContacts);
        cv.setAdapter(adapter);
        //
        Gson gson = new Gson();
        ContactDAO.serializedFile = gson.toJson(associatedContacts);
    }

    @Override
    public void onBackPressed() {
        Gson gson = new Gson();
        Intent intent = new Intent(this, AddTodoActivity.class);
        String serializeFile = ContactDAO.serializedFile;
        intent.putExtra(ContactDAO.serializedFile, serializeFile);
        setResult(10, intent);
        finish();
        super.onBackPressed();
    }

}
