#pragma once

using namespace System;
using namespace System::Collections::Generic;

/* example header:
vbf_version = 4.0;

header {
sw_part_number = "Test-12345";

sw_part_type = EXE;

ecu_address = 0x730;

frame_format = CAN_STANDARD;

erase = { { 0x00000000, 0x0000c539},
{ 0x01200100, 0x0000002c}
};
file_checksum = 0x42943d01;

public_key_hash = "77253A936B502AE8EBDFC3362FA1A4EC6C46F8DDC9C142800B8BADFB259069FF";

}*/

#define VBF_VERSION_LABEL "vbf_version"
#define PUBLIC_KEY_HASH_LABEL "public_key_hash"
#define FILE_CHECKSUM_LABEL "file_checksum"
#define VS_STRUCT_ADDRESS_LABEL "verification_structure_address"
#define SW_SIGNATURE_LABEL "sw_signature"
#define COMPRESSION_ENCRYPTION_LABEL "data_format_identifier"


ref class VBFHeader
{
private:
	String^ header;
	Single vbf_version;
	UInt32 _fileChecksum; // "file checksum" isn't really a checksum of the entire file it only covers the binary portion.
	String^ _publicKeyHash;
	List<String^>^ _swSignature; // Kind of dumb to put all the signatures into the header IMO (but it's in the VBF spec), however it does serve an important function for us, it works as an 'is signed' indicator.
	Boolean _fileIsSigned;
	Boolean _dataIsCompressed;

	String^ FindHeaderValue(String^ name);
	List<UInt32>^ FindHeaderIntList(String ^ name);
	List<String^>^ FindHeaderStringList(String^ name);
	void Parse();

public:
	property List<UInt32>^ verificationStructureAddresses;
	property array<Byte>^ headerData {array<Byte>^ get(); }
	property String^ publicKeyHash {String^ get(); }
	property UInt32 fileChecksum {UInt32 get(); }
	property Boolean fileIsSigned {Boolean get(); }
	property Boolean dataIsCompressed {Boolean get(); }

	VBFHeader(array<Byte>^ data);
	void SetVSAddresses(List<UInt32>^ vsAddresses);
	void SetSignatures(List<String^>^ signatures);
	void SetValue(String^ name, String^ value);
	virtual String^ ToString() override;
	void Validate();
};

