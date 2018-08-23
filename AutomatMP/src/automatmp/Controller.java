/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatmp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Mart
 */
public class Controller {
    private ArrayList <ArrayList<Objects>> Solutions  = new <ArrayList<Objects>>  ArrayList();
    private ArrayList <Objects> EarthReachable = new <Objects>ArrayList();
    private ArrayList <Objects> MarsReachable = new <Objects>ArrayList();
    private ArrayList <Objects> AllReach = new <Objects>ArrayList();
    private ArrayList <ArrayList<Objects>> GuiSolutions  = new <ArrayList<Objects>>  ArrayList();
    private Objects GuiState = new Objects();
    private MainGui MGui = new MainGui();
    private DFAgui DGui = new DFAgui();
    private Timer timer = new Timer();
    public Controller(){
        MGui.MoveListener(new MoveListen());
        MGui.setVisible(true);
        DGui.setVisible(true);
        ArrayList<Objects>steps = new ArrayList();
        Objects start = new Objects();
        //calling the recursion function
        int x = EarthMode(steps,start);
        for (int i =0;i<MarsReachable.size();i++){
            AllReach.add(EarthReachable.get(i));
            AllReach.add(MarsReachable.get(i));
        }
        for(int i=0;i<Solutions.size();i++){
            if(Solutions.get(i).size()!=x){
                Solutions.remove(i);
                i--;
            }
                
        }
        ArrayList <ArrayList<Objects>> newo  = new <ArrayList<Objects>>  ArrayList();
        newo = duplicate(newo,Solutions);
        DGui.setShortest(Solutions);
        DGui.setState(AllReach);
        DGui.setShortestC(newo);
        System.out.println("READY");
        //System.out.println("Fastest Solution: "+(x-1)+" Moves");
        //System.out.println("Total Reachable States that follow the rules: " + (EarthReachable.size()+MarsReachable.size()));
        //System.out.println("Total Solutions (without reusing a state): "+Solutions.size());
        //System.out.println("STATES");
        //System.out.println("========================================");
        //printsolution(EarthReachable,MarsReachable);
        //System.out.println("========================================");
        //System.out.println("SOLUTIONS");
//        for(int i=0;i<Solutions.size();i++){
//            if(Solutions.get(i).size()==8){
//                System.out.println("========================================");
//                printsolution(Solutions.get(i));
//            }
//                
//        }
        
    }
    public ArrayList <ArrayList<Objects>> duplicate(ArrayList <ArrayList<Objects>> newo, ArrayList <ArrayList<Objects>> old){
        for(int i=0;i<old.size();i++){
            newo.add(old.get(i));
        }
        return newo;
    }
    public int EarthMode(ArrayList <Objects> pastmoves, Objects newmove){
        //recursion when scientist is on earth
        if(checkMarsstuff(newmove)||!checkPast(pastmoves,newmove))
            return 0;
        else{
            if(checkPast(EarthReachable,newmove))
                EarthReachable=addingPast(EarthReachable,newmove);
            //recording this new move so it wont be redone
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            ArrayList <Integer> ints=Truearray(newmove.getthings());
            int trues = ints.size();
            boolean[] nextmoves = new boolean[trues];
            for(int i=0;i<trues;i++)
                nextmoves[i]=true;
            int x = 999;
            Objects nextmove = new Objects();
            for(int i=0;i<5;i++){
                nextmove.change(i,newmove.getthings()[i]);
            }
            //making all next moves
            while(countFalse(nextmoves)!=trues){
                for(int i=0;i<trues;i++){
                    if(nextmoves[i]){
                        nextmoves[i]=false;
                        if(i!=0){
                            i--;
                            for(int j=i;j>=0;j--){
                                nextmoves[j]=true;
                            }
                        }
                        break;
                    }
                }
                int y=999;
                //choosing all next moves that only move 1 or 2 things since rocket has max 3 seats
                if(countFalse(nextmoves)==1||countFalse(nextmoves)==2){
                    for(int i=0;i<trues;i++)
                        nextmove.change(ints.get(i),nextmoves[i] );
                    //recursion intensifies
                    y=MarsMode(newpastmoves,nextmove);
                    if(y==0)
                        y=999;
                }
                //choosing shortest move
                if(y<x)
                    x=y;

            }
            return 1+x;
            
        }
    }
    
    
    public  int MarsMode(ArrayList <Objects> pastmoves, Objects newmove){
        //recursion when scientist is on mars
        if(checkEarthstuff(newmove)||!checkPast(pastmoves,newmove))
            return 0;
        else if(newmove.win()){
            if(checkPast(MarsReachable,newmove))
                MarsReachable=addingPast(MarsReachable,newmove);
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            Solutions.add(newpastmoves);
            return 1;
        }else{
            if(checkPast(MarsReachable,newmove))
               MarsReachable=addingPast(MarsReachable,newmove);
            //recording this new move so it wont be redone
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            ArrayList <Integer> ints=FalseArray(newmove.getthings());
            int falses = ints.size();
            boolean[] nextmoves = new boolean[falses];
            for(int i=0;i<falses;i++)
                nextmoves[i]=false;
            int x = 999;
            Objects nextmove = new Objects();
            for(int i=0;i<5;i++){
                nextmove.change(i,newmove.getthings()[i]);
            }
            //making all next moves
            while(countTrue(nextmoves)!=falses){
                for(int i=0;i<falses;i++){
                    if(!nextmoves[i]){
                        nextmoves[i]=true;
                        if(i!=0){
                            i--;
                            for(int j=i;j>=0;j--)
                                nextmoves[j]=false;
                        }
                        break;
                    }
                }
                
                int y=999;
                //choosing all next moves that only move 1 or 2 things since rocket has max 3 seats
                if(countTrue(nextmoves)==1||countTrue(nextmoves)==2){
                    for(int i=0;i<falses;i++)
                        nextmove.change(ints.get(i),nextmoves[i] );
                    //recursion intensifies
                    y=EarthMode(newpastmoves,nextmove);
                    if(y==0)
                        y=999;
                }
                //choosing shortest move
                if(y<x)
                    x=y;

            }
            return 1+x;
            
        }
    }
    public  boolean checkEarthstuff(Objects move){
        //true = impossible
        //false = possible
        return move.Earthcowgrain() || move.Earthlioncow() || move.Earthmancow() || move.Earthmanlion();
    }
    public  boolean checkMarsstuff(Objects move){
        return move.Marscowgrain() || move.Marslioncow() || move.Marsmancow() || move.Marsmanlion();
    }
    public  boolean checkEarthstuffGui(Objects move){
        //true = impossible
        //false = possible
        
        if(move.Earthcowgrain()){
            MGui.showend(false, 2);
            MGui.display("The Cow has eaten the grain and is happy");
        }else if(move.Earthlioncow()){
            MGui.showend(false, 1);
            MGui.display("The Lion is showing off its meal");
        }else if(move.Earthmancow() || move.Earthmanlion()){
            MGui.showend(false, 0);
            MGui.display("The humans hunted the animals for dinner");
        }
        return move.Earthcowgrain() || move.Earthlioncow() || move.Earthmancow() || move.Earthmanlion();
    }
    public  boolean checkMarsstuffGui(Objects move){
        //true = impossible
        //false = possible
        if(move.Marscowgrain()){
            MGui.showend(false, 2);
            MGui.display("The Cow has eaten the grain and is happy");
        }else if(move.Marslioncow()){
            MGui.showend(false, 1);
            MGui.display("The Lion is showing off its meal");
        }else  if(move.Marsmancow() || move.Marsmanlion()){
            MGui.showend(false, 0);
            MGui.display("The humans hunted the animals for dinner");
        }
        return move.Marscowgrain() || move.Marslioncow() || move.Marsmancow() || move.Marsmanlion();
    }
    public  boolean checkPast(ArrayList <Objects> pastmoves, Objects newmove){

        if(pastmoves.size()==0)
            return true;
        else{
            boolean orig = true;
            for(int i=0;i<pastmoves.size();i++){
                int j;
                int ctr=0,ctr2=0;
                for(j=0;j<2;j++){
                    if(pastmoves.get(i).getthings()[j])
                        ctr++;
                    if(newmove.getthings()[j])
                        ctr2++;
                }
                
                for(j=2;j<5;j++){
                    if(pastmoves.get(i).getthings()[j]!=newmove.getthings()[j])
                        break;
                }
                if(j==5&&ctr==ctr2){
                    orig=false;
                }
                    
            }

            return orig;
        }
    }
    public  int findstate(ArrayList <Objects> pastmoves, Objects newmove){

        for(int i=0;i<pastmoves.size();i++){
            int j;
            int ctr=0,ctr2=0;
            for(j=0;j<2;j++){
                if(pastmoves.get(i).getthings()[j])
                    ctr++;
                if(newmove.getthings()[j])
                    ctr2++;
            }
            for(j=2;j<5;j++)
                if(pastmoves.get(i).getthings()[j]!=newmove.getthings()[j])
                    break;
            
            if(j==5&&ctr==ctr2)
                return i;
            
        }
        return 18;
    }
    public int EarthModeGui(ArrayList <Objects> pastmoves, Objects newmove){
        //recursion when scientist is on earth
        if(checkMarsstuff(newmove)||!checkPast(pastmoves,newmove))
            return 0;
        else{
            //recording this new move so it wont be redone
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            ArrayList <Integer> ints=Truearray(newmove.getthings());
            int trues = ints.size();
            boolean[] nextmoves = new boolean[trues];
            for(int i=0;i<trues;i++)
                nextmoves[i]=true;
            int x = 999;
            Objects nextmove = new Objects();
            for(int i=0;i<5;i++){
                nextmove.change(i,newmove.getthings()[i]);
            }
            //making all next moves
            while(countFalse(nextmoves)!=trues){
                for(int i=0;i<trues;i++){
                    if(nextmoves[i]){
                        nextmoves[i]=false;
                        if(i!=0){
                            i--;
                            for(int j=i;j>=0;j--){
                                nextmoves[j]=true;
                            }
                        }
                        break;
                    }
                }
                int y=999;
                //choosing all next moves that only move 1 or 2 things since rocket has max 3 seats
                if(countFalse(nextmoves)==1||countFalse(nextmoves)==2){
                    for(int i=0;i<trues;i++)
                        nextmove.change(ints.get(i),nextmoves[i] );
                    //recursion intensifies
                    y=MarsModeGui(newpastmoves,nextmove);
                    if(y==0)
                        y=999;
                }
                //choosing shortest move
                if(y<x)
                    x=y;

            }
            return 1+x;
            
        }
    }
    
    
    public  int MarsModeGui(ArrayList <Objects> pastmoves, Objects newmove){
        //recursion when scientist is on mars
        if(checkEarthstuff(newmove)||!checkPast(pastmoves,newmove))
            return 0;
        else if(newmove.win()){
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            GuiSolutions.add(newpastmoves);
            return 1;
        }else{
            //recording this new move so it wont be redone
            ArrayList <Objects> newpastmoves = new ArrayList();
            newpastmoves=copyingPast(newpastmoves,pastmoves);
            newpastmoves=addingPast(newpastmoves,newmove);
            ArrayList <Integer> ints=FalseArray(newmove.getthings());
            int falses = ints.size();
            boolean[] nextmoves = new boolean[falses];
            for(int i=0;i<falses;i++)
                nextmoves[i]=false;
            int x = 999;
            Objects nextmove = new Objects();
            for(int i=0;i<5;i++){
                nextmove.change(i,newmove.getthings()[i]);
            }
            //making all next moves
            while(countTrue(nextmoves)!=falses){
                for(int i=0;i<falses;i++){
                    if(!nextmoves[i]){
                        nextmoves[i]=true;
                        if(i!=0){
                            i--;
                            for(int j=i;j>=0;j--)
                                nextmoves[j]=false;
                        }
                        break;
                    }
                }
                
                int y=999;
                //choosing all next moves that only move 1 or 2 things since rocket has max 3 seats
                if(countTrue(nextmoves)==1||countTrue(nextmoves)==2){
                    for(int i=0;i<falses;i++)
                        nextmove.change(ints.get(i),nextmoves[i] );
                    //recursion intensifies
                    y=EarthModeGui(newpastmoves,nextmove);
                    if(y==0)
                        y=999;
                }
                //choosing shortest move
                if(y<x)
                    x=y;

            }
            return 1+x;
            
        }
    }
    public  ArrayList <Integer> Truearray(boolean[] array){
        ArrayList <Integer> ints = new <Integer>ArrayList();
        for(int i = 0;i<array.length;i++)
            if(array[i])
                ints.add(i);
        return ints;
                
    }
    public  ArrayList <Integer> FalseArray(boolean[] array){
        ArrayList <Integer> ints = new <Integer>ArrayList();
        for(int i = 0;i<array.length;i++)
            if(!array[i])
                ints.add(i);
        return ints;
    }
    public  int countTrue(boolean[] array){
        int ctr=0;
        for(int i = 0;i<array.length;i++)
            if(array[i])
                ctr++;
        return ctr;
                
    }
    public int countFalse(boolean[] array){
        int ctr=0;
        for(int i = 0;i<array.length;i++)
            if(!array[i])
                ctr++;
        return ctr;
                
    }
    public  ArrayList<Objects> addingPast(ArrayList <Objects> pastmoves, Objects newmove){
        Objects nextmove = new Objects();
        
        for(int i=0;i<5;i++)
            nextmove.change(i, newmove.getthings()[i]);
        
        pastmoves.add(nextmove);
        return pastmoves;
    }
    public ArrayList<Objects> copyingPast(ArrayList <Objects> newpastmoves,ArrayList <Objects> pastmoves){
        for(int i =0;i<pastmoves.size();i++){
            newpastmoves.add(pastmoves.get(i));
        }
        return newpastmoves;
    }
    public void printsolution(ArrayList <Objects> strat){
        
        for(int i=0;i<strat.size();i++){
            
            System.out.print("EARTH: ");
            for(int j=0;j<strat.get(i).things.length;j++){
                if(strat.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            if(i%2==0)
                System.out.print(s);
            System.out.println("");
            System.out.print("MARS: ");
            for(int j=0;j<strat.get(i).things.length;j++){
                if(!strat.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            if(i%2==1)
                System.out.print(s);
            System.out.println("");
            System.out.println("");
        }
    }
    public void printsolution(ArrayList <Objects> earth,ArrayList <Objects> mars){
        for(int i=0;i<earth.size();i++){
            
            System.out.print("EARTH: ");
            for(int j=0;j<earth.get(i).things.length;j++){
                if(earth.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            System.out.println(s);
            System.out.print("MARS: ");
            for(int j=0;j<earth.get(i).things.length;j++){
                if(!earth.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            System.out.println("");
            System.out.println("");
            
                        
            System.out.print("EARTH: ");
            for(int j=0;j<mars.get(i).things.length;j++){
                if(mars.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            System.out.println("");
            System.out.print("MARS: ");
            for(int j=0;j<mars.get(i).things.length;j++){
                if(!mars.get(i).getthings()[j])
                    System.out.print(getperson(j)+" ");
            }
            System.out.println(s);
            System.out.println("");
        }

    }
    public String getperson(int x){
        if(x==0){
            return "H";
        }else if(x==1){
            return "H";
        }else if(x==2){
            return "L";
        }else if(x==3){
            return "C";
        }else
            return "G";
    }
    private String s = "S";
    public void updateGuiObject(){
        if(MGui.getR1()!=5)
            GuiState.change(MGui.getR1(), !MGui.getearth());
        if(MGui.getR2()!=5)
            GuiState.change(MGui.getR2(), !MGui.getearth());
        boolean x;
        if(MGui.getearth())
            x=checkEarthstuffGui(GuiState);
        else
            x=checkMarsstuffGui(GuiState);
        if(x)
             System.out.println("hi");  
        else if(GuiState.win()){
           MGui.showend(true, 0);
           MGui.display("You Win");
        }else{
            DGui.reset();
            DGui.MoveCurrent(findstate(AllReach,GuiState));
            ArrayList<Objects>steps = new ArrayList();
            Objects newstep = new Objects();
            for(int i=0;i<5;i++){
                newstep.change(i,GuiState.getthings()[i]);
            }
            GuiSolutions.clear();
            int z=0;
            if(MGui.getearth())
                z=MarsModeGui(steps,newstep);
            else
                z=EarthModeGui(steps,newstep);
            for(int i=0;i<GuiSolutions.size();i++){
                if(GuiSolutions.get(i).size()!=z){
                    GuiSolutions.remove(i);
                    i--;
                }
            }
            ArrayList <ArrayList<Objects>> newo  = new <ArrayList<Objects>>  ArrayList();
            newo = duplicate(newo,GuiSolutions);
            DGui.setShortestC(newo);
        }
    }
    class RocketMover extends TimerTask{
        int ctr=0;
        public RocketMover(){

        }
        public void run(){
            if(MGui.getearth())
                MGui.moverocketright();
            else
                MGui.moverocketleft();
            ctr++;
            if(ctr==300){
                this.cancel();
                if(MGui.getearth())
                    MGui.leftrocket();
                else
                    MGui.rightrocket();
                updateGuiObject();
                MGui.moveObjects();
                MGui.EnablePeople();
                MGui.disenablemove();
                MGui.changemode();
            }
        }
    }
    class MoveListen implements ActionListener{
        /**
         * does the action
         * @param e contains the action performed
         */
        public void actionPerformed(ActionEvent e) {
            MGui.DisablePeople();
            MGui.DisableRockets();
            MGui.disenablemove();
            RocketMover mover= new RocketMover();
            timer.scheduleAtFixedRate(mover, 0, 10);
        }
    }
}
