package de.tungbeier


import de.redsix.pdfcompare.PdfComparator
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.apache.pdfbox.text.PDFTextStripperByArea

class PdfComparision {
    static def PATH = "/home/tung/downloads"

    static void main(String... args) {
        def actual = new File("$PATH/test.pdf")
        def expected = new File("$PATH/test2.pdf")

        pdfCompare(expected, actual)
    }

    static def pdfBox(File file) {
        PDDocument document = null
        try {
            document = PDDocument.load(file)
            document.getClass()

            if (document.isEncrypted()) {
                println('Cannot open the pdf')
                return
            }

            def stripper = new PDFTextStripperByArea()
            stripper.setSortByPosition(true)

            def tStripper = new PDFTextStripper()

            def pdfFileInText = tStripper.getText(document)
            pdfFileInText.split("\\r?\\n").each {
                line -> println(line)
            }
        } finally {
            document.close()
        }
    }

    static def pdfCompare(File expected, File actual) {
        // https://github.com/red6/pdfcompare
        def comparator = new PdfComparator(expected, actual)

        def compareResult = comparator.compare()
        if (compareResult.isNotEqual()) {
            compareResult.writeTo("$PATH/result")
            println('PDFs are not equal')
        }
    }
}
