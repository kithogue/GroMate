package com.example.gromate.gromate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.quickstart.database.R;
import com.example.gromate.gromate.NewPlanActivity;
import com.example.gromate.gromate.models.Plan;
import com.example.gromate.gromate.viewholder.PlanViewHolder;

import static com.example.gromate.gromate.DatabaseUtil.getDatabase;
import static com.example.gromate.gromate.DatabaseUtil.getUid;

public class MyPlansFragment extends Fragment {

    private FirebaseRecyclerAdapter<Plan, PlanViewHolder> mAdapter;
    private RecyclerView mRecycler;

    public MyPlansFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_plans, container, false);

        mRecycler = rootView.findViewById(R.id.plansList);
        mRecycler.setHasFixedSize(true);

        FloatingActionButton addPlanButton = rootView.findViewById(R.id.fabAddPlan);
        addPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NewPlanActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        DatabaseReference mDatabase = getDatabase();
        Query orders = mDatabase.child("plans").child(getUid()).orderByChild("name");

        FirebaseRecyclerOptions<Plan> options = new FirebaseRecyclerOptions.Builder<Plan>()
                .setQuery(orders, Plan.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Plan, PlanViewHolder>(options) {

            @NonNull
            @Override
            public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PlanViewHolder(inflater.inflate(R.layout.item_plan, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(@NonNull PlanViewHolder viewHolder, int position, final @NonNull Plan model) {
                viewHolder.bindToPlan(model);
            }
        };
        mRecycler.setAdapter(mAdapter);
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
