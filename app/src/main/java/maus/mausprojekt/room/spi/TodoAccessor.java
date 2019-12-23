package maus.mausprojekt.room.spi;

import android.arch.persistence.room.*;
import maus.mausprojekt.model.Todo;

import java.util.List;

public interface TodoAccessor {

    Todo todoById(long todoId);
    List<Todo> getAllTodos();
    void insertTodo(Todo todo);
    void updateTodo(Todo todo);
    void deleteTodo(Todo todo);
    void deleteAllTodos();
}
