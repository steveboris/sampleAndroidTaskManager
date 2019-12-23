package maus.mausprojekt.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;


/**
 * A single data item
 *
 * Base on example from:
 * @author Joern Kreutel
 * @author Martin Schafföner
 */
@Entity(tableName = "todos")
public class Todo implements Serializable {

    /**
     * some static id assignment
     */
    private static int ID = 0;

    /**
     *
     */
    private static final long serialVersionUID = -7481912314472891511L;
    /**
     * the fields
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    @ColumnInfo(name="name")
    private String name;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "isImportant")
    private int isImportant;
    @ColumnInfo(name = "date")
    private long date;
    @ColumnInfo(name = "isDone")
    private int isDone;
    @ColumnInfo(name = "contacts")
    private String contact;


    public void updateFrom(Todo todo){
        this.setId(todo.getID());
        this.setName(todo.getName());
        this.setDescription(todo.getDescription());
        this.setIsImportant(todo.getIsImportant());
        this.setDate(todo.getDate());
        this.setIsDone(todo.getIsDone());
        this.setContact(todo.getContact());
    }

    /**
     * Constructors
     * @param id
     * @param name
     * @param description
     * @param isImportant
     * @param date
     * @param isDone
     */
    public Todo(long id, String name, String description, int isImportant, long date, int isDone, String contact) {
        this.setId(id == -1 ? ID++ : id);
        this.setName(name);
        this.setDescription(description);
        this.setIsImportant(isImportant);
        this.setDate(date);
        this.setIsDone(isDone);
        this.setContact(contact);
    }

    public Todo() {
    }
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public static int getID() {
        return ID;
    }

    public static void setID(int ID) {
        Todo.ID = ID;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIsImportant() {
        return isImportant;
    }

    public void setIsImportant(int isImportant) {
        this.isImportant = isImportant;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
}
