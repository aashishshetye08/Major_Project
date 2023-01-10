//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2022 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------
// !Warning! This file is autogenerated, modify the .codegen file, not this one
// (any changes here will be wiped out during the autogen process)

#ifndef PDFTRON_H_CDigestAlgorithm
#define PDFTRON_H_CDigestAlgorithm

#ifdef __cplusplus
extern "C" {
#endif

#include <C/Common/TRN_Types.h>
#include <C/Common/TRN_Exception.h>


struct TRN_DigestAlgorithm_tag;
typedef struct TRN_DigestAlgorithm_tag* TRN_DigestAlgorithm;

/* enums:  */
enum TRN_DigestAlgorithm_Type {
	e_DigestAlgorithm_SHA1 = 0,
	e_DigestAlgorithm_SHA256 = 1,
	e_DigestAlgorithm_SHA384 = 2,
	e_DigestAlgorithm_SHA512 = 3,
	e_DigestAlgorithm_RIPEMD160 = 4,
	e_DigestAlgorithm_unknown_digest_algorithm = 5
};

TRN_API TRN_DigestAlgorithmCalculateDigest(enum TRN_DigestAlgorithm_Type in_algorithm, const TRN_UChar* in_buffer, const size_t in_buf_size, TRN_Vector* result);


#ifdef __cplusplus
} // extern C
#endif

#endif /* PDFTRON_H_CDigestAlgorithm */
