package uga.cs4370.mydbimpl;

import java.util.List;

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

        Relation department = new RelationBuilder()
                .attributeNames(List.of("dept_name", "building", "budget"))
                .attributeTypes(List.of(Type.STRING, Type.STRING, Type.DOUBLE))
                .build();
        department.loadData("./tables/department_export.csv");
        department.print();
    }

}
