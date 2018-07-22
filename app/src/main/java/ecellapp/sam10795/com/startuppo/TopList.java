package ecellapp.sam10795.com.startuppo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class TopList extends AppCompatActivity {

    ArrayList<User> users;
    ArrayList<Hostel> towers;

    ListView playerlist,hostelist;

    DatabaseReference towerbase;
    DatabaseReference userbase;

    PlayerAdapter playerAdapter;
    HostelAdapter hostelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_list);

        users = new ArrayList<>();
        towers = new ArrayList<>();

        towerbase = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Towers));
        userbase = FirebaseDatabase.getInstance().getReference(getResources().getString(R.string.Users));

        getusers();

        playerAdapter = new PlayerAdapter(this,users);
        hostelAdapter = new HostelAdapter(this,towers);

        playerlist = (ListView)findViewById(R.id.playerlist);
        hostelist = (ListView)findViewById(R.id.hostellist);


        final ToggleButton hostel,player;
        hostel = (ToggleButton)findViewById(R.id.HostelButton);
        player = (ToggleButton)findViewById(R.id.PlayerButton);

        player.setChecked(true);
        playerlist.setVisibility(View.VISIBLE);
        hostelist.setVisibility(View.GONE);


        player.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                playerlist.setVisibility(isChecked?View.VISIBLE:View.GONE);
                hostel.setChecked(!isChecked);
            }
        });
        hostel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hostelist.setVisibility(isChecked?View.VISIBLE:View.GONE);
                player.setChecked(!isChecked);
            }
        });

        playerlist.setAdapter(playerAdapter);
        hostelist.setAdapter(hostelAdapter);

    }

    private void getusers()
    {
        userbase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
                String hostel = user.getHostel();
                int score = user.getScore();
                inserthostel(hostel,score);
                Log.i("Users","Gets2");
                sortplayers();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                int index = getuserindex(user);
                if(index!=-1) {
                    users.remove(index);
                    users.add(user);
                    sortplayers();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                int index = getuserindex(user);
                if(index!=-1) {
                    users.remove(index);
                    sortplayers();
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sortplayers()
    {
        for(int i=0;i<users.size()-1;i++)
        {
            for(int j=0;j<users.size()-1;j++)
            {
                if(users.get(j).getScore()<users.get(j+1).getScore())
                {
                    Collections.swap(users,j,j+1);
                }
            }

        }
        playerAdapter.notifyDataSetInvalidated();
    }

    private void sorthostels()
    {

        for(int i=0;i<towers.size()-1;i++)
        {
            for(int j=0;j<towers.size()-1;j++)
            {
                if(towers.get(j).getCount()<towers.get(j+1).getCount())
                {
                    Collections.swap(towers,j,j+1);
                }
            }

        }
        hostelAdapter.notifyDataSetInvalidated();
    }

    private void inserthostel(String hostel, int score)
    {
        for(int i=0;i<towers.size();i++)
        {
            Log.i("Tower at i",towers.get(i).getName());
            Log.i("Tower hostel",hostel);
            if(towers.get(i).getName().equalsIgnoreCase(hostel))
            {
                Log.i("Tower","Match");
                Hostel hostel1 = new Hostel();
                hostel1 = towers.remove(i);
                hostel1.setCount(hostel1.getCount()+score);
                towers.add(hostel1);
                sorthostels();
                return;
            }
        }
        Log.i("Tower new",hostel);
        Hostel hostel2 = new Hostel();
        hostel2.setName(hostel);
        hostel2.setCount(score);
        towers.add(hostel2);
        sorthostels();
    }

    private int getuserindex(User user)
    {
        String roll = user.getRollno();
        for(User user1:users)
        {
            if(user1.getRollno().equalsIgnoreCase(roll))
            {
                return users.indexOf(user1);
            }
        }
        return -1;
    }
}
