package com.tragicdilemma.ledcontrollor;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageButton btn_primary_color, btn_secondary_color;
    Spinner sp_mode;
    SeekBar sb_config, sb_delay;
    TextView tv_data, tv_delay, tv_label_config;
    Switch sw_dual;
    ConstraintLayout cl_op;
    BTInterface btInterface;
    BluetoothDevice bluetoothDevice;
    int mode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btn_primary_color = findViewById(R.id.btn_primary_color);
        btn_secondary_color = findViewById(R.id.btn_secondary_color);
        sp_mode = findViewById(R.id.sp_mode);
        sb_config = findViewById(R.id.sb_config);
        sb_delay = findViewById(R.id.sb_delay);
        tv_data = findViewById(R.id.tv_data);
        tv_delay = findViewById(R.id.tv_delay);
        tv_label_config = findViewById(R.id.label_config);
        sw_dual = findViewById(R.id.sw_dual);
        cl_op = findViewById(R.id.cl_op);

        btn_primary_color.setTag(new int[]{0, 255, 255});
        btn_secondary_color.setTag(new int[]{0, 255, 255});
        btn_primary_color.setOnClickListener(cl_color_picker);
        btn_secondary_color.setOnClickListener(cl_color_picker);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.mode, R.layout.item_spinner);
        sp_mode.setAdapter(arrayAdapter);
        sp_mode.setOnItemSelectedListener(sl_mode);

        sb_config.setOnSeekBarChangeListener(sl_config);
        sb_delay.setOnSeekBarChangeListener(sl_delay);

        sw_dual.setOnCheckedChangeListener(cl_dual);

        sp_mode.setSelection(mode);
        updateMode(mode);

        btInterface = BTInterface.getInstance(this);

    }

    private void updateMode(int mode){
        this.mode = mode;
        String data_name[] = {"", "Rainbow Period", "Gradient Period", "Resolution", "", "Gradient Period"};
        tv_label_config.setText(data_name[mode]);
        switch(this.mode){
            case 0:{
                cl_op.setVisibility(View.GONE);
                btn_primary_color.setEnabled(false);
                btn_secondary_color.setEnabled(false);
                btn_primary_color.setBackgroundColor(Color.GRAY);
                btn_secondary_color.setBackgroundColor(Color.GRAY);
                break;
            }
            case 1: {
                cl_op.setVisibility(View.VISIBLE);
                sw_dual.setVisibility(View.GONE);
                btn_primary_color.setEnabled(false);
                btn_secondary_color.setEnabled(false);
                btn_primary_color.setBackgroundColor(Color.GRAY);
                btn_secondary_color.setBackgroundColor(Color.GRAY);
                sb_config.setEnabled(true);
                sb_delay.setEnabled(true);
                break;
            }
            case 2: case 5: {
                cl_op.setVisibility(View.VISIBLE);
                sw_dual.setVisibility(View.GONE);
                btn_primary_color.setEnabled(true);
                btn_secondary_color.setEnabled(true);
                int primary[] = (int[]) btn_primary_color.getTag(), secondary[] = (int[]) btn_secondary_color.getTag();
                btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                btn_secondary_color.setBackgroundColor(Color.HSVToColor(new float[]{secondary[0], secondary[1] / 255.f, secondary[2] / 255.f}));
                sb_config.setEnabled(true);
                sb_delay.setEnabled(true);
                break;
            }
            case 3: {
                cl_op.setVisibility(View.VISIBLE);
                sw_dual.setVisibility(View.VISIBLE);
                if(sw_dual.isChecked()){
                    btn_primary_color.setEnabled(true);
                    btn_secondary_color.setEnabled(true);
                    int primary[] = (int[]) btn_primary_color.getTag(), secondary[] = (int[]) btn_secondary_color.getTag();
                    btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                    btn_secondary_color.setBackgroundColor(Color.HSVToColor(new float[]{secondary[0], secondary[1] / 255.f, secondary[2] / 255.f}));
                }else{
                    btn_primary_color.setEnabled(true);
                    btn_secondary_color.setEnabled(false);
                    int primary[] = (int[]) btn_primary_color.getTag();
                    btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                    btn_secondary_color.setBackgroundColor(Color.GRAY);
                }
                sb_config.setEnabled(true);
                sb_delay.setEnabled(true);
                break;
            }
            case 4: {
                cl_op.setVisibility(View.GONE);
                btn_primary_color.setEnabled(true);
                btn_secondary_color.setEnabled(false);
                int primary[] = (int[]) btn_primary_color.getTag();
                btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                btn_secondary_color.setBackgroundColor(Color.GRAY);
                break;
            }
        }
    }

    private Switch.OnCheckedChangeListener cl_dual = new Switch.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                btn_primary_color.setEnabled(true);
                btn_secondary_color.setEnabled(true);
                int primary[] = (int[]) btn_primary_color.getTag(), secondary[] = (int[]) btn_secondary_color.getTag();
                btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                btn_secondary_color.setBackgroundColor(Color.HSVToColor(new float[]{secondary[0], secondary[1] / 255.f, secondary[2] / 255.f}));
            }else{
                btn_primary_color.setEnabled(true);
                btn_secondary_color.setEnabled(false);
                int primary[] = (int[]) btn_primary_color.getTag();
                btn_primary_color.setBackgroundColor(Color.HSVToColor(new float[]{primary[0], primary[1] / 255.f, primary[2] / 255.f}));
                btn_secondary_color.setBackgroundColor(Color.GRAY);
            }
        }
    };

    private Spinner.OnItemSelectedListener sl_mode = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            updateMode(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private SeekBar.OnSeekBarChangeListener sl_config = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            tv_data.setText(String.valueOf(i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener sl_delay = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            tv_delay.setText(String.valueOf(i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private ImageButton.OnClickListener cl_color_picker = new ImageButton.OnClickListener() {
        @Override
        public void onClick(View view) {
            CreateColorDialog((ImageButton) view).show();
        }
    };

    public AlertDialog CreateColorDialog(final ImageButton imgbtn){
        int[] color = (int[]) imgbtn.getTag();
        final int sat = color[1];
        final int val = color[2];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_color_picker, null);
        final SeekBar sb_sat = view.findViewById(R.id.sb_sat);
        final SeekBar sb_val = view.findViewById(R.id.sb_val);
        sb_sat.setProgress(sat);
        sb_val.setProgress(val);

        GridView gv_color = view.findViewById(R.id.gv_color);
        final ColorAdapter adapter = new ColorAdapter(sat / 255.f, val / 255.f);
        gv_color.setAdapter(adapter);

        final AlertDialog dialog = builder
                .setView(view)
                .setNegativeButton("Close", null)
                .create();
        gv_color.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int color = Color.HSVToColor(new float[]{i*3, sb_sat.getProgress() / 255.f, sb_val.getProgress() / 255.f});
                imgbtn.setBackgroundColor(color);
                imgbtn.setTag(new int[]{i*3, sb_sat.getProgress(), sb_val.getProgress()});
                dialog.dismiss();
            }
        });
        sb_sat.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adapter.updateSV(i / 255.f, null);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sb_val.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adapter.updateSV(null, i / 255.f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return dialog;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_send) {
            if(checkBT()){
                if(bluetoothDevice != null){
                    if(btInterface.isConnected()){
                        btInterface.write(generateDataArray());
                    }
                }
            }
        } else if (id == R.id.nav_device) {
            if(checkBT())showBTDevice();
        } else if (id == R.id.nav_connect) {
            if(checkBT()){
                if(bluetoothDevice != null)btInterface.connect(bluetoothDevice);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean checkBT() {
        if (btInterface.isEnable()) return true;
        btInterface.enableBT();
        return false;
    }

    private void showBTDevice(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_paired_device, null);

        RecyclerView rv_device = view.findViewById(R.id.rv_device);
        final DeviceAdapter deviceAdapter = new DeviceAdapter(btInterface.getPairedDevice());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_device.setLayoutManager(layoutManager);
        rv_device.setAdapter(deviceAdapter);

        deviceAdapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bluetoothDevice = (BluetoothDevice) deviceAdapter.getBluetoothDevices().toArray()[position];
            }
        });

        builder.setView(view)
                .setNegativeButton("Cancel", null)
                .show();
    }

    private byte[] generateDataArray(){
        byte data[] = new byte[9];
        data[0] = (byte) (mode &  0x000000ff);
        int primary[] = (int[]) btn_primary_color.getTag();
        data[1] = (byte)((int)(primary[0] * 255 / 360.f));
        data[2] = (byte) primary[1];
        data[3] = (byte) primary[2];

        int secondary[] = (int[]) btn_secondary_color.getTag();
        data[4] = (byte) ((int)(secondary[0] * 255 / 360.f));
        data[5] = (byte) secondary[1];
        data[6] = (byte) secondary[2];

        data[7] = (byte) sb_config.getProgress();
        data[8] = (byte) sb_delay.getProgress();
        return data;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case BTInterface.REQUEST_ENABLE_BT:{
                if(resultCode == RESULT_OK){

                }else{
                    Toast.makeText(getApplicationContext(), "BT off.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
