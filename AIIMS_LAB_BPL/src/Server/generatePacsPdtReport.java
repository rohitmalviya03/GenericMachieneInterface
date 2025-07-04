package Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class generatePacsPdtReport {

	
	public static String generatePDF(String patientName, String age, String testName,
            String wardNo, String labNo, String sampleType, String crNo,
            String gender, String reportText, String accessionNo) {
Document document = new Document(PageSize.A4, 36, 36, 36, 36);
String filePath = System.getProperty("user.dir") + "\\" + patientName.replace(" ", "_") + "_Report.pdf";
PdfWriter writer = null;

String imagePath = ".//config//aiimsrb_logo.png";
try {
writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
document.open();

Image logo = Image.getInstance(imagePath);
logo.scaleToFit(PageSize.A4.getWidth() - 72, 120);
logo.setAlignment(Image.ALIGN_CENTER);
document.add(logo);

Paragraph spacer = new Paragraph();
spacer.setSpacingBefore(8f);
document.add(spacer);
document.add(Chunk.NEWLINE);

PdfPTable borderWrapper = new PdfPTable(1);
borderWrapper.setWidthPercentage(100);

PdfPCell outerCell = new PdfPCell();
outerCell.setBorder(Rectangle.BOX);
outerCell.setPaddingTop(12f);
outerCell.setPaddingBottom(12f);
outerCell.setPaddingLeft(10f);
outerCell.setPaddingRight(20f);

Font labelFont = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
Font valueFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

Paragraph deptHeading = new Paragraph("Department of Radiology", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
deptHeading.setAlignment(Element.ALIGN_CENTER);
deptHeading.setSpacingAfter(10f);
outerCell.addElement(deptHeading);

PdfPTable testNameTable = new PdfPTable(1);
testNameTable.setWidthPercentage(100);

Phrase testPhrase = new Phrase();
testPhrase.add(new Chunk("", labelFont));     
testPhrase.add(new Chunk(testName, labelFont));         
PdfPCell testCell = new PdfPCell(testPhrase);
testCell.setBorder(Rectangle.NO_BORDER);
testCell.setPaddingLeft(-9f);                           
testCell.setPaddingTop(5f);
testCell.setPaddingBottom(5f);

testNameTable.addCell(testCell);
outerCell.addElement(testNameTable);

// Add other lines and content here as needed...


PdfPTable abhaLineTable = new PdfPTable(new float[]{1.2f, 0.3f, 1.5f});
abhaLineTable.setWidthPercentage(100);
abhaLineTable.setSpacingAfter(10f);

Phrase abhaNoPhrase = new Phrase();
//abhaNoPhrase.add(new Chunk("ABHA NO.: -", labelFont));
PdfPCell abhaNoCell = new PdfPCell(abhaNoPhrase);
abhaNoCell.setBorder(Rectangle.NO_BORDER);

abhaNoCell.setPaddingLeft(-9f);

PdfPCell spacerCell = new PdfPCell(new Phrase(""));
spacerCell.setBorder(Rectangle.NO_BORDER);

Phrase abhaAddrPhrase = new Phrase();
//abhaAddrPhrase.add(new Chunk("ABHA ADDRESS:-                                              ___________________", labelFont));
PdfPCell abhaAddrCell = new PdfPCell(abhaAddrPhrase);
abhaAddrCell.setBorder(Rectangle.NO_BORDER);
abhaAddrCell.setPaddingLeft(-80f);




abhaLineTable.addCell(abhaNoCell);
abhaLineTable.addCell(spacerCell);
abhaLineTable.addCell(abhaAddrCell);

outerCell.addElement(abhaLineTable);

// Line 2: CR No, Lab/Study No and Acceptance Date
PdfPTable line2Table = new PdfPTable(3);
line2Table.setWidthPercentage(100);
line2Table.setSpacingAfter(10f);

Phrase crPhrase = new Phrase();
crPhrase.add(new Chunk(String.format("%-17s", "CR No                :"), labelFont));
crPhrase.add(new Chunk(crNo, valueFont));
PdfPCell crNoCell = new PdfPCell(crPhrase);
crNoCell.setBorder(Rectangle.NO_BORDER);

crNoCell.setPaddingLeft(-9f);
Phrase agePhrase = new Phrase();
agePhrase.add(new Chunk(String.format("%-17s", "Age/Sex              :"), labelFont));
agePhrase.add(new Chunk(""+age + " Yr / "+gender, valueFont));
PdfPCell ageCell = new PdfPCell(agePhrase);
ageCell.setBorder(Rectangle.NO_BORDER);

String timestamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());



Phrase acceptDatePhrase = new Phrase();
acceptDatePhrase.add(new Chunk(String.format("%-17s", "Acceptance Date :"), labelFont));
acceptDatePhrase.add(new Chunk(timestamp, valueFont));

PdfPCell acceptDateCell = new PdfPCell(acceptDatePhrase);
acceptDateCell.setBorder(Rectangle.NO_BORDER);

line2Table.addCell(crNoCell);
//line2Table.addCell(labNoCell);
line2Table.addCell(acceptDateCell);
outerCell.addElement(line2Table);

// Line 3: Patient Name, Age/Sex and Coll./Study Date
PdfPTable line3Table = new PdfPTable(3);
line3Table.setWidthPercentage(100);
line3Table.setSpacingAfter(10f);

Phrase namePhrase = new Phrase();
namePhrase.add(new Chunk(String.format("%-17s", "Patient Name    :"), labelFont));
namePhrase.add(new Chunk(patientName, valueFont));
PdfPCell nameCell = new PdfPCell(namePhrase);
nameCell.setBorder(Rectangle.NO_BORDER);

nameCell.setPaddingLeft(-9f); 

Phrase testP = new Phrase();
testP.add(new Chunk(String.format("%-17s", "Test :"), labelFont));
testP.add(new Chunk(""+age + " Yr / "+gender, valueFont));
PdfPCell testCell2 = new PdfPCell(agePhrase);
testCell2.setBorder(Rectangle.NO_BORDER);

Phrase studyDatePhrase = new Phrase();
studyDatePhrase.add(new Chunk(String.format("%-17s", "Coll./Study Date   :"), labelFont));
studyDatePhrase.add(new Chunk(timestamp, valueFont));


PdfPCell studyDateCell = new PdfPCell(studyDatePhrase);
studyDateCell.setBorder(Rectangle.NO_BORDER);

line3Table.addCell(nameCell);
line3Table.addCell(ageCell);
line3Table.addCell(studyDateCell);
outerCell.addElement(line3Table);

// Line 4: Sample Type/No, Ward/OPD and Reporting Date
PdfPTable line4Table = new PdfPTable(3);
line4Table.setWidthPercentage(100);
line4Table.setSpacingAfter(10f);

Phrase samplePhrase = new Phrase();
samplePhrase.add(new Chunk(String.format("%-14s", "Test Name"), labelFont));
samplePhrase.add(new Chunk(testName, valueFont));
PdfPCell sampleCell = new PdfPCell(samplePhrase);
sampleCell.setBorder(Rectangle.NO_BORDER);

sampleCell.setPaddingLeft(-9f); 

Phrase wardPhrase = new Phrase();
wardPhrase.add(new Chunk(String.format("%-17s", "Ward/OPD          :"), labelFont));
wardPhrase.add(new Chunk(wardNo, valueFont));
PdfPCell wardCell = new PdfPCell(wardPhrase);
wardCell.setBorder(Rectangle.NO_BORDER);

Phrase reportDatePhrase = new Phrase();
reportDatePhrase.add(new Chunk(String.format("%-17s", "Reporting Date    :"), labelFont));
reportDatePhrase.add(new Chunk(timestamp, valueFont));

PdfPCell reportDateCell = new PdfPCell(reportDatePhrase);
reportDateCell.setBorder(Rectangle.NO_BORDER);

line4Table.addCell(sampleCell);
line4Table.addCell(wardCell);
line4Table.addCell(reportDateCell);
outerCell.addElement(line4Table);

// Line 5: Dept/Unit


PdfPTable deptTable = new PdfPTable(1);
deptTable.setWidthPercentage(100);

Phrase deptPhrase = new Phrase();
deptPhrase.add(new Chunk("Dept/Unit           :", labelFont));
deptPhrase.add(new Chunk(" General Medicine Emg", valueFont));

PdfPCell deptCell = new PdfPCell(deptPhrase);
deptCell.setBorder(Rectangle.NO_BORDER);
deptCell.setPaddingLeft(-9f);  // Align to left like other cells
deptCell.setPaddingTop(5f);
deptCell.setPaddingBottom(5f);

deptTable.addCell(deptCell);
outerCell.addElement(deptTable);


borderWrapper.addCell(outerCell);
document.add(borderWrapper);



// Report content
Paragraph resultHeading = new Paragraph("Result", new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD));
resultHeading.setSpacingBefore(20f);
document.add(resultHeading);

Font reportFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
reportText = reportText.replaceAll("\\\\X0D\\\\X0A", "\n");
reportText = reportText.replaceAll("(\\\\?X0D\\\\?X0A)+", "\n");
reportText = reportText.replaceAll("(?m)^\\\\", "");
reportText = reportText.replaceAll("(\\\\X0D\\\\X0A|\\\\X0D|\\\\X0A)+", "\n");
reportText = reportText.replaceAll("\\\\X0D\\\\X0A", "\n"); // Handles encoded newlines
reportText = reportText.replaceAll("\\\\\\\\X0D\\\\\\\\X0A", "\n"); // Handles escaped newlines
reportText = reportText.replaceAll("(\\\\?X0D\\\\?X0A)+", "\n"); // Normalize line breaks
reportText = reportText.replaceAll("(?m)^\\\\", ""); // Remove leading backslashes
reportText = reportText.replaceAll(" +", " "); // Replace multiple spaces with a single space

String[] paragraphs = reportText.split("\n");
for (String paragraph : paragraphs) {
Paragraph reportContent = new Paragraph(paragraph, reportFont);
reportContent.setSpacingBefore(10f);
document.add(reportContent);
}

Paragraph footer = new Paragraph("Report Generated by PACS", new Font(Font.FontFamily.HELVETICA, 10));
footer.setSpacingBefore(40f);
footer.setAlignment(Element.ALIGN_CENTER);
document.add(footer);

document.close();
writer.close();

// Check if the file exists before proceeding to Base64 encoding
if (Files.exists(Paths.get(filePath))) {
String base64String = convertPdfToBase64(filePath);
System.out.println("✅ PDF Report Generated Successfully at: " + filePath);
return base64String != null ? base64String : "";
} else {
System.err.println("❌ Error: PDF file was not created.");
return "";
}

} catch (DocumentException | IOException e) {
System.err.println("❌ Error generating PDF: " + e.getMessage());
e.printStackTrace();
} finally {
if (document.isOpen()) document.close();
if (writer != null) writer.close();
}
return "";
}

public static String convertPdfToBase64(String filePath) {
try {
// Verify file exists before proceeding
if (!Files.exists(Paths.get(filePath))) {
System.err.println("❌ Error: PDF file not found at " + filePath);
return null;
}

byte[] pdfBytes = Files.readAllBytes(Paths.get(filePath));
return Base64.getEncoder().encodeToString(pdfBytes); // Convert PDF to Base64
} catch (IOException e) {
System.err.println("Error reading the file: " + e.getMessage());
e.printStackTrace();
return null;
}
}

}
	

