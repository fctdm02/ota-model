/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.tmc.bytestream.mapper;

import com.djt.cvpp.ota.common.mapper.DtoMapper;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class TmcBinaryDtoMapper implements DtoMapper<com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary, com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary> {

	public com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary mapEntityToDto(com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary tmcBinaryEntity) {
		
		com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary tmcBinaryDto = new com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary();
		tmcBinaryDto.setBinaryId(tmcBinaryEntity.getBinaryId());
		tmcBinaryDto.setDescription(tmcBinaryEntity.getDescription());
		tmcBinaryDto.setByteArray(tmcBinaryEntity.getByteArray());
		
		return tmcBinaryDto;
	}
	
	public com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary mapDtoToEntity(com.djt.cvpp.ota.tmc.bytestream.mapper.dto.TmcBinary tmcBinaryDto) {
		
		com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary tmcBinaryEntity = new com.djt.cvpp.ota.tmc.bytestream.model.TmcBinary(
			tmcBinaryDto.getBinaryId(),
			tmcBinaryDto.getDescription(),
			tmcBinaryDto.getByteArray());
		
		return tmcBinaryEntity;		
	}
}
