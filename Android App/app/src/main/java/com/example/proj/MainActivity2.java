package com.example.proj;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        drawerLayout=findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,myToolbar,
                R.string.navigation_draw_open,R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        PersonInfo pi = (PersonInfo) getIntent().getParcelableExtra("pi");
        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView name_last = (TextView) headerView.findViewById(R.id.name_lastname);
        TextView nickname = (TextView) headerView.findViewById(R.id.nickname);
        TextView color = (TextView) headerView.findViewById(R.id.color);
        TextView animal = (TextView) headerView.findViewById(R.id.animal);
        TextView movie = (TextView) headerView.findViewById(R.id.movie);
        //n1.setText("Your Text Here");
        //TextView nl=findViewById(R.id.name_lastname);
        //TextView cz=findViewById(R.id.city_zip);
        //TextView lang=findViewById(R.id.lang);
        name_last.setText(pi.getName()+" "+pi.getLastname());
        nickname.setText('"'+pi.getNickname()+'"');
        color.setText("Color: "+pi.getColor());
        animal.setText("Animal: "+pi.getAnimal());
        movie.setText("Movie: "+pi.getMovie());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_aboutme:
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
                break;
            case R.id.drawer_viewpager:
                Intent intent2 = new Intent(MainActivity2.this, Signin.class);
                startActivity(intent2);
                break;
            case R.id.drawer_masterdetail:
                mAuth.signOut();
                //finish();
                Intent intent3 = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appbar_moviefrag:
                Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                startActivity(intent);
                return true;
            case R.id.appbar_viewpager:
                //Intent intent = new Intent(this,ViewPager.class);
                //startActivity(intent);
                Intent intent2 = new Intent(MainActivity2.this, Signin.class);
                startActivity(intent2);
                return true;
            case R.id.appbar_masterdetail:
                //Intent intent2 = new Intent(this,MasterDetail.class);
                //startActivity(intent2);
                Intent intent3 = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick1(View view) {
        Intent intent = new Intent(this,PracticeFrag.class);
        startActivity(intent);
    }
    public void onClick2(View view) {
        Intent intent = new Intent(this,PlayFrag.class);
        startActivity(intent);
    }
}
