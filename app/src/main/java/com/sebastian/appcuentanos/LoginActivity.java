package com.sebastian.appcuentanos;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sebastian.appcuentanos.Clases.PromoVo;
import com.sebastian.appcuentanos.Clases.Usuario;

import org.json.JSONObject;

import java.sql.Array;
import java.util.Arrays;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    String correoR = "", contrasenaR = "";
    String Correo = "", Contrasena = "";
    String nuevaContraseña = "";
    String[] Errores;
    int flag_face = 0;
    int flag_User = 0;
    int flag_recuperar = 0;
    int cont = 0;
    ////Clases
    Usuario usuario;
    //////////
    ////Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    FirebaseDatabase database;
    DatabaseReference reference;
    StorageReference storageReference;
    ////////////
    ///Login Google
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    ///////
    ///Login Facebook
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    ///////
    EditText eCorreo, eContrasena;
    ImageView iconoCorreo, iconoPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final Resources res = getResources();
        Errores = res.getStringArray(R.array.Errores);
        eCorreo = (EditText) findViewById(R.id.eCorreo);
        eContrasena = (EditText) findViewById(R.id.eContrasena);
        iconoCorreo = (ImageView) findViewById(R.id.iIcono_correo);
        iconoPassword = (ImageView) findViewById(R.id.iIcono_password);
        eCorreo.setFocusable(true);
        eContrasena.setFocusable(true);

        ///Login Facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                obtenerToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Loggin Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error en la Conexión", Toast.LENGTH_SHORT).show();
            }
        });
        ///////////

        //////Firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser User = firebaseAuth.getCurrentUser();
                if (User != null) {
                    final String Uid = User.getUid();
                    String nombre = User.getDisplayName();
                    final String correo = User.getEmail();
                    String url = "";
                    String proveedor = User.getProviders().toString();
                    if (!proveedor.equals("[password]")) {
                        url = User.getPhotoUrl().toString();
                    }
                    if (proveedor.equals("[password]")) {
                        nombre = "Usuario";
                    }
                    final String finalNombre = nombre;
                    final String finalUrl = url;
                    reference = database.getReference("usuarios");
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(User.getUid()).exists()) {
                                goMainActivity();
                            }else{
                                Usuario usuario = new Usuario(Uid, finalNombre, correo, "", "", finalUrl);
                                reference = database.getReference("usuarios").child(Uid);
                                reference.setValue(usuario);
                                goMainActivity();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        };

        ///////////////
        //Login Google
        signInButton = (SignInButton) findViewById(R.id.bGoogle);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this, LoginActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, 7777);
            }
        });

        eContrasena.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    iconoPassword.setImageResource(R.drawable.ic_password_rojo);
                } else {
                    iconoPassword.setImageResource(R.drawable.ic_password);
                }
            }
        });

        eCorreo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    iconoCorreo.setImageResource(R.drawable.ic_email_rojo);
                } else {
                    iconoCorreo.setImageResource(R.drawable.ic_email);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void obtenerToken(AccessToken accessToken) {
        ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setTitle("Cargando");
        progress.setMessage("Espere por Favor...");
        progress.setCancelable(false);
        progress.show();
        AuthCredential credencial = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "No se pudo Autenticar con Firebase", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(LoginActivity.this, "Error al Intentar Loguearse con Google", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7777) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == 7890 && resultCode == RESULT_OK) {
            nuevaContraseña = data.getExtras().getString("codigo");
            flag_recuperar = 1;
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void SigInEmailAndPassword(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Verificando");
        progressDialog.setMessage(getResources().getString(R.string.espera));
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.usuario_no_logueado), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //goMainActivity();
            FirebaseWithGoogle(result.getSignInAccount());
        } else {
            Toast.makeText(LoginActivity.this, "Inicio de Sesión Cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseWithGoogle(GoogleSignInAccount signInAccount) {
        ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setTitle("Cargando");
        progress.setMessage(getResources().getString(R.string.espera));
        progress.setCancelable(false);
        progress.show();
        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "No se pudo Autenticar con Firebase", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registrarse(View view) {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
        finish();
    }

    public void iniciar(View view) {
        Correo = eCorreo.getText().toString();
        Contrasena = eContrasena.getText().toString();

        if (flag_recuperar == 1) {
            contrasenaR = nuevaContraseña;
        }
        if (Contrasena.isEmpty() && flag_face == 0) {
            eContrasena.setError(Errores[3]);
        }
        if (Correo.isEmpty() && flag_face == 0) {
            eCorreo.setError(Errores[3]);
        }
        if (flag_face == 1) {
            goMainActivity();
            flag_recuperar = 0;
        }

        if (!Contrasena.isEmpty() && !Correo.isEmpty()) {
            SigInEmailAndPassword(Correo, Contrasena);
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void recuperar_con(View view) {
        String correo_edit = eCorreo.getText().toString();
        if (correo_edit.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Campo de Correo Vacío", Toast.LENGTH_SHORT).show();
        } else {
            if (correoR.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Todavia No Estas Registrado", Toast.LENGTH_SHORT).show();
            } else {
                if (correo_edit.equals(correoR)) {
                    Intent intent = new Intent(LoginActivity.this, RecuperarPasswordActivity.class);
                    startActivityForResult(intent, 7890);
                } else {
                    Toast.makeText(LoginActivity.this, "El Usuario No Existe", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}
