package com.sieunguoimay.vuduydu.notemanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RenameFolder extends AppCompatDialogFragment {
    private EditText editText_folderName;
    private RenameFolderListener listener;
    public enum PopupType{RENAME,NEW_FOLDER};
    private int id;
    private PopupType type;
    public void setData(PopupType type,int id){
        this.type = type;
        this.id = id;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rename_dialog,null);
        String title = "";
        if(type == PopupType.RENAME)
            title = "Rename Folder";
        else if(type==PopupType.NEW_FOLDER)
            title = "Create Folder";


        builder.setView(view).setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editText_folderName.getText().toString();
                listener.applyFolderName(name,type,id);
            }
        });
        editText_folderName = view.findViewById(R.id.edit_folderName);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RenameFolderListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+"must implement RenameFolderListener");
        }

    }

    public interface RenameFolderListener{
        void applyFolderName(String folderName, PopupType type, int position);
    }
}
