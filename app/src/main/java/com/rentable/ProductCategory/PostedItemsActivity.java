package com.rentable.ProductCategory;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rentable.DataBaseHelper.ProductInfo;
import com.rentable.HomeScreen.HomeScreenNavigation.NavigationActivity;
import com.rentable.HomeScreen.HomeScreenNavigation.SettingsActivity;
import com.rentable.ProductInfo.ProductInfoActivity;
import com.rentable.R;
import com.rentable.SessionContext;
import com.rentable.ViewProduct.ProductView;
import com.rentable.ViewProduct.RentActivity;
import com.rentable.rentableAuthentication.LoginActivity.LoginActivity;
import com.rentable.rentableAuthentication.LoginActivity.SharePrefenceHelper;
import com.squareup.picasso.Picasso;

public class PostedItemsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    SharePrefenceHelper mSharePrefenceHelper;
    private AppBarConfiguration mAppBarConfiguration;


    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Query ProductsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_feed);

        Intent intent = getIntent();

        String filter = intent.getStringExtra("FILTER");
        String type = intent.getStringExtra("TYPE");

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("productInfo").orderByChild(type).equalTo(filter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if(type.equalsIgnoreCase("userId")) {

            toolbar.setTitle("Your Posted Items");
        }
        else
        {
            toolbar.setTitle(filter);
        }

        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_profile_name);


        String email = SessionContext.getEmail();
        userName.setText(email);


        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(PostedItemsActivity.this, NavigationActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<ProductInfo> options =  new FirebaseRecyclerOptions.Builder<ProductInfo>()
                .setQuery(ProductsRef,ProductInfo.class)
                .build();

        FirebaseRecyclerAdapter<ProductInfo, ProductView> adapter =
                new FirebaseRecyclerAdapter<ProductInfo, ProductView>(options)
                {


                    @NonNull
                    @Override
                    public ProductView onCreateViewHolder(ViewGroup parent, int viewType )
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.posted_items_layout,parent, false);
                        ProductView holder = new ProductView(view);

                        return holder;
                    }



                    @Override
                    protected void onBindViewHolder(@NonNull ProductView holder, int i, @NonNull ProductInfo model) {
                        holder.mProductName.setText(model.getProcuctName());
                        holder.mProductDescription.setText(model.getCategory().toString()+ "\n" + " Posted On : " + model.getPostedDate() + "\n" + " Product Id : " + model.getProductId());
                        holder.mProductPrice.setText("Price = " + model.getPrice() + "$");

                        Picasso.get().load(model.getImageId()).into(holder.imageView);



                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        if(id == R.id.nav_rent)
        {
            Intent intent = new Intent(PostedItemsActivity.this, ProductInfoActivity.class);
            intent.putExtra("LABELTYPE", "VEHICLES");
            startActivity(intent);
        }
        else if(id == R.id.nav_categories)
        {
            Intent intent = new Intent(PostedItemsActivity.this, CategoriesActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_user_settings)
        {
            Intent intent = new Intent(PostedItemsActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_logout)
        {
            FirebaseAuth.getInstance().signOut();
            mSharePrefenceHelper = SharePrefenceHelper.getInstance(getApplicationContext());
            mSharePrefenceHelper.clear();
            Toast.makeText(PostedItemsActivity.this, "You Logged out Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();

        }
        else if(id == R.id.nav_posted_items)
        {
            Intent intent = new Intent(PostedItemsActivity.this, PostedItemsActivity.class);
            intent.putExtra("TYPE", "userId");
            intent.putExtra("FILTER", SessionContext.getEmail().split("@")[0]);
            startActivity(intent);
        }
        else if(id == R.id.nav_homefeed)
        {

            Intent intent = new Intent(PostedItemsActivity.this, NavigationActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
