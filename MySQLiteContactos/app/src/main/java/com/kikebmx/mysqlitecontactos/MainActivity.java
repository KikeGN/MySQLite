package com.kikebmx.mysqlitecontactos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView lista;
    TextView txtbuscar;
    Button btnNew,btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lista =(ListView)findViewById(R.id.listaContactos);

        cargardatos();
        busqueda();
        btnNew = (Button) findViewById(R.id.btnNew);
        btnUpdate= (Button) findViewById(R.id.btnActualizar);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inten = new Intent(getApplication(),Datos.class);
                startActivityForResult(inten,1000);
                Toast.makeText(getApplication(),"AGREGAR CONTACTO", Toast.LENGTH_LONG).show();

            }

        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargardatos();

            }

        });

    }
    public void busqueda(){
        txtbuscar=(TextView)findViewById(R.id.txtbuscar);

        txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtbuscar.getText().length()==0){
                    cargardatos();
                }else{
                    buscar(txtbuscar.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    ArrayAdapter<Contacto> adp;
    public void cargardatos(){
        DAOContactos dao = new DAOContactos(MainActivity.this);
        adp = new ArrayAdapter<Contacto>(MainActivity.this,
                android.R.layout.simple_list_item_1,dao.getAllStudentsList());
        lista.setAdapter(adp);
    }

    public void buscar(String cad){
        DAOContactos dao = new DAOContactos(MainActivity.this);
        ArrayAdapter<Contacto> adp = new ArrayAdapter<Contacto>(MainActivity.this,
                android.R.layout.simple_list_item_1,dao.buscarcontacto(cad));

        lista.setAdapter(adp);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK)
        {

            try {

                //obtener el objeto contacto
                Contacto objcontacto = (Contacto) data.getSerializableExtra("micontacto");

                DAOContactos dao = new DAOContactos(MainActivity.this);
                if(dao.insert(new Contacto(0,objcontacto.getNombre(),objcontacto.getCorreo_electronico(),objcontacto.getTwitter(),objcontacto.getTelefono(),objcontacto.getFecha_nacimiento()))>0) {
                    Toast.makeText(getBaseContext(), "Contacto Insertado", Toast.LENGTH_SHORT).show();
                    cargardatos();
                }else{
                    Toast.makeText(getBaseContext(), "No se pudo Insertar el Contacto", Toast.LENGTH_SHORT).show();
                }



            }catch (Exception err){
                Toast.makeText(getBaseContext(),err.getMessage(),Toast.LENGTH_LONG).show();
            }

        }
    }
}
