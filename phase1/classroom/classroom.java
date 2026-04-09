package phase1.classroom;


public class classroom {
    public static void main(String[] args) {
        Student[] students = new Student[5];

        // TODO: Tao mang students voi du lieu mau:
        //   ("Alice", 20, 3.8)
        //   ("Bob", 22, 3.2)
        //   ("Charlie", 21, 3.9)
        //   ("Diana", 19, 2.5)
        //   ("Eve", 23, 3.6)
        students[0] = new Student("Alice", 20, 3.8);
        students[1] = new Student("Bob", 22, 3.2);
        students[2] = new Student("Charlie", 21, 3.9);
        students[3] = new Student("Diana", 19, 2.5);
        students[4] = new Student("Eve", 23, 3.6);



        // TODO: In ra tat ca students (dung enhanced for-each)
        System.out.println("List of students: ");
        for(Student std : students){
            System.out.println(std);
        }

        // TODO: Tim va in ra student co GPA cao nhat
        System.out.println("\n\nHighest Gpa:"+Student.findHighestGpa(students));

        // TODO: In ra tuoi trung binh
        System.out.println("\n\nAverage age: "+Student.averageAge(students));

        // TODO: In ra danh sach honors students (GPA > 3.5)
        Student[] honors = Student.filterHonors(students);
        System.out.println("\n\nList of honors: ");
        for(int i=0; i<honors.length; i++){
            System.out.println(honors[i]);
        }

        // TODO: Thu so sanh 2 student records co cung gia tri — dung equals()
        System.out.println("\n\nDoes Alice and Bob have a same gpa?");
        if(students[0].equals(students[1])){
            System.out.println("Yes they do!");
        }else{
            System.out.println("No they don't!");
        }
        
    }
}


