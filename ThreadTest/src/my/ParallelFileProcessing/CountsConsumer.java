package my.ParallelFileProcessing;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/* AUTHOR : VAMSI THOTAKURA */

public class CountsConsumer implements Runnable{

	private int aggregateCountA = 0; 
	private int aggregateCountC = 0;
	private int aggregateCountG = 0;
	private int aggregateCountT = 0;
	private CountsMetaData aggregates; //Objects created by File consumer class threads. 

	private BlockingQueue<CountsMetaData> consumerQueue = null; //Reference to queue shared with File Consumer threads.
	private BlockingQueue<String> sequencesQueue = null;
	private BlockingQueue<Result> resultsQueue = null;

	private int linesProcessed;
	private int lineConsumerThreadsCount;

	private String fileName;
	private FileRead myReader;

	public CountsConsumer(BlockingQueue<CountsMetaData> consumerQueue, BlockingQueue<String>sequencesQueue, BlockingQueue<Result>resultssQueue,int lineConsumerThreadsCount, String fileName, FileRead reader) {
		this.consumerQueue = consumerQueue;
		this.sequencesQueue = sequencesQueue;
		this.lineConsumerThreadsCount = lineConsumerThreadsCount;
		this.fileName = fileName;
		this.resultsQueue = resultssQueue;
		this.myReader = reader;
	}

	@Override
	public void run() {

		while(linesProcessed <= myReader.getLinesToProcess()) { //COndition for thread exit.Will terminate when all sequence lines are processed. Logic is based a kill switch object.
			try {
				aggregates = consumerQueue.take();  //Consumes objects containing counts of individual characters per line per file. Aggregates the total counts for summarizing results. Pushes the end result onto a new queue for writer thread to consume.
				linesProcessed++;
				aggregateCountA = aggregateCountA + aggregates.getCountsA();
				aggregateCountC = aggregateCountC + aggregates.getCountsC();
				aggregateCountG = aggregateCountG + aggregates.getCountsG();
				aggregateCountT = aggregateCountT + aggregates.getCountsT();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		synchronized (sequencesQueue) {        //DEAD LOCK HANDLING TO KILL FILE CONSUMER THREADS
			for(int i=0; i< lineConsumerThreadsCount; i++) {
				sequencesQueue.add(Constants.RETURN_HOME);	
			}
			try{
				sequencesQueue.notifyAll();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		queueResults();
		return;
	}

	public void queueResults() {
		Result result = new Result(); //Create final result object.
		HashMap<Character,Integer> aggregatesMap = new HashMap<Character,Integer>();
		aggregatesMap.put(Constants.CHARACTER_CONSTANT_A, aggregateCountA);
		aggregatesMap.put(Constants.CHARACTER_CONSTANT_C, aggregateCountC);
		aggregatesMap.put(Constants.CHARACTER_CONSTANT_G, aggregateCountG);
		aggregatesMap.put(Constants.CHARACTER_CONSTANT_T, aggregateCountT);

		ResultsProcessor processor = new ResultsProcessor(); //Acts as a delegate layer. Can add new functions depending on new requirements.

		processor.breakDownFileName(result, fileName); //Format results as per requirements
		processor.setFrequencyResults(result, aggregatesMap); //Format results as per requirements

		synchronized (resultsQueue) {
			resultsQueue.add(result);  //Put them onto a queue for Writer thread to consume.
			try{
				resultsQueue.notifyAll();
			}catch(Exception e){

			}
		}


	}

}
