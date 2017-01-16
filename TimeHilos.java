package com.Notifications.patientssassistant;


import com.Notifications.patientssassistant.tables.*;
import android.util.Log;
import com.orm.query.Condition;
import com.orm.query.Select;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class TimeHilos {

    public void HilosIniciales()
    {
        int anio, mes, dia, hora, min;

        Calendar cal = new GregorianCalendar();
        cal.getTime();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora=cal.get(Calendar.HOUR_OF_DAY);
        min=cal.get(Calendar.MINUTE);

        TblHilos hilo;
        hilo=new TblHilos("HiloBD",anio,mes,dia,hora,min,false); hilo.save();
        Log.e("El HiloBD", " fue insertado");
        hilo=new TblHilos("HiloBB",anio,mes,dia,hora,min,false); hilo.save();
        hilo=new TblHilos("HiloER",anio,mes,dia,hora,min,false); hilo.save();
        hilo=new TblHilos("HiloBBBD",anio,mes,dia,hora,min,false); hilo.save();

        CalcularTiempoProximoHilo("HiloBB");
        CalcularTiempoProximoHilo("HiloER");
        CalcularTiempoProximoHilo("HiloBBBD");
    }

    public void CalcularTiempoProximoHilo(String nombreHilo)
    {
        int anio, mes, dia, hora, min;

        Calendar cal = new GregorianCalendar();
        cal.getTime();

        if(nombreHilo.equals("HiloBD")){cal.add(Calendar.HOUR_OF_DAY, 8);}
        if(nombreHilo.equals("HiloBB")){cal.add(Calendar.DAY_OF_YEAR, 2);}
        if(nombreHilo.equals("HiloER")){
            if(cal.get(Calendar.DAY_OF_WEEK)==6){cal.add(Calendar.DAY_OF_YEAR, 7);}
            else{ int num=DiasHastaElViernes(cal.get(Calendar.DAY_OF_WEEK));
                cal.add(Calendar.DAY_OF_YEAR, num);}
        }
        if(nombreHilo.equals("HiloBBBD")){cal.add(Calendar.DAY_OF_YEAR, 30);}

        cal.getTime();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora=cal.get(Calendar.HOUR_OF_DAY);
        min=cal.get(Calendar.MINUTE);


        TblHilos rh= Select.from(TblHilos.class)
                .where(Condition.prop("NOMBRE").eq(nombreHilo),
                        Condition.prop("EJECUTADO").eq(0)).first();
        if(rh!=null){
            rh.setNombre(nombreHilo);
            rh.setAnio(anio);
            rh.setMes(mes);
            rh.setDia(dia);
            rh.setHora(hora);
            rh.setMinutos(min);
            rh.setEjecutado(false);
            rh.save();

            Log.e("El "+nombreHilo, " fue insertado");
        }
        //return cal.getTimeInMillis();
    }

    public int DiasHastaElViernes(int dia){
        int num=0;
        switch (dia){
            case 1: num=5; break;
            case 2: num=4; break;
            case 3: num=3; break;
            case 4: num=2; break;
            case 5: num=1; break;
            case 6: num=0; break;
            case 7: num=6; break;
        }
        return num;
    }

    public Long TiempoEjecutarHiloDe(String nombreHilo){
        Boolean ejecutar=HiloEjecutadoONo(nombreHilo);
        Long milis=300000L;
        if(ejecutar==false){
            milis=TiempoEnMilisegundos(nombreHilo);
        }else{
            //if(!nombreHilo.equals("HiloBD")){milis=60000L;}
        }
        return milis;
    }

    public Boolean HiloEjecutadoONo(String nombreHilo)
    {
        Boolean ejecutar=false;
        int anio, mes, dia, hora, min;

        Calendar cal = new GregorianCalendar();
        cal.getTime();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora=cal.get(Calendar.HOUR_OF_DAY);
        min=cal.get(Calendar.MINUTE);

        TblHilos rh= Select.from(TblHilos.class)
                 .where(Condition.prop("NOMBRE").eq(nombreHilo),
                        Condition.prop("EJECUTADO").eq(0)).first();
        if (rh!=null){
            if (rh.getAnio()==anio && rh.getMes()==mes && rh.getDia()==dia && rh.getHora()==hora && rh.getMinutos()==min) {
                ejecutar = true;
            }
        }
        return ejecutar;
    }

    public Boolean EjecutarONo(String nombreHilo)
    {
        Boolean ejecutar=false;

        TblHilos rh= Select.from(TblHilos.class)
                .where(Condition.prop("NOMBRE").eq(nombreHilo),
                        Condition.prop("EJECUTADO").eq(0)).first();
        if (rh!=null){
            ejecutar=true;
        }
        return ejecutar;
    }

    private Long TiempoEnMilisegundos(String nombreHilo){

        int anio, mes, dia, hora, min;
        int anio1, mes1, dia1, hora1, min1;
        Long resta=0L;

        Calendar cal = new GregorianCalendar();
        cal.getTime();
        anio = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora=cal.get(Calendar.HOUR_OF_DAY);
        min=cal.get(Calendar.MINUTE);
        cal.set(anio, mes, dia, hora, min);

        TblHilos rh= Select.from(TblHilos.class)
                .where(Condition.prop("NOMBRE").eq(nombreHilo),
                        Condition.prop("EJECUTADO").eq(0)).first();

        Calendar cal1 = new GregorianCalendar();
        cal1.getTime();

        if(rh!=null){
            anio1=rh.getAnio();
            mes1=rh.getMes();
            dia1=rh.getDia();
            hora1=rh.getHora();
            min1=rh.getHora();
            cal1.set(anio1, mes1, dia1, hora1, min1);

            Long mili=cal.getTimeInMillis();
            Long mili1=cal1.getTimeInMillis();
            resta =mili1-mili;
            if(resta==0){resta=300000L;}
        }

        return resta;
    }


}