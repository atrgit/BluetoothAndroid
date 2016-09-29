package docencia.tic.unam.mx.testlist;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by macintoschhd on 07/07/16.
 */
public class MyBluetoothAdapter extends ArrayAdapter<BluetoothDevice> {
    private final Context context;
    private final BluetoothDevice[] devices;

    public MyBluetoothAdapter(Context context, BluetoothDevice[] devices) {
        super(context, -1, devices);

        this.context = context;
        this.devices = devices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View           rowView   = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView       textView  = (TextView) rowView.findViewById(R.id.label);
        ImageView      imageView = (ImageView) rowView.findViewById(R.id.icon);

        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textView.setText(devices[position].getName());

        imageView.setImageResource( R.mipmap.bluetooth );

        return rowView;
    }
}