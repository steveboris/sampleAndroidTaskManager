package maus.mausprojekt.room.DAO;

import android.arch.persistence.room.*;
import maus.mausprojekt.model.Todo;

import java.util.List;

@Dao
public interface TodoDAO {

    @Query("SELECT * FROM todos WHERE id=:todoId")
    Todo todoById(long todoId);

    @Query("SELECT * FROM todos")
    List<Todo> getAllTodos();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTodo(Todo todo);

    @Update
    void updateTodo(Todo todo);

    @Delete
    void deleteTodo(Todo todo);

    @Query("DELETE FROM todos")
    void deleteAllTodos();
}
