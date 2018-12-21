/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmm;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dell
 */
public class PageTable extends Thread{
     private static ArrayList<String> frames=new ArrayList<String>(); //should contain 256 framenumber
     private static int counter=0;
     private static int tlb_miss_counter=0;
     
     @Override
     public void run(){
         try {
            for(int i=1;i<=256;i++){
             frames.add(String.valueOf(i));
             Thread.sleep(1);
            }
         }catch(Exception e) {
            System.out.println(e+",in PageTable");
         }
     }
     
     public String get_Nframe_PT(String Npage){
         
         int value=Integer.parseInt(Npage,2);
         if(value<=128){
             value+=128;
         }else{
             value-=128;
         }
         if(frames.contains(String.valueOf(value))){
           return String.valueOf(Integer.toBinaryString(value));
         }
         return "";
     }
     
     public double get_page_fault_rate(int addresses_counter){
         return (double)counter/(double)addresses_counter;
     }
     
     public void increase_counterPT(){ //page fault counter
           counter++;
     }
     
     public void increase_counterPT_tlb_miss(){ //page fault counter
          tlb_miss_counter++;
     }
     
     public double get_tlb_miss_rate(int addresses_counter){  
         return (double)tlb_miss_counter/(double)addresses_counter;
     }
}
