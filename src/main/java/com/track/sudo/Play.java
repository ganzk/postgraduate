package com.track.sudo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

//import SQLconnect.UserDao;
//import shudu.Main;
//import vo.User;

//游戏界面的设计
public class Play {

    //设置全局变量，九宫格
    JTextField[][] numberFields=new JTextField[9][9];

    //游戏数独数组
    int[][] gamearr=new int[9][9];

    //游戏答案数组
    int[][] answerarr=new int[9][9];

    //灰色
    Color green=new Color(93, 200, 138);
    Color lightGray = new Color(217, 217, 217);
    Color blue = new Color(102, 183, 255);

    //挖空数!
    private int grade=40;
    //过去的
    private int usedgrade=0;
    //计时器
    Timer time;
    JLabel timeLabel;
    TimerTask task;

    //用标签表示游戏进展
    JLabel labelsc=new JLabel();

    //创建字体，之后所有的字体为该字体
    Font f=new Font("方正仿宋简体", Font.BOLD, 25);

    //选择九宫格中的格子的坐标
    int sx=10;
    int sy=10;

    //是否已经看过答案
    private boolean isanswer;

    //用户名
    JLabel username=new JLabel();
    JLabel usergrade=new JLabel();
    private int gamegrade=0;//游戏分数

    //登录
//    login loger=new login();
//    User user=loger.user;

    //读取文件名
    private String filename=new String("D:\\test\\shudu.txt");

    //以前游戏数组
    private int[][] usedgame=new int[9][9];

    //以前的游戏时间
    private int usedtim=1;

    //判断是不是新用户
    //是否为新用户
    private boolean isnew=true;

    //保存用户名存在在第几行
    private int act=-1;
    //关卡
    JLabel levels=new JLabel();
    int levs=1;
    //窗口
    private JFrame jf ;

    public Play(){
        //制作游戏界面
//        System.out.println(user.getUsername()+" "+user.getUserpwd()+" "+user.getUsergrade());
        System.out.println("gzk" + " "+ "eee" +" "+ "90");
        jf = new JFrame();
        jf.setTitle("数独游戏");
        jf.setBounds(400, 0, 1100, 1000);
        jf.setResizable(false);//不允许窗口最大化
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);//不执行任何操作;要求程序在已注册的 WindowListener 对象的 windowClosing 方法中处理该操作
        jf.setLocationRelativeTo(null);//居中，不用定位窗口大小
        jf.setVisible(true);
        //打开时，判断是否上一局游戏
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent arg0) {
                //如果是新用户，保存数组
                //保存记录,读取文件,读出文件,在开始游戏的时候读出
                File file=new File(filename);
                BufferedReader reader = null;
                try {
                    System.out.println("以行为单位读取文件内容，一次读一整行：");
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
                    // reader = new BufferedReader(new FileReader(file));
                    String tempString = null;
                    int line = 1;
                    // 一次读入一行，直到读入null为文件结束
                    while ((tempString = reader.readLine()) != null) {
                        // 显示行号
                        System.out.println(" line " + line + ": " + tempString);
                        if(tempString.equals("gzk")){  // todo user.getUsername()
                            act=line;
                            isnew=false;
                        }
                        else if(isnew==false){
                            //关卡
                            levs=Integer.valueOf(tempString);
                            System.out.println(levs);
                            break;
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
                newGame(grade,levs);
            }
        });
        //关闭时,保存关卡记录
        jf.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                BufferedReader reader = null;
                //保存记录,读取文件,读出文件,在开始游戏的时候读出
                File file=new File(filename);
                int num=0;//第几行
                boolean flag=false;
                System.out.println("act:"+act);
                List list = new ArrayList();
                try{
                    reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"utf-8"));
                    // reader = new BufferedReader(new FileReader(file));
                    String str = null;
                    // 一次读入一行，直到读入null为文件结束
                    while ((str = reader.readLine())!= null) {
                        ++num;
                        System.out.println(num+"行："+str);
                        if(isnew==false&&(num==act||num==act+1)){
                            System.out.println(str);
                            continue;
                        }
                        list.add(str);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
                System.out.println("list size:"+list.size());
                System.out.println("删除成功");
                //重新写入
                try{
                    FileWriter fileWritter = new FileWriter(filename);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
                    //BufferedWriter bw= new BufferedWriter(fileWritter);
                    for( int i=0;i<list.size();i++ ){
                        System.out.println("list["+i+"]"+list.get(i));
                        bw.write(list.get(i).toString());
                        bw.newLine();
                    }
                    String data = "gzk";  // todo user.getUsername()
                    //用户名,保存用户所通过的关卡？
                    bw.write(data+"\r\n");
                    //关卡
                    bw.write(levs+"\r\n");
                    System.out.println("Done");
                    bw.flush();
                    bw.close();


                }catch(IOException e){
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        //中间数独部分

        JPanel bodyPanel=new JPanel();
        bodyPanel.setBackground(lightGray);
        jf.add(bodyPanel,BorderLayout.CENTER);
        bodyPanel.setLayout(new GridLayout(9,9,0,0));


        //右边布局，难易程度，计时，重新开始，换一题等选择按钮
        JPanel rightPanel=new JPanel();
        //宽度大小设置
        rightPanel.setPreferredSize(new Dimension(200, 789));
        jf.add(rightPanel, BorderLayout.EAST);

        //显示用户名和当前分数
        username.setText("用户名:  "+ "gzk");  // todo user.getUsername()
        username.setFont(f);
        rightPanel.add(username);
        usergrade.setText("总分数:   "+ "90"); // todo user.getUsergrade()
        usergrade.setFont(f);
        rightPanel.add(usergrade);
        //显示当前关卡
        levels.setText("关卡:    "+levs);
        levels.setFont(f);
        rightPanel.add(levels);
        //难度选择,默认为容易
        JLabel label1=new JLabel("请选择模式");
        label1.setFont(f);
        rightPanel.add(label1);
        //容易，中等，难
        String[] btnstr={"容易","中等","难"};
        for(int i=0;i<3;i++){
            JButton btn=new JButton(btnstr[i]);
            btn.setFont(f);
            btn.setBackground(lightGray);
            btn.setPreferredSize(new Dimension(120,60));
            //为难度按钮加上监听器
            btn.addActionListener(new funactionListener());

            rightPanel.add(btn);

            //加监听器
        }

        //功能按钮
        JLabel label2=new JLabel("功能按钮 ");
        label2.setFont(f);
        rightPanel.add(label2);
        //换一题
        JButton changebtn=new JButton("换一题");
        changebtn.setFont(f);
        changebtn.setPreferredSize(new Dimension(120,60));
        changebtn.setBackground(lightGray);
        changebtn.setForeground(Color.black);
        changebtn.addActionListener(new funactionListener());
        rightPanel.add(changebtn);

        //重做
        JButton newbtn=new JButton("重玩");
        newbtn.setFont(f);
        newbtn.setPreferredSize(new Dimension(120,60));
        newbtn.setBackground(lightGray);
        newbtn.setForeground(Color.BLACK);
        newbtn.addActionListener(new funactionListener());
        rightPanel.add(newbtn);


        //答案
        JButton answerbtn=new JButton("答案");
        answerbtn.setFont(f);
        answerbtn.setPreferredSize(new Dimension(120,60));
        answerbtn.setBackground(Color.red);
        answerbtn.setForeground(Color.WHITE);
        answerbtn.addActionListener(new funactionListener());
        rightPanel.add(answerbtn);


        //计时
        JLabel label3=new JLabel("    计时    ");
        label3.setFont(f);
        rightPanel.add(label3);
        timeLabel=new JLabel("00:00");
        timeLabel.setFont(f);
        rightPanel.add(timeLabel);

        //放一个游戏进展
        rightPanel.add(labelsc);
        labelsc.setVisible(false);


        //下面布局
        JPanel lastPanel=new JPanel();
        jf.add(lastPanel,BorderLayout.SOUTH);
        lastPanel.setPreferredSize(new Dimension(1333, 100));
        lastPanel.setLayout(new GridLayout(0, 10, 0, 0));

        //放选择按钮
        JButton[] setNum=new JButton[10];
        for(int i=0;i<10;i++){
            if(i==9){
                setNum[i]=new JButton("清除");
                //加清除的监听器
            }
            else{
                setNum[i]=new JButton(i+1+"");
                //加按钮的监听器
            }
            setNum[i].setFont(f);
            setNum[i].setForeground(Color.WHITE);
            setNum[i].setBackground(green);
            setNum[i].setPreferredSize(new Dimension(90,70));
            setNum[i].setFocusPainted(false);
            //加下方按钮监听器
            setNum[i].addActionListener(new buttonaction());
            lastPanel.add(setNum[i]);
        }
        //对中间布局进行控制
        //组件边框
        Border centernBorder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY);
        Border rightAndBottomBorder = BorderFactory.createMatteBorder(1, 1, 4, 4, Color.GRAY);
        Border bottomBorder = BorderFactory.createMatteBorder(1, 1, 4, 1, Color.GRAY);
        Border rightBorder = BorderFactory.createMatteBorder(1, 1, 1, 4, Color.GRAY);
        //循环设置组件JTextField,九宫格
        //numberFields = new JTextField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                //System.out.println(i+j+"");
                numberFields[i][j] = new JTextField();
                //不可编辑
                numberFields[i][j].setEditable(false);
                numberFields[i][j].setFont(new Font("微软雅黑", Font.BOLD, 35));
                numberFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                numberFields[i][j].setBackground(Color.WHITE);
                numberFields[i][j].setForeground(Color.BLACK);
                //加边框！
                if (i == 2 && j == 2 || i == 2 && j == 5 || i == 5 && j == 2 || i == 5 && j == 5) {
                    numberFields[i][j].setBorder(rightAndBottomBorder);
                } else if (j == 2 || j == 5) {
                    numberFields[i][j].setBorder(rightBorder);
                } else if (i == 2 || i == 5) {
                    numberFields[i][j].setBorder(bottomBorder);
                } else {
                    numberFields[i][j].setBorder(centernBorder);
                }
                //对每个格子加上名称来标识
                numberFields[i][j].setName(Integer.toString(i) + Integer.toString(j));

                //对每个格子加上监听器
                numberFields[i][j].addFocusListener(new textfocusaction());;
                bodyPanel.add(numberFields[i][j]);
            }
        }
    }
    //开始新游戏
    public void newGame(int grade,int level){
        isanswer=false;
        //显示总分数
        usergrade.setText("总分数: "+ "80");  // user.getUsergrade()
        //显示当前关卡
        levels.setText("关卡:"+levs+"");
        Main maker=new Main();
        maker.diger(grade, level);
        gamearr=maker.getArr();
        answerarr=maker.getAnswer();
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++)
                System.out.print(answerarr[i][j]);
            System.out.println();
        }
        System.out.println("###################");
        //先清空九宫格
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if(numberFields[i][j] == null){
                    numberFields[i][j] = new JTextField();
                }
                if(gamearr[i][j]!=0){
                    numberFields[i][j].setText(gamearr[i][j]+"");
                    numberFields[i][j].setBackground(lightGray);
                    numberFields[i][j].setForeground(Color.BLACK);
                    //加监听器!
                }
                else{
                    numberFields[i][j].setText("");
                    numberFields[i][j].setBackground(Color.WHITE);
                    numberFields[i][j].setForeground(Color.BLACK);
                }
            }
        //开始计时
        startTime();
    }

    //游戏开始计时
    public void startTime(){
        //游戏开始计时!
        time = new Timer();
        task = new TimerTask() {
            int count = usedtim;

            @Override
            public void run() {

                timeLabel.setText(gettime(count));
                count++;
            }
        };
        time.schedule(task, 1000L, 1000L); // 开始游戏时自动计时
    }

    //时间重新计时
    public void restartTime(){
        //删除time记录，要使用过才能删除
        time.cancel();
        time=new Timer();
        timeLabel.setText("00:00");
        task = new TimerTask() {
            int count = 1;

            @Override
            public void run() {

                timeLabel.setText(gettime(count));
                count++;

            }
        };
        time.schedule(task, 1000L, 1000L);
    }
    //将时间转换成分:秒
    public String gettime(int count){
        String second = null;
        String minute = null;
        if (count / 60 < 10) {
            minute = "0" + (count / 60);
        } else {
            minute = "" + (count / 60);
        }
        if (count % 60 < 10) {
            second = ":0" + count % 60;
        } else {
            second = ":" + count % 60;
        }
        return minute + second;

    }

    //重玩该关
    public void again(){
        //重新设置
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if(gamearr[i][j]!=0){
                    numberFields[i][j].setText(gamearr[i][j]+"");
                    numberFields[i][j].setBackground(lightGray);
                    //加监听器!
                }
            }
    }

    //输出答案!!
    public void answer(){
        //gamearr中为0的地方放答案！！
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                if(gamearr[i][j]==0){
                    numberFields[i][j].setText(answerarr[i][j]+"");
                    numberFields[i][j].setForeground(Color.RED);
                }
            }
    }

    //清空九宫格！！！重新对获取新数独
    public void resetfields(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                numberFields[i][j].setText("");
                numberFields[i][j].setBackground(Color.WHITE);
            }
    }
    //下面为各事件的处理!!!

    //右边处理数独按钮监听器（难度类别监听，换一题，重玩，答案）
    class funactionListener implements ActionListener{
        //难度监听器
        String btnstr;
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            btnstr=e.getActionCommand();
            if(btnstr.equals("容易")){
                grade=40;
                //若选择按钮难度为容易，重新开始游戏
                resetfields();
                newGame(grade,levs);
                restartTime();
            }
            else if(btnstr.equals("中等")){
                grade=45;
                //若选择按钮难度为中等，重新开始游戏
                resetfields();
                newGame(grade,levs);
                restartTime();
            }
            else if(btnstr.equals("难")){
                //同上
                grade=50;
                resetfields();
                newGame(grade,levs);
                restartTime();
            }
            else if(btnstr.equals("换一题")){
                resetfields();
                newGame(grade,levs);
                restartTime();
            }
            //重新开始计分
            else if(btnstr.equals("重玩")){
                resetfields();
                again();
                restartTime();
            }
            //将分归零
            else if(btnstr.equals("答案")){
                answer();
                time.cancel();
                isanswer=true;
            }
        }

    }
    //九宫格数组的监听器
    class textfocusaction implements FocusListener{

        //该各自获得焦点
        @Override
        public void focusGained(FocusEvent e) {
            // TODO Auto-generated method stub
            JTextField jt=(JTextField) e.getSource();
            //点击之前还原颜色
            replace();
            sameNumber(jt);
        }

        //该格子失去焦点,将颜色还原
        @Override
        public void focusLost(FocusEvent e) {
            // TODO Auto-generated method stub
            labelsc.setVisible(false);
            replace();
        }
    }

    //处理获得焦点的文本格子
    public void sameNumber(JTextField jt){
        String name=jt.getName();
        System.out.println(name);
        int x=Integer.parseInt(name.substring(0,1));
        int y=Integer.parseInt(name.substring(1));
        String number=jt.getText();

        //System.out.println(x+"  "+y);
        if(gamearr[x][y]!=0){
            sx=10;
            sy=10;
        }
        else{
            sx=x;
            sy=y;
            System.out.println(sx+"  "+sy);
            //格子为空
            if(number.equals("")){
                //使该行该列和该小九宫格的颜色置blue，提醒用户
                for(int i=0;i<9;i++){
                    numberFields[i][sy].setBackground(blue);
                    numberFields[sx][i].setBackground(blue);
                }
                for(int i=(sx/3)*3;i<((sx/3)+1)*3;i++)
                    for(int j=(sy/3)*3;j<((sy/3)+1)*3;j++)
                        numberFields[i][j].setBackground(blue);
                numberFields[sx][sy].setBackground(green);
            }
            //格子不为空，使与格子相同的数显示粉色
            else{
                for(int i=0;i<9;i++)
                    for(int j=0;j<9;j++)
                        if(numberFields[i][j].getText().equals(number))
                            numberFields[i][j].setBackground(Color.pink);
            }
        }

    }

    //使格子颜色还原
    public void replace(){
        if(sx<10&&sy<10){
            for(int i=0;i<9;i++){
                if(gamearr[i][sy]!=0)
                    numberFields[i][sy].setBackground(lightGray);
                else
                    numberFields[i][sy].setBackground(Color.WHITE);
                if(gamearr[sx][i]!=0)
                    numberFields[sx][i].setBackground(lightGray);
                else
                    numberFields[sx][i].setBackground(Color.WHITE);
            }
            for(int i=(sx/3)*3;i<((sx/3)+1)*3;i++)
                for(int j=(sy/3)*3;j<((sy/3)+1)*3;j++)
                    if(gamearr[i][j]!=0)
                        numberFields[i][j].setBackground(lightGray);
                    else
                        numberFields[i][j].setBackground(Color.WHITE);
            for(int i=0;i<9;i++)
                for(int j=0;j<9;j++)
                    if(numberFields[i][j].getText().equals(numberFields[sx][sy].getText())&&!(i==sx&&j==sy)){
                        if(gamearr[i][j]!=0)
                            numberFields[i][j].setBackground(lightGray);
                        else
                            numberFields[i][j].setBackground(Color.WHITE);

                    }
        }

    }

    //为下方的按钮增加监听器

    class buttonaction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            String btnstr;
            btnstr=e.getActionCommand();
            replace();
            //如果按钮为清除，则将格子置空
            if(btnstr.equals("清除")&&(sx<10&&sy<10)){
                numberFields[sx][sy].setText("");
                numberFields[sx][sy].setBackground(Color.white);
            }
            //若为其他的，在格子中放入值
            else if(sx<10&&sy<10){
                numberFields[sx][sy].setText(btnstr);
                numberFields[sx][sy].setForeground(Color.blue);
                numberFields[sx][sy].setBackground(Color.white);

                //判断值放的是否正确，若不正确，则将该值和相冲突的格子背景颜色置为红色

                jugewrong(btnstr);
            }
        }


    }

    //判断值放的是否正确，若不正确，则将该值和相冲突的格子背景颜色置为红色
    public void jugewrong(String number){
        boolean flag=false; //该值是否正确
        //行列是否有相同的
        for(int i=0;i<9;i++){
            if(i!=sy&&(numberFields[sx][i].getText().equals(number))){
                numberFields[sx][i].setBackground(Color.red);
                flag=true;
            }
            if(i!=sx&&(numberFields[i][sy].getText().equals(number))){
                numberFields[i][sy].setBackground(Color.red);
                flag=true;
            }
        }
        //小九宫格内是否有已经存在过这个值
        for(int i=(sx/3)*3;i<((sx/3)+1)*3;i++)
            for(int j=(sy/3)*3;j<((sy/3)+1)*3;j++){
                if(!(i==sx&&j==sy)&&(numberFields[i][j].getText().equals(number))){
                    numberFields[i][j].setBackground(Color.red);
                    flag=true;
                }
            }
        if(flag){
            labelsc.setText("已有该数字,请检查!");
            labelsc.setFont(new Font("方正仿宋简体", Font.BOLD, 21));
            labelsc.setForeground(Color.RED);
            labelsc.setVisible(true);
        }
        else{
            //挑战成功后!!!
            if(isanswer==false&&gamesc()){
                //关卡加一
                levs++;
                time.cancel();
                String runtime=new String();
                runtime=timeLabel.getText();
                System.out.println(runtime);
                //转换成秒
                int tim=((runtime.charAt(0)-48)*10+(runtime.charAt(1))-48)*60+
                        (runtime.charAt(3)-48)*10+runtime.charAt(4)-48;
                //计分规则
                System.out.println(tim);
                if(grade==40){
                    if(tim<=180)
                        gamegrade=80;
                    else if(tim<=600)
                        gamegrade=70;
                    else if(tim<=1800)
                        gamegrade=60;
                    else
                        gamegrade=50;
                }
                else if(grade==45){
                    if(tim<=180)
                        gamegrade=90;
                    else if(tim<=600)
                        gamegrade=85;
                    else if(tim<=1800)
                        gamegrade=75;
                    else
                        gamegrade=65;
                }
                else{
                    if(tim<=180)
                        gamegrade=100;
                    else if(tim<=600)
                        gamegrade=90;
                    else if(tim<=1800)
                        gamegrade=85;
                    else
                        gamegrade=80;
                }
//                user.setUsergrade(user.getUsergrade()+gamegrade);
//                System.out.println(user.getUsergrade());
//                UserDao dao=new UserDao();
//                dao.modifyuser(user);
                //弹出一个成功的对话框!
                Object[] options = { "查看排名", "继续挑战"};
                //查看排名是0，继续挑战是1
                int x = JOptionPane.showOptionDialog(null, "挑战成功!分数为"+gamegrade,  "挑战成功!",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                System.out.println(x);
                if(x==0){
//                    rank ranker;
//                    ranker=new rank();
                    //newGame(grade);
                }
                else{
                    newGame(grade,levs);
                }
            }
        }

    }
    //判断点完之后数独是否成功!
    public boolean gamesc(){
        for(int i=0;i<9;i++)
            for(int j=0;j<9;j++){
                System.out.println(numberFields[i][j].getText());
                System.out.println("点击!");
                if(numberFields[i][j].getText().equals("")){
                    // System.out.println("111失败!");
                    return false;
                }
                for(int k=0;k<9;k++)
                    if((k!=j&&numberFields[i][k].getText().equals(numberFields[i][j].getText()))||(k!=i&&numberFields[k][j].getText().equals(numberFields[i][j].getText()))){
                        numberFields[i][j].setBackground(Color.RED);
                        numberFields[i][k].setBackground(Color.red);
                        return false;
                    }
                for(int m=(i/3)*3;m<(i/3+1)*3;m++)
                    for(int n=(j/3)*3;n<(j/3+1)*3;n++)
                        if(!(m==i&&n==j)&&(numberFields[m][n].getText().equals(numberFields[i][j].getText())))
                        {
                            return false;
                        }
            }
        System.out.println("成功!");
        return true;
    }

}

