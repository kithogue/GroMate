package com.example.gromate;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gromate.gromate.DatabaseUtil;
import com.example.gromate.gromate.models.Order;
import com.example.gromate.gromate.models.OrderItem;
import com.example.gromate.gromate.models.OrderItemSummary;
import com.example.gromate.gromate.models.Plan;
import com.example.gromate.gromate.viewholder.PlanViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.quickstart.database.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanViewHolderTest {
    private static final String TEST_UID = "test_uid";
    private static final String PLAN_ID = "plan_id";
    private static final String PLAN_NAME = "plan_name";
    private static final String PLAN_DESCRIPTION = "plan_description";
    private static final String ORDER_ID = "order_id";
    private static final String ORDER_NAME = "order_name";
    private static final String ORDER_DESCRIPTION = "order_description";

    private static final String ORDER_ITEM_UNIT = "kg";

    private static final String ORDER_ITEM_1_NAME = "order_item_1_name";
    private static final String ORDER_ITEM_1_DESCRIPTION = "order_item_1_description";
    private static final int ORDER_ITEM_1_QUANTITY = 3;

    private static final String ORDER_ITEM_2_NAME = "order_item_1_name";
    private static final String ORDER_ITEM_2_DESCRIPTION = "order_item_1_description";
    private static final int ORDER_ITEM_2_QUANTITY = 2;

    private static final String ORDER_ITEM_3_NAME = "order_item_3_name";
    private static final String ORDER_ITEM_3_DESCRIPTION = "order_item_3_description";
    private static final int ORDER_ITEM_3_QUANTITY = 10;

    private Set<Order> selectedOrders;
    private View view;
    private DatabaseReference databaseReference;
    private Order order1;
    private Plan plan;


    @Before
    public void setUp() {
        //set up orders
        selectedOrders = new HashSet<>();
        OrderItem orderItem1 = new OrderItem(ORDER_ITEM_1_NAME, ORDER_ITEM_1_DESCRIPTION, ORDER_ITEM_1_QUANTITY, ORDER_ITEM_UNIT);
        OrderItem orderItem2 = new OrderItem(ORDER_ITEM_2_NAME, ORDER_ITEM_2_DESCRIPTION, ORDER_ITEM_2_QUANTITY, ORDER_ITEM_UNIT);
        OrderItem orderItem3 = new OrderItem(ORDER_ITEM_3_NAME, ORDER_ITEM_3_DESCRIPTION, ORDER_ITEM_3_QUANTITY, ORDER_ITEM_UNIT);
        order1 = new Order(ORDER_ID, ORDER_NAME, ORDER_DESCRIPTION, new OrderItem[]{orderItem1, orderItem2, orderItem3});
        selectedOrders.add(order1);

        //set up view
        view = mock(View.class);
        when(view.findViewById(ArgumentMatchers.anyInt())).thenReturn(mock(TextView.class));
        when(view.findViewById(ArgumentMatchers.eq(R.id.planDeleteBtn))).thenReturn(mock(ImageButton.class));

        //init database
        databaseReference = mock(DatabaseReference.class);
        DatabaseUtil.setDatabaseReference(databaseReference);

        DatabaseUtil.setUid(TEST_UID);
        assertThat(DatabaseUtil.getUid(), is(TEST_UID));

        //set up plan
        plan = new Plan(PLAN_ID, PLAN_NAME, PLAN_DESCRIPTION, selectedOrders);


    }

    @Test
    public void testPlanSaveInitializedCorrectly() {

        PlanViewHolder planViewHolder = new PlanViewHolder(view);
        planViewHolder.bindToPlan(plan);

        DatabaseReference ordersReference = mock(DatabaseReference.class);
        when(databaseReference.child(ArgumentMatchers.eq("orders"))).thenReturn(ordersReference);

        DatabaseReference userOrdersReference = mock(DatabaseReference.class);
        when(ordersReference.child(ArgumentMatchers.eq(TEST_UID))).thenReturn(userOrdersReference);

        planViewHolder.savePlan();
        verify(databaseReference, times(1)).child("orders");
        verify(ordersReference, times(1)).child(TEST_UID);
        verify(userOrdersReference, times(1)).addListenerForSingleValueEvent(any(ValueEventListener.class));

    }

    @Test
    public void testCreateSummary() {

        PlanViewHolder planViewHolder = new PlanViewHolder(view);
        planViewHolder.bindToPlan(plan);

        DataSnapshot userOrders = mock(DataSnapshot.class);
        DataSnapshot orderSnapshot = mock(DataSnapshot.class);
        when(orderSnapshot.getValue()).thenReturn(order1.toMap());
        when(userOrders.getChildren()).thenReturn(Collections.singletonList(orderSnapshot));
        Map<String, OrderItemSummary> summaries = planViewHolder.createSummary(userOrders);
        assertNotNull(summaries);
        assertEquals(2, summaries.size());

        //check sum form item 1 and item 2working
        OrderItemSummary summary1 = summaries.get(ORDER_ITEM_1_NAME);
        assertEquals(ORDER_ITEM_1_NAME, summary1.getName());
        assertEquals(ORDER_ITEM_1_DESCRIPTION, summary1.getDescription());
        assertEquals(ORDER_ITEM_UNIT, summary1.getUnit());
        assertEquals(5, summary1.getQuantity());

        OrderItemSummary summary2 = summaries.get(ORDER_ITEM_3_NAME);
        assertEquals(ORDER_ITEM_3_NAME, summary2.getName());
        assertEquals(ORDER_ITEM_3_DESCRIPTION, summary2.getDescription());
        assertEquals(ORDER_ITEM_UNIT, summary2.getUnit());
        assertEquals(ORDER_ITEM_3_QUANTITY, summary2.getQuantity());

    }


}