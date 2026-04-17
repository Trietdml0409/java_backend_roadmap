package phase1.shape;

import java.util.ArrayList;
import java.util.List;

// TODO: Tạo interface Drawable với method draw()
// interface Drawable {
//     ...
// }
interface Drawable {
    public void draw();
}


// TODO: Tạo abstract class Shape với area(), perimeter(), describe()
abstract class Shape {
    public abstract double area();
    public abstract double perimeter();
    public String describe(){
        return "This is a shape";
    }
}



// TODO: Tạo class Circle extends Shape implements Drawable
class Circle extends Shape implements Drawable{
    double radius;

    Circle(double radius){this.radius = radius;}

    @Override
    public double area(){
        double area = Math.PI * this.radius * this.radius;
        return area;
    }

    @Override
    public double perimeter(){
        double perimeter = 2 * Math.PI * this.radius;
        return perimeter;
    };

    @Override
    public String describe(){
        return String.format("This is a Circle, with the area of: %.2f and the perimeter of: %.2f",
                     area(), perimeter());
    }

    @Override
    public void draw(){
        String[] lines = {
            "  ***",
            " *   *",
            " *   *",
            "  ***"
        };

        for (String line : lines) {
            System.out.println(line);
        }
    }
}


// TODO: Tạo class Rectangle extends Shape implements Drawable
class Rectangle extends Shape implements Drawable{
    double width;
    double height;

    Rectangle(double width, double height){this.height = height; this.width = width;}

    @Override
    public double area(){
        double area = this.width * this.height;
        return area;
    }

    @Override
    public double perimeter(){
        double perimeter = this.width * 2 + this.height *2;
        return perimeter;
    };

    @Override
    public String describe(){
        return String.format("This is a Rectangle, with the area of: %.2f and the perimeter of: %.2f",
                     area(), perimeter());

    }

    @Override
    public void draw(){
        String[] lines = {
            "******",
            "*    *",
            "*    *",
            "******"
        };

        for (String line : lines) {
            System.out.println(line);
        }
    }
}

// TODO: Tạo class Triangle extends Shape (KHÔNG implement Drawable)
class Triangle extends Shape{
    double a,b,c;

    public Triangle(double a, double b, double c){
        this.a = a; this.b = b; this.c = c;
    }

    @Override
    public double area(){
        double s = (a+b+c)/2;
        double area = Math.sqrt(s*(s-a)*(s-b)*(s-c));
        return area;
    }

    @Override
    public double perimeter(){
        return a+b+c;
    }

    @Override
    public String describe(){
        return String.format("This is a Triangle, with the area of: %.2f and the perimeter of: %.2f",
                     area(), perimeter());

    }



}

public class ShapeClasses{
    public static void main(String[] args) {
        System.out.println("=== Exercise 1.7: Shape Hierarchy ===\n");

        // TODO: Tạo List<Shape> chứa ít nhất 5 shapes
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(5.0));
        shapes.add(new Rectangle(4.0, 6.0));
        shapes.add(new Triangle(3.0, 4.0, 5.0));
        shapes.add(new Circle(2.5));
        shapes.add(new Rectangle(10.0, 3.0));

        // TODO: In describe() cho tất cả shapes (polymorphism)
        System.out.println("--- All Shapes ---");
        for (Shape shape : shapes) {
            System.out.println(shape.describe());
        }

        // TODO: Lọc và vẽ các Drawable shapes bằng instanceof
        System.out.println("\n--- Drawable Shapes Only ---");
        for (Shape shape : shapes) {
            if (shape instanceof Drawable drawable) {
                System.out.println("Drawing " + shape.getClass().getSimpleName() + ":");
                drawable.draw();
                System.out.println();
            } else {
                System.out.println(shape.getClass().getSimpleName() + " is not Drawable");
            }
        }

        System.out.println("TODO: Implement the classes above and uncomment this code!");
    }
}
