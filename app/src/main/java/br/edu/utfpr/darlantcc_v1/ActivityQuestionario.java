/*
    Copyright (C) 2024  Lucio A. Rocha

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.edu.utfpr.darlantcc_v1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;
import br.edu.utfpr.darlantcc_v1.model.Resposta;
import br.edu.utfpr.darlantcc_v1.persistencia.PessoaDatabase;
import br.edu.utfpr.darlantcc_v1.persistencia.RespostaDatabase;
import br.edu.utfpr.darlantcc_v1.utils.UtilsGUI;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ActivityQuestionario extends AppCompatActivity {

    private Spinner escolher_spinner00;

    private PessoaDatabase pessoaDatabase;
    private ArrayList<Pessoa> listaPessoas;

    private RespostaDatabase respostaDatabase;
    private ArrayList<Resposta> listaRespostas;

    private Button escolher_botao_iniciar;

    public static Context contP0;

    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;
    // declaring width and height
    // for our PDF file.
    //Landscape
    private int pageHeight = 595; //A4
    private int pageWidth = 842; //A4
    // creating a bitmap variable
    // for storing our images
    private Bitmap bmp, scaledbmp;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_questionario);

        setTitle(getResources().getString(R.string.app_name));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        //

        pessoaDatabase = PessoaDatabase.getDatabase(ActivityQuestionario.this);
        listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();

        //
        //--
        escolher_spinner00 = findViewById(R.id.escolher_spinner00);

        ArrayAdapter<String> spinnerAdapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        escolher_spinner00.setAdapter(spinnerAdapter0);

        for(int i=0; i<listaPessoas.size();i++)
            spinnerAdapter0.add(listaPessoas.get(i).getID()+" "+listaPessoas.get(i).getNOME() + " " + listaPessoas.get(i).getIDADE());

        //Apenas para nao comecar todos no mesmo pessoa
        escolher_spinner00.setSelection(0);

        spinnerAdapter0.notifyDataSetChanged();
        //

         escolher_spinner00.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Atualiza a lista de camisas que ainda nao foram selecionadas no time
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //your code here
                String item = escolher_spinner00.getSelectedItem().toString();
                StringTokenizer token;
                token = new StringTokenizer(item, " ");
                String id_pessoa = token.nextToken();

                Pessoa pessoa = listaPessoas.get(0); //Inicia na primeira pessoa
                for(int i=0; i<listaPessoas.size();i++)
                    if(listaPessoas.get(i).getID()==Integer.parseInt(id_pessoa))
                        pessoa = listaPessoas.get(i);

                Toast.makeText(ActivityQuestionario.this,pessoa.getNOME(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing is done
            }

        });

        escolher_botao_iniciar = findViewById(R.id.escolher_botao_iniciar);
        escolher_botao_iniciar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //Busca o ID do pessoa no DAO
                String item = escolher_spinner00.getSelectedItem().toString();
                StringTokenizer token;
                token = new StringTokenizer(item, " ");
                String id_pessoa = token.nextToken();

                Pessoa pessoa = listaPessoas.get(0); //Inicia na primeira pessoa
                for(int i=0; i<listaPessoas.size();i++)
                    if(listaPessoas.get(i).getID()==Integer.parseInt(id_pessoa))
                        pessoa = listaPessoas.get(i);


                //Cria a tupla do questionario
                Resposta resposta = new Resposta();
                //resposta.setID_RESPOSTA(String.valueOf(id_resposta));
                resposta.setID_PESSOA(String.valueOf(pessoa.getID()));

                Log.d("[LOG5]:",pessoa.getID()+"");

                //Insere no banco de dados
                respostaDatabase = RespostaDatabase.getDatabase(ActivityQuestionario.this);
                respostaDatabase.respostaDAO().insert(resposta);

                //Faz uma consulta para ter certeza que inseriu
                listaRespostas = (ArrayList<Resposta>) respostaDatabase.respostaDAO().queryAll();
                Log.d("[LOG6]:",String.valueOf(listaRespostas.size()));

                //Adquire o ultimo indice que inseriu (assumo que, ao remover, o Room sempre incrementa o ID)
                //Room usa o indice 0 como o mais recente ( :-/ )
                for(int i=0;i<listaRespostas.size();i++)
                    Log.d("[LOG8]:",listaRespostas.get(i).getID()+"");

                ActivityP1.iniciar(ActivityQuestionario.this, pessoa, String.valueOf(listaRespostas.get(0).getID()));
            }
        });

        //Para o activity filho finalizar o activity pai
        contP0=ActivityQuestionario.this;

        //---PDF
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.p1a);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 70, 70, false);
    }

    public static void iniciar(ActivityInicio activityInicio){

        Intent intent = new Intent(activityInicio, ActivityQuestionario.class);

        activityInicio.startActivityForResult(intent,1); //Inicia esta ActivityQuestionario com requestcode=1

    }

    public void finalizar() {

        //Retorno da Activity no fim da chamada
        Intent intent = new Intent();

        setResult(ActivityQuestionario.RESULT_CANCELED, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        finalizar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.respostas_menu,menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prepararTXT(ActivityQuestionario activityQuestionario) {

        if (checkPermission()) {
            Toast.makeText(this, R.string.questionario_permissao_concedida, Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        ///---
        gerarTXT();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void prepararPDF(ActivityQuestionario activityQuestionario) {

        if (checkPermission()) {
            Toast.makeText(this, R.string.questionario_permissao_concedida, Toast.LENGTH_SHORT).show();
        } else {
            requestPermission();
        }

        ///---
        gerarPDF();
        //gerarPDF2();
    }

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(this, "Permissão concedida. Clique novamente para salvar.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permissão negada.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void gerarPDF(){
        // creating an object variable
        // for our PDF document.
        PdfDocument pdfDocument = new PdfDocument();

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        Paint paint = new Paint();
        Paint title = new Paint();

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create();

        // below line is used for setting
        // start page for our PDF file.
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);

        // creating a variable for canvas
        // from our page of PDF.
        Canvas canvas = myPage.getCanvas();

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 20, 20, paint);

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.setTextSize(15);

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(this, R.color.black));

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        int x=250;
        int y=50;
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String cdt = sd.format(new Date());
        canvas.drawText("DARLANTCC - Análise Estatística - " + cdt, x, y, title);

        x=100;
        y+=40;
        //canvas.drawText("Time1",x,y,title);

        y+=20;
        int SIZE_COL=10;
        String sep="";
        for(int i=0;i<SIZE_COL;i++)
            sep += " ";

        //canvas.drawText("CAMISA"+sep+"JOGADOR"+sep+"LANCES"+sep+"GOL"+sep+"DEFENDEU"+sep+"OUT"+sep+"ERROS"+sep+"PENALTY",x,y,title);

        // similarly we are creating another text and in this
        // we are aligning this text to center of our PDF file.
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        title.setColor(ContextCompat.getColor(this, R.color.black));
        title.setTextSize(15);

        // below line is used for setting
        // our text to center of PDF.
        //title.setTextAlign(Paint.Align.CENTER);

        ArrayList<Pessoa> listaPessoas = new ArrayList<>();
        pessoaDatabase = PessoaDatabase.getDatabase(ActivityQuestionario.this);
        listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();
        StringBuffer conteudo = new StringBuffer();
        int i=0;
        int j=0;

        x = 100;
        y += 20;

        //setup line
        Paint p = new Paint();
        p.setColor(Color.BLACK);

        for(Pessoa pessoa : listaPessoas){
    
                canvas.drawText(preencherLinha(pessoa).toString(), x, y, title);
                y+=5;
                canvas.drawLine(0, y, pageWidth, y, p);
                y +=20;

                i++; //proximo pessoa
    
        }
    
        //---
        /*y+=40;
        canvas.drawText("Time2",x,y,title);

        y+=20;
        canvas.drawText("CAMISA"+sep+"JOGADOR"+sep+sep+sep+"LANCES"+sep+"GOL"+sep+"DEFENDEU"+sep+"OUT"+sep+"ERROS"+sep+"PENALTY",x,y,title);

        y+=20;
        */
        y+=40;
        canvas.drawText("Respostas",x,y,title);

        y+=20;
        ArrayList<Resposta> listaRespostas = new ArrayList<>();
        respostaDatabase = RespostaDatabase.getDatabase(this);
        listaRespostas = (ArrayList<Resposta>) respostaDatabase.respostaDAO().queryAll();
        for(Resposta resposta : listaRespostas){
            canvas.drawText(resposta.toString(), x, y, title);
            y +=20;
        }

        //Write statistical analysis

        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage);

        // below line is used to set the name of
        // our PDF file and its path.
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String arquivo = "DARLANTCC_" + currentDateandTime + ".pdf";

        File file = new File(Environment.getExternalStorageDirectory(), arquivo);

        try {
            // after creating a file name we will
            // write our PDF file to that location.
            pdfDocument.writeTo(new FileOutputStream(file));

            // below line is to print toast message
            // on completion of PDF generation.
            Toast.makeText(this, R.string.questionario_pdfgerado, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // below line is used
            // to handle error
            e.printStackTrace();
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        pdfDocument.close();
    }

    private StringBuffer preencherLinha(Pessoa pessoa) {
        StringBuffer conteudo = new StringBuffer();
        int SIZE_COL = 10; //until 10 chars in columns

        //Initial sep
        /*String sep="";
        for(int i=pessoa.getCAMISA().length();i<SIZE_COL;i++)
            sep += " ";

        conteudo.append(sep+pessoa.getCAMISA());
        int sp=pessoa.getCAMISA().length();
        sep="";
        for(int i=sp;i<SIZE_COL;i++)
            sep += " ";
        conteudo.append(sep);
        //----
*/
        //--
        String nome = pessoa.getNOME();
        if(nome.length()<SIZE_COL) {
            for(int i=nome.length();i<SIZE_COL;i++) //fill until reach the SIZE_COL
                nome += "#";
        } else
        if(nome.length()>SIZE_COL)
            nome = nome.substring(0,SIZE_COL); //trunk
        conteudo.append(nome);

        /*sp=nome.length();
        sep="";
        for(int i=sp;i<SIZE_COL;i++)
            sep += "#";
        conteudo.append(sep);*/
        //--

        //--

        conteudo.append("\n");
        return conteudo;
    }

    private void gerarTXT() {
        try {


                // below line is to print toast message
                // on completion of PDF generation.
                Toast.makeText(this, R.string.questionario_txtgerado, Toast.LENGTH_SHORT).show();

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            String arquivo = "DARLANTCC_" + currentDateandTime + ".txt";

            File file = new File(Environment.getExternalStorageDirectory(), arquivo);
            FileOutputStream stream = new FileOutputStream(file);

            ArrayList<Resposta> listaRespostas = new ArrayList<>();
            respostaDatabase = RespostaDatabase.getDatabase(this);
            listaRespostas = (ArrayList<Resposta>) respostaDatabase.respostaDAO().queryAll();
            StringBuffer data = new StringBuffer();
            for(Resposta resposta : listaRespostas){
                data.append(resposta.toString()+"\n");
            }
            try {
                stream.write(data.toString().getBytes());
            } finally {
                stream.close();
            }

        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemRespostas: {
                ActivityRespostas.iniciar(ActivityQuestionario.this);
                return true;
            }
            case R.id.menuItemPDF: {
                
                    //Verifica as permissoes
                    prepararPDF(this);
                    
                return true;
            }
            case R.id.menuItemTXT: {

                //Verifica as permissoes
                prepararTXT(this);

                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

}