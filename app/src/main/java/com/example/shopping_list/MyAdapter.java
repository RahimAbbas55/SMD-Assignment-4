package com.example.shopping_list;

import android.content.Context;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<ItemModal> items;
    DatabaseReference myDB;

    public MyAdapter(Context context, ArrayList<ItemModal> items) {
        this.context = context;
        this.items = items;
        this.myDB = FirebaseDatabase.getInstance().getReference("items");
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ItemModal item = items.get(position);
        holder.item_name.setText(item.getItem_name());
        holder.item_quantity.setText(String.valueOf("Qt: " + item.getQuantity()));
        holder.item_price.setText(String.valueOf("Pkr: " + item.getPrice()));

        holder.deleteBtn.setOnClickListener(v -> {
            String key = item.getKey(); // Get the unique key of the item
            if (key != null) {
                myDB.child(key).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        items.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                        Toast.makeText(context, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to delete item!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Failed to delete: Missing key!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView item_name, item_quantity, item_price;
        ImageButton deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_quantity = itemView.findViewById(R.id.item_quantity);
            item_price = itemView.findViewById(R.id.item_price);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }
}
