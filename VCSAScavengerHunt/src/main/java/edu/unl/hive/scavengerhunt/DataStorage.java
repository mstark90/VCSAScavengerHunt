package edu.unl.hive.scavengerhunt;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLContext;

/**
 * Created by mstark on 8/4/13.
 */
public class DataStorage
{
    private static UserInfo currentUser;
    private static int currentSurveyQuestion;
    private static List<SurveyQuestion> surveyQuestions;
    private static List<SurveyResponse> surveyResponses;
    private static Riddle currentRiddle;
    private static HttpClient httpClient;

    public static String getBaseURL()
    {
        return "http://10.10.12.192:8080/ScavengerHunt/rest/";
    }

    public static HttpClient getHttpClient()
    {
        if(httpClient == null)
        {
            try
            {
                Scheme http = new Scheme("http", PlainSocketFactory.getSocketFactory(), 80);

                SSLContext sslcontext = SSLContext.getInstance("TLS");
                sslcontext.init(null, null, null);
                SSLSocketFactory sf = SSLSocketFactory.getSocketFactory();
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                Scheme https = new Scheme("https", sf, 443);
                SchemeRegistry sr = new SchemeRegistry();
                sr.register(http);
                sr.register(https);
                HttpParams params = new BasicHttpParams();

                SingleClientConnManager mgr = new SingleClientConnManager(params, sr);
                httpClient = new DefaultHttpClient(mgr, params);
            }
            catch (Exception e)
            {
                httpClient = new DefaultHttpClient();
            }
        }
        return httpClient;
    }

    public static UserInfo getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(UserInfo currentUser) {
        DataStorage.currentUser = currentUser;
    }

    public static int getCurrentSurveyQuestion() {
        return currentSurveyQuestion;
    }

    public static void setCurrentSurveyQuestion(int currentSurveyQuestion) {
        DataStorage.currentSurveyQuestion = currentSurveyQuestion;
    }

    public static List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public static void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        DataStorage.surveyQuestions = surveyQuestions;
    }

    public static SurveyQuestion getCurrentQuestion()
    {
        return surveyQuestions.get(currentSurveyQuestion);
    }

    public static List<SurveyResponse> getSurveyResponses()
    {
        if(surveyResponses == null)
        {
            surveyResponses = new LinkedList<SurveyResponse>();
        }
        return surveyResponses;
    }

    public static Riddle getCurrentRiddle() {
        return currentRiddle;
    }

    public static void setCurrentRiddle(Riddle currentRiddle) {
        DataStorage.currentRiddle = currentRiddle;
    }
}
