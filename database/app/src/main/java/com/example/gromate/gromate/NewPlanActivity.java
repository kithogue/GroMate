package com.example.gromate.gromate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.databinding.ActivityNewPlanBinding;
import com.example.gromate.gromate.models.Order;
import com.example.gromate.gromate.models.Plan;
import com.example.gromate.gromate.viewholder.PlanOrderViewHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class NewPlanActivity extends BaseActivity {

    private static final String REQUIRED = "Required";
    private DatabaseReference mDatabase;
    private ActivityNewPlanBinding binding;

    private FirebaseRecyclerAdapter<Order, PlanOrderViewHolder> mAdapter;
    private final Set<Order> selectedOrders = new TreeSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewPlanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.fabSavePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPlan();
            }
        });

        RecyclerView mRecycler = binding.planOrdersList;
        // Set up Layout Manager, reverse layout
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);

        Query orders = mDatabase.child("orders").child(getUid()).orderByChild("name");

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(orders, Order.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Order, PlanOrderViewHolder>(options) {
            @NonNull
            @Override
            public PlanOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PlanOrderViewHolder(inflater.inflate(R.layout.item_plan_order, viewGroup, false), selectedOrders);
            }

            @Override
            protected void onBindViewHolder(@NonNull PlanOrderViewHolder viewHolder, int position, final @NonNull Order model) {
                viewHolder.bindToOrder(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.stopListening();
    }

    private void submitPlan() {
        final String name = binding.planName.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(name)) {
            binding.planDescription.setError(REQUIRED);
            return;
        }

        final String description = binding.planDescription.getText().toString();
        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Creating Plan...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        // Write new order
        writeNewPlan(userId, name, description);
        // Finish this Activity, back to the stream
        setEditingEnabled(true);
        finish();
        // [END_EXCLUDE]
    }


    private void setEditingEnabled(boolean enabled) {
        binding.planName.setEnabled(enabled);
        binding.planDescription.setEnabled(enabled);
        if (enabled) {
            binding.fabSavePlan.show();
        } else {
            binding.fabSavePlan.hide();
        }
    }

    private void writeNewPlan(String userId, String name, String description) {
        String key = mDatabase.child("plans").push().getKey();
        Plan plan = new Plan(key, name, description, selectedOrders);
        Map<String, Object> postValues = plan.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/plans/" + userId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }

}
