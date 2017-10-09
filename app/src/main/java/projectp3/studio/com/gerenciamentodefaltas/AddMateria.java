package projectp3.studio.com.gerenciamentodefaltas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AddMateria extends AppCompatActivity {

    private EditText materia;
    private EditText cargaH;
    private EditText numF;
    private Button add;
    private SQLiteDatabase banco;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_materia);

        materia = (EditText) findViewById(R.id.nomeM);
        cargaH = (EditText) findViewById(R.id.cargaH);
        numF = (EditText) findViewById(R.id.NumF);
        add = (Button) findViewById(R.id.addButton);

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String m = materia.getText().toString();
                String c = cargaH.getText().toString();
                String n = numF.getText().toString();

                if(m == null || n == null || c == null){
                    Toast.makeText(AddMateria.this, "Por favor, preencha todos os campos!", Toast.LENGTH_LONG).show();
                }else{
                    String toAdd = "'"+m+"',"+c+","+n;
                    try {
                        banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
                        banco.execSQL("INSERT INTO Materias (Nome, CargaHoraria, MaximoDeFaltas, FaltasAtuais) VALUES (" + toAdd + ",0)" );
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(AddMateria.this, "Matéria Adicionada!!! " + toAdd, Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }





}
