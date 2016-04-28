package my.ParallelFileProcessing;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

      /*Producer Thread*
       * Author: VAMSI THOTAKURA *
       * Read file line and by line and push the sequences onto a queue, for consumers to consume*/

public class FileRead implements Runnable{

	private Scanner myScanner = null;
	private BlockingQueue<String> queue = null;

	private int totalSequencesToProcess;

	public FileRead(BlockingQueue<String> queue, String filePath) {
		try{
			myScanner = new Scanner(new FileInputStream(filePath));
			this.queue = queue;
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public int getLinesToProcess(){
		return this.totalSequencesToProcess;
	}

	@Override
	public void run() {

		int index = 1;
		int n = 0;
		int sequenceLineNumber = 0;
		String sequence = "";

		while(myScanner.hasNextLine()){
			//Each line on positions {2, 6, .. 4n + 2 ..}

			sequenceLineNumber = 4*(n)+2;

			if(index == sequenceLineNumber){
				sequence = myScanner.nextLine();
				totalSequencesToProcess++;
				queue.add(sequence); //Read file line by line and push them onto a queue. This queue will be shared with threads of File Consumer class.
				n++;
				index++;
			} else {
				myScanner.nextLine();
				index++;
			}
		}

		myScanner.close();  //Close stream
		queue.add(Constants.END_OF_FILE);  //--> Acts as a signal for thread wait.
	}
}
