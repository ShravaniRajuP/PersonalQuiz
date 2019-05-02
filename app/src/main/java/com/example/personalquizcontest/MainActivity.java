package com.example.personalquizcontest;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static final String USERID = "userid";
    public static final String SCORE = "score";
    EditText firstname;
    EditText nickname;
    EditText lastname;
    EditText age;
    Button submit;
    TextView score;

    // Creating First activity - User info activity
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstname = (EditText) findViewById(R.id.fname);
        nickname = (EditText) findViewById(R.id.nname);
        lastname = (EditText) findViewById(R.id.lname);
        age = (EditText) findViewById(R.id.age);
        score = (TextView) findViewById(R.id.score_report);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                submit();
            }
        });
    }

    private DbConnection getDBConnection() {
        return new DbConnection(getApplicationContext());
    }

    @Override
    protected void onPause(){
        super.onPause();
        User user = new User(firstname.getText().toString(), lastname.getText().toString(), nickname.getText().toString(), Integer.parseInt(age.getText().toString()));
        DbConnection db = new DbConnection(getApplicationContext());
        db.saveData(user);
    }

    @Override
    protected void onResume(){
        super.onResume();
        ArrayList<User> users;
        DbConnection db = new DbConnection(getApplicationContext());
        users = db.loadData();
        if(users.size() > 0){
            firstname.setText(users.get(0).getFirstname());
            lastname.setText(users.get(0).getLastname());
            nickname.setText(users.get(0).getNickname());
            age.setText(users.get(0).getAge()+ "");
        }
    }

    // On clicking the submit - "Start Quiz" button
    public void submit() {
        if(firstname.getText().toString().trim().equals("") || lastname.getText().toString().trim().equals("") || nickname.getText().toString().trim().equals("") || age.getText().toString().trim().equals("")){
            Toast.makeText(MainActivity.this, "Please input values for all fields!!!", Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User(firstname.getText().toString(), lastname.getText().toString(), nickname.getText().toString(), Integer.parseInt(age.getText().toString()));
        int userId = getDBConnection().insertUser(user);
        goToPersonalQuizActivity(userId);
    }

    // Starting Quiz for the user with userid
    private void goToPersonalQuizActivity(int userid) {
        Intent intent = new Intent(MainActivity.this, PersonalQuiz.class);
        intent.putExtra(USERID, userid);
        startActivityForResult(intent, 1);
    }

    // Saving user score into DB
    private void saveToDB(int score, int userid) {
        DbConnection db = new DbConnection(getApplicationContext());
        db.saveToDB(score, userid, Calendar.getInstance().getTime().toString());
    }

    // Printing Score onto activity screen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            if (data != null)  {
                score.setText(data.getIntExtra(SCORE, 0) + "");
                saveToDB(data.getIntExtra(SCORE, 0), data.getIntExtra(USERID, 0));
            }
        }
        else {
                score.setText("0");
        }
    }
}