package com.example.gromate.gromate.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.OrderItem;

public class OrderItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView quantity;
    private final TextView unit;

    public OrderItemViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.orderItemNameFieldRec);
        quantity = itemView.findViewById(R.id.orderItemQuantityFieldRec);
        unit = itemView.findViewById(R.id.orderItemUnitFieldRec);

    }

    public void bindToOrderItem(OrderItem orderItem, View.OnClickListener deleteListener) {
        name.setText(orderItem.getName());
        quantity.setText(String.valueOf(orderItem.getQuantity()));
        unit.setText(orderItem.getUnit());
        ImageButton deleteButton = itemView.findViewById(R.id.orderItemDeleteBtn);
        deleteButton.setOnClickListener(deleteListener);
    }

}
