package maus.mausprojekt.room.DAO;

import android.arch.persistence.room.*;
import maus.mausprojekt.model.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateTodo(User user);

    @Delete
    void deleteTodo(User user);

    @Query("DELETE FROM users")
    void deleteAllUsers();
}
