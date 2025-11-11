import java.util.List;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        ClienteHttp clienteHttp = new ClienteHttp();
        GestorMonedas gestorMonedas = new GestorMonedas(clienteHttp); // Instanciamos el gestor de monedas
        Scanner teclado = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("""
                \n**************************
                   CONVERSOR DE MONEDAS
                **************************
                
                Monedas disponibles:
                """);

            for (Moneda moneda : gestorMonedas.getMonedas()) { // Usamos el for-each aquí
                System.out.println(moneda.codigo() + " - " + moneda.nombre());
            }

            System.out.println("""
                \nMenú de Opciones:
                1. Realizar conversión de moneda (lista)
                2. Añadir nueva moneda a la lista
                3. Salir
                """);
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        // Pasamos el gestor al método de conversión para que pueda mostrar las opciones
                        realizarConversion(teclado, clienteHttp, gestorMonedas);
                        break;
                    case 2:
                        // Pasamos el gestor al método de añadir moneda
                        agregarNuevaMoneda(teclado, gestorMonedas);
                        break;
                    case 3:
                        System.out.println("Saliendo del conversor. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido para la opción.");
                opcion = 0; // Para mantener el bucle
            }

        } while (opcion != 3);

        teclado.close();
    }

    // Método privado para manejar la lógica de la conversión (Opción 1 del menú)
    // Recibe el GestorMonedas como parámetro
    private static void realizarConversion(Scanner teclado, ClienteHttp clienteHttp, GestorMonedas gestor) {

        System.out.println("\n--- INICIAR CONVERSIÓN ---");
        gestor.mostrarMonedas(); // Usamos el método del gestor para mostrar la lista

        String monedaOrigen;
        String monedaDestino;
        Double importe;

        System.out.print("\nINGRESE CÓDIGO MONEDA ORIGEN: ");
        monedaOrigen = teclado.nextLine().toUpperCase();

        boolean origenValido = gestor.getMonedas().stream()
                        .anyMatch(m -> m.codigo().equalsIgnoreCase(monedaOrigen));

        if (!origenValido) {
            System.out.println("El código de la moneda de origen " + monedaOrigen + " no está en la lista local");
            return;
        }

        System.out.print("INGRESE CÓDIGO MONEDA DESTINO: ");
        monedaDestino = teclado.nextLine().toUpperCase();

        boolean destinoValido = gestor.getMonedas().stream()
                        .anyMatch(m -> m.codigo().equalsIgnoreCase(monedaDestino));

        if (!destinoValido) {
            System.out.println("El código de la moneda de destino " + monedaDestino + " no está en la lista local");
            return;
        }

        System.out.print("Ingrese el IMPORTE que desea convertir: ");
        try {
            importe = Double.parseDouble(teclado.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error: El monto ingresado no es un número válido.");
            return;
        }

        System.out.println("\nRealizando la consulta a la API...");
        IntercambioMoneda resultado = clienteHttp.obtenerConversion(monedaOrigen, monedaDestino, importe);

        if (resultado != null && "success".equals(resultado.resultado())) {
            System.out.printf(
                    """
                        \n********************************
                       RESULTADO DE LA CONVERSIÓN
                    ********************************
                       %.2f %s equivale a %.2f %s
                       (Tasa de conversión: %.4f)
                    """,
                    importe,
                    monedaOrigen,
                    resultado.resultadoConversion(),
                    monedaDestino,
                    resultado.tasaConversion()
            );
        } else if (resultado != null) {
            System.out.println("\nError de API: " + resultado.resultado() + ". Verifique los códigos de moneda o su API Key.");
        } else {
            System.out.println("\nNo se pudo obtener el resultado de la conversión. Revise su conexión o los códigos.");
        }
    }

    private static void agregarNuevaMoneda(Scanner teclado, GestorMonedas gestor) {
        System.out.println("\n--- AÑADIR NUEVA MONEDA ---");
        System.out.println("\nPor favor, verifique el código a ingresar en el siguiente listado:");

        ClienteHttp cliente = new ClienteHttp();

        // Obtener los datos desde la API
        MonedaApi monedaApi = cliente.obtenerDatos();

        if (monedaApi == null || !"success".equals(monedaApi.resultado())) {
            System.out.println("Error al obtener los datos de la API.");
            return;
        }

        // Convertir los pares [codigo, descripcion] en objetos Moneda
        List<Moneda> listaMonedas = monedaApi.codigosSoportados().stream()
                .map(item -> new Moneda(item.get(0), item.get(1)))
                .toList();

        for (Moneda moneda : listaMonedas) {
            System.out.println(moneda.codigo() + " - " + moneda.nombre());
        }

        System.out.print("\nIngrese el código de la nueva moneda (ej: ARS): ");
        String codigo = teclado.nextLine().toUpperCase();

        // Verificar si el código existe en la lista de la API
        boolean existe = listaMonedas.stream()
                .anyMatch(m -> m.codigo().equalsIgnoreCase(codigo));

        if (!existe) {
            System.out.println("Error: El código de moneda " + codigo + " no es válido según la API.");
            return;
        }

        // Obtener el nombre de la moneda directamente de la API
        String nombre = listaMonedas.stream()
                .filter(m -> m.codigo().equalsIgnoreCase(codigo))
                .findFirst()
                .map(Moneda::nombre)
                .orElse("Desconocido");

        gestor.agregarMoneda(codigo, nombre);
        System.out.println("Moneda " + codigo + " (" + nombre + ") añadida exitosamente a la lista local.");
    }
}

