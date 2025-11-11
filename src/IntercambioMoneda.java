import com.google.gson.annotations.SerializedName;

public record IntercambioMoneda(

        @SerializedName("result")
        String resultado,

        @SerializedName("base_code")
        String monedaOrigen,

        @SerializedName("target_code")
        String monedaDestino,

        @SerializedName("conversion_rate")
        Double tasaConversion,

        @SerializedName("conversion_result")
        Double resultadoConversion
) {
}
