package ecellapp.sam10795.com.startuppo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ecellapp.sam10795.com.startuppo.R;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by SAM10795 on 17-09-2015.
 */
public class HostelAdapter extends ArrayAdapter {

    private Context mcontext;
    private ArrayList<Hostel> towers;

    public HostelAdapter(Context context, ArrayList<Hostel> sups)
    {
        super(context,R.layout.layout_hostel,sups);
        mcontext = context;
        towers = sups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_hostel,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.hostel_name);
            holder.caps = (TextView)convertView.findViewById(R.id.captures);
            holder.position = (TextView)convertView.findViewById(R.id.posn);
            holder.layout = (LinearLayout)convertView.findViewById(R.id.hlayout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        Hostel hostel = (Hostel)getItem(position);
        holder.name.setText(hostel.getName());
        holder.caps.setText(Integer.toString(hostel.getCount()));
        holder.position.setText((position+1)+"");
        int id = position%2==0?R.color.blue:R.color.blue2;
        holder.layout.setBackgroundColor(ContextCompat.getColor(mcontext,id));
        return convertView;
    }

    static class ViewHolder
    {
        TextView position;
        TextView name;
        TextView caps;
        LinearLayout layout;
    }
}
