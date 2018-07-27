package com.example.hlarbi.app3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.hlarbi.app3.API.APIClient;
import com.example.hlarbi.app3.API.GetResquest;
import com.example.hlarbi.app3.API.ServiceGenerator;
import com.example.hlarbi.app3.API.objects.FitBitApi.Activities;
import com.example.hlarbi.app3.API.objects.Oauth.AccessToken;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstActivity extends AppCompatActivity {

    public static final String useridExtra = "";
    public String dateFirst = "today";

    // TODO Replace this with your own data
    public static final String API_LOGIN_URL = "https://www.fitbit.com/oauth2/authorize?response_type=code";
    public static final String client_id = "22CT2D";
    public static final String client_secret = "1a26ad3ac2d4fb2a8cfa7410bd5847bb";
    public static final String API_OAUTH_REDIRECT = "futurestudio://callback";
    public static final String content_type = "application/x-www-form-urlencoded";

    Bundle extras = new Bundle();
    EditText edtName, edtNumber;
    Button btnChoose, btnAdd, btnList;
    ImageView imageView;

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstactivity);
////////////////////////////DATA BASE/////////////////////////
        btnAdd = (Button)findViewById(R.id.btnAdd);

        sqLiteHelper = new SQLiteHelper(this, "RunningDB.sqlite", null, 1);

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RUNNING(Id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, number VARCHAR)");


        ////////////////////////////DATA BASE////////////////////////

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(API_LOGIN_URL + "&client_id=" + client_id + "&redirect_uri=" + API_OAUTH_REDIRECT + "&scope=activity&expired_in=604800"));
                // This flag is set to prevent the browser with the login form from showing in the history stack
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);

                //On ferme la le navi et maintenant on va passer à la méthode OnResume

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SQLiteDatabase db = sqLiteHelper.getWritableDatabase();

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

                            //Nous obtenons le tokenUser_id d'acces ainsi que d'autres paramètres
                            final AccessToken tokenUser_id = response.body();
                            prefs.edit().putBoolean("oauth.loggedin", true).apply();
                            prefs.edit().putString("oauth.accesstoken", tokenUser_id.getAccessToken()).apply();
                            prefs.edit().putString("oauth.refreshtoken", tokenUser_id.getRefreshToken()).apply();
                            prefs.edit().putString("oauth.tokentype", tokenUser_id.getTokenType()).apply();
                            prefs.edit().putString("oauth.tokentype", tokenUser_id.getUser_ID()).apply();

                            //Dès à présent nous commencons à construire le Header qui nous permettra de demander les Datas

                            final String headertoken = " " + String.valueOf(tokenUser_id.getTokenType()) + " " + String.valueOf(tokenUser_id.getAccessToken());
                            // TODO Show the user they are logged in

                            //ActivitiesCall
                            final GetResquest clientg = ServiceGenerator.createService(GetResquest.class);

                            final Map<String, String> map = new HashMap<>();

                            map.put("Authorization", headertoken);
                            final Call<Activities> callg = clientg.getActivitiesData(map,tokenUser_id.getUser_ID(),dateFirst);
                            callg.enqueue(new Callback<Activities>() {

                                @Override
                                public void onResponse(Call<Activities> call, Response<Activities> response) {
                                    Activities activities =response.body();
                                    String usId = String.valueOf(tokenUser_id.getUser_ID());
                                    String oauthHeader = headertoken +"²"+ usId;



                                    String steps = String.valueOf(activities.getSummary().getSteps());
                                    String calo= String.valueOf(activities.getSummary().getCaloriesOut());
                                    String distance= String.valueOf(activities.getSummary().getDistances().get(1).getDistance());
                                    String dura= String.valueOf(activities.getSummary().getSedentaryMinutes());
                                    String floors= String.valueOf(activities.getSummary().getFloors());
                                    String height= String.valueOf(activities.getSummary().getElevation());

                                    int count = sqLiteHelper.getProfilesCount();
                                    if (count==0){
                                        sqLiteHelper.insertData( "Steps",steps);
                                        sqLiteHelper.insertData( "Calo",calo);
                                        sqLiteHelper.insertData( "Distance",distance);
                                        sqLiteHelper.insertData( "Dura",dura);
                                        sqLiteHelper.insertData( "Floors",floors);
                                        sqLiteHelper.insertData( "Height",height);


                                        Intent intenttoken = new Intent(FirstActivity.this, MainActivity.class);
                                        extras.putString(useridExtra, oauthHeader);
                                        intenttoken.putExtras(extras);
                                        startActivity(intenttoken);
                                    }
                                    if(count==6){
                                        ContentValues cvSteps = new ContentValues();
                                        ContentValues cvCalo = new ContentValues();
                                        ContentValues cvDis = new ContentValues();
                                        ContentValues cvDura = new ContentValues();
                                        ContentValues cvFloors = new ContentValues();
                                        ContentValues cvHeight = new ContentValues();

                                        cvSteps.put("number",steps);
                                        cvCalo.put("number",calo);
                                        cvDis.put("number",distance);
                                        cvDura.put("number",dura);
                                        cvFloors.put("number",floors);
                                        cvHeight.put("number",height);

                                        db.update("RUNNING", cvSteps, "id = 1", null);
                                        db.update("RUNNING", cvCalo, "id = 2", null);
                                        db.update("RUNNING", cvDis, "id = 3", null);
                                        db.update("RUNNING", cvDura, "id = 4", null);
                                        db.update("RUNNING", cvFloors, "id = 5", null);
                                        db.update("RUNNING", cvHeight, "id = 6", null);

                                        // Intent intenttoken = new Intent(FirstActivity.this, RunningList.class);


                                        Intent intenttoken = new Intent(FirstActivity.this, MainActivity.class);
                                        extras.putString(useridExtra, oauthHeader);
                                        intenttoken.putExtras(extras);
                                        startActivity(intenttoken);



                                    }

                                }

                                @Override
                                public void onFailure(Call<Activities> call, Throwable t) {

                                }

                            });

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






    }


}