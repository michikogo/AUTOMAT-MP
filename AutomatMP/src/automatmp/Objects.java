/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatmp;

/**
 *
 * @author Mart
 */
public class Objects {
    //true earth
    // false mars
    public boolean[] things = new boolean[5];
    public Objects(){
        for(int i=0;i<5;i++){
            things[i]=true;
        }
        
    }
    public boolean[] getthings(){
        return things;
    }
    public void change(int i,boolean change){
        things[i]= change;
    }
    //0 man1
    //1 man2
    //2 lion
    //3 cow;
    //4 grain
    //check functions
    //true game over
    //false safe
    public boolean Earthmanlion(){
        if((things[0]||things[1]) && things[2])
            return true;
        else
            return false;
    }
    public boolean Marsmanlion(){
        if((!things[0]||!things[1])&&!things[2])
            return true;
        else
            return false;
    }
    public boolean Earthmancow(){
        if( (things[0]||things[1]) && things[3])
            return true;
        else
            return false;
    }
    public boolean Marsmancow(){
        if((!things[0]||!things[1])&&!things[3])
            return true;
        else
            return false;
    }
    public boolean Earthlioncow(){
        if(things[2] && things[3])
            return true;
        else
            return false;
    }
    public boolean Marslioncow(){
        if(!things[2] &&!things[3])
            return true;
        else
            return false;
    }
    public boolean Earthcowgrain(){
        if(things[4] && things[3])
            return true;
        else
            return false;
    }
    public boolean Marscowgrain(){
        if(!things[4] &&!things[3])
            return true;
        else
            return false;
    }
    public boolean win(){
        if(!things[0]&&!things[1]&&!things[2]&&!things[3]&&!things[4])
            return true;
        else
            return false;
    }

}
