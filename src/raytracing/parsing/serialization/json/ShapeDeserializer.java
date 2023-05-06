package raytracing.parsing.serialization.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.AllArgsConstructor;
import raytracing.geometry.Plane;
import raytracing.geometry.Shape;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.lang.reflect.Type;

@AllArgsConstructor
public class ShapeDeserializer implements JsonDeserializer<Shape> {

    private final Vector3Factory vector3Factory;

    @Override
    public Shape deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final String type = jsonObject.get("type").getAsString();
        final ShapeType shapeType = ShapeType.valueOf(type);

        switch (shapeType) {
            case PLANE: {
                final int material = getMaterial(jsonObject);
                final Vector3 normal = CustomVector3Deserializers.getVector3(jsonObject.getAsJsonArray("normal"), vector3Factory);
                final double offset = jsonObject.get("offset").getAsDouble();
                return new Plane(normal, offset, material);
            }
            default:
                throw new IllegalArgumentException("Unknown SuperShapeType " + type);
        }
    }

    private Vector3 getVector3(final JsonObject normalJsonObject) {
        return vector3Factory.getVector3(
                normalJsonObject.get("x").getAsDouble(),
                normalJsonObject.get("y").getAsDouble(),
                normalJsonObject.get("z").getAsDouble()
        );
    }

    private int getMaterial(final JsonObject jsonObject) {
        return jsonObject.get("material").getAsInt();
    }

}
