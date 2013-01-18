package com.example.vncreatures.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.vncreatures.R;
import com.example.vncreatures.common.Common;

public abstract class AbstractFragmentActivity extends SherlockFragmentActivity
        implements OnClickListener {

    SharedPreferences pref;
    int tabPosition;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        // Create indicator bar
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

        // Action bar
        setTheme(Common.THEME);

        // get preference
        pref = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        // init view
        setContentView(R.layout.parent_container);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.container);
        initTabButton();
        container.addView(createView());
        this.tabPosition = indentifyTabPosition();
        pref.edit().putInt(Common.TAB_PREF, this.tabPosition).commit();
        resetTabState();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.tabPosition = indentifyTabPosition();
        pref.edit().putInt(Common.TAB_PREF, this.tabPosition).commit();
        resetTabState();
    }

    protected abstract View createView();

    protected abstract int indentifyTabPosition();

    protected void initTabButton() {
        /*
         * Intent mainIntent = new Intent(context, NewsTabsPagerActivity.class);
         * startActivity(mainIntent);
         */

        resetTabState();

        View button = (View) findViewById(R.id.tabHome_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabNews_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabDiscussion_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabsMap_button);
        button.setOnClickListener(this);
        button = (View) findViewById(R.id.tabNews_button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
        switch (v.getId()) {
        case R.id.tabHome_button:
            resetTabState();
            if (v.getId() != id) {
                Intent mainIntent = new Intent(this,
                        KingdomChooseActivity.class);
                startActivity(mainIntent);
            }
            break;
        case R.id.tabNews_button:
            resetTabState();
            if (v.getId() != id) {
                Intent mainIntent = new Intent(this,
                        NewsTabsPagerActivity.class);
                startActivity(mainIntent);
            }
            break;
        case R.id.tabDiscussion_button:
            resetTabState();
            break;
        case R.id.tabsMap_button:
            resetTabState();
            break;

        default:
            break;
        }
    }

    private void resetTabState() {
        int id = pref.getInt(Common.TAB_PREF, R.id.tabHome_button);
        LinearLayout tabControl = (LinearLayout) findViewById(R.id.tab_control);
        for (int i = 0; i < tabControl.getChildCount(); i++) {
            View v = tabControl.getChildAt(i);
            if (v.getId() == id)
                v.setSelected(true);
            else
                v.setSelected(false);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                .getWindowToken(), 0);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    AbstractActivity.hideSoftKeyboard(AbstractFragmentActivity.this);
                    return false;
                }

            });
        }

        // If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

    protected Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri uri = Uri.fromFile(getFileStreamPath("shared.png"));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;
    }
}