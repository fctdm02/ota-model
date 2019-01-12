/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.djt.cvpp.ota.common.model.AbstractEntity;
import com.djt.cvpp.ota.vadr.model.enums.FileFormat;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareCategory;
import com.djt.cvpp.ota.vadr.model.enums.SoftwareClassification;

/**
 *
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 */
public class BinaryMetadata extends AbstractEntity {
	
	private static final long serialVersionUID = 1L;
	
	
	// Node level
	private String ecuAcronymName;
	private String ecuLogicalAddress;
	private String publicKeyHash;
	
	// Application/Software level
	private String swPartNumber;
	private String legacySwPartType;
	private SoftwareCategory swCategory;
	private SoftwareClassification swClassification;
	
	// This attribute specifies the DID on the Node that contains the list (up to 16) of software part numbers
	private String didAddress;
	
	// For signed commands generation
	private Boolean isOvtp;
	private Set<ProgramLogicalBlock> programLogicalBlocks = new TreeSet<>();
	private Set<EraseLogicalBlock> eraseLogicalBlocks = new TreeSet<>();
	private Set<VerificationLogicalBlock> verificationLogicalBlocks = new TreeSet<>();
	
	// For manifest generation
	private String autonomicUrl;
	 
	// Actual VBF/File/Binary metadata
	private String fileName;
	private FileFormat fileFormat;
	
	
	// TODO: Are any of the following actually needed for OTA Update purposes?  If not, remove them. 
	private String description;
	private long unzippedFileSize;
	private long zippedFileSize;
	private String vbfVersion;
	private String dataFormatIdentifier;
	private String frameFormat;
	private String callStartAddress;
	private String fileChecksum;
	private String fileFingerprint;
	private boolean programProtected;
	private String firmwareInstallType;
	private String createdBy;
    private Timestamp createdTimestamp;
    private String lastModifiedBy;
    private Timestamp lastModifiedTimestamp;

    private BinaryMetadata() {
	}

	private BinaryMetadata(BinaryMetadataBuilder builder) {
		
		this.persistentIdentity = builder.persistentIdentity;
		this.ecuAcronymName = builder.ecuAcronymName;
		this.ecuLogicalAddress = builder.ecuLogicalAddress;
		this.publicKeyHash = builder.publicKeyHash;
		this.swPartNumber = builder.swPartNumber;
		this.legacySwPartType = builder.legacySwPartType;
		this.swCategory = builder.swCategory;
		this.swClassification = builder.swClassification; 
		this.didAddress = builder.didAddress;
		this.isOvtp = builder.isOvtp;

		setProgramLogicalBlocks(builder.programLogicalBlocks);
		setEraseLogicalBlocks(builder.eraseLogicalBlocks);
		setVerificationLogicalBlocks(builder.verificationLogicalBlocks);

		this.autonomicUrl = builder.autonomicUrl;
		this.fileName = builder.fileName;
		this.fileFormat = builder.fileFormat;
		this.description = builder.description;
		this.unzippedFileSize = builder.unzippedFileSize;
		this.zippedFileSize = builder.zippedFileSize;
		this.vbfVersion = builder.vbfVersion;
		this.dataFormatIdentifier = builder.dataFormatIdentifier;
		this.frameFormat = builder.frameFormat;
		this.callStartAddress = builder.callStartAddress;
		this.fileChecksum = builder.fileChecksum;
		this.fileFingerprint = builder.fileFingerprint;
		this.programProtected = builder.programProtected;
		this.firmwareInstallType = builder.firmwareInstallType;
		this.createdBy = builder.createdBy;
		this.createdTimestamp = builder.createdTimestamp;
		this.lastModifiedBy = builder.lastModifiedBy;
		this.lastModifiedTimestamp = builder.lastModifiedTimestamp;
	}

	public String getNaturalIdentity() {
		return this.swPartNumber;
	}

	public void validate(List<String> validationMessages) {
		
		// Node level
		if (ecuAcronymName == null || ecuAcronymName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuAcronymName' must be specified.");
		}
		if (ecuLogicalAddress == null || ecuLogicalAddress.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'ecuLogicalAddress' must be specified.");
		}
		if (publicKeyHash == null || publicKeyHash.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'publicKeyHash' must be specified.");
		}


		// Application/Software level
		if (swPartNumber == null || swPartNumber.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'swPartNumber' must be specified.");
		}
		if (legacySwPartType == null || legacySwPartType.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'legacySwPartType' must be specified.");
		}
		if (swCategory == null) {
			validationMessages.add(getClassAndIdentity() + "'swCategory' must be specified.");
		}
		if (swClassification == null) {
			validationMessages.add(getClassAndIdentity() + "'swClassification' must be specified.");
		}


		// This attribute specifies the DID on the Node that contains the list (up to 16) of software part numbers
		if (didAddress == null || didAddress.trim().isEmpty()) {
			// TODO: TDM: Uncomment when the VADR databases have been refreshed/updated for the "new data"
			//validationMessages.add(getClassAndIdentity() + "'didAddress' must be specified.");
		}


		// For signed commands generation
		if (isOvtp == null) {
			validationMessages.add(getClassAndIdentity() + "'isOvtp' must be specified.");
		}
		Iterator<ProgramLogicalBlock> programLogicalBlocksIterator = programLogicalBlocks.iterator();
		while (programLogicalBlocksIterator.hasNext()) {

			ProgramLogicalBlock programLogicalBlock = programLogicalBlocksIterator.next();
			programLogicalBlock.validate(validationMessages);
		}

		Iterator<EraseLogicalBlock> eraseLogicalBlocksIterator = eraseLogicalBlocks.iterator();
		while (eraseLogicalBlocksIterator.hasNext()) {

			EraseLogicalBlock eraseLogicalBlock = eraseLogicalBlocksIterator.next();
			eraseLogicalBlock.validate(validationMessages);
		}

		Iterator<VerificationLogicalBlock> verificationLogicalBlocksIterator = verificationLogicalBlocks.iterator();
		while (verificationLogicalBlocksIterator.hasNext()) {

			VerificationLogicalBlock verificationLogicalBlock = verificationLogicalBlocksIterator.next();
			verificationLogicalBlock.validate(validationMessages);
		}


		// For manifest generation
		if (autonomicUrl == null || autonomicUrl.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'autonomicUrl' must be specified.");
		}

		// Actual VBF/File/Binary metadata
		if (fileName == null || fileName.trim().isEmpty()) {
			validationMessages.add(getClassAndIdentity() + "'fileName' must be specified.");
		}
		if (fileFormat == null) {
			validationMessages.add(getClassAndIdentity() + "'fileFormat' must be specified.");
		}
	}
	
	public String getEcuAcronymName() {
		return ecuAcronymName;
	}

	public String getEcuLogicalAddress() {
		return ecuLogicalAddress;
	}

	public String getPublicKeyHash() {
		return publicKeyHash;
	}

	public String getSwPartNumber() {
		return swPartNumber;
	}

	public String getLegacySwPartType() {
		return legacySwPartType;
	}

	public SoftwareCategory getSwCategory() {
		return swCategory;
	}

	public SoftwareClassification getSwClassification() {
		return swClassification;
	}

	public String getDidAddress() {
		return didAddress;
	}

	public Boolean getIsOvtp() {
		return isOvtp;
	}

	public List<ProgramLogicalBlock> getProgramLogicalBlocks() {
		List<ProgramLogicalBlock> list = new ArrayList<>();
		list.addAll(this.programLogicalBlocks);
		Collections.sort(list);
		return list;
	}

	public List<EraseLogicalBlock> getEraseLogicalBlocks() {
		List<EraseLogicalBlock> list = new ArrayList<>();
		list.addAll(this.eraseLogicalBlocks);
		Collections.sort(list);
		return list;
	}

	public List<VerificationLogicalBlock> getVerificationLogicalBlocks() {
		List<VerificationLogicalBlock> list = new ArrayList<>();
		list.addAll(this.verificationLogicalBlocks);
		Collections.sort(list);
		return list;
	}

	public String getAutonomicUrl() {
		return autonomicUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public FileFormat getFileFormat() {
		return fileFormat;
	}

	public String getDescription() {
		return description;
	}

	public long getUnzippedFileSize() {
		return unzippedFileSize;
	}

	public long getZippedFileSize() {
		return zippedFileSize;
	}

	public String getVbfVersion() {
		return vbfVersion;
	}

	public String getDataFormatIdentifier() {
		return dataFormatIdentifier;
	}

	public String getFrameFormat() {
		return frameFormat;
	}

	public String getCallStartAddress() {
		return callStartAddress;
	}

	public String getFileChecksum() {
		return fileChecksum;
	}

	public String getFileFingerprint() {
		return fileFingerprint;
	}

	public boolean isProgramProtected() {
		return programProtected;
	}

	public String getFirmwareInstallType() {
		return firmwareInstallType;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	
	public Timestamp getCreatedTimestamp() {
		return new Timestamp(createdTimestamp.getTime());
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	
	public Timestamp getLastModifiedTimestamp() {
		return new Timestamp(lastModifiedTimestamp.getTime());
	}
	
	public void setProgramLogicalBlocks(Set<ProgramLogicalBlock> programLogicalBlocks) {
    	this.programLogicalBlocks.clear();
    	this.programLogicalBlocks.addAll(programLogicalBlocks);
		Iterator<ProgramLogicalBlock> programLogicalBlocksIterator = programLogicalBlocks.iterator();
		while (programLogicalBlocksIterator.hasNext()) {

			ProgramLogicalBlock programLogicalBlock = programLogicalBlocksIterator.next();
			programLogicalBlock.setParentBinaryMetadata(this);
		}
	}

	public void setEraseLogicalBlocks(Set<EraseLogicalBlock> eraseLogicalBlocks) {
		this.eraseLogicalBlocks.clear();
		this.eraseLogicalBlocks.addAll(eraseLogicalBlocks);
		Iterator<EraseLogicalBlock> eraseLogicalBlocksIterator = eraseLogicalBlocks.iterator();
		while (eraseLogicalBlocksIterator.hasNext()) {

			EraseLogicalBlock eraseLogicalBlock = eraseLogicalBlocksIterator.next();
			eraseLogicalBlock.setParentBinaryMetadata(this);
		}
	}

	public void setVerificationLogicalBlocks(Set<VerificationLogicalBlock> verificationLogicalBlocks) {
		this.verificationLogicalBlocks.clear();
		this.verificationLogicalBlocks.addAll(verificationLogicalBlocks);
		Iterator<VerificationLogicalBlock> verificationLogicalBlocksIterator = verificationLogicalBlocks.iterator();
		while (verificationLogicalBlocksIterator.hasNext()) {

			VerificationLogicalBlock verificationLogicalBlock = verificationLogicalBlocksIterator.next();
			verificationLogicalBlock.setParentBinaryMetadata(this);
		}
	}

	/**
	 * Used for dealing with the fact that VADR data could be missing binary metadata information.
	 *
	 * @return
	 */
	protected boolean hasSkeletonBinaryMetadataOnly() {

		// If all of these attributes is null, then assume nothing else is set, which means that we are a "skeleton"...
		if (autonomicUrl == null && fileName == null && ecuAcronymName == null && ecuLogicalAddress == null) {
			return true;
		}
		return false;
	}

	public static final class BinaryMetadataBuilder {

		private Long persistentIdentity;
		private String ecuAcronymName;
		private String ecuLogicalAddress;
		private String publicKeyHash;
		private String swPartNumber;
		private String legacySwPartType;
		private SoftwareCategory swCategory;
		private SoftwareClassification swClassification;
		private String didAddress;
		private Boolean isOvtp;
		private Set<ProgramLogicalBlock> programLogicalBlocks = new TreeSet<>();
		private Set<EraseLogicalBlock> eraseLogicalBlocks = new TreeSet<>();
		private Set<VerificationLogicalBlock> verificationLogicalBlocks = new TreeSet<>();
		private String autonomicUrl;
		private String fileName;
		private FileFormat fileFormat;
		private String description;
		private long unzippedFileSize;
		private long zippedFileSize;
		private String vbfVersion;
		private String dataFormatIdentifier;
		private String frameFormat;
		private String callStartAddress;
		private String fileChecksum;
		private String fileFingerprint;
		private boolean programProtected;
		private String firmwareInstallType;	
		private String createdBy;
	    private Timestamp createdTimestamp;
	    private String lastModifiedBy;
	    private Timestamp lastModifiedTimestamp;
		
		public BinaryMetadataBuilder() {
		}

		public BinaryMetadataBuilder withPersistentIdentity(Long persistentIdentity) {
			this.persistentIdentity = persistentIdentity;
			return this;
		}
		
		public BinaryMetadataBuilder withEcuAcronymName(String ecuAcronymName) {
			this.ecuAcronymName = ecuAcronymName;
			return this;
		}

		public BinaryMetadataBuilder withEcuLogicalAddress(String ecuLogicalAddress) {
			this.ecuLogicalAddress = ecuLogicalAddress;
			return this;
		}

		public BinaryMetadataBuilder withPublicKeyHash(String publicKeyHash) {
			this.publicKeyHash = publicKeyHash;
			return this;
		}

		public BinaryMetadataBuilder withSwPartNumber(String swPartNumber) {
			this.swPartNumber = swPartNumber;
			return this;
		}

		public BinaryMetadataBuilder withLegacySwPartType(String legacySwPartType) {
			this.legacySwPartType = legacySwPartType;
			return this;
		}

		public BinaryMetadataBuilder withSwCategory(SoftwareCategory swCategory) {
			this.swCategory = swCategory;
			return this;
		}

		public BinaryMetadataBuilder withSwClassification(SoftwareClassification swClassification) {
			this.swClassification = swClassification;
			return this;
		}
		
		public BinaryMetadataBuilder withDidAddress(String didAddress) {
			this.didAddress = didAddress;
			return this;
		}
		
		public BinaryMetadataBuilder withIsOvtp(Boolean isOvtp) {
			this.isOvtp = isOvtp;
			return this;
		}
		
		public BinaryMetadataBuilder withProgramLogicalBlocks(Set<ProgramLogicalBlock> programLogicalBlocks) {
			this.programLogicalBlocks = programLogicalBlocks;
			return this;
		}

		public BinaryMetadataBuilder withEraseLogicalBlocks(Set<EraseLogicalBlock> eraseLogicalBlocks) {
			this.eraseLogicalBlocks = eraseLogicalBlocks;
			return this;
		}

		public BinaryMetadataBuilder withVerificationLogicalBlocks(Set<VerificationLogicalBlock> verificationLogicalBlocks) {
			this.verificationLogicalBlocks = verificationLogicalBlocks;
			return this;
		}

		public BinaryMetadataBuilder withAutonomicUrl(String autonomicUrl) {
			this.autonomicUrl = autonomicUrl;
			return this;
		}

		public BinaryMetadataBuilder withFileName(String fileName) {
			this.fileName = fileName;
			return this;
		}

		public BinaryMetadataBuilder withFileFormat(FileFormat fileFormat) {
			this.fileFormat = fileFormat;
			return this;
		}

		public BinaryMetadataBuilder withDescription(String description) {
			this.description = description;
			return this;
		}

		public BinaryMetadataBuilder withUnzippedFileSize(long unzippedFileSize) {
			this.unzippedFileSize = unzippedFileSize;
			return this;
		}

		public BinaryMetadataBuilder withZippedFileSize(long zippedFileSize) {
			this.zippedFileSize = zippedFileSize;
			return this;
		}

		public BinaryMetadataBuilder withVbfVersion(String vbfVersion) {
			this.vbfVersion = vbfVersion;
			return this;
		}

		public BinaryMetadataBuilder withDataFormatIdentifier(String dataFormatIdentifier) {
			this.dataFormatIdentifier = dataFormatIdentifier;
			return this;
		}

		public BinaryMetadataBuilder withFrameFormat(String frameFormat) {
			this.frameFormat = frameFormat;
			return this;
		}
		
		public BinaryMetadataBuilder withCallStartAddress(String callStartAddress) {
			this.callStartAddress = callStartAddress;
			return this;
		}

		public BinaryMetadataBuilder withFileChecksum(String fileChecksum) {
			this.fileChecksum = fileChecksum;
			return this;
		}
		
		public BinaryMetadataBuilder withFileFingerprint(String fileFingerprint) {
			this.fileFingerprint = fileFingerprint;
			return this;
		}

		public BinaryMetadataBuilder withProgramProtected(boolean programProtected) {
			this.programProtected = programProtected;
			return this;
		}
		
		public BinaryMetadataBuilder withFirmwareInstallType(String firmwareInstallType) {
			this.firmwareInstallType = firmwareInstallType;
			return this;
		}

		public BinaryMetadataBuilder withCreatedBy(String createdBy) {
			this.createdBy = createdBy;
			return this;
		}
		
		public BinaryMetadataBuilder withCreatedTimestamp(Timestamp createdTimestamp) {
			this.createdTimestamp = new Timestamp(createdTimestamp.getTime());
			return this;
		}

		public BinaryMetadataBuilder withLastModifiedBy(String lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}
		
		public BinaryMetadataBuilder withLastModifiedTimestamp(Timestamp lastModifiedTimestamp) {
			this.lastModifiedTimestamp = new Timestamp(lastModifiedTimestamp.getTime());
			return this;
		}
		
		public BinaryMetadata build() {
			return new BinaryMetadata(this);
		}
	}	
}
