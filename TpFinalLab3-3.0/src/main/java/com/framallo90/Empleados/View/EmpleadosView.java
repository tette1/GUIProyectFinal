/**
 * Clase que proporciona métodos para la interacción con el usuario para la gestión de empleados.
 *
 * @author Framballo90
 * @since v1.0
 */
package com.framallo90.Empleados.View;

import com.framallo90.Empleados.Model.Entity.Empleados;
import com.framallo90.consola.Consola;

import java.util.List;

public class EmpleadosView {

    /**
     * Genera un nuevo objeto Empleados a partir de la información ingresada por el usuario.
     *
     * @return Un nuevo objeto Empleados con los datos ingresados.
     */
    public Empleados generarEmpleado() {
        String nombre, apellido, username, password, tipo;
        Integer dni;

        nombre = Consola.ingresarXString("el Nombre");
        apellido = Consola.ingresarXString("el Apellido");
        dni = Consola.ingresarXInteger("DNI");
//        Consola.limpiarBuffer();
        username = Consola.ingresarXStringSimple("el Username");
        while (true){
            password = Consola.ingresarXStringSimple("la Password");
            if (validarPassword(password)) break;
            else if (password.equals("0")) return null;
            else Consola.soutString("Contraseña Inválida!. Debe tener al menos:" +
                        "\n 1 letra minúscula" +
                        "\n 1 letra mayúscula" +
                        "\n 1 número" +
                        "\n 1 caracter especial (!#$%&);");
        }
        tipo = this.generarTipo();

        return new Empleados(nombre, apellido, dni, 0, username, password, tipo);
    }

    private boolean validarPassword(String password) {

        // Patrón de expresión regular para la validación
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).*$";

        // Validación usando expresiones regulares
        return password.matches(regex);
    }

    /**
     * Permite al usuario seleccionar el tipo de empleado (vendedor o administrador) y lo devuelve como cadena.
     *
     * @return El tipo de empleado seleccionado por el usuario.
     */
    public String generarTipo() {
        String tipo;

        while (true) {
            tipo = Consola.ingresarXString("""
                    \n  Tipos de Usuario:
                    - Vendedor
                    - Administrador\n""");

            if (tipo.equalsIgnoreCase("administrador") ||
                    tipo.equalsIgnoreCase("vendedor")) {
                return tipo;
            } else {
                System.out.println("Ingresar un dato válido!.");
            }
        }
    }


    //DOCUMENTAR
    public void printMenuAdministrador(){
        System.out.println();
        System.out.println("--- MENU EMPLEADOS (administrador) ---\n");
        System.out.println("1. Agregar Empleado.");
        System.out.println("2. Modificar Empleado.");
        System.out.println("3. Eliminar Empleado.");
        System.out.println("4. Buscar un Empleado");
        System.out.println("5. Historial de Empleado.");
        System.out.println("\n0. Volver.");
    }

    public void mostrarEmpleado(Empleados empleados){

        System.out.println("----------");
        System.out.println("- ID................: " + empleados.getId());
        System.out.println("- Nombre............: " + empleados.getApellido() + ", " + empleados.getNombre());
        System.out.println("- DNI...............: "+empleados.getDni());
        System.out.println("- Tipo de Usuario...: "+empleados.getTipo());
        System.out.println("- Autos Vendidos....: "+empleados.getAutosvendidos());
        System.out.println("- Username..........: "+empleados.getUsername());
        System.out.println("----------");
    }
    public void muestroEmpleados(List<Empleados> empleados){
        for (Empleados empleados1 : empleados){
            mostrarEmpleado(empleados1);
        }
    }
}