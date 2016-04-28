package my.ParallelFileProcessing;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/* AUTHOR : VAMSI THOTAKURA *
 * Threads of this class consume input files line by line
 * Each thread of this class will be acting on sequence strings found per file
 * There will be a single producer (FileRead class thread) pushing sequence strings onto a shared queue
 * Multiple threads of this class will be consuming*/

public class FileConsumer implements Runnable {

	private BlockingQueue<String> consumerQueue = null;
	private BlockingQueue<CountsMetaData> producerQueue = null;

	public FileConsumer(BlockingQueue<String> consumerQueue, BlockingQueue<CountsMetaData> producerQueue){
		this.consumerQueue = consumerQueue;
		this.producerQueue = producerQueue;
	}

	@Override
	public void run() {

		while(true) {
			try {
				String sequenceString = consumerQueue.take();//Each consumer thread will be acting on a specific String sequence. The out comes of each sequence are pushed onto a queue for further aggregation.
				if(sequenceString !=null) {
					sequenceString = sequenceString.trim();
					if(sequenceString.equals(Constants.RETURN_HOME)) {  //KILL SWITTCH FOR THREADS TO TERMINATE
						return;
					}else if(sequenceString.equals(Constants.END_OF_FILE)) {
						CountsMetaData aggregatedCounts = new CountsMetaData();
						aggregatedCounts.setEndOfFile(true); 
						producerQueue.add(aggregatedCounts); //This Producer Queue will be consumed by an aggregator instance. Aggregator summarizes outcomes of individual threads. 
					} else {
						CountsMetaData aggregatedCounts = sortCounts(sequenceString);
						producerQueue.add(aggregatedCounts);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	//Below method computes individual counts for character sequences - 'A', 'C', 'G', 'T'.
	//Instead of sorting, I am using a HashMap for keeping track of counts.
	public CountsMetaData sortCounts(String sequenceString) {
		HashMap<Character, Integer> countsMap = new HashMap<Character, Integer>();
		for(int i=0; i<sequenceString.length(); i++){
			Character ch = sequenceString.charAt(i);
			if(ch.equals(Constants.CHARACTER_CONSTANT_A)||ch.equals(Constants.CHARACTER_CONSTANT_C)||ch.equals(Constants.CHARACTER_CONSTANT_G)||ch.equals(Constants.CHARACTER_CONSTANT_T)){
				if(countsMap.containsKey(ch)) {
					int val = countsMap.get(ch);
					val++;
					countsMap.put(ch, val);
				} else {
					countsMap.put(ch, 1);
				}
			}
		}
		CountsMetaData aggregatedCounts = new CountsMetaData();
		if(countsMap.containsKey(Constants.CHARACTER_CONSTANT_A)){
			aggregatedCounts.setCountsA(countsMap.get(Constants.CHARACTER_CONSTANT_A));
		} 
		if(countsMap.containsKey(Constants.CHARACTER_CONSTANT_C)) {
			aggregatedCounts.setCountsC(countsMap.get(Constants.CHARACTER_CONSTANT_C));
		}
		if(countsMap.containsKey(Constants.CHARACTER_CONSTANT_G)){
			aggregatedCounts.setCountsG(countsMap.get(Constants.CHARACTER_CONSTANT_G));
		}
		if(countsMap.containsKey(Constants.CHARACTER_CONSTANT_T)) {
			aggregatedCounts.setCountsT(countsMap.get(Constants.CHARACTER_CONSTANT_T));
		}
		aggregatedCounts.setEndOfFile(false);
		return aggregatedCounts;
	}


}
