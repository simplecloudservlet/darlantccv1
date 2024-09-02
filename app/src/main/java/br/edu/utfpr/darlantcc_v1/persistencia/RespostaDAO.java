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
import br.edu.utfpr.darlantcc_v1.model.Resposta;

@Dao
public interface RespostaDAO {

    @Insert
    long insert(Resposta resposta);

    @Delete
    void delete(Resposta resposta);

    @Update
    void update(Resposta resposta);

    //Nao diferencia nome da tabela (nao eh case-sensitive)
    @Query("SELECT * FROM resposta WHERE id = :id")  //:id - parametro vem do metodo
    Resposta queryForId(int id);

    @Query("SELECT * FROM resposta ORDER BY id DESC")
    List<Resposta> queryAll();
}
