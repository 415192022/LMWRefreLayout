package com.lmw.widget.lmwrefrelayout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefrelayout.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.VH> {
    private List<User> dataList;
    private Context mContext;

    public UserAdapter(Context context, ArrayList<User> data) {
        this.dataList = data;
        this.mContext = context;
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(View.inflate(mContext, R.layout.recycler_item_user, null));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.tvName.setText(dataList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        public VH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}