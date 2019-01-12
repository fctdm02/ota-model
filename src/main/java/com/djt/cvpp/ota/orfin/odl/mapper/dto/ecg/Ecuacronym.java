/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.odl.mapper.dto.ecg;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Ecuacronym {

    private Gateway gateway;
    private List<Did> optimizedDID = new ArrayList<>();
    private Object ignoreDIDs;
    private String acronym;
    private Integer diagnosticSpecificationResponse;
    private String hasConditionBasedOnDTC;
}
