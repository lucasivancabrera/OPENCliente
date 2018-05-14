package com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.SistemaPedidos;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.opencliente.applic.opencliente.R;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.adaptadores.adapter_recyclerView_pedidos;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.SistemaPedidos.adaptadores.adaptador_direccion;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.SistemaPedidos.adaptadores.adaptador_pedido;
import com.opencliente.applic.opencliente.interface_principal.navigation_drawe.negocio.cuenta.SistemaPedidos.adaptadores.adapter_recyclerView_direccion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.firebase.firestore.FieldValue.serverTimestamp;

public class MainActivity_negocios_formulario_pedidos extends AppCompatActivity {


    // Views Layout
    private LinearLayout layoutHorarios;
    private LinearLayout layoutDireeccion;
    private LinearLayout layoutContenidos;

    // String Datos
    private  String ID_NEGOCIO;
    private String sContacto;
    private String sTipoEntrega;
    private Object objDireccion;
    private String sHorario;
    private String sFormaPago;
    private String sNota;
    private String sTelefono;

    // Edittext
    private EditText editText_Contacto;
    private EditText editText_Nota;
    private EditText editText_telefono;

    // Spinner
    private Spinner spinnerTipoPedido;


    // TextView
    private TextView textView_Direccion;


    // RECYCLERVIEW
    public RecyclerView recyclerViewdirecciones;
    public List<adaptador_direccion> adapter_productoListDirecciones =new ArrayList<>();
    public adapter_recyclerView_direccion adapter_recyclerView_direcciones;
    private adaptador_direccion adaptadorDireccion=new adaptador_direccion();
    private Map<String, Object> mapDireccion;

    // Firebase
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_negocios_formulario_pedidos);
        setTitle(getResources().getString(R.string.realizar_pedido));


        // habilita botón físico de atrás en la Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--- Obtenemos los datos pasados por parametro
        ID_NEGOCIO = getIntent().getExtras().getString("ID_NEGOCIO");

        // References
        textView_Direccion=(TextView) findViewById(R.id.textView37);
        layoutHorarios=(LinearLayout) findViewById(R.id.layout_horario);
        layoutDireeccion=(LinearLayout) findViewById(R.id.layout_direccion);
        layoutContenidos=(LinearLayout) findViewById(R.id.layout_contenido);

        editText_Contacto=(EditText) findViewById(R.id.input_contacto);
        editText_Nota=(EditText) findViewById(R.id.editext_nota);
        editText_telefono=(EditText) findViewById(R.id.input_telefono);

        // Spinner Tipo de pedido
        spinnerTipoPedido = (Spinner) findViewById(R.id.spinneer_tipo_entrega);
        spinnerTipoPedido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {

                switch (position){
                    case 0:

                        //Control de visibilidad
                        layoutContenidos.setVisibility(View.GONE);

                        // set Dato
                        sTipoEntrega="";

                        break;
                    case 1:
                        //Control de visibilidad
                        layoutContenidos.setVisibility(View.VISIBLE);
                        layoutHorarios.setVisibility(View.VISIBLE);
                        layoutDireeccion.setVisibility(View.GONE);

                        // set Dato
                        sTipoEntrega=spinnerTipoPedido.getItemAtPosition(position).toString();

                        break;
                    case 2:
                        //Control de visibilidad
                        layoutContenidos.setVisibility(View.VISIBLE);
                        layoutHorarios.setVisibility(View.GONE);
                        layoutDireeccion.setVisibility(View.VISIBLE);

                        // set Dato
                        sTipoEntrega=spinnerTipoPedido.getItemAtPosition(position).toString();

                        break;
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio
            }
        });

        // Spinner Horario
        final Spinner spinnerHorario = (Spinner) findViewById(R.id.spinner_horarios);
        spinnerHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if(position == 0){
                    // set Dato
                    sHorario="";
                }else{
                    // set Dato
                    sHorario=spinnerHorario.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio
            }
        });

        // Spinner Forma de pago

        final Spinner spinner_formaPago = (Spinner) findViewById(R.id.spinner_forma_pago);
        spinner_formaPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                if(position == 0){
                    // set Dato
                    sFormaPago="";
                }else{
                    // set Dato
                    sFormaPago=spinner_formaPago.getItemAtPosition(position).toString();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                // vacio
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //hago un case por si en un futuro agrego mas opciones
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // BUTTON
    public void Button_SeleccioneDireccion(View view){

        //////////////////////////////// Cuadro de Dialog //////////////////////////////////
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_negocio_seleccionar_direccion, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_negocios_formulario_pedidos.this);
        builder.setView(dialoglayout);
        final AlertDialog alertDialogHors;
        alertDialogHors = builder.show();

        // Reference
        Button button_AddDireccion=(Button) dialoglayout.findViewById(R.id.button_add_negocio);

        // Recyclerview
        recyclerViewdirecciones =(RecyclerView) dialoglayout.findViewById(R.id.recyclerview_direcciones);
        recyclerViewdirecciones.setLayoutManager(new LinearLayoutManager(this));
        adapter_recyclerView_direcciones =new adapter_recyclerView_direccion(adapter_productoListDirecciones);


        // Firestore
        CollectionReference collectionReference=firestore.collection( getResources().getString(R.string.DB_CLIENTES) ).document( firebaseAuth.getUid() ).collection( getResources().getString(R.string.DB_DIRECCIONES) );

        // EventListener
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                adapter_productoListDirecciones.removeAll(adapter_productoListDirecciones);

                // Recorre lista
                for(DocumentSnapshot documentSnapshot: documentSnapshots){
                    // Adaptador direcciones
                    adaptador_direccion adaptadorDireccion=documentSnapshot.toObject(adaptador_direccion.class);
                    // ADD
                    adapter_productoListDirecciones.add(adaptadorDireccion);

                }
                // Actualiza el RecyclerView
                adapter_recyclerView_direcciones.notifyDataSetChanged();
            }
        });


        //OnClick
        adapter_recyclerView_direcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Adaptador
                adaptadorDireccion=adapter_productoListDirecciones.get(recyclerViewdirecciones.getChildAdapterPosition(view));
                // Put map
                mapDireccion= new HashMap<String, Object>();
                mapDireccion.put("calle",adaptadorDireccion.getCalle());
                mapDireccion.put("numero",adaptadorDireccion.getNumero());
                mapDireccion.put("piso_depto",adaptadorDireccion.getPiso_depto());
                mapDireccion.put("entre_calles",adaptadorDireccion.getEntre_calles());
                mapDireccion.put("ciudad",adaptadorDireccion.getCiudad());
                mapDireccion.put("localidad",adaptadorDireccion.getLocalidad());

                textView_Direccion.setText(adaptadorDireccion.getCalle() +" "+ adaptadorDireccion.getNumero());

                // Finaliza cuadro de dialogo
                alertDialogHors.dismiss();

            }
        });
        recyclerViewdirecciones.setAdapter(adapter_recyclerView_direcciones);

        // Button onClick
        button_AddDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Lanzador1=new Intent(MainActivity_negocios_formulario_pedidos.this,MainActivity_negocios_formulario_direccion.class);
                startActivity(Lanzador1);

            }
        });

    }

    public void Button_EnviarPedido(View view){

        if(spinnerTipoPedido.getSelectedItemPosition() != 0){


            // ID
            String ID_Pedido = firebaseAuth.getUid();

            // get Datos
            sContacto=editText_Contacto.getText().toString();
            sNota=editText_Nota.getText().toString();
            sTelefono=editText_telefono.getText().toString();


            if(!sContacto.equals("") || !sTelefono.equals("") || !sFormaPago.equals("")){


                // adaptador pedido
                Map<String, Object> mapPedido = new HashMap<>();
                mapPedido.put("id_cliente",firebaseAuth.getUid() );
                mapPedido.put("id",ID_Pedido );
                mapPedido.put("contacto",sContacto );
                mapPedido.put("tipo_entrega",sTipoEntrega );
                mapPedido.put("forma_pago", sFormaPago);
                mapPedido.put("nota", sNota);
                if(mapPedido != null){ mapPedido.put("direccion",mapDireccion );}else{mapPedido.put("direccion",null );}
                mapPedido.put("timestamp",FieldValue.serverTimestamp() );
                mapPedido.put("estado",false );
                mapPedido.put("telefono",sTelefono );


                // FIRESTORE ( Referecia DB negocio)
                DocumentReference documentReference=firestore.collection(  getResources().getString(R.string.DB_NEGOCIOS)  ).document(  ID_NEGOCIO  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(ID_Pedido);
                documentReference.set(mapPedido);
                // FIRESTORE ( Referecia DB cliente)
                DocumentReference documentReferenceCliente=firestore.collection(  getResources().getString(R.string.DB_CLIENTES)  ).document(  firebaseAuth.getUid()  ).collection(  getResources().getString(R.string.DB_PEDIDOS)  ).document(ID_Pedido);
                documentReferenceCliente.set(mapPedido);

                // Finaliza la activity
                finish();

            }else{
                Toast.makeText(MainActivity_negocios_formulario_pedidos.this,getResources().getString(R.string.noti_complete_los_campos_necesarios),Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(MainActivity_negocios_formulario_pedidos.this,getResources().getString(R.string.selecciones_metodo),Toast.LENGTH_LONG).show();
        }


    }

}
