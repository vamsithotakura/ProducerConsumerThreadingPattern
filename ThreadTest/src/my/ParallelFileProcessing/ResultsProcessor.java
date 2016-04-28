package my.ParallelFileProcessing;

import java.util.HashMap;

/* AUTHOR : VAMSI THOTAKURA *
 * Assuming files with .fastq extension have the same format as mentioned in the requirements.*/

public class ResultsProcessor {

	public void breakDownFileName(Result result, String fileName){
		try{
			result.setFileName(fileName);
			String experimentCompositions[] = fileName.split(Constants.REGEX_UNDERSCORE);
			String store[]; 
			for(int i=0; i< experimentCompositions.length; i++){
				if(i == 0){
					store = experimentCompositions[i].split(Constants.REGEX_HYPHEN);
					result.setFlowCell(store[0]);
					result.setProject(store[1]);
				} else if(i == 1){
					result.setSubProject(experimentCompositions[i]);
				} else if(i == 2){
					result.setMethod(experimentCompositions[i]);
				} else if(i == 3){
					result.setId(experimentCompositions[i]);
				} else if(i == 4){
					store = experimentCompositions[i].split(Constants.REGEX_PERIOD);
					result.setCode(Integer.parseInt(store[0]));
				}
			}
		}catch(Exception e){

		}
	}

	public void setFrequencyResults(Result result, HashMap<Character,Integer>countsMap) {
		result.setA_freq(countsMap.get(Constants.CHARACTER_CONSTANT_A));
		result.setC_freq(countsMap.get(Constants.CHARACTER_CONSTANT_C));
		result.setG_freq(countsMap.get(Constants.CHARACTER_CONSTANT_G));
		result.setT_freq(countsMap.get(Constants.CHARACTER_CONSTANT_T));
	}

}
