package docencia.tic.unam.mx.testlist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public  static final int  REQUEST_ENABLE_BT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            //Toast.makeText(getApplicationContext(), "No hay capacidad de Bluetooth.", Toast.LENGTH_SHORT);
            msj("No hay capacidad de Bluetooth.");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                this.setupBluetooth( );
            }
        }
    }

    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if( requestCode == REQUEST_ENABLE_BT && resultCode ==  RESULT_OK ) {
            this.setupBluetooth( );
        }
    }

    private void setupBluetooth( ) {
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()) {
            // Listar los dispositivos emparejados
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            // If there are paired devices
            if (pairedDevices.size() > 0) {
                BluetoothDevice[] devices = new BluetoothDevice[pairedDevices.size()];
                // Loop through paired devices
                int i = 0;
                for (BluetoothDevice device : pairedDevices) {
                    devices[ i++ ] = device;
                }
                final ListView listview = (ListView) findViewById(R.id.listview);
                listview.setAdapter( new MyBluetoothAdapter(getApplicationContext(), devices ) );
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BluetoothDevice device = (BluetoothDevice) parent.getItemAtPosition( position );
                        BluetoothDevice actual = mBluetoothAdapter.getRemoteDevice( device.getAddress() );

                        mBluetoothAdapter.cancelDiscovery();

                        Intent i = new Intent(MainActivity.this, ControlPanel.class);
                        i.putExtra("address", actual.getAddress());
                        startActivity( i );
                    }
                });
            }

        }
    }

    public void msj(String mensaje) {
        try {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        } catch (Exception e ) {
            Log.e("ERROR:", e.getMessage());
        }
    }
}
