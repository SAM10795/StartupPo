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
public class SUPAdapter extends ArrayAdapter {

    private Context mcontext;
    private ArrayList<SUP> suplist;

    public SUPAdapter(Context context, ArrayList<SUP> sups)
    {
        super(context,R.layout.sup,sups);
        mcontext = context;
        suplist = sups;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sup,parent,false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.supname);
            holder.type = (TextView)convertView.findViewById(R.id.suptyp);
            holder.level = (TextView)convertView.findViewById(R.id.suplv);
            holder.pokemon = (ImageView)convertView.findViewById(R.id.supview);
            holder.pklayout = (LinearLayout)convertView.findViewById(R.id.suplayout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        SUP sup = (SUP)getItem(position);
        switch (sup.getBag())
        {
            case 0:
            {
                holder.pklayout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.name.setTextColor(ContextCompat.getColor(mcontext,R.color.red));
                holder.type.setTextColor(ContextCompat.getColor(mcontext,R.color.red));
                holder.level.setTextColor(ContextCompat.getColor(mcontext,R.color.red));
                break;
            }
            case 1:
            {
                holder.pklayout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.blue));
                holder.name.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.type.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.level.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                break;
            }
            case 2:
            {
                holder.pklayout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.yellow));
                holder.name.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.type.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.level.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                break;
            }
            case 3:
            {
                holder.pklayout.setBackgroundColor(ContextCompat.getColor(mcontext,R.color.purple));
                holder.name.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.type.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                holder.level.setTextColor(ContextCompat.getColor(mcontext,R.color.white));
                break;
            }
        }
        int icon = mcontext.getResources().getIdentifier(new Utilities().formatname(sup.getName()),"drawable",mcontext.getPackageName());
        holder.pokemon.setImageResource(icon);
        String text = "Level : "+sup.getLv();
        holder.name.setText(sup.getName());
        holder.level.setText(text);
        holder.type.setText(sup.getType());

        return convertView;
    }

    static class ViewHolder
    {
        LinearLayout pklayout;
        TextView name;
        TextView level;
        TextView type;
        ImageView pokemon;
    }
}
