package maus.mausprojekt.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import maus.mausprojekt.R;
import maus.mausprojekt.model.Todo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListArrayAdapter extends ArrayAdapter<Todo> {


    public  ListArrayAdapter(final Activity aContext, final List<Todo> aItems){
           super(aContext,0,aItems);
       }


          @Override
          public View getView(int position, View convertView, ViewGroup parent) {
              // Get the data item for this position
              Todo todo = getItem(position);
              int lightgrey = Color.parseColor("#FAFAFA");

              // Check if an existing view is being reused, otherwise inflate the view
              if (convertView == null) {
                  convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_todo_item_layout, parent, false);
              }
              // Lookup view for data population
              TextView todoName = (TextView) convertView.findViewById(R.id.txt_name);
              TextView todoDate = (TextView) convertView.findViewById(R.id.txt_date);
              ImageView importancy = convertView.findViewById(R.id.icon);

              //Date from milisecond
              SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
              Date inputDate = new Date(todo.getDate());


              if (todo.getIsImportant() == 1) {
                  importancy.setVisibility(View.VISIBLE);
              } else {
                  importancy.setVisibility(View.INVISIBLE);
              }
              if (todo.getIsDone() == 1) {
                  todoName.setPaintFlags(todoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                  todoDate.setPaintFlags(todoDate.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
              } else {
                  todoName.setPaintFlags(todoName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                  todoDate.setPaintFlags(todoDate.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
              }

              long timeMilli = new Date().getTime();
              if (todo.getDate() < timeMilli && todo.getIsDone() == 0) {
                  todoDate.setBackgroundColor(Color.RED);
              } else {
                  todoDate.setBackgroundColor(lightgrey);
              }

              // Populate the data into the template view using the data object
              todoName.setText(todo.getName());
              todoDate.setText(sdf.format(inputDate));
              // Return the completed view to render on screen
              return convertView;
          }
}
