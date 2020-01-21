package com.mygdx.fuegopeligro;

import java.util.Map;
import java.util.HashMap;

public class Random {
    static java.util.Random random = new java.util.Random();
    // Generate a random integer N within the range lowerBound<=N<=upperBound
    static public int rand(int lowerBound, int upperBound){
        if(lowerBound>upperBound){ // Swap if lowerBound>upperBound
            int tmp=upperBound;
            upperBound=lowerBound;
            lowerBound=tmp;
        }
        return random.nextInt(upperBound-lowerBound+1)+lowerBound;
    }

    static public int[] GenerateRandomNumbers(int lowerBound, int upperBound, int count){
        if(lowerBound>upperBound){ // Swap if lowerBound>upperBound
            int tmp=upperBound;
            upperBound=lowerBound;
            lowerBound=tmp;
        }
        if(count>upperBound-lowerBound+1){ // It is impossible to generate the array
            return null;
        }

        int[] result = new int[count+1];
        int diff=upperBound-lowerBound;

        for(int i=0;i<=count;++i){ // Initialize the array with the first numbers
            result[i]=i+lowerBound;
        }

        Map<Integer,Integer> map = new HashMap<Integer,Integer>();

        for(int i=0;i<=count;++i){
            int index=rand(i,diff);
            if(index<=count){ // The index is in the array, so swap the items
                int tmp=result[i];
                result[i]=result[index];
                result[index]=tmp;
            }
            else if(map.containsKey(index+lowerBound)){ // Lookup the map
                int tmp=map.get(index+lowerBound);
                map.put(index+lowerBound,result[i]);
                result[i]=tmp;
            }
            else{
                map.put(index+lowerBound,result[i]); // Add the item into the map
                result[i]=index+lowerBound;
            }
        }

        return result;
    }
}
