package com.example.gromate.gromate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.firebase.quickstart.database.databinding.ActivityNewOrderItemBinding;
import com.example.gromate.gromate.models.OrderItem;

import static com.example.gromate.gromate.Constants.DATA_VALUE;

public class NewOrderItemActivity extends BaseActivity {

    private static final String REQUIRED = "Required";

    private ActivityNewOrderItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewOrderItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabSaveOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderItem();
            }
        });
    }

    private void createOrderItem() {
        final String name = binding.orderItemName.getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.orderItemName.setError(REQUIRED);
            return;
        }

        final String quantity = binding.orderItemQuantity.getText().toString();
        if (TextUtils.isEmpty(quantity)) {
            binding.orderItemQuantity.setError(REQUIRED);
            return;
        }
        final String unit = binding.orderItemUnit.getText().toString();
        if (TextUtils.isEmpty(unit)) {
            binding.orderItemUnit.setError(REQUIRED);
            return;
        }

        final String description = binding.orderItemDescription.getText().toString();
        OrderItem orderItem = new OrderItem(name, description, Integer.parseInt(quantity), unit);
        Intent intent = new Intent();
        intent.putExtra(DATA_VALUE, orderItem);
        setResult(RESULT_OK, intent);
        finish();
    }
}
