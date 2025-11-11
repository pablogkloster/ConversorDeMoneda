import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GestorMonedas {

    private final List<Moneda> monedasDisponibles = new ArrayList<>();
    private static final String Nombre_Archivo = "monedas_disponibles.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ClienteHttp cliente;

    public GestorMonedas(ClienteHttp cliente) {
        this.cliente = cliente;

        if(!cargarDesdeArchivo()) {
            monedasDisponibles.add(new Moneda("ARS", "Peso argentino"));
            monedasDisponibles.add(new Moneda("BOB", "Boliviano boliviano"));
            monedasDisponibles.add(new Moneda("BRL", "Real brasileño"));
            monedasDisponibles.add(new Moneda("CLP", "Peso chileno"));
            monedasDisponibles.add(new Moneda("COP", "Peso colombiano"));
            monedasDisponibles.add(new Moneda("USD", "Dólar estadounidense"));
            guardarEnArchivo();
        }
    }

    public boolean esCodigoValidoApi(String codigo) {
        List<String> codigosSoportados = cliente.obtenerCodigosSoportados();
        return codigosSoportados.contains(codigo.toUpperCase());
    }

    public void agregarMoneda (String codigo, String nombre) {
        if (esCodigoValidoApi(codigo)) {
            monedasDisponibles.add(new Moneda(codigo, nombre));
            guardarEnArchivo();
            System.out.println("Moneda " + codigo + " - " + nombre + " añadida exitosamente");
        } else {
            System.out.println("Error: El código de moneda " + codigo + " no es válido");
        }
    }

    public List<Moneda> getMonedas() {
        return monedasDisponibles;
    }

    public void mostrarMonedas() {
        System.out.println("""
                Códigos disponibles:
                -----------------------------
                """);
        for (Moneda moneda : monedasDisponibles) { // Usamos el for-each aquí
            System.out.println(moneda.codigo() + " - " + moneda.nombre());
        }
        System.out.println("-----------------------------");
    }

    public boolean existeCodigo(String codigo) {
        return monedasDisponibles.stream().anyMatch(m -> m.codigo().equals(codigo));
    }

    private boolean cargarDesdeArchivo() {
        try (Reader reader = new FileReader(Nombre_Archivo)) {
            Type tipoListaMonedas = new TypeToken<List<Moneda>>(){}.getType();
            List<Moneda> monedasCargadas = gson.fromJson(reader, tipoListaMonedas);
            if(monedasCargadas != null) {
                monedasDisponibles.clear();
                monedasDisponibles.addAll(monedasCargadas);
                System.out.println("Monedas cargadas exitosamente");
                return true;
            }
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            System.err.println("Error al cargar monedas desde archivo Json: " + e.getMessage());
        }
        return false;
    }

    private void guardarEnArchivo() {
        try (Writer writer = new FileWriter(Nombre_Archivo)) {
            monedasDisponibles.sort(Comparator.comparing(Moneda::codigo));
            gson.toJson(monedasDisponibles, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar monedas en el archivo json:" + e.getMessage());
        }
    }


}
