package com.alejandro.clase.dogypark;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Created by clase on 08/02/2018.
 */

public class SecondActivity extends AppCompatActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing the tablayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Usuario"));
        tabLayout.addTab(tabLayout.newTab().setText("Maps"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager());

        MapsActivity frag_maps = new MapsActivity();
        Usuario frag_usuario = new Usuario();

        Intent intent = getIntent();
        if(intent!=null){
            if(intent.getBooleanExtra("doShowMap",false)){
                Log.d("changetab", "setargument");
                Parquesfavoritos parque= new Parquesfavoritos();
                parque=parque.toObject(intent.getStringExtra("parque"));
                if (parque!= null){
                    Bundle bundle = new Bundle();
                    bundle.putString("parque", parque.toString());
                    frag_maps.setArguments(bundle);

                }
            }
        }
        adapter.addFrag(frag_usuario);
        adapter.addFrag(frag_maps);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //Toast.makeText(getApplicationContext(),""+tab.getPosition(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if(intent!=null){
            if(intent.getBooleanExtra("doShowMap",false)){
                tabLayout.getTabAt(1).select();
                Log.d("changetab", "cambiando tab a maps");
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tabbed, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(getApplicationContext(),settings.class));
                return true;
            /*case R.id.test:
                Toast.makeText(this,"Funciona", Toast.LENGTH_SHORT).show();
                return true;*/
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }

}
