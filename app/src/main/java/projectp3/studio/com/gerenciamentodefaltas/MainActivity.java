package projectp3.studio.com.gerenciamentodefaltas;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    private Button addMat;
    private Button addF;
    private Button verF;
    private SQLiteDatabase banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMat = (Button) findViewById(R.id.addMat);
        addF = (Button) findViewById(R.id.addF);
        verF = (Button) findViewById(R.id.verF);

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

    }
}
