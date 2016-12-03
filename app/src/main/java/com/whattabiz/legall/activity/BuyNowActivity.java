package com.whattabiz.legall.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.whattabiz.legall.models.CartItemModel;
import com.whattabiz.legall.R;


/**
 * Set the Email option in the layout to retrive user email from the backend and
 * add an option to edit it and send the email address along with the item adding to the cart to the backend
 */

public class BuyNowActivity extends AppCompatActivity {

    TextView title,cost,ready;
    Button get;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);




        b = getIntent().getExtras();
        title = (TextView) findViewById(R.id.txt_title);
        cost = (TextView) findViewById(R.id.txt_cost);
        ready = (TextView) findViewById(R.id.txt_ready);
        ready.setVisibility(View.GONE);
        title.setText(b.getString("TITLE"));
        cost.setText("₹ "+b.getString("COST"));



        get =(Button) findViewById(R.id.btn_get);
        get.setText("Add to Cart");


        //check whether the item is already in cart
        checkCart();


        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int flag = 0;

                for (CartItemModel itemModel :  CartActivity.getInstance().cartItemList){

                    if (itemModel.getId().matches(b.getString("ID"))){
                        Toast.makeText(BuyNowActivity.this,"Item already in cart ",Toast.LENGTH_LONG).show();
                        flag = 1;
                    }
                }

                if (flag==0) {

                    CartActivity.getInstance().cartItemList.add(new CartItemModel(b.getString("ID"), b.getString("TITLE"), b.getString("COST"), b.getString("LINK")));

                    //updating the selected image list in the activity
                    //CartActivity.getInstance().prepareCartList();


                    Toast.makeText(BuyNowActivity.this, "Item Added to Cart", Toast.LENGTH_LONG).show();

                    checkCart();
                }

            }





            });







        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Get Service");




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.tb_cart:
                startActivity(new Intent(BuyNowActivity.this,CartActivity.class));
                break;


        }
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);

        return true;
    }



    public void checkCart() {


        if (!CartActivity.getInstance().cartItemList.isEmpty()) {

            for (CartItemModel c : CartActivity.getInstance().cartItemList)

                if (b.getString("ID").matches(c.getId())) {

                    ready.setVisibility(View.VISIBLE);
                    //get.setVisibility(View.GONE);
                }
        }

    }
}
