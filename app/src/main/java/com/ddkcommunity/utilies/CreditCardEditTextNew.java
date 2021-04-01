package com.ddkcommunity.utilies;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;


/**
 * Created by Jaison on 29/05/17.
 */

public class CreditCardEditTextNew extends AppCompatEditText {

    public Context context;

    BackButtonListener backButtonListener;

    public CreditCardEditTextNew(Context context) {
        super(context);
        this.context=context;
    }

    public CreditCardEditTextNew(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public CreditCardEditTextNew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void setOnBackButtonListener(BackButtonListener listener)
    {
        backButtonListener = listener;
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getAction()==KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            Log.d("ET", "onKeyPreIme: ");
            if(backButtonListener!=null)
                backButtonListener.onBackClick();
            return true;
            // TODO: Hide your view as you do it in your activity
        }
        return false;
    }

    public interface BackButtonListener
    {
        public void onBackClick();
    }
}
