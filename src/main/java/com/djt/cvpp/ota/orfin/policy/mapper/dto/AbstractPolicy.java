/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.mapper.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class AbstractPolicy {

}
