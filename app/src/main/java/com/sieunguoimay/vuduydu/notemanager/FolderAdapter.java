package com.sieunguoimay.vuduydu.notemanager;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder>{
    public enum ViewType{FOLDER_ICON, FOLDER_NAME}
    public interface OnItemClickListener{
        void onItemClick(View view,int position, ViewType type);
    }
    List<Folder2> folderList;
    View.OnDragListener listener;
    Context context;
    private final OnItemClickListener itemClickListener;

    class FolderViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;
        public FolderViewHolder(View v){
            super(v);
            imageView = (ImageView)v.findViewById(R.id.recyclerView_imageView_folder);
            textView = (TextView)v.findViewById(R.id.recyclerView_textView_folder);
        }

        public void bind(final int position, final OnItemClickListener itemClickListener, View.OnDragListener listener){
            String name = folderList.get(position).name;
            textView.setText(name);
            imageView.setOnDragListener(listener);
            imageView.setTag(position);

            textView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    itemClickListener.onItemClick(v,position,ViewType.FOLDER_NAME);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    itemClickListener.onItemClick(v,position,ViewType.FOLDER_ICON);
                }
            });

        }

    }



    public FolderAdapter(List<Folder2> folderList,View.OnDragListener listener, OnItemClickListener itemClickListener){
        this.folderList = folderList;
        this.listener = listener;
        this.itemClickListener = itemClickListener;
    }
    public void changeFolder(List<Folder2>folderList){
        this.folderList = folderList;

    }
    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.folder_view_layout,parent,false);
        return new FolderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position){
        holder.bind(position,itemClickListener,this.listener);
    }
    @Override
    public int getItemCount(){
        return folderList.size();
    }
}
