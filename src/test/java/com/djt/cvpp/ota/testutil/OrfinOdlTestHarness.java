package com.djt.cvpp.ota.testutil;

import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.orfin.odl.model.Did;
import com.djt.cvpp.ota.orfin.odl.model.Network;
import com.djt.cvpp.ota.orfin.odl.model.Node;
import com.djt.cvpp.ota.orfin.odl.model.Odl;
import com.djt.cvpp.ota.orfin.odl.model.enums.SpecificationCategoryType;
import com.djt.cvpp.ota.orfin.program.model.ModelYear;
import com.djt.cvpp.ota.orfin.program.model.Program;
import com.djt.cvpp.ota.orfin.program.model.ProgramModelYear;

public class OrfinOdlTestHarness {
	
	public Odl buildOdl() throws ValidationException {
		
		Set<Network> networks = new TreeSet<>();
		Network networkEntity = new Network
			.NetworkBuilder()
			.withNetworkName("networkName1")
			.withDataRate("dataRate1")
			.withDclName("dclName1")
			.withNetworkPins("pins1")
			.withProtocol("protocol1")
			.withNodes(buildNodes(SpecificationCategoryType.GGDS))
			.build();
		networks.add(networkEntity);

		networkEntity = new Network
			.NetworkBuilder()
			.withNetworkName("networkName2")
			.withDataRate("dataRate2")
			.withDclName("dclName2")
			.withNetworkPins("pins2")
			.withProtocol("protocol2")
			.withNodes(buildNodes(SpecificationCategoryType.PART2_SPEC))
			.build();
		networks.add(networkEntity);
		
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

		Odl odlEntity = new Odl
			.OdlBuilder()
			.withOdlName("odllName1")
			.withNetworks(networks)
			.withProgramModelYears(programModelYears)
			.build();
		
		odlEntity.assertValid();
		
		return odlEntity;		
	}
	
	public Set<Node> buildNodes(SpecificationCategoryType specificationCategoryType) {
		
		Set<String> ignoredDids = new TreeSet<>();
		Set<Node> nodes = new TreeSet<>();
		for (int i=0; i < 2; i++) {
			Node node = new Node
				.NodeBuilder()
				.withAcronym("acronym" + i)
				.withAddress("address" + i)
				.withActivationTime(Integer.valueOf(15))
				.withGatewayNodeId("gatewayNodeId" + i)
				.withGatewayType("gatewayType" + i)
				.withHasConditionBasedOnDtc(Boolean.TRUE)
				.withIsOvtp(Boolean.TRUE)
				.withOvtpDestinationAddress("ovtpDestinationAddress" + i)
				.withSpecificationCategoryType(specificationCategoryType)
				.withIgnoredDids(ignoredDids)
				.withDids(buildDids())
				.build();
			nodes.add(node);
		}
		
		return nodes;
	}
	
	public Set<Did> buildDids() {
		
		Set<Did> dids = new TreeSet<>();
		for (int i=0; i < 2; i++) {
			Did did = new Did
				.DidBuilder()
				.withDidName("didName" + i)
				.withDescription("description" + i)
				.withDirectConfigurationDidFlag(Boolean.TRUE)
				.withPrivateNetworkDidFlag(Boolean.TRUE)
				.withVinSpecificDidFlag(Boolean.TRUE)
				.build();
			dids.add(did);
		}
		
		return dids;
	}
}
