package com.framallo90.Venta.Controller;
import com.framallo90.Automovil.Controller.AutomovilController;
import com.framallo90.Automovil.Model.Entity.Automovil;
import com.framallo90.Comprador.Controller.CompradorController;
import com.framallo90.Comprador.Model.Entity.Comprador;
import com.framallo90.Empleados.Controller.EmpleadosController;
import com.framallo90.Empleados.Model.Entity.Empleados;
import com.framallo90.Excepciones.InvalidIdNotFound;
import com.framallo90.MetodoDePago.Controller.MetodoController;
import com.framallo90.MetodoDePago.Model.Entity.MetodoDePago;
import com.framallo90.Venta.Model.Entity.Venta;
import com.framallo90.Venta.Model.Repository.VentaRepository;
import com.framallo90.Venta.View.VentaView;
import com.framallo90.consola.Consola;

import java.time.DateTimeException;
import java.time.LocalDate;
public class VentaController {
    private EmpleadosController empleadosController;
    private CompradorController compradorController;
    private AutomovilController automovilController;
    private MetodoController metodoController;
    private VentaView ventaView;
    private VentaRepository ventaRepository;
    public VentaController(EmpleadosController empleadosController, CompradorController compradorController, AutomovilController automovilController, MetodoController metodoController, VentaView ventaView, VentaRepository ventaRepository) {
        this.empleadosController = empleadosController;
        this.compradorController = compradorController;
        this.automovilController = automovilController;
        this.metodoController = metodoController;
        this.ventaView = ventaView;
        this.ventaRepository = ventaRepository;
    }

    //documentar
    public void add() throws InvalidIdNotFound {
        empleadosController.mostrarHistorial(); /// para facilitar la eleccion
        Empleados empleados = this.empleadosController.find(Consola.ingresarXInteger("el ID del Empleado Vendedor"));
        if (empleados == null) {
            throw new InvalidIdNotFound("El Empleado NO esta registrado.");
        }
        compradorController.verHisorial();
        Comprador comprador = this.compradorController.find(Consola.ingresarXInteger("el ID del Comprador actual"));
        if (comprador == null) throw new InvalidIdNotFound("El Comprador NO esta registrado.");

        automovilController.mostrarAutomovilesEnStock();
        Integer id = Consola.ingresarXInteger("el ID del Automovil en Stock");
        Automovil automovil = this.automovilController.find(id);

        if (automovil == null)throw new InvalidIdNotFound("El Automovil NO esta registrado.");
        LocalDate fecha = LocalDate.now();
        MetodoDePago metodoDePago = this.metodoController.cargarMDP(automovil.getPrecio());
        Venta venta = this.ventaView.generarVenta(empleados,comprador,automovil,fecha,metodoDePago);
        this.ventaRepository.add(venta);
        this.automovilController.borrarAutomovilEnStockPorId(id);

    }
    public void show()  {
        Venta buscar = this.ventaRepository.find(Consola.ingresarXInteger("el ID de la Venta"));
        this.ventaView.mostrarVenta(buscar);
    }
    public void update() throws InvalidIdNotFound{
        Venta buscar = this.ventaRepository.find(Consola.ingresarXInteger("el ID de la Venta"));
        if (buscar == null )throw new InvalidIdNotFound("NO se ha encontrado la Venta.");
        modifVenta(buscar);
    }
    public void remover() throws InvalidIdNotFound{
        Venta buscar = this.ventaRepository.find(Consola.ingresarXInteger("el ID de la Venta"));
        if (buscar == null )throw new InvalidIdNotFound("NO se ha encontrado la Venta.");
        try {
            this.ventaRepository.remove(buscar.getIdVenta());
        } catch (Exception e) {
            Consola.soutString(e.getMessage());
        }
    }

    public void modifVenta(Venta venta){
        while (true){
            this.ventaView.printMenuModifVenta();
            switch (Consola.ingresarXInteger("una opcion de Venta")){
                case 1: //empleado
                    empleadosController.modificarEmpleado(venta.getEmpleados());
                    break;
                case 2: //comprador
                    compradorController.update(venta.getComprador());
                    break;
                case 3: //automovil
                    try {
                        venta.setAutomovil(automovilController.cambiarCoche(venta.getAutomovil()));
                    } catch (InvalidIdNotFound e) {
                        Consola.soutString(e.getMessage());
                    }
                    break;
                case 4: //mtodo de pago
                    metodoController.updateMDP(venta.getTransaccion(), venta.getAutomovil().getPrecio());
                    break;
                case 0: //salir
                    return;
                default:
                    Consola.soutString("Saliste de la modificacion de Venta");
                    break;
            }
        }
    }
    public void menuVentas(){
        int eleccion;
        do{
            this.ventaView.printMenuVentas();
            eleccion = Consola.ingresarXInteger("una opcion del Menu Venta");
            switch(eleccion){
                case 0: //salir
                    Consola.soutString("Saliste del Menu Venta.");
                    break;
                case 1://agregar venta
                    try {
                        this.add();
                        break;
                    } catch (InvalidIdNotFound e) {
                        Consola.soutString(e.getMessage());
                    }
                    break;
                case 2://mostrar
                    this.ventaView.mostrarHistorial(this.ventaRepository.getMap());
                    if (!this.ventaRepository.isEmpty())
                        this.show();
                    break;
                case 3: //modificar una vnta
                    try {
                        this.ventaView.mostrarHistorial(this.ventaRepository.getMap());
                        if (!this.ventaRepository.isEmpty())
                            this.update();
                        break;
                    } catch (InvalidIdNotFound e) {
                        Consola.soutString(e.getMessage());
                    }
                    break;
                case 4://remover
                    try {
                        this.ventaView.mostrarHistorial(this.ventaRepository.getMap());
                        if (!this.ventaRepository.isEmpty())
                            this.remover();
                        break;
                    } catch (InvalidIdNotFound e) {
                        Consola.soutString(e.getMessage());
                    }
                    break;
                case 5: //mostrar todas
                    this.ventaView.mostrarHistorial(this.ventaRepository.getMap());
                    break;
                default:
                    Consola.soutString("Ingresar una opcion valida!.");
                    break;
            }

        }
        while (eleccion!=0);

    }
}