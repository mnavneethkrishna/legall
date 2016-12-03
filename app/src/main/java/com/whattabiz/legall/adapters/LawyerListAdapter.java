package com.whattabiz.legall.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.whattabiz.legall.models.AdvocateModel;
import com.whattabiz.legall.R;
import com.whattabiz.legall.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 8/3/2016.
 */

public class LawyerListAdapter extends RecyclerView.Adapter<LawyerListAdapter.MyViewHolder> {

    private List<AdvocateModel> advocateList;

    Context c;

    String url;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,rating,exp;
        CircleImageView image;
        LinearLayout main_view;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txt_name);
            rating = (TextView) view.findViewById(R.id.txt_rating);
            exp = (TextView) view.findViewById(R.id.txt_exp);
            image = (CircleImageView) view.findViewById(R.id.propic);
            main_view=(LinearLayout) view.findViewById(R.id.view_main);
        }
    }


    public LawyerListAdapter(List<AdvocateModel> advocateList) {
        this.advocateList = advocateList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_lawyer_list, parent, false);

        c = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AdvocateModel cView = advocateList.get(position);
        holder.name.setText(cView.getName());
        holder.rating.setText(Integer.valueOf(cView.getRating()).toString()+" cases");
        holder.exp.setText(cView.getExp().toString()+" Yrs. exp");





        //Setting on click listener for each row

        holder.main_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final AppCompatDialog dialog = new AppCompatDialog(c);
                dialog.setContentView(R.layout.dialog_advocate);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                CircleImageView propic = (CircleImageView) dialog.findViewById(R.id.propic);
                TextView name = (TextView) dialog.findViewById(R.id.txt_name);
                TextView rating = (TextView) dialog.findViewById(R.id.txt_rating);
                TextView exp = (TextView) dialog.findViewById(R.id.txt_exp);
                Button get = (Button) dialog.findViewById(R.id.btn_get);
                TextView city =(TextView) dialog.findViewById(R.id.txt_location);

                city.setText(cView.getCity());
                name.setText(cView.getName());
                rating.setText(Integer.valueOf(cView.getRating()).toString()+" Cases");
                exp.setText(cView.getExp().toString()+" Years experience");
                get.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        requestAppointment(cView.getId());

                        dialog.dismiss();












                    }
                });

                dialog.show();


            }
        });
    }



    public void requestAppointment(String lawyerid){

        final ProgressDialog pd = new ProgressDialog(c);
        pd.setMessage("Requesting Appointment ..");
        pd.show();

        url = "http://www.legall.co.in/web/appointment.php?key=Legall&lawyerId="+lawyerid+"&user_id="+ User.getInstance(c).Id;
        //String url = "http://data.colorado.gov/resource/4ykn-tg5h.json";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {




                        Log.i("Legall","Appointment URL : "+url);
                        Log.i("Legall","Appointment responce : "+response);

                        //Success handle response
                        try {
                            JSONObject obj = new JSONObject(response);

                            Integer status = obj.getInt("status");

                            if (status == 1){
                                pd.dismiss();

                                final AppCompatDialog Asuccess = new AppCompatDialog(c);
                                Asuccess.setContentView(R.layout.dialog_request_success);
                                Asuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                Asuccess.findViewById(R.id.btn_dismiss).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Asuccess.dismiss();
                                    }
                                });
                                Asuccess.show();

                            }else {
                                Toast.makeText(c, "Sorry, we couldn't submit your request . please try again after sometime.", Toast.LENGTH_LONG).show();
                            }


                        }
                        catch(JSONException ex) {
                            ex.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Log.e("Legall",error.toString());


                final AppCompatDialog dialog = new AppCompatDialog(c);
                dialog.setContentView(R.layout.dialog_no_internet);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                Button back =(Button) dialog.findViewById(R.id.btn_back);
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
        });

        // Add the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(c).add(stringRequest);



    }



    @Override
    public int getItemCount() {
        return advocateList.size();
    }
}

