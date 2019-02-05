package com.sieunguoimay.vuduydu.notemanager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;

    class NoteViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView textView_EditedDate;
        public NoteViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.recyclerView_textView);
            textView_EditedDate = v.findViewById(R.id.textView_EditedDate);
        }
    }


    public NoteAdapter( List<Note> notes) {
        this.noteList = notes;
    }

    public void changeNoteList(List<Note> newList){
        this.noteList = newList;
    }




    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_view_layout,parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position){
        String note = noteList.get(position).note;
        holder.textView.setText(note);
        holder.textView_EditedDate.setText(noteList.get(position).edited_date);
    }

    @Override
    public int getItemCount(){
        return noteList.size();
    }

}
