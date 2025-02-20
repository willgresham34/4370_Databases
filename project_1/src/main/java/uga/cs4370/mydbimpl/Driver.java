package uga.cs4370.mydbimpl;

import java.util.Arrays;
import java.util.List;

import uga.cs4370.mydb.RAImpl;
import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;

public class Driver {
    
    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.
        
        // Relation advisor = new RelationBuilder()
        // .attributeNames(List.of("Student_Id", "Instructor_Id"))
        // .attributeTypes(List.of(Type.INTEGER, Type.INTEGER))
        // .build();
        // advisor.loadData("./tables/advisor_export.csv");
        // advisor.print();
        RAImpl ra = new RAImpl();
        
        // advisor: s_ID, i_ID
        Relation advisors = new RelationBuilder()
                .attributeNames(List.of("s_ID", "i_ID"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER))
                .build();
        advisors.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/advisor_export.csv");
        
        // classroom: building, room_number, capacity
        Relation classroom = new RelationBuilder()
                .attributeNames(List.of("building", "room_number", "capacity"))
                .attributeTypes(List.of(Type.STRING, Type.INTEGER, Type.INTEGER))
                .build();
        classroom.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/classroom_export.csv");
        
        // course: course_id, name, dept_name, cred_hr
        Relation course = new RelationBuilder()
                .attributeNames(List.of("course_id", "name", "dept_name", "cred_hr"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER))
                .build();
        course.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/course_export.csv");
        
        // department: dept_name, building, budget
        Relation department = new RelationBuilder()
                .attributeNames(List.of("dept_name", "building", "budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        department.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/department_export.csv");
        
        // instructors
        Relation instructors = new RelationBuilder()
                .attributeNames(List.of("ID", "name", "dept_name", "salary"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        instructors.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/instructor_export.csv");
        
        // students
        Relation students = new RelationBuilder()
                .attributeNames(List.of("ID", "name", "dept_name", "tot_cred"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        students.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/student_export.csv");
        
        // takes: ID, course_id, sec_id, semester, year, grade (ID can also be t_ID)
        Relation takes = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year", "grade"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER, Type.STRING))
                .build();
        takes.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/takes_export.csv");
        
        // teaches: ID, course_id, sec_id, semester, year (ID can also be s_ID
        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER))
                .build();
        teaches.loadData("./project_1/submission/src/main/java/uga/cs4370/mydbimpl/tables/teaches_export.csv");
        
        // ----------------------- Queries below here ----------------------------
        
        // ### Adam's Query ###

        System.out.println("BEGINNING OF Adam's query");

        // Return names of advisors who advise students majoring in math
        Relation math_students = ra.select(students, row -> row.get(students.getAttrIndex("dept_name")).getAsString().equals("Math"));
        // project ids from math students
        Relation math_ids = ra.project(math_students, List.of("ID"));
        // Join math student ids with their advisors
        Relation adv_of_math = ra.join(math_ids, advisors, row -> row.get(0).getAsInt() == row.get(advisors.getAttrIndex("s_ID") + 1).getAsInt());
        // Project instructor ids
        Relation adv_id = ra.project(adv_of_math, List.of("i_ID"));
        // Join instructor ids with instructor table
        Relation inst_math = ra.join(adv_id, instructors, row -> row.get(0).getAsInt() == row.get(instructors.getAttrIndex("ID") + 1).getAsInt());
        // Project instructor names and print
        Relation inst_names = ra.project(inst_math, List.of("name"));
        inst_names.print();


        // ### Anthony's Query ###
        // Rename Computer Science's dept_name in the course table to its full form
        // and take the cartesian product of this renamed course table and classroom.

        System.out.println("\nBEGINNING OF Anthony's query");

        // create a list of all courses in the computer science department 
        Relation courseInCompSci = ra.select(course, row -> row.get(course.getAttrIndex("dept_name")).getAsString().equals("Comp. Sci."));
        
        // rename dept_name from "Comp. Sci." to "Computer Science"
        Relation renamedCourseInCompSci = ra.rename(courseInCompSci, Arrays.asList("dept_name"), Arrays.asList("department_name"));
        
        // print before and after
        System.out.println("original:");
        courseInCompSci.print();
        System.out.println("renamed:");
        renamedCourseInCompSci.print();
        
        // test cartesianProduct()
        Relation cartesianProductOfRenamedCourseInCompSciAndClassroom = ra.cartesianProduct(renamedCourseInCompSci, classroom);
        System.out.println("cartesianProductOfRenamedCourseInCompSciAndClassroom:");
        cartesianProductOfRenamedCourseInCompSciAndClassroom.print();


        // ### Connor's Query ###
        // Return the name and dept_name of students who took any class in Civil Eng. in 2009

        System.out.println("\nBEGINNING OF Connor's query");
        
        // Get all Civil Eng. Courses with course id and type
        Relation civilEngCourses = ra.select(course, row -> row.get(course.getAttrIndex("dept_name")).getAsString().equals("Civil Eng."));
        // Project only course_id
        Relation civilEngCid = ra.project(civilEngCourses, List.of("course_id"));
        // Join Civil Eng. classes on takes to get all civil eng course taken
        Relation takesCivilEngCourse = ra.join(civilEngCid, takes);
        // Select all courses taken in 2009 from takesCivilEngCourse
        Relation takesCivilEngCourse2009 = ra.select(takesCivilEngCourse, row -> row.get(takesCivilEngCourse.getAttrIndex("year")).getAsInt() == 2009);
        // Project only id
        Relation idTakesCivilEngCourse2009 = ra.project(takesCivilEngCourse2009, List.of("ID"));
        // // Join on student
        Relation studentTakesCivilEngCourse2009 = ra.join(idTakesCivilEngCourse2009, students);
        // Project name, major
        Relation stu_nameMajorCivilEngCourse2009 = ra.project(studentTakesCivilEngCourse2009, List.of("name", "dept_name"));
        System.out.println("Name/Dept of students who took any class in Civil Eng. in 2009:");
        stu_nameMajorCivilEngCourse2009.print();


        // ### Will's First Query
        // return the name of the advisors of students with more than 70 credit hours in
        // get all students in cs with 70 or more credits

        System.out.println("\nBEGINNING OF Will's first query");

        Relation csStudentsWith70 = ra.select(students, row -> row.get(students.getAttrIndex("dept_name")).getAsString().equals("Comp. Sci.") && row.get(students.getAttrIndex("tot_cred")).getAsDouble() > 70);
        
        // project just there student ids
        Relation w70Ids = ra.project(csStudentsWith70, List.of("ID"));
        
        // join the student ids with the advisor table
        Relation advisorOfStu = ra.join(w70Ids, advisors, row -> row.get(0).getAsInt() == row.get(advisors.getAttrIndex("s_ID") + 1).getAsInt());
        
        // project just the instructor ids
        Relation instIdsOfCsStuds = ra.project(advisorOfStu, List.of("i_ID"));
        
        // combine instIds with instructor table
        Relation instOfCsStuds = ra.join(instIdsOfCsStuds, instructors, row -> row.get(0).getAsInt() == row.get(instructors.getAttrIndex("ID") + 1).getAsInt());
        System.out.println("joined instIds with instrcutor table");
        
        // project only the names of the instructors
        Relation instNamesOfCsStudWith70 = ra.project(instOfCsStuds, List.of("name"));
        System.out.println("Advisor names of the CS students with more than 70 total credit hours");
        instNamesOfCsStudWith70.print();


        // ### Will's Second Query ###
        // Return the names, semester and year of courses who are taught by teachers that make more than 100,000

        System.out.println("\nBEGINNING OF Will's second query");

        // get instructors who make 100k
        Relation instr_100k = ra.select(instructors, row -> row.get(instructors.getAttrIndex("salary")).getAsDouble() > 100000);
        
        // project just there ids
        Relation instr_100k_id = ra.project(instr_100k, List.of("ID"));
        
        // join with teaches tables
        Relation instr_teaches = ra.join(instr_100k_id, teaches);
        
        // join teaches with course table to get names
        Relation courses_teaches = ra.join(instr_teaches, course);
        
        // project only the cols we want
        Relation course_result = ra.project(courses_teaches, List.of("name", "semester", "year"));
        System.out.println("Name, semester, and year of courses taught by instructors whose salary is greater than 100,000");
        course_result.print();
    }

}
