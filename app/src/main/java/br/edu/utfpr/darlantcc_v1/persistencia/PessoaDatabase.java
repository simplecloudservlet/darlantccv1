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

package br.edu.utfpr.darlantcc_v1.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;

//exportSchema: backup da estrutura do database=false
@Database(entities = {Pessoa.class}, version=1, exportSchema = false)
public abstract class PessoaDatabase extends RoomDatabase {
    public abstract PessoaDAO pessoaDAO();

    private static PessoaDatabase instance;

    public static PessoaDatabase getDatabase(final Context context){

        if(instance == null){

            synchronized (PessoaDatabase.class){
                if(instance==null){
                    instance = Room.databaseBuilder(context,
                            PessoaDatabase.class,
                            "pessoa.db").allowMainThreadQueries().build();
                }
            }

        }
        return instance;
    }
}
