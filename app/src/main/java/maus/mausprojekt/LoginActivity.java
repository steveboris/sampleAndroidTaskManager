package maus.mausprojekt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView passwd, email;
    Button login_button;
    //public static TodoDatabase todoDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //todoDatabase = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, "tododb").build();
        login_button = findViewById(R.id.button_activity_login);
        login_button.setEnabled(false);
        email = findViewById(R.id.email_activity_input);
        passwd = findViewById(R.id.passwd_activity_login);

        CatchTextInput(email, login_button);
        CatchTextInput(passwd, login_button);
       login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO check id input is email in real time
                String emailStr = email.getText().toString().trim();

                if (Patterns.EMAIL_ADDRESS.matcher(emailStr).matches() && passwd.getText().toString().length()==6) {
                    Log.i(LoginActivity.class.getName(), "onClick: " + v);
                    startActivity(new Intent( LoginActivity.this, ListViewActivity.class));
                }
                else if(emailStr.matches("") && passwd.getText().toString().matches("")){
                    email.setError("Email not entered");
                    passwd.setError("Password cannot be empty!");
                    CatchTextInput(email, login_button);
                    CatchTextInput(passwd, login_button);
                }
                else if(!(Patterns.EMAIL_ADDRESS.matcher(emailStr).matches())){
                    email.setError("Please input valid email!");
                    CatchTextInput(email, login_button);
                }
                else if(passwd.getText().toString().isEmpty() || passwd.getText().toString().length()!=6){
                    passwd.setError("Password too short or empty!");
                    CatchTextInput(passwd, login_button);

                }
                else{
                    email.setError("Invalid Email or Password");
                    CatchTextInput(email, login_button);

                }
            }
        });
    }
    public void CatchTextInput(TextView textView, final Button button){
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.toString().trim().length()==0 || s.equals("")){
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}

