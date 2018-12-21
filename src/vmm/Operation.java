/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmm;

import java.io.PrintWriter;
import java.math.BigInteger;

/**
 *
 * @author Dell
 */
public class Operation extends Thread{
    private PrintWriter pw;
    private String logical_address;
    private TLB tlb;
    private PageTable pagetable;
    private boolean if_pagefault=false; // page fault handler
    
    public Operation(PrintWriter pw,String logical_address,TLB tlb,PageTable pagetable){
      this.pw=pw;  
      this.logical_address=logical_address;
      this.tlb=tlb;
      this.pagetable=pagetable;
    }
    
    @Override
    public void run(){
        try{
        this.logical_address=this.with_mask(this.logical_address);
        int len=this.logical_address.length();
        String Npage=this.logical_address.substring(0,8);
        String offset=this.logical_address.substring(8);
        VMM.sem.acquire();
        String Nframe=tlb.get_Nframe_TLB(Npage);
          Thread.sleep(1);
          this.all_states(Nframe, Npage, offset);
        VMM.sem.release();
        while(this.if_pagefault==true){  //page fault handler
            VMM.sem.acquire();
             this.all_states(Nframe, Npage, offset);
             VMM.sem.release();
            Thread.sleep(200);
        }
        }catch(Exception e){
            System.out.println(e+",in "+this.getName());
        }
    }
    
     private String get_physical_address(String Nframe,String offset){ //returns octal physical address
        int x=Integer.parseInt(Nframe,2);
        String str=Integer.toBinaryString(x);
        str+=offset;
        int v=Integer.parseInt(str,2);
      //  return Integer.toOctalString(v);
      return Integer.toUnsignedString(v);
    }
     
    private void all_states(String Nframe,String Npage,String offset){
         if(Nframe.equals("")){
            System.out.println("TLB miss with logical address:*"+this.logical_address);
            Nframe=pagetable.get_Nframe_PT(Npage);
            if(Nframe.equals("")){ 
                System.out.println("page fault with logical address:*"+this.logical_address);   
                 if(!this.if_pagefault){
                 this.pagetable.increase_counterPT();
                 }
                pw.println("*"+this.logical_address+" ,page fault");
                this.if_pagefault=true;
            }else{
                if(!this.if_pagefault){
                this.pagetable.increase_counterPT_tlb_miss();
                }
               this.tlb.add_to_TLB(Npage, Nframe);
               pw.println("logical address :*"+this.logical_address+",physical address :"+this.get_physical_address(Nframe,offset));
               this.if_pagefault=false;
            }
        }else{
             System.out.println("TLB hit with logiacl address:*"+this.logical_address);
              if(!this.if_pagefault){
                 this.tlb.increase_counterTLB();
              }
             pw.println("logical address :*"+this.logical_address+",physical address :"+this.get_physical_address(Nframe,offset));
             this.if_pagefault=false;
        }
    }
    
    private String with_mask(String full_address){
        String mask="00000000000000001111111111111111";
        BigInteger b1=new BigInteger(mask,2);
        BigInteger b2=new BigInteger(full_address,2);
            String value=b1.and(b2).toString(2);
             int len=value.length();
             if(len<16){
                 int dif=16-len;
                for(int i=0;i<dif;i++){
                     value="0"+value;
                 }
             }
        return value;
    }
     
}
