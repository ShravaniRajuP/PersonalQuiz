package com.example.personalquizcontest;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PersonalQuiz extends AppCompatActivity implements Listener {

    public static final String USERID = "userid";
    public static final String QID = "qid";
    public static final String QUE = "que";
    public static final String OPT_1 = "opt1";
    public static final String OPT_2 = "opt2";
    public static final String OPT_3 = "opt3";
    public static final String OPT_4 = "opt4";
    public static final String NEXT = "next";
    public static final String FINISH = "Finish";
    public static final String Q_FRAG = "qFrag";
    public static final String QUESTION = "Question";
    List<Question> ques = null;

    // Creating Second activity - Quiz activity - fragment
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_quiz);
        ques = getQuestions();
        questionFrag(1);
    }

    // Loading the Json file
    private String load() {
        String json = null;
        try {
            final AssetManager assets = getApplicationContext().getAssets();
            final String[] names = assets.list( "" );
            InputStream is = getApplicationContext().getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    // Fetching questions one by one from the Json File
    private List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        try {
            JSONObject obj = new JSONObject(load());
            JSONArray questionJSON = obj.getJSONArray("questions");
            for (int i = 0; i < questionJSON.length(); i++){
                JSONObject jsonObject = questionJSON.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String question = jsonObject.getString("question");
                int answer = jsonObject.getInt("answer");
                String option1 = jsonObject.getString("option1");
                String option2 = jsonObject.getString("option2");
                String option3 = jsonObject.getString("option3");
                String option4 = jsonObject.getString("option4");
                Question question1 = new Question();
                question1.setId(id);
                question1.setOpt1(option1);
                question1.setOpt2(option2);
                question1.setOpt3(option3);
                question1.setOpt4(option4);
                question1.setQuestion(question);
                question1.setAnswer(answer);
                question1.setIsCorrect(false);
                questions.add(question1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // Creating Fragment view in .xml
    public void questionFrag(int nextQues) {
        QFragment qFragment = new QFragment();
        for (Question que: ques) {
            if (que.getId() == nextQues) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                qFragment.setArguments(getBundle(que, nextQues));
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.quiz_fragment, qFragment, Q_FRAG);
                transaction.commit();
            }
        }
    }

    // Creating bundle of questions
    private Bundle getBundle(Question que, int qid) {
        Bundle bundle = new Bundle();
        bundle.putInt(QID, que.getId());
        bundle.putString(QUE, que.getQuestion());
        bundle.putString(OPT_1, que.getOpt1());
        bundle.putString(OPT_2, que.getOpt2());
        bundle.putString(OPT_3, que.getOpt3());
        bundle.putString(OPT_4, que.getOpt4());
        if (qid == ques.size()) {
            bundle.putString(NEXT, FINISH);
        } else {
            bundle.putString(NEXT, NEXT);
        }
        return bundle;
    }

    // Calculating Score
    @Override
    public void send(int qid, int answer) {
        updateScore(qid-1, answer);
        QFragment qFragment = new QFragment();
        if (qid > ques.size()) {
            showScore();
        } else {
            for (Question que: ques) {
                if (que.getId() == qid) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    qFragment.setArguments(getBundle(que, qid));
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.quiz_fragment, qFragment, Q_FRAG);
                    transaction.addToBackStack(QUESTION);
                    transaction.commit();
                }
            }
        }
    }

    // Correct answer
    private void updateScore(int id, int ans) {
        for (Question que: ques) {
            if (que.getId() == id && que.getAnswer() == ans+1) {
                que.setCorrect(true);
            }
        }
    }

    // Calculating Score
    private int getScore() {
        int score = 0;
        for (int i=0; i<ques.size(); i++) {
            if (ques.get(i).getIsCorrect()) {
                score++;
            }
        }
        return score;
    }

    // Showing score on activity layout
    private void showScore() {
        Intent data = new Intent();
        data.putExtra("score", getScore());
        data.putExtra(USERID, getIntent().getExtras().getInt(USERID, 0));
        setResult(RESULT_OK, data);
        finish();
    }
}
