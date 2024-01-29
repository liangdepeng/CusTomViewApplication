package com.dpdp.testapplication.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dpdp.testapplication.databinding.ItemDialogTestBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogTestAdapter extends RecyclerView.Adapter<DialogTestAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener itemClickListener;
    private final ArrayList<String> datas = new ArrayList<>();

    public DialogTestAdapter(Context context) {
        this.context = context;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData() {
        datas.clear();
        datas.addAll(Arrays.asList(btns));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDialogTestBinding binding = ItemDialogTestBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String s = datas.get(position);
        holder.binding.btn.setText(s);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemDialogTestBinding binding;

        public ViewHolder(ItemDialogTestBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position < 0 || position > datas.size() - 1) {
                        return;
                    }
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(datas.get(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(String itemShowContent, int position);
    }

    private final String[] btns = new String[]{LOADING,TIPS};
    public static final String LOADING = "加载弹窗";
    public static final String TIPS = "提示弹窗";
}
