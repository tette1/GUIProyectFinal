package com.framallo90.Automovil.Controller;

import com.framallo90.Automovil.API.ApiAutomovilService;
import com.framallo90.Automovil.Model.Entity.Automovil;
import com.framallo90.Automovil.Model.Repository.AutomovilRepository;
import com.framallo90.Automovil.View.AutomovilView;
import com.framallo90.Excepciones.InvalidIdNotFound;
import com.framallo90.consola.Consola;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class AutomovilController {
    private final AutomovilRepository automovilRepository;
    private final AutomovilView automovilView;
    private final ApiAutomovilService apiAutomovilService;

    public AutomovilController(AutomovilRepository automovilRepository, AutomovilView automovilView) {
        this.automovilRepository = automovilRepository;
        this.automovilView = automovilView;
        this.apiAutomovilService = new ApiAutomovilService();
    }

    // Método para seleccionar una marca usando la API.
    private Map.Entry<Integer, String> seleccionarMarca() {
        try {
            Map<Integer, String> marcas = apiAutomovilService.obtenerMarcas();
            System.out.println("Seleccione una marca:");
            for (Map.Entry<Integer, String> entry : marcas.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            int marcaId = Consola.ingresarXInteger("el ID de la Marca");
            if (!marcas.containsKey(marcaId)) {
                System.out.println("ID de marca Invalido!. Intente nuevamente.");
                return seleccionarMarca();
            }
            String marcaNombre = marcas.get(marcaId);
            return Map.entry(marcaId, marcaNombre);
        } catch (IOException e) {
            System.out.println("Error al obtener las marcas: " + e.getMessage());
            return null;
        }
    }

    // Método para seleccionar un modelo usando la API.
    private Map.Entry<Integer, String> seleccionarModelo(int marcaId) {
        try {
            Map<Integer, String> modelos = apiAutomovilService.obtenerModelos(marcaId);
            System.out.println("Seleccione un Modelo:");
            for (Map.Entry<Integer, String> entry : modelos.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            int modeloId = Consola.ingresarXInteger("el ID del Modelo");
            if (!modelos.containsKey(modeloId)) {
                System.out.println("ID de modelo Invalido. Intente nuevamente.");
                return seleccionarModelo(marcaId);
            }
            String modeloNombre = modelos.get(modeloId);
            return Map.entry(modeloId, modeloNombre);
        } catch (IOException e) {
            System.out.println("Error al obtener los modelos: " + e.getMessage());
            return null;
        }
    }

    // Método para seleccionar un año usando la API.
    private String seleccionarAno(int marcaId, int modeloId) {
        try {
            Map<String, String> anos = apiAutomovilService.obtenerAnos(marcaId, modeloId);
            System.out.println("Seleccione un año:");
            for (Map.Entry<String, String> entry : anos.entrySet()) {
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
            while (true) {
                String anoId = Consola.ingresarXStringSimple("ID del año");
                if (anos.containsKey(anoId)) {
                    return anoId;
                } else {
                    System.out.println("Ingresar un dato válido!.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error al obtener los años: " + e.getMessage());
            return null;
        }
    }

    // Método para obtener el precio usando la API.
    private Double obtenerPrecio(int marcaId, int modeloId, String anoId) {
        try {
            return apiAutomovilService.obtenerPrecio(marcaId, modeloId, anoId);
        } catch (IOException e) {
            System.out.println("Error al obtener el precio: " + e.getMessage());
            return null;
        }
    }

    // Método para agregar un automóvil al stock utilizando la API.
    public void agregarAutomovilAlStock() {
        Map.Entry<Integer, String> marca = seleccionarMarca();
        if (marca == null) return;

        Map.Entry<Integer, String> modelo = seleccionarModelo(marca.getKey());
        if (modelo == null) return;

        String anoId = seleccionarAno(marca.getKey(), modelo.getKey());
        if (anoId == null) return;

        Double precio = obtenerPrecio(marca.getKey(), modelo.getKey(), anoId);
        if (precio == null) return;

        String patente = automovilView.ingresoPatente();
        int anio = Integer.parseInt(anoId.split("-")[0]);

        Automovil nuevo = new Automovil(marca.getValue(), modelo.getValue(), precio, patente, anio);
        automovilRepository.add(nuevo);
    }

    public void borrarAutomovilEnStock() {
        try {
            automovilRepository.remove(Consola.ingresarXInteger("el ID"));
        } catch (InvalidIdNotFound e) {
            System.out.println(e.getMessage());
        }
    }
    public void borrarAutomovilEnStockPorId(Integer id){
        if (!this.isRepoEmpty())
            try {
                automovilRepository.remove(id);
            }catch (InvalidIdNotFound e)
            {
                System.out.println(e.getMessage());
            }
        else Consola.soutString("Aún no se han registrado Automoviles.");
    }

    public void modificar() {
        try {
            automovilRepository.update(Consola.ingresarXInteger("el ID"));
        } catch (InvalidIdNotFound e) {
            System.out.println(e.getMessage());
        }
    }

    public Automovil cambiarCoche(Automovil automovil) throws InvalidIdNotFound {
        mostrarAutomovilesEnStock();
        Automovil retorno = automovilRepository.find(Consola.ingresarXInteger("el ID del Automovil"));
        if (retorno == null) {
            Consola.soutString("NO se ha encontrado un Automovil con el ID ingresado.");
            return automovil;
        }
        this.automovilRepository.remove(retorno.getId());
        this.automovilRepository.add(automovil);
        return retorno;
    }

    public void mostrarAutomovilesEnStock() {
        try {
            automovilView.mostrarAutomoviles(automovilRepository.getAutomovilList());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Automovil find(Integer id) {
        return automovilRepository.find(id);
    }

    public void buscarAutomovilesXFiltro() {
            Predicate<Automovil>[] arrayCondiciones = new Predicate[5];
            arrayCondiciones[0] = null;arrayCondiciones[1] = null;
            arrayCondiciones[2] = null;arrayCondiciones[3] = null;
            arrayCondiciones[4] = null;

            String[] arrayTagsMostrar = new String[5];
            //tendremos un maximo de 5 filtros unicos

            int cont = 0;
            int opc = -1;
            mostrarAutomovilesEnStock();
            do{
                System.out.println("-> LISTA DE AUTOMOVILES:" +
                        "\n1. Filtrar por Marca" +
                        "\n2. Filtrar por Modelo" +
                        "\n3. Filtrar por Año" +
                        "\n4. Establecer Precio máximo" +
                        "\n5. Establecer Precio mínimo"
                );
                if(cont>0){System.out.println("6. Quitar filtro");}
                System.out.println();
                System.out.println("0. Volver");

                opc = Consola.ingresarXInteger("un Filtro");

                //se agrega/cambia/saca uno de los filtros
                switch (opc){
                    case 1:
                        String marca = automovilView.ingresoMarca();
                        arrayCondiciones[0] = m -> m.getMarca().toLowerCase().contains(marca.toLowerCase());
                        arrayTagsMostrar[0] = "Marca " + marca + " | ";
                        break;
                    case 2:
                        String modelo = automovilView.ingresoModelo();
                        arrayCondiciones[1] = m -> m.getModelo().toLowerCase().contains(modelo.toLowerCase());
                        arrayTagsMostrar[1] = "Modelo " + modelo + " | ";
                        break;
                    case 3:
                        Integer anio = automovilView.ingresoAnio();
                        arrayCondiciones[2] = (a -> a.getAnio().equals(anio));
                        arrayTagsMostrar[2] = "Anio " + anio.toString() + " | ";
                        break;
                    case 4:
                        Double max = Consola.ingresarXdouble("máximo");
                        arrayCondiciones[3] = p-> p.getPrecio() <= max;
                        arrayTagsMostrar[3] = "Precio hasta $" + max + " | ";
                        break;
                    case 5:
                        Double min = Consola.ingresarXdouble("mínimo");
                        arrayCondiciones[4] = p -> p.getPrecio() >= min;
                        arrayTagsMostrar[4] = "Precio desde $" + min + " | ";
                        break;
                    case 6:
                        if(cont>0){
                            System.out.println(
                                    "\n1 - Filtro marca" +
                                    "\n2 - Filtro modelo" +
                                    "\n3 - Filtro año" +
                                    "\n4 - Filtro precio máximo" +
                                    "\n5 - Filtro precio mínimo" +
                                    "\n0 - Volver"
                            );
                            Integer sacar = Consola.ingresarXInteger("una opcion");
                            if(6 > sacar && sacar>0){
                                arrayCondiciones[sacar-1] = null;
                            }
                        }else{
                            System.out.println("Opción NO reconocida");
                        }
                        break;
                    case 0:
                        //se sale
                        break;
                    default:
                        opc = -1;
                        System.out.println("Opción NO reconocida");
                        break;
                }


                //verifico el array de filtros y si tengo los paso a una lista para usarlos
                List<Predicate<Automovil>> listaFiltros = new ArrayList<>();
                for(Predicate<Automovil> p:arrayCondiciones){
                    if(p != null){
                        listaFiltros.add(p);
                    }
                }

                cont = listaFiltros.size();

                if(opc != 0){
                    if(cont >0){///si se usan filtros
                        ///muestra los filtros que se usan
                        System.out.println("Filtros:");
                        System.out.printf("\033[36m | ");
                        for (int i = 0;i<5;i++){
                            if(arrayCondiciones[i] != null){
                                System.out.printf(arrayTagsMostrar[i]);
                            }
                        }
                        System.out.println("\u001B[0m");



                        //procesa los datos y muestra si hay coincidencias
                        Integer coincidencias = 0;
                        if (cont == 1) {
                            coincidencias = (int) this.automovilRepository.getAutomovilList().stream()
                                    .filter(listaFiltros.get(0))
                                    .peek(System.out::println)
                                    .count();

                        } else if( cont == 2){
                            coincidencias = (int) this.automovilRepository.getAutomovilList().stream()
                                    .filter(listaFiltros.get(0))
                                    .filter(listaFiltros.get(1))
                                    .peek(System.out::println)
                                    .count();
                        } else if(cont == 3) {
                            coincidencias = (int) this.automovilRepository.getAutomovilList().stream()
                                    .filter(listaFiltros.get(0))
                                    .filter(listaFiltros.get(1))
                                    .filter(listaFiltros.get(2))
                                    .peek(System.out::println)
                                    .count();

                        } else if(cont == 4) {
                            coincidencias = (int) this.automovilRepository.getAutomovilList().stream()
                                    .filter(listaFiltros.get(0))
                                    .filter(listaFiltros.get(1))
                                    .filter(listaFiltros.get(2))
                                    .filter(listaFiltros.get(3))
                                    .peek(System.out::println)
                                    .count();
                        } else if (cont == 5){
                            coincidencias = (int) this.automovilRepository.getAutomovilList().stream()
                                    .filter(arrayCondiciones[0])
                                    .filter(arrayCondiciones[1])
                                    .filter(arrayCondiciones[2])
                                    .filter(arrayCondiciones[3])
                                    .filter(arrayCondiciones[4])
                                    .peek(System.out::println)
                                    .count();
                        }


                        ///avisa que no hay coincidencias
                        if(coincidencias == 0){
                            System.out.println("NO hay Automoviles que coincidan con los Filtros");
                        }


                    }else{
                        ///muestra la lista resultante sin filtros
                        if(this.automovilRepository.isEmpty()){
                            System.out.println("NO hay Automoviles");
                        }else{
                            this.automovilRepository.getAutomovilList().forEach(System.out::println);
                        }
                    }
                }

            }while(opc != 0);
    }

    public void menuAutomovilAdmin() {
        int eleccion;
        while (true) {
            automovilView.printMenuAutomovilAdmin();
            eleccion = Consola.ingresarXInteger("una opcion del Menu Automoviles");
            switch (eleccion) {
                case 0: // salir
                    return;
                case 1: // agregar
                    agregarAutomovilAlStock();
                    break;
                case 2: // remover
                    mostrarAutomovilesEnStock();
                    borrarAutomovilEnStock();
                    break;
                    /*
                case 3: // modificar
                    mostrarAutomovilesEnStock();
                    modificar();
                    break;
                     */
                case 3: // mostrar
                    mostrarAutomovilesEnStock();
                    Automovil find = find(Consola.ingresarXInteger("el ID del Automovil"));
                    if (find == null) {
                        Consola.soutString("NO se ha encontrado un Automovil con el ID ingresado.");
                    } else {
                        Consola.soutString(find.toString());
                    }
                    break;
                case 4: // ver lista filtrada
                    buscarAutomovilesXFiltro();
                    break;
                default: // Opción no reconocida
                    Consola.soutString("El dato ingresado no es válido!. Reintentar");
                    break;
            }
        }
    }

    public void menuAutomovilVendedor() {
        int eleccion;
        while (true) {
            automovilView.printMenuAutomovilVendedor();
            eleccion = Consola.ingresarXInteger("una opcion del Menu");
            switch (eleccion) {
                case 0: // salir
                    return;
                case 1: // mostrar
                    mostrarAutomovilesEnStock();
                    Automovil find = find(Consola.ingresarXInteger("el ID del Automovil"));
                    if (find != null) Consola.soutString(find.toString());
                    break;
                case 2: // ver stock
                    mostrarAutomovilesEnStock();
                    break;
                case 3: // ver lista filtrada
                    buscarAutomovilesXFiltro();
                    break;
                default: // Opción no reconocida
                    Consola.soutString("El dato ingresado no es válido!. Reintentar.");
                    break;
            }
        }
    }

    public boolean isRepoEmpty(){
        return this.automovilRepository.isEmpty();
    }
}
