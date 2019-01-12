/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.common.model;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public abstract class AssociativeEntity extends AbstractEntity {

	/* */
	private static final long serialVersionUID = 1L;
	
    public Set<AbstractEntity> parentEntities = new TreeSet<>();

    public void setParentEntities(Set<AbstractEntity> parentEntities) {
        this.parentEntities = parentEntities;
        assertParentEntities();
    }

    public void addParentEntity(AbstractEntity parentEntity) {
        this.parentEntities.add(parentEntity);
    }

    public int hashCode() {
        assertParentEntities();
        return this.parentEntities.hashCode();
    }

    public Set<AbstractEntity> getParentEntities() {
        assertParentEntities();
        return this.parentEntities;
    }

    public boolean equals(Object that) {

        if (that == null) {
            return false;
        }

        if (that == this) {
            return true;
        }

        if (!this.getClass().equals(that.getClass())) {
            return false;
        }

        assertParentEntities();
        return this.parentEntities.equals(((AssociativeEntity)that).parentEntities);
    }

    public String getNaturalIdentity() {

        assertParentEntities();
        StringBuilder sb = new StringBuilder();
        Iterator<AbstractEntity> iterator = this.parentEntities.iterator();
        while (iterator.hasNext()) {
            AbstractEntity abstractEntity = iterator.next();
            sb.append(abstractEntity.getNaturalIdentity());
            if (iterator.hasNext()) {
                sb.append(NATURAL_IDENTITY_DELIMITER);
            }
        }
        return sb.toString();
    }

    private void assertParentEntities() {
        if (parentEntities == null || parentEntities.size() < 2) {
            throw new IllegalStateException("An AssociativeEntity must contain at least 2 parent AbstractEntity instances.");
        }
    }
}
