#pragma once
#include "openssl\evp.h"
#include "VBFDataSection2.h"
#include "VerificationStructure.h"

using namespace System;

ref class VerificationStructureDataSection: VBFDataSection2
{
private:
	VerificationStructure^ verificationStructure;

public:
	VerificationStructureDataSection(array<Byte>^ data, Boolean isFileSigned, Boolean isDataCompressed);
	property String^ signature {String^ get();}
	void Sign(EVP_PKEY* privateKey);
	bool 

(EVP_PKEY* publicKey);
	virtual String^ ToString() override;
};

