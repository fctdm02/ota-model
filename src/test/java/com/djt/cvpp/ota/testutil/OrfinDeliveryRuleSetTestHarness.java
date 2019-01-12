package com.djt.cvpp.ota.testutil;

import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.orfin.delivery.model.ComplexCondition;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRule;
import com.djt.cvpp.ota.orfin.delivery.model.DeliveryRuleSet;
import com.djt.cvpp.ota.orfin.delivery.model.enums.ConnectionType;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryAudience;
import com.djt.cvpp.ota.orfin.delivery.model.enums.DeliveryMethod;
import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;
import com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease;

public class OrfinDeliveryRuleSetTestHarness {
	
	public DeliveryRuleSet buildDeliveryRuleSet() throws ValidationException, EntityDoesNotExistException {
		
		Set<ComplexCondition> complexConditions = new TreeSet<>();
		ComplexCondition deliveryRuleEntity = new ComplexCondition
			.ComplexConditionBuilder()
			.withComplexConditionName("complexConditionName1")
			.withComplexConditionValue("complexConditionValue1")
			.build();
		complexConditions.add(deliveryRuleEntity);

		deliveryRuleEntity = new ComplexCondition
			.ComplexConditionBuilder()
			.withComplexConditionName("complexConditionName2")
			.withComplexConditionValue("complexConditionValue2")
			.build();
		complexConditions.add(deliveryRuleEntity);
		

		
		Set<DeliveryRule> deliveryRules = new TreeSet<>();
		DeliveryRule deliveryRuleSetChannelEntity = new DeliveryRule
			.DeliveryRuleBuilder()
			.withDeliveryAudience(DeliveryAudience.CONSUMER)
			.withDeliveryMethod(DeliveryMethod.OTA_ECG)
			.withConnectionType(ConnectionType.CELLULAR)
			.withAllowable(Boolean.TRUE)
			.withPrecedenceLevel(Integer.valueOf(1))
			.build();
		deliveryRules.add(deliveryRuleSetChannelEntity);
		
		deliveryRuleSetChannelEntity = new DeliveryRule
			.DeliveryRuleBuilder()
			.withDeliveryAudience(DeliveryAudience.SERVICE)
			.withDeliveryMethod(DeliveryMethod.TOOL)
			.withConnectionType(ConnectionType.USB)
			.withAllowable(Boolean.TRUE)
			.withPrecedenceLevel(Integer.valueOf(2))
			.build();
		deliveryRules.add(deliveryRuleSetChannelEntity);

				
		Set<ProgramModelYear> programModelYears = new TreeSet<>();
		Program programEntity = new Program.ProgramBuilder()
			.withProgramCode("C344N")
			.build();  
		
		ModelYear modelYearEntity = new ModelYear
			.ModelYearBuilder()
			.withModelYearValue(Integer.valueOf("2017"))
			.build();
		
		ProgramModelYear programModelYearEntity = new ProgramModelYear
			.ProgramModelYearBuilder()
			.withParentProgram(programEntity)
			.withParentModelYear(modelYearEntity)
			.build();
		programModelYears.add(programModelYearEntity);

		
		String domainName = "MayHackathon-ECG";		
		String domainInstanceName = "ECG_Hack_02";
		String domainInstanceDescription = "";
		String domainInstanceVersion = "01.01.02";
		String appId = null;
		String appVersion = null;
		String productionState = "PRODUCTION";
		
		Set<VadrRelease> vadrReleases = new TreeSet<>();
		vadrReleases.add(new VadrRelease
			.VadrReleaseBuilder()
			.withDomainName(domainName)
			.withDomainInstanceName(domainInstanceName)
			.withDomainInstanceDescription(domainInstanceDescription)
			.withDomainInstanceVersion(domainInstanceVersion)
			.withAppId(appId)
			.withAppVersion(appVersion)
			.withProductionState(productionState)
			.withReleaseDate(AbstractEntity.getTimeKeeper().getCurrentTimestamp().toString())
			.build());
		
		DeliveryRuleSet deliveryRuleSetEntity = new DeliveryRuleSet
			.DeliveryRuleSetBuilder()
			.withDeliveryRuleSetName("deliveryRuleSetName1")
			.withAuthorizedBy("tmyers28")
			.withMessageToConsumer("message to consumer")
			.withConsentType(com.djt.cvpp.ota.orfin.delivery.model.enums.ConsentType.SAFETY_UPDATE_NOTICE)
			.withScheduledRolloutDate(null)
			.withComplexConditions(complexConditions)
			.withDeliveryRules(deliveryRules)
			.withProgramModelYears(programModelYears)
			.withVadrReleases(vadrReleases)
			.build();
		
		deliveryRuleSetEntity.assertValid();
		
		return deliveryRuleSetEntity;		
	}
}
