package com.sieunguoimay.vuduydu.notemanager;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        RenameFolder.RenameFolderListener,
        FindFriendDialog.FindFriendListener,
        View.OnDragListener,FolderAdapter.OnItemClickListener
{

    private LinearLayout canvasView;
    private int trashViewOriginY = 0;
    private CardView trashView;
    private FloatingActionButton buttonNewFolder;
    private CardView buttonNewNote;
    private TextView textViewFolderPath;
    private CardView cardViewFolderPath;
    private ImageView imageViewUp;
    private ScrollView scrollViewMainScreen;
    private ImageView accountButton;
    private ImageView imageView_preloader;

    private CardView cardView_sharedSpace;
    private ImageView imageView_sharedSpace1;
    private ImageView imageView_sharedSpace2;

    private CardView button_People;
    private CardView bottomBar;
    private CardView cardView_peopleBar;

    private RecyclerView recyclerView_folder;
    private FolderAdapter folderAdapter;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;

    private RecyclerView recyclerView_PeopleBar;
    private PeopleAdapter peopleAdapter;

    private static final int REQUEST_CODE_NOTE_EDIT = 1000;
    private static final int REQUEST_CODE_SIGN_IN = 1001;



//    private List<Folder> folderStack = new ArrayList<>();

    private List<Folder2> folderList = new ArrayList<>();
    private List<Note> noteList = new ArrayList<>();


    private DatabaseReference mCurrentUserRoot;
    private DatabaseReference mNotebookRoot;
    private DatabaseReference mSharedNotebookRoot;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseReference=null;
    private ValueEventListener listener;
    private String folderPath="";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        canvasView = (LinearLayout)findViewById(R.id.canvasView);
        trashView = (CardView )findViewById(R.id.trashView);
        buttonNewFolder = (FloatingActionButton)findViewById(R.id.button_newFolder);
        buttonNewNote = (CardView) findViewById(R.id.cardView_createNewNote);
        textViewFolderPath = (TextView)findViewById(R.id.textView_folderPath);
        cardViewFolderPath = (CardView)findViewById(R.id.cardView_folderPath);
        imageViewUp = (ImageView)findViewById(R.id.imageView_up);
        scrollViewMainScreen = (ScrollView)findViewById(R.id.scrollView_mainScreen);
        accountButton = (ImageView) findViewById(R.id.accountButton);
        button_People = (CardView)findViewById(R.id.button_People);
        bottomBar = (CardView)findViewById(R.id.bottomBar);
        cardView_peopleBar = (CardView)findViewById(R.id.cardView_peopleBar);
        cardView_sharedSpace = (CardView)findViewById(R.id.cardView_sharedSpace);
        imageView_sharedSpace1 = (ImageView)findViewById(R.id.imageView_sharedSpace1);
        imageView_sharedSpace2 = (ImageView)findViewById(R.id.imageView_sharedSpace2);
        imageView_preloader = (ImageView)findViewById(R.id.imageView_preloader);

        cardView_peopleBar.post(new Runnable() {
            @Override
            public void run() {
                cardView_peopleBar.animate().translationY(cardView_peopleBar.getHeight());
            }
        });
        button_People.setTag("hide");
        button_People.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(!button_People.getTag().toString().equals("show")) {
                    button_People.setTag("show");
                    cardView_peopleBar.animate().translationY(0);
                    buttonNewFolder.animate().translationY(-cardView_peopleBar.getHeight());
                    trashViewOriginY = -cardView_peopleBar.getHeight();
                }else{
                    button_People.setTag("hide");
                    cardView_peopleBar.animate().translationY(cardView_peopleBar.getHeight());
                    buttonNewFolder.animate().translationY(0);
                    trashViewOriginY = 0;
                }
            }
        });

        //status bar icon color visible dark instead of white. :V
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (buttonNewFolder!= null) {
                buttonNewFolder.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }


        trashView.setOnDragListener(this);
        //on new folder button clicked
        buttonNewFolder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popUpToCreateNewFolder(RenameFolder.PopupType.NEW_FOLDER,-1);
            }
        });

        buttonNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNoteEditorToCreate(v);
            }
        });
        buttonNewFolder.setOnDragListener(this);
        buttonNewFolder.setTag("default_listener");

        findViewById(R.id.cardView_buttonUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goUpFolder(v);
            }
        });

        initFolderRecyclerView();
        initNoteRecyclerView();
        initPeopleBarRecyclerView();

        imageViewUp.setImageResource(R.drawable.ic_arrow_upward_black_24dp_2);
        cardViewFolderPath.post(new Runnable() {
            @Override
            public void run() {
                cardViewFolderPath.animate().translationX(-cardViewFolderPath.getWidth());
            }
        });
        cardView_sharedSpace.post(new Runnable() {
            @Override
            public void run() {
                cardView_sharedSpace.animate().translationX(cardView_sharedSpace.getWidth());
                cardView_sharedSpace.setTag("hide");
            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,StartActivity.class));
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
        checkAuthentication();

    }

    //for the first time
    private void checkAuthentication(){
        if(mAuth.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this,StartActivity.class);
            startActivity(intent);
        }else{
            //ok
            String name = mAuth.getCurrentUser().getDisplayName();
            Toast.makeText(MainActivity.this,"Welcome back "+name,Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut(){
        mAuth.signOut();
        if(StartActivity.getGoogleSignInClient()==null){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignIn.getClient(this, gso).signOut();
        }else{
            StartActivity.getGoogleSignInClient().signOut();
        }
        Intent intent = new Intent(MainActivity.this,StartActivity.class);
        startActivityForResult(intent,REQUEST_CODE_SIGN_IN);
        firstTime = true;
    }








    private void globalTask(){
        imageView_preloader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        recyclerView_folder.setVisibility(View.INVISIBLE);
    }


    private void createNewNoteIntoFolder(DatabaseReference reference, Note note,int folder){
        reference.child("/folders/"+folder+"/notes").setValue(note.toMap());
        globalTask();
    }
    private void createNewNote(DatabaseReference reference,Note note){
        reference.child("/notes").push().setValue(note.toMap());
        globalTask();
    }
    private void editNote(DatabaseReference reference, Note note){
        reference.child("/notes").updateChildren(note.toMap());
        globalTask();
    }
    private void putFolderInto(DatabaseReference reference, String key,Folder2 newFolder){
        reference.child("/folders/"+key+"/folders").setValue(newFolder.toMap());
        globalTask();
    }

    private void createNewFolder(DatabaseReference reference,Folder2 newFolder){
        reference.child("/folders").push().setValue(newFolder.toMap());
        globalTask();
    }

    private void renameFolder(DatabaseReference reference, String key, String folderName){
        reference.child("/folders/"+key+"/name").setValue(folderName);
        globalTask();
    }
    private void deleteNote(DatabaseReference reference, String key){
        reference.child("/notes/"+key).removeValue();
        globalTask();
    }
    private void deleteFolder(DatabaseReference reference,String key){
        reference.child("/folders/"+key).removeValue();
        globalTask();
    }







    private void changeFirebaseReference(DatabaseReference reference){
        mFirebaseReference.removeEventListener(listener);
        mFirebaseReference = reference;
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mFirebaseReference.addValueEventListener(listener);
    }

















    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_NOTE_EDIT) &&
                (resultCode== Activity.RESULT_OK)) {
            String note = data.getStringExtra("note");
            int index = data.getIntExtra("note_id",-1);
            String created_date = data.getStringExtra("created_date");
            String edited_date = data.getStringExtra("edited_date");
            String key = data.getStringExtra("key");
            if(!note.isEmpty()){
                if(index==-1){
                    //Note newNote = new Note(note,created_date);
                    //Toast.makeText(MainActivity.this,"new note "+newNote.toMap().toString(),Toast.LENGTH_SHORT).show();
                    createNewNote(mFirebaseReference,new Note(note,created_date));
                }else{
                    Note newNote = new Note(note,created_date);
                    newNote.edited_date = edited_date;
                    newNote.key = key;
                    editNote(mFirebaseReference,newNote);
                }
                noteAdapter.notifyDataSetChanged();
            }
            else{
                Toast.makeText(MainActivity.this,"Empty note",Toast.LENGTH_SHORT).show();
            }
        }
    }







    private static boolean firstTime = true;
    @Override
    public void onStart(){
        super.onStart();
        if(firstTime) {
            getUserInformation();
            refreshFolder();
            firstTime = false;
        }
    }

    private String imageUrl = "";

    private void getUserInformation(){
        if(mAuth.getCurrentUser()!=null){

            FirebaseUser user = mAuth.getCurrentUser();
            imageUrl = user.getPhotoUrl().toString();
            if(!imageUrl.isEmpty())
                Picasso.with(MainActivity.this).load(imageUrl)
                    .into(accountButton);

            mSharedNotebookRoot =FirebaseDatabase.getInstance().getReference("/shared_notebooks");
            mNotebookRoot = FirebaseDatabase.getInstance().getReference("/notebooks");
            mNotebookRoot.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getPeopleData(dataSnapshot);
                    if(friendData!=null)
                        setFriendList(friendData);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mCurrentUserRoot = FirebaseDatabase.getInstance().getReference("/notebooks/"+mAuth.getUid());
            mFirebaseReference = mCurrentUserRoot;
            listener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        Map<String,Object>map = new HashMap<>();
                        map.put("id",0);
                        map.put("name","");
                        map.put("folder_num",1);
                        map.put("note_num",1);


                        if(mAuth.getCurrentUser().getDisplayName()!=null) map.put("user_name",mAuth.getCurrentUser().getDisplayName());
                        if(mAuth.getCurrentUser().getEmail()!=null) map.put("email_id",mAuth.getCurrentUser().getEmail());
                        if(mAuth.getCurrentUser().getPhoneNumber()!=null) map.put("phone_no",mAuth.getCurrentUser().getPhoneNumber());
                        Date date = new Date();
                        map.put("login_date",date.toString());



                        map.put("/folders/0/name","Tab here to rename folder");
                        map.put("/folders/0/id",0);
                        map.put("/folders/0/folder_num",0);
                        map.put("/folders/0/note_num",0);

                        map.put("notes/0/note","This is a sample note. Touch to see and edit it. Hope you enjoy the app and have a great day...");
                        map.put("notes/0/created_date",DateFormat.getInstance().format(date));

                        mCurrentUserRoot.updateChildren(map);

                    }else{
                        FetchData(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
            mFirebaseReference.addValueEventListener(listener);
        }
    }

    private void FetchData(DataSnapshot dataSnapshot){
        updateMissingData(dataSnapshot,imageUrl,mAuth.getCurrentUser().getEmail());
        new FetchData(dataSnapshot,folderList,noteList,this).execute();
    }
    public void AfterFetchingData(){
        folderAdapter.notifyDataSetChanged();
        noteAdapter.notifyDataSetChanged();
        imageView_preloader.setVisibility(View.GONE);
        recyclerView_folder.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private DataSnapshot friendData = null;
    private void updateMissingData(DataSnapshot dataSnapshot, String imageUrl,String email){
        boolean photo_existed = false;
        boolean email_existed = false;
        for(DataSnapshot a:dataSnapshot.getChildren()){
            if(a.getKey().equals("photo_url"))
                photo_existed = true;
            else if(a.getKey().equals("email_id"))
                email_existed = true;
            else if(a.getKey().equals("friends")){
                friendData = a;
                setFriendList(a);
            }
        }
        if(!photo_existed)
            mCurrentUserRoot.child("photo_url").setValue(imageUrl);
        if(!email_existed)
            mCurrentUserRoot.child("email_id").setValue(email);
    }





    private void initFolderRecyclerView(){
        recyclerView_folder = (RecyclerView)findViewById(R.id.recyclerView_folder);
        folderAdapter = new FolderAdapter(folderList,this,this);
        RecyclerView.LayoutManager layoutManager_folder = new GridLayoutManager(getApplicationContext(),4);
        recyclerView_folder.setLayoutManager(layoutManager_folder);
        recyclerView_folder.setItemAnimator(new DefaultItemAnimator());
        recyclerView_folder.setAdapter(folderAdapter);
        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView_folder.setLayoutAnimation(animation);
        recyclerView_folder.addOnItemTouchListener(new FolderTouchListener(getApplicationContext(), recyclerView_folder,
                new FolderTouchListener.ClickListener() {
                    @Override
                    public void onLongClick(View view, int position) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(position);
                        ClipData data = ClipData.newPlainText("id",sb.toString());
                        ClipData.Item item_type = new ClipData.Item("folder");
                        data.addItem(item_type);
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(data,shadowBuilder,view,0);
                        view.setVisibility(View.INVISIBLE);

                        trashView.setVisibility(View.VISIBLE);
                        trashView.setAlpha(0);
                        trashView.animate().translationY(-trashView.getHeight()+trashViewOriginY).alpha(1.0f);

                    }
                }));
        folderAdapter.notifyDataSetChanged();

    }
    private void initNoteRecyclerView(){
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        noteAdapter = new NoteAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        recyclerView.setLayoutAnimation(animation);
        recyclerView.addOnItemTouchListener(new NoteTouchListener(getApplicationContext(),recyclerView, new NoteTouchListener.ClickListener(){

            @Override
            public void onClick(View view, int position){
                lauchNoteEditorToEdit(position);
            }
            @Override
            public void onLongClick(View view, int position, final MotionEvent event){
                drawDrag(view,position,event);
            }
        }

        ));
        noteAdapter.notifyDataSetChanged();
    }

    private List<People>peopleList = new ArrayList<>();
    private List<People>friendList = new ArrayList<>();
    private void initPeopleBarRecyclerView(){
        recyclerView_PeopleBar = (RecyclerView)findViewById(R.id.recyclerView_PeopleBar);
        peopleAdapter = new PeopleAdapter(friendList );
        recyclerView_PeopleBar.setItemAnimator(new DefaultItemAnimator());
        recyclerView_PeopleBar.setAdapter(peopleAdapter);
        recyclerView_PeopleBar.addOnItemTouchListener(new PeopleTouchListener(getApplicationContext(), recyclerView_PeopleBar,
                new PeopleTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if(position==0){
                            FindFriendDialog findFriendDialog = new FindFriendDialog();
                            findFriendDialog.setPeopleList(peopleList);
                            findFriendDialog.show(getSupportFragmentManager(),"find new friend here");
                        }else{
                            if(!friendList.get(position).shared_key.isEmpty())
                                changeFirebaseReference(mSharedNotebookRoot.child(friendList.get(position).shared_key));
                            folderPath="Shared Space";
                            refreshFolder();
                            hidePeopleBar();
                            showSharedSpaceIcon(imageUrl,friendList.get(position).photo_url);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position, MotionEvent event) {

                    }
                }));

        friendList.add(new People());
        peopleAdapter.notifyDataSetChanged();
    }





    private void setFriendList(DataSnapshot dataSnapshot){
        if(!friendList.isEmpty())
            friendList.clear();
        friendList.add(new People());
        for(DataSnapshot a:dataSnapshot.getChildren()){//friends
            String key = a.getKey();
            People friend = null;
            for(People p:peopleList){
                if(key.equals(p.key)){
                    friend = p;
                    break;
                }
            }
            if(friend==null) continue;
            for(DataSnapshot b:a.getChildren()){//friend values
                if(b.getKey().equals("shared_key"))
                    friend.shared_key = b.getValue().toString();
            }
            friendList.add(friend);
        }
        peopleAdapter.notifyDataSetChanged();
        if(!friendList.isEmpty())
            findViewById(R.id.textView_noFriends).setVisibility(View.INVISIBLE);

    }


    @Override
    public void addFriend(People friend){

        DatabaseReference newSharedNotebook = mSharedNotebookRoot.push();

        Map<String,Object>sharedData = new HashMap<>();
        sharedData.put("created_date",(new Date()).toString());
        sharedData.put("user1",mAuth.getUid());
        sharedData.put("user2",friend.key);
        newSharedNotebook.setValue(sharedData);


        Map<String,Object>map= new HashMap<>();
        Map<String,Object>data=new HashMap<>();
        data.put("email_id",friend.email_id);
        data.put("date",(new Date()).toString());
        data.put("shared_key",newSharedNotebook.getKey());
        map.put("friends/"+friend.key,data);

        Map<String,Object>friendData = new HashMap<>();
        friendData.put("email_id",mAuth.getCurrentUser().getEmail());
        friendData.put("date",(new Date()).toString());
        friendData.put("shared_key",newSharedNotebook.getKey());
        Map<String,Object>friendMap =new HashMap<>();
        friendMap.put("/"+friend.key+"/friends/"+ mCurrentUserRoot.getKey(),friendData);

        mCurrentUserRoot.updateChildren(map);
        mNotebookRoot.updateChildren(friendMap);
    }
    void getPeopleData(DataSnapshot snapshot){
        if(!peopleList.isEmpty()) peopleList.clear();
        for(DataSnapshot user:snapshot.getChildren()){
            if(user.getKey().equals(mCurrentUserRoot.getKey()))
                continue;
            peopleList.add(People.getData(user));
        }
    }





    @Override
    public boolean onTouchEvent(MotionEvent event){
        View view = this.getCurrentFocus();
        //hide the people bar
        if(view!=cardView_peopleBar){
            hidePeopleBar();
        }
        return true;
    }
    private void hidePeopleBar(){
        if(!button_People.getTag().equals("hide")){
            button_People.setTag("hide");
            cardView_peopleBar.animate().translationY(cardView_peopleBar.getHeight());
            buttonNewFolder.animate().translationY(0);
            trashViewOriginY = 0;

        }
    }
    private void showSharedSpaceIcon(String url1,String url2){
        Picasso.with(MainActivity.this).load(url1).into(imageView_sharedSpace1);
        Picasso.with(MainActivity.this).load(url2).into(imageView_sharedSpace2);
        cardView_sharedSpace.setTag("show");
        cardView_sharedSpace.animate().translationX(0);
    }
    private void hideSharedSpaceIcon(){
        if(cardView_sharedSpace.getTag().equals("show")){
            cardView_sharedSpace.setTag("hide");
            cardView_sharedSpace.animate().translationX(cardView_sharedSpace.getWidth());
        }
    }


    void drawDrag(final View view, int position, MotionEvent event){
        StringBuilder sb = new StringBuilder();
        sb.append(position);
        ClipData data = ClipData.newPlainText("id",sb.toString());
        ClipData.Item item_id = new ClipData.Item("note");
        data.addItem(item_id);

        trashView.setVisibility(View.VISIBLE);
        trashView.setAlpha(0);
        trashView.animate().translationY(-trashView.getHeight()+trashViewOriginY).alpha(1.0f);
        final View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(canvasView){
            @Override
            public void onDrawShadow(Canvas canvas){
                View v = this.getView();
                super.onDrawShadow(canvas);
            }
        };
        view.startDrag(data,shadowBuilder,view,position);
        view.setVisibility(View.INVISIBLE);
    }




    @Override
    public boolean onDrag(View view, DragEvent event){

        switch(event.getAction()){
            case DragEvent.ACTION_DROP:
                if(!view.getTag().toString().equals("default_listener")){


                        String id = event.getClipData().getItemAt(0).getText().toString();
                        String type = event.getClipData().getItemAt(1).getText().toString();

                        view.invalidate();

                        int drag_id = Integer.parseInt(id);


                    if(view.getTag().toString().equals("trash")){

                        Toast.makeText(MainActivity.this,"Deleted",Toast.LENGTH_SHORT).show();

                        if(type.equals("note")) {
                            deleteNote(mFirebaseReference,noteList.get(drag_id).key);
                            noteAdapter.notifyDataSetChanged();
                        }else if(type.equals("folder")){
                            deleteFolder(mFirebaseReference,folderList.get(drag_id).key);
                            folderAdapter.notifyDataSetChanged();
                        }
                    }else {

                        int folder_id = Integer.parseInt(view.getTag().toString());

                        Toast.makeText(MainActivity.this, "Drop Successfully " + drag_id + " -> " + folderList.get(folder_id).name,
                                Toast.LENGTH_SHORT).show();

                        //make it here.
                        if (type.equals("note")) {
                            createNewNoteIntoFolder(mFirebaseReference,noteList.get(drag_id),folder_id);
                            deleteNote(mFirebaseReference,noteList.get(drag_id).key);
                            noteAdapter.notifyDataSetChanged();

                        } else if (type.equals("folder")) {
                            putFolderInto(mFirebaseReference,folderList.get(folder_id).key,folderList.get(drag_id));
                            deleteFolder(mFirebaseReference,folderList.get(drag_id).key);
                            folderAdapter.notifyDataSetChanged();
                        }
                    }

                    view.invalidate();
                    ((View) event.getLocalState()).setVisibility(View.VISIBLE);
                    trashView.animate().translationY(trashView.getHeight()+trashViewOriginY).alpha(0.0f);
                }

                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                view.invalidate();
                if(view.getTag().toString().equals("default_listener")||
                        !event.getResult()) {
                    ((View) event.getLocalState()).setVisibility(View.VISIBLE);
                    trashView.animate().translationY(trashView.getHeight()+trashViewOriginY).alpha(0.0f);
                }
            break;
        }

        return true;
    }








    private void goUpFolder(View v){
        if(!folderPath.equals("")){
            if(folderPath.equals("Shared Space")){
                changeFirebaseReference(mCurrentUserRoot);
                hideSharedSpaceIcon();
            }
             else
                 changeFirebaseReference(mFirebaseReference.getParent().getParent());

            int i = folderPath.lastIndexOf('/');
            if(i==-1)
                folderPath="";
            else
                folderPath = folderPath.substring(0,folderPath.lastIndexOf('/'));

            refreshFolder();
        }
    }


    private void launchNoteEditorToCreate(View v){
        Intent intent = new Intent(v.getContext(), NoteEdit.class);
        startActivityForResult(intent, REQUEST_CODE_NOTE_EDIT);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    private void lauchNoteEditorToEdit(int position){
        Bundle bundle = new Bundle();
        bundle.putInt("note_id",position);
        bundle.putString("note_content",noteList.get(position).note);
        bundle.putString("created_date",noteList.get(position).created_date);
        bundle.putString("key",noteList.get(position).key);



        Intent intent = new Intent(MainActivity.this, NoteEdit.class);
        intent.putExtra("existedNote",bundle);

        startActivityForResult(intent, REQUEST_CODE_NOTE_EDIT);

        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void refreshFolder(){
        folderAdapter.notifyDataSetChanged();
        noteAdapter.notifyDataSetChanged();
        if(folderPath.isEmpty()){
            cardViewFolderPath.animate().translationX(-cardViewFolderPath.getWidth());
        }else{
            cardViewFolderPath.animate().translationX(0);
        }
        textViewFolderPath.setText(folderPath);
        globalTask();
    }



    private void openFolder(int position){
        if(folderPath.isEmpty())
            folderPath=folderList.get(position).name;
        else
            folderPath=folderPath+"/"+folderList.get(position).name;

        changeFirebaseReference(mFirebaseReference.child("folders/"+folderList.get(position).key));
        refreshFolder();
    }




    @Override
    public void onItemClick(View view,int position, FolderAdapter.ViewType type){
        if(type == FolderAdapter.ViewType.FOLDER_NAME)
            popUpToCreateNewFolder(RenameFolder.PopupType.RENAME,position);
        else if(type == FolderAdapter.ViewType.FOLDER_ICON){
            view.animate();
            openFolder(position);

        }

    }

    public void popUpToCreateNewFolder(RenameFolder.PopupType type, int position){
        RenameFolder renameFolder = new RenameFolder();
        renameFolder.setData(type, position);
        renameFolder.show(getSupportFragmentManager(),"rename folder dialog");
    }
    @Override
    public void applyFolderName(String folderName, RenameFolder.PopupType type, int position) {
        if(folderName.isEmpty()){
            folderName = "Untitled";
        }

        if(type== RenameFolder.PopupType.NEW_FOLDER){
            Folder2 newFolder = new Folder2();
            newFolder.name = folderName;
            newFolder.created_date = (new Date()).toString();
            newFolder.edited_date = newFolder.created_date;
            createNewFolder(mFirebaseReference,newFolder);
        }else if(type == RenameFolder.PopupType.RENAME){
            renameFolder(mFirebaseReference,folderList.get(position).key,folderName);
        }
        folderAdapter.notifyDataSetChanged();
        refreshFolder();
    }

    public static Bitmap getBitmapFromaURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }



    //to serve for CardView Riffle effect
    public Drawable getSelectedItemDrawable() {
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray ta = MainActivity.this.obtainStyledAttributes(attrs);
        Drawable selectedItemDrawable = ta.getDrawable(0);
        ta.recycle();
        return selectedItemDrawable;
    }

    //dp to pixels
    public int DPToPX(int dps){
        final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }
    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
