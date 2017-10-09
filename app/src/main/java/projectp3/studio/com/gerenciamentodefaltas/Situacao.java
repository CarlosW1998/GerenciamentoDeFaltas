package projectp3.studio.com.gerenciamentodefaltas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Situacao extends AppCompatActivity {

    private Button voltar;
    private ListView listaMat;
    private SQLiteDatabase banco;
    private Cursor cursor;
    private ArrayAdapter<String> listaMaterias;
    private ArrayList<String> mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao);

        voltar = (Button) findViewById(R.id.voltar);

        try {
            banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");

            if(banco==null){
                Toast.makeText(Situacao.this, "Tem banco!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Situacao.this, "Não tem banco!", Toast.LENGTH_LONG).show();
            }

            banco.execSQL("DELETE FROM Materias WHERE id IS NOT null");
            recuperarInfo();

        }catch(Exception e){
            Toast.makeText(Situacao.this, "deu ruim jão " + e.toString(), Toast.LENGTH_LONG).show();
        }


        //Botão Voltar
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


    public void recuperarInfo(){
        try{
            Cursor cursor = banco.rawQuery("SELECT nome FROM materias", null);

            int indexNome = cursor.getColumnIndex("nome");
            cursor.moveToFirst();

            listaMat = (ListView) findViewById(R.id.listaM);

            //Adapter
            mat = new ArrayList<String>();
            listaMaterias = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2, mat);
            listaMat.setAdapter(listaMaterias);

            if(cursor.getCount() > 0){
                while(cursor != null){
                    mat.add( cursor.getString(indexNome) );
                    Log.i("AQUI - ", cursor.getString(indexNome));
                    cursor.moveToNext();
                }
            }else{
                Toast.makeText(Situacao.this, "merda no cursor jao!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Toast.makeText(Situacao.this, "Exception -> " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
