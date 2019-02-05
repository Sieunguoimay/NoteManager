package com.sieunguoimay.vuduydu.notemanager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.squareup.picasso.Picasso;

import java.util.BitSet;
import java.util.List;

class People {
    public String name="";
    public String photo_url="";
    public String email_id="";
    public String key="";
    public String shared_key="";//only friended people have shared_key
    public static People getData(@NonNull DataSnapshot dataSnapshot){
        People newPeople = new People();
        newPeople.key = dataSnapshot.getKey();
        for(DataSnapshot a:dataSnapshot.getChildren()){
            if(a.getKey().equals("user_name"))
                newPeople.name =(String)a.getValue().toString();

            if(a.getKey().equals("photo_url"))
                newPeople.photo_url = (String)a.getValue().toString();

            if(a.getKey().equals("email_id"))
                newPeople.email_id=(String)a.getValue().toString();

        }

        return newPeople;
    }
}

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {

    private List<People> PeopleList;
    private Context context;
    class PeopleViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView_people;
        TextView textView_people;
        public PeopleViewHolder(View v){
            super(v);
            imageView_people = v.findViewById(R.id.imageView_people);
            textView_people = v.findViewById(R.id.textView_peopleName);
        }
    }


    public PeopleAdapter( List<People> Peoples) {
        this.PeopleList = Peoples;
    }

    public void changePeopleList(List<People> newList){
        this.PeopleList = newList;
    }


    @Override
    public int getItemViewType(int position){
        if(position==0)
            return 1;
        return 0;
    }


    @Override
    public PeopleViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context = parent.getContext();
        switch (viewType){
            case 0:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.people_view_layout,parent, false);
                return new PeopleViewHolder(view);
            case 1:
                View view2 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.people_search_view_layout,parent, false);
                return new PeopleViewHolder(view2);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(PeopleViewHolder holder, int position){
        if(holder.getItemViewType()==0){
            if(!PeopleList.get(position).photo_url.equals(""))
                Picasso.with(context).load(PeopleList.get(position).photo_url).into(holder.imageView_people);

//            holder.imageView_people.setImageBitmap(MainActivity.getBitmapFromURL(PeopleList.get(position).photo_url));
            holder.textView_people.setText(PeopleList.get(position).name);
        }
    }

    @Override
    public int getItemCount(){
        return PeopleList.size();
    }

}
