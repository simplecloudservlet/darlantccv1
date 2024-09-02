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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;
import br.edu.utfpr.darlantcc_v1.persistencia.PessoaDatabase;
import br.edu.utfpr.darlantcc_v1.utils.UtilsGUI;

public class ActivityInicio extends AppCompatActivity {

    private Button botaoCadastro;
    private Button botaoQuestionario;
    private Button botaoRespostasSalvas;

    public PessoaDatabase pessoaDatabase;  //Todos os jogadores na BD Room

    public static Context contextToString; //Para internacionalizar a exibicao do toString da 'Cidade'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_inicio);
        contextToString = this;  //Para exibir conteudo do string.xml

        //Botao de retorno
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        //Apenas deixa acessar o jogo se tiver pelo menos 6 (seis) jogadores
        pessoaDatabase = PessoaDatabase.getDatabase(ActivityInicio.this);



        //Esse estah funcionando
        botaoQuestionario = findViewById(R.id.botaoQuestionario);
        botaoQuestionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Pessoa> lista = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();
                if(lista.size()<1) {
                    UtilsGUI.avisoErro(ActivityInicio.this, getResources().getString(R.string.inicio_sem_cadastro));
                    return;
                }
                ActivityQuestionario.iniciar(ActivityInicio.this);

            }
        });

       /*botaoJogo = findViewById(R.id.botaoJogo);
        botaoJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<Jogador> lista = (ArrayList<Jogador>) pessoaDatabase.jogadorDAO().queryAll();
                if(lista.size()<6) {
                    UtilsGUI.avisoErro(ActivityInicio.this, getResources().getString(R.string.jogo_faltam_jogadores));
                    return;
                }
                ActivityJogo.iniciar(ActivityInicio.this);

            }
        });*/

        /*botaoEstatistica = findViewById(R.id.botaoEstatistica);
        botaoEstatistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityEstatisticas.iniciar(ActivityInicio.this);

            }
        });*/

        botaoCadastro = findViewById(R.id.botaoCadastro);
        botaoCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ActivityVisualizarPessoas.iniciar(ActivityInicio.this);

            }
        });

        botaoRespostasSalvas = findViewById(R.id.botaoRespostasSalvas);
        botaoRespostasSalvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityVisualizarRespostas.iniciar(ActivityInicio.this);

            }
        });
    }

    public static void iniciar(ActivityCapa activityCapa){

        Intent intent = new Intent(activityCapa, ActivityInicio.class);

        activityCapa.startActivityForResult(intent,1); //Inicia esta ActivityInicio com requestcode=1

    }

    public void finalizar() {

        //Retorno da Activity no fim da chamada
        Intent intent = new Intent();

        setResult(Activity.RESULT_CANCELED, intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        finalizar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.inicio_opcoes_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSobre: {
                ActivitySobre.exibir(this);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}