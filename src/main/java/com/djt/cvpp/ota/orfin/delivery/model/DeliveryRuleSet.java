/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.delivery.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityAlreadyExistsException;
import com.djt.cvpp.ota.common.exception.FenixRuntimeException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class DeliveryRuleSet extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_DELIVERY_RULE_SET_NAME = "DEFAULT";

	
	private String deliveryRuleSetName;
	private String authorizedBy;
	private String messageToConsumer;
	private ConsentType consentType;
	private Timestamp scheduledRolloutDate; // Cannot be set on the default delivery rule set
	private Set<DeliveryRule> deliveryRules = new TreeSet<>();
	private Set<ProgramModelYear> programModelYears = new TreeSet<>(); // Associated ProgramModelYears are treated as "inclusions" in the vehicle discovery process.  If empty, include all.  If non-empty, only include the ones specified.
	private Set<ComplexCondition> complexConditions = new TreeSet<>(); // These correspond to current state/IVS "service actions" and serve as a placeholder.
	private Set<VadrRelease> vadrReleases = new TreeSet<>();
	
	private DeliveryRuleSet(DeliveryRuleSetBuilder builder) {
		
		this.deliveryRuleSetName = builder.deliveryRuleSetName;
		this.authorizedBy = builder.authorizedBy;
		this.messageToConsumer = builder.messageToConsumer;
		this.consentType = builder.consentType;
		this.scheduledRolloutDate = builder.scheduledRolloutDate; 
		this.deliveryRules = builder.deliveryRules;
		this.programModelYears = builder.programModelYears;
				
		this.deliveryRules = builder.deliveryRules;
		if (this.deliveryRules != null) {
			Iterator<DeliveryRule> iterator = deliveryRules.iterator();
			while (iterator.hasNext()) {

				DeliveryRule deliveryRule = iterator.next();
				deliveryRule.setParentDeliveryRuleSet(this);
			}
		}
		
		this.programModelYears = builder.programModelYears;
		if (this.programModelYears != null) {
			Iterator<ProgramModelYear> iterator = programModelYears.iterator();
			while (iterator.hasNext()) {

				ProgramModelYear programModelYear = iterator.next();
				programModelYear.addDeliveryRuleSet(this);
			}
		}
		
		this.complexConditions = builder.complexConditions;
		if (this.complexConditions != null) {
			Iterator<ComplexCondition> iterator = complexConditions.iterator();
			while (iterator.hasNext()) {

				ComplexCondition complexCondition = iterator.next();
				complexCondition.setParentDeliveryRuleSet(this);
			}
		}
		
		this.vadrReleases = builder.vadrReleases;
		if (this.vadrReleases != null) {
			Iterator<VadrRelease> iterator = vadrReleases.iterator();
			while (iterator.hasNext()) {

				VadrRelease vadrRelease = iterator.next();
				vadrRelease.setParentDeliveryRuleSet(this);
			}
		}
	}
	
	public String getNaturalIdentity() {

		return this.deliveryRuleSetName;
	}

	public void validate(List<String> validationMessages) {

		if (deliveryRuleSetName == null || deliveryRuleSetName.trim().isEmpty()) {
			validationMessages.add("deliveryRuleSetName must be specified.");
		}

		if (deliveryRules == null || deliveryRules.isEmpty()) {
			validationMessages.add("At least one ComplexCondition must be specified.");
		} else {
			Iterator<DeliveryRule> deliveryRuleSetChannelsIterator = deliveryRules.iterator();
			while (deliveryRuleSetChannelsIterator.hasNext()) {

				DeliveryRule deliveryRule = deliveryRuleSetChannelsIterator.next();
				deliveryRule.validate(validationMessages);
			}
		}
	}

	public String getDeliveryRuleSetName() {
		return deliveryRuleSetName;
	}

	public String getAuthorizedBy() {
		return authorizedBy;
	}

	public List<DeliveryRule> getDeliveryRules() {
		List<DeliveryRule> list = new ArrayList<>();
		list.addAll(this.deliveryRules);
		return list;
	}
	
	public boolean addDeliveryRule(DeliveryRule deliveryRule) throws EntityAlreadyExistsException {
		
		if (this.deliveryRules.contains(deliveryRule)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has deliveryRule: [" + deliveryRule + "].");
		}
		deliveryRule.setParentDeliveryRuleSet(this);
		return this.deliveryRules.add(deliveryRule);
	}

	public List<ProgramModelYear> getProgramModelYears() {
		List<ProgramModelYear> list = new ArrayList<>();
		list.addAll(this.programModelYears);
		return list;
	}

	public List<ComplexCondition> getComplexConditions() {
		List<ComplexCondition> list = new ArrayList<>();
		list.addAll(this.complexConditions);
		return list;
	}

	public boolean addComplexCondition(ComplexCondition complexCondition) throws EntityAlreadyExistsException {
		
		if (this.complexConditions.contains(complexCondition)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has complexCondition: [" + complexCondition + "].");
		}
		complexCondition.setParentDeliveryRuleSet(this);
		return this.complexConditions.add(complexCondition);
	}

	public List<VadrRelease> getVadrReleases() {
		
		List<VadrRelease> list = new ArrayList<>();
		list.addAll(vadrReleases);
		return list;
	}

	public boolean addVadrRelease(VadrRelease vadrRelease) throws EntityAlreadyExistsException {
		
		if (this.vadrReleases.contains(vadrRelease)) {
			throw new EntityAlreadyExistsException(this.getClassAndIdentity() + " already has vadrRelease: [" + vadrRelease + "].");
		}
		vadrRelease.setParentDeliveryRuleSet(this);
		return this.vadrReleases.add(vadrRelease);
	}
	
	public boolean addProgramModelYear(ProgramModelYear programModelYear) {
		
		if (this.programModelYears.contains(programModelYear)) {
			throw new FenixRuntimeException(this.getClassAndIdentity() + " already has programModelYear: [" + programModelYear + "].");
		}
		programModelYear.addDeliveryRuleSet(this);
		return this.programModelYears.add(programModelYear);
	}

	public void removeProgramModelYear(ProgramModelYear programModelYear) {
		
		programModelYear.setPolicySet(null);
		this.programModelYears.remove(programModelYear);
	}
	
	public void setDeliveryRuleSetName(String deliveryRuleSetName) {
		this.deliveryRuleSetName = deliveryRuleSetName;
	}

	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}
	
	public String getMessageToConsumer() {
		return messageToConsumer;
	}

	public void setMessageToConsumer(String messageToConsumer) {
		this.messageToConsumer = messageToConsumer;
	}

	public ConsentType getConsentType() {
		return consentType;
	}

	public void setConsentType(ConsentType consentType) {
		this.consentType = consentType;
	}

	public Timestamp getScheduledRolloutDate() {
		return scheduledRolloutDate;
	}

	public void setScheduledRolloutDate(Timestamp scheduledRolloutDate) {
		this.scheduledRolloutDate = scheduledRolloutDate;
	}

	/**
	 * Inclusions take the form of: XXXX_YYYY where XXXX is the programCode and modelYear is the 4 digit model year
	 * 
	 * @return
	 */
	public Set<String> getProgramCodeModelYearInclusions() {
		
		Set<String> programCodeModelYearInclusions = new HashSet<>(); 					
		Iterator<ProgramModelYear> iterator = this.programModelYears.iterator();
		while (iterator.hasNext()) {
			programCodeModelYearInclusions.add(iterator.next().getNaturalIdentity());
		}
		
		return programCodeModelYearInclusions;
	}

	public static final class DeliveryRuleSetBuilder {

		private String deliveryRuleSetName;
		private String authorizedBy;
		private String messageToConsumer;
		private ConsentType consentType;
		private Timestamp scheduledRolloutDate;
		private Set<DeliveryRule> deliveryRules = new TreeSet<>();
		private Set<ProgramModelYear> programModelYears = new TreeSet<>();
		private Set<ComplexCondition> complexConditions = new TreeSet<>();
		private Set<VadrRelease> vadrReleases = new TreeSet<>();

		public DeliveryRuleSetBuilder() {
		}
		
		public DeliveryRuleSetBuilder withDeliveryRuleSetName(String deliveryRuleSetName) {
			this.deliveryRuleSetName = deliveryRuleSetName;
			return this;
		}

		public DeliveryRuleSetBuilder withAuthorizedBy(String authorizedBy) {
			this.authorizedBy = authorizedBy;
			return this;
		}

		public DeliveryRuleSetBuilder withMessageToConsumer(String messageToConsumer) {
			this.messageToConsumer = messageToConsumer;
			return this;
		}

		public DeliveryRuleSetBuilder withConsentType(ConsentType consentType) {
			this.consentType = consentType;
			return this;
		}

		public DeliveryRuleSetBuilder withScheduledRolloutDate(Timestamp scheduledRolloutDate) {
			this.scheduledRolloutDate = scheduledRolloutDate;
			return this;
		}
		
		public DeliveryRuleSetBuilder withProgramModelYears(Collection<ProgramModelYear> programModelYears) {
			this.programModelYears.addAll(programModelYears);
			return this;
		}

		public DeliveryRuleSetBuilder withDeliveryRules(Collection<DeliveryRule> deliveryRules) {
			this.deliveryRules.addAll(deliveryRules);
			return this;
		}
		
		public DeliveryRuleSetBuilder withComplexConditions(Collection<ComplexCondition> complexConditions) {
			this.complexConditions.addAll(complexConditions);
			return this;
		}
		
		public DeliveryRuleSetBuilder withVadrReleases(Collection<VadrRelease> vadrReleases) {
			this.vadrReleases.addAll(vadrReleases);
			return this;
		}
		
		public DeliveryRuleSet build() {
			return new DeliveryRuleSet(this);
		}
	}
}
