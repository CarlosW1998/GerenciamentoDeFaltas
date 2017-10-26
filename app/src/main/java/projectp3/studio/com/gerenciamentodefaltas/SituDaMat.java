package projectp3.studio.com.gerenciamentodefaltas;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Strategy.InfosDB;
import Strategy.StrategyFuncs;

public class SituDaMat extends Activity {

    private TextView nomeMat;
    private TextView faltasRest;
    private TextView faltasAtuais;
    private TextView status;
    private Button voltar;

    private StrategyFuncs s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situ_da_mat);

        s = new StrategyFuncs(SituDaMat.this);

        nomeMat = (TextView) findViewById(R.id.NomeMat);
        faltasRest = (TextView) findViewById(R.id.RestF);
        faltasAtuais = (TextView) findViewById(R.id.FaltasAt);
        status = (TextView) findViewById(R.id.status);
        voltar = (Button) findViewById(R.id.voltar);
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            ArrayList<String> dados = extra.getStringArrayList("Dados");
            Integer faltasR = Integer.parseInt(dados.get(2)) - Integer.parseInt(dados.get(1));
            nomeMat.setText(dados.get(0));
            faltasAtuais.setText(dados.get(1));
            if(faltasR < 0)
                faltasR = 0;
            faltasRest.setText(faltasR.toString());
            status.setText(s.calcStatus(Integer.parseInt(dados.get(1)) , Integer.parseInt(dados.get(2))));
        }

        //Botão Voltar
        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}