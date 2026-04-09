package phase1.shape;

// TODO: Tạo interface Drawable với method draw()
// interface Drawable {
//     ...
// }

// TODO: Tạo abstract class Shape với area(), perimeter(), describe()
// abstract class Shape {
//     ...
// }

// TODO: Tạo class Circle extends Shape implements Drawable
// class Circle extends Shape implements Drawable {
//     private double radius;
//
//     // Constructor
//     // Circle(double radius) { ... }
//
//     // Implement area(): Math.PI * radius * radius
//     // @Override
//     // public double area() { ... }
//
//     // Implement perimeter(): 2 * Math.PI * radius
//     // @Override
//     // public double perimeter() { ... }
//
//     // Implement draw(): in hình tròn ASCII đơn giản
//     // Ví dụ:
//     //   ***
//     //  *   *
//     //  *   *
//     //   ***
//     // @Override
//     // public void draw() { ... }
// }

// TODO: Tạo class Rectangle extends Shape implements Drawable
// class Rectangle extends Shape implements Drawable {
//     private double width;
//     private double height;
//
//     // Constructor
//     // Rectangle(double width, double height) { ... }
//
//     // Implement area(): width * height
//     // Implement perimeter(): 2 * (width + height)
//
//     // Implement draw(): in hình chữ nhật ASCII
//     // Ví dụ:
//     //  +----+
//     //  |    |
//     //  +----+
//     // @Override
//     // public void draw() { ... }
// }

// TODO: Tạo class Triangle extends Shape (KHÔNG implement Drawable)
// class Triangle extends Shape {
//     private double a, b, c;
//
//     // Constructor — kiểm tra tam giác hợp lệ (tổng 2 cạnh > cạnh còn lại)
//     // Triangle(double a, double b, double c) { ... }
//
//     // Implement area() dùng công thức Heron:
//     //   double s = (a + b + c) / 2;
//     //   return Math.sqrt(s * (s - a) * (s - b) * (s - c));
//
//     // Implement perimeter(): a + b + c
// }

public class shape {
    public static void main(String[] args) {
        System.out.println("=== Exercise 1.7: Shape Hierarchy ===\n");

        // TODO: Tạo List<Shape> chứa ít nhất 5 shapes
        // List<Shape> shapes = new ArrayList<>();
        // shapes.add(new Circle(5.0));
        // shapes.add(new Rectangle(4.0, 6.0));
        // shapes.add(new Triangle(3.0, 4.0, 5.0));
        // shapes.add(new Circle(2.5));
        // shapes.add(new Rectangle(10.0, 3.0));

        // TODO: In describe() cho tất cả shapes (polymorphism)
        // System.out.println("--- All Shapes ---");
        // for (Shape shape : shapes) {
        //     System.out.println(shape.describe());
        // }

        // TODO: Lọc và vẽ các Drawable shapes bằng instanceof
        // System.out.println("\n--- Drawable Shapes Only ---");
        // for (Shape shape : shapes) {
        //     if (shape instanceof Drawable drawable) {
        //         System.out.println("Drawing " + shape.getClass().getSimpleName() + ":");
        //         drawable.draw();
        //         System.out.println();
        //     } else {
        //         System.out.println(shape.getClass().getSimpleName() + " is not Drawable");
        //     }
        // }

        System.out.println("TODO: Implement the classes above and uncomment this code!");
    }
}
