package docencia.tic.unam.mx.testlist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ControlPanel extends AppCompatActivity {
    // Unique UUID for this application
    private static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // Comunicación con bluetooth
    private BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothSocket  btSocket;

    // Control asíncrono
    private ConnectAsyncTask connectAsyncTask;

    private ImageView ivLed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        // UI
        Button btnPrender     = (Button)findViewById(R.id.btnPrender);
        Button btnApagar      = (Button)findViewById(R.id.btnApagar);
        Button btnDesconectar = (Button)findViewById(R.id.btnDesc);

        ivLed = (ImageView)findViewById(R.id.ivLed);

        // Configuración de los botones
        btnPrender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prenderLed();
            }
        });

        btnApagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apagarLed();
            }
        });

        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desconertarBT();
            }
        });

        // Configuración de la conexión Bletooth
        String          address = getIntent().getStringExtra("address");
        BluetoothDevice device  = btAdapter.getRemoteDevice( address );

        connectAsyncTask = new ConnectAsyncTask();
        connectAsyncTask.execute( device );
    }

    private void prenderLed() {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write("1".toString().getBytes());
                InputStream is   = btSocket.getInputStream();
                char        data = (char)is.read( );
                if( data == '1' ) {
                    ivLed.setImageResource(R.mipmap.on);
                } else if( data == '0' ) {
                    ivLed.setImageResource(R.mipmap.off);
                }
            } catch (IOException e) {
                msj("Error de comunicación con Bluetooth." + e.getMessage());
            }
        }
    }

    private void apagarLed() {
        if (this.btSocket != null) {
            try {
                btSocket.getOutputStream().write("0".toString().getBytes());
                InputStream is   = btSocket.getInputStream();
                char        data = (char)is.read( );
                if( data == '1' ) {
                    ivLed.setImageResource(R.mipmap.on);
                } else if( data == '0' ) {
                    ivLed.setImageResource(R.mipmap.off);
                }
            } catch (IOException e) {
                msj("Error de comunicación con Bluetooth." + e.getMessage());
            }
        }
    }

    private void desconertarBT() {
        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                msj("Error de comunicación con Bluetooth." + e.getMessage());
            }
        }
        finish();
    }

    private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket> {
        private BluetoothSocket mmSocket;
        private BluetoothDevice mmDevice;

        @Override
        protected BluetoothSocket doInBackground(BluetoothDevice... device) {
            mmDevice = device[0];

            try {
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(APP_UUID);
                mmSocket.connect();

            } catch (Exception e) {
                ControlPanel.this.msj("ERR: " + e.getMessage());
            }

            return mmSocket;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        @Override
        protected void onPostExecute(BluetoothSocket result) {
            btSocket = result;
            try {
                btSocket.getOutputStream().write("2".toString().getBytes());
                InputStream is = btSocket.getInputStream();

                char data = (char)is.read( );
                if( data == '1' ) {
                    ivLed.setImageResource(R.mipmap.on);
                }
            } catch (IOException e) {
                msj("Error de comunicación con Bluetooth." + e.getMessage());
            }
        }

        public void setProgressPercent(Integer progressPercent) {
            // Para utilizar el progreso
        }
    }

    public void msj(String mensaje) {
        try {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("ERROR:", e.getMessage());
        }
    }
}