package com.track.sudo;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main {

    private int[][] Arry;   //得到一个9*9的数独
    private int[][] shudu;  //挖空arry数组中的数字
    private int[][] answer; //存储数独答案
    private int[][] game;  //最终数独
    private int grade;
    private int[] row; //每一行的个数
    private int[] col;  //每一列的个数
    private int[] places;  //每一个九宫格的个数
    private boolean flag=false;
    //读取文件名
    private String filename=new String("D:\\test\\shudu.txt");
    public static void main(String[] args){
        Main mainer=new Main();
        mainer.UI();

        Play play = new Play();

//        play.newGame();

    }

    public void UI(){
        for(int k=0;k<100;k++){
            this.Arry=new int[9][9];
            this.shudu=new int[9][9];
            this.game=new int[9][9];
            this.answer=new int[9][9];
            this.row=new int[9];
            this.col=new int[9];
            this.places=new int[9];
            this.grade=grade;
            flag=false;
            //初始化数组
            for(int i=0;i<9;i++)
                row[i]=col[i]=places[i]=9;
            //调试
            // this.answer=new int[9][9]; //最终答案存储再arry中
            rand();//先产生15个随机数加上随机位置,一定需要随机生成，不然就会一直都是一个数组

            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++)
                    System.out.print(Arry[i][j]);
                System.out.println();
            }
            dfs(Arry,0);//获得一个数组答案d

            // diger(grade);//挖空数组
            //将100个数独写入文件中

            try{
                String data = "";
                File file =new File(filename);
                //if file doesnt exists, then create it
                if(!file.exists()){
                    file.createNewFile();
                }
                //true = append file
                FileWriter fileWritter = new FileWriter(filename,true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write(k+1+"\r\n");
                for(int i=0;i<9;i++){
                    data="";
                    for(int j=0;j<9;j++){
                        data=data+answer[i][j]+"";
                    }
                    System.out.println(data);
                    bufferWritter.write(data+"\r\n");
                }
                bufferWritter.close();
                System.out.println("Done");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    //随机给数
    public void rand(){
        int t=0;
        //t=14不随机性太高，容易产生没有解的数独，经过参考资料发现，当t=6的时候，几乎100%有解
        while(t<6){
            int x=new Random().nextInt(9);
            int y=new Random().nextInt(9);
            int i=new Random().nextInt(9)+1;
            if(Arry[x][y]==0){
                if(istrue(Arry,x,y,i)){  //判断数是否能填
                    Arry[x][y]=i;
                    t++;
                }
            }
        }
    }
    //判断在arry[x][y]上是否能放num
    public boolean istrue(int arry[][],int x,int y,int num){
        //横竖是否有num
        for(int i=0;i<9;i++){
            if(arry[x][i]==num||arry[i][y]==num)
                return false;
        }

        for(int i=(x/3)*3;i<(x/3+1)*3;i++)
            for(int j=(y/3)*3;j<(y/3+1)*3;j++)
                if(arry[i][j]==num)
                    return false;
        return true;
    }
    //根据前面放的数获得一个正确的答案，dfs获取
    public void dfs(int arry[][],int n){
        if(n<81){
            if(flag==true) return;
            int x=n/9;//x第N个数的横坐标
            int y=n%9;//y第N个数的纵坐标
            if(arry[x][y]==0){
                //若第N个数为0，没有被填过，则判断0~9是否能被填
                for(int i=1;i<10;i++){
                    if(istrue(arry,x,y,i)){
                        //第N个数可以填i，填入然后dfs
                        arry[x][y]=i;
                        dfs(arry,n+1);
                        //dfs回溯
                        arry[x][y]=0;
                    }
                }
            }
            else{
                dfs(arry,n+1);
            }
        }
        else{
            //获得第一个结果,flag置true!!!!!

            flag=true;
            //将获得的数组放入shudu中然后再挖空
            //if(all==false){
            for(int i=0;i<9;i++)
                for(int j=0;j<9;j++)
                    shudu[i][j]=answer[i][j]=arry[i][j];

            System.out.println("###################");
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++)
                    System.out.print(arry[i][j]);
                System.out.println();
            }
        }
    }
    //为了避免数独无解,保证数独有唯一解
    //挖空数组,分难易程度,,grade为挖空个数
    //是否有一个行、列、九宫格已经为空
    boolean emptyrow=false,emptycol=false,emptyplaces=false;
    //挖空数、关卡
    public void diger(int grade,int level){
        this.shudu=new int[9][9];
        this.game=new int[9][9];
        this.answer=new int[9][9];
        this.row=new int[9];
        this.col=new int[9];
        this.places=new int[9];
        this.grade=grade;
        File file=new File(filename);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
            // reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1,k=0;
            boolean flag=false;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null&&k<9) {
                // 显示行号
                System.out.println(" line " + line + ": " + tempString);
                if(tempString.equals(level+"")){
                    flag=true;
                    continue;
                }
                if(flag==true){
                    for(int i=0;i<9;i++)
                        answer[k][i]=tempString.charAt(i)-48;
                    k++;
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                shudu[i][j]=answer[i][j];
        int t=grade;
        while(t>0){
            //随机抽到x,y
            int x=new Random().nextInt(9);
            int y=new Random().nextInt(9);
            //若x,y没有被挖空则挖空x,y
            if(shudu[x][y]!=0){
                row[x]--;
                col[y]--;
                places[(y/3)*3+x/3]--;
                if((row[x]==0&&emptyrow)||(col[y]==0&&emptycol)||(places[(y/3)*3+x/3]==0&&emptyplaces))
                {
                    System.out.println(x+" "+y+" 不可以");
                    continue;
                }
                else{
                    shudu[x][y]=0;
                    t=t-1;}
                if(row[x]==0)
                    emptyrow=true;
                if(col[y]==0)
                    emptycol=true;
                if(places[(y/3)*3+x/3]==0)
                    emptyplaces=true;
            }
        }


        //获得最终游戏数独
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++)
                game[i][j]=shudu[i][j];

    }
    //获得最终游戏数独
    public int[][] getArr(){
        int t=0;
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                System.out.print(game[i][j]);
                if(game[i][j]==0)
                    t++;

            }
            System.out.println();
        }
        System.out.println("###################");
        System.out.println("挖空数为:"+t);
        return this.game;
    }

    //获得答案数独
    public int[][] getAnswer(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++)
                System.out.print(answer[i][j]);
            System.out.println();
        }
        System.out.println("###################");
        return this.answer;
    }
}

