package Strategy;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Lucas on 22/10/2017.
 */
public class InfosDB {

    private Context context;
    private Cursor cursor;
    private ArrayAdapter<String> listaMaterias;
    private ArrayList<String> mat;
    private ArrayList<Integer> ids;
    private ArrayList<String> faltasA;
    private ArrayList<String> faltasMax;

    public InfosDB (Context context){
        this.context = context;
    }

    public ListView recuperarInfo(SQLiteDatabase banco, ListView listaMat){
        try{
            cursor = banco.rawQuery("SELECT id, nome,faltas,maxFaltas  FROM materias", null);

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
            listaMaterias = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_2, android.R.id.text2, mat);
            listaMat.setAdapter(listaMaterias);

            while(cursor != null){
                mat.add( cursor.getString(indexNome) );
                ids.add( Integer.parseInt(cursor.getString(indexId)) );
                faltasA.add( cursor.getString(indexFaltas) );
                faltasMax.add( cursor.getString(indexMaxF) );
                cursor.moveToNext();
            }
        }catch(Exception e){}
        finally {
            return listaMat;
        }
    }

    public ArrayList<String> getDados (int position){
        ArrayList<String> extra = new ArrayList<String>();
        extra.add(mat.get(position));
        extra.add(faltasA.get(position));
        extra.add(faltasMax.get(position));
        return extra;
    }

    public void updateFaltas (SQLiteDatabase banco, int f, ListView listaMat, Integer id){
        try{
            banco.execSQL("UPDATE materias SET faltas="+ f +" WHERE id=" + id);
            Toast.makeText(context, "Falta adicionada!", Toast.LENGTH_LONG).show();
            recuperarInfo(banco, listaMat);

        }catch(Exception e){
            e.printStackTrace();;
        }
    }

    public Context getContext() {
        return context;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public ArrayAdapter<String> getListaMaterias() {
        return listaMaterias;
    }

    public ArrayList<String> getMat() {
        return mat;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public ArrayList<String> getFaltasA() {
        return faltasA;
    }

    public ArrayList<String> getFaltasMax() {
        return faltasMax;
    }
}
