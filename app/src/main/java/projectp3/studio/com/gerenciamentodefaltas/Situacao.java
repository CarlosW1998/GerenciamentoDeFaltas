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

import Strategy.InfosDB;
import Strategy.StrategyFuncs;

public class Situacao extends AppCompatActivity {

    private Button voltar;
    private ListView listaMat;
    private SQLiteDatabase banco;
    private AlertDialog.Builder dialog;
    private InfosDB idb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situacao);

        voltar = (Button) findViewById(R.id.voltar);
        listaMat = (ListView) findViewById(R.id.listaM);

        idb = new InfosDB(Situacao.this);

        try {
            banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");

            idb.recuperarInfo(banco, listaMat);

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
                            removerTarefa( idb.getIds().get( position ) );
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
                    Intent i = new Intent(Situacao.this, SituDaMat.class);
                    i.putExtra("Dados", idb.getDados(position));
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

    public void removerTarefa(Integer id){
        try{
            banco.execSQL("DELETE FROM materias WHERE id=" + id);
            Toast.makeText(Situacao.this, "Materia Excluida", Toast.LENGTH_LONG).show();
            idb.recuperarInfo(banco, listaMat);
        }catch(Exception e){
            e.printStackTrace();;
        }
    }
}
