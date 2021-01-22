package com.example.gromate.gromate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.databinding.ActivityNewOrderBinding;
import com.example.gromate.gromate.models.Order;
import com.example.gromate.gromate.models.OrderItem;
import com.example.gromate.gromate.viewholder.OrderItemViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.gromate.gromate.Constants.DATA_VALUE;


public class NewOrderActivity extends BaseActivity {

    private static final String TAG = "NewOrderActivity";
    private static final String REQUIRED = "Required";
    private static final int ADD_ORDER_ITEM_REQUEST_CODE = 1;
    private DatabaseReference mDatabase;
    private ActivityNewOrderBinding binding;

    private List<OrderItem> orderItems = new ArrayList<>();

    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance().getReference();

        binding.fabSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitOrder();
            }
        });
        binding.fabAddOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrderItem();
            }
        });

        mRecycler = binding.orderItemsList;
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(new OrderItemsAdapter());
    }

    private class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemViewHolder> {
        public OrderItemsAdapter() {
        }

        @NonNull
        @Override
        public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            return new OrderItemViewHolder(inflater.inflate(R.layout.item_order_item, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull OrderItemViewHolder holder, final int position) {
            holder.bindToOrderItem(orderItems.get(position), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderItems.remove(position);
                    OrderItemsAdapter.this.notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderItems.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_ORDER_ITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            orderItems.add((OrderItem) data.getSerializableExtra(DATA_VALUE));
            mRecycler.getAdapter().notifyDataSetChanged();
        }
    }

    private void addOrderItem() {
        startActivityForResult(new Intent(this, NewOrderItemActivity.class), ADD_ORDER_ITEM_REQUEST_CODE);
    }

    private void submitOrder() {
        final String name = binding.orderName.getText().toString();
        final String description = binding.orderDescription.getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.orderName.setError(REQUIRED);
            return;
        }

        setEditingEnabled(false);
        Toast.makeText(this, "Creating Order...", Toast.LENGTH_SHORT).show();

        writeNewOrder(name, description);
        setEditingEnabled(true);
        finish();
    }


    private void setEditingEnabled(boolean enabled) {
        binding.orderName.setEnabled(enabled);
        binding.orderDescription.setEnabled(enabled);
        if (enabled) {
            binding.fabSaveOrder.show();
        } else {
            binding.fabSaveOrder.hide();
        }
    }

    private void writeNewOrder(String name, String description) {
        String key = mDatabase.child("orders").push().getKey();
        Order order = new Order(key, name, description, orderItems.toArray(new OrderItem[0]));
        Map<String, Object> postValues = order.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/orders/" + getUid() + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
}
