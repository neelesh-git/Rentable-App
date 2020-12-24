package com.rentable.ViewProduct;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rentable.DataBaseHelper.IuserData;
import com.rentable.DataBaseHelper.ProductInfo;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.HomeScreen.HomeScreenNavigation.SettingsActivity;
import com.rentable.ProductCategory.BooksFeed;
import com.rentable.ProductCategory.CategoriesActivity;
import com.rentable.ProductInfo.ProductInfoActivity;
import com.rentable.R;
import com.rentable.SessionContext;
import com.rentable.rentableAuthentication.LoginActivity.LoginActivity;
import com.rentable.rentableAuthentication.LoginActivity.SharePrefenceHelper;
import com.squareup.picasso.Picasso;

public class RentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    SharePrefenceHelper mSharePrefenceHelper;
    private AppBarConfiguration mAppBarConfiguration;
    Query ProductQ;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);


        IuserData userData = SessionContext.getUserData();
        String email = userData.getEmail();


        ProductQ = FirebaseDatabase.getInstance().getReference().child("userData").orderByChild("email").equalTo(email);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Order Placed");
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_profile_name);


        String email1 = SessionContext.getEmail();
        userName.setText(email1);

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<ProductInfo> options =  new FirebaseRecyclerOptions.Builder<ProductInfo>()
                .setQuery(ProductQ,ProductInfo.class)
                .build();


        FirebaseRecyclerAdapter<ProductInfo, ProductView > adapter =
                new FirebaseRecyclerAdapter<ProductInfo, ProductView>(options)
                {


                    @NonNull
                    @Override
                    public ProductView onCreateViewHolder(ViewGroup parent, int viewType )
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rent_successful,parent, false);


                        ProductView holder = new ProductView(view);


                        return holder;
                    }



                    @Override
                    protected void onBindViewHolder(@NonNull final ProductView holder, int i, @NonNull ProductInfo model) {


                        holder.mRentSuccessful.setText("Your rent request is successfull and your product will be shipped to you shortly");

                        Toast.makeText(RentActivity.this, "Order Placed Successfull" , Toast.LENGTH_SHORT).show();
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }



    @SuppressWarnings("Navigation")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        if(id == R.id.nav_rent)
        {
            Intent intent = new Intent(RentActivity.this, ProductInfoActivity.class);
            intent.putExtra("LABELTYPE", "VEHICLES");
            startActivity(intent);
        }
        else if(id == R.id.nav_categories)
        {
            Intent intent = new Intent(RentActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_user_settings)
        {
            Intent intent = new Intent(RentActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_logout)
        {
            FirebaseAuth.getInstance().signOut();
            mSharePrefenceHelper = SharePrefenceHelper.getInstance(getApplicationContext());
            mSharePrefenceHelper.clear();
            Toast.makeText(RentActivity.this, "You Logged out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }
        else if(id == R.id.nav_posted_items)
        {

            Intent intent = new Intent(RentActivity.this, BooksFeed.class);
            intent.putExtra("TYPE", "userId");
            intent.putExtra("FILTER", SessionContext.getEmail().split("@")[0]);
            startActivity(intent);
        }
        else if(id == R.id.nav_homefeed)
        {

            Intent intent = new Intent(RentActivity.this, NavigationActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
