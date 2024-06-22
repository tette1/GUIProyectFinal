package com.framallo90.Automovil.View;
import com.framallo90.Automovil.Model.Entity.Automovil;
import com.framallo90.Excepciones.EmptyAStockException;
import com.framallo90.consola.Consola;
import java.util.List;
public class AutomovilView {
    public AutomovilView() {
    }
    public void mostrarAutomoviles(List<Automovil> automovilList) throws EmptyAStockException
    {
        /**
         * Metodo que muestra el stock actual de vehiculos, recibiendo por parametro la lista cargada (o no)
         * Si esta vacia evitaremos iterar sobre ella para avisar al usuario que la lista esta vacia
         * Cuando tenga elementos se iterará con un for-each mostrando cada automovil, finalmente se informa
         * acerca del contador de automoviles.
         */
        if (automovilList==null)
            throw new EmptyAStockException("No hay Vehiculos registrados.");

        for (Automovil automovil : automovilList)
        {
            System.out.println(automovil.toString());
        }
        System.out.println("Total de Vehiculos en Stock -> " + Automovil.getCont());
    }

    public String ingresoMarca(){
        return Consola.ingresarXString("la Marca");
    }
    public String ingresoModelo(){
        return Consola.ingresarXStringSimple("el Modelo");
    }

    public Double ingresoPrecio(){
        return Consola.ingresarXdouble("el Precio");
    }
    public String ingresoPatente()
    {
        return Consola.patente("la Patente");
    }
    public Integer ingresoAnio() {return Consola.ingresarXInteger("el Anio");
    }

    public void printMenuAutomovilAdmin(){
        System.out.println("""
                --- MENU AUTOMOVIL (administrador) ---\n
                1. Agregar
                2. Eliminar
                3. Buscar Automovil
                4. Listado de Automoviles (con filtro)\n
                0. Volver""");
    }
    public void printMenuAutomovilVendedor(){
        System.out.println("""
                --- MENU AUTOMOVIL (vendedor) --- \n
                1. Buscar Automovil
                2. Listado de Automoviles
                3. Listado de Automoviles (con filtro)\n
                0. Volver""");
    }
}
