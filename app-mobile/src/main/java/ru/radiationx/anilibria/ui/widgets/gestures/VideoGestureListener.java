package ru.radiationx.anilibria.ui.widgets.gestures;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class VideoGestureListener extends GestureDetector.SimpleOnGestureListener {

    public static final String TAG = "VideoGestureListener";
    private static final int SWIPE_THRESHOLD = 100;
    private final int minFlingVelocity;
    private final VideoGestureEventsListener listener;

    public VideoGestureListener(VideoGestureEventsListener listener, ViewConfiguration viewConfiguration) {
        this.listener = listener;
        minFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        listener.onTap(e);
        return super.onSingleTapUp(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //listener.onTap(e);
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        listener.onDoubleTap(e);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // Touch has been long enough to indicate a long press.
        // Does not indicate motion is complete yet (no up event necessarily)
        //Log.i(TAG, "Long Press");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //Log.i(TAG, "Scroll");

        //Log.d("gestureLalala_int", "" + (e1.getAction()) + " : " + e2.getAction());

        float deltaY = e2.getY() - e1.getY();
        float deltaX = e2.getX() - e1.getX();

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            //if (Math.abs(deltaX) > SWIPE_THRESHOLD) {
            listener.onHorizontalScroll(e2, deltaX);
                /*if (deltaX > 0) {
                    Log.i(TAG, "Slide right");
                } else {
                    Log.i(TAG, "Slide left");
                }*/
            //}
        } else {
            //if (Math.abs(deltaY) > SWIPE_THRESHOLD) {
            listener.onVerticalScroll(e2, deltaY);
                /*if (deltaY > 0) {
                    Log.i(TAG, "Slide down");
                } else {
                    Log.i(TAG, "Slide up");
                }*/
            //}
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Fling event occurred.  Notification of this one happens after an "up" event.
        //Log.i(TAG, "Fling");
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > minFlingVelocity) {
                    if (diffX > 0) {
                        listener.onSwipeRight();
                    } else {
                        listener.onSwipeLeft();
                    }
                }
                result = true;
            } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > minFlingVelocity) {
                if (diffY > 0) {
                    listener.onSwipeBottom();
                } else {
                    listener.onSwipeTop();
                }
            }
            result = true;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        //Log.i(TAG, "Show Press");
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //Log.i(TAG, "Down");
        return false;
    }


}
