package projectp3.studio.com.gerenciamentodefaltas;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddFalta extends AppCompatActivity {

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
    private NotificationCompat.Builder notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_falta);

        voltar = (Button) findViewById(R.id.voltar);
        listaMat = (ListView) findViewById(R.id.listaAddF);

        try {
            banco = openOrCreateDatabase("GerencFaltas", MODE_PRIVATE, null);
            banco.execSQL("CREATE TABLE IF NOT EXISTS materias (id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR, cargaHoraria INT(2), maxFaltas INT(2), faltas INT(2))");
            recuperarInfo();

            listaMat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                    dialog = new AlertDialog.Builder(AddFalta.this);
                    dialog.setTitle("Adicionar falta");
                    dialog.setMessage("Deseja adicionar uma falta nessa matéria?");
                    dialog.setCancelable(false);

                    dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });

                    dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String prevStatus = calcStatus(Integer.parseInt(faltasA.get(position)), Integer.parseInt(faltasMax.get(position)));
                            updateFaltas(ids.get(position), faltasA.get(position));
                            String newStatus = calcStatus(Integer.parseInt(faltasA.get(position)), Integer.parseInt(faltasMax.get(position)));

                            if(!prevStatus.equals(newStatus)){
                                int nvl = (int)((Integer.parseInt(faltasA.get(position))*100)/Integer.parseInt(faltasMax.get(position)));
                                if (nvl >= 50 && nvl < 80){
                                    notificar("Perigoso", position);
                                }else if (nvl >= 80 && nvl <= 100){
                                    notificar("CRÍTICO", position);
                                }else if(nvl > 100){
                                    notificar("ULTRAPASSADO", position);
                                }
                            }
                        }
                    });
                    dialog.create();
                    dialog.show();
                }
            });

        }catch(Exception e){}

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
            int indexNot = cursor.getColumnIndex("notificar");
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

    public void updateFaltas (Integer id, String faltas){
        try{
            int f = Integer.parseInt(faltas) + 1;
            banco.execSQL("UPDATE materias SET faltas="+ f +" WHERE id=" + id);
            Toast.makeText(AddFalta.this, "Falta adicionada!", Toast.LENGTH_LONG).show();
            recuperarInfo();

        }catch(Exception e){
            e.printStackTrace();;
        }
    }

    public void notificar(String status, int position){

        notification = new NotificationCompat.Builder(AddFalta.this);
        notification.setAutoCancel(true);
        notification.setSmallIcon(R.drawable.ic_launcher);
        notification.setTicker("Nível de Faltas " + status + "!");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Nível de Faltas " + status + "!");
        notification.setContentText("Clique para ver o status da matéria");
        //notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), /*caminho da imagem*/));
        int id = (int) System.currentTimeMillis();
        notification.setVibrate(new long[] {150, 800});

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(AddFalta.this, som);
            toque.play();
        }catch(Exception e){}

        ArrayList<String> extra = new ArrayList<String>();
        extra.add(mat.get(position));
        extra.add(faltasA.get(position));
        extra.add(faltasMax.get(position));

        Intent i = new Intent(AddFalta.this, SituDaMat.class);
        i.putExtra("Dados", extra);

        PendingIntent pi = PendingIntent.getActivity(AddFalta.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(id, notification.build());
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