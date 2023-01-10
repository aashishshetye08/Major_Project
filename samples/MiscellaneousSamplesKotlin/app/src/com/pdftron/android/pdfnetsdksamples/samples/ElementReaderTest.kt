//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

package com.pdftron.android.pdfnetsdksamples.samples

import com.pdftron.android.pdfnetsdksamples.OutputListener
import com.pdftron.android.pdfnetsdksamples.PDFNetSample
import com.pdftron.android.pdfnetsdksamples.R
import com.pdftron.android.pdfnetsdksamples.util.Utils
import com.pdftron.common.PDFNetException
import com.pdftron.pdf.Element
import com.pdftron.pdf.ElementReader
import com.pdftron.pdf.PDFDoc
import com.pdftron.pdf.PageIterator
import java.util.*

class ElementReaderTest : PDFNetSample() {
    init {
        setTitle(R.string.sample_elementreader_title)
        setDescription(R.string.sample_elementreader_description)
    }

    override fun run(outputListener: OutputListener?) {
        super.run(outputListener)
        mOutputListener = outputListener
        mFileList.clear()
        printHeader(outputListener!!)

        try
        // Extract text data from all pages in the document
        {
            mOutputListener!!.println("-------------------------------------------------")
            mOutputListener!!.println("Sample 1 - Extract text data from all pages in the document.")
            mOutputListener!!.println("Opening the input pdf...")

            val doc = PDFDoc(Utils.getAssetTempFile(PDFNetSample.INPUT_PATH + "newsletter.pdf")!!.absolutePath)
            doc.initSecurityHandler()

            val pgnum = doc.pageCount

            val itr: PageIterator
            val page_reader = ElementReader()

            itr = doc.pageIterator
            while (itr.hasNext())
            //  Read every page
            {
                page_reader.begin(itr.next())
                ProcessElements(page_reader)
                page_reader.end()
            }

            //Close the open document to free up document memory sooner.
            doc.close()
            mOutputListener!!.println("Done.")
        } catch (e: Exception) {
            mOutputListener!!.printError(e.stackTrace)
        }

        for (file in mFileList) {
            addToFileList(file)
        }
        printFooter(outputListener)
    }

    companion object {

        private var mOutputListener: OutputListener? = null

        private val mFileList = ArrayList<String>()

        @Throws(PDFNetException::class)
        internal fun ProcessElements(reader: ElementReader) {
            var element: Element? = reader.next()
            while (element != null)
            // Read page contents
            {
                when (element.type) {
                    Element.e_path                 // Process path data...
                    -> {
                        val data = element.pathData
                        val operators = data.operators
                        val points = data.points
                    }
                    Element.e_text                // Process text strings...
                    -> {
                        val data = element.textString
                        mOutputListener!!.println(data)
                    }
                    Element.e_form                // Process form XObjects
                    -> {
                        reader.formBegin()
                        ProcessElements(reader)
                        reader.end()
                    }
                }
                element = reader.next()
            }
        }
    }

}