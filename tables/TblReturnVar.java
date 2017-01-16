package com.Notifications.patientssassistant.tables;

import android.util.Log;

import com.orm.SugarRecord;

/**
 * Created by Jazmine on 10/01/2016.
 */
public class TblReturnVar extends SugarRecord<TblReturnVar> {

    private String Nombre;
    private Long Ids;
    private Boolean Estado;

    public TblReturnVar(){}

    public TblReturnVar(String nombre, Long ids, Boolean estado) {
        Nombre = nombre;
        Ids = ids;
        Estado = estado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public Long getIds() {
        return Ids;
    }

    public void setIds(Long ids) {
        Ids = ids;
    }

    public Boolean getEstado() {
        return Estado;
    }

    public void setEstado(Boolean estado) {
        Estado = estado;
    }

    public static void EliminarDatos(){
        TblReturnVar.executeQuery("delete from "+TblReturnVar.getTableName(TblReturnVar.class));
        if(TblReturnVar.count(TblReturnVar.class)==0) {
            Log.i("TBLRETURNVAR", "------------->Eliminado");
        }else{
            Log.i("TBLRETURNVAR", "-------------> NOOOOOO Eliminado :'(");
        }
    }
}
