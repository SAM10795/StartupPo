package ecellapp.sam10795.com.startuppo;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Activity {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    boolean read = false,write = false;
    SUP sent,received;

    MediaPlayer mediaPlayer;

    // Layout Views
    private GridView PKMNView;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private SUPAdapter supadapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothChatService mChatService = null;

    private SUPDataSource supDataSource;

    private ArrayList<SUP> sups = new ArrayList<>();

    Button button;

    Utilities utilities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypkmn);
        // Get local Bluetooth adapter

        PKMNView = (GridView)findViewById(R.id.gridView);

        supDataSource = new SUPDataSource(this);
        utilities = new Utilities();

        AlertDialog.Builder bluealertbuild = new AlertDialog.Builder(BluetoothChatFragment.this);
        bluealertbuild.setTitle("Warning!")
                .setCancelable(true)
                .setMessage("This feature is still in Beta.\n Trade at your own risk.")
                .create();
        AlertDialog alert = bluealertbuild.create();
        alert.show();


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            this.finish();
        }

        ensureDiscoverable();

        button = (Button)findViewById(R.id.scan);
        button.setVisibility(View.VISIBLE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BluetoothChatFragment.this,DeviceListActivity.class);
                startActivityForResult(intent,REQUEST_CONNECT_DEVICE_SECURE);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        mediaPlayer = MediaPlayer.create(this,R.raw.other);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

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
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer.start();
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

        supDataSource.close();

        supadapter = new SUPAdapter(this,sups);

        PKMNView.setAdapter(supadapter);

        PKMNView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!write) {
                    final SUP pkmn = (SUP) parent.getItemAtPosition(position);
                    new AlertDialog.Builder(BluetoothChatFragment.this)
                            .setTitle("Trade")
                            .setMessage("Trade " + pkmn.getName())
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    sendMessage(pkmn);
                                    sent = pkmn;
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

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param sup A SUP to send.
     */
    private void sendMessage(SUP sup) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (sup != null) {
            // Get the message bytes and tell the BluetoothChatService to write

            String message = getStringfromSUP(sup);

            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
        }
    }

    private String getStringfromSUP(SUP sup)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SUPDB.COLUMN_STARTUP_NAME,sup.getName());
            jsonObject.put(SUPDB.COLUMN_STARTUP_BAG,sup.getBag());
            jsonObject.put(SUPDB.COLUMN_STARTUP_LVL,sup.getLv());
            jsonObject.put(SUPDB.COLUMN_STARTUP_ZONE,sup.getZone());

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
            sup.setBag(jsonObject.getInt(SUPDB.COLUMN_STARTUP_BAG));
            sup.setZone(jsonObject.getString(SUPDB.COLUMN_STARTUP_ZONE));
            sup.setTraded(true);
            sup.setName(jsonObject.getString(SUPDB.COLUMN_STARTUP_NAME));
            sup.setLv(jsonObject.getInt(SUPDB.COLUMN_STARTUP_LVL));
            //sup.setLegend(jsonObject.getBoolean(SUPDB.COLUMN_STARTUP_ISLEGEND));
            List<String[]> records = utilities.getRecords(this);
            for(String[] record: records)
            {
                if(utilities.formatname(record[0]).equalsIgnoreCase(utilities.formatname(sup.getName())))
                {
                    sup.setEvaluation(record[3]);
                    sup.setFunding(record[6]);
                    sup.setInfo(record[4]);
                    sup.setFoundate(record[2]);
                    sup.setFounders(record[5]);
                    sup.setType(record[1]);
                    sup.setLegend(record[7].equalsIgnoreCase("1"));
                    break;
                }
            }

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
                    write = true;
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.i("PKMN",readMessage);
                    received = getSUPfromString(readMessage);
                    read = true;
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                        Toast.makeText(BluetoothChatFragment.this, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                        Toast.makeText(BluetoothChatFragment.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    break;
            }
            if(read&&write)
            {
                supDataSource.open();
                supDataSource.releaseSUP(sent);
                supDataSource.createSUP(received);
                sups.remove(sent);
                sups.add(received);
                supadapter.notifyDataSetChanged();
                supDataSource.close();
                Toast.makeText(BluetoothChatFragment.this,"Trade completed",Toast.LENGTH_SHORT).show();
                read = false;
                write = false;
                sent = null;
                received = null;
                new Utilities().updatescore(BluetoothChatFragment.this);
                Intent intent = new Intent(BluetoothChatFragment.this,mypkmn.class);
                startActivity(intent);
                finish();
            }
        }
    };

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



  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }*/

}
