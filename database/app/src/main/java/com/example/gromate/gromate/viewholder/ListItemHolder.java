package com.example.gromate.gromate.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.OrderItemSummary;

import java.util.HashMap;
import java.util.Map;

import static com.example.gromate.gromate.Constants.SELECTED_COLOR;
import static com.example.gromate.gromate.DatabaseUtil.getDatabase;
import static com.example.gromate.gromate.DatabaseUtil.getUid;

public class ListItemHolder extends RecyclerView.ViewHolder {
    private OrderItemSummary summary;
    private final TextView name;
    private final TextView quantity;
    private final TextView unit;

    public ListItemHolder(@NonNull final View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.orderItemNameFieldRec);
        quantity = itemView.findViewById(R.id.orderItemQuantityFieldRec);
        unit = itemView.findViewById(R.id.orderItemUnitFieldRec);
        ImageButton deleteButton = itemView.findViewById(R.id.orderItemDeleteBtn);
        deleteButton.setVisibility(View.INVISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (summary != null) {
                    itemView.setEnabled(false);
                    summary.toggleSelected();
                    DatabaseReference mDatabase = getDatabase();
                    Map<String, Object> update = new HashMap<>();
                    update.put(summary.getId(), summary.toMap());
                    mDatabase.child("current-list").child(getUid()).updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            itemView.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    public void bindToOrderItem(OrderItemSummary summary) {
        name.setText(summary.getName());
        quantity.setText(String.valueOf(summary.getQuantity()));
        unit.setText(summary.getUnit());
        this.summary = summary;
        itemView.setBackgroundColor(summary.isSelected() ? SELECTED_COLOR : Color.WHITE);
    }

}
