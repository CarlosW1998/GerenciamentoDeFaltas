package projectp3.studio.com.gerenciamentodefaltas;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetMaterias extends AppCompatActivity {

    private EditText user;
    private EditText password;
    private Button importar;
    private Button voltar;
    //private SQLiteDatabase banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_materias);

        user = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.senha);
        importar = (Button) findViewById(R.id.impM);
        voltar = (Button) findViewById(R.id.voltar);

        importar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = user.getText().toString();
                String p = password.getText().toString();
                if(!u.equals("") && !p.equals("")){
                    //INTEGRAR COM SIE WEB
                    //CHAMAR API
                    //CONFIRMAR CADASTRO
                    //IF TRUE -> GET MATERIAS E ADD ON BANCO
                    //ELSE -> TOAST "USUARIO OU SENHA INCORRETOS"
                    Toast.makeText(GetMaterias.this, "User: " + u + " Password: " + p, Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(GetMaterias.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
/*
    public void addMaterias(String materia, String cargaH){
        try {
            int maxFaltas =
            String toAdd = "'" + materia + "'," +
            banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");
            banco.execSQL("INSERT INTO materias (nome, cargaHoraria, maxFaltas, faltas) VALUES (" + toAdd + ")");
            Toast.makeText(AddMateria.this, "Matéria Adicionada!", Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception e){}
    }


    public int calcMaxfaltas(int cargaH){
        return (int)((cargaH*60)/100) * 0,75;
    }
*/
}
