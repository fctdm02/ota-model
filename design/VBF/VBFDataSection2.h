#pragma once

using namespace System;

ref class VBFDataSection2
{
protected:
	bool fileUsesCompression; // Does the VBF specify compression?
	bool isCompressed; // Is the payload of this block currently compressed?
	array<Byte>^ effectivePayload; // The payload. Decompressed if necessary.
	UInt32 declaredPayloadLength; // Length of the payload as it is in the file. (represents COMPRESSED/ENCRYPTED length if applicable)
	UInt16 declaredBlockChecksum; // The block checksum present in the file. It is calculated on the UNCOMPRESSED/UNENCRYPTED payload only
	UInt16 calculatedBlockChecksum; // The block checksum. Calculated by us. Used for verification.
	String^ hash; // SHA256 hash of the payload. Calculated by us. Used for verification.

	void Parse(array<Byte> ^VBFData);
	UInt16 CalculateBlockChecksum();
	String^ CalculateBlockHash();

public:
	UInt32 address;

	VBFDataSection2(array<Byte> ^data, bool isCompressed);
	array<Byte>^ Serialize();
	virtual String^ ToString() override;
	void Validate();
	void CompressPayload();
	void DecompressPayload();

};

