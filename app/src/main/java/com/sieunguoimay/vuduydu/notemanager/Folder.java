package com.sieunguoimay.vuduydu.notemanager;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class Note{
    public String note;
    public String created_date;
    public String edited_date;
    public String key="";
    public Note(){

    }
    public Note(String note, String created_date){
        this.note = note;
        this.created_date = created_date;
        this.edited_date = created_date;
    }
    public Map<String ,Object>toMap(){
        Map<String,Object>map = new HashMap<>();
        map.put("note",note);
        map.put("created_date", created_date);
        map.put("edited_date", edited_date);

        if(key.isEmpty())
            return map;

        Map<String ,Object> note = new HashMap<>();
        note.put(key,map);
        return note;
    }
}
class Folder2{
    public String name;
    public String created_date;
    public String edited_date;
    public String key="";

    public Map<String ,Object>toMap(){
        Map<String,Object>map = new HashMap<>();
        map.put("name",name);
        map.put("created_date", created_date);
        map.put("edited_date", edited_date);
        if(key.isEmpty())
            return map;

        Map<String ,Object> note = new HashMap<>();
        note.put(key,map);
        return note;
    }
}

public class Folder {

    public static void getData(DataSnapshot snapshot, List<Note>noteList, List<Folder2>folderList){
        noteList.clear();
        folderList.clear();
        for(DataSnapshot a:snapshot.getChildren()){
            if(a.getKey().equals("notes")){

                for(DataSnapshot b:a.getChildren()) {
                    Note newNote = new Note();
                    for (DataSnapshot c : b.getChildren()) {
                        if (c.getKey().equals("created_date")) {
                            newNote.created_date = c.getValue().toString();
                        } else if (c.getKey().equals("edited_date"))
                            newNote.edited_date = c.getValue().toString();
                        else if (c.getKey().equals("note"))
                            newNote.note = c.getValue().toString();
                    }
                    newNote.key = b.getKey();
                    noteList.add(0,newNote);
                }

            }
            else if(a.getKey().equals("folders")){

                for(DataSnapshot b:a.getChildren()) {
                    Folder2 newFolder = new Folder2();
                    for (DataSnapshot c : b.getChildren()) {
                        if (c.getKey().equals("created_date")) {
                            newFolder .created_date = c.getValue().toString();
                        } else if (c.getKey().equals("edited_date"))
                            newFolder .edited_date = c.getValue().toString();
                        else if (c.getKey().equals("name"))
                            newFolder .name = c.getValue().toString();
                    }
                    newFolder .key = b.getKey();
                    folderList.add(newFolder);
                }

            }
        }
    }


    private List<Note> m_noteList = new ArrayList<>();
    private List<Folder> m_folderList = new ArrayList<>();
    private String m_name;
    private int m_id;
    public void setId(int id){m_id = id;}
    public int getId(){return m_id;}
    public Folder(String name){
        m_name = name;
    }
    public Folder(Folder other){
        m_folderList = other.m_folderList;
        m_folderList = other.m_folderList;
        m_name = other.m_name;
        m_id = other.m_id;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        map.put("name",m_name);
        map.put("id",m_id);
        Map<String,Object>noteList = new HashMap<>();
        for(int i =0; i<m_noteList.size(); i++)
            noteList.put(""+i,m_noteList.get(i));
        map.put("notes",noteList);
        map.put("note_num",m_noteList.size());

        Map<String,Object>folderList = new HashMap<>();
        for(int i = 0; i<m_folderList.size(); i++){
            folderList.put(""+i,m_folderList.get(i).toMap());
        }
        map.put("folder_num",m_folderList.size());
        map.put("folders",folderList);
        return map;
    }
    public Folder getData(@NonNull DataSnapshot dataSnapshot){
        int folderNum = 0;
        int noteNum = 0;
        DataSnapshot folders = null;
        DataSnapshot notes = null;

        for(DataSnapshot a:dataSnapshot.getChildren()){
            if(a.getKey().equals("name"))
                m_name =(String)a.getValue().toString();
            else if(a.getKey().equals("id")){
                String id =(String)a.getValue().toString();
                m_id = Integer.parseInt(id);
            }else if(a.getKey().equals("folder_num")){
                String folder_num=(a.getValue()).toString();
                folderNum = Integer.parseInt(folder_num);

            }else if(a.getKey().equals("note_num")){
                String note_num=(a.getValue()).toString();
                noteNum = Integer.parseInt(note_num);
            }else if(a.getKey().equals("folders"))
                folders = a;
            else if(a.getKey().equals("notes"))
                notes = a ;
        }

        if(folders!=null)
            for(DataSnapshot a:folders.getChildren()){
                Folder newFolder = new Folder("");
                newFolder.getData(a);
                m_folderList.add(newFolder);
            }

        if(notes!=null)
            for(DataSnapshot a:notes.getChildren()){
                String created_date ="";
                String edited_date ="";
                String note="";
                for(DataSnapshot b:a.getChildren()){
                    if(b.getKey().equals("created_date")){
                        created_date = b.getValue().toString();
                    }else if(b.getKey().equals("edited_date"))
                        edited_date = b.getValue().toString();
                    else if(b.getKey().equals("note"))
                        note = b.getValue().toString();

                }
                Note newNote = new Note(note,created_date);
                newNote.edited_date = edited_date;
                newNote.key = a.getKey();
                m_noteList.add(newNote);

            }
        return this;
    }
    public void rename(String name){
        m_name = name;
    }
    public String getName(){
        return m_name;
    }
    void addFolder(String name){
        m_folderList.add(new Folder(name));
    }
    void addFolder(Folder folder){
        m_folderList.add(folder);
    }
    public List<Folder>getFolderList(){
        return m_folderList;
    }
    public Folder getFolder(int index){
        return m_folderList.get(index);
    }


    public void addNote(String note,String date){
        m_noteList.add(new Note(note,date));
    }
    public void addNote(Note note){
        m_noteList.add(note);
    }
    public List<Note> getNoteList(){
        return m_noteList;
    }
    public Note getNote(int index){
        return m_noteList.get(index);
    }
    public void changeNote(int index, String newNote){
        m_noteList.get(index).note = newNote;
    }
    public void changeNote(int index, Note newNote){
        m_noteList.set(index, newNote);
    }
}
