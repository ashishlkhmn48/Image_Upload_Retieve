package com.ashishlakhmani.imageuploadretieve;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ViewContactAdapter extends RecyclerView.Adapter {

    private JSONArray array;
    private Context context;

    public ViewContactAdapter(JSONArray array,Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout, parent, false);
        return new ViewContactAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final JSONObject object;
        try {
            object = array.getJSONObject(position);
            ((MyViewHolder) holder).name.setText(object.getString("name"));
            ((MyViewHolder) holder).email.setText(object.getString("email"));
            String url = "http://ashishlakhmani.000webhostapp.com/upload/" + object.getString("name") + ".jpg";
            Picasso.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder_album)
                    .into(((MyViewHolder) holder).pic);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // initialize the item view's

        TextView name;
        TextView email;
        ImageView pic;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
