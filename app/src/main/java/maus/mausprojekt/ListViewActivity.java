package maus.mausprojekt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import maus.mausprojekt.adapter.ListArrayAdapter;
import maus.mausprojekt.adapter.SortListArray;
import maus.mausprojekt.contact.ContactDAO;
import maus.mausprojekt.contact.ContactDataModel;
import maus.mausprojekt.model.Todo;
import maus.mausprojekt.room.RoomTodoListAccessor;
import maus.mausprojekt.room.spi.TodoListAccessor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {
    /**
     * the field for the listview
     */
    private ListView listview;
    private Todo choosedTodo;
    private TodoListAccessor accessor;
    private List<Todo> todoList = new ArrayList<Todo>();
    private ArrayAdapter<Todo> adapter;
    private int sortType = 0;

   // ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listview = findViewById(R.id.list);

        accessor = new RoomTodoListAccessor();
        ((RoomTodoListAccessor) accessor).setActivity(this);
        adapter = accessor.getAdapter();
        adapter = new ListArrayAdapter(this, todoList);
        registerForContextMenu(listview);
        todoList.addAll(accessor.getAllTodos());
        listview.setAdapter(adapter);
        SortListArray.sortByDone(todoList, adapter, sortType);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // Action for a click on item
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Todo todo = todoList.get(position);
                    String serializedContact = todo.getContact();
                    Gson gson = new Gson();
                    ArrayList<ContactDataModel> associatedContacts = gson.fromJson(serializedContact, new TypeToken<ArrayList<ContactDataModel>>(){}.getType());
                    ArrayList<String> contactslist = new ArrayList<>();
                    if (associatedContacts != null)  {
                        for (ContactDataModel contact : associatedContacts) {
                            contactslist.add(contact.getName());
                        }
                    }
                    Date date = new Date(todo.getDate());
                    AlertDialog dialog = new AlertDialog.Builder(ListViewActivity.this)
                            .setTitle(todo.getName())
                            .setMessage("Date: " + date.toString() + "\nDescription: " + todo.getDescription() + "\nImportant: " + (todo.getIsImportant() == 1 ? "True" : "False") +
                                    "\nIs Done: " + (todo.getIsDone() == 1 ? "True" : "False") + "\nAssociated contacts: \n" + contactslist.toString())
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
            }
        });

        // Grant permission for access the contact list
        final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_date:
                this.sortType = 2;
                SortListArray.sortByDone(todoList, adapter, sortType);
                adapter.notifyDataSetChanged();
                return true;
            case R.id.sort_by_priority:
                this.sortType = 1;
                SortListArray.sortByDone(todoList, adapter, sortType);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        //content von dem contact String löschen
        ContactDAO.serializedFile = null;
        //add buttom clicked
        Intent intent = new Intent(this, AddTodoActivity.class);
        //start the edit task new activity
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle("Select action:");
        menu.add(Menu.NONE, 0, Menu.NONE, "VIEW/EDIT");
        menu.add(Menu.NONE, 3, Menu.NONE, "IMPORTANT");
        menu.add(Menu.NONE, 4, Menu.NONE, "DONE");
        menu.add(Menu.NONE, 5, Menu.NONE, "DELETE");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        final Todo todo = todoList.get(info.position);
        switch (item.getItemId())
        {
            case 0: //VIew
            {
                Intent intent = new Intent(ListViewActivity.this, AddTodoActivity.class);
                intent.putExtra("TODO", todo);
                startActivity(intent);
            }
            break;
            case 5: //delete
            {
                new AlertDialog.Builder(ListViewActivity.this)
                        .setMessage("Do you want to delete this TODO:\n "+ todo.getName() + " ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accessor.deleteTodo(todo);
                                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                                int position = info.position;
                                adapter.remove(adapter.getItem(position));
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                SortListArray.sortByDone(todoList, adapter, sortType);
                adapter.notifyDataSetChanged();
            }
            SortListArray.sortByDone(todoList, adapter, sortType);
            adapter.notifyDataSetChanged();
            break;
            case 3: //IMPORTANT
            {
                if(todo.getIsImportant() == 1){
                    todo.setIsImportant(0);
                }
                else todo.setIsImportant(1);
                accessor.updateTodo(todo);
            }
            SortListArray.sortByDone(todoList, adapter, sortType);
            adapter.notifyDataSetChanged();
            break;
            case 4: //DONE
            {
                if(todo.getIsDone() == 1){
                    todo.setIsDone(0);
                }
                else todo.setIsDone(1);
                accessor.updateTodo(todo);
            }
            SortListArray.sortByDone(todoList, adapter, sortType);
            adapter.notifyDataSetChanged();
            break;
        }
        return true;
    }
}
