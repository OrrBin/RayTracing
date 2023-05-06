package raytracing.parsing.serialization.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.AllArgsConstructor;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.lang.reflect.Type;

@AllArgsConstructor
public class CustomVector3Deserializers implements JsonDeserializer<Vector3> {
        private final Vector3Factory vector3Factory;

        @Override
        public Vector3 deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            return getVector3(json, vector3Factory);
        }

    public static Vector3 getVector3(final JsonElement json, final Vector3Factory vector3Factory) {
        final JsonArray jsonArray = json.getAsJsonArray();

        return vector3Factory.getVector3(
                jsonArray.get(0).getAsDouble(),
                jsonArray.get(1).getAsDouble(),
                jsonArray.get(2).getAsDouble());
    }
}
