package my.ParallelFileProcessing;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* AUTHOR : VAMSI THOTAKURA *
 * THIS IS THE MAIN PROGRAM
 * RUN THIS FILE TO START THE PIPELINE*
 * PIPELINE SEQUENCE: PRODUCER --> CONSUMER 1 --> CONSUMER 2 --> CONSUMER 3 --> summary.txt
 * */

public class Driver {

	public static int totalSequenceFiles;
	public static boolean sequenceFilesExist = false;

	public static void main(String[] args) {
		String DIRECTORY_PATH = "";
		if(args.length < 1) {
			System.out.println(Constants.SAFETY_CHECK_ONE);
			return;
		} else{ //Processing begins
			DIRECTORY_PATH = args[0];
			Driver myDriver = new Driver();
			myDriver.checkAndCreateOutputFile(DIRECTORY_PATH); //Check if the path is correct and if data files exist.Create summary file if it doesnot exist.
			myDriver.processFiles(DIRECTORY_PATH); //Start data processing. Following Producer-Consumer pattern.
		}
	}

	public void processFiles(String DIRECTORY_PATH){
		try {
			if(sequenceFilesExist){
				File folder = new File(DIRECTORY_PATH);
				File[] listOfFiles = folder.listFiles();

				BlockingQueue<Result> resultsQueue = new LinkedBlockingQueue<Result>();
				ResultWriter writerInstance = new ResultWriter(resultsQueue, totalSequenceFiles, DIRECTORY_PATH+Constants.SUMMARY_FILE_EXTENSION);
				for(File myFile : listOfFiles){
					if(myFile.getName().trim().endsWith(Constants.SEQUENCE_FILE_EXTENSION)){
						initializeThreads(myFile, resultsQueue);
					}
				}
				new Thread(writerInstance,Constants.WRITER).start(); //CONSUMER 3 
			} else {
				System.out.println(Constants.SAFETY_CHECK_FIVE);
				return;
			}
		} catch(Exception e){
		}
	}

	public void checkAndCreateOutputFile(String DIRECTORY_PATH){

		boolean outPutFileExists = false; 
		try {
			File folder = new File(DIRECTORY_PATH);
			if(folder.isDirectory()) {
				File[] listOfFiles = folder.listFiles();
				if(listOfFiles.length > 0){
					for(File myFile : listOfFiles){
						if(myFile.getName().trim().equals(Constants.SUMMARY_FILE_EXTENSION)){ //Check if output file exists
							outPutFileExists = true;
						} else if(myFile.getName().trim().endsWith(Constants.SEQUENCE_FILE_EXTENSION)){ //Check if there are files with .fastq extension
							totalSequenceFiles++;
							sequenceFilesExist = true;
						}
					}
					if(!outPutFileExists && sequenceFilesExist) { //Create Summary.txt file if it doesnot exist.
						File summaryFile = new File(DIRECTORY_PATH+Constants.SUMMARY_FILE_EXTENSION);
						outPutFileExists = summaryFile.createNewFile();
					}
				} else {
					System.out.println(Constants.SAFETY_CHECK_TWO);
					return;
				}
			} else {
				System.out.println(Constants.SAFETY_CHECK_THREE);
				return;
			}
		} catch(Exception e){
			System.out.println(Constants.SAFETY_CHECK_FOUR);
		}
	}

	public void initializeThreads(File myFile, BlockingQueue<Result> resultsQueue){

		BlockingQueue<String> producerQueue = new LinkedBlockingQueue<String>(); 
		BlockingQueue<CountsMetaData> aggregateCountsQueue = new LinkedBlockingQueue<CountsMetaData>(); //Produced by individual String sequence consumer threads. Will be consumed by the aggregator instance for summarizing results.

		//INITIAL PRODUCER
		FileRead producer = new FileRead(producerQueue,myFile.getAbsolutePath()); //Reads input file line by line and pushes String sequence on to a Consumer queue. 

		//CONSUMER 1
		List<FileConsumer> consumerThreads = new LinkedList<FileConsumer>();  //Creating multiple consumers per file. Each consumer thread works on the String sequences pushed by the previous producer. After computing aggregations per line, results are pushed onto another different queue.
		for(int i=0; i<Constants.FILE_READER_THREADS; i++){
			consumerThreads.add(new FileConsumer(producerQueue,aggregateCountsQueue));
		}

		//Aggregator thread will be consuming the outcomes of each consumer thread.
		//CONSUMER 2
		CountsConsumer aggregatorInstance = new CountsConsumer(aggregateCountsQueue,producerQueue,resultsQueue,Constants.FILE_READER_THREADS,myFile.getName(),producer);

		new Thread(producer,Constants.PRODUCER+Constants.SPACE+myFile.getName()).start(); //Start Producer

		int consumerNumber = 1;
		for(FileConsumer consumer : consumerThreads) { //A total of five threads would be consuming each file. 
			new Thread(consumer,Constants.CONSUMER+Constants.SPACE+myFile.getName()+Constants.SPACE+consumerNumber).start(); //Start Consumer 1
			consumerNumber++;
		}

		//Start Consumer 2
		new Thread(aggregatorInstance,Constants.AGGREGATOR+Constants.SPACE+myFile.getName()).start(); //Aggregates the outcomes of all threads per file.

	}

}


