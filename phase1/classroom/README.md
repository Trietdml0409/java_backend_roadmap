/**
 * Bai tap 1.5: Student Record
 *
 * Yeu cau:
 * 1. Tao record Student voi cac fields:
 *    - name (String)
 *    - age (int)
 *    - gpa (double)
 *
 * 2. Compact constructor de validate:
 *    - name khong null/rong
 *    - age tu 16 den 100
 *    - gpa tu 0.0 den 4.0
 *    - Throw IllegalArgumentException neu khong hop le
 *
 * 3. Custom method: boolean isHonors()
 *    - Tra ve true neu gpa >= 3.5
 *
 * 4. Viet cac static methods trong class Exercise1_5_StudentRecord:
 *
 *    a) findHighestGpa(Student[] students) -> Student
 *       - Tim student co GPA cao nhat
 *       - Tra ve null neu mang rong
 *
 *    b) averageAge(Student[] students) -> double
 *       - Tinh tuoi trung binh cua tat ca students
 *       - Tra ve 0 neu mang rong
 *
 *    c) filterHonors(Student[] students) -> Student[]
 *       - Loc ra cac student co GPA > 3.5
 *       - Goi y: dem truoc, tao mang, sau do dien vao
 *
 * Goi y:
 * - Record tu dong tao constructor, getters, equals, hashCode, toString
 * - Getter cua record khong co prefix "get": student.name(), student.gpa()
 * - De tao mang moi: new Student[count]
 */

