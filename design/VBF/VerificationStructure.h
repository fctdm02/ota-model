#pragma once
#include "VerificationStructureRecord.h"

ref class VerificationStructure
{
private:
	void LoadRecords(array<Byte>^ data);

public:
	array<Byte> ^signature;
	UInt32 version;
	UInt32 recordCount; // This could conflict with the actual number of records in the collection and so is generally bad practice however, we want to validate the number presented in the file so we retain it.
	array<VerificationStructureRecord^> ^records;

	VerificationStructure(Boolean isFileSigned, array<Byte> ^data);
	virtual String^ ToString() override;
};

