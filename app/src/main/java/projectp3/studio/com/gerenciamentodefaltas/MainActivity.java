package projectp3.studio.com.gerenciamentodefaltas;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends Activity {

    private Button addMat;
    private Button addF;
    private Button verF;
    private Button share;
    private SQLiteDatabase banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMat = (Button) findViewById(R.id.addMat);
        addF = (Button) findViewById(R.id.addF);
        verF = (Button) findViewById(R.id.verF);
        share = (Button) findViewById(R.id.share);

        banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);

        banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");

        addMat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AddMateria.class));
            }
        });

        addF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, AddFalta.class));
            }
        });

        verF.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, Situacao.class));
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Compartilhar em The Dank Network");
                dialog.setMessage("Deseja divulgar o app através de uma postagem em The Dank Network?");
                dialog.setCancelable(false);

                dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String encoded = "";
                        try {
                            encoded= URLEncoder.encode("Estou usando o Gerenciador de Faltas! Confira na seção apps", "utf-8");
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://the-dank-network.herokuapp.com/post?content=" + encoded)));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "ERRO!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.create();
                dialog.show();
            }
        });

    }
}
