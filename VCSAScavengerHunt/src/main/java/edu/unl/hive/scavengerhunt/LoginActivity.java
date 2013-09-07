package edu.unl.hive.scavengerhunt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.preference.DialogPreference;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.LinkedList;

public class LoginActivity extends Activity {
    private EditText userName, password;
    private Dialog splashScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityState data = (ActivityState) getLastNonConfigurationInstance();
        if (data != null) {
            if(!data.splashScreenDisplayed)
            {
                showSplashScreen();
            }
        }
        else
        {
            showSplashScreen();
        }

        setContentView(R.layout.login);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "josefinslab_semibold.ttf");

        TextView titleText = (TextView)findViewById(R.id.titleText);
        Typeface titleTypeface = Typeface.createFromAsset(getAssets(), "josefinslab_bold.ttf");
        titleText.setTypeface(titleTypeface);
        titleText.setText("Login");

        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        TextView createAccountView = (TextView)findViewById(R.id.createAccountView);
        Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setTypeface(typeface);

        createAccountView.setOnClickListener(onCreateAccountViewClick);

        loginButton.setOnClickListener(onLoginButtonClicked);

    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        ActivityState data = new ActivityState();
        // Save your important data here

        if (splashScreen != null) {
            data.splashScreenDisplayed = false;
            removeSplashScreen();
        }
        return data;
    }

    private void removeSplashScreen()
    {
        splashScreen.dismiss();
        splashScreen = null;
    }

    private void showSplashScreen()
    {
        splashScreen = new Dialog(this, R.style.SplashScreen);
        splashScreen.setContentView(R.layout.splash);
        splashScreen.setCancelable(false);
        splashScreen.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeSplashScreen();
            }
        }, 5000);
    }

    private View.OnClickListener onCreateAccountViewClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        }
    };

    private View.OnClickListener onLoginButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AsyncTask<Object, Object, Object> loginTask = new AsyncTask<Object, Object, Object>() {
                private AlertDialog alertDialog;
                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage("Logging in...");
                        alertDialog = builder.create();
                        alertDialog.show();
                    }
                    catch (Exception e)
                    {
                        cancel(true);
                    }
                }
                @Override
                protected Object doInBackground(Object... objects) {
                    try
                    {
                        HttpPost post = new HttpPost(DataStorage.getBaseURL() +"auth/login");
                        LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
                        params.add(new BasicNameValuePair("userName", objects[0].toString()));
                        params.add(new BasicNameValuePair("password", objects[1].toString()));
                        UrlEncodedFormEntity data = new UrlEncodedFormEntity(params);
                        post.setEntity(data);
                        HttpResponse response = DataStorage.getHttpClient().execute(post);
                        InputStream is = response.getEntity().getContent();
                        String json = "", line = null;
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        while((line = reader.readLine()) != null)
                        {
                            json = json + line;
                        }
                        reader.close();
                        return new JSONObject(json);
                    }
                    catch(Exception e)
                    {
                        return e;
                    }
                }
                @Override
                protected void onPostExecute(Object result)
                {
                    if(alertDialog != null)
                    {
                        alertDialog.dismiss();
                    }
                    if(result instanceof JSONObject)
                    {
                        try
                        {
                            UserInfo userInfo = new UserInfo();
                            JSONObject data = (JSONObject)result;
                            if(data.getBoolean("error"))
                            {
                                showInvalidLoginDialog(data.getString("message"));
                            }
                            else
                            {
                                userInfo.setUserId(data.getInt("userId"));
                                userInfo.setFirstName(data.getString("firstName"));
                                DataStorage.setCurrentUser(userInfo);
                                Intent i = new Intent(LoginActivity.this, SurveyActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        catch(Exception e)
                        {
                            showInvalidLoginDialog(null);
                        }
                    }
                    else
                    {
                        showInvalidLoginDialog(null);
                    }
                    cancel(true);
                }
            };
            loginTask.execute(userName.getText().toString(), password.getText().toString());
        }
    };

    private void showInvalidLoginDialog(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(text == null ? "Could not log you in right now. Please try again later." : text);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private class ActivityState
    {
        public boolean splashScreenDisplayed = true;
    }

}
