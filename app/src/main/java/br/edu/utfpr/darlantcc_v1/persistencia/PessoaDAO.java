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

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import br.edu.utfpr.darlantcc_v1.model.Pessoa;

@Dao
public interface PessoaDAO {

    @Insert
    long insert(Pessoa cadastro);

    @Delete
    void delete(Pessoa cadastro);

    @Update
    void update(Pessoa cadastro);

    //Nao diferencia nome da tabela (nao eh case-sensitive)
    @Query("SELECT * FROM pessoa WHERE id = :id")  //:id - parametro vem do metodo
    Pessoa queryForId(int id);

    @Query("SELECT * FROM pessoa ORDER BY id ASC")
    List<Pessoa> queryAll();
}
