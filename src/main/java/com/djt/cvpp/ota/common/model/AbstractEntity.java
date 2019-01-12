/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.common.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.event.AbstractEvent;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.timekeeper.TimeKeeper;
import com.djt.cvpp.ota.common.timekeeper.impl.TimeKeeperImpl;

/**
 * With regards to the getNaturalIdentity() method here, that method is used to implement equals() and hashCode() 
 * when an entity has not been persisted yet (i.e. in a transient state)  
 * From: https://docs.jboss.org/hibernate/orm/3.3/reference/en-US/html/mapping.html#mapping-declaration-naturalid
<pre>
5.1.14. Natural-id

<natural-id mutable="true|false"/>
        <property ... />
        <many-to-one ... />
        ......
</natural-id>

Although we recommend the use of surrogate keys as primary keys, you should try to identify natural keys for all entities. 
A natural key is a property or combination of properties that is unique and non-null. It is also immutable. Map the properties 
of the natural key inside the <natural-id> element. Hibernate will generate the necessary unique key and nullability constraints 
and, as a result, your mapping will be more self-documenting.

It is recommended that you implement equals() and hashCode() to compare the natural key properties of the entity.

This mapping is not intended for use with entities that have natural primary keys.

    mutable (optional - defaults to false): by default, natural identifier properties are assumed to be immutable (constant). 
</pre>  
 * 
 * 
 * From: http://docs.jboss.org/hibernate/orm/3.3/reference/en-US/html/persistent-classes.html
 * <pre>
   
	4.3. Implementing equals() and hashCode()
	
	You have to override the equals() and hashCode() methods if you:
	
	    - intend to put instances of persistent classes in a Set (the recommended way to represent many-valued associations)
	    
	    - intend to use reattachment of detached instances 
	
	Hibernate guarantees equivalence of persistent identity (database row) and Java identity only inside a particular 
	session scope. When you mix instances retrieved in different sessions, you must implement equals() and hashCode() 
	if you wish to have meaningful semantics for Sets.
	
	The most obvious way is to implement equals()/hashCode() by comparing the identifier value of both objects. If the
	value is the same, both must be the same database row, because they are equal. If both are added to a Set, you will 
	only have one element in the Set). Unfortunately, you cannot use that approach with generated identifiers. Hibernate 
	will only assign identifier values to objects that are persistent; a newly created instance will not have any identifier
	value. Furthermore, if an instance is unsaved and currently in a Set, saving it will assign an identifier value to the
	object. If equals() and hashCode() are based on the identifier value, the hash code would change, breaking the contract 
	of the Set. See the Hibernate website for a full discussion of this problem. This is not a Hibernate issue, but normal 
	Java semantics of object identity and equality.
	
	It is recommended that you implement equals() and hashCode() using Business key equality. Business key equality means 
	that the equals() method compares only the properties that form the business key. It is a key that would identify our 
	instance in the real world (a natural candidate key):
	
	In order to ensure identity constraints between Java objects and the relational database, it is imperative to have a 
	unique constraint defined in the database that CORRESPONDS to the fields that comprise the "business key" (described below)
	that are used to establish natural identity.
	
	e.g. For class ABC, assume there is an ID field that corresponds to the PK (which is null for new instances)
	If there exists a field, such as NAME, that is considered to be a "business key" (a.k.a. natural identity), then it is 
	assumed that in the database that there's a UNIQUE constraint defined on the corresponding NAME column for the ABC table. 
 * </pre>
 * The term "business key" that the Hibernate designers use is what I call "natural identity" here...
 * <p>
 * Other useful links:<br>
 * See: https://www.owasp.org/index.php/Hibernate-Guidelines#Identify_natural_keys
 * <p>
 * See: https://community.jboss.org/wiki/EqualsAndHashCode
 * <p>
 * See: http://stackoverflow.com/questions/2719877/object-equality-in-context-of-hibernate-webapp
 * <p>
 * See: http://www.ericfeminella.com/blog/2010/12/05/domain-models-and-value-objects/
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 */
public abstract class AbstractEntity implements Serializable, Comparable<AbstractEntity> {
	
	/* */
	private static final long serialVersionUID = 1L;
	
	/** Used when optimistic locking is employed. */
	public static final int INITIAL_VERSION_NUMBER = 1;

	/** Used to make natural identifiers more human readable. */
	public static final String NATURAL_IDENTITY_DELIMITER = "_";


	/** For general use. */
	public static final Integer ZERO = Integer.valueOf(0);
	public static final Integer ONE = Integer.valueOf(1);
	public static final Integer ONE_HUNDRED = Integer.valueOf(100);
	public static final Integer ONE_HUNDRED_EIGHTY = Integer.valueOf(180);
	public static final Integer ONE_THOUSAND = Integer.valueOf(1000);
	public static final Integer TEN_THOUSAND = Integer.valueOf(10000);
	public static final Integer ONE_HUNDRED_THOUSAND = Integer.valueOf(100000);
	public static final Integer NINETEEN_FIFTY = Integer.valueOf(1950);
	public static final Integer TWENTY_FIFTY = Integer.valueOf(2050);


	/*
	 * Used to manage the current time.  In a test context, the "current time" may be set to be a 
	 * static point in time, either in the past or in the future.  This is to facilitate testing 
	 * operations/events that are time-dependent.
	 * <p>
	 * In a production context, the time keeper will always delegate to System.getCurrentTimeMillis() 
	 * when asked what the current date is. 
	 */
	private static TimeKeeper TIME_KEEPER = new TimeKeeperImpl();
					
	/**
	 * Used for retrieving the time and time related functionality.  In PROD, time is retrieved from the 
	 * application container.  In lower environments, a test timekeeper can be employed that has static
	 * time and can allow for "time travel" to test events that are initiated via the passage of time.
	 * 
	 * @return
	 */
	public static TimeKeeper getTimeKeeper() {
		return TIME_KEEPER;
	}
	
	/**
	 * 
	 * @param timeKeeper
	 */
	public static final void setTimeKeeper(TimeKeeper timeKeeper) {
		//LOGGER.debug("Setting timeKeeper to: {} with current date: {}", timeKeeper.getClass().getSimpleName(), timeKeeper.getCurrentTime());
		TIME_KEEPER = timeKeeper;
	}

	/*
	 * a.k.a. "surrogate key" or "artificial key", which is used to uniquely identify a domain object when persisted to the repository.
	 * This identity can be null (i.e. for newly created domain objects that have not been persisted yet.  In this case, an object is
	 * identified by its "natural identity" (see abstract method below).  This "natural identity is also known as the "business identity
	 * and refers to the unique identity of an object/entity consisting of its intrinsic attributes (e.g. a citizen of the U.S. can be
	 * identified naturally by their social security number.
	 */
	protected Long persistentIdentity;


	/* Used when optimistic locking is employed. */
	private Integer version = Integer.valueOf(INITIAL_VERSION_NUMBER);


	// NOTE: It is assumed that there will be infrastructure aspect/interceptor that will be able to use
	// reflection to set the values of the "audit" fields below (and thus no need to expose setters)

	/* Used for auditing */
	private String createdBy;

	/* Used for auditing */
	private Timestamp createdByTimestamp;

	/* Used for auditing */
	private String lastModifiedBy;

	/* Used for auditing */
	private Timestamp lastModifiedByTimestamp;
	
	/* Used for "soft deletion" */
	private Boolean isDeleted = Boolean.FALSE;
	
	/* Used for "soft deletion".  When isDeleted is set to TRUE, this should be set to the current timestamp, in order to facilitate time-based archival */
	private Timestamp deletionDate;

    /**
     * Presumably, all mutations to any entity would be tracked through domain events, ordered by time, and like a database log, would
     * serve as a natural audit trail to show who/what/when types of actions
     */
	private Set<AbstractEvent> domainEvents = new TreeSet<>();
	
	/**
	 * 
	 */
	public AbstractEntity() {
		super();
		this.createdBy = "";
		this.createdByTimestamp = new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis());
		this.lastModifiedBy = "";
		this.lastModifiedByTimestamp = new Timestamp(AbstractEntity.getTimeKeeper().getCurrentTimeInMillis());
	}

	/**
	 * Used to facilitate optimistic locking when used with some persistent repository.
	 * 
	 * @return version
	 */
	public Integer getVersion() {
		
		return this.version;
	}
	
	/**
	 * An object's <code>naturalIdentity</code> serves to uniquely identify a domain object
	 * instance using intrinsic attributes of the object itself.  It should be noted that 
	 * these attributes should be immutable, as changing these attributes constitutes changing
	 * the identity of the object and allowing this would allow possible collisions when 
	 * dealing with identity (i.e. equals(), hashCode() and compareTo() all depend upon this notion.
	 * 
	 * @return The instance's natural identity
	 */
	public abstract String getNaturalIdentity();

	/**
	 * Serves to provide a unique artificial identity of a domain object instance that,
	 * when non-null, represents the notion that the domain object has been
	 * stored (a.k.a. persisted or saved) in some domain object repository
	 * (e.g. database via ORM or content repository).
	 * <p>
	 * When a domain object hasn't been persisted yet (i.e. Hibernate ORM refers to this
	 * as the "transient" lifecycle state), then in order to determine unique identity,
	 * it is necessary to use the domain object's <code>naturalIdentity</code>.
	 * <p>
	 *
	 * @return The persisted identifier for this instance
	 */
	public Long getPersistentIdentity() {
		return this.persistentIdentity;
	}

	/**
	 * 
	 * Validates the domain object's state and adds zero or more validation warning messages to the given <code>validationMessages</code> list.
	 * 
	 * @param validationMessages
	 */
	public abstract void validate(List<String> validationMessages);
	
	/**
	 * 
	 * @return
	 */
	public boolean isValid() {
		
		if (validate().isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Throws a ValidationException if the entity is in an invalid state, as determined by validate()
	 *
	 * @throws ValidationException
	 */
	public void assertValid() throws ValidationException {

		List<String> validationMessages = this.validate();
		if (!validationMessages.isEmpty()) {

			StringBuilder sb = new StringBuilder();
			sb.append(this.getClassAndIdentity());
			sb.append(" is in an invalid state, validationMessages: ");
			Iterator<String> iterator = validationMessages.iterator();
			while (iterator.hasNext()) {
				String validationMessage = iterator.next();
				sb.append(validationMessage);
			}
			throw new ValidationException(this.getClassAndIdentity(), sb.toString());
		}
	}

	/**
	 * 
	 * Validates the domain object's state and adds zero or more validation warning messages to a new <code>validationMessages</code> list.
	 */
	public List<String> validate() {
		
		List<String> validationMessages = new ArrayList<String>();
		validate(validationMessages);
		return validationMessages;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getValidationMessages() {
		
		return this.validate();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AbstractEntity that) {
		
		return this.getNaturalIdentity().compareTo(((AbstractEntity)that).getNaturalIdentity());	
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {

		if (this.getPersistentIdentity() != null) {
			return this.getPersistentIdentity().hashCode();
		}

		return this.getNaturalIdentity().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

		if (this.getPersistentIdentity() != null && ((AbstractEntity)that).getPersistentIdentity() != null) {
			return this.getPersistentIdentity().equals(((AbstractEntity)that).getPersistentIdentity());
		}

		return this.getNaturalIdentity().equals(((AbstractEntity)that).getNaturalIdentity());
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.getNaturalIdentity();
	}

	/**
	 * @return the domainEvents
	 */
	public Set<AbstractEvent> getDomainEvents() {
		return domainEvents;
	}

	// NOTE: It is assumed that there will be infrastructure aspect/interceptor that will be able to use
	// reflection to set the values of the "audit" fields below (and thus no need to expose setters)

	/* Used for auditing */
	public String getCreatedBy() {
		return this.createdBy;
	}

	/* Used for auditing */
	public Timestamp getCreatedByTimestamp() {
		return new Timestamp(this.createdByTimestamp.getTime());
	}

	/* Used for auditing */
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	/* Used for auditing */
	public Timestamp getLastModifiedByTimestamp() {
		return new Timestamp(this.lastModifiedByTimestamp.getTime());
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
		if (isDeleted.equals(Boolean.TRUE)) {
			this.deletionDate = AbstractEntity.getTimeKeeper().getCurrentTimestamp();
		} else {
			this.deletionDate = null;
		}
	}

	public Timestamp getDeletionDate() {
		return new Timestamp(deletionDate.getTime());
	}

	/**
	 *
	 * @return
	 */
	public String getClassAndIdentity() {

		StringBuilder sb = new StringBuilder();
		sb.append(this.getClass().getSimpleName());
		sb.append("[");
		sb.append(this);
		if (this.persistentIdentity != null) {
			sb.append("] with persistentIdentity: [");
			sb.append(this.persistentIdentity);
		}
		sb.append("]: ");
		return sb.toString();
	}

    /**
     * Convenience method to reduce duplicate code.
     *
     * @param naturalIdentityComponents
     * @return
     */
	public static String buildNaturalIdentity(Object... naturalIdentityComponents) {

        StringBuilder sb = new StringBuilder();
	    for (int i=0; i < naturalIdentityComponents.length; i++) {

            sb.append(naturalIdentityComponents[i]);
			if ((i+1) < naturalIdentityComponents.length) {
                sb.append(NATURAL_IDENTITY_DELIMITER);
            }
        }
        return sb.toString();
    }

	/*
	 * 
	 * @param validationMessages
	 * @param attributeName
	 * @param attributeValue
	 */
	protected void validateNotNull(List<String> validationMessages, String attributeName, Object attributeValue) {

		validateNotNullWithMessage(validationMessages, attributeValue, attributeName + " must be specified.");
	}

	/*
	 *
	 * @param validationMessages
	 * @param attributeName
	 * @param attributeValue
	 * @param message
	 */
	protected void validateNotNullWithMessage(List<String> validationMessages, Object attributeValue, String message) {

		if (attributeValue == null || attributeValue.toString().trim().isEmpty()) {
			validationMessages.add(this.getClassAndIdentity() + " " + message);
		}
	}

	/*
	 * 
	 * @param validationMessages
	 * @param attributeName
	 * @param attributeValue
	 */
	protected void validateDate(List<String> validationMessages, String attributeName, Timestamp timestampValue) {

		if (timestampValue == null || timestampValue.toString().trim().isEmpty()) {
			validationMessages.add(this.getClassAndIdentity() + " " + attributeName + " must be specified and be between 1950-2050.");
		}
	}

	/*
	 *
	 * @param validationMessages
	 * @param attributeName
	 * @param attributeValue
	 */
	protected void validateNumber(List<String> validationMessages, String attributeName, Number numericValue, Number lowerBound) {

		if (numericValue == null || numericValue.longValue() < lowerBound.longValue()) {
			validationMessages.add(this.getClassAndIdentity() + " " + attributeName + " must be specified and be greater than [" + lowerBound + "].");
		}
	}
	
	/*
	 *
	 * @param validationMessages
	 * @param attributeName
	 * @param attributeValue
	 */
	protected void validateNumericRange(List<String> validationMessages, String attributeName, Number numericValue, Number lowerBound, Number upperBound) {

		if (numericValue == null || numericValue.longValue() < lowerBound.longValue() || numericValue.longValue() > upperBound.longValue()) {
			validationMessages.add(this.getClassAndIdentity() + " " + attributeName + " must be specified and be between [" + lowerBound + "] and [" + upperBound + "].");
		}
	}
}
