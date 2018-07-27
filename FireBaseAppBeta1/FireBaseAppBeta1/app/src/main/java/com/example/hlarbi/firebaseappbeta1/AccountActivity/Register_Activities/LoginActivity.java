package com.example.hlarbi.firebaseappbeta1.AccountActivity.Register_Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hlarbi.firebaseappbeta1.AccountActivity.MainClasses.MainActivity;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.APIClient;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.GetResquest;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.ServiceGenerator;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.objects.FitBitApi.Activities;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.Oauth_Api.api.objects.Oauth.AccessToken;
import com.example.hlarbi.firebaseappbeta1.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.example.hlarbi.firebaseappbeta1.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //Oauth Part
    private static final String TAG = "LoginActivity";

    public static  final String htExtra = "";
    public static final String useridExtra = "";
    public String dateFirst = "today";



    // TODO Replace this with your own data
    public static final String API_LOGIN_URL = "https://www.fitbit.com/oauth2/authorize?response_type=code";
    public static final String client_id = "22CXXD";
    public static final String client_secret = "54c50f95c9b2dab5471f31598e328c90";
    public static final String API_OAUTH_REDIRECT = "pulseyou://callback";
    public static final String content_type = "application/x-www-form-urlencoded";
    Bundle extras = new Bundle();
    TextView steps;
//Oauth Part


    public static Activities activities;
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    LinearLayout mLayout;
    AnimationDrawable animationDrawable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final Context cxt = this;
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.changement_login);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        //Animation Background
        mLayout = (LinearLayout) findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) mLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4500);
        animationDrawable.setExitFadeDuration(4500);
        animationDrawable.start();

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create(); //Read Update
                                    alertDialog.setTitle("Fitbit webpage");
                                    alertDialog.setMessage("You'll be redirected a moment into Fitbit Webpage.Please register to it.");

                                    alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // here you can add functions
                                            Intent intent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(API_LOGIN_URL + "&client_id=" + client_id + "&redirect_uri=" + API_OAUTH_REDIRECT + "&scope=activity&expired_in=604800"));
                                            // This flag is set to prevent the browser with the login form from showing in the history stack
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            //On ferme la le navi et maintenant on va passer à la méthode OnResume
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    alertDialog.show();  //<-- See This




                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String base2 = Credentials.basic(client_id, client_secret).substring(6);


        //Collect de données grace au lien uri
        Uri uri = getIntent().getData();
        //Si l'uri n'est pas nul on va attraper le code redonné par l'uri
        if(uri != null && uri.toString().startsWith(API_OAUTH_REDIRECT)) {
            String code = uri.getQueryParameter("code");
            if(code != null) {
                // TODO We can probably do something with this code! Show the user that we are logging them in

                final SharedPreferences prefs = this.getSharedPreferences(
                        BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
                //Nous appelons l'intercepteur (ServiceGenerator) qui va communiquer avec le serveur
                APIClient client = ServiceGenerator.createService(APIClient.class);
                //Pour avoir le token d'acces nous devons envoyer certains parametre définis dans la document pas FitBit
                Call<AccessToken> call = client.getNewAccessToken("authorization_code",
                        client_id,
                        API_OAUTH_REDIRECT,
                        code,
                        base2);
                //Nous demandons la réponse par la commande suivante :
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(final Call<AccessToken> call, Response<AccessToken> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {

                            //Nous obtenons le token d'acces ainsi que d'autres paramètres
                            final AccessToken token = response.body();
                            prefs.edit().putBoolean("oauth.loggedin", true).apply();
                            prefs.edit().putString("oauth.accesstoken", token.getAccessToken()).apply();
                            prefs.edit().putString("oauth.refreshtoken", token.getRefreshToken()).apply();
                            prefs.edit().putString("oauth.tokentype", token.getTokenType()).apply();
                            prefs.edit().putString("oauth.tokentype", token.getUser_ID()).apply();

                            //Dès à présent nous commencons à construire le Header qui nous permettra de demander les Datas

                            final String headertoken = " " + String.valueOf(token.getTokenType()) + " " + String.valueOf(token.getAccessToken());
                            // TODO Show the user they are logged in

                            //ActivitiesCall
                            final GetResquest clientg = ServiceGenerator.createService(GetResquest.class);

                            final Map<String, String> map = new HashMap<>();

                            map.put("Authorization", headertoken);
                            // Toast.makeText(LoginActivity.this, String.valueOf(map), Toast.LENGTH_SHORT).show();


                            final Call<Activities> callg = clientg.getActivitiesData(map,token.getUser_ID(),dateFirst);
                            callg.enqueue(new Callback<Activities>() {


                                @Override
                                public void onResponse(Call<Activities> call, Response<Activities> response) {
                                    activities =response.body();
                                    String usId = String.valueOf(token.getUser_ID());
                                    String oauthHeader = headertoken +"²"+ usId;
                                    String steps = String.valueOf(activities.getSummary().getSteps());
                                   Intent intenttoken = new Intent(LoginActivity.this, MainActivity.class);
                                    //extras.putString(useridExtra, oauthHeader);
                                    //intenttoken.putExtras(extras);
                                    startActivity(intenttoken);
                                }

                                @Override
                                public void onFailure(Call<Activities> call, Throwable t) {

                                }
                            });

                         /*   mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                    month = month + 1;
                                    Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                                    String date = year + "-" + month + "-" + day;
                                    chosenDate.setText(date);

                                    final Call<Activities>calld = clientg.getActivitiesData(map,token.getUser_ID(),String.valueOf(date));
                                    calld.enqueue(new Callback<Activities>() {
                                        @Override
                                        public void onResponse(Call<Activities> call, Response<Activities> response) {
                                            Activities activities =response.body();
                                            textView.setText(String.valueOf(activities.getSummary().getSteps()));
                                        }

                                        @Override
                                        public void onFailure(Call<Activities> call, Throwable t) {

                                        }
                                    });

                                }

                            };*/

                        } else {
                            // TODO Handle a missing code in the redirect URI
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {

                    }

                });
            }
        }

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }




    }
}
