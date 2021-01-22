package com.example.gromate.gromate.viewholder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.OrderItemSummary;
import com.example.gromate.gromate.models.Plan;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.example.gromate.gromate.Constants.SELECTED_COLOR;
import static com.example.gromate.gromate.DatabaseUtil.getDatabase;
import static com.example.gromate.gromate.DatabaseUtil.getUid;

public class PlanViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final TextView description;
    private final TextView orders;
    private Plan plan;
    private View itemView;

    public PlanViewHolder(final View itemView) {
        super(itemView);
        this.itemView = itemView;
        name = itemView.findViewById(R.id.planNameField);
        description = itemView.findViewById(R.id.planDescriptionField);
        orders = itemView.findViewById(R.id.planOrdersField);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(), "Creating Shopping List", Toast.LENGTH_SHORT).show();
                savePlan();
            }
        });

        ImageButton deleteButton = itemView.findViewById(R.id.planDeleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (plan != null) {
                    DatabaseReference mDatabase = getDatabase();
                    mDatabase.child("plans").child(getUid()).child(plan.getId()).removeValue();
                    mDatabase.child("current-list").child(getUid()).removeValue();
                }
            }
        });
    }

    public void bindToPlan(Plan plan) {
        this.plan = plan;
        name.setText(plan.getName());
        description.setText(plan.getDescription());
        orders.setText(plan.getOrderNames());
        itemView.setBackgroundColor(plan.isSelected() ? SELECTED_COLOR : Color.WHITE);
    }

    @SuppressWarnings("unchecked")
    public void savePlan() {
        final DatabaseReference mDatabase = getDatabase();
        final String userId = getUid();
        mDatabase.child("orders").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                mDatabase.child("current-list").child(userId).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Map<String, OrderItemSummary> summary = createSummary(snapshot);
                        createCurrentList(summary);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });
    }

    public Map<String, OrderItemSummary> createSummary(DataSnapshot snapshot) {
        final Set<String> orderIds = new HashSet<>(Arrays.asList(plan.getOrders().split(",")));
        final Map<String, OrderItemSummary> summary = new HashMap<>();
        for (DataSnapshot child : snapshot.getChildren()) {
            Map<String, Object> order = (Map<String, Object>) child.getValue();
            String id = (String) order.get("id");
            if (!orderIds.contains(id)) {
                continue;
            }
            List<Map<String, Object>> items = (List<Map<String, Object>>) order.get("items");
            if (items != null) {
                for (Map<String, Object> item : items) {
                    String name = (String) item.get("name");
                    int quantity = ((Number) item.get("quantity")).intValue();
                    OrderItemSummary summaryItem = summary.get(name);
                    if (summaryItem == null) {
                        String unit = (String) item.get("unit");
                        String description = (String) item.get("description");
                        summaryItem = new OrderItemSummary(name, description, quantity, unit);
                    } else {
                        summaryItem.addQuantity(quantity);
                    }
                    summary.put(name, summaryItem);
                }
            }
        }
        return summary;
    }

    public void createCurrentList(Map<String, OrderItemSummary> summary) {
        final DatabaseReference mDatabase = getDatabase();
        final String userId = getUid();
        final Map<String, Object> childUpdates = new HashMap<>();
        for (OrderItemSummary summaryItem : summary.values()) {
            String key = mDatabase.child("current-list").push().getKey();
            summaryItem.setId(key);
            childUpdates.put("/current-list/" + userId + "/" + key, summaryItem.toMap());
        }
        mDatabase.child("plans").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    String planId = child.getKey();
                    childUpdates.put("/plans/" + userId + "/" + planId + "/selected", planId.equals(plan.getId()));
                }
                mDatabase.updateChildren(childUpdates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //do nothing
            }
        });
    }


}
