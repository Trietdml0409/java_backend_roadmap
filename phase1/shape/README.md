import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 1.7 — Shape Hierarchy
 *
 * BÀI TẬP: Xây dựng hệ thống phân cấp hình học sử dụng abstract class,
 * interface, và polymorphism.
 *
 * YÊU CẦU:
 *
 * 1. Abstract class Shape:
 *    - Abstract method: double area()
 *    - Abstract method: double perimeter()
 *    - Concrete method: String describe()
 *      → Trả về "Shape: [tên class], Area: [area], Perimeter: [perimeter]"
 *
 * 2. Subclasses:
 *    - Circle(double radius)
 *    - Rectangle(double width, double height)
 *    - Triangle(double a, double b, double c) — dùng công thức Heron cho area
 *      Công thức Heron: s = (a+b+c)/2, area = sqrt(s*(s-a)*(s-b)*(s-c))
 *
 * 3. Interface Drawable:
 *    - Method: void draw() — in ra hình ASCII đơn giản
 *    - Circle và Rectangle implement Drawable (Triangle thì KHÔNG)
 *
 * 4. Trong main():
 *    - Tạo List<Shape> chứa nhiều shapes khác nhau
 *    - In describe() cho tất cả (polymorphism)
 *    - Lọc ra các shape là Drawable bằng instanceof, gọi draw()
 *
 * GỢI Ý:
 * - Dùng Math.PI cho pi, Math.sqrt() cho căn bậc hai
 * - String.format("%.2f", value) để format số thập phân
 * - getClass().getSimpleName() để lấy tên class
 */



