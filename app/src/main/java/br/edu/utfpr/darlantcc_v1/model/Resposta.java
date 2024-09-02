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
public class Resposta {

    //No Room, por padrao, todos os atributos viram colunas na tabela
    //Identificacao da linha da tabela - primary key
    @PrimaryKey(autoGenerate = true)
    private int ID;  //ID gerado automaticamente pelo Room

    // Decidi utilizar apenas o ID do Room
    // private String ID_RESPOSTA; //ID gerado, para comecar em 0 (zero)

    //ID_CADASTRO: Cadastro que respondeu o questionario
    private String ID_PESSOA;

    private String R1;
    private String R2;
    private String R3;
    private String R4;
    private String R5;

    private String R6;
    private String R7;
    private String R8;
    private String R9;
    private String R10;

    private String R11;
    private String R12;
    private String R13;
    private String R14;
    private String R15;

    private String R16;
    private String R17;
    private String R18;
    private String R19;
    private String R20;

    private String R21;
    private String R22;
    private String R23;
    private String R24;
    private String R25;


    //ID automatico do Room
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    //ID manual do lance (para comecar em 0 (zero))
   // public String getID_RESPOSTA() { return ID_RESPOSTA; }
   // public void setID_RESPOSTA(String ID_RESPOSTA) { this.ID_RESPOSTA = ID_RESPOSTA; }

    public String getID_PESSOA() { return ID_PESSOA; }
    public void setID_PESSOA(String ID_PESSOA) { this.ID_PESSOA = ID_PESSOA; }

    public String getR1() {
        return R1;
    }
    public void setR1(String R1) {
        this.R1 = R1;
    }

    public String getR2() {
        return R2;
    }
    public void setR2(String R2) {
        this.R2 = R2;
    }

    public String getR3() {
        return R3;
    }
    public void setR3(String R3) {
        this.R3 = R3;
    }

    public String getR4() {
        return R4;
    }
    public void setR4(String R4) {
        this.R4 = R4;
    }

    public String getR5() {
        return R5;
    }
    public void setR5(String R5) {
        this.R5 = R5;
    }

    public String getR6() {
        return R6;
    }
    public void setR6(String R6) {
        this.R6 = R6;
    }

    public String getR7() {
        return R7;
    }
    public void setR7(String R7) {
        this.R7 = R7;
    }

    public String getR8() {
        return R8;
    }
    public void setR8(String R8) {
        this.R8 = R8;
    }

    public String getR9() {
        return R9;
    }
    public void setR9(String R9) {
        this.R9 = R9;
    }

    public String getR10() {
        return R10;
    }
    public void setR10(String R10) {
        this.R10 = R10;
    }

    public String getR11() {
        return R11;
    }
    public void setR11(String R11) {
        this.R11 = R11;
    }

    public String getR12() {
        return R12;
    }
    public void setR12(String R12) {
        this.R12 = R12;
    }

    public String getR13() {
        return R13;
    }
    public void setR13(String R13) {
        this.R13 = R13;
    }

    public String getR14() {
        return R14;
    }
    public void setR14(String R14) {
        this.R14 = R14;
    }

    public String getR15() {
        return R15;
    }
    public void setR15(String R15) {
        this.R15 = R15;
    }

    public String getR16() {
        return R16;
    }
    public void setR16(String R16) {
        this.R16 = R16;
    }

    public String getR17() {
        return R17;
    }
    public void setR17(String R17) {
        this.R17 = R17;
    }

    public String getR18() {
        return R18;
    }
    public void setR18(String R18) {
        this.R18 = R18;
    }

    public String getR19() {
        return R19;
    }
    public void setR19(String R19) {
        this.R19 = R19;
    }

    public String getR20() {
        return R20;
    }
    public void setR20(String R20) {
        this.R20 = R20;
    }

    public String getR21() {
        return R21;
    }
    public void setR21(String R21) {
        this.R21 = R21;
    }

    public String getR22() {
        return R22;
    }
    public void setR22(String R22) {
        this.R22 = R22;
    }

    public String getR23() {
        return R23;
    }
    public void setR23(String R23) {
        this.R23 = R23;
    }

    public String getR24() {
        return R24;
    }
    public void setR24(String R24) {
        this.R24 = R24;
    }

    public String getR25() {
        return R25;
    }
    public void setR25(String R25) {
        this.R25 = R25;
    }

    //Nao posso passar o argumento 'Cadastro' (Room soh aceita tipo primitivo e String)
    public Resposta() {
    }




    public Resources getResources() {
        return ActivityInicio.contextToString.getResources();
    }

    @Override
    public String toString() {
        //Exibe apenas alguns campos
        //Resposta: [ID_RESPOSTA][ID_PESSOA][R1][R2]...[R25]
        String result = "Resposta" +
                ":" + getID() +
                ":" + getID_PESSOA() +

                ":" + getR1() +
                ":" + getR2() +
                ":" + getR3() +
                ":" + getR4() +
                ":" + getR5() +

                ":" + getR6() +
                ":" + getR7() +
                ":" + getR8() +
                ":" + getR9() +
                ":" + getR10() +

                ":" + getR11() +
                ":" + getR12() +
                ":" + getR13() +
                ":" + getR14() +
                ":" + getR15() +

                ":" + getR16() +
                ":" + getR17() +
                ":" + getR18() +
                ":" + getR19() +
                ":" + getR20() +

                ":" + getR21() +
                ":" + getR22() +
                ":" + getR23() +
                ":" + getR24() +
                ":" + getR25();

        return result;
    }//ArrayAdapter da ListView usa o metodo toString

}