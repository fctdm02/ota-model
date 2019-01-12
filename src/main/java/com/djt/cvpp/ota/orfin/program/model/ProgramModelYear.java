/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.program.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.policy.model.PolicySet;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class ProgramModelYear extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private Program parentProgram;
	private ModelYear parentModelYear;
	
	private Odl odl;
	private PolicySet policySet;
	private Set<DeliveryRuleSet> deliveryRuleSets = new TreeSet<>();
	
	private ProgramModelYear(ProgramModelYearBuilder builder) {
		
		this.parentProgram = builder.parentProgram;
		this.parentModelYear = builder.parentModelYear;
		
		this.odl = builder.odl;
		this.policySet = builder.policySet;
		this.deliveryRuleSets = builder.deliveryRuleSets;
		
		Iterator<DeliveryRuleSet> deliveryRuleSetsIterator = this.deliveryRuleSets.iterator();
		while (deliveryRuleSetsIterator.hasNext()) {
			DeliveryRuleSet deliveryRuleSet = deliveryRuleSetsIterator.next();
			deliveryRuleSet.addProgramModelYear(this);
		}
	}
	
	public String getNaturalIdentity() {
		return AbstractEntity.buildNaturalIdentity(
			parentProgram, 
			parentModelYear);
	}

	public void validate(List<String> validationMessages) {

		if (parentProgram == null) {
			validationMessages.add("parentProgram must be specified.");
		}

		if (parentModelYear == null) {
			validationMessages.add("parentModelYear must be specified.");
		}
				
		if (odl == null) {
			validationMessages.add("odl must be specified.");
		}

		if (policySet == null) {
			validationMessages.add("policySet must be specified.");
		}

		if (deliveryRuleSets == null || deliveryRuleSets.isEmpty()) {
			validationMessages.add("At least one DeliveryRuleSet must be specified.");
		} else {
			Iterator<DeliveryRuleSet> deliveryRuleSetsIterator = this.deliveryRuleSets.iterator();
			while (deliveryRuleSetsIterator.hasNext()) {
				
				DeliveryRuleSet deliveryRuleSet = deliveryRuleSetsIterator.next();
				deliveryRuleSet.validate(validationMessages);
			}
		}
	}

	public Program getParentProgram() {
		return this.parentProgram;
	}

	public ModelYear getParentModelYear() {
		return this.parentModelYear;
	}

	public Odl getOdl() {
		return this.odl;
	}
	
	public void setOdl(Odl odl) {
		this.odl = odl;
	}

	public PolicySet getPolicySet() {
		return this.policySet;
	}

	public void setPolicySet(PolicySet policySet) {
		this.policySet = policySet;
	}
	
	public List<DeliveryRuleSet> getDeliveryRuleSets() {
		
		List<DeliveryRuleSet> list = new ArrayList<>();
		list.addAll(this.deliveryRuleSets);
		return list;
	}
	
	public boolean addDeliveryRuleSet(DeliveryRuleSet deliveryRuleSet) {
		return this.deliveryRuleSets.add(deliveryRuleSet);
	}

	public boolean removeDeliveryRuleSet(DeliveryRuleSet deliveryRuleSet) {
		return this.deliveryRuleSets.remove(deliveryRuleSet);
	}
	
	public boolean addDeliveryRuleSets(Collection<DeliveryRuleSet> deliveryRuleSets) {
		return this.deliveryRuleSets.addAll(deliveryRuleSets);
	}
	
	public static final class ProgramModelYearBuilder {
		
		private Program parentProgram;
		private ModelYear parentModelYear;
		private Odl odl;
		private PolicySet policySet;
		private Set<DeliveryRuleSet> deliveryRuleSets = new TreeSet<>();

		public ProgramModelYearBuilder() {
		}
		
		public ProgramModelYearBuilder withParentProgram(Program parentProgram) {
			this.parentProgram = parentProgram;
			return this;
		}

		public ProgramModelYearBuilder withParentModelYear(ModelYear parentModelYear) {
			this.parentModelYear = parentModelYear;
			return this;
		}
		
		public ProgramModelYearBuilder withOdl(Odl odl) {
			this.odl = odl;
			return this;
		}

		public ProgramModelYearBuilder withPolicySet(PolicySet policySet) {
			this.policySet = policySet;
			return this;
		}
		
		public ProgramModelYearBuilder withDeliveryRuleSets(Set<DeliveryRuleSet> deliveryRuleSets) {
			this.deliveryRuleSets = deliveryRuleSets;
			return this;
		}

		public ProgramModelYear build() {
			return new ProgramModelYear(this);
		}
	}
}
