package raytracing.parsing.serialization.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
            final JsonObject jsonObject = json.getAsJsonObject();

            return vector3Factory.getVector3(
                    jsonObject.get("x").getAsDouble(),
                    jsonObject.get("y").getAsDouble(),
                    jsonObject.get("z").getAsDouble());
    }
}
