package kos.cvut.getdata;



/**
 * 
 * @author Tonda Novak (http://qmsource.net/), 2011
 *
 */

public class Course {

	private String nameCz = "";
	private String nameEn = "";
	private String code = "";
	private int credits = 0;
	private String completion = "";
//	private String classesLang;
//	private String department;
//	private String descriptionCz;
//	private String descriptionEn;
//	private String keywordsCz;
//	private String keywordsEn;
//	private Line lecturesContentsCz;
//	private List<LineEntry> lecturesContentsCz;
//	private String lecturesContentsEn;
//	private String literatureCz;
//	private String literatureEn;
//	private String noteCz;
//	private String noteEn;
//	private String objectivesCz;
//	private String objectivesEn;
//	private String range;
//	private String requirementsCz;
//	private String requirementsEn;
//	private String semesterSeason;
//	private String studyForm;
//	private String status;
//	private String tutorialsContentsCz;
//	private String tutorialsContentsEn;
	private String url = "";
	private TimetableSlot timetableSlot = new TimetableSlot();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public TimetableSlot getTimetableSlot() {
		return timetableSlot;
	}

	public void setTimetableSlot(TimetableSlot timetableSlot) {
		this.timetableSlot = timetableSlot;
	}

	public Course(String nameCZ,String code,String completion, int credits, String url){
		this.nameCz = nameCZ;
		this.code = code;
		this.completion = completion;
		this.credits = credits;
		this.url = url;
	}
	
	public Course(String nameCZ,String code,String completion, int credits, String url, TimetableSlot timetableSlot){
		this.nameCz = nameCZ;
		this.code = code;
		this.completion = completion;
		this.credits = credits;
		this.url = url;
		this.timetableSlot = timetableSlot;
	}
	
	public String getNameCz() {
		return nameCz;
	}

	public void setNameCz(String nameCz) {
		this.nameCz = nameCz;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public String getCompletion() {
		return completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

//	public String getClassesLang() {
//		return classesLang;
//	}
//
//	public void setClassesLang(String classesLang) {
//		this.classesLang = classesLang;
//	}
//
//	public String getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(String department) {
//		this.department = department;
//	}
//
//	public String getDescriptionCz() {
//		return descriptionCz;
//	}
//
//	public void setDescriptionCz(String descriptionCz) {
//		this.descriptionCz = descriptionCz;
//	}
//
//	public String getDescriptionEn() {
//		return descriptionEn;
//	}
//
//	public void setDescriptionEn(String descriptionEn) {
//		this.descriptionEn = descriptionEn;
//	}
//
//	public String getKeywordsCz() {
//		return keywordsCz;
//	}
//
//	public void setKeywordsCz(String keywordsCz) {
//		this.keywordsCz = keywordsCz;
//	}
//
//	public String getKeywordsEn() {
//		return keywordsEn;
//	}
//
//	public void setKeywordsEn(String keywordsEn) {
//		this.keywordsEn = keywordsEn;
//	}

//	public Line getLecturesContentsCz() {
//		return lecturesContentsCz;
//	}
//
//	public void setLecturesContentsCz(Line lecturesContentsCz) {
//		this.lecturesContentsCz = lecturesContentsCz;
//	}

//	public String getLecturesContentsEn() {
//		return lecturesContentsEn;
//	}
//
//	public void setLecturesContentsEn(String lecturesContentsEn) {
//		this.lecturesContentsEn = lecturesContentsEn;
//	}
//
//	public String getLiteratureCz() {
//		return literatureCz;
//	}
//
//	public void setLiteratureCz(String literatureCz) {
//		this.literatureCz = literatureCz;
//	}
//
//	public String getLiteratureEn() {
//		return literatureEn;
//	}
//
//	public void setLiteratureEn(String literatureEn) {
//		this.literatureEn = literatureEn;
//	}
//
//	public String getNoteCz() {
//		return noteCz;
//	}
//
//	public void setNoteCz(String noteCz) {
//		this.noteCz = noteCz;
//	}
//
//	public String getNoteEn() {
//		return noteEn;
//	}
//
//	public void setNoteEn(String noteEn) {
//		this.noteEn = noteEn;
//	}
//
//	public String getObjectivesCz() {
//		return objectivesCz;
//	}
//
//	public void setObjectivesCz(String objectivesCz) {
//		this.objectivesCz = objectivesCz;
//	}
//
//	public String getObjectivesEn() {
//		return objectivesEn;
//	}
//
//	public void setObjectivesEn(String objectivesEn) {
//		this.objectivesEn = objectivesEn;
//	}
//
//	public String getRange() {
//		return range;
//	}
//
//	public void setRange(String range) {
//		this.range = range;
//	}
//
//	public String getRequirementsCz() {
//		return requirementsCz;
//	}
//
//	public void setRequirementsCz(String requirementsCz) {
//		this.requirementsCz = requirementsCz;
//	}
//
//	public String getRequirementsEn() {
//		return requirementsEn;
//	}
//
//	public void setRequirementsEn(String requirementsEn) {
//		this.requirementsEn = requirementsEn;
//	}
//
//	public String getSemesterSeason() {
//		return semesterSeason;
//	}
//
//	public void setSemesterSeason(String semesterSeason) {
//		this.semesterSeason = semesterSeason;
//	}
//
//	public String getStudyForm() {
//		return studyForm;
//	}
//
//	public void setStudyForm(String studyForm) {
//		this.studyForm = studyForm;
//	}
//
//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}
//
//	public String getTutorialsContentsCz() {
//		return tutorialsContentsCz;
//	}
//
//	public void setTutorialsContentsCz(String tutorialsContentsCz) {
//		this.tutorialsContentsCz = tutorialsContentsCz;
//	}
//
//	public String getTutorialsContentsEn() {
//		return tutorialsContentsEn;
//	}
//
//	public void setTutorialsContentsEn(String tutorialsContentsEn) {
//		this.tutorialsContentsEn = tutorialsContentsEn;
//	}
	
	
	
}
