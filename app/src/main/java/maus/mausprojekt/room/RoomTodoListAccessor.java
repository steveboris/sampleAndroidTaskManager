package maus.mausprojekt.room;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import maus.mausprojekt.accessors.AbstractActivityDataAccessor;
import maus.mausprojekt.adapter.ListArrayAdapter;
import maus.mausprojekt.adapter.SortListArray;
import maus.mausprojekt.model.Todo;
import maus.mausprojekt.room.DAO.TodoDAO;
import maus.mausprojekt.room.spi.TodoListAccessor;

import java.util.ArrayList;
import java.util.List;

public class RoomTodoListAccessor extends AbstractActivityDataAccessor implements TodoListAccessor {

    private TodoDAO todoDAO;
    private ArrayAdapter<Todo> mAdapter;
    private AppDatabase mAppDB;

    @Override
    public void setActivity(AppCompatActivity activity) {
        super.setActivity(activity);
        init();
    }

    private void init() {
        mAppDB = AppDatabase.getInstance(getActivity());
        todoDAO = mAppDB.todoDAO();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public ArrayAdapter<Todo> getAdapter() {
        final List<Todo> todo = new ArrayList<>();
        mAdapter = new ListArrayAdapter(getActivity(),todo);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                mAdapter.addAll(todoDAO.getAllTodos());
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
        return mAdapter;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void insertTodo(final Todo todo) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                todoDAO.insertTodo(todo);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                //Crash whern try to add new Todo
               // mAdapter.add(todo);
               // mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void updateTodo(final Todo todo) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                todoDAO.updateTodo(todo);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void deleteTodo(final Todo todo) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                todoDAO.deleteTodo(todo);
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                if(mAdapter != null) {
                    mAdapter.remove(todo);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }.execute();
    }

    @Override
    public Todo todoById(final long itemId) {
      Todo todo = null;
      todo = todoDAO.todoById(itemId);
        return todo;
    }

    @Override
    public List<Todo> getAllTodos() {
        List<Todo> todoList = todoDAO.getAllTodos();
        return todoList;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void deleteAllTodos() {
        new AsyncTask<Void, Void, Void>()  {

            @Override
            protected Void doInBackground(Void... voids) {
                todoDAO.deleteAllTodos();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.clear();
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
