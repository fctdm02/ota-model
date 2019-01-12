/*
 * 
 *
 * 
 *
 * 
 *
 */
package com.djt.cvpp.ota.vadr.mapper.dto;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fileId",
    "ecuAcronymName",
    "ecuLogicalAddress",
    "publicKeyHash",
    "swPartNumber",
    "legacySwPartType",
    "swCategory",
    "swClassification",
    "didAddress",
    "isOvtp",
    "programLogicalBlocks",
    "eraseLogicalBlocks",
    "verificationLogicalBlocks",
    "autonomicUrl",
    "fileName",
    "fileFormat",
    "description",
    "unzippedFileSize",
    "zippedFileSize",
    "vbfVersion",
    "dataFormatIdentifier",
    "frameFormat",
    "callStartAddress",
    "fileChecksum",
    "fileFingerprint",
    "programProtected",
    "firmwareInstallType",
    "createdTimestamp",
    "lastModifiedTimestamp"
})
/**
 * 
 * @author tmyers1@yahoo.com (Tom Myers)
 *
 * Generated using http://www.jsonschema2pojo.org/
 */
public class BinaryMetadata {

    @JsonProperty("fileId")
    private Integer fileId;
    @JsonProperty("ecuAcronymName")
    private String ecuAcronymName;
    @JsonProperty("ecuLogicalAddress")
    private String ecuLogicalAddress;
    @JsonProperty("publicKeyHash")
    private String publicKeyHash;
    @JsonProperty("swPartNumber")
    private String swPartNumber;
    @JsonProperty("legacySwPartType")
    private String legacySwPartType;
    @JsonProperty("swCategory")
    private String swCategory;
    @JsonProperty("swClassification")
    private String swClassification;
    @JsonProperty("didAddress")
    private String didAddress;
    @JsonProperty("isOvtp")
    private Boolean isOvtp;
    @JsonProperty("programLogicalBlocks")
    private List<ProgramLogicalBlock> programLogicalBlocks = new ArrayList<>();
    @JsonProperty("eraseLogicalBlocks")
    private List<EraseLogicalBlock> eraseLogicalBlocks = new ArrayList<>();
    @JsonProperty("verificationLogicalBlocks")
    private List<VerificationLogicalBlock> verificationLogicalBlocks = new ArrayList<>();
    @JsonProperty("autonomicUrl")
    private String autonomicUrl;
    @JsonProperty("fileName")
    private String fileName;
    @JsonProperty("fileFormat")
    private String fileFormat;
    @JsonProperty("description")
    private String description;
    @JsonProperty("unzippedFileSize")
    private Integer unzippedFileSize;
    @JsonProperty("zippedFileSize")
    private Integer zippedFileSize;
    @JsonProperty("vbfVersion")
    private String vbfVersion;
    @JsonProperty("dataFormatIdentifier")
    private String dataFormatIdentifier;
    @JsonProperty("frameFormat")
    private String frameFormat;
    @JsonProperty("callStartAddress")
    private String callStartAddress;
    @JsonProperty("fileChecksum")
    private String fileChecksum;
    @JsonProperty("fileFingerprint")
    private String fileFingerprint;
    @JsonProperty("programProtected")
    private Boolean programProtected;
    @JsonProperty("firmwareInstallType")
    private String firmwareInstallType;
    @JsonProperty("createdTimestamp")
    private String createdTimestamp;
    @JsonProperty("lastModifiedTimestamp")
    private String lastModifiedTimestamp;

    @JsonProperty("fileId")
    public Integer getFileId() {
        return fileId;
    }

    @JsonProperty("fileId")
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    @JsonProperty("ecuAcronymName")
    public String getEcuAcronymName() {
        return ecuAcronymName;
    }

    @JsonProperty("ecuAcronymName")
    public void setEcuAcronymName(String ecuAcronymName) {
        this.ecuAcronymName = ecuAcronymName;
    }

    @JsonProperty("ecuLogicalAddress")
    public String getEcuLogicalAddress() {
        return ecuLogicalAddress;
    }

    @JsonProperty("ecuLogicalAddress")
    public void setEcuLogicalAddress(String ecuLogicalAddress) {
        this.ecuLogicalAddress = ecuLogicalAddress;
    }

    @JsonProperty("publicKeyHash")
    public String getPublicKeyHash() {
        return publicKeyHash;
    }

    @JsonProperty("publicKeyHash")
    public void setPublicKeyHash(String publicKeyHash) {
        this.publicKeyHash = publicKeyHash;
    }

    @JsonProperty("swPartNumber")
    public String getSwPartNumber() {
        return swPartNumber;
    }

    @JsonProperty("swPartNumber")
    public void setSwPartNumber(String swPartNumber) {
        this.swPartNumber = swPartNumber;
    }

    @JsonProperty("legacySwPartType")
    public String getLegacySwPartType() {
        return legacySwPartType;
    }

    @JsonProperty("legacySwPartType")
    public void setLegacySwPartType(String legacySwPartType) {
        this.legacySwPartType = legacySwPartType;
    }

    @JsonProperty("swCategory")
    public String getSwCategory() {
        return swCategory;
    }

    @JsonProperty("swCategory")
    public void setSwCategory(String swCategory) {
        this.swCategory = swCategory;
    }

    @JsonProperty("swClassification")
    public String getSwClassification() {
        return swClassification;
    }

    @JsonProperty("swClassification")
    public void setSwClassification(String swClassification) {
        this.swClassification = swClassification;
    }

    @JsonProperty("didAddress")
    public String getDidAddress() {
        return didAddress;
    }

    @JsonProperty("didAddress")
    public void setDidAddress(String didAddress) {
        this.didAddress = didAddress;
    }

    @JsonProperty("isOvtp")
    public Boolean getIsOvtp() {
        return isOvtp;
    }

    @JsonProperty("isOvtp")
    public void setIsOvtp(Boolean isOvtp) {
        this.isOvtp = isOvtp;
    }

    @JsonProperty("programLogicalBlocks")
    public List<ProgramLogicalBlock> getProgramLogicalBlocks() {
        return programLogicalBlocks;
    }

    @JsonProperty("programLogicalBlocks")
    public void setProgramLogicalBlocks(List<ProgramLogicalBlock> programLogicalBlocks) {
        this.programLogicalBlocks = programLogicalBlocks;
    }

    @JsonProperty("eraseLogicalBlocks")
    public List<EraseLogicalBlock> getEraseLogicalBlocks() {
        return eraseLogicalBlocks;
    }

    @JsonProperty("eraseLogicalBlocks")
    public void setEraseLogicalBlocks(List<EraseLogicalBlock> eraseLogicalBlocks) {
        this.eraseLogicalBlocks = eraseLogicalBlocks;
    }

    @JsonProperty("verificationLogicalBlocks")
    public List<VerificationLogicalBlock> getVerificationLogicalBlocks() {
        return verificationLogicalBlocks;
    }

    @JsonProperty("verificationLogicalBlocks")
    public void setVerificationLogicalBlocks(List<VerificationLogicalBlock> verificationLogicalBlocks) {
        this.verificationLogicalBlocks = verificationLogicalBlocks;
    }

    @JsonProperty("autonomicUrl")
    public String getAutonomicUrl() {
        return autonomicUrl;
    }

    @JsonProperty("autonomicUrl")
    public void setAutonomicUrl(String autonomicUrl) {
        this.autonomicUrl = autonomicUrl;
    }

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonProperty("fileFormat")
    public String getFileFormat() {
        return fileFormat;
    }

    @JsonProperty("fileFormat")
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("unzippedFileSize")
    public Integer getUnzippedFileSize() {
        return unzippedFileSize;
    }

    @JsonProperty("unzippedFileSize")
    public void setUnzippedFileSize(Integer unzippedFileSize) {
        this.unzippedFileSize = unzippedFileSize;
    }

    @JsonProperty("zippedFileSize")
    public Integer getZippedFileSize() {
        return zippedFileSize;
    }

    @JsonProperty("zippedFileSize")
    public void setZippedFileSize(Integer zippedFileSize) {
        this.zippedFileSize = zippedFileSize;
    }

    @JsonProperty("vbfVersion")
    public String getVbfVersion() {
        return vbfVersion;
    }

    @JsonProperty("vbfVersion")
    public void setVbfVersion(String vbfVersion) {
        this.vbfVersion = vbfVersion;
    }

    @JsonProperty("dataFormatIdentifier")
    public String getDataFormatIdentifier() {
        return dataFormatIdentifier;
    }

    @JsonProperty("dataFormatIdentifier")
    public void setDataFormatIdentifier(String dataFormatIdentifier) {
        this.dataFormatIdentifier = dataFormatIdentifier;
    }

    @JsonProperty("frameFormat")
    public String getFrameFormat() {
        return frameFormat;
    }

    @JsonProperty("frameFormat")
    public void setFrameFormat(String frameFormat) {
        this.frameFormat = frameFormat;
    }

    @JsonProperty("callStartAddress")
    public String getCallStartAddress() {
        return callStartAddress;
    }

    @JsonProperty("callStartAddress")
    public void setCallStartAddress(String callStartAddress) {
        this.callStartAddress = callStartAddress;
    }

    @JsonProperty("fileChecksum")
    public String getFileChecksum() {
        return fileChecksum;
    }

    @JsonProperty("fileChecksum")
    public void setFileChecksum(String fileChecksum) {
        this.fileChecksum = fileChecksum;
    }

    @JsonProperty("fileFingerprint")
    public String getFileFingerprint() {
        return fileFingerprint;
    }

    @JsonProperty("fileFingerprint")
    public void setFileFingerprint(String fileFingerprint) {
        this.fileFingerprint = fileFingerprint;
    }

    @JsonProperty("programProtected")
    public Boolean getProgramProtected() {
        return programProtected;
    }

    @JsonProperty("programProtected")
    public void setProgramProtected(Boolean programProtected) {
        this.programProtected = programProtected;
    }

    @JsonProperty("firmwareInstallType")
    public String getFirmwareInstallType() {
        return firmwareInstallType;
    }

    @JsonProperty("firmwareInstallType")
    public void setFirmwareInstallType(String firmwareInstallType) {
        this.firmwareInstallType = firmwareInstallType;
    }

    @JsonProperty("createdTimestamp")
    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    @JsonProperty("createdTimestamp")
    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    @JsonProperty("lastModifiedTimestamp")
    public String getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    @JsonProperty("lastModifiedTimestamp")
    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

}
