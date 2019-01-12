/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.vadrevent.mapper;

import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VadrReleaseEventDtoMapper implements DtoMapper<com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease, com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease> {

	public com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease mapEntityToDto(com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease vadrReleaseEntity) {

		com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease vadrReleaseDto = new com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease();
		
		vadrReleaseDto.setDomainName(vadrReleaseEntity.getDomainName());
		vadrReleaseDto.setDomainInstanceName(vadrReleaseEntity.getDomainInstanceName());
		vadrReleaseDto.setDomainInstanceDescription(vadrReleaseEntity.getDomainInstanceDescription());
		vadrReleaseDto.setDomainInstanceVersion(vadrReleaseEntity.getDomainInstanceVersion());
		vadrReleaseDto.setAppId(vadrReleaseEntity.getAppId());
		vadrReleaseDto.setAppVersion(vadrReleaseEntity.getAppVersion());
		vadrReleaseDto.setProductionState(vadrReleaseEntity.getProductionState());
		vadrReleaseDto.setSoftwarePriorityLevel(vadrReleaseEntity.getSoftwarePriorityLevel());
		vadrReleaseDto.setReleaseDate(vadrReleaseEntity.getReleaseDate());
		
		return vadrReleaseDto;
	}
	
	public com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease mapDtoToEntity(com.djt.cvpp.ota.orfin.vadrevent.mapper.dto.VadrRelease vadrReleaseDto) {

		return new com.djt.cvpp.ota.orfin.vadrevent.model.VadrRelease
			.VadrReleaseBuilder()
			.withDomainName(vadrReleaseDto.getDomainName())
			.withDomainInstanceName(vadrReleaseDto.getDomainInstanceName())
			.withDomainInstanceDescription(vadrReleaseDto.getDomainInstanceDescription())
			.withDomainInstanceVersion(vadrReleaseDto.getDomainInstanceVersion())
			.withAppId(vadrReleaseDto.getAppId())
			.withAppVersion(vadrReleaseDto.getAppVersion())
			.withProductionState(vadrReleaseDto.getProductionState())
			.withSoftwarePriorityLevel(vadrReleaseDto.getSoftwarePriorityLevel())
			.withReleaseDate(vadrReleaseDto.getReleaseDate())
			.build();
	}
}
