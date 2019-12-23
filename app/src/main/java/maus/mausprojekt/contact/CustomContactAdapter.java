package maus.mausprojekt.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import maus.mausprojekt.R;

import java.util.ArrayList;

public class CustomContactAdapter extends BaseAdapter {

    public CustomContactAdapter(Context context , int layout , ArrayList<ContactDataModel> contact) {
        this.context = context;
        this.layout = layout;
        this.contact = contact;
    }


    @Override
    public int getCount() {
        return contact.size();
    }

    @Override
    public Object getItem(int position) {
        return contact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if(row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) row.findViewById(R.id.contact_name);
            holder.phone = (TextView) row.findViewById(R.id.phone_nb);
            holder.image = (ImageView) row.findViewById(R.id.profil);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }

        final ContactDataModel cdm = contact.get(position);
        holder.name.setText(cdm.getName());
        holder.phone.setText("Phone: " + cdm.getPhone());
        holder.image.setImageResource(R.mipmap.user_profil);

        return row;
    }


    private class ViewHolder {
        TextView name , phone;
        ImageView image;
    }

    private Context context;
    private int layout;
    ArrayList<ContactDataModel> contact;
}
