#pragma once
#include "openssl\evp.h"
#include "VBFDataSection2.h"
#include "VerificationStructureDataSection.h"

using namespace System::Collections::Generic;

ref class VBFBinaryData
{
private:
	// ALL of the data sections are contained in the 'dataSections' collection.
	List<VBFDataSection2^>^ dataSections;

	// This collection contains pointers to just the VS sections.
	// These are also contained in the 'dataSections' member.
	// This exists only to provide more efficient asccess to the verification structure data sections
	// for those times when things need to be done to just them.
	// This way you don't have to loop through the entire 'dataSections' collection and check the type of every object.
	List<VerificationStructureDataSection^>^ verificationStructureDataSections;

	void LoadDataSections(List<UInt32>^verificationSectionAddresses, Boolean isFileSigned, Boolean isDataCompressed, array<Byte>^data);
	void UpdateFileChecksum();
	bool DoesThisSectionContainAVerificationStructure(unsigned int sectionAddress, unsigned int sectionLength, List<UInt32>^verificationSectionAddresses);

public:
	UInt32 fileChecksum; // 'file' checksum is really only over the binary data.

	VBFBinaryData(List<UInt32>^verificationSectionAddresses, Boolean isFileSigned, Boolean isDataCompressed, array<Byte>^data);
	array<Byte>^ Serialize();
	List<UInt32>^ GetVerificationStructureAddresses();
	List<String^>^ GetSignatures();
	void Sign(EVP_PKEY *privateKey);
	virtual String^ ToString() override;
	void Validate();
	bool VerifySignatures(EVP_PKEY *publicKey);
	void Compress();
};

