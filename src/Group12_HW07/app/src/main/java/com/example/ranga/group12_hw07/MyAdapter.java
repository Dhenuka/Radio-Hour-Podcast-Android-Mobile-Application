package com.example.ranga.group12_hw07;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ranga on 3/10/2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Podcast> mDataset;
    private final OnItemClickListener listener;
    static IDATA acticity;
    SimpleDateFormat parser=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        private TextView title;
        private TextView pubDate;
        private ImageButton ib;
       private  ImageView iv;


        public ViewHolder(View v) {
            super(v);
            if(MainActivity.isViewStateLOG) {
                title = (TextView) v.findViewById(R.id.linearLayoutID);
                pubDate = (TextView) v.findViewById(R.id.pubDateID);
                iv = (ImageView) v.findViewById(R.id.episodeIVID);
                ib = (ImageButton) v.findViewById(R.id.playIVID);
                ib.setOnClickListener(this);

            }else if(!MainActivity.isViewStateLOG)
            {
                title = (TextView) v.findViewById(R.id.gridTitleID);
                iv = (ImageView) v.findViewById(R.id.gridIV);
                iv.setOnClickListener(this);
            }
        }

        public void bind(final Podcast pod, final OnItemClickListener listener) {
            if(MainActivity.isViewStateLOG) {
                title.setText(pod.getTitle());
                String input = pod.getPdate();
                input = input.substring(0,input.length()-6);
                Log.d("demo",input+"");

                SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                Date date = null;
                try {
                    date = parser.parse(input);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat formatter = new SimpleDateFormat("EEE,d,MMM yyyy");
                String formattedDate = formatter.format(date);
                pubDate.setText(formattedDate);
                Picasso.with(iv.getContext()).load(pod.getImage()).into(iv);
            }else if(!MainActivity.isViewStateLOG)
            {
                title.setText(pod.getTitle());
                Picasso.with(iv.getContext()).load(pod.getImage()).into(iv);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(pod);
                }
            });





        }

        @Override
        public void onClick(View view) {
            if(view.getId()==R.id.gridIV||view.getId()==R.id.playIVID){
                try {
                    acticity.onReturn(getAdapterPosition());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


      /* *//* @Override
        public void onClick(View view) {
            if(view.getId()==ib.getId())
            {

            } else {
                listener1.onItemClick(myPod);
            }
        }*/
    }
    public interface IDATA{
        void onReturn(int id) throws IOException;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(IDATA acticity,ArrayList<Podcast> myDataset,OnItemClickListener listener1) {
        mDataset = myDataset;
        this.listener = listener1;
        this.acticity=acticity;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
    /*    RelativeLayout l = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, null, false);
    */
        RelativeLayout l = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(MainActivity.isViewStateLOG?R.layout.item_layout:R.layout.item_grid_layout, null, false);
        ViewHolder vh=null;

     /*  TextView titleView = (TextView) l.findViewById(R.id.titleID);
       Log.d("demo",titleView.getText()+" tv");

       // LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent);
        // set the view's size, margins, paddings and layout parameters
        if((titleView.getParent()!=null)){
            ((ViewGroup)titleView.getParent()).removeView(titleView);
  */        vh = new ViewHolder(l);
       // }

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bind(mDataset.get(position), listener);

       /* if(MainActivity.isViewStateLOG) {
            holder.title.setText(mDataset.get(position).getTitle());
            String input = mDataset.get(position).getPdate();
            input = input.substring(0,input.length()-6);
            Log.d("demo",input+"");
            SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = null;
            try {
                date = parser.parse(input);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("EEE,d,MMM yyyy");
            String formattedDate = formatter.format(date);
            holder.pubDate.setText(formattedDate);
            Picasso.with(holder.iv.getContext()).load(mDataset.get(position).getImage()).into(holder.iv);
        }else if(!MainActivity.isViewStateLOG)
        {
            holder.title.setText(mDataset.get(position).getTitle());
            Picasso.with(holder.iv.getContext()).load(mDataset.get(position).getImage()).into(holder.iv);
        }*/


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Podcast pod);
    }

}
