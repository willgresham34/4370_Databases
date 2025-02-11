package uga.cs4370.mydb;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for the relational algebra operators.
 * 
 * Note: The implementing classes should not modify the relations
 * that are passed as parameters to the methods. Instead new relations
 * should be initialized and returned as the result.
 */
public class RAImpl implements RA {

    /**
     * Performs the select operation on the relation rel
     * by applying the predicate p.
     * 
     * @return The resulting relation after applying the select operation.
     */
    public Relation select(Relation rel, Predicate p) {
        return rel;
    };

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
        return rel;
    };

    /**
     * Performs the union operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the union operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation union(Relation rel1, Relation rel2) {

        // Check type compatibility
        List<Type> rel1Types = new ArrayList<>(rel1.getTypes());
        List<Type> rel2Types = new ArrayList<>(rel2.getTypes());
        if (rel1Types.size() != rel2Types.size()) {
            throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
        }

        for (int i = 0; i < rel1Types.size(); i++) {
            if (!rel1Types.get(i).equals(rel2Types.get(i))) {
                throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
            }
        }
        
        // Check attribute compatibility, may not be needed
        List<String> commonAttr = new ArrayList<>(rel1.getAttrs());
        commonAttr.retainAll(rel2.getAttrs());
        if (commonAttr.size() != rel1Types.size()) {
            throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
        }

        // set up new relation based on rel1
        Relation result = new RelationBuilder().attributeNames(rel1.getAttrs())
                                               .attributeTypes(rel1.getTypes())
                                               .build();

        // add all rows from rel1
        for (int i = 0; i < rel1.getSize(); i++) {
            result.insert(rel1.getRow(i));
        }

        // add rows from rel2 not present in rel1

        // for each row in rel2
        for (int i = 0; i < rel2.getSize(); i++) {

            List<Cell> rel2row = rel2.getRow(i);
            boolean rowPresent = false;

            // for each row in rel1
            for (int j = 0; i < rel1.getSize() && rowPresent == false; j++) {

                List<Cell> rel1row = rel1.getRow(j)
                boolean rowMatch = true;

                // for each cell in rel1, rel2, check equality
                for (int k = 0; k < rel2row.size() && rowMatch; k++) {

                    if (!rel2row.get(k).equals(rel1row.get(k))) {
                        rowMatch = false;
                    }
                }
                
                // if rowMatch, then rowPresent
                if (rowMatch == true) {
                    rowPresent = true;
                }
                
            }

            // if no rowPresent after checking all rel1, insert into result
            if (!rowPresent) {
                result.insert(rel2row);
            }

        }
        return result;
    };

    /**
     * Performs the set difference operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2) {

        // Check type compatibility
        List<Type> rel1Types = new ArrayList<>(rel1.getTypes());
        List<Type> rel2Types = new ArrayList<>(rel2.getTypes());
        if (rel1Types.size() != rel2Types.size()) {
            throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
        }

        for (int i = 0; i < rel1Types.size(); i++) {
            if (!rel1Types.get(i).equals(rel2Types.get(i))) {
                throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
            }
        }
        
        // Check attribute compatibility, may not be needed
        List<String> commonAttr = new ArrayList<>(rel1.getAttrs());
        commonAttr.retainAll(rel2.getAttrs());
        if (commonAttr.size() != rel1Types.size()) {
            throw new IllegalArgumentException("rel1 and rel2 are incompatible.");
        }

        // set up new relation based on rel1
        Relation result = new RelationBuilder().attributeNames(rel1.getAttrs())
                                               .attributeTypes(rel1.getTypes())
                                               .build();

        // for each row in rel1
        for (int i = 0; i < rel1.getSize(); i++) {

            List<Cell> rel1row = rel1.getRow(i);
            boolean rowPresent = false;

            // for each row in rel2
            for (int j = 0; i < rel2.getSize() && rowPresent == false; j++) {

                List<Cell> rel2row = rel2.getRow(j)
                boolean rowMatch = true;

                // for each cell in rel1, rel2, check equality
                for (int k = 0; k < rel1row.size() && rowMatch; k++) {

                    if (!rel1row.get(k).equals(rel2row.get(k))) {
                        rowMatch = false;
                    }
                    
                }
                
                // if rowMatch, then rowPresent
                if (rowMatch == true) {
                    rowPresent = true;
                }
                
            }

            // if no rowPresent after checking all rel2, insert into result
            if (!rowPresent) {
                result.insert(rel1row);
            }

        }
        return result;
    };

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
    };

    /**
     * Performs cartesian product on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying cartesian product.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        return rel1;
    };

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
     *         relation should have the attributes of both rel1 and rel2. The
     *         attributes of rel1 should appear in the the order they appear in rel1
     *         but before the attributes of rel2. Attributes of rel2 as well should
     *         appear in the order they appear in rel2.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attributes.
     */
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        return rel1;
    };

}
