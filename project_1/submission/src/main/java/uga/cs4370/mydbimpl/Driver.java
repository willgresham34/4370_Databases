package uga.cs4370.mydbimpl;

import java.util.List;

import uga.cs4370.mydb.RA;
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

        // department: dept_name, building, budget
        Relation department = new RelationBuilder()
                .attributeNames(List.of("dept_name", "building", "budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        department.loadData("./project_1/tables/department_export.csv");

        // classroom: building, room_number, capacity
        Relation classroom = new RelationBuilder()
                .attributeNames(List.of("building", "room_number", "capacity"))
                .attributeTypes(List.of(Type.STRING, Type.INTEGER, Type.INTEGER))
                .build();
        classroom.loadData("./project_1/tables/classroom_export.csv");

        // takes: ID, course_id, sec_id, semester, year, grade (ID can also be t_ID)
        Relation takes = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year", "grade"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER, Type.STRING))
                .build();
        takes.loadData("./project_1/tables/takes_export.csv");

        // teaches: ID, course_id, sec_id, semester, year (ID can also be s_ID
        Relation teaches = new RelationBuilder()
                .attributeNames(List.of("ID", "course_id", "sec_id", "semester", "year"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER))
                .build();
        teaches.loadData("./project_1/tables/teaches_export.csv");

        // advisor: s_ID, i_ID
        Relation advisors = new RelationBuilder()
                .attributeNames(List.of("s_ID", "i_ID"))
                .attributeTypes(
                        List.of(Type.INTEGER, Type.INTEGER))
                .build();
        advisors.loadData("./project_1/tables/advisors_export.csv");

        // ----------------------- Queries below here ----------------------------

        // natural join test(s)
        Relation classroomDeptNJ = ra.join(department, classroom);
        classroomDeptNJ.print();

        Relation teachesXTakes = ra.join(takes, teaches);
        teachesXTakes.print();

    }

}
