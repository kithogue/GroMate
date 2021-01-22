package com.example.gromate.gromate.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.Order;

import java.util.Set;

import static com.example.gromate.gromate.Constants.SELECTED_COLOR;

public class PlanOrderViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView description;
    private Order order;

    public PlanOrderViewHolder(final View itemView, final Set<Order> selectedOrders) {
        super(itemView);
        name = itemView.findViewById(R.id.planOrderNameField);
        description = itemView.findViewById(R.id.planOrderDescriptionField);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedOrders.contains(order)) {
                    itemView.setBackgroundColor(Color.WHITE);
                    selectedOrders.remove(order);
                } else {
                    itemView.setBackgroundColor(SELECTED_COLOR);
                    selectedOrders.add(order);
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
