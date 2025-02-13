package uga.cs4370.mydb;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the relational algebra operators.
 *
 * Note: The implementing classes should not modify the relations that are
 * passed as parameters to the methods. Instead new relations should be
 * initialized and returned as the result.
 */
public class RAImpl implements RA {

    /**
     * Performs the select operation on the relation rel by applying the
     * predicate p.
     *
     * @return The resulting relation after applying the select operation.
     */
    public Relation select(Relation rel, Predicate p) {
        Relation ans = new RelationBuilder()
                .attributeNames(rel.getAttrs())
                .attributeTypes(rel.getTypes())
                .build();

        int size = rel.getSize();

        for (int i = 0; i < size; i++) {
            boolean isValid = p.check(rel.getRow(i));

            if (isValid) {
                ans.insert(rel.getRow(i));
            }
        }
        return ans;

    }

    ;

    /**
     * Performs the project operation on the relation rel
     * given the attributes list attrs.
     * 
     * @return The resulting relation after applying the project operation.
     * 
     * @throws IllegalArgumentException If attributes in attrs are not
     *                                  present in rel.
     */
    public Relation project(Relation rel, List<String> attrs) {

        List<Type> types = rel.getTypes();
        List<Type> new_types = new ArrayList<>();
        List<String> rel_attrs = rel.getAttrs();

        //Check if requested attributes are present in given relation
        try {
            for (int i = 0; i < attrs.size(); i++) {
                boolean found = rel.hasAttr(attrs.get(i));
                if (!found) {
                    throw new IllegalArgumentException();
                } else {
                    int index = rel.getAttrIndex(attrs.get(i));
                    new_types.add(rel.getRow(0).get(index).getType());
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Requested attributes are not present in given Relation");
            return rel;
        }

        //create new relation
        Relation ans = new RelationBuilder().attributeNames(attrs).attributeTypes(new_types).build();

        //Go thru given relation. Pick out cells in each row matching requested attrs and put in a list. insert "row" list into new relation
        for (int i = 0; i < rel.getSize(); i++) {
            List<Cell> new_row = new ArrayList<>();
            List<Cell> current_row = rel.getRow(i);
            for (int j = 0; j < attrs.size(); j++) {
                int index = rel.getAttrIndex(attrs.get(j));
                new_row.add(current_row.get(index));
            }
            ans.insert(new_row);
        }

        return ans;
    }

    ;

    /**
     * Performs the union operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the union operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation union(Relation rel1, Relation rel2) {
        return rel1;
    }

    ;

    /**
     * Performs the set difference operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2) {
        return rel1;
    }

    ;

    /**
     * Renames the attributes in origAttr of relation rel to corresponding
     * names in renamedAttr.
     * 
     * @return The resulting relation after renaming the attributes.
     * 
     * @throws IllegalArgumentException If attributes in origAttr are not present in
     *                                  rel or origAttr and renamedAttr do not have
     *                                  matching argument counts.
     */
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
        return rel;
    }

    ;

    /**
     * Performs cartesian product on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying cartesian product.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        return rel1;
    }

    ;

    /**
     * Performs natural join on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying natural join.
     */
    public Relation join(Relation rel1, Relation rel2) {

        // find common cols
        List<String> commonAttr = new ArrayList<String>(rel1.getAttrs());
        commonAttr.retainAll(rel2.getAttrs());

        if (commonAttr.isEmpty()) {
            throw new IllegalArgumentException("No common attributes to join on");
        }

        // mark cols
        List<Integer> rel1Index = new ArrayList<Integer>();
        List<Integer> rel2Index = new ArrayList<Integer>();

        for (String attr : commonAttr) {
            rel1Index.add(rel1.getAttrIndex(attr));
            rel2Index.add(rel2.getAttrIndex(attr));
        }

        // create new set of cols
        List<String> resAttrs = new ArrayList<String>(rel1.getAttrs());
        for (String attr : rel2.getAttrs()) {
            if (!commonAttr.contains(attr)) {
                resAttrs.add(attr);
            }
        }

        // create types for
        List<Type> resTypes = new ArrayList<Type>();
        for (String attr : rel1.getAttrs()) {
            resTypes.add(rel1.getTypes().get(rel1.getAttrIndex(attr)));
        }

        for (String attr : rel2.getAttrs()) {
            if (!commonAttr.contains(attr)) {
                resTypes.add(rel2.getTypes().get(rel2.getAttrIndex(attr)));
            }
        }

        // create relation using resulting types and attributes
        Relation result = new RelationBuilder().attributeNames(resAttrs).attributeTypes(resTypes).build();

        for (int i = 0; i < rel1.getSize(); i++) {
            // get row from rel 1
            List<Cell> rel1Row = rel1.getRow(i);

            for (int j = 0; j < rel2.getSize(); j++) {

                // get row from rel 2
                List<Cell> rel2Row = rel2.getRow(j);

                // prep match
                boolean match = true;

                // check the attribute values of each common cell against eachother
                for (int k = 0; k < commonAttr.size(); k++) {
                    if (!rel1Row.get(rel1Index.get(k)).equals(rel2Row.get(rel2Index.get(k)))) {
                        match = false;
                        break;
                    }
                }

                // merge matches
                if (match) {
                    List<Cell> mergedRow = new ArrayList<>(rel1Row);

                    // check each col of rel2 against the common ones & add to row if false
                    for (String attr : rel2.getAttrs()) {
                        if (!commonAttr.contains(attr)) {
                            mergedRow.add(rel2Row.get(rel2.getAttrIndex(attr)));
                        }
                    }

                    result.insert(mergedRow);
                }
            }
        }

        return result;
    }

    /**
     * Performs theta join on relations rel1 and rel2 with predicate p.
     *
     * @return The resulting relation after applying theta join. The resulting
     * relation should have the attributes of both rel1 and rel2. The attributes
     * of rel1 should appear in the the order they appear in rel1 but before the
     * attributes of rel2. Attributes of rel2 as well should appear in the order
     * they appear in rel2.
     *
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation join(Relation rel1, Relation rel2, Predicate p) {

        // find common cols
        List<String> commonAttr = new ArrayList<String>(rel1.getAttrs());
        commonAttr.retainAll(rel2.getAttrs());

        if (!commonAttr.isEmpty()) {
            throw new IllegalArgumentException("Cannot join relations with common attributes");
        }

        // create result cols
        List<String> resAttrs = new ArrayList<String>(rel1.getAttrs());
        resAttrs.addAll(rel2.getAttrs());

        // create result types
        List<Type> resTypes = new ArrayList<Type>(rel1.getTypes());
        resTypes.addAll(rel2.getTypes());

        System.out.println(resAttrs.toString());

        // create relation using resulting types and attributes
        Relation result = new RelationBuilder().attributeNames(resAttrs).attributeTypes(resTypes).build();

        for (int i = 0; i < rel1.getSize(); i++) {
            // get row from rel 1
            List<Cell> rel1Row = rel1.getRow(i);

            for (int j = 0; j < rel2.getSize(); j++) {

                // get row from rel 2
                List<Cell> rel2Row = rel2.getRow(j);

                // create combined row
                List<Cell> mergedRow = new ArrayList<>();
                mergedRow.addAll(rel1Row);
                mergedRow.addAll(rel2Row);

                if (p.check(mergedRow)) {
                    result.insert(mergedRow);
                }
            }
        }

        return result;
    }
;

}
