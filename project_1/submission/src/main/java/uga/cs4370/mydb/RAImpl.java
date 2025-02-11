package uga.cs4370.mydb;

import java.text.NumberFormat.Style;
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
        return rel1;
    };

    /**
     * Performs the set difference operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2) {
        return rel1;
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
        List<String> commonAttr = new ArrayList<>(rel1.getAttrs());
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

        // use marked columns to find common values

        List<String> resAttr = null;
        List<Type> resTypes = null;

        Relation result = new RelationBuilder().attributeNames(resAttr).attributeTypes(resTypes).build();

        return result;
    };

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
