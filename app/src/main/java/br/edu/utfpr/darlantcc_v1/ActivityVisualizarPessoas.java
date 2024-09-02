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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;
import br.edu.utfpr.darlantcc_v1.persistencia.PessoaDatabase;
import br.edu.utfpr.darlantcc_v1.utils.UtilsGUI;

public class ActivityVisualizarPessoas extends AppCompatActivity {

    private ListView      listViewPessoas;
    private PessoaAdapter listaAdapter;

    private ArrayList<Pessoa> listaPessoas; //memoria local
    private PessoaDatabase pessoaDatabase;   //BD

    private int        posicaoSelecionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pessoas);

        setTitle(getResources().getString(R.string.app_name));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        ////////////////
        listViewPessoas = findViewById(R.id.listViewPessoas);

        //Informar que a listView abre menus de contexto
        registerForContextMenu(listViewPessoas);

        listViewPessoas.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    //Quando clica no item do menu, acessa o alterarPessoa
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        posicaoSelecionada = i;

                        alterarPessoa();

                    }
                });

        //Apenas um item pode ser clicado por vez
        listViewPessoas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //inicia artificialmente a lista de pessoas
        //quando nao ha nenhum cadastrado
        popularPessoas();

        popularLista();
    }

    private void popularPessoas() {

        pessoaDatabase = PessoaDatabase.getDatabase(this);

        //Simula a adicao arbitraria de pessoas na lista e na base de dados
        //
        //Verifica se ha conteudo na base de dados.
        // Se tem conteudo, apenas atualiza a lista (Ex.: fechou o aplicativo, ou saiu da activity)
        // Se nao tem conteudo, cria uma nova lista e uma nova base de dados.

        //Atualiza a listaPessoas com os dados coletados do Room, para ter o ID
        listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();

        if(listaPessoas.size()<6) { //Abriu o app e nao tem nada na base de dados OU
                                     //   Cadastrou menos de 6 pessoas

            for (int i = 0; i < 6; i++) {
                Pessoa pessoa = new Pessoa();
                pessoa.setNOME("Fulano" + i);

                listaPessoas.add(pessoa);

                pessoaDatabase.pessoaDAO().insert(pessoa);

            }
            //Precisa atualizar de novo a consulta da base de dados, para ter o ID do que foi inserido
            //Atualiza a listaPessoas com os dados coletados do Room, para ter o ID
            listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();
        }

    }
    private void popularLista(){

        listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();


        //Esse estah funcionando
        /*listaAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listaPessoas);
        */
        listaAdapter = new PessoaAdapter(this, R.layout.linha_pessoa, listaPessoas);
        listViewPessoas.setAdapter(listaAdapter); //Based on: https://stackoverflow.com/questions/34328235/how-to-extends-listactivity-where-appcompatactivity-in-android-activity

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();
            if(bundle != null){

                String nome = bundle.getString(ActivityCadastrarPessoa.CAMPO_NOME);
                String idade = bundle.getString(ActivityCadastrarPessoa.CAMPO_IDADE);

                if (requestCode == ActivityCadastrarPessoa.ALTERAR) { //Apenas altera o cadastro

                    posicaoSelecionada = -1;

                } else { //Novo cadastro, adiciona na lista
                    Pessoa pessoa = new Pessoa();

                    //ID foi gerado pelo Room, em Cadastro.salvar --> insert
                    //pessoa.setID(Integer.parseInt(ID));
                    pessoa.setNOME(nome);
                    pessoa.setIDADE(idade);

                    //Toast.makeText(this,cidade.isVisitada()+"",Toast.LENGTH_SHORT).show();

                    listaPessoas.add(pessoa);
                }

                //Jah guardou no banco no metodo 'Cadastro.salvar'.
                //Atualiza a listView
                listaAdapter.clear(); //Para que o 'custom adapter' exiba a listView apos a insercao
                popularLista(); //Para atualizar o banco no room e me dar o ID do pessoa. Caso contario, pessoa.getID nao funciona :-)
                listaAdapter.notifyDataSetChanged();

            }
        }
        if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this,
                    R.string.cadastro_cancelado, Toast.LENGTH_SHORT).show();
        }
        if(resultCode == ActivityCadastrarPessoa.RESULT_DELETED){
            Toast.makeText(this,
                    R.string.cadastro_removido, Toast.LENGTH_SHORT).show();
            //Atualiza a listView
            listaAdapter.clear(); //Para que o 'custom adapter' exiba a listView apos a insercao
            popularLista(); //Para atualizar o banco no room e me dar o ID do pessoa. Caso contario, pessoa.getID nao funciona :-)
            listaAdapter.notifyDataSetChanged();
        }

    }



    private void alterarPessoa(){

        Pessoa pessoa = listaPessoas.get(posicaoSelecionada);

        //Toast.makeText(this,cidade.isVisitada()+"",Toast.LENGTH_SHORT).show();

        ActivityCadastrarPessoa.alterarCadastro(this, pessoa);
    }

    public static void iniciar(ActivityInicio activityInicio){

        Intent intent = new Intent(activityInicio, ActivityVisualizarPessoas.class);

        activityInicio.startActivityForResult(intent,1); //Inicia esta ActivityPessoas com requestcode=1

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
        getMenuInflater().inflate(R.menu.pessoa_opcoes,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.menuItemAdicionar: {
                ActivityCadastrarPessoa.novoCadastro(this);
                return true;
            }
            case R.id.menuItemCadastroExcluir: {
                //excluirTodos(this);
                Toast.makeText(this,"Opção excluir todos indisponível. Aguarde.",Toast.LENGTH_SHORT).show();
                return true;
            }

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    /*private void excluirTodos(ActivityPessoas activityPessoas) {

        String mensagem = getString(R.string.pessoa_desejaexcluirtodos);

        //Abre uma janelaModal: é a janaela que aparece acima das outras
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch(i){
                            case DialogInterface.BUTTON_POSITIVE: {

                                PessoaDatabase database = PessoaDatabase.getDatabase(ActivityPessoas.this);
                                for(Pessoa pessoa : listaPessoas) {
                                    database.pessoaDAO().delete(pessoa);
                                }

                                //Atualiza a listView
                                listaAdapter.clear(); //Para que o 'custom adapter' exiba a listView apos a remocao
                                popularLista(); //Para atualizar o banco no room
                                listaAdapter.notifyDataSetChanged();

                                Toast.makeText(ActivityPessoas.this, R.string.cadastro_removido, Toast.LENGTH_SHORT).show();

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
*/
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

        getMenuInflater().inflate(R.menu.pessoa_menu_contexto, menu);

    }

    private void excluir(int pos) {

        String mensagem = getString(R.string.cadastro_desejaexcluir);

        //Abre uma janelaModal: é a janaela que aparece acima das outras
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        switch(i){
                            case DialogInterface.BUTTON_POSITIVE: {

                                Pessoa pessoa = listaPessoas.get(pos);

                                pessoaDatabase.pessoaDAO().delete(pessoa);


                                //Atualiza a listView
                                listaAdapter.clear(); //Para que o 'custom adapter' exiba a listView apos a remocao
                                popularLista(); //Para atualizar o banco no room
                                listaAdapter.notifyDataSetChanged();

                                Toast.makeText(ActivityVisualizarPessoas.this, R.string.cadastro_removido, Toast.LENGTH_SHORT).show();

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