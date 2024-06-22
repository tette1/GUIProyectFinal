package com.framallo90.Comprador.Controller;
import com.framallo90.Comprador.Model.Entity.Comprador;
import com.framallo90.Comprador.Model.Repository.CompradorRepository;
import com.framallo90.Comprador.View.CompradorView;
import com.framallo90.Excepciones.InvalidIdNotFound;
import com.framallo90.consola.Consola;
public class CompradorController {
    CompradorView compradorView;
    CompradorRepository compradorRepository;
    public CompradorController(CompradorView compradorView, CompradorRepository compradorRepository) {
        this.compradorView = compradorView;
        this.compradorRepository = compradorRepository;
    }
    public void compradorMenu(){
        int opt;
        do {
            System.out.println();
            System.out.println("--- MENU CLIENTES ---\n");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Modificar Cliente");
            System.out.println("3. Elimniar Cliente");
            System.out.println("4. Buscar un Cliente");
            System.out.println("5. Historial de Clientes\n");
            System.out.println("0. Volver.");
            opt = Consola.ingresarXInteger("una opcion del Menu Cliente");
            switch (opt){
                case 1:
                    add();
                    break;
                case 2:
                    this.verHisorial();
                    update();
                    break;
                case 3:
                    this.verHisorial();
                    remove();
                    break;

                case 4: //buscar
                    this.verHisorial();
                    show();
                    break;

                case 5: //ver todos
                    verHisorial();
                    break;

                case 6:
                    System.out.println("Saliste del Menu.");
                    break;

                default:
                    System.out.println("Opcion Invalida! Vuelva a intentarlo");
                    break;
            }
        }while (opt!=0);
    }
    public void add(){
        String nombre = Consola.ingresarXString("el Nombre");
        String apellido = Consola.ingresarXString("el Apellido");
        Integer dni = Consola.ingresarXInteger("el DNI");
        String email = compradorView.ingresoEmail();
        Comprador comprador = new Comprador(nombre,apellido,dni,email);
        this.compradorRepository.add(comprador);
    }
    public void remove(){
        compradorRepository.remove(Consola.ingresarXInteger("el ID"));
    }
    public void update()
    {
        int opt;
        Comprador comprador = compradorRepository.find(Consola.ingresarXInteger("el ID del Comprado"));
        if (comprador != null){
            do{
                System.out.println("1. Nombre");
                System.out.println("2. Apellido");
                System.out.println("3. DNI");
                System.out.println("4. E-Mail");
                System.out.println();
                System.out.println("5. SALIR.");
                System.out.println();
                System.out.println("Ingrese el ID del Cliente a modificar: ");
                opt = Consola.ingresarXInteger("un campo para modificar");
                switch (opt){
                    case 0:
                        System.out.println("Saliste de la modificacion Cliente.");
                        break;
                    case 1:
                        compradorRepository.cambioNombre(comprador,Consola.ingresarXString("el Nuevo Nombre"));
                        break;
                    case 2:
                        compradorRepository.cambioApellido(comprador,Consola.ingresarXString("el Nuevo Apellido"));
                        break;
                    case 3:
                        compradorRepository.cambioDni(comprador, Consola.ingresarXInteger("el Nuevo DNI"));
                        break;
                    case 4:
                        compradorRepository.cambioEmail(comprador, compradorView.ingresoEmail());
                        break;
                    default:
                        System.out.println("Opcion Invalida!, Vuelva a intentarlo.");
                        break;
                }
            }while (opt != 0);
        }
    }
    //sobrecarga
    public void update(Comprador comprador)
    {
        int opt;
        do{
            System.out.println("1. Nombre");
            System.out.println("2. Apellido");
            System.out.println("3. DNI");
            System.out.println("4. E-Mail");
            System.out.println("5. SALIR.");
            opt = Consola.ingresarXInteger("una campo a modificar");
            switch (opt){
                case 1:
                    compradorRepository.cambioNombre(comprador,Consola.ingresarXString("el Nuevo Nombre"));
                    break;

                case 2:
                    compradorRepository.cambioApellido(comprador,Consola.ingresarXString("el Nuevo Apellido"));
                    break;

                case 3:
                    compradorRepository.cambioDni(comprador, Consola.ingresarXInteger("el Nuevo DNI"));
                    break;

                case 4:
                    compradorRepository.cambioEmail(comprador, compradorView.ingresoEmail());
                    break;

                case 5:
                    System.out.println("Saliste de la modificacion Comprador.");
                    break;

                default:
                    System.out.println("Opcion Invalida, Vuelva a intentarlo.");
                    break;
            }
        }while (opt != 5);
    }
    public void show()
    {
        Integer id = Consola.ingresarXInteger("el ID del Comprador Buscado");
        Comprador comprador = compradorRepository.find(id);
        if (comprador != null)
            compradorView.muestroUnComprador(comprador);
    }
    public Comprador find (Integer id){
        return compradorRepository.find(id);
    }
    public void verHisorial(){
        compradorView.muestroCompradores(compradorRepository.getListaCompradores());
    }
}