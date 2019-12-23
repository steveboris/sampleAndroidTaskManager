package maus.mausprojekt.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import maus.mausprojekt.model.Todo;
import maus.mausprojekt.room.DAO.TodoDAO;

@Database(entities = {Todo.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME="maus-database";
    private static volatile AppDatabase sInstance;
    public abstract TodoDAO todoDAO();

    public static AppDatabase getInstance(final Context aContext) {
        if ( sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(aContext.getApplicationContext(), AppDatabase.class,DATABASE_NAME )
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return sInstance;
    }
}
