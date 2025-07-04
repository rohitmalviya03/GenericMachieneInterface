package Server;

public class PacsResultDetails {
    private String patName;
    private String age;
    private String testName;
    private String wardNo;
    private String labNo;
    private String sampleType;
    private String crNo;
    private String gender;
    private String reportText;
    private String accessionNo;
    private String base64Pdf;

    // Constructors, getters, setters, toString, etc.
    
    public PacsResultDetails(String patName, String age, String testName, String wardNo, String labNo, String sampleType,
                             String crNo, String gender, String reportText, String accessionNo, String base64Pdf) {
        this.patName = patName;
        this.age = age;
        this.testName = testName;
        this.wardNo = wardNo;
        this.labNo = labNo;
        this.sampleType = sampleType;
        this.crNo = crNo;
        this.gender = gender;
        this.reportText = reportText;
        this.accessionNo = accessionNo;
        this.base64Pdf = base64Pdf;
    }
    
    // Getters and setters
}
