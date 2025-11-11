import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ClienteHttp {

    // Creando cliente HTTP (final porque no se reasigna)
    private final HttpClient cliente = HttpClient.newHttpClient();

    //private static final String API_KEY = "563a591f4ce481726fed1389";

    private final Gson gson = new Gson();

    private final String API_KEY;

    public ClienteHttp() {
        Properties propiedades = new Properties();
        try (FileInputStream archivo = new FileInputStream("src/config.properties")) {
            propiedades.load(archivo);
            API_KEY = propiedades.getProperty("APY_KEY");
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la API_KEY desde config.properties", e);
        }
    }

    public IntercambioMoneda obtenerConversion(String monedaOrigen, String monedaDestino, Double importe) {

        String url = "https://v6.exchangerate-api.com/v6/" +
                API_KEY + "/pair/" +
                monedaOrigen + "/" +
                monedaDestino + "/" +
                importe;

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            // Enviando solicitud y recibiendo respuesta
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(respuesta.body(), IntercambioMoneda.class);

        } catch (IOException | InterruptedException e) {
            System.err.println("Error al realizar la solicitud HTTP: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<String> obtenerCodigosSoportados() {
        String url = "https://v6.exchangerate-api.com/v6/" +
                API_KEY + "/codes";

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());

            MonedaApi apiRespuesta = gson.fromJson(respuesta.body(), MonedaApi.class);

            if (apiRespuesta != null && "success".equals(apiRespuesta.resultado())) {
                return apiRespuesta.codigosSoportados().stream()
                        .map(list -> list.get(0))
                        .collect(Collectors.toList());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al obtener códigos soportados: " + e.getMessage());
        }
        return Collections.emptyList();
    }

    public MonedaApi obtenerDatos() {
        String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/codes";

        HttpRequest solicitud = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> respuesta = cliente.send(solicitud, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(respuesta.body(), MonedaApi.class);
        } catch (IOException | InterruptedException e) {
            System.err.println("Error al obtener los datos de la API: " + e.getMessage());
            return null;
        }
    }
}
