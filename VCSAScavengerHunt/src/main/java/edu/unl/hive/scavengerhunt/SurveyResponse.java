package edu.unl.hive.scavengerhunt;

/**
 * Created by mstark on 8/5/13.
 */
public class SurveyResponse
{
    private int userId;
    private int surveyQuestionId;
    private String response;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSurveyQuestionId() {
        return surveyQuestionId;
    }

    public void setSurveyQuestionId(int surveyQuestionId) {
        this.surveyQuestionId = surveyQuestionId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
