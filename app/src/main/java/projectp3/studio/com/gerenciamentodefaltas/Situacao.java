package projectp3.studio.com.gerenciamentodefaltas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    private ArrayList<Integer> ids;
    private ArrayList<String> faltasA;
    private ArrayList<String> faltasMax;
    private AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao);

        voltar = (Button) findViewById(R.id.voltar);
        listaMat = (ListView) findViewById(R.id.listaM);

        try {
            banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");

            recuperarInfo();

            listaMat.setLongClickable(true);
            listaMat.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    dialog = new AlertDialog.Builder(Situacao.this);
                    dialog.setTitle("Excluir matéria");
                    dialog.setMessage("Deseja excluir a matéria da lista?");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removerTarefa( ids.get( position ) );
                        }
                    });
                    dialog.create();
                    dialog.show();
                    return true;
                }
            });


            listaMat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ArrayList<String> extra = new ArrayList<String>();
                    extra.add(mat.get(position));
                    extra.add(faltasA.get(position));
                    extra.add(faltasMax.get(position));
                    Intent i = new Intent(Situacao.this, SituDaMat.class);
                    i.putExtra("Dados", extra);
                    startActivity(i);
                }
            });

        }catch(Exception e){}

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
            Cursor cursor = banco.rawQuery("SELECT id, nome,faltas,maxFaltas  FROM materias", null);

            int indexNome = cursor.getColumnIndex("nome");
            int indexId = cursor.getColumnIndex("id");
            int indexFaltas = cursor.getColumnIndex("faltas");
            int indexMaxF = cursor.getColumnIndex("maxFaltas");
            cursor.moveToFirst();
            //Adapter
            mat = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            faltasA = new ArrayList<String>();
            faltasMax = new ArrayList<String>();
            listaMaterias = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2, mat);
            listaMat.setAdapter(listaMaterias);

            while(cursor != null){
                mat.add( cursor.getString(indexNome) );
                ids.add( Integer.parseInt(cursor.getString(indexId)) );
                faltasA.add( cursor.getString(indexFaltas) );
                faltasMax.add( cursor.getString(indexMaxF) );
                cursor.moveToNext();
            }
        }catch(Exception e){}
    }

    public void removerTarefa(Integer id){
        try{
            banco.execSQL("DELETE FROM materias WHERE id=" + id);
            Toast.makeText(Situacao.this, "Materia Excluida", Toast.LENGTH_LONG).show();
            recuperarInfo();
        }catch(Exception e){
            e.printStackTrace();;
        }
    }
}
