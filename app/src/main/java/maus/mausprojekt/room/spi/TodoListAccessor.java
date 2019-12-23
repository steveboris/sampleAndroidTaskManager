package maus.mausprojekt.room.spi;

import android.widget.ArrayAdapter;
import maus.mausprojekt.model.Todo;

import java.util.List;

/**
 *
 * Interface for dealing with a list of data items, used by ItemListActivity
 *
 * Base on work of:
 * @author Joern Kreutel
 *
 */
public interface TodoListAccessor {
    /**
     * add an item to the list
     *
     * @param todo
     */
    public void insertTodo(Todo todo);

    /**
     * get an adapter for the list
     * @return
     */
    public ArrayAdapter<Todo> getAdapter();

    /**
     * update an existing item
     *
     * @param todo
     */
    public void updateTodo(Todo todo);

    /**
     * delete an item
     *
     * @param todo
     */
    public void deleteTodo(Todo todo);

    /**
     * determine the item selected by the user given either the position in the
     * list or the item id
     *
     * @param itemId
     * @return
     */
    public Todo todoById(long itemId);

    public List<Todo> getAllTodos();

    public void deleteAllTodos();
}
