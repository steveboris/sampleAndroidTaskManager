package maus.mausprojekt.adapter;

import android.widget.ArrayAdapter;
import maus.mausprojekt.model.Todo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SortListArray {


    public static void sortByDone(List<Todo> todoList, ArrayAdapter<Todo> adapter, int choosedSort) {
        if(choosedSort == 0){
            todoList.sort((a, b) -> {
                int cmp = Integer.compare(a.getIsDone(), b.getIsDone());
                return cmp;
            });
            adapter.notifyDataSetChanged();
        }
        else if(choosedSort == 1){
            todoList.sort((a, b) -> {
                int cmp = Integer.compare(a.getIsDone(), b.getIsDone());
                if (cmp == 1) {
                    return 1;
                } else if (cmp == -1) {
                    return -1;
                } else {
                    cmp = Integer.compare(b.getIsImportant(), a.getIsImportant());
                    if (cmp == 0) {
                        cmp = Long.compare(a.getDate(), b.getDate());
                        if (cmp == 0) {
                            return 0;
                        }
                        return cmp;
                    }
                    return cmp;
                }
            });
        }
        else{
            todoList.sort((a, b) -> {
                int cmp = Integer.compare(a.getIsDone(), b.getIsDone());
                if (cmp == 1) {
                    return 1;
                } else if (cmp == -1) {
                    return -1;
                } else {
                    cmp = Long.compare(a.getDate(), b.getDate());
                    if (cmp == 0) {
                        cmp = Integer.compare(a.getIsImportant(), b.getIsImportant());
                        if (cmp == 0) {
                            return 1;
                        }
                        return cmp;
                    }
                    return cmp;
                }
            });
        }
    }
}
