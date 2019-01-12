/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.deployment.client;

import com.djt.cvpp.ota.common.exception.EntityDoesNotExistException;
import com.djt.cvpp.ota.common.exception.ValidationException;
import com.djt.cvpp.ota.flare.rollout.model.CampaignBatch;
import com.djt.cvpp.ota.flare.rollout.model.VehicleCampaignBatch;
import com.djt.cvpp.ota.tmc.deployment.exception.TmcDeploymentException;
import com.djt.cvpp.ota.tmc.deployment.mapper.DeploymentDtoMapper;
import com.djt.cvpp.ota.tmc.deployment.mapper.DeploymentJsonConverter;
import com.djt.cvpp.ota.tmc.deployment.model.Deployment;
import com.djt.cvpp.ota.tmc.deployment.model.Release;
import com.djt.cvpp.ota.tmc.deployment.model.ReleaseArtifact;
import com.djt.cvpp.ota.tmc.deployment.model.TargetBundle;

/**
 *
<pre>
HttpClientService.sendDeployRequest():
{"tenant_id":"ed67d6c9-3f14-4e7d-8d08-94265a53f326","name":"1FA6P8TH4J5107939_3-1","description":"manifest","author":{"id":"e100a463-54d1-4b56-95bc-81a77b725018","tenant_id":"ed67d6c9-3f14-4e7d-8d08-94265a53f326","name":"TMC Communicator","description":"microservice","type":"microservice"},"uri":"aui:bytestream::/M_3-1","type":"manifest","checksum":[],"byte_length":1,"tags":{}}


HttpClientService.query():
https://api.autonomic.ai/1/resources/ed67d6c9-3f14-4e7d-8d08-94265a53f326/vehicles?q=%28model+IN+%28%22Mustang%22%29+AND+year%3D%3D2018%29



QueryBuilderService.constructQueryForVehicleDiscoveryFromJson():
			{"queries":[{"programCode":"Mustang","nodes":[{"name":"ECG","address":"716","partnumber":"GB5T-10GREF-INS9"},{"name":"APIM","address":"7D0","partnumber":"HN1T-14G380-BA"}]}]}
			{"queries":[{"programCode":"Mustang","modelYear":2018,"nodes":[{"name":"ECG","address":"716","partnumber":"GB5T-10GREF-INS9"},{"name":"APIM","address":"7D0","partnumber":"HN1T-14G380-BA"}]}]}
			{"queries":[{"nodes":[{"name":"ECG","address":"716","partnumber":"GB5T-10GREF-INS9"},{"name":"APIM","address":"7D0","partnumber":"HN1T-14G380-BA"}]}]}
			{"programCode":"Mustang","modelYear":2018,"targetNodeName":"ECG","targetNodePartnumber":"GB5T-10GREF-INS9","vin":""}
			{"queries":[{"programCode":"","modelYear":0,"vin":"","esn":"","nodes":[{"name":"","address":"","partnumber":""}]}]}
queries:	{"queries":[{"vin":"1FA6P8TH4J5107939","nodes":[{"name":"ECG","address":"716","partnumber":"GB5T-10GREF-INS9"},{"name":"APIM","address":"7D0","partnumber":"HN1T-14G380-BA"}]}]}
queryUrl: https://api.autonomic.ai/1/resources/ed67d6c9-3f14-4e7d-8d08-94265a53f326/vehicles?q=
https://api.autonomic.ai/1/resources/ed67d6c9-3f14-4e7d-8d08-94265a53f326/vehicles?q=%28vin%3D%3D%221FA6P8TH4J5107939%22%29



DeploymentService.createBundleJson():
{"tenant_id":"ed67d6c9-3f14-4e7d-8d08-94265a53f326","name":"1FA6P8TH4J5107939_3-1","description":"bundle","author":{"id":"e100a463-54d1-4b56-95bc-81a77b725018","tenant_id":"ed67d6c9-3f14-4e7d-8d08-94265a53f326","name":"TMC Communicator","description":"microservice","type":"microservice"},"artifact_ids":["3a6fda3e-fa7b-4f85-a53b-54a9761990e4","b77733a3-335e-448d-a9af-d781ccf1330e"],"matchers":[{"name":"vin","value":"1FA6P8TH4J5107939","type":"Field"}],"tags":{}}



CommandControlService.buildUrlForVin():
URL: https://api.autonomic.ai/v1/deployment-group/ed67d6c9-3f14-4e7d-8d08-94265a53f326/vehicle/1FA6P8TH4J5107939/actuation
REQUEST BODY: {"oem_correlation_id":"407d9470-3bcc-45c3-ab9f-7c016c18fc8d","actuation_type":"INVENTORY"}
NOTE: oem_correlation_id corresponds to the TMC Deployment ID


Send cancellation request to TMC:
https://api.autonomic.ai/1/deploy/ed67d6c9-3f14-4e7d-8d08-94265a53f326/deploys/67b8a88f-abb2-4bcf-b529-a230fe93effa/cancel

</pre>
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public interface TmcDeploymentClient {
	
	/** Used for unique identification of exceptions thrown */
	String BOUNDED_CONTEXT_NAME = "TMC";
	
	/** Used for unique identification of exceptions thrown */
	String SERVICE_NAME = "DEPLOYMENT";
	
	/**
	 * 
	 * @param campaignBatch
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 * @throws TmcDeploymentException
	 */
	Deployment pushDeploymentToTmc(CampaignBatch campaignBatch)
	throws
		EntityDoesNotExistException,
		ValidationException,
		TmcDeploymentException;	

	/**
	 * 
	 * @param vehicleCampaignBatch
	 * @throws EntityDoesNotExistException
	 * @throws ValidationException
	 * @throws TmcDeploymentException
	 */
	void sendVehicleDeploymentTriggerToTmc(VehicleCampaignBatch vehicleCampaignBatch) 
	throws
		EntityDoesNotExistException,
		ValidationException,
		TmcDeploymentException;	
	
	/**
	 * 
	 * https://api.autonomic.ai/1/deploy/ed67d6c9-3f14-4e7d-8d08-94265a53f326/deploys/67b8a88f-abb2-4bcf-b529-a230fe93effa/cancel
	 * 
	 * @param deployment
	 * @return
	 * @throws TmcDeploymentException
	 */
	String sendCancelDeploymentRequestToTmc(Deployment deployment) throws TmcDeploymentException;
	
	/**
	 * 
	 * @param requestJson
	 * @return
	 * @throws TmcDeploymentException
	 */
	ReleaseArtifact createTmcReleaseArtifact(String requestJson) throws TmcDeploymentException;
	
	/**
	 * 
	 * @param requestJson
	 * @return
	 * @throws TmcDeploymentException
	 */
	TargetBundle createTmcTargetBundle(String requestJson) throws TmcDeploymentException;

	/**
	 * 
	 * @param requestJson
	 * @return
	 * @throws TmcDeploymentException
	 */
	Release createTmcRelease(String requestJson) throws TmcDeploymentException;
	
	/**
	 * 
	 * @param requestJson
	 * @return
	 * @throws TmcDeploymentException
	 */
	Deployment createTmcDeployment(String requestJson) throws TmcDeploymentException;
	
	/**
	 * 
	 * @return
	 */
	DeploymentJsonConverter getDeploymentJsonConverter();

	/**
	 * 
	 * @return
	 */
	DeploymentDtoMapper getDeploymentDtoMapper();
}
