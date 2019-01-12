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
public class EraseLogicalBlock extends AbstractEntity {

    private static final long serialVersionUID = 1L;


    private BinaryMetadata parentBinaryMetadata;
    private String startAddress;
    private String length;

    private EraseLogicalBlock() {
    }

    private EraseLogicalBlock(EraseLogicalBlockBuilder builder) {

        this.persistentIdentity = builder.persistentIdentity;
        this.parentBinaryMetadata = builder.parentBinaryMetadata;
        this.startAddress = builder.startAddress;
        this.length = builder.length;
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

    protected void setParentBinaryMetadata(BinaryMetadata parentBinaryMetadata) {
        this.parentBinaryMetadata = parentBinaryMetadata;
    }

    public static final class EraseLogicalBlockBuilder {

        private Long persistentIdentity;
        private BinaryMetadata parentBinaryMetadata;
        private String startAddress;
        private String length;

        public EraseLogicalBlockBuilder() {
        }

        public EraseLogicalBlockBuilder withPersistentIdentity(Long persistentIdentity) {
            this.persistentIdentity = persistentIdentity;
            return this;
        }

        public EraseLogicalBlockBuilder withParentBinaryMetadata(BinaryMetadata parentBinaryMetadata) {
            this.parentBinaryMetadata = parentBinaryMetadata;
            return this;
        }

        public EraseLogicalBlockBuilder withStartAddress(String startAddress) {
            this.startAddress = startAddress;
            return this;
        }

        public EraseLogicalBlockBuilder withLength(String length) {
            this.length = length;
            return this;
        }

        public EraseLogicalBlock build() {
            return new EraseLogicalBlock(this);
        }
    }
}
