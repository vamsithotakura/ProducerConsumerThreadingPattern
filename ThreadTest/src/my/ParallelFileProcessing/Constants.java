package my.ParallelFileProcessing;

/* AUTHOR : VAMSI THOTAKURA *
 * THIS FILE ESENTIALLY ACTS AS A CONFIGURATION MECHANISM
 * COULD CONFIGURE MORE THREADS DEPENDING ON AVAILABLE COMPUTATION*/

public class Constants {

	public static final String RETURN_HOME = "RETURN_HOME";
	public static final String END_OF_FILE = "END_OF_FILE";

	public static final Character CHARACTER_CONSTANT_A = 'A';
	public static final Character CHARACTER_CONSTANT_C = 'C';
	public static final Character CHARACTER_CONSTANT_G = 'G';
	public static final Character CHARACTER_CONSTANT_T = 'T';

	public static final int FILE_READER_THREADS = 5; //CAN BE CONFIGURED AS PER MACHINE.

	public static final String PRODUCER = "PRODCER";
	public static final String CONSUMER = "CONSUMER";
	public static final String AGGREGATOR = "AGGREGATOR";
	public static final String WRITER = "WRITER";

	public static final String FILENAME = "'filename'";
	public static final String FLOWCELL = "'flowcell'";
	public static final String PROJECT = "'project'";
	public static final String SUBPROJECT = "'subproject'";
	public static final String METHOD = "'method'";
	public static final String ID = "'id'";
	public static final String CODE = "'code'";

	public static final String A_FREQ = "'A_freq'";
	public static final String T_FREQ = "'T_freq'";
	public static final String G_FREQ = "'G_freq'";
	public static final String C_FREQ = "'C_freq'";

	public static final String COLON = ":";
	public static final String COMMA = ",";

	public static final String SPACE = " ";
	public static final String TAB = "\t";

	public static final String SEQUENCE_FILE_EXTENSION = ".fastq";
	public static final String SUMMARY_FILE_EXTENSION = "\\summary.txt";

	public static final String SAFETY_CHECK_ONE = "PLEASE PROVIDE A DIRECTORY PATH AS INPUT!";
	public static final String SAFETY_CHECK_TWO = "EMPTY DIRECTORY DETECTED!";
	public static final String SAFETY_CHECK_THREE = "ILLEGAL INPUT PATH DETECTED. PLEASE PROVIDE A VALID DIRECTORY PATH AS INPUT!";
	public static final String SAFETY_CHECK_FOUR = "NO SUCH DIRECTORY EXISTS. PLEASE CHECK YOUR PATH!";
	public static final String SAFETY_CHECK_FIVE = "NO SEQUENCE FILES DETECTED. EXITING COMPUTATION!!";

	public static final String REGEX_UNDERSCORE = "_";
	public static final String REGEX_HYPHEN = "-";
	public static final String REGEX_PERIOD = "\\.";

	public static final String PROCESSING_FINISHED = "PROCESSING DONE";


}
