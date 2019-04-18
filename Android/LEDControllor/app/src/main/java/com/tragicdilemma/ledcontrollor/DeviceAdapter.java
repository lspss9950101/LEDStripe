package com.tragicdilemma.ledcontrollor;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> implements View.OnClickListener{

    private Set<BluetoothDevice> bluetoothDevices;

    public DeviceAdapter(Set<BluetoothDevice> bluetoothDevices){
        this.bluetoothDevices = bluetoothDevices;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, null);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tv_device.setText(((BluetoothDevice)bluetoothDevices.toArray()[i]).getName());
        viewHolder.itemView.setTag(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.size();
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null)onItemClickListener.onItemClick(view, (int) view.getTag());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_device;
        public ViewHolder(View view){
            super(view);
            tv_device = view.findViewById(R.id.rv_device);
        }
    }

    private OnItemClickListener onItemClickListener = null;

    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public Set<BluetoothDevice> getBluetoothDevices(){
        return bluetoothDevices;
    }
}
