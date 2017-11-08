package com.sebastian.appcuentanos.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sebastian.appcuentanos.Clases.Usuario;
import com.sebastian.appcuentanos.PerfilActivity;
import com.sebastian.appcuentanos.R;

import java.io.File;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilFragment extends Fragment {

    private int mes, dia, year;
    private String nombre_go,url_imagen, img_url_go="", direccion_foto = "", Fecha = "";
    static final int DATE_ID = 0;
    int flag_loggin = 0;
    EditText eFecha, eNombre, eCorreo;
    String genero = "";
    TextInputLayout tFecha;
    ImageView iPerfil;
    Button bGuardar;
    Uri fotoperfil = null;
    RadioButton rMasculino, rFemenino;
    ////Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    private StorageReference storageReference;
    FirebaseUser User;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface MyInterface {
        /**
         * Called by HeadlinesFragment when a list item is selected
         */
        public void onItemSelected(int position);
    }


    public PerfilFragment() {
        // Required empty public constructor
    }

    // The container Activity must implement this interface so the frag can deliver messages
    MyInterface mCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_perfil, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        User = FirebaseAuth.getInstance().getCurrentUser();

        tFecha = (TextInputLayout) rootView.findViewById(R.id.Tefecha);
        eFecha = (EditText) rootView.findViewById(R.id.eFecha);
        eNombre = (EditText) rootView.findViewById(R.id.eNombre);
        eCorreo = (EditText) rootView.findViewById(R.id.eCorreo);
        iPerfil = (ImageView) rootView.findViewById(R.id.iPerfil);
        rMasculino = (RadioButton) rootView.findViewById(R.id.rmasculino);
        rFemenino = (RadioButton) rootView.findViewById(R.id.rFemenino);
        eFecha.setInputType(InputType.TYPE_NULL);
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new ContenedorFragment(), "PRINCIPAL").commit();
                return true;
            }
        });

        bGuardar = (Button) rootView.findViewById(R.id.bGuardar);
        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombre = eNombre.getText().toString();
                final String correo = eCorreo.getText().toString();
                final String Fecha = eFecha.getText().toString();
                String genero = "";
                if(rMasculino.isChecked()){
                    genero = "masculino";
                }else if(rFemenino.isChecked()){
                    genero = "femenino";
                }
                if (fotoperfil != null) {
                    StorageReference filePath = storageReference.child("usuarios").child(fotoperfil.getLastPathSegment());
                    filePath.putFile(fotoperfil).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();
                                /*String imagen = taskSnapshot.getDownloadUrl().toString();
                                reference = database.getReference("usuarios").child(User.getUid());
                                Usuario usuario = new Usuario(User.getUid(), nombre, correo, "", "", imagen);
                                reference.setValue(usuario);*/
                            Toast.makeText(getContext(), "los cambios se han guardado exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    reference = database.getReference("usuarios").child(User.getUid());
                    Usuario usuario;
                    if (User.getProviders().toString().equals("[password]")) {
                        usuario = new Usuario(User.getUid(), nombre, correo, Fecha, genero,"");
                    } else {
                        usuario = new Usuario(User.getUid(), nombre, correo, Fecha, genero,url_imagen);
                    }
                    reference.setValue(usuario);
                    Toast.makeText(getContext(), "los cambios se han guardado exitosamente", Toast.LENGTH_SHORT).show();
                }

            }
        });

        iPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colocar_foto();

            }
        });


        ///Firebase
        if (User != null) {
            flag_loggin = 1;
            reference = database.getReference("usuarios");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(User.getUid()).exists()) {
                        Usuario usuario = dataSnapshot.child(User.getUid()).getValue(Usuario.class);
                        url_imagen = usuario.getUrl_foto();
                        eNombre.setText(usuario.getNombre());
                        eCorreo.setText(usuario.getCorreo());
                        eFecha.setText(usuario.getFecha_nacimiento());
                        String genero = usuario.getSexo();
                        if (genero.equals("masculino")) {
                            rMasculino.setChecked(true);
                            rFemenino.setChecked(false);
                        } else if (genero.equals("femenino")) {
                            rMasculino.setChecked(false);
                            rFemenino.setChecked(true);
                        }
                        img_url_go = usuario.getUrl_foto();
                        if (img_url_go.isEmpty() || User.getUid().toString().equals("[password]") || img_url_go == null) {
                            iPerfil.setImageResource(R.drawable.ic_person);
                        } else {
                            if(isAdded()) {
                                Glide.with(getActivity()).load(img_url_go).error(R.drawable.ic_person).into(iPerfil);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        ///////////

        eFecha.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    final Calendar calendar = Calendar.getInstance();
                    dia = calendar.get(Calendar.DAY_OF_MONTH);
                    mes = calendar.get(Calendar.MONTH);
                    year = calendar.get(Calendar.YEAR);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                            eFecha.setText(Fecha);
                        }
                    }, dia, mes, year);
                    datePickerDialog.show();
                }
            }
        });

        rMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "masculino";
            }
        });
        rFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "femenino";
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Perfil");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Log.d("Camilo", direccion.toString());
                    fotoperfil = direccion;
                    Log.d("Camilo", fotoperfil.toString());
                    direccion_foto = direccion.toString();
                    iPerfil.setImageURI(Uri.parse(direccion_foto));
                }
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (MyInterface) context;
    }

    public void colocar_foto() {
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        builder.show();

    }

    private void abrir_camara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 201);
    }


}
