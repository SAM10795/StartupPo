package ecellapp.sam10795.com.startuppo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import pl.droidsonroids.gif.GifDrawable;


/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BT_battle extends AppCompatActivity {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    boolean read = false,write = false,ingame=false;

    ViewFlipper viewFlipper;

    private static double[] Nor = {1,1,1,1,1,1,1,1,1,1,1,1,0.5,0,1,1,0.5,1};
    private static double[] Fir = {1,0.5,0.5,1,2,2,1,1,1,1,1,2,0.5,1,0.5,1,2,1};
    private static double[] Wat = {1,2,0.5,1,0.5,1,1,1,2,1,1,1,2,1,0.5,1,1,1};
    private static double[] Ele = {1,1,2,0.5,0.5,1,1,1,0,2,1,1,1,1,0.5,1,1,1};
    private static double[] Gra = {1,0.5,2,1,0.5,1,1,0.5,2,0.5,1,0.5,2,1,0.5,1,0.5,1};
    private static double[] Ice = {1,0.5,0.5,1,2,0.5,1,1,2,2,1,1,1,1,2,1,0.5,1};
    private static double[] Fig = {2,1,1,1,1,2,1,0.5,1,0.5,0.5,0.5,2,0,1,2,2,0.5};
    private static double[] Poi = {1,1,1,1,2,1,1,0.5,0.5,1,1,1,0.5,0.5,1,1,0,2};
    private static double[] Gro = {1,2,1,2,0.5,1,1,2,1,0,1,0.5,2,1,1,1,2,1};
    private static double[] Fly = {1,1,1,0.5,2,1,2,1,1,1,1,2,0.5,1,1,1,0.5,1};
    private static double[] Psy = {1,1,1,1,1,1,2,2,1,1,0.5,1,1,1,1,0,0.5,1};
    private static double[] Bug = {1,0.5,1,1,2,1,0.5,0.5,1,0.5,2,1,1,0.5,1,2,0.5,0.5};
    private static double[] Roc = {1,2,1,1,1,2,0.5,1,0.5,2,1,2,1,1,1,1,0.5,1};
    private static double[] Gho = {0,1,1,1,1,1,1,1,1,1,2,1,1,2,1,0.5,1,1};
    private static double[] Dra = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,1,0.5,0};
    private static double[] Dar = {1,1,1,1,1,1,0.5,1,1,1,2,1,1,2,1,0.5,1,0.5};
    private static double[] Ste = {1,0.5,0.5,0.5,1,2,1,1,1,1,1,1,2,1,1,1,0.5,2};
    private static double[] Fai = {1,0.5,1,1,1,1,2,0.5,1,1,1,1,1,1,2,2,0.5,1};

    private static double[][] Tchart = {Nor,Fir,Wat,Ele,Gra,Ice,Fig,Poi,Gro,Fly,Psy,Bug,Roc,Gho,Dra,Dar,Ste,Fai};



    // Layout Views
    private GridView PKMNView;

    private TextView score,oppscore;

    ImageView pkmn;

    private String mConnectedDeviceName = null;

    private StringBuffer mOutStringBuffer;

    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothChatService mChatService = null;

    private SUPDataSource supDataSource;

    private int[] myscores;
    private int[] opscores;

    private SUP[] sentpkmn;
    private SUP[] recpkmn;

    int recpk=0,senpk=0;

    private double[][] tapdamages;

    ImageView[] recpks;
    ImageView[] sentpks;
    ImageView[] sendpks;

    boolean[][] faceoff;

    boolean[][] wins;

    int[] recid = {R.id.pkrec1,R.id.pkrec2,R.id.pkrec3,R.id.pkrec4,R.id.pkrec5,R.id.pkrec6};
    int[] sentid = {R.id.pksent1,R.id.pksent2,R.id.pksent3,R.id.pksent4,R.id.pksent5,R.id.pksent6};
    int[] sendid = {R.id.pksend1,R.id.pksend2,R.id.pksend3,R.id.pksend4,R.id.pksend5,R.id.pksend6};

    private ArrayList<SUP> sentlist = new ArrayList<>();
    private ArrayList<SUP> sups = new ArrayList<>();

    boolean lvorder = true;

    SUPAdapter supadapter;

    Button button;

    int h,w;

    Random random;
    ViewGroup.MarginLayoutParams layoutParams;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button start;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bt_battle);
        // Get local Bluetooth adapter

        PKMNView = (GridView)findViewById(R.id.gridView);

        score = (TextView)findViewById(R.id.myscore);
        oppscore = (TextView)findViewById(R.id.oppscore);

        supDataSource = new SUPDataSource(this);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        pkmn = (ImageView)findViewById(R.id.pkview);

        random = new Random();
        random.setSeed(4791);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        h = (displayMetrics.heightPixels);
        w = (displayMetrics.widthPixels);

        int left = random.nextInt(w-75);
        int top = random.nextInt(h-75);

        top = top>600?600:top;
        left = left>590?590:left;

        layoutParams = (ViewGroup.MarginLayoutParams)pkmn.getLayoutParams();
        layoutParams.setMargins(left,top,0,0);
        pkmn.setLayoutParams(layoutParams);

        pkmn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opscores[recpk]-=tapdamages[senpk][recpk];
                opscores[recpk] = opscores[recpk]<0?0:opscores[recpk];
                sendScore(1000*senpk+opscores[recpk]);
                int left = random.nextInt(w-75);
                int top = random.nextInt(h-75);
                top = top>600?600:top;
                left = left>590?590:left;
                layoutParams = (ViewGroup.MarginLayoutParams)pkmn.getLayoutParams();
                layoutParams.setMargins(left,top+30,0,0);
                pkmn.setLayoutParams(layoutParams);

            }
        });

        ensureDiscoverable();

        button = (Button)findViewById(R.id.scan);
        button.setVisibility(View.VISIBLE);

        start = (Button)findViewById(R.id.start);
        start.setVisibility(View.GONE);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.GONE);
                sentpkmn = new SUP[sentlist.size()];
                int i=0;
                for(SUP s:sentlist)
                {
                    sentpkmn[i] = s;
                    i++;
                }
                sendPKMNs();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BT_battle.this,DeviceListActivity.class);
                startActivityForResult(intent,REQUEST_CONNECT_DEVICE_SECURE);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }


    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        supDataSource.open();

        sups = supDataSource.getAllSUP();

        supadapter = new SUPAdapter(this,sups);

        PKMNView.setAdapter(supadapter);

        sharedPreferences = getSharedPreferences("PKDT",MODE_PRIVATE);

        sendpks = new ImageView[6];

        for(int i=0;i<6;i++)
        {
            sendpks[i] = (ImageView)findViewById(sendid[i]);
            sendpks[i].setVisibility(View.GONE);
            final int x = i;
            sendpks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sups.add(sentlist.remove(x));
                    sortLv();
                    for(int j=x+1;j<=sentlist.size();j++)
                    {
                        sendpks[j-1].setImageDrawable(sendpks[j].getDrawable());
                    }
                    sendpks[sentlist.size()].setVisibility(View.GONE);
                }
            });
        }


        PKMNView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!write&&sentlist.size()<6) {
                    final SUP pkmn = (SUP) parent.getItemAtPosition(position);
                    new AlertDialog.Builder(BT_battle.this)
                            .setTitle("Select Pokemon for battle?")
                            .setMessage("Select " + pkmn.getName()+" for the battle?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(start.getVisibility()==View.GONE)
                                    {
                                        start.setVisibility(View.VISIBLE);
                                    }
                                    sentlist.add(pkmn);
                                    sendpks[sentlist.size()-1].setVisibility(View.VISIBLE);
                                    sups.remove(pkmn);
                                    supadapter.notifyDataSetChanged();
                                    try {
                                        GifDrawable gifDrawable = new GifDrawable(getResources(),
                                                getResources().getIdentifier(pkmn.getName().toLowerCase(),"drawable",getPackageName()));
                                        sendpks[sentlist.size()-1].setImageDrawable(gifDrawable);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        });

        mChatService = new BluetoothChatService(this, mHandler);

        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 900);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *.
     */
    private void sendPKMNs() {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        String message = String.valueOf(sentpkmn.length)+"%";

        for(SUP p:sentpkmn) {
            if (p != null) {
                // Get the message bytes and tell the BluetoothChatService to write
                message = message+getStringfromSUP(p)+"%";
            }
        }
            byte[] send = message.getBytes();
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
    }

    private void sendScore(int score) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

            // Get the message bytes and tell the BluetoothChatService to write

            String message = String.valueOf(score);

            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
    }

    private String getStringfromSUP(SUP sup)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SUPDB.COLUMN_STARTUP_NAME,sup.getName());
            jsonObject.put(SUPDB.COLUMN_STARTUP_ISLEGEND,sup.isLegend());
            jsonObject.put(SUPDB.COLUMN_STARTUP_LVL,sup.getLv());
            jsonObject.put(SUPDB.COLUMN_STARTUP_TYPE,sup.getType());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private SUP getSUPfromString(String string)
    {
        SUP sup = new SUP();
        try {
            JSONObject jsonObject = new JSONObject(string);
            sup.setName(jsonObject.getString(SUPDB.COLUMN_STARTUP_NAME));
            sup.setLegend(jsonObject.getBoolean(SUPDB.COLUMN_STARTUP_ISLEGEND));
            sup.setLv(jsonObject.getInt(SUPDB.COLUMN_STARTUP_LVL));
            sup.setType(jsonObject.getString(SUPDB.COLUMN_STARTUP_TYPE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sup;
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pkmn, menu);
        return true;
    }

    private void sortLv()
    {

        for(int i=1;i<sups.size();i++)
        {
            for(int j=0;j<i;j++)
            {
                if(sups.get(j).getLv()<sups.get(i).getLv())
                {
                    swapPKMN(i,j);
                }
            }
        }
        supadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_sort2)
        {
            sortLv();
            lvorder = true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void swapPKMN(int i,int j)
    {
        SUP pkmni = sups.get(i);
        SUP pkmnj = sups.get(j);
        sups.set(i,pkmnj);
        sups.set(j,pkmni);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus("Connected to"+ mConnectedDeviceName);
                            button.setVisibility(View.GONE);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            setStatus("Connecting");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus("Not connected");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    Log.i("PKMN",writeMessage);
                    if(ingame) {
                        oppscore.setText(opscores[recpk]+"/"+5*recpkmn[recpk].getLv());
                    }
                    write = !ingame;
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i("PKMN",readMessage);
                    if(!ingame) {
                        //received = getPKMNfromString(readMessage);
                        fillrec(readMessage);
                    }
                    else
                    {
                        int recscore = Integer.parseInt(readMessage);
                        recpk = recscore/1000;
                        myscores[senpk] = recscore%1000;
                        score.setText(myscores[senpk]+"/"+5*sentpkmn[senpk].getLv());
                    }
                    read = !ingame;
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(BT_battle.this, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    Toast.makeText(BT_battle.this, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            if(read&&write)
            {
                fillscores();
                filldrawables();
                gettaps();
                Toast.makeText(BT_battle.this,"Battle starting in 5 seconds",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewFlipper.showNext();
                        read = false;
                        write = false;
                        ingame = true;
                        recpk = 0;
                        senpk = 0;
                        sentpks[0].setScaleY(2);
                        sentpks[0].setScaleX(2);
                        try {
                            GifDrawable gifDrawable = new GifDrawable(getResources(),
                                    getResources().getIdentifier(recpkmn[0].getName().toLowerCase(),"drawable",getPackageName()));
                            pkmn.setImageDrawable(gifDrawable);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },5000);
            }
            if(!read&&!write&&ingame)
            {
                if(endgame()) {

                    if(opscores[recpk]<=0)
                    {
                        recpks[recpk].setVisibility(View.GONE);
                        for(int i=0;i<sentpkmn.length;i++)
                        {
                            if(faceoff[i][recpk])
                            {
                                wins[i][recpk] = true;
                            }
                        }
                    }

                    int i=0;
                    editor = sharedPreferences.edit();
                    Toast.makeText(BT_battle.this, "Game Over", Toast.LENGTH_SHORT).show();
                    if (loss()&&!win()) {
                        Toast.makeText(BT_battle.this, "You lose", Toast.LENGTH_SHORT).show();
                        editor.putInt("Loss", sharedPreferences.getInt("Loss", 0) + 1);
                    } else if (win()&&!loss()) {
                        Toast.makeText(BT_battle.this, "You win", Toast.LENGTH_SHORT).show();
                        editor.putInt("Win", sharedPreferences.getInt("Win", 0) + 1);
                    } else {
                        Toast.makeText(BT_battle.this, "It's a tie!", Toast.LENGTH_SHORT).show();
                        editor.putInt("Tie", sharedPreferences.getInt("Tie", 0) + 1);
                    }
                    for(SUP sent:sentlist) {
                        int lvup= 0;
                        for(int j=0;j<recpkmn.length;j++) {
                            if(wins[i][j])
                            {
                                lvup++;
                            }
                        }
                        if (sent.getLv()<100&&lvup>0&&lvup<=6) {
                            int flv = ((sent.getLv()+lvup)>=100?100:sent.getLv()+lvup);
                            sent.setLv(flv);
                            Toast.makeText(BT_battle.this, sent.getName() + " has levelled up", Toast.LENGTH_SHORT).show();
                            supDataSource.updateSUP(sent);
                        }
                        i++;
                    }
                    editor.apply();
                    ingame = false;
                    read = false;
                    write = false;
                    Intent intent = new Intent(BT_battle.this,Mainu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    faceoff[senpk][recpk] = true;
                    for(int j=0;j<recpkmn.length;j++)
                    {
                        if(opscores[j]<=0)
                        {
                            recpks[j].setVisibility(View.GONE);
                            for(int i=0;i<sentpkmn.length;i++)
                            {
                                if(faceoff[i][recpk])
                                {
                                    wins[i][recpk] = true;
                                }
                            }
                        }
                    }
                    if(myscores[senpk]<=0) {
                        sentpks[senpk].setVisibility(View.GONE);
                        for (int j = 0; j < sentpkmn.length; j++) {
                            if (sentpks[j].getVisibility() == View.VISIBLE) {
                                senpk = j;
                                sentpks[j].setScaleX(2);
                                sentpks[j].setScaleY(2);
                                sendScore(1000 * j + opscores[recpk]);
                                break;
                            }
                        }
                    }
                    try {
                        GifDrawable gifDrawable = new GifDrawable(getResources(),
                                getResources().getIdentifier(recpkmn[recpk].getName().toLowerCase(),"drawable",getPackageName()));
                        pkmn.setImageDrawable(gifDrawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(!ingame) {
            super.onBackPressed();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                    button.setVisibility(View.GONE);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, "BT Not enabled",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }


    private boolean endgame()
    {
        return win()||loss();
    }

    private boolean win()
    {
        boolean win = true;
        for(int i=0;i<recpkmn.length;i++)
        {
            win = win&&(opscores[i]<=0);
        }
        return win;
    }

    private boolean loss()
    {
        boolean loss = true;
        for(int i=0;i<sentpkmn.length;i++)
        {
            loss = loss&&(myscores[i]<=0);
        }
        return loss;
    }

    private double tapdamage(String stype,String rtype)
    {
        int st = gettypeint(stype);
        int rt = gettypeint(rtype);
        return (Tchart[st][rt]);
    }

    private void gettaps()
    {
        tapdamages = new double[sentpkmn.length][recpkmn.length];
        for(int i=0;i<sentpkmn.length;i++)
        {
            for(int j=0;j<recpkmn.length;j++)
            {
                tapdamages[i][j] = tapdamage(sentpkmn[i].getType(),recpkmn[j].getType())+(sentpkmn[i].isLegend()?1:0);
            }
        }
    }

    private void fillscores()
    {
        myscores = new int[sentpkmn.length];
        for(int i=0;i<sentpkmn.length;i++)
        {
            myscores[i] = 5*sentpkmn[i].getLv();
        }

        opscores = new int[recpkmn.length];
        for(int i=0;i<recpkmn.length;i++)
        {
            opscores[i] = 5*recpkmn[i].getLv();
        }

        faceoff = new boolean[sentpkmn.length][recpkmn.length];
        wins = new boolean[sentpkmn.length][recpkmn.length];
        for(int i=0;i<sentpkmn.length;i++)
        {
            for(int j=0;j<recpkmn.length;j++)
            {
                faceoff[i][j] = false;
                wins[i][j] = false;
            }
        }
    }

    private void filldrawables() {

        sentpks = new ImageView[sentpkmn.length];
        recpks = new ImageView[recpkmn.length];

        for(int i=0;i<sentpkmn.length;i++)
        {
            sentpks[i] = (ImageView)findViewById(sentid[i]);
            try {
                GifDrawable gifDrawable = new GifDrawable(getResources(),
                        getResources().getIdentifier(sentpkmn[i].getName().toLowerCase(),"drawable",getPackageName()));

                sentpks[i].setImageDrawable(gifDrawable);
                final int x = i;
                sentpks[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int k=0;k<sentpks.length;k++)
                        {
                            sentpks[k].setScaleX(1);
                            sentpks[k].setScaleY(1);
                        }
                        sentpks[x].setScaleX(2);
                        sentpks[x].setScaleY(2);
                        senpk = x;
                        sendScore(1000*x+opscores[recpk]);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<recpkmn.length;i++)
        {
            recpks[i] = (ImageView)findViewById(recid[i]);
            try {
                GifDrawable gifDrawable = new GifDrawable(getResources(),
                        getResources().getIdentifier(recpkmn[i].getName().toLowerCase(),"drawable",getPackageName()));
                recpks[i].setImageDrawable(gifDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int gettypeint(String type)
    {
        switch (type)
        {
            case "Normal":
            {
                return 0;
            }
            case "Fire":
            {
                return 1;
            }
            case "Water":
            {
                return 2;
            }
            case "Electric":
            {
                return 3;
            }
            case "Grass":
            {
                return 4;
            }
            case "Ice":
            {
                return 5;
            }
            case "Fighting":
            {
                return 6;
            }
            case "Poison":
            {
                return 7;
            }
            case "Ground":
            {
                return 8;
            }
            case "Flying":
            {
                return 9;
            }
            case "Psychic":
            {
                return 10;
            }
            case "Bug":
            {
                return 11;
            }
            case "Rock":
            {
                return 12;
            }
            case "Ghost":
            {
                return 13;
            }
            case "Dragon":
            {
                return 14;
            }
            case "Dark":
            {
                return 15;
            }
            case "Steel":
            {
                return 16;
            }
            case "Fairy":
            {
                return 17;
            }
            default:
                return 0;
        }
    }

    private void fillrec(String message)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(message,"%");
        int num = Integer.parseInt(stringTokenizer.nextToken());
        recpkmn = new SUP[num];
        for(int j=0;j<num;j++)
        {
            recpkmn[j] = getSUPfromString(stringTokenizer.nextToken());
        }
    }

}
