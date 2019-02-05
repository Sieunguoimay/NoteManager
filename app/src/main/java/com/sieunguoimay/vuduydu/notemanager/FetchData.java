package com.sieunguoimay.vuduydu.notemanager;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class FetchData extends AsyncTask<Void,Void, Void> {
    DataSnapshot dataSnapshot;
    List<Folder2>folderList;
    List<Note>noteList;

    MainActivity mainActivity;

    public FetchData(DataSnapshot dataSnapshot,List<Folder2>folderList,List<Note>noteList
                     ,MainActivity mainActivity){
        this.dataSnapshot = dataSnapshot;
        this.folderList = folderList;
        this.noteList = noteList;

        this.mainActivity = mainActivity;
    }

    protected Void doInBackground(Void... lists){
        Folder.getData(dataSnapshot,noteList,folderList);
        return null;
    }
    protected void onProgressUpdate(Void... progress){

    }
    protected void onPostExecute(Void result){
        mainActivity.AfterFetchingData();
    }
}
