package maus.mausprojekt.accessors;

import android.widget.ArrayAdapter;
import maus.mausprojekt.model.Todo;
import maus.mausprojekt.room.spi.TodoListAccessor;
//import org.jboss.resteasy.client.ProxyFactory;
//import org.jboss.resteasy.client.core.executors.ApacheHttpClient4Executor;

import java.util.List;

public class ResteasyTodoCRUDAAccessor implements TodoListAccessor {

    private TodoListAccessor restClient;

    /*public ResteasyTodoCRUDAAccessor(String baseUrl) {


        // create a client for the server-side implementation of the interface
        this.restClient = ProxyFactory.create(TodoListAccessor.class,
                baseUrl,
                new ApacheHttpClient4Executor());

    }*/
    @Override
    public void insertTodo(Todo todo) {

    }

    @Override
    public ArrayAdapter<Todo> getAdapter() {
        return null;
    }

    @Override
    public void updateTodo(Todo todo) {

    }

    @Override
    public void deleteTodo(Todo todo) {

    }

    @Override
    public Todo todoById(long itemId) {
        return null;
    }

    @Override
    public List<Todo> getAllTodos() {
        return null;
    }

    @Override
    public void deleteAllTodos() {

    }
}
