package com.dpdp.testapplication.base;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpdp.testapplication.R;

import java.util.List;

/**
 * Created by ldp.
 * <p>
 * Date: 2021-03-16
 * <p>
 * Summary:
 * <p>
 * api path:
 */
public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private Context context;
    private List<ClassBean> classBeanList;

    public ClassAdapter(Context context, List<ClassBean> classBeanList) {
        this.context = context;
        this.classBeanList = classBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_item_class_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (classBeanList == null || classBeanList.size() == 0)
            return;
        if (classBeanList.get(position) == null)
            return;

        holder.button.setText(classBeanList.get(position).getDesc());

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, classBeanList.get(position).getaClass());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classBeanList == null ? 0 : classBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.button);
        }
    }
}
