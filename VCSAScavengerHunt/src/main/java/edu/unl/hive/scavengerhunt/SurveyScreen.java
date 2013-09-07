package edu.unl.hive.scavengerhunt;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mstark on 8/4/13.
 */
public class SurveyScreen
{
    private LinearLayout surveyView;
    private TextView titleText;
    private TextView interjection;
    private Button nextButton;
    private Context context;
    public SurveyScreen(Context context)
    {
        this.context = context;
        surveyView = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.interjection_screen, null);
        titleText = (TextView)surveyView.findViewById(R.id.titleText);
        Typeface titleTypeface = Typeface.createFromAsset(context.getAssets(), "josefinslab_bold.ttf");
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "josefinslab_semibold.ttf");
        titleText.setTypeface(titleTypeface);
        interjection = (TextView)surveyView.findViewById(R.id.interjectionText);
        interjection.setTypeface(typeface);
        nextButton = (Button)surveyView.findViewById(R.id.nextButton);
        nextButton.setTypeface(typeface);
    }
    public View getView()
    {
        return surveyView;
    }
    public Button getNextButton()
    {
        return nextButton;
    }
    public String getTitle()
    {
        return titleText.getText().toString();
    }
    public void setTitle(String value)
    {
        titleText.setText(value);
    }
    public String getQuestionText()
    {
        return interjection.getText().toString();
    }
    public void setQuestionText(String value)
    {
        interjection.setText(value);
    }
    public void addView(View view)
    {
        ((ViewGroup)interjection.getParent()).addView(view);
    }
    public void addView(View view, ViewGroup.LayoutParams layoutParams)
    {
        ((ViewGroup)interjection.getParent()).addView(view, layoutParams);
    }
}
