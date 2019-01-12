#pragma once
#include "openssl\evp.h"
#include "VBFHeader.h"
#include "VBFBinaryData.h"

using namespace System;

/*
	A VBF file is made up of two sections, the header - represented by (VBFHeader) class,
	and a binary part - (VBFBinaryData).

	The binary part is further divided into data sections (VBFDataSection2).
	Some of the data sections are special because they are used to provide security 
	for the other sections (VBFVerificationStructureDataSection). These are called
	Verification Structure Data Sections and they are still valid data sections and 
	follow the same rules, so they are a subclass of VBFDataSection2.

	The VS data sections contain in their payload a Verification Structure (VerificationStructure)
	which contains a table with records (VerificationStructureRecord) - a record per data section. 
	It contains info about the other data sections.
*/

ref class VBFFile2
{
private:
	bool usesCompression;
	VBFHeader^ header;
	VBFBinaryData^ binaryData;

	int FindDataSection(array<Byte>^ data);

public:
	property String ^publicKeyHash {String ^get();}

	VBFFile2(array<Byte> ^data);
	void Sign(EVP_PKEY * privateKey);
	void Compress();
	void ValidateConstruction();
	bool VerifySignatures(EVP_PKEY * publicKey);

	virtual String^ ToString() override;
	array<Byte>^ Serialize();
};


