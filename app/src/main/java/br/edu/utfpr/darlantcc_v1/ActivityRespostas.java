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
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import br.edu.utfpr.darlantcc_v1.model.Resposta;
import br.edu.utfpr.darlantcc_v1.persistencia.RespostaDatabase;

public class ActivityRespostas extends AppCompatActivity {

    private ListView listViewRespostas;
    private ArrayAdapter<Resposta> listaAdapter;
    private ArrayList<Resposta> listaRespostas;

    public RespostaDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Seta fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_respostas);

        setTitle(getResources().getString(R.string.app_name));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Cria uma nova instancia da Activity parent
        }

        listViewRespostas = findViewById(R.id.listViewRespostas);

        database = RespostaDatabase.getDatabase(ActivityRespostas.this);

        popularRespostas();

        popularLista();

    }

    private void popularRespostas() {
        //Inicializa o ArrayList
        listaRespostas = new ArrayList<>();

        //Simula a adicao arbitraria de Respostas na lista e na base de dados
        //
        //Verifica se ha conteudo na base de dados.
        // Se tem conteudo, apenas atualiza a lista (Ex.: fechou o aplicativo, ou saiu da activity)
        // Se nao tem conteudo, cria uma nova lista e uma nova base de dados.

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


    public static void iniciar(AppCompatActivity origem){

        Intent intent = new Intent(origem, ActivityRespostas.class);

        origem.startActivityForResult(intent,1); //Inicia esta ActivityEstatistica com requestcode=1

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
}