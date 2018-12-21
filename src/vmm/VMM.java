/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmm;

import java.io.*;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Dell
 */
public class VMM {

  private static TLB tlb=new TLB();
  private static PageTable pagetable=new PageTable();   
  public static Semaphore sem=new Semaphore(1);
   public static void main(String[] args) {
        //physical memory is 65536 bytes
       //from 0 to 255 frame1 ,from 256 to 511(255+256) frame2 ...etc
       //each frame/page has 256 bytes(2^8),so each offset will take 8 bits
        try{
           File file = new File("pagenumberProb.txt");
           File file2 = new File("addresses.txt");
           File file3 = new File("output.txt");
           int size=500;//number of addresses
         if(!file.exists()){
             System.out.println("Creating all page number probabilities in 'pagenumberProb' file");
             PrintWriter pw=new PrintWriter(new FileOutputStream(file,false));        
          for (int i = 0; i < 2; i++){
            for (int j = 0; j < 2; j++){
              for (int k = 0; k < 2; k++){
                for (int l = 0; l < 2; l++){
                  for (int m = 0; m < 2; m++){
                    for (int n = 0; n < 2; n++){
                      for (int o = 0; o < 2; o++){
                        for (int p = 0; p < 2; p++){
                          pw.println("****************" + i + j + k + l + m + n + o + p+"********");
                        }
                      }
                    }
                  }
                }
              }
            }
          }
            pw.close(); 
         }
         if(!file2.exists()){
            System.out.println("Creating random 32bit addresses in 'addresses' file");
            PrintWriter pw2=new PrintWriter(new FileOutputStream(file2,false)); 
            Random r=new Random();
            for(int i=0;i<size;i++){
                pw2.println(String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))
                +String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))
                +String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))
                +String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))
                +String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2))+String.valueOf(r.nextInt(2)));
            }
            pw2.close();
         }
        // String mask="00000000000000001111111111111111";
         Scanner sc=new Scanner(new FileReader(file2));
         String st;
         /*BigInteger b1=new BigInteger(mask,2);
         BigInteger b2;*/
         PrintWriter pw3=new PrintWriter(new FileOutputStream(file3,false));
        
         
         pagetable.start();
         
         int counter=0;
         Operation[] op=new Operation[size];
         while(sc.hasNextLine()){
             st=sc.nextLine();
            /* b2=new BigInteger(st,2);
             String value=b1.and(b2).toString(2);
             int len=value.length();
             if(len<16){
                 int dif=16-len;
                for(int i=0;i<dif;i++){
                     value="0"+value;
                 }
             }*/
           //  op[counter]= new Operation(pw3,value,tlb,pagetable);
           op[counter]= new Operation(pw3,st,tlb,pagetable);
               op[counter].start();
             counter++;
         // System.out.println(value);
         }
         for (int i=0;i<counter;i++){
             op[i].join();
         }
            double rate=tlb.get_tlb_hit_rate(counter);
            System.out.println("----------");
            pw3.println("----------");
            System.out.println("tlb hit rate :"+rate);
            pw3.println("tlb hit rate :"+rate);
            rate=pagetable.get_page_fault_rate(counter);
            pw3.println("page fault rate :"+rate);
            System.out.println("page fault rate :"+rate);
            rate=pagetable.get_tlb_miss_rate(counter);
            System.out.println("tlb miss rate :"+rate);
            pw3.println("tlb miss rate :"+rate);
          pw3.close();
        }catch(Exception e){
            System.out.println(e+",in main");
        }
    }
    
}