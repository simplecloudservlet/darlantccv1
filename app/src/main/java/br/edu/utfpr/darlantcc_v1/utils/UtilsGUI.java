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

package br.edu.utfpr.darlantcc_v1.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import br.edu.utfpr.darlantcc_v1.R;

public class UtilsGUI {
    public static void confirmaAcao(Context contexto,
                                    String mensagem,
                                    DialogInterface.OnClickListener listener){

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.app_name);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(mensagem);

        builder.setPositiveButton(R.string.sim,listener);
        builder.setNegativeButton(R.string.nao,listener);

        AlertDialog alert = builder.create();
        alert.show();
    }
    //AvisoErro do Campo usuario vazio
    public static void avisoErro(Context contexto, String idTexto){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);

        builder.setTitle(R.string.aviso);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(idTexto);

        builder.setNeutralButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static String validaCampoTexto(Context contexto,
                                          EditText editText,
                                          String idMensagemErro){
        //int TAM = 20; //tamanho maximo da string
        String texto = editText.getText().toString();
        if (stringVazia(texto)) {
            UtilsGUI.avisoErro(contexto, idMensagemErro);
            editText.setText(null);
            editText.requestFocus();
            return null;
        } else {
            return texto.trim();
        }

        /*else
            if(texto.length()<TAM) {
                for(int i=0;i<TAM;i++)
                    texto += ".";
                return texto;
            } else
            //Esse abaixo nao acontece por conta do layout (maxlenght="10")
                if(texto.length()>TAM) {
                    return texto.substring(0,TAM);
                } else {
                    return texto.trim();
                }
            */
    }
    public static void erroCamisa(Context contexto,
                                          String camisa,
                                          String idMensagemErro){

            UtilsGUI.avisoErro(contexto, idMensagemErro);

    }

    public static boolean stringVazia(String texto){
        return texto == null || texto.trim().length()==0;
    }

}
