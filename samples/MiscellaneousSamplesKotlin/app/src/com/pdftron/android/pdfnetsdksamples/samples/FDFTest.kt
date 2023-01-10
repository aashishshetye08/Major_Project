//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------
package com.pdftron.android.pdfnetsdksamples.samples

import com.pdftron.android.pdfnetsdksamples.OutputListener
import com.pdftron.android.pdfnetsdksamples.PDFNetSample
import com.pdftron.android.pdfnetsdksamples.R
import com.pdftron.android.pdfnetsdksamples.util.Utils
import com.pdftron.fdf.FDFDoc
import com.pdftron.fdf.FDFField
import com.pdftron.fdf.FDFFieldIterator
import com.pdftron.pdf.FieldIterator
import com.pdftron.pdf.PDFDoc
import com.pdftron.sdf.SDFDoc

class FDFTest : PDFNetSample() {
    override fun run(outputListener: OutputListener?) {
        super.run(outputListener)
        mOutputListener = outputListener
        mFileList.clear()
        printHeader(outputListener!!)

        // Example 1)
        // Iterate over all form fields in the document. Display all field names.
        try {
            val doc = PDFDoc(Utils.getAssetTempFile(INPUT_PATH.toString() + "form1.pdf")!!.getAbsolutePath())
            doc.initSecurityHandler()
            val itr: FieldIterator = doc.getFieldIterator()
            while (itr.hasNext()) {
                val current: com.pdftron.pdf.Field? = itr.next()
                mOutputListener!!.println("Field name: " + current!!.getName())
                mOutputListener!!.println("Field partial name: " + current.getPartialName())
                mOutputListener!!.print("Field type: ")
                val type: Int = current.getType()
                when (type) {
                    com.pdftron.pdf.Field.e_button -> mOutputListener!!.println("Button")
                    com.pdftron.pdf.Field.e_check -> mOutputListener!!.println("Check")
                    com.pdftron.pdf.Field.e_radio -> mOutputListener!!.println("Radio")
                    com.pdftron.pdf.Field.e_text -> mOutputListener!!.println("Text")
                    com.pdftron.pdf.Field.e_choice -> mOutputListener!!.println("Choice")
                    com.pdftron.pdf.Field.e_signature -> mOutputListener!!.println("Signature")
                    com.pdftron.pdf.Field.e_null -> mOutputListener!!.println("Null")
                }
                mOutputListener!!.println("------------------------------")
            }
            doc.close()
            mOutputListener!!.println("Done.")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }

        // Example 2) Import XFDF into FDF, then merge data from FDF into PDF
        try {
            // XFDF to FDF
            // form fields
            mOutputListener!!.println("Import form field data from XFDF to FDF.")
            val fdf_doc1: FDFDoc = FDFDoc.createFromXFDF(Utils.getAssetTempFile(INPUT_PATH.toString() + "form1_data.xfdf")!!.getAbsolutePath())
            fdf_doc1.save(Utils.createExternalFile("form1_data.fdf", mFileList).getAbsolutePath())

            // annotations
            mOutputListener!!.println("Import annotations from XFDF to FDF.")
            val fdf_doc2: FDFDoc = FDFDoc.createFromXFDF(Utils.getAssetTempFile(INPUT_PATH.toString() + "form1_annots.xfdf")!!.getAbsolutePath())
            fdf_doc2.save(Utils.createExternalFile("form1_annots.fdf", mFileList).getAbsolutePath())

            // FDF to PDF
            // form fields
            mOutputListener!!.println("Merge form field data from FDF.")
            val doc = PDFDoc(Utils.getAssetTempFile(INPUT_PATH.toString() + "form1.pdf")!!.getAbsolutePath())
            doc.initSecurityHandler()
            doc.fdfMerge(fdf_doc1)

            // Refreshing missing appearances is not required here, but is recommended to make them
            // visible in PDF viewers with incomplete annotation viewing support. (such as Chrome)
            doc.refreshAnnotAppearances()
            doc.save(Utils.createExternalFile("form1_filled.pdf", mFileList).getAbsolutePath(), SDFDoc.SaveMode.LINEARIZED, null)

            // annotations
            mOutputListener!!.println("Merge annotations from FDF.")
            doc.fdfMerge(fdf_doc2)
            // Refreshing missing appearances is not required here, but is recommended to make them
            // visible in PDF viewers with incomplete annotation viewing support. (such as Chrome)
            doc.refreshAnnotAppearances()
            doc.save(Utils.createExternalFile("form1_filled_with_annots.pdf", mFileList).getAbsolutePath(), SDFDoc.SaveMode.LINEARIZED, null)
            doc.close()
            mOutputListener!!.println("Done.")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }

        // Example 3) Extract data from PDF to FDF, then export FDF as XFDF
        try {
            // PDF to FDF
            val in_doc = PDFDoc(Utils.createExternalFile("form1_filled_with_annots.pdf", mFileList).getAbsolutePath())
            in_doc.initSecurityHandler()

            // form fields only
            mOutputListener!!.println("Extract form fields data to FDF.")
            val doc_fields: FDFDoc = in_doc.fdfExtract(PDFDoc.e_forms_only)
            doc_fields.setPDFFileName(Utils.createExternalFile("form1_filled_with_annots.pdf", mFileList).getAbsolutePath())
            doc_fields.save(Utils.createExternalFile("form1_filled_data.fdf", mFileList).getAbsolutePath())

            // annotations only
            mOutputListener!!.println("Extract annotations to FDF.")
            val doc_annots: FDFDoc = in_doc.fdfExtract(PDFDoc.e_annots_only)
            doc_annots.setPDFFileName(Utils.createExternalFile("form1_filled_with_annots.pdf", mFileList).getAbsolutePath())
            doc_annots.save(Utils.createExternalFile("form1_filled_annot.fdf", mFileList).getAbsolutePath())

            // both form fields and annotations
            mOutputListener!!.println("Extract both form fields and annotations to FDF.")
            val doc_both: FDFDoc = in_doc.fdfExtract(PDFDoc.e_both)
            doc_both.setPDFFileName(Utils.createExternalFile("form1_filled_with_annots.pdf", mFileList).getAbsolutePath())
            doc_both.save(Utils.createExternalFile("form1_filled_both.fdf", mFileList).getAbsolutePath())

            // FDF to XFDF
            // form fields
            mOutputListener!!.println("Export form field data from FDF to XFDF.")
            doc_fields.saveAsXFDF(Utils.createExternalFile("form1_filled_data.xfdf", mFileList).getAbsolutePath())

            // annotations
            mOutputListener!!.println("Export annotations from FDF to XFDF.")
            doc_annots.saveAsXFDF(Utils.createExternalFile("form1_filled_annot.xfdf", mFileList).getAbsolutePath())

            // both form fields and annotations
            mOutputListener!!.println("Export both form fields and annotations from FDF to XFDF.")
            doc_both.saveAsXFDF(Utils.createExternalFile("form1_filled_both.xfdf", mFileList).getAbsolutePath())
            in_doc.close()
            mOutputListener!!.println("Done.")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }

        // Example 4) Merge/Extract XFDF into/from PDF
        try {
            // Merge XFDF from string
            val in_doc = PDFDoc(Utils.getAssetTempFile(INPUT_PATH.toString() + "numbered.pdf")!!.getAbsolutePath())
            in_doc.initSecurityHandler()
            mOutputListener!!.println("Merge XFDF string into PDF.")
            val str = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><xfdf xmlns=\"http://ns.adobe.com/xfdf\" xml:space=\"preserve\"><square subject=\"Rectangle\" page=\"0\" name=\"cf4d2e58-e9c5-2a58-5b4d-9b4b1a330e45\" title=\"user\" creationdate=\"D:20120827112326-07'00'\" date=\"D:20120827112326-07'00'\" rect=\"227.7814207650273,597.6174863387978,437.07103825136608,705.0491803278688\" color=\"#000000\" interior-color=\"#FFFF00\" flags=\"print\" width=\"1\"><popup flags=\"print,nozoom,norotate\" open=\"no\" page=\"0\" rect=\"0,792,0,792\" /></square></xfdf>"
            val fdoc: FDFDoc = FDFDoc.createFromXFDF(str)
            in_doc.fdfMerge(fdoc)
            in_doc.save(Utils.createExternalFile("numbered_modified.pdf", mFileList).getAbsolutePath(), SDFDoc.SaveMode.LINEARIZED, null)
            mOutputListener!!.println("Merge complete.")

            // Extract XFDF as string
            mOutputListener!!.println("Extract XFDF as a string.")
            val fdoc_new: FDFDoc = in_doc.fdfExtract(PDFDoc.e_both)
            val XFDF_str: String = fdoc_new.saveAsXFDF()
            mOutputListener!!.println("Extracted XFDF: ")
            mOutputListener!!.println(XFDF_str)
            in_doc.close()
            mOutputListener!!.println("Extract complete.")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }

        // Example 5) Read FDF files directly
        try {
            val doc = FDFDoc(Utils.createExternalFile("form1_filled_data.fdf", mFileList).getAbsolutePath())
            val itr: FDFFieldIterator = doc.getFieldIterator()
            while (itr.hasNext()) {
                val current: FDFField? = itr.next()
                mOutputListener!!.println("Field name: " + current!!.getName())
                mOutputListener!!.println("Field partial name: " + current.getPartialName())
                mOutputListener!!.println("------------------------------")
            }
            doc.close()
            mOutputListener!!.println("Done.")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }

        // Example 6) Direct generation of FDF.
        try {
            val doc = FDFDoc()
            // Create new fields (i.e. key/value pairs).
            doc.fieldCreate("Company", com.pdftron.pdf.Field.e_text, "PDFTron Systems")
            doc.fieldCreate("First Name", com.pdftron.pdf.Field.e_text, "John")
            doc.fieldCreate("Last Name", com.pdftron.pdf.Field.e_text, "Doe")
            // ...

            // doc.setPdfFileName("mydoc.pdf");
            doc.save(Utils.createExternalFile("sample_output.fdf", mFileList).getAbsolutePath())
            doc.close()
            mOutputListener!!.println("Done. Results saved in sample_output.fdf")
        } catch (e: java.lang.Exception) {
            mOutputListener!!.printError(e.getStackTrace())
        }
        for (file in mFileList) {
            addToFileList(file)
        }
        printFooter(outputListener)
    }

    companion object {
        private var mOutputListener: OutputListener? = null
        private val mFileList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    }

    init {
        setTitle(R.string.sample_fdf_title)
        setDescription(R.string.sample_fdf_description)
    }
}