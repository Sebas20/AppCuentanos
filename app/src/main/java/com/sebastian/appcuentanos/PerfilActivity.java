package com.sebastian.appcuentanos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

public class PerfilActivity extends AppCompatActivity {

    private int mes, dia, year, mdia, mmes, myear;
    private String nombre_go,correo_go,img_url_go,direccion_foto;
    static final int DATE_ID = 0;
    int flag = 0;
    EditText eFecha,eNombre,eCorreo;
    ImageView iPerfil;
    private int cont = 0;
    private GoogleApiClient googleApiClient;
    Uri fotoperfil = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        eFecha = (EditText)findViewById(R.id.eFecha);
        eNombre = (EditText)findViewById(R.id.eNombre);
        eNombre.clearFocus();
        eCorreo = (EditText)findViewById(R.id.eCorreo);
        iPerfil = (ImageView)findViewById(R.id.iPerfil);
        eFecha.setInputType(InputType.TYPE_NULL);
        eFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(cont == 0) {
                    fecha();
                }
                cont++;
                if(cont >= 2){
                    cont = 0;
                }
            }
        });

        ///Firebase
        FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
        if(User != null){
            nombre_go = User.getDisplayName();
            correo_go = User.getEmail();
            img_url_go = User.getPhotoUrl().toString();
            eNombre.setText(nombre_go);
            eCorreo.setText(correo_go);
            Glide.with(this).load(img_url_go).into(iPerfil);

        }
        ///////////
        //Google
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

        ////SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String correo = sharedPreferences.getString("correo","c");
        flag = sharedPreferences.getInt("flag",0);
        String contrasena = sharedPreferences.getString("contrasena","c");
        String nombre = sharedPreferences.getString("nombre","Usuario");
        if(!correo.equals("c") && !contrasena.equals("c") && flag == 3){
            eCorreo.setText(correo);
            eNombre.setText(nombre);
        }
        /////////////////////

    }

    public void fecha() {
        showDialog(DATE_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_ID:
                return new DatePickerDialog(this, mDatePicker, year, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myear = i;
            mmes = i1;
            mdia = i2;
            colocar_fecha();
        }
    };

    private void colocar_fecha() {
        String fecha = Integer.toString(mdia) + "/" + Integer.toString(mmes + 1) + "/" + Integer.toString(myear);
        eFecha.setText(fecha);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            nombre_go = account.getDisplayName();
            correo_go = account.getEmail();
            eNombre.setText(nombre_go);
            eCorreo.setText(correo_go);
            if (account.getPhotoUrl() != null) {
                img_url_go = account.getPhotoUrl().toString();
                Glide.with(this).load(img_url_go).into(iPerfil);


            } else {
                img_url_go = "vacia";
            }

        }
    }

    public void colocar_foto(View view) {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(PerfilActivity.this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i] == "Tomar Foto") {
                    abrir_camara();
                } else if (opciones[i] == "Elegir de Galeria") {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Selecciona un App"), 200);

                } else if (opciones[i] == "Cancelar") {
                    dialogInterface.dismiss();
                }

            }
        });
        if (flag == 3) {
            builder.show();
        }

    }

    private void abrir_camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 201);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 201:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap bmp = (Bitmap) extras.get("data");
                    iPerfil.setImageBitmap(bmp);
                }
                break;
            case 200:
                if (resultCode == RESULT_OK) {
                    Uri direccion = data.getData();
                    fotoperfil = direccion;
                    direccion_foto = direccion.toString();
                    iPerfil.setImageURI(Uri.parse(direccion_foto));
                }
                break;
        }
    }


    public void guardar(View view) {
        String nombre = eNombre.getText().toString();
        String correo = eCorreo.getText().toString();
        if(!nombre.isEmpty() || !correo.isEmpty()){
            if(fotoperfil != null){
                Toast.makeText(this,"Los cambios se han guardado exitosamente",Toast.LENGTH_SHORT).show();
                //StorageReference filePath = storageReference.child("Promociones").child(direccion.getLastPathSegment());
            }



        }
    }
}
