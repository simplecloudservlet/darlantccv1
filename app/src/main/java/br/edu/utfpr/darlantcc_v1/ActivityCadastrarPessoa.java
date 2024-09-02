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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;
import br.edu.utfpr.darlantcc_v1.persistencia.PessoaDatabase;
import br.edu.utfpr.darlantcc_v1.utils.UtilsGUI;

/**
 * Aqui eh feito o cadastro de Pessoas
 */
public class ActivityCadastrarPessoa extends AppCompatActivity {

    //public static final String CAMPO_ID = "CAMPO_ID";
    public static final String CAMPO_NOME = "CAMPO_NOME";
    public static final String CAMPO_IDADE = "CAMPO_IDADE";

    public static final String MODO = "MODO";
    public static final int NOVO = 1;
    public static final int ALTERAR = 2;

    private TextView cadastro_ID;

    private EditText cadastro_etNome;
    private Spinner cadastro_spinnerIdade;

    private int modo;

    public static final int RESULT_DELETED = 1;

    private ArrayList<Pessoa> listaPessoas; //local
    private PessoaDatabase pessoaDatabase; //BD Room

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cadastrar_pessoa);

        setTitle(getResources().getString(R.string.cadastro_titulo));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        pessoaDatabase = PessoaDatabase.getDatabase(this);
        listaPessoas = (ArrayList<Pessoa>) pessoaDatabase.pessoaDAO().queryAll();

        cadastro_ID = findViewById(R.id.cadastro_ID);

        cadastro_etNome = findViewById(R.id.cadastro_et_nome);
        
        //--
        cadastro_spinnerIdade = findViewById(R.id.spinnerIdade);
        String[] lista = getResources().getStringArray(R.array.array_idade);
        String[] lista_campo_nulo = new String[18]; //Primeiro campo nulo
        lista_campo_nulo[0] = "";
        for (int i = 1; i < lista_campo_nulo.length; i++) { //Insere os outros 2 campos com os nomes dos times
            lista_campo_nulo[i] = lista[i - 1];
        }
        //Deixa o inicio vazio para que possa haver exibicao dinamica com base nas idades nao selecionadas 
        ArrayAdapter adapterIdade =
                new ArrayAdapter(ActivityCadastrarPessoa.this, R.layout.spinner_item, lista_campo_nulo);

        cadastro_spinnerIdade.setAdapter(adapterIdade);

        /*cadastro_spinnerIdade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //Atualiza a lista de camisas que ainda nao foram selecionadas no time
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //your code here
                String campoTime = getResources().getString(R.string.cadastro_time1);
                switch (position) {
                    case 1:
                        cadastro_spinnerCamisas.setVisibility(View.VISIBLE);
                        cadastro_spinnerCamisas.setEnabled(true);
                        campoTime = getResources().getString(R.string.cadastro_time1);
                        break;
                    case 2:
                        cadastro_spinnerCamisas.setVisibility(View.VISIBLE);
                        cadastro_spinnerCamisas.setEnabled(true);
                        campoTime = getResources().getString(R.string.cadastro_time2);
                        break;
                    default:
                        cadastro_spinnerCamisas.setVisibility(View.INVISIBLE);
                        cadastro_spinnerCamisas.setEnabled(false);
                        break;
                }

                //Atualiza o spinner
                ArrayList<String> lista = new ArrayList<>();

                //---
                //Insere primeiro a idade atual
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                if (bundle != null) {

                    modo = bundle.getInt(MODO);

                    if (modo == ALTERAR) {
                        lista.add(bundle.getString("CAMPO_IDADE"));
                    }
                }
                //---

                //
                ArrayAdapter adapter =
                        new ArrayAdapter(ActivityCadastrarPessoa.this, R.layout.spinner_item, lista);

                //Para exibir os elementos espaçados no segundo spinner
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                cadastro_spinnerIdade.setAdapter(adapter);
                //adapter.notifyDataSetChanged();

                Toast.makeText(ActivityCadastrarPessoa.this, "LOG1: PASSEI POR AQUI", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Nothing is done
            }


        });
        */

        //--

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            modo = bundle.getInt(MODO);

            if (modo == ALTERAR) {

                cadastro_ID.setText(bundle.getString("CAMPO_ID"));
                cadastro_etNome.setText(bundle.getString("CAMPO_NOME"));

                //Idade
                String campoIdade = bundle.getString("CAMPO_IDADE");
                String[] listaIdade = getResources().getStringArray(R.array.array_idade);

                int posicao = 0;
                for (int i = 0; i < listaIdade.length; i++) {
                    Log.d("LOG:CADASTRO", "campoIdade:" + campoIdade + " listaTimes["+i+"]:"+listaIdade[i]);
                    if (listaIdade[i].equals(campoIdade))
                        posicao = i;
                }
                cadastro_spinnerIdade.setSelection(posicao+1); //+1 por causa do campo nulo


            }

        }

    }

    public static void novoCadastro(AppCompatActivity activityPessoaes) {
        //origem (recebe resposta)/ classe que envia a resposta
        Intent intent = new Intent(activityPessoaes, ActivityCadastrarPessoa.class);

        intent.putExtra(MODO, NOVO);

        activityPessoaes.startActivityForResult(intent, NOVO); //Vai para o metodo Cadastro.onCreate
    }

    public static void alterarCadastro(AppCompatActivity activityPessoas, Pessoa pessoa) {
        //origem (recebe resposta)/ classe que envia a resposta
        Intent intent = new Intent(activityPessoas, ActivityCadastrarPessoa.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra("CAMPO_ID", pessoa.getID() + "");
        intent.putExtra("CAMPO_NOME", pessoa.getNOME());
        intent.putExtra("CAMPO_IDADE",pessoa.getIDADE());


        activityPessoas.startActivityForResult(intent, ALTERAR); //Vai para o metodo Cadastro.onCreate
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

    //Invocado ao clicar no icone 'Salvar'
    public void salvar(AppCompatActivity activityCadastro) {

        Pessoa pessoa;
        //Quero preservar a POSICAO, caso ele esteja em pessoaesQuadra
        if(modo == NOVO)
            pessoa = new Pessoa();
        else
            //MODO==ALTERAR
            pessoa = pessoaDatabase.pessoaDAO().queryForId(Integer.parseInt((String)cadastro_ID.getText()));


        Intent intent = new Intent();

        String mensagem = "";

        String oldNome = cadastro_etNome.getText().toString();

        String campoNome = UtilsGUI.validaCampoTexto(this,
                cadastro_etNome,
                getString(R.string.cadastro_nomevazio));
        if (campoNome == null) {
            return;    //Nao continua o codigo a seguir
        }


        mensagem += getString(R.string.cadastro_nome) + campoNome + "\n";
        //Guarda no Intent
        //
        //Remove todos os espacos do nome
        //campoNome = campoNome.replace(" ","").trim();
        intent.putExtra(CAMPO_NOME, campoNome);
        //Guarda para o banco
        pessoa.setNOME(campoNome);


        //IDADE
        mensagem += getString(R.string.cadastro_idade);

        String campoIdade = (String) cadastro_spinnerIdade.getSelectedItem();
        if(campoIdade.equals("")) {
            UtilsGUI.avisoErro(this,
                    getString(R.string.cadastro_idade_naoSelecionada));
            return;    //Nao continua o codigo a seguir
        }

        mensagem += campoIdade + "\n";
        intent.putExtra(CAMPO_IDADE, campoIdade);
        //
        //Guarda para o BD
        pessoa.setIDADE(campoIdade);

        //Se chegou até aqui, estah tudo ok
        if (modo == NOVO) {

            //Deixa o Room atribuir o ID
            pessoaDatabase.pessoaDAO().insert(pessoa);

            //Toast.makeText(this, "PASSEI POR AQUI", Toast.LENGTH_SHORT).show();
        } else {
            //Mantem dados anteriores do pessoa
            //Mantem a POSICAO do pessoa, update

            Log.d("CADASTRO_ALTERAR", pessoa.getNOME());

            //Adquire o id do pessoa
            pessoa.setID(Integer.parseInt(cadastro_ID.getText().toString()));


            pessoaDatabase.pessoaDAO().update(pessoa);
        }


        //Nao Poe no intent o pessoa.ID gerado pelo banco de dados.
        //O ID soh estara disponivel apos atualizar o room, na chamada do metodo 'queryAll'

        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        Log.d("LOG:", mensagem);

        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    //Invocado ao clicar no icone 'excluir'
    public void excluir(AppCompatActivity activityCadastro) {

        //Nao ha como ter certeza que o retorno do DAO serah null
        //if(pessoa!=null){

        Intent intent = new Intent();
        if (!cadastro_ID.getText().toString().equals("-1")) { //Pessoa estah na Room

            int idPessoa = Integer.parseInt(cadastro_ID.getText().toString());

            PessoaDatabase pessoaDatabase = PessoaDatabase.getDatabase(this);
            Pessoa pessoa = pessoaDatabase.pessoaDAO().queryForId(idPessoa);

            String mensagem = getString(R.string.cadastro_apagar) + "\n\n" +
                    getString(R.string.cadastro_nome) + pessoa.getNOME() + "\n\n";

            //Abre uma janelaModal: é a janaela que aparece acima das outras
            DialogInterface.OnClickListener listener =
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            switch (i) {
                                case DialogInterface.BUTTON_POSITIVE: {

                                    pessoaDatabase.pessoaDAO().delete(pessoa);

                                    //listaPessoas.remove(posicaoSelecionada);

                                    //listaAdapter.notifyDataSetChanged();

                                    Toast.makeText(ActivityCadastrarPessoa.this, R.string.cadastro_removido, Toast.LENGTH_SHORT).show();

                                    Log.d("LOG:", getString(R.string.cadastro_removido));
                                    setResult(RESULT_DELETED, intent);
                                    finish();

                                    break;
                                }
                                case DialogInterface.BUTTON_NEGATIVE: {
                                    break;
                                }
                            }
                        }
                    };

            UtilsGUI.confirmaAcao(ActivityCadastrarPessoa.this, mensagem, listener);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cadastro_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuItemSalvar: {
                salvar(this);
                return true;
            }
            /*case R.id.menuItemExcluir: {
                Toast.makeText(this, "Opção excluir indisponível. Aguarde.", Toast.LENGTH_SHORT).show();
                //excluir(this);
                return true;
            }*/
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    //---Testes Unitarios
    public static int teste_inserirCadastro(){
        return 1;
    }
}