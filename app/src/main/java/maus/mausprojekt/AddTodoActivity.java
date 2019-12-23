package maus.mausprojekt;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import maus.mausprojekt.adapter.SortListArray;
import maus.mausprojekt.contact.ContactDAO;
import maus.mausprojekt.contact.ContactDataModel;
import maus.mausprojekt.contact.ContactViewActivity;
import maus.mausprojekt.model.Todo;
import maus.mausprojekt.room.RoomTodoListAccessor;
import maus.mausprojekt.room.spi.TodoListAccessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddTodoActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_back_to_listview;

    Todo todo;
    Button btnDatePicker, btnTimePicker, btnAddTodo, btnEditTodo, btnUpdateTodo, btnDeleteTodo;
    EditText  txtName, txtDescription, editTextId;
    TextView txtDate, txtTime;
    private CheckBox important, done;
    private TodoListAccessor accessor;
    private Calendar calendar;
    private Button btn_contact;
    private TextView tv;
    private static final int RESULT_PICK_CONTACT = 1;
    private ArrayList<ContactDataModel> associatedContacts = new ArrayList<>();
    private ContactDAO contactDAO;
    private Intent intentContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        accessor = new RoomTodoListAccessor();
        ((RoomTodoListAccessor) accessor).setActivity(this);
        txtName=findViewById(R.id.taskName);
        txtDate=findViewById(R.id.date);
        txtTime=findViewById(R.id.time);
        important=findViewById(R.id.checkbox_imp);
        done=findViewById(R.id.checkbox_done);
        txtDescription=findViewById(R.id.description);
        btn_back_to_listview = findViewById(R.id.btn_cancel);
        btnAddTodo = findViewById(R.id.btn_save);
        btnUpdateTodo = findViewById(R.id.btn_update);
        btnUpdateTodo.setVisibility(View.GONE);
        btnDeleteTodo = findViewById(R.id.btn_delete_todo);
        calendar = Calendar.getInstance();
        //
        btn_contact = (Button)findViewById(R.id.btn_contact);
        tv = (TextView)findViewById(R.id.tv_contact);

        contactDAO = new ContactDAO();
        todo = (Todo) getIntent().getSerializableExtra("TODO");
        if(todo != null){
            btnAddTodo.setVisibility(View.GONE);
            btnUpdateTodo.setVisibility(View.VISIBLE);
            btnDeleteTodo.setVisibility(View.VISIBLE);
            txtName.setText(todo.getName());
            txtDescription.setText(todo.getDescription());
            important.setChecked(todo.getIsImportant() == 1? true : false);
            done.setChecked(todo.getIsDone() == 1 ? true : false);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date inputDate = new Date(todo.getDate());
            txtTime.setText(sdf.format(inputDate));
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            txtDate.setText(sdf.format(inputDate));
            calendar.setTime(inputDate);
            //Contact
            String serializedContact = todo.getContact();
            associatedContacts = deserialize(serializedContact);
            if (associatedContacts != null) {
                tv.setText("Associated contacts (" + associatedContacts.size() + ")");
                intentContact = new Intent(getApplicationContext(), ContactViewActivity.class);
                intentContact.putExtra(ContactDAO.serializedFile, serializedContact);
            }

        }
        else{
            btnDeleteTodo.setVisibility(View.GONE);
        }

        btn_back_to_listview.setOnClickListener(this);
        btnUpdateTodo.setOnClickListener(this);
        btnAddTodo.setOnClickListener(this);
        btnDeleteTodo.setOnClickListener(this);
        TodoListAccessor accessor = new RoomTodoListAccessor();
        ((RoomTodoListAccessor) accessor).setActivity(this);
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialisiere DatePicker default
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTodoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year,month,dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        txtDate.setText(sdf.format(calendar.getTime()));
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initialisiere TimePicker default
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);
                final TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodoActivity.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute,0);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                        txtTime.setText(dateFormat.format(calendar.getTime())+" Uhr");
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        try {
            btn_contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult( intent, RESULT_PICK_CONTACT);
                }
            });
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

        if(v == btn_back_to_listview){
            startActivity(new Intent( AddTodoActivity.this, ListViewActivity.class));
        }

        if(v == btnAddTodo){
            Todo todo = new Todo();
            //check if the same already exists
            String newTodoname = txtName.getText().toString();
            boolean exists = false;
            for (Todo values : accessor.getAllTodos()) {
                if (values.getName().equals(newTodoname)) {
                    exists = true;
                    break;
                }
            }

            if (exists == false && !newTodoname.equals("")) {
                todo.setName(txtName.getText().toString());
                todo.setIsDone(done.isChecked() ? 1 : 0);
                todo.setIsImportant(important.isChecked() ? 1 : 0);
                todo.setDescription(txtDescription.getText().toString());
                todo.setDate(calendar.getTimeInMillis());
                todo.setContact(ContactDAO.serializedFile);
                accessor.insertTodo(todo);

                Toast.makeText(this, "New TODO added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent( AddTodoActivity.this, ListViewActivity.class));
            } else {
                if (newTodoname.equals(""))
                    Toast.makeText(this, "Give a name to this todo", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "A todo with the same name already exists", Toast.LENGTH_LONG).show();
            }
        }

        if(v == btnUpdateTodo) {
            todo.setName(txtName.getText().toString());
            todo.setIsDone(done.isChecked() ? 1 : 0);
            todo.setIsImportant(important.isChecked() ? 1 : 0);
            todo.setDescription(txtDescription.getText().toString());
            todo.setDate(calendar.getTimeInMillis());
            todo.setContact(ContactDAO.serializedFile);
            accessor.updateTodo(todo);
            Toast.makeText(this, "TODO updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent( AddTodoActivity.this, ListViewActivity.class));
        }

        if(v == btnDeleteTodo){
            new AlertDialog.Builder(AddTodoActivity.this)
                    .setMessage("Do you want to delete this TODO:\n "+ todo.getName() + " ?")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            accessor.deleteTodo(todo);
                            Toast.makeText(AddTodoActivity.this, "TODO DELETED", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddTodoActivity.this, ListViewActivity.class));
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            //Toast.makeText(this, "Failed to pick Contact", Toast.LENGTH_SHORT).show();
        }

        if (resultCode == 10) {
            String serializedFile = data.getStringExtra(ContactDAO.serializedFile);
            associatedContacts = deserialize(serializedFile);

            if (associatedContacts != null && associatedContacts.size() > 0)
                tv.setText("Associated contacts (" + associatedContacts.size() + ")");
            else
                tv.setText("");
        }
    }

    private void contactPicked(Intent data) {

        // in case that the user want to add more contact
        try {
            Uri contactData = data.getData();
            Cursor cursor =  getContentResolver().query(contactData, null, null, null, null);

            if (cursor.moveToFirst()) {

                String phone= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String email = "";
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                // Find Email Addresses
                Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
                while (emails.moveToNext())
                {
                    email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
                emails.close();

                //check if the contact is not already in the list of associated contacts
                boolean exists = false;
                if (associatedContacts != null) {
                    for (ContactDataModel contact : associatedContacts) {
                        if (contact.getName().equals(name)) {
                            Toast.makeText(this, "Already associated!", Toast.LENGTH_LONG).show();
                            exists = true;
                            break;
                        }
                    }
                }

                if (exists == false && associatedContacts != null) {
                    ContactDataModel contact = new ContactDataModel(name, phone, email);
                    associatedContacts.add(contact);
                }

                //increase the number of associated contact while adding
                if (associatedContacts != null) {
                    tv.setText("Associated contacts (" + associatedContacts.size() + ")");
                    //Build the contact object accessor to save all contacts information and be retrieving in others activity
                    String serializedFile = serialize(associatedContacts);
                    ContactDAO.serializedFile = serializedFile;
                    intentContact = new Intent(getApplicationContext(), ContactViewActivity.class);
                    intentContact.putExtra(ContactDAO.serializedFile, serializedFile);
                } else {
                    associatedContacts = new ArrayList<>();
                    String serializedFile = serialize(associatedContacts);
                    ContactDAO.serializedFile = serializedFile;
                    intentContact = new Intent(getApplicationContext(), ContactViewActivity.class);
                    intentContact.putExtra(ContactDAO.serializedFile, serializedFile);
                }

            }
            cursor.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //serialize method
    public String serialize(ArrayList<ContactDataModel> contactList) {
        Gson gson = new Gson();
        return gson.toJson(contactList);
    }

    //deserialize method. Return the list of contact
    public ArrayList<ContactDataModel> deserialize(String serialize) {
        Gson gson = new Gson();
        return gson.fromJson(serialize, new TypeToken<ArrayList<ContactDataModel>>(){}.getType());
    }

    public void displayContactList(View v) {
        startActivityForResult(intentContact, 10);
    }
}
