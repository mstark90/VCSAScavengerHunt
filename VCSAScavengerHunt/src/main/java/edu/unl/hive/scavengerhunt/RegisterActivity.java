package edu.unl.hive.scavengerhunt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class RegisterActivity extends Activity {

    private EditText firstName;
    private EditText lastName;
    private EditText emailAddress;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "josefinslab_semibold.ttf");
        Typeface titleTypeface = Typeface.createFromAsset(getAssets(), "josefinslab_bold.ttf");

        TextView titleText = (TextView)findViewById(R.id.titleText);
        titleText.setTypeface(titleTypeface);
        titleText.setText("Register");

        firstName = (EditText)findViewById(R.id.firstName);
        lastName = (EditText)findViewById(R.id.lastName);
        emailAddress = (EditText)findViewById(R.id.emailAddress);
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.password);
        Button registerButton = (Button)findViewById(R.id.registerButton);

        registerButton.setOnClickListener(onRegisterButtonClicked);
        registerButton.setTypeface(typeface);

    }

    private View.OnClickListener onRegisterButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AsyncTask<Object, Object, Object> loginTask = new AsyncTask<Object, Object, Object>() {
                private JSONObject loginData;
                private AlertDialog alertDialog;
                @Override
                protected void onPreExecute()
                {
                    loginData = new JSONObject();
                    try
                    {
                        loginData.put("userName", userName.getText().toString());
                        loginData.put("password", password.getText().toString());
                        loginData.put("firstName", firstName.getText().toString());
                        loginData.put("lastName", lastName.getText().toString());
                        loginData.put("emailAddress", emailAddress.getText().toString());
                        loginData.put("birthday", 0);
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Creating Account...");
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
                        LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
                        params.add(new BasicNameValuePair("userName", objects[0].toString()));
                        params.add(new BasicNameValuePair("password", objects[1].toString()));
                        params.add(new BasicNameValuePair("firstName", objects[2].toString()));
                        params.add(new BasicNameValuePair("lastName", objects[3].toString()));
                        params.add(new BasicNameValuePair("emailAddress", objects[4].toString()));
                        params.add(new BasicNameValuePair("birthday", "0"));
                        HttpPut post = new HttpPut(DataStorage.getBaseURL() +"auth/register");
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
                    finally
                    {
                        loginData = null;
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
                                showRegisterFailureDialog(data.getString("message"));
                            }
                            else
                            {
                                userInfo.setUserId(data.getInt("userId"));
                                userInfo.setFirstName(data.getString("firstName"));
                                DataStorage.setCurrentUser(userInfo);
                                Intent i = new Intent(RegisterActivity.this, SurveyActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        catch(Exception e)
                        {
                            showRegisterFailureDialog(null);
                        }
                    }
                    else
                    {
                        showRegisterFailureDialog(null);
                    }
                    cancel(true);
                }
            };
            loginTask.execute(userName.getText().toString(), password.getText().toString(),
            firstName.getText().toString(), lastName.getText().toString(), emailAddress.getText().toString());
        }
    };

    private void showRegisterFailureDialog(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(text == null ? "Could not create your account right now. Please try again later." : text);
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
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }
    
}
