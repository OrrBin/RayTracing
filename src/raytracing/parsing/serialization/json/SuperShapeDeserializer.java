package raytracing.parsing.serialization.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import lombok.AllArgsConstructor;
import raytracing.geometry.Polygon3D;
import raytracing.geometry.SuperShape;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class SuperShapeDeserializer implements JsonDeserializer<SuperShape> {

    private final Vector3Factory vector3Factory;

    @Override
    public SuperShape deserialize(final JsonElement json,
                                  final Type typeOfT,
                                  final JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final String type = jsonObject.get("type").getAsString();
        final SuperShapeType shapeType = SuperShapeType.valueOf(type);

        switch (shapeType) {
            case POLYGON: {
                final int material = getMaterial(jsonObject);
                List<Vector3> vertices = new ArrayList<>();
                final JsonArray verticesJsonArray = jsonObject.get("vertices").getAsJsonArray();
                for (final JsonElement vertexJsonElement : verticesJsonArray) {
                    vertices.add(CustomVector3Deserializers.getVector3(vertexJsonElement.getAsJsonArray(), vector3Factory));
                }

                return new Polygon3D(vertices, material, vector3Factory);
            }

            default:
                throw new IllegalArgumentException("Unknown SuperShapeType " +  type);
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
