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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.utfpr.darlantcc_v1.model.Resposta;
import br.edu.utfpr.darlantcc_v1.model.Resposta;
import br.edu.utfpr.darlantcc_v1.persistencia.RespostaDatabase;
import br.edu.utfpr.darlantcc_v1.utils.UtilsGUI;

public class ActivityVisualizarRespostas extends AppCompatActivity {

    public static final String CAMPO_POSICAO_SELECIONADA = "CAMPO_POSICAO_SELECIONADA";

    private ListView listViewRespostas;
    private ArrayAdapter<Resposta> listaAdapter;
    private ArrayList<Resposta> listaRespostas;

    public RespostaDatabase database;

    private int        posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_respostas);

        setTitle(getResources().getString(R.string.respostas_titulo)+getResources().getString(R.string.registro_titulo_dica));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        listViewRespostas = findViewById(R.id.listViewRespostas);

        //Informar que a listView abre menus de contexto
        registerForContextMenu(listViewRespostas);

        /*listViewRespostas.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    //Quando clica no item do menu, acessa o alterarResposta
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            String mensagem = getString(R.string.reload);

                            //Abre uma janelaModal: é a janaela que aparece acima das outras
                            DialogInterface.OnClickListener listener =
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int opcao) {

                                            switch (opcao) {
                                                case DialogInterface.BUTTON_POSITIVE: {

                                                    posicaoSelecionada = i;

                                                    //carregarJogo();

                                                    break;
                                                }
                                                case DialogInterface.BUTTON_NEGATIVE: {

                                                    Toast.makeText(ActivityVisualizarRespostas.this, R.string.reload_cancelado,Toast.LENGTH_SHORT).show();
                                                    break;
                                                }
                                            }
                                        }
                                    };

                            UtilsGUI.confirmaAcao(ActivityVisualizarRespostas.this, mensagem, listener);
                    }

                });
        */

        //Apenas um item pode ser clicado por vez
        listViewRespostas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        database = RespostaDatabase.getDatabase(ActivityVisualizarRespostas.this);

        popularRespostas();

        popularLista();

    }


    /*public void carregarJogo() {

        //You need to reload the fields from Resposta
        //origem (recebe resposta)/ classe que envia a resposta
        Intent intent = new Intent(this, ActivityQuestionario.class);

        //Extract the useful information from "Resposta:1"
        StringTokenizer token = new StringTokenizer(String.valueOf(listaRespostas.get(posicaoSelecionada)),":");
        token.nextToken(); //jump 'Resposta:' from String

        intent.putExtra(CAMPO_POSICAO_SELECIONADA, token.nextToken());

        this.startActivityForResult(intent, 1); //Vai para o metodo Jogo.onCreate

    }*/

    private void popularRespostas() {
        //Inicializa o ArrayList
        listaRespostas = new ArrayList<>();

        //Atualiza a listaRespostas com os dados coletados do Room, para ter o ID
        listaRespostas = (ArrayList<Resposta>) database.respostaDAO().queryAll();

    }

        private void popularLista(){

            listaRespostas = new ArrayList<>();

            listaRespostas = (ArrayList<Resposta>) database.respostaDAO().queryAll();

            listaAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1,
                    listaRespostas);

            listViewRespostas.setAdapter(listaAdapter);

            listaAdapter.notifyDataSetChanged();
        }


    public static void iniciar(ActivityInicio activityInicio){

        Intent intent = new Intent(activityInicio, ActivityVisualizarRespostas.class);

        activityInicio.startActivityForResult(intent,1); //Inicia esta Activity com requestcode=1

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
    public boolean onContextItemSelected(MenuItem item){

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch(item.getItemId()){

            case R.id.menuItemCadastroExcluir: {
                excluir(info.position);
                return true;
            }
            default: {
                return super.onContextItemSelected(item);
            }
        }

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.registro_menu_contexto, menu);

    }

    private void excluir(int pos) {

            String mensagem = getString(R.string.registro_desejaexcluir);

            //Abre uma janelaModal: é a janaela que aparece acima das outras
            DialogInterface.OnClickListener listener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            switch(i){
                                case DialogInterface.BUTTON_POSITIVE: {

                                    //Extract the useful information from "Resposta:1"
                                    StringTokenizer token = new StringTokenizer(String.valueOf(listaRespostas.get(pos)),":");
                                    token.nextToken(); //jump 'Resposta:' from String
                                    int registro_id = Integer.parseInt(token.nextToken());

                                    database.respostaDAO().delete(database.respostaDAO().queryForId(registro_id));


                                    //Atualiza a listView
                                    listaAdapter.clear(); //Para que o 'custom adapter' exiba a listView apos a remocao
                                    popularLista(); //Para atualizar o banco no room
                                    listaAdapter.notifyDataSetChanged();

                                    Toast.makeText(ActivityVisualizarRespostas.this, R.string.registro_removido, Toast.LENGTH_SHORT).show();

                                    break;
                                }
                                case DialogInterface.BUTTON_NEGATIVE: {
                                    break;
                                }
                            }
                        }
                    };

            UtilsGUI.confirmaAcao(this, mensagem, listener);


        }
    }
