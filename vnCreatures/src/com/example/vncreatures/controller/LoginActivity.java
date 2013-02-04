package com.example.vncreatures.controller;

import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidquery.util.Common;
import com.example.vncreatures.R;
import com.example.vncreatures.model.LoginViewModel;
import com.example.vncreatures.model.discussion.FacebookUser;
import com.example.vncreatures.rest.HrmService;
import com.example.vncreatures.rest.HrmService.PostTaskCallback;
import com.example.vncreatures.view.LoginView;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.gson.Gson;

public class LoginActivity extends AbstractActivity implements OnClickListener {

    private LoginViewModel mModel = new LoginViewModel();
    private LoginView mView;
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    private UiLifecycleHelper uiHelper;
    private boolean mHandleLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Transition
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);

        mView = new LoginView(this, mModel);

        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        // setupUI(findViewById(R.id.layout_parent));

        // init session
        /*
         * Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
         * 
         * Session session = Session.getActiveSession(); if (session == null) {
         * if (savedInstanceState != null) { session =
         * Session.restoreSession(this, null, statusCallback,
         * savedInstanceState);
         * 
         * } if (session == null) { session = new Session(this); } else { Intent
         * mainIntent = new Intent(LoginActivity.this,
         * DiscussionActivity.class);
         * mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         * startActivity(mainIntent); } Session.setActiveSession(session); if
         * (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
         * session.openForRead(new Session.OpenRequest(this) .setPermissions(
         * Arrays.asList("email", "user_hometown",
         * "user_birthday")).setCallback( statusCallback)); } } else { if
         * (!session.isClosed()) { Intent mainIntent = new
         * Intent(LoginActivity.this, DiscussionActivity.class);
         * mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         * startActivity(mainIntent); } }
         */

        // Event
        mModel.facebookLoginButton.setOnClickListener(this);
        mModel.loginButton.setOnClickListener(this);

    }

    @Override
    protected View createView() {
        return mView;
    }

    @Override
    protected void onResume() {
        // Transition
        overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.login);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
        case R.id.facebookLogin_button:
            loginToFacebook();
            break;
        case R.id.login_button:
            Session session = Session.getActiveSession();
            session.closeAndClearTokenInformation();
            break;

        default:
            break;
        }
    }

    private void loginToFacebook() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            session.openForRead(new Session.OpenRequest(this).setPermissions(
                    Arrays.asList("email", "user_hometown", "user_birthday"))
                    .setCallback(statusCallback));
        } else {
            Session.openActiveSession(this, true, statusCallback);
        }
    }

    @Override
    protected int indentifyTabPosition() {
        return R.id.tabDiscussion_button;
    }

    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state,
                Exception exception) {
            String userid = pref.getString(
                    com.example.vncreatures.common.Common.USER_ID, null);
            if (state == SessionState.OPENING) {
                mHandleLogin = true;
            }
            if (session != null && session.isOpened()) {
                // if the session is already open, try to show the discussion
                // activity
                if (mHandleLogin) {
                    addUser(session);
                    mHandleLogin = false;
                } else {
                    if (userid == null) {
                        addUser(session);
                    } else {
                        Intent mainIntent = new Intent(LoginActivity.this,
                                DiscussionActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        }
    }

    private void addUser(Session session) {
        mView.setVisibility(View.GONE);
        setSupportProgressBarIndeterminateVisibility(true);
        Request.executeMeRequestAsync(session, new GraphUserCallback() {

            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    Gson gson = new Gson();
                    String json2 = user.getInnerJSONObject().toString();
                    FacebookUser fb = gson.fromJson(json2, FacebookUser.class);
                    HrmService service = new HrmService();
                    service.setCallback(new PostTaskCallback() {

                        @Override
                        public void onSuccess(String result) {
                            pref.edit()
                                    .putString(
                                            com.example.vncreatures.common.Common.USER_ID,
                                            result).commit();
                            setSupportProgressBarIndeterminateVisibility(false);
                            Intent mainIntent = new Intent(LoginActivity.this,
                                    DiscussionActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                            finish();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    service.requestAddUser(fb);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

}
