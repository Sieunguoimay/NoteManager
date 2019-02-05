package com.sieunguoimay.vuduydu.notemanager;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FindFriendDialog extends AppCompatDialogFragment {
    private FindFriendListener listener;
    private List<People> peopleList;
    private People foundFriend=null;
    private boolean added = false;
    private EditText editText_searchFriend;
    private CardView cardView_foundFriend;
    private CardView cardView_search;
    private CardView cardView_addFriend;
    private ImageView imageView_friendPhoto;
    private TextView textView_friendName;
    private ImageView imageView_addFriend;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.find_friend_dialog,null);

        builder.setView(view)
            .setTitle("Find Friends")
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        editText_searchFriend = view.findViewById(R.id.editText_searchFriend);
        cardView_search = view.findViewById(R.id.cardView_search);

        cardView_foundFriend = view.findViewById(R.id.cardView_foundFriend);
        imageView_friendPhoto = view.findViewById(R.id.imageView_friendPhoto);
        textView_friendName = view.findViewById(R.id.textView_friendName);
        cardView_addFriend = view.findViewById(R.id.cardView_addFriend);
        imageView_addFriend= view.findViewById(R.id.imageView_addFriend);

        cardView_search.setVisibility(View.INVISIBLE);
        cardView_foundFriend.setVisibility(View.GONE);
        cardView_addFriend.setVisibility(View.INVISIBLE);

        editText_searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //show the search button
                if(s.toString().isEmpty()) {
                    cardView_search.setVisibility(View.INVISIBLE);
                    cardView_foundFriend.setVisibility(View.GONE);
                }
                else
                    cardView_search.setVisibility(View.VISIBLE);

            }
        });
        cardView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriend();
            }
        });
        cardView_addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!added){
                    listener.addFriend(foundFriend);
                    imageView_addFriend.animate().scaleX(1.1f).scaleY(1.1f);
                    imageView_addFriend.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_check_black_24dp));
                    imageView_addFriend.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            added = true;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }else{
                    dismiss();
                }
            }
        });
        return builder.create();
    }
    private void searchFriend(){
        String email = editText_searchFriend.getText().toString();
        People people = null;
        for(People p:peopleList){
            if(p.email_id.equals(email)){
                people = p;
                break;
            }
        }
        if(people!=null){
            Picasso.with(getContext()).load(people.photo_url)
                    .into(imageView_friendPhoto);
            textView_friendName.setText(people.name);
            foundFriend = people;
            cardView_addFriend.setVisibility(View.VISIBLE);
        }else{
            imageView_friendPhoto.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_person_black_24dp));
            textView_friendName.setText("No friend found.");
        }
        cardView_foundFriend.setVisibility(View.VISIBLE);
    }
    public void setPeopleList(List<People> peopleList){
        this.peopleList=peopleList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (FindFriendListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement FindFriendListener");
        }

    }

    public interface FindFriendListener{
        void addFriend(People people);
    }
}
