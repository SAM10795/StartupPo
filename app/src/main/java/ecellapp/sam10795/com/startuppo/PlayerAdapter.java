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
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by SAM10795 on 17-09-2015.
 */
public class PlayerAdapter extends ArrayAdapter {

    private Context mcontext;
    private ArrayList<User> userlist;

    public PlayerAdapter(Context context, ArrayList<User> sups)
    {
        super(context,R.layout.layout_player,sups);
        mcontext = context;
        userlist = sups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_player,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.name);
            holder.hostel = (TextView)convertView.findViewById(R.id.hostel);
            holder.rank = (TextView)convertView.findViewById(R.id.rank);
            holder.score = (TextView)convertView.findViewById(R.id.score);
            holder.layout = (LinearLayout)convertView.findViewById(R.id.playout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        User user = (User)getItem(position);
        holder.name.setText(user.getName());
        holder.hostel.setText(user.getHostel());
        holder.score.setText(user.getScore()+"");
        holder.rank.setText((position+1)+"");
        int id = position%2==0?R.color.blue:R.color.blue2;
        holder.layout.setBackgroundColor(ContextCompat.getColor(mcontext,id));
        return convertView;
    }

    static class ViewHolder
    {
        TextView name;
        TextView hostel;
        TextView score;
        TextView rank;
        LinearLayout layout;
    }

}
