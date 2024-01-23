import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        /*
        * 测试数据集：西瓜数据集
        * 特征：
        * 1.色泽： 0：青绿 1：乌黑
        * 2.根蒂： 0：蜷缩 1：硬挺 2:稍蜷
        * 3.敲声： 0：浊响 1：清脆 2：沉闷
        * 标签： 1：好瓜 0：坏瓜
        *
        */
        int[][] list1 = {{0,0,0},{1,0,0},{0,1,1},{1,2,2}};
        int[] list2 = {1,1,0,0};
        DecisionTree decisionTree = new DecisionTree();
        decisionTree.fit(list1,list2);
        int[][] l3 = {{0,1,1},{1,0,1}};
        decisionTree.predict(l3);
    }
}