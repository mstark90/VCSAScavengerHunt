/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.unl.hive.scavengerhunt;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author
 * mstark
 */
public class SurveyQuestion extends ReturnMessage implements Serializable {
    private int surveyQuestionId;
    private String description;
    private int questionType;
    private List<String> surveyResponses;

    public static List<SurveyQuestion> getSurveyQuestions()
    {
        List<SurveyQuestion> surveyQuestions = new LinkedList<SurveyQuestion>();
        try
        {
            HttpGet request = new HttpGet(DataStorage.getBaseURL()
                    +"survey/getSurveyQuestions?userId="+ DataStorage.getCurrentUser().getUserId());
            HttpResponse response = DataStorage.getHttpClient().execute(request);
            InputStream is = response.getEntity().getContent();
            String json = "", line = null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            while((line = reader.readLine()) != null)
            {
                json = json + line;
            }
            reader.close();
            JSONArray data = new JSONArray(json);
            for(int i = 0; i < data.length(); i++)
            {
                JSONObject obj = data.getJSONObject(i);
                SurveyQuestion question = new SurveyQuestion();
                question.setDescription(obj.getString("description"));
                question.setQuestionType(obj.getInt("questionType"));
                question.setSurveyQuestionId(obj.getInt("surveyQuestionId"));
                List<String> responses = new LinkedList<String>();
                JSONArray responsesJSON = obj.getJSONArray("surveyResponses");
                for(int ix = 0; ix < responsesJSON.length(); ix++)
                {
                    responses.add(responsesJSON.getString(ix));
                }
                question.setSurveyResponses(responses);
                surveyQuestions.add(question);
            }
        }
        catch(Exception e)
        {
            Log.d("SurveyQuestion", "Could not get the survey questions: ", e);
        }
        return surveyQuestions;
    }

    /**
     * @return the surveyQuestionId
     */
    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    /**
     * @param surveyQuestionId the surveyQuestionId to set
     */
    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the questionType
     */
    public int getQuestionType() {
        return questionType;
    }

    /**
     * @param questionType the questionType to set
     */
    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    /**
     * @return the surveyResponses
     */
    public List<String> getSurveyResponses() {
        return surveyResponses;
    }

    /**
     * @param surveyResponses the surveyResponses to set
     */
    public void setSurveyResponses(List<String> surveyResponses) {
        this.surveyResponses = surveyResponses;
    }
}
