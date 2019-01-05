package com.sieunguoimay.vuduydu.notemanager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    GridLayout folderRegion;
    Context mContext;
    DisplayMetrics mMetrics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        folderRegion = findViewById(R.id.folderRegion);
        mMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mMetrics);


        findViewById(R.id.button_newFolder).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                folderRegion.addView(createCardViewFolder());
                Toast.makeText(MainActivity.this,"New Folder created",Toast.LENGTH_SHORT).show();

            }
        });

        final GridLayout folderRegion = findViewById(R.id.folderRegion);
        folderRegion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                v.setFocusableInTouchMode(true);
                closeKeyboard();
            }
        });
        final GridLayout noteRegion = findViewById(R.id.noteRegion);
        noteRegion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                v.setFocusableInTouchMode(true);
                closeKeyboard();
            }
        });
    }
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    private LinearLayout createCardViewFolder(){

        int folder_width = (int)((double)mMetrics.widthPixels/4.5);
        int folder_height = (mMetrics.heightPixels/8);
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                folder_height
        );
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        CardView newCardView = new CardView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        newCardView.setLayoutParams(layoutParams);
        newCardView.setRadius(15);
        newCardView.setElevation(0);

            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams((int)(3.0f*((float)folder_width/4.0f)),(int)(5.0f*(float)folder_height/9.0f));
            imageView.setLayoutParams(imageParams);
            imageView.setBackgroundResource(R.drawable.ic_create_new_folder_yellow_24dp);

        newCardView.addView(imageView);
        linearLayout.addView(newCardView);

        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                folder_width,
                LinearLayout.LayoutParams.FILL_PARENT
        );
        textView.setLayoutParams(textParams);
        textView.setText("Untitled long long long long");
        textView.setTextColor(Color.parseColor("#6f6f6f"));
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setMaxLines(2);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        linearLayout.addView(textView);

        return linearLayout;
    }

}

/*
*
*
*                     <LinearLayout

                        android:id="@+id/linearLayout_Folder"
                        android:layout_width="wrap_content"
                        android:layout_height="120dp"
                        android:gravity="center"
                        android:layout_margin="8dp"
                        android:orientation="vertical"
                        >
                        <android.support.v7.widget.CardView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            app:cardCornerRadius="15dp"
                            app:cardElevation="0dp"
                            >

                        <ImageView
                            android:id="@+id/imageView_Folder"
                            android:layout_width="70dp"
                            android:layout_height="70dp"
                            android:src="@drawable/ic_create_new_folder_yellow_24dp" />
                        </android.support.v7.widget.CardView>

                        <TextView
                            android:id="@+id/textView_Fodler"
                            android:layout_width="80dp"
                            android:layout_height="fill_parent"
                            android:layout_marginTop="5dp"
                            android:text="Untitled long long long and long"
                            android:textColor="#6f6f6f"
                            android:textAlignment="center"
                            android:maxLines="2"
                            android:ellipsize="end"
                            />
                    </LinearLayout>
                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="120dp"
                        android:gravity="center"
                        android:layout_margin="8dp"
                        android:orientation="vertical"
                        >
                        <android.support.v7.widget.CardView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            app:cardCornerRadius="15dp"
                            app:cardElevation="0dp"
                            >

                            <ImageView
                                android:layout_width="70dp"
                                android:layout_height="70dp"
                                android:src="@drawable/ic_create_new_folder_yellow_24dp" />
                        </android.support.v7.widget.CardView>

                        <TextView
                            android:layout_width="80dp"
                            android:layout_height="fill_parent"
                            android:layout_marginTop="5dp"
                            android:text="Untitled long long long and long"
                            android:textColor="#6f6f6f"
                            android:textAlignment="center"
                            android:maxLines="2"
                            android:ellipsize="end"
                            />
                    </LinearLayout>
                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="120dp"
                        android:gravity="center"
                        android:layout_margin="8dp"
                        android:orientation="vertical"
                        >
                        <android.support.v7.widget.CardView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            app:cardCornerRadius="15dp"
                            app:cardElevation="0dp"
                            >

                            <ImageView
                                android:layout_width="70dp"
                                android:layout_height="70dp"
                                android:src="@drawable/ic_create_new_folder_yellow_24dp" />
                        </android.support.v7.widget.CardView>

                        <TextView
                            android:layout_width="80dp"
                            android:layout_height="fill_parent"
                            android:layout_marginTop="5dp"
                            android:text="Untitled long long long and long"
                            android:textColor="#6f6f6f"
                            android:textAlignment="center"
                            android:maxLines="2"
                            android:ellipsize="end"
                            />
                    </LinearLayout>
                    <LinearLayout
                        android:layout_width="wrap_content"
                        android:layout_height="120dp"
                        android:gravity="center"
                        android:layout_margin="8dp"
                        android:orientation="vertical"
                        >
                        <android.support.v7.widget.CardView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            app:cardCornerRadius="15dp"
                            app:cardElevation="0dp"
                            >

                            <ImageView
                                android:layout_width="70dp"
                                android:layout_height="70dp"
                                android:src="@drawable/ic_create_new_folder_yellow_24dp" />
                        </android.support.v7.widget.CardView>

                        <TextView
                            android:layout_width="80dp"
                            android:layout_height="fill_parent"
                            android:layout_marginTop="5dp"
                            android:text="Untitled long long long and long"
                            android:textColor="#6f6f6f"
                            android:textAlignment="center"
                            android:maxLines="2"
                            android:ellipsize="end"
                            />
                    </LinearLayout>
*
*
*
*
                <android.support.v7.widget.CardView
                    android:id="@+id/cardView_Folder"
                    android:layout_width="wrap_content"
                    android:layout_height="50dp"
                    android:layout_rowWeight="1"
                    android:layout_columnWeight="1"
                    android:layout_margin="8dp"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="6dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:orientation="vertical"
                        android:paddingTop="5dp">

                        <ImageView
                            android:layout_width="50dp"
                            android:layout_height="50dp"
                            android:src="@mipmap/ic_launcher" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="5dp"
                            android:text="Untitled"
                            android:textColor="#6f6f6f"
                            android:textSize="18dp" />
                    </LinearLayout>
                </android.support.v7.widget.CardView>

                <android.support.v7.widget.CardView
                    android:layout_width="wrap_content"
                    android:layout_height="50dp"
                    android:layout_columnWeight="1"
                    android:layout_margin="8dp"
                    android:layout_rowWeight="1"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="6dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:orientation="vertical"
                        android:paddingTop="5dp">

                        <ImageView
                            android:layout_width="50dp"
                            android:layout_height="50dp"
                            android:src="@mipmap/ic_launcher" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="5dp"
                            android:text="Untitled"
                            android:textColor="#6f6f6f"
                            android:textSize="18dp" />
                    </LinearLayout>
                </android.support.v7.widget.CardView>

                <android.support.v7.widget.CardView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_columnWeight="1"
                    android:layout_margin="8dp"
                    android:layout_rowWeight="1"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="6dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:orientation="vertical"
                        android:paddingTop="5dp">

                        <ImageView
                            android:layout_width="50dp"
                            android:layout_height="50dp"
                            android:src="@mipmap/ic_launcher" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="5dp"
                            android:text="Untitled"
                            android:textColor="#6f6f6f"
                            android:textSize="18dp" />
                    </LinearLayout>
                </android.support.v7.widget.CardView>
                <android.support.v7.widget.CardView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_columnWeight="1"
                    android:layout_margin="8dp"
                    android:layout_rowWeight="1"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="6dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:orientation="vertical"
                        android:paddingTop="5dp">

                        <ImageView
                            android:layout_width="50dp"
                            android:layout_height="50dp"
                            android:src="@mipmap/ic_launcher" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="5dp"
                            android:text="Untitled"
                            android:textColor="#6f6f6f"
                            android:textSize="18dp" />
                    </LinearLayout>
                </android.support.v7.widget.CardView>
                <android.support.v7.widget.CardView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_columnWeight="1"
                    android:layout_margin="8dp"
                    android:layout_rowWeight="1"
                    app:cardCornerRadius="8dp"
                    app:cardElevation="6dp">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:gravity="center"
                        android:orientation="vertical"
                        android:paddingTop="5dp">

                        <ImageView
                            android:layout_width="50dp"
                            android:layout_height="50dp"
                            android:src="@mipmap/ic_launcher" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="5dp"
                            android:text="Untitled"
                            android:textColor="#6f6f6f"
                            android:textSize="18dp" />
                    </LinearLayout>
                </android.support.v7.widget.CardView>

*
* */