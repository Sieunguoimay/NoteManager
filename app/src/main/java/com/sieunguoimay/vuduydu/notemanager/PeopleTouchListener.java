package com.sieunguoimay.vuduydu.notemanager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class PeopleTouchListener implements RecyclerView.OnItemTouchListener{
    private GestureDetector gestureDetector;
    private PeopleTouchListener.ClickListener clickListener;

    public PeopleTouchListener(Context context, final RecyclerView recyclerView, final PeopleTouchListener.ClickListener clickListener) {
        this.clickListener = clickListener;
        this.gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child),e);
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e){
        View child = rv.findChildViewUnder(e.getX(),e.getY());
        if(child!=null&&clickListener!=null&&gestureDetector.onTouchEvent(e)){
            clickListener.onClick(child,rv.getChildAdapterPosition(child));
        }
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e){
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept){

    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position, MotionEvent event);
    }
}
