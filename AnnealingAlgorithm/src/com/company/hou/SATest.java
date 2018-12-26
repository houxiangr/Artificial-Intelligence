package com.company.hou;

public class SATest {
    private static double T = 1000;// 初始化温度
    private static final double Tmin = 1;// 温度的下界
    private static final int k = 100;// 迭代的次数
    private static final double delta = 0.98;// 温度的下降率​
    private static double getX() {
        return Math.random() * 100;
    }

    /**
     * 评价函数的值,即对应上文中的f(x)
     *
     * @param x 目标函数中的一个参数
     * @param y 目标函数中的另一个参数
     * @return函数值
     */
    private static double getFuncResult(double x, double y) {
        return 6 * Math.pow(x, 7) + 8 * Math.pow(x, 6) + 7 * Math.pow(x, 3) + 5 * Math.pow(x, 2) - x * y;
    }
    /**
     * 模拟退火算法的过程
     * @param y 目标函数中的指定的参数
     * @return最优解
     */
    private static double getSA(double y) {
        double result = Double.MAX_VALUE;// 初始化最终的结果
        double x[] = new double[k];
        // 初始化初始解
        for (int i = 0; i < k; i++) {
            x[i] = getX();
        }
        // 迭代的过程
        while (T > Tmin) {
            for (int i = 0; i < k; i++) {
                // 计算此时的函数结果
                double funTmp = getFuncResult(x[i], y);
                // 在邻域内产生新的解
                double x_new = x[i] + (Math.random() * 2 - 1);
                // 判断新的x不能超出界
                if (x_new >= 0 && x_new <= 100) {
                    double funTmp_new = getFuncResult(x_new, y);
                    if (funTmp_new - funTmp < 0) {
                        // 替换
                        x[i] = x_new;
                    } else {
                        // 以概率替换
                        double p = 1 / (1 + Math
                                .exp(-(funTmp_new - funTmp) / T));
                        if (Math.random() < p) {
                            x[i] = x_new;
                        }
                    }
                }
            }
            T = T * delta;
        }
        for (int i = 0; i < k; i++) {
            result = Math.min(result, getFuncResult(x[i], y));
        }
        return result;
    }


    public static void main(String args[]) {
        // 设置y的值
        int y = 10;
        System.out.println("最优解为：" + getSA(y));
    }
}