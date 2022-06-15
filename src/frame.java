import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;


public class frame extends JFrame {
    static final int DEFAULT_WIDTH=455;
    static final int DEFAULT_HEIGHT=605;

    JPanel p1,p2,p3;

    int[][] arr=new int[][]{
        {2,2,4,8},
        {8,4,2,4},
        {8,4,2,4},
        {8,4,2,4}
    };
    int[][] undoarr=new int[4][4];

    int sc=0,bsc,loseflag=0;
    int deltasc=0;//每次移动增加的分数

    JLabel scLabel=new JLabel(String.valueOf(sc));
    JLabel bscLabel=new JLabel(String.valueOf(bsc));



    public frame(){
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("2048");
        setBounds(screenSize.width/2-DEFAULT_WIDTH/2,screenSize.height/2-DEFAULT_HEIGHT/2,DEFAULT_WIDTH,DEFAULT_HEIGHT);
        addKeyListener(new movecontrol());
        this.requestFocusInWindow(); 
        getContentPane().setBackground(new Color(0x69c8ce));

        Toolkit kit =Toolkit.getDefaultToolkit();
        Image image=kit.createImage("src/icon/icon.png");
        setIconImage(image);

        init();
    }

    void init(){
        this.setLayout(null);
        reset();
        p1=score();
        p2=bestscore();
        p3 =new JPanel();
		p3.addKeyListener(new movecontrol());
		cude();
        this.add(p1);
        this.add(p2);
        this.add(p3);
        this.add(restartButton());
        this.add(undoButton());
        this.add(shadow1());
        this.add(shadow2());
        this.add(shadow3());

        try {
            BufferedReader in = new BufferedReader(new FileReader("bestscore.txt"));
            String str;
            str = in.readLine();
            bsc=Integer.valueOf(str);
            bscLabel.setText(String.valueOf(bsc));
            in.close();
        } catch (IOException e) {
        }//程序启动时读取上一次的最高分显示在top

        

    }


    JPanel score(){
        JPanel jp1=new JPanel();
        jp1.setLayout(new FlowLayout(FlowLayout.CENTER,0,37));
        Font f = new Font("Arial", Font.BOLD+ Font.ITALIC,28);
        scLabel.setFont(f);
        jp1.setBounds(20,20,170,110);
        jp1.add(scLabel);
        jp1.setBackground(new Color(0x97d8dc));
        scLabel.setForeground(Color.white);
        return jp1;
    }//分数面板

    JPanel bestscore(){
        JPanel jp1=new JPanel();
        jp1.setLayout(new FlowLayout(FlowLayout.LEFT,20,12));
        var lb1=new JLabel("TOP:");
        Font f = new Font("Arial", Font.BOLD+ Font.ITALIC,22);
        lb1.setFont(f);
        bscLabel.setFont(f);
        jp1.setBounds(200,20,220,50);
        jp1.add(lb1);
        jp1.add(bscLabel);
        jp1.setBackground(new Color(0x97d8dc));
        lb1.setForeground(Color.white);
        bscLabel.setForeground(Color.white);
        return jp1;
    }//最高分面板

    JButton restartButton(){
        JButton jb1=new JButton();
        jb1.setLayout(new FlowLayout(FlowLayout.CENTER,0,13));
        var lb1=new JLabel("NEW GAME");
        Font f = new Font("Arial", Font.BOLD+ Font.ITALIC,18);
        lb1.setFont(f);
        jb1.setBounds(200,80,140,50);
        jb1.add(lb1);
        jb1.setBackground(new Color(0x97d8dc));
        lb1.setForeground(Color.white);
        jb1.setBorder(null);

        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loseflag=0;//重置游戏结束标记
                reset();
                cude();
                p3.repaint();
                p3.requestFocus();
                sc=0;//分数归零
                scLabel.setText(String.valueOf(sc));
                
            }
        });//重新开始一局游戏

        return jb1;
    }//重新开始按钮

    JButton undoButton(){
        JButton jb1=new JButton();
        jb1.setLayout(new FlowLayout(FlowLayout.CENTER,0,13));
        var lb1=new JLabel("undo");
        Font f = new Font("Arial", Font.BOLD+ Font.ITALIC,18);
        lb1.setFont(f);
        jb1.setBounds(350,80,70,50);
        jb1.add(lb1);
        jb1.setBackground(new Color(0x97d8dc));
        lb1.setForeground(Color.white);
        jb1.setBorder(null);

        jb1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copy(undoarr,arr);
                sc-=deltasc;//减去这一步增加的分数
                cude();
                p3.repaint();
                p3.requestFocus();//恢复备份的数组
            }
        });


        return jb1;
    }//撤销按钮

    JPanel losepanel(){
        JPanel jp1=new JPanel();
        jp1.setLayout(null);
        jp1.setBounds(0,0, 400, 400);
        jp1.setBackground(new Color(144, 205, 209,200));//设置背景

        JLabel lb1=new JLabel("Game Over!!");
        lb1.setBounds(93,130,350,50);
        Font f1 = new Font("Arial", Font.BOLD+ Font.ITALIC,35);
        lb1.setFont(f1);
        lb1.setForeground(Color.white);
        jp1.add(lb1);

        return jp1;
    }//失败后弹出面板

    public void cude() {//方块界面
		
        

		p3.removeAll();//清除p3的所有组件
		p3.setLayout(null);
		p3.setBackground(new Color(0x91cfd3));//设置背景
		p3.setBounds(20,150, 400, 400);	

        if(loseflag==1){
            p3.add(losepanel());
        }



		for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                JLabel img=new JLabel();
                img.setHorizontalAlignment(SwingConstants.CENTER);
                img.setOpaque(true);
                Font f1 = new Font("Arial", Font.BOLD,37);
                if(arr[i][j]!=0)img.setText(String.valueOf(arr[i][j]));
                
                img.setFont(f1);
                img.setForeground(Color.white);

                switch(arr[i][j]) {
                    case 0:
                    img.setBackground(new Color(0xd6f4f4));

                    break;
                    case 2:
                    img.setBackground(new Color(0xa8f2f1));

                    break;
                    
                    case 4:
                    img.setBackground(new Color(0x65daee));
                    break;

                    case 8:
                    img.setBackground(new Color(0x49a5f0));
                    break;

                    case 16:
                    img.setBackground(new Color(0x7ca0f4));
                    break;

                    case 32:
                    img.setBackground(new Color(0x3478d9));
                    break;

                    case 64:
                    img.setBackground(new Color(0x637fc2));
                    break;

                    case 128:
                    img.setBackground(new Color(0x3981bd));
                    break;

                    case 256:
                    img.setBackground(new Color(0x1a5383));
                    break;

                    case 512:
                    img.setBackground(new Color(0x5a63b2));
                    break;

                    case 1024:
                    img.setBackground(new Color(0x3f78ff));
                    break;

                    case 2048:
                    img.setBackground(new Color(0x5840ff));
                    break;

                    case 4096:
                    img.setBackground(new Color(0x801ed2));
                    break;

                    case 8192:
                    img.setBackground(new Color(0xb531ff));
                    break;

                    case 16384:
                    Font f2 = new Font("Arial", Font.BOLD,30);
                    img.setFont(f2);
                    img.setBackground(new Color(0xcc16e3));
                    break;

                    default:
                    
                }
                img.setBounds(5+j*100, 5+i*100, 90, 90);
                p3.add(img);
                
            }
        }//添加数字对应图像到主界面

        

        scLabel.setText(String.valueOf(sc));


        

        if(sc>bsc){
            bsc=sc;
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("bestscore.txt"));
                out.write(String.valueOf(bsc));
                out.close();
                bscLabel.setText(String.valueOf(bsc));
            } catch (IOException e) {
            }
        }//创建文件并记录最高分

        


	} 
    

    void reset(){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                arr[i][j]=0;
            }
        }
    addcude();
    addcude();
    copy(arr,undoarr);
    }//重置数组


    void addcude(){
        int[] x=new int[16];
        int[] y=new int[16];
        int n=0;//记录空的格子数；

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(arr[i][j]==0){
                    x[n]=i;
                    y[n]=j;
                    n++;
                }
            }
        }
        if(n==0)return;
        double rand=Math.floor(Math.random()*n);//[0,n-1]
        if(Math.random()*5<4)arr[x[(int) rand]][y[(int)rand]]=2;//4/5概率随机生成2
        else arr[x[(int) rand]][y[(int)rand]]=4;//1/5概率随机生成4

    }//随机添加新数字

    class movecontrol implements KeyListener{//添加键盘监听上下左右移动

        @Override
		public void keyTyped(KeyEvent e) {
				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
                check();
                copy(arr, undoarr);//备份用来撤销的数组
                int[][] temp1=new int[4][4];
                copy(arr,temp1);
				MOVE_UP(1);
                if(!cmp(arr,temp1)){
                    addcude();
                }//比较移动前后数组的变化，若有变化则添加新块
                cude();
                p3.repaint();

				break;
			case KeyEvent.VK_DOWN:
                check();
                copy(arr, undoarr);
                int[][] temp2=new int[4][4];
                copy(arr,temp2);
				MOVE_DOWN(1);
                if(!cmp(arr,temp2)){
                    addcude();
                }
                cude();
                p3.repaint();

				break;
			case KeyEvent.VK_LEFT:
                check();
                copy(arr, undoarr);
                int[][] temp3=new int[4][4];
                copy(arr,temp3);
				MOVE_LEFT(1);
                if(!cmp(arr,temp3)){
                    addcude();
                }
                cude();
                p3.repaint();

				break;
			case KeyEvent.VK_RIGHT:
                check();
                copy(arr, undoarr);
                int[][] temp4=new int[4][4];
                copy(arr,temp4);
				MOVE_RIGHT(1);
                if(!cmp(arr,temp4)){
                    addcude();
                }
                cude();
                p3.repaint();

				break;								
			default:
				break;
			}						
		}		

		@Override
		public void keyReleased(KeyEvent e) {
									
		}
    }

    void check(){
        if(!checkLeft() && !checkRight() && !checkUp() && !checkDown()){
            loseflag=1;
            cude();
            p3.repaint();
        }
    }//检查四个方向能否移动

    public boolean checkLeft() {//判断是否可以向左移动
        int[][] temp=new int[4][4];
        boolean flag=true;
        copy(arr,temp); 
        MOVE_LEFT(0);//向左移动，不计入分数
        flag=!cmp(arr,temp);
        System.out.println("向左"+flag);
        copy(temp,arr);//还原数组
        return flag;
    }
    public boolean checkRight() {//判断是否可以向左移动
        int[][] temp=new int[4][4];
        boolean flag=true;
        copy(arr,temp); 
        MOVE_RIGHT(0);
        flag=!cmp(arr,temp);
        System.out.println("向右"+flag);
        copy(temp,arr);
        return flag;
    }
    public boolean checkUp() {//判断是否可以向左移动
        int[][] temp=new int[4][4];
        boolean flag=true;
        copy(arr,temp); 
        MOVE_UP(0);
        flag=!cmp(arr,temp);
        System.out.println("向上"+flag);
        copy(temp,arr);
        return flag;
    }
    public boolean checkDown() {//判断是否可以向左移动
        int[][] temp=new int[4][4];
        boolean flag=true;
        copy(arr,temp); 
        MOVE_DOWN(0);
        flag=!cmp(arr,temp);
        System.out.println("向下"+flag);
        copy(temp,arr);
        return flag;
    }


    void MOVE_LEFT(int a){
        int temp=0;
        for(int i=0;i<arr.length;i++) {
            int[] newarr =new int[4];	//创建一个新的一维数组，
            int index=0;				//在该数组的前面存放第i行的非0元素，0元素放到后面(系统默认值为0，所以不用管)
            for(int x=0;x<arr[i].length;x++) {
                if(arr[i][x]!=0) {//不等于0的元素就先放进去
                    newarr[index]=arr[i][x];
                        index++;
                }
            }
            arr[i]=newarr;//重新给arr[i]行				
            //合并元素
            for(int x=0;x<3;x++) {
                if(arr[i][x]==arr[i][x+1]) {//若该元素与后面的元素相同则合并
                    arr[i][x]*=2;
                    if(a==1){
                        sc+=arr[i][x];
                        temp+=arr[i][x];
                    }
                    
                    //后面的元素向前移动
                    for(int j=x+1;j<3;j++) {
                        arr[i][j]=arr[i][j+1];
                    }
                    arr[i][3]=0;//最后再补0
                }
            }		
            deltasc=temp;
        }
        cude();
        p3.repaint();
    }

    void MOVE_RIGHT(int a){
        int temp=0;
        for(int i=0;i<arr.length;i++) {
            int[] newarr =new int[4];	
            int index=3;
            for(int x=3;x>=0;x--) {
                if(arr[i][x]!=0) {
                    newarr[index]=arr[i][x];
                    index--;
                }
            }
            arr[i]=newarr;	
            for(int x=3;x>0;x--) {
                if(arr[i][x]==arr[i][x-1]) {
                    arr[i][x]*=2;
                    if(a==1){
                        sc+=arr[i][x];
                        temp+=arr[i][x];
                    }
                    
                    for(int j=x-1;j>0;j--) {
                        arr[i][j]=arr[i][j-1];
                    }
                    arr[i][0]=0;
                }
            }	
            deltasc=temp;							
        }
        cude();
        p3.repaint();
    }

    void MOVE_UP(int a){
        int temp=0;
        for(int j=0;j<4;j++) {
            int[] newarr =new int[4];
            int index=0;			
            for(int x=0;x<4;x++) {
                if(arr[x][j]!=0) {
                    newarr[index]=arr[x][j];
                    index++;
                }
            }	
            for(int n=0;n<4;n++) {
                arr[n][j]=newarr[n];
            }
            for(int x=0;x<3;x++) {
                if(arr[x][j]==arr[x+1][j]) {
                    arr[x][j]*=2;
                    if(a==1){
                        sc+=arr[x][j];
                        temp+=arr[x][j];
                    }
                    
                    for(int i=x+1;i<3;i++) {
                        arr[i][j]=arr[i+1][j];
                    }
                    arr[3][j]=0;
                }
            }	
            deltasc=temp;							
        }
        cude();
        p3.repaint();
    }

    void MOVE_DOWN(int a){
        int temp=0;
        for(int j=0;j<4;j++) {
            int[] newarr =new int[4];	
            int index=3;				
            for(int x=3;x>=0;x--) {
                if(arr[x][j]!=0) {
                    newarr[index]=arr[x][j];
                    index--;
                }
            }
            for(int n=3;n>=0;n--) {
                arr[n][j]=newarr[n];
            }
            for(int x=3;x>0;x--) {
                if(arr[x][j]==arr[x-1][j]) {
                    arr[x][j]*=2;
                    if(a==1){
                        sc+=arr[x][j];
                        temp+=arr[x][j];
                    }
                    
                    for(int i=x-1;i>0;i--) {
                        arr[i][j]=arr[i-1][j];
                    }
                    arr[0][j]=0;
                }
            }	
            deltasc=temp;							
        }
        cude();
        p3.repaint();
    }



    void copy(int[][] a,int[][] b){
        for(int i=0;i<a.length;i++){
            for(int j=0;j<a[i].length;j++){
                b[i][j]=a[i][j];
            }
        }
    }//复制数组


    boolean cmp(int[][] a,int[][] b){
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                if(a[j][i]!=b[j][i]){
                    return false;
                }
            }
        }
        return true;
    }//比较两个数组是否相同

    JPanel shadow1(){
        JPanel jp1=new JPanel();
        jp1.setBounds(30,157,400,400);
        jp1.setBackground(new Color(23,23,23,50));
        return jp1;
    }//主界面阴影

    JPanel shadow2(){
        JPanel jp1=new JPanel();
        jp1.setBounds(204,84,140,50);
        jp1.setBackground(new Color(23,23,23,50));
        return jp1;
    }//重新开始按钮阴影

    JPanel shadow3(){
        JPanel jp1=new JPanel();
        jp1.setBounds(354,84,70,50);
        jp1.setBackground(new Color(23,23,23,50));
        return jp1;
    }//撤销按钮阴影

}


