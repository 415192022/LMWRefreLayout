package com.lmw.widget.lmwrefrelayout.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lmw.widget.lmwrefrelayout.R;
import com.lmw.widget.lmwrefreshlayout.lib.slide.SlideLayout;
import com.lmw.widget.lmwrefreshlayout.lib.utils.ScreenUtils;
import com.lmw.widget.lmwrefrelayout.model.User;

import java.util.ArrayList;
import java.util.List;

public class SlideDelAdapter extends RecyclerView.Adapter<SlideDelAdapter.VH> {
    private static int screenWidth;
    private List<User> dataList;
    private Context mContext;

    public SlideDelAdapter(Context context, ArrayList<User> data) {
        this.dataList = data;
        this.mContext = context;
        screenWidth = ScreenUtils.getScreenWidth(mContext);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(View.inflate(mContext, R.layout.recycler_item_slide_del, null));
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
        Button btnDel;
        SlideLayout slideLayout;

        public VH(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            btnDel = itemView.findViewById(R.id.btnDel);
            slideLayout = itemView.findViewById(R.id.slideLayout);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvName.getLayoutParams();
            layoutParams.width = screenWidth;

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(btnDel.getContext(), "tvName", Toast.LENGTH_SHORT).show();
                    slideLayout.closeMenu();
                }
            });

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(btnDel.getContext(), "删除", Toast.LENGTH_SHORT).show();
                    slideLayout.closeMenu();
                }
            });
        }
    }


}