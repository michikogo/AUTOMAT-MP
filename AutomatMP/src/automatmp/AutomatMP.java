/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatmp;

import java.util.ArrayList;

/**
 *
 * @author Mart
 */
public class AutomatMP {

    /**
     * @param args the command line arguments
     */
    public static ArrayList <ArrayList<Objects>> Solutions  = new <ArrayList<Objects>>  ArrayList();
    public static ArrayList <Objects> EarthReachable = new <Objects>ArrayList();
    public static ArrayList <Objects> MarsReachable = new <Objects>ArrayList();
    public static void main(String[] args) {
        ArrayList<Objects>steps = new ArrayList();
        Objects start = new Objects();
        //calling the recursion function
        int x = EarthMode(steps,start);
        System.out.println("Fastest Solution: "+(x-1)+" Moves");
        System.out.println("Total Reachable States that follow the rules: " + (EarthReachable.size()+MarsReachable.size()));
        System.out.println("Total Solutions (without reusing a state): "+Solutions.size());
        System.out.println("STATES");
        System.out.println("========================================");
        printsolution(EarthReachable,MarsReachable);
        System.out.println("========================================");
        System.out.println("SOLUTIONS");
        for(int i=0;i<Solutions.size();i++){
            if(Solutions.get(i).size()==8){
                System.out.println("========================================");
                printsolution(Solutions.get(i));
            }
                
        }
    }
    public static int EarthMode(ArrayList <Objects> pastmoves, Objects newmove){
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
    
    
    public static  int MarsMode(ArrayList <Objects> pastmoves, Objects newmove){
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
    public static  boolean checkEarthstuff(Objects move){
        //true = impossible
        //false = possible
        return move.Earthcowgrain() || move.Earthlioncow() || move.Earthmancow() || move.Earthmanlion();
    }
    public static  boolean checkMarsstuff(Objects move){
        return move.Marscowgrain() || move.Marslioncow() || move.Marsmancow() || move.Marsmanlion();
    }
    public static  boolean checkPast(ArrayList <Objects> pastmoves, Objects newmove){

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
    public static  ArrayList <Integer> Truearray(boolean[] array){
        ArrayList <Integer> ints = new <Integer>ArrayList();
        for(int i = 0;i<array.length;i++)
            if(array[i])
                ints.add(i);
        return ints;
                
    }
    public static  ArrayList <Integer> FalseArray(boolean[] array){
        ArrayList <Integer> ints = new <Integer>ArrayList();
        for(int i = 0;i<array.length;i++)
            if(!array[i])
                ints.add(i);
        return ints;
    }
    public static  int countTrue(boolean[] array){
        int ctr=0;
        for(int i = 0;i<array.length;i++)
            if(array[i])
                ctr++;
        return ctr;
                
    }
    public static int countFalse(boolean[] array){
        int ctr=0;
        for(int i = 0;i<array.length;i++)
            if(!array[i])
                ctr++;
        return ctr;
                
    }
    public static ArrayList<Objects> addingPast(ArrayList <Objects> pastmoves, Objects newmove){
        Objects nextmove = new Objects();
        
        for(int i=0;i<5;i++)
            nextmove.change(i, newmove.getthings()[i]);
        
        pastmoves.add(nextmove);
        return pastmoves;
    }
    public static ArrayList<Objects> copyingPast(ArrayList <Objects> newpastmoves,ArrayList <Objects> pastmoves){
        for(int i =0;i<pastmoves.size();i++){
            newpastmoves.add(pastmoves.get(i));
        }
        return newpastmoves;
    }
    public static void printsolution(ArrayList <Objects> strat){
        
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
    public static void printsolution(ArrayList <Objects> earth,ArrayList <Objects> mars){
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
    public static String getperson(int x){
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
    private static String s = "S";
}
