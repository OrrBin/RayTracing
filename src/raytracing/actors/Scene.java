package raytracing.actors;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import raytracing.actors.graph.GraphNode;
import raytracing.actors.graph.SceneNode;
import raytracing.actors.graph.ShapeNode;
import raytracing.actors.graph.SuperShapeNode;
import raytracing.animation.StageManager;
import raytracing.geo.LatLon;
import raytracing.geometry.Shape;
import raytracing.geometry.Sphere;
import raytracing.geometry.SuperShape;
import raytracing.math.Vector3;
import raytracing.math.Vector3Factory;
import raytracing.math.util.IntersectionUtils;
import raytracing.util.ImageUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Getter
@Setter
public class Scene {

    private String sceneId;

    private int imageWidth, imageHeight;

    private double aspectRatio;

    private Settings settings;
    private Camera camera;
    private List<Light> lights;
    private List<Shape> shapes;
    private SceneNode sceneNode;
    private List<Material> materials;

    private StageManager stageManager;
    private Vector3Factory vector3Factory;
    private ImageUtils imageUtils;

    public Scene(final String sceneId, final int width, final int height,
                 final StageManager stageManager,
                 final ImageUtils imageUtils,
                 final Vector3Factory vector3Factory) {
        this.sceneId = sceneId;
        this.imageHeight = height;
        this.imageWidth = width;
        this.aspectRatio = ((double) this.imageWidth) / this.imageHeight;

        this.materials = new ArrayList<>();
        this.shapes = new ArrayList<>();
        this.sceneNode = new SceneNode(this);
        this.lights = new ArrayList<>();
        this.settings = new Settings(imageUtils);

        this.stageManager = stageManager;
        this.imageUtils = imageUtils;
        this.vector3Factory = vector3Factory;
    }

    public Ray[] getSuperSamplingRays(int top, int left) {
        int N = Math.max(1, settings.getSuperSamplingLevel());
        Ray[] rays = new Ray[N * N];

        Random rnd = new Random();

        double mult = 1.0;
        double subRectWidth = mult / N;
        for (int i = 0; i < N; i++) {
            double yMin = (subRectWidth * i) + left;
            for (int j = 0; j < N; j++) {
                double xMin = (subRectWidth * j) + top;

                double xOffset = xMin + rnd.nextDouble() * subRectWidth;
                double yOffset = yMin + rnd.nextDouble() * subRectWidth;
                rays[i * N + j] = getRay(xOffset, yOffset);
            }
        }
        return rays;
    }

    public Ray getRay(double i, double j) {
        Vector3 forward = this.camera.getForward();
        Vector3 right = this.camera.getRight();
        Vector3 top = this.camera.getTop();
        double d = this.camera.getScreenDistance();
        double screenWidth = this.camera.getScreenWidth();
        double screenHeight = screenWidth / aspectRatio;

        Vector3 p0 = this.camera.getPosition();

        Vector3 p1 = p0.cpy().addInPlace(forward.multiply(d)).addInPlace(right.multiply(-screenWidth / 2))
                .addInPlace(top.multiply(screenHeight / 2));


        double leftOff = j - this.imageWidth / 2.0;
        double topOff = i - this.imageHeight / 2.0;

        double leftPixel = (leftOff / this.imageWidth + 0.5) * screenWidth;
        double topPixel = (topOff / this.imageHeight + 0.5) * screenHeight;

        Vector3 rightScreenVec = right.multiply(leftPixel);
        Vector3 topScreenVec = top.multiply(-topPixel);

        Vector3 p = p1.cpy().addInPlace(rightScreenVec).addInPlace(topScreenVec);

        Vector3 direction = p0.connectingVector(p).normalize();

        return new Ray(p0, direction);
    }

    /**
     * calculates diffuse and sepcular colors
     */
    Vector3 diffuseAndSpecColor(IntersectionData inter, final Vector3 normal, Ray ray, Material mat) {
        Vector3 diffLight = vector3Factory.getVector3(0, 0, 0);
        Vector3 specLight = vector3Factory.getVector3(0, 0, 0);

        final Vector3 v = ray.direction.multiply(-1).normalize();

        // Calculate each of the scenes light diffuse and specular color and sum up
        for (Light light : this.lights) {
            final Vector3 lightDirection = light.getPosition().connectingVector(inter.getIntersectionPointCpy()).normalize();
            final Vector3 reveresedLightDirection = lightDirection.multiply(-1);

            if (normal.dotProduct(reveresedLightDirection) < 0)
                continue;

            Ray[] rays = light.getRays(inter.getIntersectionPointCpy(), this.settings.getNumOfShadowRays());
            double shadow = 0;
            for (Ray subRay : rays) {
                List<Shape> intersections = blockingShapes(subRay, inter.getShape());
                double intersectionsSum = 0;
                for (Shape shape : intersections) {
                    intersectionsSum += 1 - this.materials.get(shape.getMaterial()).getTransparency();
                }
                shadow += Math.pow(1 - light.getShadowIntensity(), intersectionsSum);
            }

            double factor = normal.dotProduct(reveresedLightDirection) * shadow / rays.length;
            Vector3 added = light.getColor().multiply(factor);
            diffLight = diffLight.cpy().addInPlace(added);

            Vector3 reflectedLight = lightDirection.cpy()
                    .addInPlace(normal.multiply(-2 * lightDirection.dotProduct(normal))).normalize();

            double angleWithLightReflection = reflectedLight.dotProduct(v);

            if (angleWithLightReflection > 0) {
                specLight = specLight.cpy().addInPlace(light.getColor()
                        .multiply(Math.pow(angleWithLightReflection, mat.getPhongSpecularityCoefficient()) * shadow
                                * light.getSpecularIntensity() / rays.length));
            }

        }

        Vector3 shapeDiffuseColor = mat.getDiffuseColor();
        Vector3 shapeSpecColor = mat.getSpecularColor();

        //TODO : hardcoding testing world map on sphere
        if (inter.getShape() instanceof Sphere) {

            final Sphere sphere = (Sphere) inter.getShape();

            if (sphere.getImage() != null) {
                final LatLon latlon = sphere.getLatLon(inter.getIntersectionPointCpy());
                final int x = sphere.getProjection().lon2x(latlon.lon, sphere.getImage().getWidth());
                final int y = sphere.getProjection().lat2y(latlon.lat, sphere.getImage().getHeight());
                final Vector3 imagePixelColor = imageUtils.getPixelColor(sphere.getImage(), x ,y);

                if (imagePixelColor != null) {
                    shapeDiffuseColor = imagePixelColor;
                    shapeSpecColor = imagePixelColor;
                }
            }

        }

        diffLight = diffLight.multiply(shapeDiffuseColor);
        specLight = specLight.multiply(shapeSpecColor);

        return diffLight.addInPlace(specLight);
    }

    /**
     * calculates transparent colors
     */
    Vector3 transparencyColor(IntersectionData inter, Ray ray, Material mat, int nextRecDepth, int row, int col) {
        Vector3 transColor = vector3Factory.getVector3(0, 0, 0);
        if (mat.getTransparency() > 0) {
            Vector3 delta = ray.direction.multiply(0.0001);
            Ray trasRay = new Ray(inter.intersectionPoint.cpy().addInPlace(delta), ray.direction.cpy());
            transColor = calculateColor(trasRay, nextRecDepth, inter.getShape(), row, col);
        }

        return transColor;
    }

    /**
     * calculates reflection colors
     */
    Vector3 reflectionColor(IntersectionData inter, final Vector3 normal, Ray ray, Material mat, int nextRecDepth, int row, int col) {
        Vector3 dir = ray.direction.normalize();
        double dot = dir.dotProduct(normal);

        Vector3 reflectionVector = dir.cpy().addInPlace(normal.multiply(-2 * dot)).normalize();
        Ray reflectionRay = new Ray(inter.getIntersectionPointCpy(), reflectionVector);

        return calculateColor(reflectionRay, nextRecDepth, inter.getShape(), row, col).multiply(mat.getReflectionColor());
    }

    /**
     * Calculates closest shape that intersects with given ray. returns null if no
     * shape intersects the ray.
     */
    public IntersectionData calcIntersection(Ray ray, Shape excludeShape) {
        double tmpDistance;
        double minDistance = Double.POSITIVE_INFINITY;
        IntersectionData intersection = null;

        final List<Shape> shapesToCheck = getShapesToCheck(ray);

        for (Shape shape : shapesToCheck) {
            if (shape.equals(excludeShape))
                continue;
            Vector3 intersectionPoint = shape.intersection(ray);
            if (intersectionPoint == null)
                continue;

            if ((tmpDistance = ray.originPoint.distance(intersectionPoint)) < minDistance) {
                minDistance = tmpDistance;
                intersection = new IntersectionData(shape, intersectionPoint);
            }
        }

        return intersection;
    }

    private void calcIntersection(final Ray ray, final SuperShapeNode superShapeNode, final List<Shape> shapesToCheck) {
        final SuperShape superShape = superShapeNode.getValue();
        final double[][] bounds = superShape.getBounds();

        if(IntersectionUtils.intersectionWithBoundingBox(ray, bounds)) {
            for(GraphNode<?> childNode : superShapeNode.getChildren()) {
                if(childNode instanceof ShapeNode) {
                    shapesToCheck.add(((ShapeNode) childNode).getValue());
                } else {
                    SuperShapeNode childSuperShapeNode = (SuperShapeNode) childNode;
                    calcIntersection(ray, childSuperShapeNode, shapesToCheck);
                }
            }
        }
    }

    /**
     * Returns list of shapes that block this ray before hitting the target shape
     */
    public List<Shape> blockingShapes(Ray ray, Shape targetShape) {
        Map<Shape, Double> shapesMap = new HashMap<>();

        final List<Shape> shapesToCheck = getShapesToCheck(ray);

        double maxDistance = Double.MAX_VALUE;

        for (Shape shape : shapesToCheck) {
            final Vector3 interPoint = shape.intersection(ray);
            if (interPoint != null) {
                final IntersectionData inter = new IntersectionData(shape, interPoint);
                final double distance = ray.originPoint.connectingVector(inter.getIntersectionPointCpy()).norm();

                if (targetShape == shape) {
                    maxDistance = distance;
                    continue;
                }
                shapesMap.put(shape, distance);
            }
        }

        final List<Shape> result = new ArrayList<>();
        for (Shape shape : shapesMap.keySet()) {
            if (shapesMap.get(shape) < maxDistance) {
                result.add(shape);
            }
        }

        return result;

    }

    private List<Shape> getShapesToCheck(Ray ray) {
        List<Shape> shapesToCheck = new ArrayList<>();

        for(GraphNode<?> node : sceneNode.getChildren()){
            if(node instanceof ShapeNode) {
                shapesToCheck.add(((ShapeNode) node).getValue());
            } else {
                final SuperShapeNode superShapeNode = (SuperShapeNode) node;
                calcIntersection(ray, superShapeNode, shapesToCheck);
            }
        }
        return shapesToCheck;
    }

    public Vector3 calculateColor(Ray ray, int row, int col) {
        return calculateColor(ray, 1, null, row, col);
    }

    public Vector3 calculateColor(Ray ray, int recursionDepth, Shape excludeShape, int row, int col) {
        // We limit recursion depth
        if (recursionDepth > this.settings.getMaxRecursionLevel()) {
            return vector3Factory.getVector3(0, 0, 0);
        }

        recursionDepth += 1;

        IntersectionData inter = calcIntersection(ray, excludeShape);

        // If Ray does not intersect any body, return the background color
        if (inter == null)
            return this.settings.getBackgroundColor(col ,row);

        Material mat = this.materials.get(inter.getShape().getMaterial());

        Vector3 normal = inter.getShape().normal(inter.getIntersectionPointCpy(), ray);

        //////////////////////////////////////////////////////////////////
        // Diffuse and sepecular color calculation
        Vector3 diffuseAndSpecColor = diffuseAndSpecColor(inter, normal, ray, mat);

        //////////////////////////////////////////////////////////////////
        // Transparency Color calculation
        Vector3 transColor = transparencyColor(inter, ray, mat, recursionDepth, row, col);

        //////////////////////////////////////////////////////////////////
        // Reflection Color calculation
        Vector3 reflectionColor = reflectionColor(inter, normal, ray, mat, recursionDepth, row, col);

        Vector3 result = vector3Factory.getVector3(0, 0, 0);
        result.addInPlace(transColor.multiply(mat.getTransparency()));
        result.addInPlace(diffuseAndSpecColor.cpy().multiply(1 - mat.getTransparency()));
        result.addInPlace(reflectionColor);

        return result.boundFromAboveInPlace(new double[]{1d, 1d, 1d});
    }

    public void addShape(final Shape shape) {
        this.shapes.add(shape);
        this.sceneNode.addChild(new ShapeNode(shape));
    }

    public void addSuperShape(final SuperShape superShape) {
        final SuperShapeNode superShapeNode = new SuperShapeNode(superShape);
        this.sceneNode.addChild(superShapeNode);

        if (superShape.getSuperShapeChildren() != null) {
            superShape.getSuperShapeChildren().stream()
                    .map(SuperShapeNode::new)
                    .forEach(childSuperShape -> this.addSuperShape(childSuperShape, superShapeNode));
        }
        if (superShape.getShapeChildren() != null) {
            superShape.getShapeChildren().stream()
                    .map(ShapeNode::new)
                    .forEach(childShapeNode -> {
                        this.shapes.add(childShapeNode.getValue());
                        superShapeNode.addChild(childShapeNode);
                    });

        }
    }

    private void addSuperShape(final SuperShapeNode superShapeNode, final SuperShapeNode parentSuperShapeNode) {
        parentSuperShapeNode.addChild(superShapeNode);

        final SuperShape superShape = superShapeNode.getValue();
        if (superShapeNode.getValue().getShapeChildren() != null) {
            superShape.getSuperShapeChildren().stream()
                    .map(SuperShapeNode::new)
                    .forEach(childSuperShape -> this.addSuperShape(childSuperShape, superShapeNode));
        }
        if (superShape.getShapeChildren() != null) {
            superShape.getShapeChildren().stream()
                    .map(ShapeNode::new)
                    .forEach(childShapeNode -> {
                        this.shapes.add(childShapeNode.getValue());
                        superShapeNode.addChild(childShapeNode);
                    });

        }
    }

}
