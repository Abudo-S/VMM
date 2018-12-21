/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmm;

import java.util.*;

/**
 *
 * @author Dell
 */
public class TLB {
     private static int counter=0;
     private static int[] lru=new int[16];
     private static ArrayList<ArrayList<String>> tlb=new ArrayList<ArrayList<String>>();   
     
     public int get_size(){
         return tlb.size();
     }
     
     public void add_to_TLB(String Npage,String Nframe){
         ArrayList<String> arr=new ArrayList<>();
             arr.add(Npage);
             arr.add(Nframe);
         if(tlb.size()!=16){
             tlb.add(arr);
         }else{  //lru 
            int index=0;
            int min =lru[0];
            for(int i=1;i<16;i++){
                if(lru[i]<min){
                      min=lru[i];
                      index=i;
                    }
            }
            tlb.set(index,arr);
            int max =lru[0];
            for(int i=1;i<16;i++){
                if(lru[i]>min){
                      max=lru[i];
                      index=i;
                    }
            }
            lru[index]=max;//give the highest priority to the new row
         }
         System.out.println("TLB is updated.");
         
     }
     
     public String get_Nframe_TLB(String Npage){ // check tlb
         for(int i=0;i<tlb.size();i++){
            if(tlb.get(i).contains(Npage)){
                lru[i]++;    
               return tlb.get(i).get(1); //tlb hit
            }
         }
         return ""; //tlb miss
     }
     
     public double get_tlb_hit_rate(int addresses_counter){
         return (double)counter/(double)addresses_counter;
     }
     
     public void increase_counterTLB(){ //TLB counter
           counter++;
     }
     
}
