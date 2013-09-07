package edu.unl.hive.scavengerhunt;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.DialogPreference;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

public class SurveyActivity extends Activity {

    private SurveyScreen surveyScreen;
    private List<String> selectedResponses;
    private List<View> selectedViews;
    private boolean allowMultipleResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedResponses = new LinkedList<String>();
        selectedViews = new LinkedList<View>();
        surveyScreen = new SurveyScreen(this);
        setContentView(surveyScreen.getView());
        surveyScreen.setTitle("Survey");
        surveyScreen.getNextButton().setOnClickListener(onNextButtonClicked);
        updateQuestions();
    }

    private View.OnClickListener onNextButtonClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            for(String selectedResponse : selectedResponses)
            {
                SurveyResponse response = new SurveyResponse();
                response.setSurveyQuestionId(DataStorage.getCurrentQuestion().getSurveyQuestionId());
                response.setUserId(DataStorage.getCurrentUser().getUserId());
                response.setResponse(selectedResponse);
                DataStorage.getSurveyResponses().add(response);
            }
            submitResponses();
            if(DataStorage.getCurrentSurveyQuestion() == DataStorage.getSurveyQuestions().size() - 1)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
                builder.setMessage("End of Demo");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                builder.create().show();
                DataStorage.setSurveyQuestions(null);
            }
            else
            {
                DataStorage.setCurrentSurveyQuestion(DataStorage.getCurrentSurveyQuestion() + 1);
                Intent i = new Intent(SurveyActivity.this, SurveyActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    private void submitResponses()
    {
        AsyncTask<Object, Object, Object> updateTask = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... objects) {
                for(int i = 0; i < DataStorage.getSurveyResponses().size(); i++)
                {
                    try
                    {
                        SurveyResponse response = DataStorage.getSurveyResponses().get(i);
                        LinkedList<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();
                        params.add(new BasicNameValuePair("userId",
                                Integer.toString(response.getUserId())));
                        params.add(new BasicNameValuePair("surveyQuestionId",
                                Integer.toString(response.getSurveyQuestionId())));
                        params.add(new BasicNameValuePair("answer",
                                response.getResponse()));
                        HttpPost post = new HttpPost(DataStorage.getBaseURL() +"survey/postAnswer");
                        UrlEncodedFormEntity data = new UrlEncodedFormEntity(params);
                        post.setEntity(data);
                        HttpResponse responseData = DataStorage.getHttpClient().execute(post);
                        responseData.getEntity().consumeContent();
                        DataStorage.getSurveyResponses().remove(i);
                        i--;
                    }
                    catch(Exception e)
                    {

                    }
                }
                return null;
            }
            @Override
            protected void onPostExecute(Object result)
            {
                cancel(true);
            }
        };
        updateTask.execute();
    }

    private void updateQuestions()
    {
        if(DataStorage.getSurveyQuestions() == null || DataStorage.getSurveyQuestions().size() == 0)
        {
            AsyncTask<Object, Object, Object> updateTask = new AsyncTask<Object, Object, Object>() {
                private AlertDialog alertDialog;
                @Override
                protected void onPreExecute()
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
                    builder.setMessage("Loading questions...");
                    alertDialog = builder.create();
                    alertDialog.show();
                }
                @Override
                protected Object doInBackground(Object... objects) {
                    DataStorage.setSurveyQuestions(SurveyQuestion.getSurveyQuestions());
                    if(DataStorage.getSurveyQuestions().size() == 0)
                    {
                        return null;
                    }
                    DataStorage.setCurrentSurveyQuestion(0);
                    return DataStorage.getSurveyQuestions().get(0);
                }
                @Override
                protected  void onPostExecute(Object result)
                {
                    alertDialog.dismiss();
                    if(result != null)
                    {
                        SurveyQuestion question = (SurveyQuestion)result;
                        surveyScreen.setQuestionText(question.getDescription());
                        bindAdapter(question);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SurveyActivity.this);
                        builder.setMessage("No questions are available right now.");
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        });
                        builder.create().show();
                    }
                    cancel(true);
                }
            };
            updateTask.execute();
        }
        else
        {
            SurveyQuestion question = (SurveyQuestion)DataStorage.getSurveyQuestions().get(DataStorage.getCurrentSurveyQuestion());
            surveyScreen.setQuestionText(question.getDescription());
            bindAdapter(question);
        }
    }

    private void bindAdapter(SurveyQuestion question)
    {
        ListView spinner = (ListView)findViewById(R.id.surveyResponses);
        allowMultipleResponses = question.getQuestionType() == 2;
        spinner.setAdapter(new SpinnerArrayAdapter(SurveyActivity.this,
                question.getSurveyResponses()));
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(allowMultipleResponses)
                {
                    RadioButton radioButton = null;
                    if(selectedViews.size() == 2)
                    {
                        selectedResponses.remove(0);
                        View view2 = selectedViews.get(0);
                        radioButton = (RadioButton)view2.findViewById(R.id.surveyRadioButton);
                        radioButton.setChecked(false);
                        selectedViews.remove(0);
                    }
                    radioButton = (RadioButton)view.findViewById(R.id.surveyRadioButton);
                    boolean isSelected = radioButton.isChecked();
                    radioButton.setChecked(!isSelected);
                    if(isSelected)
                    {
                        selectedResponses.remove(adapterView.getAdapter().getItem(i));
                        selectedViews.remove(view);
                    }
                }
                else
                {
                    if(selectedViews.size() > 0)
                    {
                        View selectedView = selectedViews.get(0);
                        ((RadioButton)selectedView.findViewById(R.id.surveyRadioButton)).setChecked(false);
                    }
                    selectedViews.clear();
                    selectedResponses.clear();
                    RadioButton radioButton = (RadioButton)view.findViewById(R.id.surveyRadioButton);
                    radioButton.setChecked(true);
                }
                selectedViews.add(view);
                selectedResponses.add(adapterView.getAdapter().getItem(i).toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.survey, menu);
        return true;
    }
    
}
