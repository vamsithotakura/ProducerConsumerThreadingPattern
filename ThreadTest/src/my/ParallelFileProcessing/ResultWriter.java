package my.ParallelFileProcessing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/* AUTHOR : VAMSI THOTAKURA *
 * Aggregator instances (one per file) would be pushing their final results onto a shared queue. 
 * ResultWriter thread would then be flushing the output to summary.txt file.
 * Multiple aggregator instances would be pushing onto the queue shared between threads of CountsConsumer class and this particular class*/

public class ResultWriter implements Runnable {

	private BlockingQueue<Result> resultsQueue = null;
	private int totalFiles;
	BufferedWriter writer = null;

	public ResultWriter(BlockingQueue<Result> resultsQueue, int totalFiles, String outputPath) {
		this.resultsQueue = resultsQueue;
		this.totalFiles = totalFiles;
		try {
			writer = new BufferedWriter(new FileWriter(outputPath,true));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try{
			while(totalFiles > 0) {
				Result result = resultsQueue.take(); //Consumes from aggregator queue and writes out to Summary file. 
				totalFiles--; //Condition to kill thread.

				writer.write(Constants.FILENAME);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getFileName()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.FLOWCELL);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getFlowCell()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.PROJECT);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getProject()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.SUBPROJECT);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getSubProject()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.METHOD);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getMethod()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.ID);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+"'"+result.getId()+"'"+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.CODE);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+String.valueOf(result.getCode())+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.A_FREQ);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+String.valueOf(result.getA_freq())+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.T_FREQ);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+String.valueOf(result.getT_freq())+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.G_FREQ);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+String.valueOf(result.getG_freq())+Constants.COMMA);
				writer.newLine();

				writer.write(Constants.C_FREQ);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.TAB);
				writer.write(Constants.COLON);
				writer.write(Constants.SPACE+String.valueOf(result.getC_freq()));
				writer.newLine();

				writer.write("**********************************");
				writer.newLine();
				writer.flush();

			}
		}catch(Exception e){

		} finally {
			System.out.println(Constants.PROCESSING_FINISHED);
			if(writer!=null) {
				try{
					writer.close(); //CLOSE STREAM
				}catch(Exception e){

				}
			}
		}

	}

}
	
	


