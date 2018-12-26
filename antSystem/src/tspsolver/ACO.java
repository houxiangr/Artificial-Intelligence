package tspsolver;
import java.io.*;
/**
 *蚁群优化算法，用来求解TSP问题
 * @author FashionXu
 */
public class ACO {
    //定义蚂蚁群
    ant[] ants;
    int antcount;//蚂蚁的数量
    int [][]distance;//表示城市间距离
    double [][]tao;//信息素矩阵
    int citycount;//城市数量
    int[]besttour;//求解的最佳路径
    int bestlength;//求的最优解的长度
    int begin,end;
    /** 初始化函数
     *@param filename tsp数据文件
     *@param antnum 系统用到蚂蚁的数量
     *@throws FileNotFoundException 如果文件不存在则抛出异常
     */
    public void init(String filename,int antnum,int begin,int end) throws FileNotFoundException, IOException{
        this.begin = begin;
        this.end = end;
        antcount=antnum;
        ants=new ant[antcount];
        //读取数据
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader tspdata = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        strbuff = tspdata.readLine();
        citycount = Integer.valueOf(strbuff);
        distance = new int[citycount][citycount];
        x = new int[citycount];
        y = new int[citycount];
        for (int citys = 0; citys < citycount; citys++) {
            strbuff = tspdata.readLine();
            String[] strcol = strbuff.split(" ");
            x[citys] = Integer.valueOf(strcol[1]);
            y[citys] = Integer.valueOf(strcol[2]);
        }
        //计算距离矩阵
        for (int city1 = 0; city1 < citycount - 1; city1++) {
            distance[city1][city1] = 0;
            for (int city2 = city1 + 1; city2 < citycount; city2++) {
                distance[city1][city2] = (int) (Math.sqrt((x[city1] - x[city2]) * (x[city1] - x[city2])
                        + (y[city1] - y[city2]) * (y[city1] - y[city2])) + 0.5);
                distance[city2][city1] = distance[city1][city2];
            }
        }
        distance[citycount - 1][citycount - 1] = 0;
        //初始化信息素矩阵
        tao=new double[citycount][citycount];
        for(int i=0;i<citycount;i++)
        {
            for(int j=0;j<citycount;j++){
                tao[i][j]=0.1;
            }
        }
        bestlength=Integer.MAX_VALUE;
        besttour=new int[citycount+1];
        //将蚂蚁放到开始位置
        for(int i=0;i<antcount;i++){
            ants[i]=new ant();
            ants[i].RandomSelectCity(citycount,begin);
        }
    }

    /**
     * ACO的运行过程
     * @param maxgen 最多循环次数
     *
     */
    public void run(int maxgen){
        for(int runtimes=0;runtimes<maxgen;runtimes++){
            //每一只蚂蚁移动的过程
            for(int i=0;i<antcount;i++){
                for(int j=1;j<citycount;j++){
                    if(ants[i].SelectNextCity(j,tao,distance,end)){
                        break;
                    }
                }
                //计算蚂蚁获得的路径长度
                ants[i].CalTourLength(distance);
                if(ants[i].tourlength<bestlength){
                    //保留最优路径
                    bestlength=ants[i].tourlength;
                    System.out.println("第"+runtimes+"代，发现新的解"+bestlength);
                    if (citycount + 1 >= 0) System.arraycopy(ants[i].anstour, 0, besttour, 0, citycount + 1);
                }
            }
            //更新信息素矩阵
            UpdateTao();
            //重新将蚂蚁放到开始位置蚂蚁
            for(int i=0;i<antcount;i++){
                ants[i].RandomSelectCity(citycount,begin);
            }
        }
    }
    /**
     * 更新信息素矩阵
     */
    private void UpdateTao(){
        double rou=0.5;
        //信息素挥发
        for(int i=0;i<citycount;i++)
            for(int j=0;j<citycount;j++)
                tao[i][j]=tao[i][j]*(1-rou);
        //信息素更新
        for(int i=0;i<antcount;i++){
            for(int j=0;j<citycount && ants[i].anstour[j+1] != -1;j++){
                tao[ants[i].anstour[j]][ants[i].anstour[j+1]]+=1.0/ants[i].tourlength;
            }
        }
    }
    /**
     * 输出程序运行结果
     */
    public void ReportResult(){
        System.out.println("最优路径长度是"+bestlength);
    }
}
