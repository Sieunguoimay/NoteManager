package com.sieunguoimay.vuduydu.notemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteEdit extends AppCompatActivity {
    private EditText editText;
    private TextView textView_Date;
    private int note_id;
    private String created_date ="";
    private String edited_date ="";
    private String key="";
    private boolean newText = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        editText = findViewById(R.id.edit_noteEditor);
        textView_Date = ((TextView)findViewById(R.id.textView_Date));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Date d = new Date();
                edited_date = DateFormat.getInstance().format(d);
            }
        });




        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("existedNote");
        note_id = -1;
        if(bundle!=null){
            editText.setText(bundle.getString("note_content"));
            note_id = bundle.getInt("note_id");
            created_date = bundle.getString("created_date");
            key = bundle.getString("key");
            newText = false;
        }else{
            newText = true;
            Date d = new Date();
            created_date =DateFormat.getInstance().format(d);
        }

        textView_Date.setText("Created "+created_date);

        findViewById(R.id.back_arrow_from_editor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra("note",editText.getText().toString());
                intent.putExtra("note_id",note_id);
                intent.putExtra("created_date", created_date);
                intent.putExtra("edited_date", edited_date);
                intent.putExtra("key", key);
                setResult(Activity.RESULT_OK,intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        });
        findViewById(R.id.relativeLayout_editor).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,InputMethodManager.SHOW_IMPLICIT);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }
        });
        editText.setSelection(editText.getText().length());
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


}
