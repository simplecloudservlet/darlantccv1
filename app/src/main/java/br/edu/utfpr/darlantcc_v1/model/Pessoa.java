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

package br.edu.utfpr.darlantcc_v1.model;

import android.content.res.Resources;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import br.edu.utfpr.darlantcc_v1.ActivityInicio;

@Entity
public class Pessoa {

    //No Room, por padrao, todos os atributos viram colunas na tabela
    //Identificacao da linha da tabela - primary key
    @PrimaryKey(autoGenerate = true)
    private int ID;

    private String NOME;
    private String IDADE;

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOME() {
        return NOME;
    }
    public void setNOME(String NOME) {
        this.NOME = NOME;
    }

    public String getIDADE() {
        return IDADE;
    }
    public void setIDADE(String IDADE) {
        this.IDADE = IDADE;
    }

    public Resources getResources(){
        return ActivityInicio.contextToString.getResources();
    }

    public String toString(){
        return "Nome: " + getNOME() + "\n" +
                "Idade: " + getIDADE() + "\n\n";
    }

}
