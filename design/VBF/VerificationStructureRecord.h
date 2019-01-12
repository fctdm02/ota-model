#pragma once

using namespace System;

ref class VerificationStructureRecord
{
public:
	UInt32 address;
	UInt32 length;
	array<Byte> ^hash;
	String ^hashAsB64String;
	VerificationStructureRecord(array<Byte>^ data);

	virtual String^ ToString() override;
};

