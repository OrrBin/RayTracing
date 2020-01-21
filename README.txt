Our creative idea was to have an option for rendering the graph of the function z = cos(x) + sin(y) + offset.

running instructions:
in the scene file to add the new kind of object add the following line:

cps	offset	material

where offset is the offset from the equation above, and material is the material index.

There are scene files that uses the new object in the creative_scenes folder
There are results of those scenes attached in the "creative_results" folder.

What we did:
In order to implement the rendering of the function graph we had to find where the ray intesects the graph.
In order to do that we use a custom variation on the "Ray marching" method, that we came up with.
We march the ray in contant steps up to a point where we pass the function graph, then we lower the step and goes back.
We repeat this process until we are very close to the real value.

Once we find the intersection point we can calculate the normal using the differntial of the function,
And then claculate the color.

note 1:
The calculation is far from ideal and takes time. we used multi threading in order to accelerate the process.
It took us few minutes to render each scene we attached in the "creative_results" folder.
You might want to create smaller images to reduce time (300x400 for example), and the Super Sampling level low (maybe even 1
for between 30 seconds to 1 minute render)

improvements suggestions:
1. In order to implement graphs of more functions all that is required is to extend the abstract class "FunctionGraph" which has the next abstract functions:
func - which calculates the function value at a given point 
isOutOfBounds - which is used to determine when we know we wont meet the function graph
maxIterations - helper function to determine maximumNumber of iterations in the "custom Ray Marching algorithm".
2. we could improve performence by using "Gradient Descent" or "Newton Raphson" to find where the function graph intersects the ray.
We didn't do it because of time limitations.



