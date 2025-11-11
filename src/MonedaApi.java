import com.google.gson.annotations.SerializedName;

import java.util.List;

public record MonedaApi(

        @SerializedName("result")
        String resultado,

        @SerializedName("supported_codes")
        List<List<String>> codigosSoportados
) {
}


