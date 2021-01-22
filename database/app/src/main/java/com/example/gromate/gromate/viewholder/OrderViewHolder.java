package com.example.gromate.gromate.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.Order;

import static com.example.gromate.gromate.DatabaseUtil.getDatabase;
import static com.example.gromate.gromate.DatabaseUtil.getUid;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    private Order order;
    private final TextView name;
    private final TextView description;

    public OrderViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.orderNameFieldRec);
        description = itemView.findViewById(R.id.orderDescriptionFieldRec);

        ImageButton deleteButton = itemView.findViewById(R.id.orderDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null) {
                    DatabaseReference mDatabase = getDatabase();
                    mDatabase.child("orders").child(getUid()).child(order.getId()).removeValue();
                }
            }
        });

    }

    public void bindToOrder(Order order) {
        this.order = order;
        name.setText(order.getName());
        description.setText(order.getDescription());
    }
}
