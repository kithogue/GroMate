package com.example.gromate.gromate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.models.OrderItemSummary;
import com.example.gromate.gromate.viewholder.ListItemHolder;

import java.util.Map;

import static com.example.gromate.gromate.DatabaseUtil.getDatabase;
import static com.example.gromate.gromate.DatabaseUtil.getUid;

public class ShoppingListFragment extends Fragment {

    private RecyclerView mRecycler;
    private FirebaseRecyclerAdapter<OrderItemSummary, ListItemHolder> mAdapter;

    private View shareButton;
    private StringBuilder shareText;

    public ShoppingListFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        mRecycler = rootView.findViewById(R.id.shoppingList);
        mRecycler.setHasFixedSize(true);

        shareText = new StringBuilder();
        //share button
        shareButton = rootView.findViewById(R.id.fabShareList);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareList();
            }
        });
        return rootView;
    }

    private void shareList() {

        if (shareText.length() > 0) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setHasFixedSize(true);


        DatabaseReference mDatabase = getDatabase();
        Query currentList = mDatabase.child("current-list").child(getUid()).orderByChild("name");

        FirebaseRecyclerOptions<OrderItemSummary> options = new FirebaseRecyclerOptions.Builder<OrderItemSummary>()
                .setQuery(currentList, OrderItemSummary.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<OrderItemSummary, ListItemHolder>(options) {

            @NonNull
            @Override
            public ListItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new ListItemHolder(inflater.inflate(R.layout.item_order_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull ListItemHolder viewHolder, int position, final @NonNull OrderItemSummary model) {
                addShareText(model);
                viewHolder.bindToOrderItem(model);
            }

        };
        mRecycler.setAdapter(mAdapter);
    }

    private void addShareText(OrderItemSummary model) {
        shareText.append(model.getName().trim())
                .append(" - ")
                .append(model.getQuantity())
                .append(' ')
                .append(model.getUnit().trim()).append('\n');
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
