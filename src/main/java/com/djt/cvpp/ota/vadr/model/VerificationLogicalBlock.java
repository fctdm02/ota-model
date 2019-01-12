/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.util.List;

import com.djt.cvpp.ota.common.model.AbstractEntity;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class VerificationLogicalBlock extends AbstractEntity {

	private static final long serialVersionUID = 1L;


	private BinaryMetadata parentBinaryMetadata;
	private String startAddress;
	private String length;
	private String rootHash;

	private VerificationLogicalBlock() {
	}

	private VerificationLogicalBlock(VerificationLogicalBlockBuilder builder) {

		this.persistentIdentity = builder.persistentIdentity;
		this.parentBinaryMetadata = builder.parentBinaryMetadata;
		this.startAddress = builder.startAddress;
		this.length = builder.length;
		this.rootHash = builder.rootHash;
	}

	public String getNaturalIdentity() {
		String parentBinaryMetadataNaturalIdentity = "";
		if (parentBinaryMetadata != null) {
			parentBinaryMetadataNaturalIdentity = parentBinaryMetadata.getNaturalIdentity();
		}
		return AbstractEntity.buildNaturalIdentity(
			parentBinaryMetadataNaturalIdentity,
			startAddress
		);
	}

	public void validate(List<String> validationMessages) {

		if (parentBinaryMetadata == null) {
			validationMessages.add(getClassAndIdentity() + "'parentBinaryMetadata' must be specified.");
		}
		if (startAddress == null || startAddress.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'startAddress' must be specified.");
		}
		if (length == null || length.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'length' must be specified.");
		}
		if (rootHash == null || rootHash.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'rootHash' must be specified.");
		}
	}

	public BinaryMetadata getParentBinaryMetadata() {
		return this.parentBinaryMetadata;
	}

	public String getStartAddress() {
		return this.startAddress;
	}

	public String getLength() {
		return this.length;
	}

	public String getRootHash() {
		return  this.rootHash;
	}

	protected void setParentBinaryMetadata(BinaryMetadata parentBinaryMetadata) {
		this.parentBinaryMetadata = parentBinaryMetadata;
	}

	public static final class VerificationLogicalBlockBuilder {

        private Long persistentIdentity;
		private BinaryMetadata parentBinaryMetadata;
		private String startAddress;
		private String length;
		private String rootHash;

		public VerificationLogicalBlockBuilder() {
		}

		public VerificationLogicalBlockBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}

		public VerificationLogicalBlockBuilder withParentBinaryMetadata(BinaryMetadata parentBinaryMetadata) {
			this.parentBinaryMetadata = parentBinaryMetadata;
			return this;
		}

		public VerificationLogicalBlockBuilder withStartAddress(String startAddress) {
			this.startAddress = startAddress;
			return this;
		}

		public VerificationLogicalBlockBuilder withLength(String length) {
			this.length = length;
			return this;
		}

		public VerificationLogicalBlockBuilder withRootHash(String rootHash) {
			this.rootHash = rootHash;
			return this;
		}

		public VerificationLogicalBlock build() {
			return new VerificationLogicalBlock(this);
		}
	}
}
