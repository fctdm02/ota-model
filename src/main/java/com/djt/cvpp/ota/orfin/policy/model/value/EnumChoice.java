/*
 * 
 * 
 * 
 * 
 * 
 * 
 */
package com.djt.cvpp.ota.orfin.policy.model.value;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class EnumChoice extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	private String choiceName;
	private Set<EnumValue> enumValues = new TreeSet<>();
	
	public EnumChoice(
		String choiceName,
		Set<EnumValue> enumValues) {
		this.choiceName = choiceName;
		this.enumValues = enumValues;
	}
	
	public String getNaturalIdentity() {
		return choiceName;
	}

	public void validate(List<String> validationMessages) {

		validateNotNull(validationMessages, "choiceName", choiceName);
		
		if (enumValues == null || enumValues.isEmpty()) {
			validationMessages.add(this.getClassAndIdentity() + " at least one EnumValue must be specified.");
		} else {
			Iterator<EnumValue> iterator = enumValues.iterator();
			while (iterator.hasNext()) {

				EnumValue enumValue = iterator.next();
				enumValue.validate(validationMessages);
			}
		}
	}
	
	public List<EnumValue> getEnumValues() {
		List<EnumValue> list = new ArrayList<>();
		list.addAll(enumValues);
		return list;
	}
}
