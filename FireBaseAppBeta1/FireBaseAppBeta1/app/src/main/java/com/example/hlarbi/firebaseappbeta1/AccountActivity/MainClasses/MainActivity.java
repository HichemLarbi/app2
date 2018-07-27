package com.example.hlarbi.firebaseappbeta1.AccountActivity.MainClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses.FragmentOne;
import com.example.hlarbi.firebaseappbeta1.AccountActivity.ViewClasses.FragmentTwo;
import com.example.hlarbi.firebaseappbeta1.R;


public class MainActivity extends AppCompatActivity {

    //<Fragments
    private BottomNavigationView mMainnav;


    ///>Fragments
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mMainnav = (BottomNavigationView) findViewById(R.id.navigation);
            switch (item.getItemId()) {
                case R.id.navigation_run:

                    FragmentOne fragmentOne = new FragmentOne();
                    android.support.v4.app.FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction1.replace(R.id.fram, fragmentOne, "FragmentName");
                    fragmentTransaction1.commit();
                    mMainnav.setItemBackgroundResource(R.color.june4);

                   return true;

                case R.id.navigation_pollu:
                    FragmentTwo fragmentTwo = new FragmentTwo();
                    android.support.v4.app.FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.replace(R.id.fram, fragmentTwo, "FragmentName");
                    fragmentTransaction2.commit();
                    mMainnav.setItemBackgroundResource(R.color.bleu);

                    return true;

                case R.id.navigation_history:
                    Toast.makeText(MainActivity.this,"3",Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.navigation_profil:
                    Toast.makeText(MainActivity.this,"4",Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }
    };

     @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_main);
         AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

         BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
         navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        }
     }


