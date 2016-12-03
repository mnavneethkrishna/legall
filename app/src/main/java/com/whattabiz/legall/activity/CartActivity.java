package com.whattabiz.legall.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.whattabiz.legall.models.CartItemModel;
import com.whattabiz.legall.adapters.CartListAdapter;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Cart activity
 * complete data from backend reload contents from backend cart
 */

public class CartActivity extends AppCompatActivity {

    public static List<CartItemModel> cartItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    public static CartListAdapter mAdapter;

    LinearLayout buttonView, emptyView;
    Button checkout;

    public TextView txtTotal;

    public static CartActivity cartActivity;

    public static CartActivity getInstance() {
        return cartActivity;
    }

    TextView total;
    Integer sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cart");

        //total of the items
        //total = (TextView) findViewById(R.id.txt_total);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        mAdapter = new CartListAdapter(cartItemList,CartActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CartActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        buttonView = (LinearLayout) findViewById(R.id.button_view);
        emptyView = (LinearLayout) findViewById(R.id.view_empty);
        txtTotal = (TextView) findViewById(R.id.txt_total);
        txtTotal.setText("₹ " + getTotal());


        updateCart();


        checkout = (Button) findViewById(R.id.checkout);

        // = (LinearLayout) findViewById(R.id.view_buttons);
        //buttonView.setVisibility(View.GONE);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (cartItemList.isEmpty()) {
                    Toast.makeText(CartActivity.this, "Cart Is Empty", Toast.LENGTH_LONG).show();
                } else {

                    //orderItems();
                    int total = 0;

                    JSONObject object = new JSONObject();

                    try {


                        JSONArray jsonArray = new JSONArray();
                        for (CartItemModel c : cartItemList) {

                            total += Double.valueOf(c.getCost()).intValue();
                            JSONObject obj = new JSONObject();
                            obj.put("name", c.getTitle());
                            obj.put("description", c.getCat());
                            obj.put("vale", c.getCost());
                            obj.put("isRequired", "true");
                            obj.put("settlementEvent", "EmailConfirmation");

                            jsonArray.put(obj);
                        }

                        object.put("paymentParts", jsonArray);

                        JSONArray identifierArray = new JSONArray();

                        JSONObject obj1 = new JSONObject();
                        obj1.put("field", "completionDate");
                        obj1.put("value", "11/27/16");
                        identifierArray.put(obj1);


                        JSONObject obj2 = new JSONObject();
                        obj2.put("field", "txnid");
                        obj2.put("value", "12345678");
                        identifierArray.put(obj2);

                        object.put("paymentIdentifiers", identifierArray);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    openPaymentGateway(String.valueOf(total), object.toString());
                }


            }
        });

    }

    public void openPaymentGateway(String total, String productInfo) {

        Intent i = new Intent(CartActivity.this, PaymentGatewayActivity.class);
        i.putExtra("TOTAL", total);
        i.putExtra("PRODUCT_INFO", productInfo);
        startActivity(i);


    }

    public void orderItems() {
        final String url = "http://legall.co.in/web/cart.php?key=Legall";


        final ProgressDialog pd = new ProgressDialog(CartActivity.this);
        pd.setMessage("Sending order details..");
        pd.setCancelable(false);
        pd.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Success handle response

                        pd.dismiss();

                        Log.i("Legall", response);


                        try {

                            JSONObject object = new JSONObject(response);
                            int status = object.getInt("status");

                            if (status == 1) {

                                final AlertDialog.Builder dialog = new AlertDialog.Builder(CartActivity.this);
                                dialog.setMessage("Your order request has been sent, we'll contact you soon.");
                                dialog.setTitle("Success");
                                dialog.setCancelable(false);
                                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //clearing user data and preparing for login

                                        cartItemList.clear();
                                        finish();

                                    }
                                });

                                dialog.show();
                            } else {


                                //registration form is being rejected by the server

                                pd.dismiss();

                                final AppCompatDialog dialog = new AppCompatDialog(CartActivity.this);
                                dialog.setContentView(R.layout.dialog_invalid_form);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                TextView message = (TextView) dialog.findViewById(R.id.txt_message);
                                message.setText("Please check the Cart and try again.");


                                Button back = (Button) dialog.findViewById(R.id.btn_back);
                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(final View view) {

                                        dialog.dismiss();
                                        return;
                                    }


                                });

                                dialog.show();
                                return;

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error handle error


                pd.dismiss();

                final AppCompatDialog dialog = new AppCompatDialog(CartActivity.this);
                dialog.setContentView(R.layout.dilog_request_error);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back = (Button) dialog.findViewById(R.id.btn_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        dialog.dismiss();

                        return;
                    }


                });

                dialog.show();
                return;


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();


                JSONArray cart = new JSONArray();

                try {


                    for (CartItemModel c : cartItemList) {
                        JSONObject object = new JSONObject();
                        object.put("item", c.getTitle().toString());
                        object.put("price", c.getCost().toString());
                        object.put("cat", c.getCat().toString());


                        cart.put(object);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Log.i("Legall",cart.toString());


                param.put("cart", cart.toString());
                param.put("user_id", User.Id);
                param.put("profession", User.type);


                Log.i("Legall", param.toString());


                return param;
            }

        };

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(CartActivity.this).add(stringRequest);


    }


    public void updateCart() {
        if (cartItemList.isEmpty()) {
            buttonView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            buttonView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        txtTotal.setText("₹ " + getTotal());

        mAdapter.notifyDataSetChanged();
    }


    public static void clearCart() {

        cartItemList.clear();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    public void onResume() {
        updateCart();
        super.onResume();
    }

    public int getTotal() {

        int total = 0;

        if (!cartItemList.isEmpty())

            for (CartItemModel c : cartItemList) {

                total += Double.valueOf(c.getCost()).intValue();

            }

        return total;

    }


    public  void  updateTotal(){

        txtTotal.setText("₹ " + getTotal());
    }
}