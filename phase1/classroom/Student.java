package phase1.classroom;

public record Student(String name, int age, double gpa) {

    public Student {
        if (name == null) {
            System.out.println("Name cannot be null");
            throw new IllegalArgumentException("Invalid name");
        }

        if (age < 16 || age > 100) {
            System.out.println("Age need to be above 16 and below 100");
            throw new IllegalArgumentException("Invalid age");
        }

        if (gpa < 0.0 || gpa > 4.0) {
            System.out.println("GPA need to be above 0 and below 4");
            throw new IllegalArgumentException("Invalid GPA");
        }

        // No assignments — Java automatically does:
        // this.name = name;
        // this.age = age;
        // this.gpa = gpa;
    }

    // TODO: Them method isHonors() tra ve true neu gpa >= 3.5
    public static boolean isHonors(Student student){
        return student.gpa() >= 3.5;
    }




    // TODO: Tim student co GPA cao nhat
    static Student findHighestGpa(Student[] students) {

        Student highestGpaStudent = students[0];

        // TODO: Implement
        for(int i=0; i<students.length; i++ ){
            if(students[i].gpa() > highestGpaStudent.gpa()){
                highestGpaStudent = students[i];
            }
        }

        return highestGpaStudent;
    }

    // TODO: Tinh tuoi trung binh
    static double averageAge(Student[] students) {

        double sum_age = 0;
        double average;

        for(int i=0; i<students.length; i++){
            sum_age += students[i].age();
        }

        average = sum_age/students.length;

        return average;
    }

    // TODO: Loc ra cac student co GPA > 3.5
    static Student[] filterHonors(Student[] students) {
        int honors_count = 0;
        for(int i=0; i<students.length;i++){
            if(Student.isHonors(students[i])){
                honors_count++;
            }
        }
        Student[] honors_students = new Student[honors_count];

        int index = 0;
        for(int i=0; i<students.length;i++){
            if(Student.isHonors(students[i])){
                honors_students[index++] = students[i];
            }
        }


        // TODO: Implement
        // Goi y: buoc 1 — dem so luong honors students
        //         buoc 2 — tao mang moi voi kich thuoc do
        //         buoc 3 — dien cac honors students vao mang
        return honors_students;
    }
}

