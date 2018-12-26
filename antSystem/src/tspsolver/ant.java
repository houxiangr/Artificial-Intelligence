package tspsolver;

import java.util.Random;

/**
 *蚂蚁类
 *
 */
public class ant {
    /**
     * 蚂蚁获得的路径
     */
    public int[] anstour;
    //unvisitedcity 取值是0或1，
    //1表示没有访问过，0表示访问过
    int[] unvisitedcity;
    /**
     * 蚂蚁从起点到终点获得的路径长度
     */
    public int tourlength;
    int citys;
    /**
     * 随机分配蚂蚁到某个城市中
     * 同时完成蚂蚁包含字段的初始化工作
     * @param begin 开始城市
     */
    public void RandomSelectCity(int citycount,int begin){
        citys=citycount;
        unvisitedcity=new int[citycount];
        anstour=new int[citycount+1];
        tourlength=0;
        for(int i=0;i<citycount;i++){
            anstour[i]=-1;
            unvisitedcity[i]=1;
        }
        int firstcity=begin;
        unvisitedcity[firstcity]=0;
        anstour[0]=firstcity;
    }

    /**
     * 选择下一个城市
     * @param index 需要选择第index个城市了
     * @param tao   全局的信息素信息
     * @param distance  全局的距离矩阵信息
     */
     public boolean SelectNextCity(int index, double[][] tao, int[][] distance, int end){
        double[] p;
        p=new double[citys];
        double alpha=1.0;
        double beta=2.0;
        double sum=0;
        int currentcity=anstour[index-1];
        //计算公式中的分母部分
        for(int i=0;i<citys;i++){
            if(unvisitedcity[i]==1)
                sum+=(Math.pow(tao[currentcity][i], alpha)*
                        Math.pow(1.0/distance[currentcity][i], beta));
        }
        //计算每个城市被选中的概率
        for(int i=0;i<citys;i++){
            if(unvisitedcity[i]==0)
                p[i]=0.0;
            else{
                p[i]=(Math.pow(tao[currentcity][i], alpha)*
                        Math.pow(1.0/distance[currentcity][i], beta))/sum;
            }
        }
         //计算随机选择城市的范围
         int randomcitycount = 0;
         int[] randomcitys = new int[citys];
         for (int i = 0; i < citys; i++) {
             if (unvisitedcity[i] != 0) {
                 randomcitys[randomcitycount] = i;
                 randomcitycount++;
             }
         }
        //计算随机选择城市的概率
        long r = System.currentTimeMillis();
        Random ran=new Random(r);
        int selectcity=-1;
        if(ran.nextDouble() >0.6){
            selectcity = randomcitys[ran.nextInt(randomcitycount)];
        }else{
            long r1 = System.currentTimeMillis();
            Random rnd=new Random(r1);
            double selectp=rnd.nextDouble();
            //轮盘赌选择一个城市；
            double sumselect=0;
            for(int i=0;i<citys;i++){
                sumselect+=p[i];
                if(sumselect>=selectp){
                    selectcity=i;
                    break;
                }
            }
        }
         if (selectcity==-1)
             System.out.println();
         anstour[index]=selectcity;
         unvisitedcity[selectcity]=0;
         return selectcity == end;
    }

    /**
     * 计算蚂蚁获得的路径的长度
     * @param distance  全局的距离矩阵信息
     */
    public void CalTourLength(int[][] distance){
        tourlength=0;
        for(int i=0;i<citys&&anstour[i+1]!=-1;i++){
            tourlength+=distance[anstour[i]][anstour[i+1]];
        }
    }
}
