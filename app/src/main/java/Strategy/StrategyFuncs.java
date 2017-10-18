package Strategy;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Lucas on 17/10/2017.
 */
public class StrategyFuncs extends Activity {

    private Button voltar;
    private SQLiteDatabase banco;
    private ArrayAdapter<String> listaMaterias;
    private ArrayList<String> mat;
    private ArrayList<Integer> ids;
    private ArrayList<String> faltasA;
    private ArrayList<String> faltasMax;


    public StrategyFuncs(){
        this.banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
    }


    public void recuperarInfo(ListView listaMat){
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


    public String calcStatus(Integer faltasA, Integer maxFaltas){
        //ACEITAVEL (VERDE) -> ate 50% [0, 50)
        //PERIGOSO (AMARELO) -> entre 50% e 90% [50, 90)
        //CRITICO (VERMELHO) -> mais de 90% [90, 100]
        //ULTRAPASSADO (PRETO) -> mais de 100% (100, +inf)
        int nvl = (int)((faltasA*100)/maxFaltas);
        if( nvl < 50 ){
            return "ACEITÁVEL";
        }else if (nvl >= 50 && nvl < 80){
            return "PERIGOSO!";
        }else if (nvl >= 80 && nvl <= 100){
            return "CRÍTICO!!!";
        }else{
            return "LIMITE ULTRAPASSADO!";
        }
    }


}
