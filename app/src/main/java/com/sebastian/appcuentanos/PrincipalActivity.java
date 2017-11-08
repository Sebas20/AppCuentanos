package com.sebastian.appcuentanos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sebastian.appcuentanos.Clases.CircleTransform;
import com.sebastian.appcuentanos.Clases.Usuario;
import com.sebastian.appcuentanos.Fragments.AddResFragment;
import com.sebastian.appcuentanos.Fragments.BottonNavigationFragment;
import com.sebastian.appcuentanos.Fragments.CategoriasFragment;
import com.sebastian.appcuentanos.Fragments.CheckInFragment;
import com.sebastian.appcuentanos.Fragments.ConfiguracionFragment;
import com.sebastian.appcuentanos.Fragments.ContenedorFragment;
import com.sebastian.appcuentanos.Fragments.InfoSiteFragment;
import com.sebastian.appcuentanos.Fragments.ListFragment;
import com.sebastian.appcuentanos.Fragments.PerfilFragment;
import com.sebastian.appcuentanos.Fragments.ResenasFragment;
import com.sebastian.appcuentanos.Fragments.SitesFragment;
import com.sebastian.appcuentanos.Interfaces.InterfaceSite;

import de.hdodenhof.circleimageview.CircleImageView;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PerfilFragment.MyInterface,CategoriasFragment.MyInterfaceCategorias,InterfaceSite {

    private GoogleApiClient googleApiClient;
    private String correo_go, nombre_go, img_url_go="";
    Fragment fragment = null;
    FragmentManager fm;
    FragmentTransaction ft;
    CircleImageView imagen_menu;
    TextView tNombre_menu, tCorreo_menu;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser User;
    ListFragment listFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ///Firebase
         User = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        if (User != null) {
            reference = database.getReference("usuarios");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(User.getUid()).exists()) {
                        Usuario usuario = dataSnapshot.child(User.getUid()).getValue(Usuario.class);
                        nombre_go = usuario.getNombre();
                        correo_go = usuario.getCorreo();
                        img_url_go = usuario.getUrl_foto();
                        mostrar_nav();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        ///////////
        ///Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            if (result.isSuccess()) {
                handleSignInResult(result);
            }

        }
        ////////

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);

        tCorreo_menu = (TextView) hView.findViewById(R.id.tCorreo_menu);
        tNombre_menu = (TextView) hView.findViewById(R.id.tNombre_menu);
        imagen_menu = (CircleImageView) hView.findViewById(R.id.imageView);

        mostrar_nav();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.content_main);

        if (fragment == null) {
            ft = fm.beginTransaction();
            fragment = new ContenedorFragment();
            ft.add(R.id.content_main, fragment, "PRINCIPAL");
            ft.commit();
        }

    }

    private void mostrar_nav() {
        tCorreo_menu.setText(correo_go);
        tNombre_menu.setText(nombre_go);
        if(img_url_go.isEmpty()){
            imagen_menu.setImageResource(R.drawable.ic_person);
        }else{
            Glide.with(this.getApplicationContext())
                    .load(img_url_go)
                    .into(imagen_menu);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_resenas) { ////Reseñas
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ResenasFragment resenasFragment = (ResenasFragment) getSupportFragmentManager().findFragmentByTag("RESENAS");
            if (resenasFragment != null && resenasFragment.isVisible()) {
            } else {
                ResenasFragment resenas = new ResenasFragment();
                ft.replace(R.id.content_main, resenas, "RESENAS");
                ft.commit();
            }
        } else if (id == R.id.nav_checkin) { ///Check_in
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            CheckInFragment checkInFragment = (CheckInFragment) getSupportFragmentManager().findFragmentByTag("CHECK-IN");

            if (checkInFragment != null && checkInFragment.isVisible()) {
            } else {
                CheckInFragment check = new CheckInFragment();
                ft.replace(R.id.content_main, check, "CHECK-IN");
                ft.commit();
            }
        } else if (id == R.id.nav_configurar) {////Configuración
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ConfiguracionFragment configuracionFragment = (ConfiguracionFragment) getSupportFragmentManager().findFragmentByTag("CONFIGURACION");
            if (configuracionFragment != null && configuracionFragment.isVisible()) {
            } else {
                ConfiguracionFragment configuracion = new ConfiguracionFragment();
                ft.replace(R.id.content_main, configuracion, "CONFIGURACION");
                ft.commit();
            }
        } else if (id == R.id.nav_perfil) {/////// Perfil
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            PerfilFragment perfilFragment = (PerfilFragment) getSupportFragmentManager().findFragmentByTag("PERFIL");
            if (perfilFragment != null && perfilFragment.isVisible()) {
            } else {
                PerfilFragment perfil = new PerfilFragment();
                ft.replace(R.id.content_main, perfil, "PERFIL");
                ft.commit();
            }


        } else if (id == R.id.nav_principal) {///// Principal
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ContenedorFragment contenedorFragment = (ContenedorFragment) getSupportFragmentManager().findFragmentByTag("PRINCIPAL");
            if (contenedorFragment != null && contenedorFragment.isVisible()) {
            } else {
                ContenedorFragment principal = new ContenedorFragment();
                ft.replace(R.id.content_main, principal, "PRINCIPAL");
                ft.commit();
            }

        } else if (id == R.id.nav_cerrar_sesion) { ///Cerrar Sesión
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            signOut();
            goLoginActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
            }
        });
    }

    private void goLoginActivity() {
        Intent intent = new Intent(PrincipalActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            //nombre_go = account.getDisplayName();
            //correo_go = account.getEmail();
            if (account.getPhotoUrl() != null) {
                img_url_go = account.getPhotoUrl().toString();

            } else {
                img_url_go = "vacia";
            }

        }
    }

    @Override
    public void onItemSelected(int position) {
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView tNombre_menu, tCorreo_menu;
        tCorreo_menu = (TextView) hView.findViewById(R.id.tCorreo_menu);
        tNombre_menu = (TextView) hView.findViewById(R.id.tNombre_menu);
        imagen_menu = (CircleImageView) hView.findViewById(R.id.imageView);
        nombre_go = sharedPreferences.getString("nombre", "Usuario");
        String direccion = sharedPreferences.getString("imagen", "");
        Log.d("carlos", direccion);
        tCorreo_menu.setText(correo_go);
        tNombre_menu.setText(nombre_go);
        if (!direccion.isEmpty()) {
            imagen_menu.setImageURI(Uri.parse(direccion));
        }

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }



    @Override
    protected void onDestroy() {
        Glide.clear(imagen_menu);
        super.onDestroy();
    }


    @Override
    public void onItemCategoria(String categoria) {
        fm = getSupportFragmentManager();
        SitesFragment sitesFragment = new SitesFragment();
        ft = fm.beginTransaction();
        ft.replace(R.id.content_main,sitesFragment).commit();
    }

    @Override
    public void searchSite(String id) {
        Bundle args = new Bundle();
        args.putString("id",id);
        listFragment.dismiss();
        ft = fm.beginTransaction();
        InfoSiteFragment fragment = new InfoSiteFragment();
        fragment.setArguments(args);
        ft.replace(R.id.content_main,fragment).commit();
        ft.addToBackStack("ListCat");
    }

    @Override
    public void listSite(String name) {
        ft=fm.beginTransaction();
        listFragment = ListFragment.newInstance(name);
        listFragment.show(ft,"List");
    }

    @Override
    public void aregarReseña(String idSite) {
        Bundle args = new Bundle();
        args.putString("idSite",idSite);
        ft = fm.beginTransaction();
        AddResFragment addResFragment = new AddResFragment();
        addResFragment.setArguments(args);
        ft.replace(R.id.content_main,addResFragment).commit();
        ft.addToBackStack("InfoSite");

    }
}
